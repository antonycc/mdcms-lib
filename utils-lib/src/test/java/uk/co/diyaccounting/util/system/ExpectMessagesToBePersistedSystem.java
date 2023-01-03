package uk.co.diyaccounting.util.system;

import org.apache.commons.dbutils.DbUtils;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.diyaccounting.util.ops.ExpiredListener;
import uk.co.diyaccounting.util.ops.InvalidListener;
import uk.co.diyaccounting.util.ops.NameCountJsonMessage;
import uk.co.diyaccounting.util.ops.PrimaryListener;

import javax.jms.JMSException;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;

/**
 * Initialise the Spring messaging context and send a message
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/jms-test-context.xml"})
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ExpectMessagesToBePersistedSystem {

	private static final Logger logger = LoggerFactory.getLogger(ExpectMessagesToBePersistedSystem.class);

	@Autowired
	@Qualifier("messagePrimaryTestQueueTemplate")
	private JmsTemplate messagePrimaryTestQueueTemplate;;

	@Autowired
	@Qualifier("messageDataSource")
	DataSource dataSource;

	private Random random = new Random();

	/**
	 * Empty test just to see if the Spring context initialises and the message queue is empty
	 */
	@Test
	public void aNOP()
		throws SQLException{

		// Test parameters
		long waitTime = 150L;

		// Expected results

		// Mocks

		// Class under test
		JmsTemplate classUnderTest = this.messagePrimaryTestQueueTemplate;
		Assert.assertNotNull(classUnderTest);

		// Check the queue is empty
		int messageCount = this.countMessagesInDatabase();
		Assert.assertEquals(0, messageCount);

		// Execute

		// Wait for a short time
		long t1 = System.currentTimeMillis();
		long t2;
		do {
			this.sleep();
			t2 = System.currentTimeMillis();
		} while ((t2 - t1) < waitTime);
	}

	@Test
	public void bExpectMessageToBePersisted()
		throws InterruptedException, JmsException, JMSException, SQLException{

		// Test parameters
		String name = UUID.randomUUID().toString();
		int count = this.generateRandomInteger(1, Integer.MAX_VALUE);
		long waitTime = 250L;
		long processingTime = 100L;

		// Expected results
		NameCountJsonMessage jsonMessage = new NameCountJsonMessage();
		jsonMessage.setName(name);
		jsonMessage.setCount(count);
		int messageCount;

		// Mocks

		// Class under test
		JmsTemplate classUnderTest = this.messagePrimaryTestQueueTemplate;

		// Check the queue is empty
		messageCount = this.countMessagesInDatabase();
		Assert.assertEquals(0, messageCount);

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		PrimaryListener.received = 0;
		ExpiredListener.received = 0;
		InvalidListener.received = 0;
		try {
			PrimaryListener.processingTime = processingTime;
			logger.debug("Sending...");
			classUnderTest.convertAndSend(jsonMessage.toJsonString());
			logger.debug("Sent test message [{}]", jsonMessage.toString());

			// Check message is on the queue
			messageCount = this.countMessagesInDatabase();
			Assert.assertEquals(1, messageCount);

			// Give message time to arrive
			long t1 = System.currentTimeMillis();
			long t2;
			do {
				this.sleep();
				t2 = System.currentTimeMillis();
				messageCount = this.countMessagesInDatabase();
			}
			while (PrimaryListener.received != 1 && (t2 - t1) < waitTime && messageCount != 0 && PrimaryListener.received != 1);
			logger.debug("primary {}, expired {}, invalid {}", PrimaryListener.received, ExpiredListener.received, InvalidListener.received);

			// Check the queue is empty
			messageCount = this.countMessagesInDatabase();
			Assert.assertEquals(0, messageCount);
		}finally {
			PrimaryListener.received = Integer.MIN_VALUE;
			ExpiredListener.received = Integer.MIN_VALUE;
			InvalidListener.received = Integer.MIN_VALUE;
			PrimaryListener.processingTime = savedProcessingTime;
		}
	}

	@Test
	public void cExpectMessageToBePersistedAgain()
			throws InterruptedException, JMSException, SQLException{
		this.bExpectMessageToBePersisted();
	}

	private int countMessagesInDatabase()
			throws SQLException{
		String sql = "select count(*) from ACTIVEMQ_MSGS";
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		try{
			connection = this.dataSource.getConnection();
			statement = connection.prepareStatement(sql);
			rs = statement.executeQuery();
			rs.next();
			int messageCount = rs.getInt(1);
			//logger.debug("The are currently {} messages in ACTIVEMQ_MSGS", messageCount);
			return messageCount;
		}finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(statement);
			DbUtils.closeQuietly(connection);
		}
	}

	private void sleep(){
		long sleepDuration = 100L;
		long sleepGranularity = 10L;
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
