package uk.co.diyaccounting.util.ops;

import javax.jms.QueueBrowser;
import javax.jms.Session;

/**
 * A means of passing the session and queue from a BrowserCallback to a object that will use them.
 */
public class SessionAndQueueContainer {

	private Session session;

	private QueueBrowser queueBrowser;

	public Session getSession() {
		return session;
	}

	public void setSession(final Session session) {
		this.session = session;
	}

	public QueueBrowser getQueueBrowser() {
		return queueBrowser;
	}

	public void setQueueBrowser(final QueueBrowser queueBrowser) {
		this.queueBrowser = queueBrowser;
	}
}
