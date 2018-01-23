package com.ugcs.messaging.api;

/**
 * There is no specific workflow of invoking handlers by the parties.
 * Any handler can be invoked after session passivation.
 */
public interface MessageHandler {

	/**
	 * <p>Handles message within a session context: given a request message
	 * produces a response message, if possible.</p>
	 * <p>Message handler is not required to be stateless and can involve side effects.</p>
	 * <p>Message handler should not send constructed response within the current session.
	 * It is assumed to be the caller responsibility. But handler can interact with
	 * the session party: send and listen for messages.</p>
	 *
	 * @param messageSession session, where the message was received
	 * @param message        message to process, can be {@code null}
	 * @return
	 * @throws IllegalArgumentException request message is of invalid type
	 *                                  or cannot be processed
	 * @throws Exception
	 */
	Object process(MessageSession messageSession, Object message) throws Exception;
}
