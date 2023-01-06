package uk.co.polycode.mdcms.ops;

import org.springframework.jms.core.SessionCallback;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueBrowser;
import javax.jms.Session;

/**
 * Call back to provide queue browser
 */
public class SessionCallbackImpl implements SessionCallback<QueueBrowser> {

	private Queue queue;

	public SessionCallbackImpl(final Queue queue){
		this.queue = queue;
	}

	@Override
	public QueueBrowser doInJms(final Session session) throws JMSException {
		QueueBrowser queueBrowser = session.createBrowser(this.queue);
		return queueBrowser;
	}
}
