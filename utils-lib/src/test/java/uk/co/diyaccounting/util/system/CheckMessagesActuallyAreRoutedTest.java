package uk.co.diyaccounting.util.system;

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
import uk.co.diyaccounting.util.ops.ExpiringMessagePostProcessorImpl;
import uk.co.diyaccounting.util.ops.InvalidListener;
import uk.co.diyaccounting.util.ops.NameCountJsonMessage;
import uk.co.diyaccounting.util.ops.PrimaryListener;
import uk.co.diyaccounting.util.ops.SomeOtherJsonMessage;

import javax.jms.JMSException;
import java.util.Random;
import java.util.UUID;

/**
 * Initialise the Spring messaging context and send a message
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/jms-test-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CheckMessagesActuallyAreRoutedTest {

	private static final Logger logger = LoggerFactory.getLogger(CheckMessagesActuallyAreRoutedTest.class);

	@Autowired
	@Qualifier("messagePrimaryTestQueueTemplate")
	private JmsTemplate messagePrimaryTestQueueTemplate;

	private Random random = new Random();

	@Test
	public void aNOP(){
		// Empty test just to see if the Spring context initialises
		Assert.assertNotNull(this.messagePrimaryTestQueueTemplate);
	}

	@Test
	public void bExpectATextMessageToBeSentAndReceived()
		throws InterruptedException, JMSException{

		// Test parameters
		String name = UUID.randomUUID().toString();
		int count = this.generateRandomInteger(1, Integer.MAX_VALUE);
		long waitTime = 100L;
		long processingTime = 10L;

		// Expected results
		NameCountJsonMessage jsonMessage = new NameCountJsonMessage();
		jsonMessage.setName(name);
		jsonMessage.setCount(count);

		// Mocks

		// Class under test
		JmsTemplate classUnderTest = this.messagePrimaryTestQueueTemplate;

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = processingTime;
			logger.debug("Sending...");
			classUnderTest.convertAndSend(jsonMessage.toJsonString());
			logger.debug("Sent test message [{}]", jsonMessage.toString());

			// Give message time to arrive
			long t1 = System.currentTimeMillis();
			long t2;
			do {
				this.sleep();
				t2 = System.currentTimeMillis();
			} while (PrimaryListener.received != 1 && (t2 - t1) < waitTime);
			logger.debug("primary {}, expired {}, invalid {}", PrimaryListener.received, ExpiredListener.received, InvalidListener.received);

			// Checks
			Assert.assertEquals(1, PrimaryListener.received);
			Assert.assertEquals(0, ExpiredListener.received);
			Assert.assertEquals(0, InvalidListener.received);
		}finally {
			PrimaryListener.received = Integer.MIN_VALUE;
			ExpiredListener.received = Integer.MIN_VALUE;
			InvalidListener.received = Integer.MIN_VALUE;
			PrimaryListener.processingTime = savedProcessingTime;
		}
	}

	@Test
	public void bExpectATwoTextMessagesToBeSentAndReceived()
			throws InterruptedException, JMSException{

		// Test parameters
		String name1 = "[#1]" + UUID.randomUUID().toString();
		int count1 = this.generateRandomInteger(1, Integer.MAX_VALUE);
		String name2 = "[#2]" + UUID.randomUUID().toString();
		int count2 = this.generateRandomInteger(1, Integer.MAX_VALUE);
		long waitTime = 100L;
		long processingTime = 10L;

		// Expected results
		NameCountJsonMessage jsonMessage1 = new NameCountJsonMessage();
		jsonMessage1.setName(name1);
		jsonMessage1.setCount(count1);
		NameCountJsonMessage jsonMessage2 = new NameCountJsonMessage();
		jsonMessage2.setName(name2);
		jsonMessage2.setCount(count2);

		// Mocks

		// Class under test
		JmsTemplate classUnderTest = this.messagePrimaryTestQueueTemplate;

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = processingTime;
			logger.debug("Sending...");
			classUnderTest.convertAndSend(jsonMessage1.toJsonString());
			logger.debug("Sent test message [{}]", jsonMessage1.toString());
			classUnderTest.convertAndSend(jsonMessage1.toJsonString());
			logger.debug("Sent test message [{}]", jsonMessage2.toString());

			// Give message time to arrive
			long t1 = System.currentTimeMillis();
			long t2;
			do {
				this.sleep();
				t2 = System.currentTimeMillis();
			} while (PrimaryListener.received != 2 && (t2 - t1) < waitTime);
			logger.debug("primary {}, expired {}, invalid {}", PrimaryListener.received, ExpiredListener.received, InvalidListener.received);

			// Checks
			Assert.assertEquals(2, PrimaryListener.received);
			Assert.assertEquals(0, ExpiredListener.received);
			Assert.assertEquals(0, InvalidListener.received);
		}finally {
			PrimaryListener.received = Integer.MIN_VALUE;
			ExpiredListener.received = Integer.MIN_VALUE;
			InvalidListener.received = Integer.MIN_VALUE;
			PrimaryListener.processingTime = savedProcessingTime;
		}
	}

	@Test
	public void cExpectATextMessageToBeRejectedAndMoveToTheInvalidQueue()
			throws InterruptedException, JMSException{

		// Test parameters
		String name = UUID.randomUUID().toString();
		int count = this.generateRandomInteger(1, Integer.MAX_VALUE);
		long waitTime = 100L;
		long processingTime = 10L;

		// Expected results
		SomeOtherJsonMessage jsonMessage = new SomeOtherJsonMessage();
		jsonMessage.setNotName(name);
		jsonMessage.setNotCount(count);

		// Mocks

		// Class under test
		JmsTemplate classUnderTest = this.messagePrimaryTestQueueTemplate;

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = processingTime;
			logger.debug("Sending...");
			classUnderTest.convertAndSend(jsonMessage.toJsonString());
			logger.debug("Sent test message [{}]", jsonMessage.toString());

			// Give message time to arrive
			long t1 = System.currentTimeMillis();
			long t2;
			do {
				this.sleep();
				t2 = System.currentTimeMillis();
			} while (InvalidListener.received != 1 && (t2 - t1) < waitTime);
			logger.debug("primary {}, expired {}, invalid {}", PrimaryListener.received, ExpiredListener.received, InvalidListener.received);

			// Checks
			Assert.assertEquals(0, PrimaryListener.received);
			Assert.assertEquals(0, ExpiredListener.received);
			Assert.assertEquals(1, InvalidListener.received);
		}finally {
			PrimaryListener.received = Integer.MIN_VALUE;
			ExpiredListener.received = Integer.MIN_VALUE;
			InvalidListener.received = Integer.MIN_VALUE;
			PrimaryListener.processingTime = savedProcessingTime;
		}
	}

	@Test
	public void dExpectATextMessageToExpireAndMoveToTheExpiredQueue()
			throws InterruptedException, JMSException{

		// Test parameters
		String name1 = "[#1]" + UUID.randomUUID().toString();
		int count1 = this.generateRandomInteger(1, Integer.MAX_VALUE);
		String name2 = "[#2]" + UUID.randomUUID().toString();
		int count2 = this.generateRandomInteger(1, Integer.MAX_VALUE);
		long waitTime = 200L;
		long processingTime = 110L;
		long timeToLive = 100L;

		// Expected results
		NameCountJsonMessage jsonMessage1 = new NameCountJsonMessage();
		jsonMessage1.setName(name1);
		jsonMessage1.setCount(count1);
		NameCountJsonMessage jsonMessage2 = new NameCountJsonMessage();
		jsonMessage2.setName(name2);
		jsonMessage2.setCount(count2);
		ExpiringMessagePostProcessorImpl expiringMessagePostProcessor = new ExpiringMessagePostProcessorImpl(timeToLive);

		// Mocks

		// Class under test
		JmsTemplate classUnderTest = this.messagePrimaryTestQueueTemplate;

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = processingTime;
			logger.debug("Sending...");
			classUnderTest.convertAndSend((Object)jsonMessage1.toJsonString(), expiringMessagePostProcessor);
			logger.debug("Sent test message [{}]", jsonMessage1.toString());
			classUnderTest.convertAndSend((Object)jsonMessage2.toJsonString(), expiringMessagePostProcessor);
			logger.debug("Sent test message [{}]", jsonMessage2.toString());

			// Give message time to arrive
			long t1 = System.currentTimeMillis();
			long t2;
			do {
				this.sleep();
				t2 = System.currentTimeMillis();
			} while (ExpiredListener.received != 1 && (t2 - t1) < waitTime);
			logger.debug("primary {}, expired {}, invalid {}", PrimaryListener.received, ExpiredListener.received, InvalidListener.received);

			// Checks
			Assert.assertEquals(1, PrimaryListener.received);
			Assert.assertEquals(1, ExpiredListener.received);
			Assert.assertEquals(0, InvalidListener.received);
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
