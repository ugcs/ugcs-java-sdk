package com.ugcs.messaging.mina;

import java.util.Objects;

import org.apache.mina.core.session.IoEvent;
import org.apache.mina.core.session.IoEventType;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.WriteRequest;

import com.ugcs.messaging.api.MessageMapper;
import com.ugcs.messaging.api.TaskMapper;

public class MinaTaskMappers {
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

	static class SessionMapper implements TaskMapper {
		private final MessageMapper messageMapper;

		public SessionMapper(MessageMapper messageMapper) {
			this.messageMapper = messageMapper;
		}

		@Override
		public Object map(Runnable runnable) {
			Objects.requireNonNull(runnable);

			if (!(runnable instanceof IoEvent))
				throw new IllegalArgumentException();

			IoEvent event = (IoEvent) runnable;
			TaskChannel channel = new TaskChannel();

			// sessionId
			IoSession session = event.getSession();
			if (session != null)
				channel.sessionId = session.getId();

			// channelType
			channel.type = TaskChannelType.of(event.getType());

			// isolation
			if (messageMapper != null) {
				Object parameter = event.getParameter();
				if (parameter != null) {
					Object message = null;
					if (event.getParameter() instanceof WriteRequest) {
						WriteRequest writeRequest = (WriteRequest) event.getParameter();
						message = writeRequest.getMessage();
					} else {
						message = event.getParameter();
					}
					if (message != null)
						channel.isolation = messageMapper.map(message);
				}
			}
			return channel;
		}
	}

	enum TaskChannelType {
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

	static class TaskChannel {
		private long sessionId;
		private TaskChannelType type;
		private Object isolation;

		@Override
		public int hashCode() {
			int h = 1;
			h = 31 * h + (int) sessionId;
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

			TaskChannel channel = (TaskChannel) other;
			return sessionId == channel.sessionId
					&& type == channel.type
					&& isolation == null
					? channel.isolation == null
					: isolation.equals(channel.isolation);
		}
	}

	/* message mappers */

	static class TypeMapper implements MessageMapper {

		@Override
		public Object map(Object message) {
			if (message == null)
				return null;
			return message.getClass();
		}
	}

	static class SelfMapper implements MessageMapper {

		@Override
		public Object map(Object message) {
			if (message == null)
				return null;
			return message;
		}
	}
}
