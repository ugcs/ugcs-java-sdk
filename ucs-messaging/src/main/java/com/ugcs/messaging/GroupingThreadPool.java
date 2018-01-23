package com.ugcs.messaging;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.AbstractExecutorService;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GroupingThreadPool extends AbstractExecutorService {

	private static final Logger log = LoggerFactory.getLogger(GroupingThreadPool.class);

	/* properties */

	private final int coreWorkers;
	private final int maxWorkers;
	private final TaskMapper taskMapper;
	private final ThreadFactory threadFactory;

	/* state */

	private volatile boolean shutdown = false;

	// all active queues: both assigned to workers and waiting
	private final Map<Object, TaskQueue> queues = new HashMap<>();

	// how to compare two queues to peek most "urgent"
	private final TaskQueueComparator queueComparator = new TaskQueueComparator();

	// queues that are not currently assigned to any worker
	// invariant: cannot contain an empty queue
	// TODO remove initial capacity when switched to Java 8
	private final Queue<TaskQueue> waiting = new PriorityQueue<>(11, queueComparator);

	// set of the workers (processing threads)
	private final Set<Worker> workers = new HashSet<>();

	/* locks & monitors */

	// queues lock:
	// a new task is added to any queue (either waiting or assigned to worker);
	// a queue is added to or removed from the waiting pool;
	// a new queue created;
	private final ReentrantLock ql = new ReentrantLock();

	// signals that a queue in a waiting pool become non-empty
	private final Condition taskWaiting = ql.newCondition();

	// workers lock:
	// any changes in a workers set;
	private final Lock wl = new ReentrantLock();

	// workers set became empty when executor is in the shutdown state
	private final Condition terminated = wl.newCondition();

	public GroupingThreadPool(int coreWorkers, int maxWorkers, TaskMapper taskMapper) {
		this(coreWorkers, maxWorkers, taskMapper, Executors.defaultThreadFactory());
	}

	public GroupingThreadPool(int coreWorkers, int maxWorkers, TaskMapper taskMapper, ThreadFactory threadFactory) {
		if (coreWorkers < 0
				|| maxWorkers < 1
				|| coreWorkers > maxWorkers)
			throw new IllegalArgumentException();
		Objects.requireNonNull(taskMapper);
		Objects.requireNonNull(threadFactory);

		this.coreWorkers = coreWorkers;
		this.maxWorkers = maxWorkers;
		this.threadFactory = threadFactory;
		this.taskMapper = taskMapper;
	}

	@Override
	public void execute(Runnable runnable) {
		Objects.requireNonNull(runnable);

		if (shutdown)
			rejectTask(runnable);

		Object isolation = taskMapper.map(runnable);
		if (isolation == null)
			isolation = runnable;
		queueTask(runnable, isolation);
	}

	private void queueTask(Runnable runnable, Object isolation) {
		Objects.requireNonNull(runnable);
		Objects.requireNonNull(isolation);

		Task task = new Task(runnable, System.nanoTime());
		ql.lock();
		try {
			// target queue for the task
			TaskQueue queue = queues.get(isolation);
			if (queue == null) {
				// new queue
				queue = new TaskQueue(isolation);
				queue.tasks.offer(task);

				queues.put(isolation, queue);
				waiting.offer(queue);

				// signal: can spawn worker
				signalWaitingTask(true);
			} else {
				// target queue exists:
				// if queue resides in a waiting pool,
				// it should be non-empty, thus there is no need
				// to awake any waiting worker; adding a new task
				// to the queue does not affect waiting priority, so
				// there is also no need to re-offer it to the priority
				// queue;
				// if queue is bound to worker (possibly empty),
				// there is no need to signal as it is already
				// being processed
				queue.tasks.offer(task);
			}
		} finally {
			ql.unlock();
		}
	}

	private void rejectTask(Runnable runnable) {
		throw new RejectedExecutionException("Task "
				+ runnable.toString()
				+ " rejected from "
				+ this.toString());
	}

	private void signalWaitingTask(boolean canSpawnWorker) {
		boolean spawnWorker = false;
		ql.lock();
		try {
			// TODO inaccurate waiters check
			if (ql.hasWaiters(taskWaiting)) {
				// somebody is waiting for a task
				taskWaiting.signal();
			} else {
				spawnWorker = true;
			}
		} finally {
			ql.unlock();
		}
		if (spawnWorker)
			spawnWorker();
	}

	private boolean addWorker(Worker worker) {
		if (worker == null)
			return false;

		wl.lock();
		try {
			return workers.add(worker);
		} finally {
			wl.unlock();
		}
	}

	private boolean removeWorker(Worker worker) {
		if (worker == null)
			return false;

		wl.lock();
		try {
			boolean removed = workers.remove(worker);
			if (removed && isTerminated())
				terminated.signalAll();
			return removed;
		} finally {
			wl.unlock();
		}
	}

	private boolean spawnWorker() {
		return spawnWorker(false);
	}

	private boolean spawnWorker(boolean ignoreShutdown) {
		if (!ignoreShutdown && shutdown)
			return false;

		Worker worker = null;
		wl.lock();
		try {
			if (workers.size() >= maxWorkers)
				return false;
			worker = new Worker();
			addWorker(worker);
		} finally {
			wl.unlock();
		}

		boolean started = false;
		try {
			worker.thread.start();
			started = true;
		} finally {
			if (!started)
				removeWorker(worker);
		}
		return started;
	}

	private boolean respawnCoreWorker() {
		wl.lock();
		try {
			if (workers.size() >= coreWorkers)
				return false;
			return spawnWorker(true);
		} finally {
			wl.unlock();
		}
	}

	private boolean dismissWorker(Worker worker) {
		if (worker == null)
			return false;

		wl.lock();
		try {
			if (!shutdown && workers.size() <= coreWorkers)
				return false;

			removeWorker(worker);
			return true;
		} finally {
			wl.unlock();
		}
	}

	@Override
	public void shutdown() {
		shutdown = true;
		ql.lock();
		try {
			taskWaiting.signalAll();
		} finally {
			ql.unlock();
		}
	}

	@Override
	public List<Runnable> shutdownNow() {
		shutdown = true;

		// interrupt all workers
		wl.lock();
		try {
			for (Worker worker : workers) {
				try {
					worker.interrupt();
				} catch (Throwable ignored) {
					// heavy train
				}
			}
		} finally {
			wl.unlock();
		}

		// collect unreachable tasks
		List<Runnable> tasks = new ArrayList<>();
		ql.lock();
		try {
			Iterator<Map.Entry<Object, TaskQueue>> iterator = queues.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<Object, TaskQueue> entry = iterator.next();
				TaskQueue queue = entry.getValue();
				// drain queue
				Task task;
				while ((task = queue.tasks.poll()) != null) {
					tasks.add(task.runnable);
				}
				// remove queue from everywhere
				waiting.remove(queue);
				iterator.remove();
			}
		} finally {
			ql.unlock();
		}
		return tasks;
	}

	@Override
	public boolean isShutdown() {
		return shutdown;
	}

	@Override
	public boolean isTerminated() {
		if (shutdown) {
			wl.lock();
			try {
				return workers.isEmpty();
			} finally {
				wl.unlock();
			}
		}
		return false;
	}

	@Override
	public boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException {
		long t = System.nanoTime() + unit.toNanos(timeout);
		wl.lock();
		try {
			while (true) {
				if (isTerminated())
					return true;
				long nanosTimeout = t - System.nanoTime();
				if (nanosTimeout <= 0)
					return false;
				terminated.awaitNanos(nanosTimeout);
			}
		} finally {
			wl.unlock();
		}
	}

	@Override
	public String toString() {
		int numQueues = 0;
		int numTasks = 0;
		int numWaitingQueues = 0;
		long maxWaitingNanos = 0L;
		ql.lock();
		try {
			numQueues = queues.size();
			numWaitingQueues = waiting.size();
			for (TaskQueue queue : queues.values()) {
				numTasks += queue.tasks.size();
				Task mostWaiting = queue.tasks.peek();
				if (mostWaiting != null) {
					maxWaitingNanos = Math.max(
							maxWaitingNanos,
							System.nanoTime() - mostWaiting.createdAt);
				}
			}
		} finally {
			ql.unlock();
		}
		int numWorkers = 0;
		wl.lock();
		try {
			numWorkers = workers.size();
		} finally {
			wl.unlock();
		}

		StringBuilder builder = new StringBuilder(super.toString());
		builder.append(" [");
		if (isShutdown()) {
			builder.append(isTerminated() ? "Terminated" : "Shutting down");
		} else {
			builder.append("Running");
		}
		builder
				.append(", workers = ")
				.append(numWorkers)
				.append(", queues = ")
				.append(numQueues);
		if (numWaitingQueues > 0) {
			builder
					.append(" (")
					.append(numWaitingQueues)
					.append(" waiting)");
		}
		builder
				.append(", tasks = ")
				.append(numTasks);
		if (numTasks > 0) {
			builder
					.append(" (max waiting ")
					.append(maxWaitingNanos)
					.append(" ns)");
		}
		builder.append("]");
		return builder.toString();
	}

	/* Worker */

	private class Worker implements Runnable {

		private final Thread thread;

		public Worker() {
			Thread thread = threadFactory.newThread(this);
			Objects.requireNonNull(thread);
			this.thread = thread;
		}

		private void interrupt() {
			if (thread != null && !thread.isInterrupted()) {
				try {
					thread.interrupt();
				} catch (SecurityException ignore) {
					// ignored
				}
			}
		}

		private TaskQueue selectWorkingQueue(TaskQueue queue) {
			// dirty check:
			// waiting.size may be invisible to the current thread,
			// but it is ok, while it has tasks to run
			if (waiting.isEmpty()
					&& queue != null
					&& !queue.tasks.isEmpty())
				return queue;

			// try to acquire task from the most waiting queue,
			// if its priority is higher
			ql.lock();
			try {
				TaskQueue mostWaiting = waiting.poll();
				if (mostWaiting == null)
					return queue;

				if (queue == null) {
					// there is no queue assigned to the worker:
					// switch to the polled waiting queue
					return mostWaiting;
				}
				// queue != null
				if (queueComparator.compare(mostWaiting, queue) < 0) {
					// most waiting queue's priority is higher
					releaseWorkingQueue(queue);
					return mostWaiting;
				} else {
					// most waiting queue can wait little more
					waiting.offer(mostWaiting);
					return queue;
				}
			} finally {
				ql.unlock();
			}
		}

		private TaskQueue releaseWorkingQueue(TaskQueue queue) {
			if (queue == null)
				return null;

			ql.lock();
			try {
				if (queue.tasks.isEmpty()) {
					// delete empty queue
					queues.remove(queue.isolation);
				} else {
					waiting.offer(queue);
					// TODO is there any waiting worker interested?
					signalWaitingTask(true);
				}
			} finally {
				ql.unlock();
			}
			return null;
		}

		@Override
		public void run() {
			log.info("W-{} START", Thread.currentThread().getName());
			TaskQueue queue = null;
			try {
				while (!Thread.interrupted()) {
					queue = selectWorkingQueue(queue);

					Task task = null;
					if (queue != null) {
						task = queue.tasks.poll();
						if (task == null) {
							// lock to ensure absence of the new tasks
							// in the queue
							ql.lock();
							try {
								task = queue.tasks.poll();
								if (task == null)
									queue = releaseWorkingQueue(queue);
							} finally {
								ql.unlock();
							}
						}
					}
					if (task == null) {
						// queue == null
						// wait for a new task in a waiting pool
						boolean timedOut = false;
						ql.lock();
						try {
							try {
								timedOut = !taskWaiting.await(10L, TimeUnit.SECONDS);
							} catch (InterruptedException ignored) {
								// ignored
							}
						} finally {
							ql.unlock();
						}
						if (timedOut && dismissWorker(this))
							break;
					} else {
						task.runnable.run();
					}
				}
			} catch (RuntimeException | Error e) {
				removeWorker(this);
				respawnCoreWorker();
				throw e;
			} catch (Throwable e) {
				removeWorker(this);
				respawnCoreWorker();
				throw new Error(e);
			} finally {
				releaseWorkingQueue(queue);
				removeWorker(this);
				log.info("W-{} SHUTDOWN", Thread.currentThread().getName());
			}
		}
	}

	/* Tasks */

	private static class Task {

		private final Runnable runnable;
		private final long createdAt;

		public Task(Runnable runnable) {
			this(runnable, System.nanoTime());
		}

		public Task(Runnable runnable, long createdAt) {
			Objects.requireNonNull(runnable);

			this.runnable = runnable;
			this.createdAt = createdAt;
		}
	}

	private static class TaskOrder implements Comparator<Task> {

		@Override
		public int compare(Task x, Task y) {
			return Long.compare(
					x != null ? x.createdAt : Long.MAX_VALUE,
					y != null ? y.createdAt : Long.MAX_VALUE);
		}
	}

	private static class TaskQueue {

		private final Queue<Task> tasks;
		private final Object isolation;

		public TaskQueue(Object isolation) {
			this.tasks = new ConcurrentLinkedQueue<>();
			this.isolation = isolation;
		}
	}

	private static class TaskQueueComparator implements Comparator<TaskQueue> {

		private final Comparator<Task> taskComparator = new TaskOrder();

		@Override
		public int compare(TaskQueue x, TaskQueue y) {
			return taskComparator.compare(x.tasks.peek(), y.tasks.peek());
		}
	}
}
