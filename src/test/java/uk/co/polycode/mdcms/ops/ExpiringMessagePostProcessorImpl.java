package uk.co.polycode.mdcms.ops;

import org.springframework.jms.core.MessagePostProcessor;

import javax.jms.JMSException;
import javax.jms.Message;

/**
 * Provide the message used to create this object as the created message
 */
public class ExpiringMessagePostProcessorImpl implements MessagePostProcessor {

	private long timeToLive;

	public ExpiringMessagePostProcessorImpl(final long timeToLive){
		this.timeToLive = timeToLive;
	}

	@Override
	public Message postProcessMessage(final Message message) throws JMSException {
		long currentTime = System.currentTimeMillis();
		message.setJMSExpiration(currentTime + timeToLive);
		return message;
	}
}
