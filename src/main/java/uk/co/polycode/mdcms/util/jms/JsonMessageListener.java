package uk.co.polycode.mdcms.util.jms;

import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * Convert text messages to JSON messages
 */
public abstract class JsonMessageListener implements MessageListener {

	public void onMessage(Message message) {
		// Process the message if it is a text message
		if (message instanceof TextMessage) {
			this.onTextMessage((TextMessage) message);
		}else {
			throw new InvalidMessageException("Message type is not one this listener can process [TextMessage] it is: "
					+ message.getClass().getSimpleName());
		}
	}

	public void onTextMessage(TextMessage textMessage) {
		this.onJsonMessage(textMessage);
	}

	public abstract void onJsonMessage(TextMessage textMessage);

}