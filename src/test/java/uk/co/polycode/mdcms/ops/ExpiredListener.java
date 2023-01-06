package uk.co.polycode.mdcms.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Listener for expired messages
 */
public class ExpiredListener implements MessageListener {

	private static final Logger logger = LoggerFactory.getLogger(ExpiredListener.class);

	public static int received = Integer.MIN_VALUE;

	@Override
	public void onMessage(Message message) {
		logger.debug("Received [{}] (Expired)", message.toString());
		ExpiredListener.received++;
	}

}