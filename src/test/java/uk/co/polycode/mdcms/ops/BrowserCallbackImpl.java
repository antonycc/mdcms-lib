package uk.co.polycode.mdcms.ops;

import org.springframework.jms.core.BrowserCallback;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import javax.jms.Session;

/**
 * Call back to provide queue and session
 */
public class BrowserCallbackImpl implements BrowserCallback<SessionAndQueueContainer> {

	public BrowserCallbackImpl(){
	}

	@Override
	public SessionAndQueueContainer doInJms(final Session session, final QueueBrowser queueBrowser) throws JMSException {
		SessionAndQueueContainer sessionAndQueueContainer = new SessionAndQueueContainer();
		sessionAndQueueContainer.setSession(session);
		sessionAndQueueContainer.setQueueBrowser(queueBrowser);
		return sessionAndQueueContainer;
	}
}
