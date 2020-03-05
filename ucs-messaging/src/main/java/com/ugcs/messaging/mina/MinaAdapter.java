package com.ugcs.messaging.mina;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ugcs.messaging.api.MessageSession;
import com.ugcs.messaging.api.MessageSessionErrorEvent;
import com.ugcs.messaging.api.MessageSessionEvent;
import com.ugcs.messaging.api.MessageSessionListener;

class MinaAdapter extends IoHandlerAdapter {

	private static final Logger log = LoggerFactory.getLogger(MinaAdapter.class);
	private final List<MessageSessionListener> sessionListeners = new CopyOnWriteArrayList<>();

	/* listeners */

	public void addSessionListener(MessageSessionListener sessionListener) {
		Objects.requireNonNull(sessionListener);

		sessionListeners.add(sessionListener);
	}

	public void removeSessionListener(MessageSessionListener sessionListener) {
		Objects.requireNonNull(sessionListener);

		sessionListeners.remove(sessionListener);
	}

	/* message session */

	private MinaMessageSession createMessageSession(IoSession session) {
		Objects.requireNonNull(session);

		MinaMessageSession messageSession = null;
		synchronized (session) {
			messageSession = (MinaMessageSession)session.getAttribute("messageSession");
			if (messageSession == null) {
				messageSession = new MinaMessageSession(session);
				session.setAttribute("messageSession", messageSession);
			}
			MessageSessionEvent sessionEvent = new MessageSessionEvent(this, messageSession);
			for (MessageSessionListener listener : sessionListeners)
				listener.sessionOpened(sessionEvent);
		}
		log.info("Session {} opened {local: {}, remote: {}}",
				session.getId(),
				session.getLocalAddress(),
				session.getRemoteAddress());
		return messageSession;
	}

	public MinaMessageSession getMessageSession(IoSession session) {
		Objects.requireNonNull(session);

		synchronized (session) {
			return (MinaMessageSession)session.getAttribute("messageSession");
		}
	}

	private void closeMessageSession(IoSession session) {
		Objects.requireNonNull(session);

		boolean closePending = false;
		synchronized (session) {
			closePending = (boolean)session.getAttribute("closePending", false);
			if (!closePending)
				session.setAttribute("closePending", true);
		}
		if (!closePending) {
			log.info("Session {} closed {local: {}, remote: {}}",
					session.getId(),
					session.getLocalAddress(),
					session.getRemoteAddress());

			MinaMessageSession messageSession = getMessageSession(session);
			// notify session to interrupt pending listeners
			messageSession.cancelAllListeners();

			MessageSessionEvent sessionEvent = new MessageSessionEvent(this, messageSession);
			for (MessageSessionListener listener : sessionListeners)
				listener.sessionClosed(sessionEvent);
		}
	}

	/* Mina handlers */

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		Objects.requireNonNull(session);

		MinaMessageSession messageSession = getMessageSession(session);
		messageSession.messageReceived(message);
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		Objects.requireNonNull(session);

		createMessageSession(session);
	}

	@Override
	public void sessionOpened(IoSession session) throws Exception {
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		Objects.requireNonNull(session);

		MessageSession messageSession = getMessageSession(session);
		MessageSessionEvent sessionEvent = new MessageSessionEvent(this, messageSession);
		for (MessageSessionListener listener : sessionListeners)
			listener.sessionIdle(sessionEvent);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		Objects.requireNonNull(session);

		closeMessageSession(session);
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		Objects.requireNonNull(session);

		log.error("Session {} error: {}", session, cause);

		MessageSession messageSession = getMessageSession(session);
		MessageSessionEvent sessionEvent = new MessageSessionErrorEvent(
				this, messageSession, cause);
		for (MessageSessionListener listener : sessionListeners)
			listener.sessionError(sessionEvent);

		closeMessageSession(session);
	}
}
