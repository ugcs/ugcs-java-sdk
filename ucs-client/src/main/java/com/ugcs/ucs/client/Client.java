package com.ugcs.ucs.client;

import java.io.Closeable;
import java.io.IOException;
import java.net.SocketAddress;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import com.google.protobuf.Message;
import com.ugcs.messaging.api.Connector;
import com.ugcs.messaging.api.MessageEvent;
import com.ugcs.messaging.api.MessageExecutor;
import com.ugcs.messaging.api.MessageListenerAdapter;
import com.ugcs.messaging.api.MessageSelector;
import com.ugcs.messaging.api.MessageSession;
import com.ugcs.messaging.mina.MinaConnector;
import com.ugcs.ucs.proto.MessagesProto;
import com.ugcs.ucs.proto.MessagesProto.Error;
import com.ugcs.ucs.proto.MessagesProto.Notification;
import com.ugcs.ucs.proto.codec.MessageWrapper;
import com.ugcs.ucs.proto.codec.MessageWrapperCodecFactory;
import com.ugcs.ucs.proto.mapping.HciMessageMapping;

public class Client implements Closeable {
	private final Connector connector;
	private final SocketAddress serverAddress;
	private MessageSession session;
	private MessageExecutor executor;
	private int messageInstanceId = 0;
	private final List<ServerNotificationListener> notificationListeners = 
			new CopyOnWriteArrayList<>();
	
	private static final long DEFAULT_REQUEST_TIMEOUT = 60_000L;
	
	public Client(SocketAddress serverAddress) {
		if (serverAddress == null)
			throw new IllegalArgumentException("serverAddress");
		
		this.serverAddress = serverAddress;
		this.connector = new MinaConnector(
				new MessageWrapperCodecFactory(new HciMessageMapping()), 
				1, 
				1);
	}
	
	public void addNotificationListener(ServerNotificationListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listener");
		
		notificationListeners.add(listener);
	}
	
	public void removeNotificationListener(ServerNotificationListener listener) {
		if (listener == null)
			throw new IllegalArgumentException("listener");
		
		notificationListeners.remove(listener);
	}
	
	public boolean isConnected() {
		return session != null && session.isOpened();
	}
	
	public void connect() throws IOException {
		if (session != null) {
			session.close();
			session = null;
		}
		try {
			session = connector.connect(serverAddress);
			session.addListener(
					new NotificationDispatcher(), 
					new NotificationSelector());
			
			executor = new MessageExecutor(session);
		} catch (Exception e) {
			if (session != null)
				session.close();
			throw new IOException("Connection error", e);
		}
	}
	
	@Override
	public void close() throws IOException {
		if (session != null)
			session.close();
		if (connector != null)
			connector.close();
	}
	
	@SuppressWarnings("unchecked")
	public <T> T execute(Message message, long timeoutMillis) throws Exception {
		MessageWrapper request = wrap(message);
		MessageSelector selector = new ResponseSelector(request);
		Message response = ((MessageWrapper) executor.submit(request, selector)
				.get(timeoutMillis, TimeUnit.MILLISECONDS))
				.getMessage();
		if (response instanceof MessagesProto.Error) {
			Error errorResponse = (MessagesProto.Error) response;
			throw new Exception(errorResponse.getErrorMessage());
		}
		return (T) response;
	}
	
	public <T> T execute(Message message) throws Exception {
		return execute(message, DEFAULT_REQUEST_TIMEOUT);
	}
	
	private MessageWrapper wrap(Message message) {
		MessageWrapper messageWrapper = new MessageWrapper(message, messageInstanceId++);
		return messageWrapper;
	}
	
	class NotificationDispatcher extends MessageListenerAdapter {

		@Override
		public void messageReceived(MessageEvent messageEvent) {
			if (!(messageEvent.getMessage() instanceof MessageWrapper))
				return;
			MessageWrapper wrapper = (MessageWrapper) messageEvent.getMessage();
			Object message = wrapper.getMessage();
			if (message instanceof Notification) {
				Notification notification = (Notification) message;
				ServerNotification serverNotification = new ServerNotification(
						Client.this, 
						notification.getEvent(), 
						notification.getSubscriptionId());
				for (ServerNotificationListener listener : notificationListeners)
					listener.notificationReceived(serverNotification);
			}
		}
	}
	
	static class NotificationSelector implements MessageSelector {

		@Override
		public boolean select(Object message) {
			if (message == null)
				return false;
			if (!(message instanceof MessageWrapper))
				return false;
			
			MessageWrapper wrapper = (MessageWrapper) message;
			return wrapper != null && wrapper.getInstanceId() == -1;
		}
	}
	
	static class ResponseSelector implements MessageSelector {
		private int instanceId;
		
		public ResponseSelector(MessageWrapper request) {
			if (request == null)
				throw new IllegalArgumentException("request");
			
			instanceId = request.getInstanceId();
		}
		
		@Override
		public boolean select(Object message) {
			if (message == null)
				return false;
			if (!(message instanceof MessageWrapper))
				return false;
			
			MessageWrapper wrapper = (MessageWrapper) message;
			return wrapper != null && instanceId == wrapper.getInstanceId();
		}
	}
}