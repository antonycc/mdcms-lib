package uk.co.diyaccounting.util.system;

import org.apache.activemq.command.ActiveMQQueue;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.diyaccounting.util.ops.ExpiredListener;
import uk.co.diyaccounting.util.ops.InvalidListener;
import uk.co.diyaccounting.util.ops.NameCountJsonMessage;
import uk.co.diyaccounting.util.ops.PrimaryListener;
import uk.co.diyaccounting.util.ops.SessionCallbackImpl;

import javax.jms.JMSException;
import javax.jms.QueueBrowser;
import java.util.Random;
import java.util.UUID;

/**
 * Initialise the Spring messaging context and send a message
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/jms-test-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BrowseMessagesQueueTest {

	private static final Logger logger = LoggerFactory.getLogger(BrowseMessagesQueueTest.class);

	@Autowired
	@Qualifier("messagePrimaryTestQueueTemplate")
	private JmsTemplate messagePrimaryTestQueueTemplate;

	@Autowired
	@Qualifier("test-primary")
	private ActiveMQQueue primaryTestQueue;

	private Random random = new Random();

	@Test
	public void aNOP(){
		// Empty test just to see if the Spring context initialises
		Assert.assertNotNull(this.messagePrimaryTestQueueTemplate);
	}

	//@Test
	public void bExpectMessageToBeOnTheQueueThenOffTheQueue()
		throws InterruptedException, JMSException{

		// Test parameters
		String name = UUID.randomUUID().toString();
		int count = this.generateRandomInteger(1, Integer.MAX_VALUE);
		long waitTime = 150L;
		long processingTime = 100L;

		// Expected results
		NameCountJsonMessage jsonMessage = new NameCountJsonMessage();
		jsonMessage.setName(name);
		jsonMessage.setCount(count);
		Assert.assertNotNull(this.primaryTestQueue);
		SessionCallbackImpl sessionCallback = new SessionCallbackImpl(this.primaryTestQueue);

		// Mocks

		// Class under test
		JmsTemplate classUnderTest = this.messagePrimaryTestQueueTemplate;

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		QueueBrowser queueBrowser;
		PrimaryListener.received = 0;
		ExpiredListener.received = 0;
		InvalidListener.received = 0;
		try {
			// Check message is not on the queue
			queueBrowser = classUnderTest.execute(sessionCallback);
			Assert.assertNotNull(queueBrowser);
			Assert.assertNotNull(queueBrowser.getEnumeration());

			// Add a message to the queue
			PrimaryListener.processingTime = processingTime;
			logger.debug("Sending...");
			classUnderTest.convertAndSend(jsonMessage.toJsonString());
			logger.debug("Sent test message [{}]", jsonMessage.toString());

			// Check message is on the queue
			queueBrowser = classUnderTest.execute(sessionCallback);
			Assert.assertNotNull(queueBrowser);
			Assert.assertNotNull(queueBrowser.getEnumeration());

			// Give message time to arrive
			long t1 = System.currentTimeMillis();
			long t2;
			do {
				this.sleep();
				t2 = System.currentTimeMillis();
			} while (PrimaryListener.received != 1 && (t2 - t1) < waitTime);
			logger.debug("primary {}, expired {}, invalid {}", PrimaryListener.received, ExpiredListener.received, InvalidListener.received);

			// Check message is not on the queue
			queueBrowser = classUnderTest.execute(sessionCallback);
			Assert.assertNotNull(queueBrowser);
			Assert.assertNotNull(queueBrowser.getEnumeration());

		}finally {
			PrimaryListener.received = Integer.MIN_VALUE;
			ExpiredListener.received = Integer.MIN_VALUE;
			InvalidListener.received = Integer.MIN_VALUE;
			PrimaryListener.processingTime = savedProcessingTime;
		}
	}


	private void sleep(){
		long sleepDuration = 10L;
		long sleepGranularity = 1L;
		long sleepTime = sleepDuration / sleepGranularity;
		for(long i=0; i<sleepGranularity; i++) {
			logger.debug("Sleeping...");
			try {
				Thread.sleep(sleepTime);
			}catch(InterruptedException e){
				logger.error("Test did not sleep", e);
			}
		}
	}

	/**
	 * Returns a pseudo-random number between min and max, inclusive.
	 * The difference between min and max can be at most
	 * <code>Integer.MAX_VALUE - 1</code>.
	 *
	 * @param min Minimum value
	 * @param max Maximum value.  Must be greater than min.
	 * @return Integer between min and max, inclusive.
	 * @see java.util.Random#nextInt(int)
	 */
	public int generateRandomInteger(int min, int max) {

		// nextInt is normally exclusive of the top value,
		// so add 1 to make it inclusive
		int randomNum = this.random.nextInt((max - min) + 1) + min;

		return randomNum;
	}
}
