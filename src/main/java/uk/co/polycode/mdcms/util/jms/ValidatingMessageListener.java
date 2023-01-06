package uk.co.polycode.mdcms.util.jms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Receive messages, check for expired messages and is processing is not completed move the message to the invalid queue
 */
public class ValidatingMessageListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ValidatingMessageListener.class);

	private JmsTemplate invalidTemplate;

	private MessageListener downstreamListener;

	@Override
	public void onMessage(Message message) {

		try {

			// Push the message to the downstream listener
			this.downstreamListener.onMessage(message);
			logger.debug("Processed [{}]", message.toString());

			// If the downstream listener rejected the message push it onto the invalid queue
		}catch(Exception e) {
			logger.warn(e.getMessage(), e);
			this.rejectMessage(message);
		}
	}

	private void rejectMessage(final Message message){
		MessageCreatorImpl messageCreator = new MessageCreatorImpl(message);
		this.invalidTemplate.send(messageCreator);
		logger.debug("Rejected [{}]", message.toString());
	}

	public void setInvalidTemplate(final JmsTemplate invalidTemplate) {
		this.invalidTemplate = invalidTemplate;
	}

	public void setDownstreamListener(final MessageListener downstreamListener) {
		this.downstreamListener = downstreamListener;
	}
}