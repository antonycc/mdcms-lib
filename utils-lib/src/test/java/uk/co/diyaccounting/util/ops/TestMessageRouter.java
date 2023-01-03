package uk.co.diyaccounting.util.ops;

import org.springframework.jms.JmsException;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * A creator which just routes to a given listener
 */
public class TestMessageRouter extends JmsTemplate {

	private MessageListener messageListener;

	@Override
	public void send(final MessageCreator messageCreator) throws JmsException {
		try {
			Message message = messageCreator.createMessage(null);
			this.messageListener.onMessage(message);
		}catch(JMSException e){
			throw new UncategorizedJmsException(e);
		}
	}

	public void setMessageListener(final MessageListener messageListener) {
		this.messageListener = messageListener;
	}
}
