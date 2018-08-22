package com.ugcs.messaging.mina;

import java.util.Objects;

import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.ugcs.messaging.api.MessageMapper;
import com.ugcs.messaging.TaskMapper;

public final class MinaTaskMappers {

	private MinaTaskMappers() {
	}

	public static TaskMapper newMapper(MessageMapper messageMapper) {
		return new SessionMapper(messageMapper);
	}

	public static TaskMapper orderedBySessions() {
		return newMapper(null);
	}

	public static TaskMapper orderedByMessageTypes() {
		return newMapper(new TypeMapper());
	}

	public static TaskMapper unordered() {
		return newMapper(new SelfMapper());
	}

	/* task mappers */

	private static class SessionMapper implements TaskMapper {

		private final MessageMapper messageMapper;

		public SessionMapper(MessageMapper messageMapper) {
			this.messageMapper = messageMapper;
		}

		@Override
		public Object map(Runnable runnable) {
			Objects.requireNonNull(runnable);

			if (!(runnable instanceof IoEvent))
				throw new IllegalArgumentException();

			IoEvent event = (IoEvent)runnable;

			// sessionId
			IoSession session = event.getSession();
			long sessionId = session != null
					? session.getId()
					: 0L;

			// channelType
			TaskChannelType type = TaskChannelType.of(event.getType());

			// isolation
			Object isolation = null;
			if (messageMapper != null) {
				Object parameter = event.getParameter();
				if (parameter != null) {
					Object message = null;
					if (event.getParameter() instanceof WriteRequest) {
						WriteRequest writeRequest = (WriteRequest)event.getParameter();
						message = writeRequest.getMessage();
					} else {
						message = event.getParameter();
					}
					if (message != null)
						isolation = messageMapper.map(message);
				}
			}

			return new TaskChannel(sessionId, type, isolation);
		}
	}

	private enum TaskChannelType {
		IN,
		OUT;

		public static TaskChannelType of(IoEventType eventType) {
			Objects.requireNonNull(eventType);

			switch (eventType) {
				case MESSAGE_SENT:
				case WRITE:
				case CLOSE:
					return TaskChannelType.OUT;
				default:
					return TaskChannelType.IN;
			}
		}
	}

	private static class TaskChannel {

		private final long sessionId;
		private final TaskChannelType type;
		private final Object isolation;

		public TaskChannel(long sessionId, TaskChannelType type, Object isolation) {
			this.sessionId = sessionId;
			this.type = type;
			this.isolation = isolation;
		}

		@Override
		public int hashCode() {
			int h = 1;
			h = 31 * h + (int)sessionId;
			h = 31 * h + (type != null
					? type.hashCode()
					: 0);
			h = 31 * h + (isolation != null
					? isolation.hashCode()
					: 0);
			return h;
		}

		@Override
		public boolean equals(Object other) {
			if (this == other)
				return true;
			if (!(other instanceof TaskChannel))
				return false;

			TaskChannel channel = (TaskChannel)other;
			return sessionId == channel.sessionId
					&& type == channel.type
					&& isolation == null
							? channel.isolation == null
							: isolation.equals(channel.isolation);
		}
	}

	/* message mappers */

	private static class TypeMapper implements MessageMapper {

		@Override
		public Object map(Object message) {
			if (message == null)
				return null;
			return message.getClass();
		}
	}

	private static class SelfMapper implements MessageMapper {

		@Override
		public Object map(Object message) {
			return message;
		}
	}
}
