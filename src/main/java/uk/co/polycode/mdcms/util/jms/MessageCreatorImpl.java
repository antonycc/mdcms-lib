package uk.co.polycode.mdcms.util.jms;

import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;

/**
 * Provide the message used to create this object as the created message
 */
public class MessageCreatorImpl implements MessageCreator {

	private Message message;

	public MessageCreatorImpl(final Message message) {
		this.message = message;
	}

	@Override
	public Message createMessage(final Session session) throws JMSException {
		return this.message;
	}

}
