package uk.co.polycode.mdcms.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import uk.co.polycode.mdcms.util.jms.ValidatingMessageListener;
import uk.co.polycode.mdcms.ops.ExpiredListener;
import uk.co.polycode.mdcms.ops.InvalidListener;
import uk.co.polycode.mdcms.ops.NameCountJsonMessage;
import uk.co.polycode.mdcms.ops.PrimaryListener;
import uk.co.polycode.mdcms.ops.SomeOtherJsonMessage;
import uk.co.polycode.mdcms.ops.TestMessageRouter;
import uk.co.polycode.mdcms.ops.UnknownMessage;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.util.Random;
import java.util.UUID;

/**
 * Send messages using mocked templates
 */
public class CheckMessageRoutingTest {

	private static final Logger logger = LoggerFactory.getLogger(CheckMessageRoutingTest.class);

	private Random random = new Random();

	@Test
	public void expectATextMessageToBeReceived()
		throws InterruptedException, JMSException{

		// Test parameters
		String name = UUID.randomUUID().toString();
		int count = this.generateRandomInteger(1, Integer.MAX_VALUE);

		// Expected results
		NameCountJsonMessage jsonMessage = new NameCountJsonMessage();
		jsonMessage.setName(name);
		jsonMessage.setCount(count);

		// Mocks
		TextMessage message = EasyMock.createNiceMock(TextMessage.class);
		EasyMock.expect(message.getText()).andReturn(jsonMessage.toJsonString());
		EasyMock.replay(message);

		// Class under test
		MessageListener classUnderTest = new PrimaryListener();

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = 0;
			classUnderTest.onMessage(message);

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
	public void expectATextMessageToBeReceivedByValidatingListener()
			throws InterruptedException, JMSException{

		// Test parameters
		String name = UUID.randomUUID().toString();
		int count = this.generateRandomInteger(1, Integer.MAX_VALUE);

		// Expected results
		NameCountJsonMessage jsonMessage = new NameCountJsonMessage();
		jsonMessage.setName(name);
		jsonMessage.setCount(count);
		PrimaryListener primaryListener = new PrimaryListener();

		// Mocks
		TextMessage message = EasyMock.createNiceMock(TextMessage.class);
		EasyMock.expect(message.getText()).andReturn(jsonMessage.toJsonString());
		EasyMock.replay(message);

		// Class under test
		ValidatingMessageListener classUnderTest = new ValidatingMessageListener();
		classUnderTest.setDownstreamListener(primaryListener);

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = 0;
			classUnderTest.onMessage(message);

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
	public void expectATwoTextMessagesToBeSentAndReceived()
			throws InterruptedException, JMSException{

		// Test parameters
		String name1 = "[#1]" + UUID.randomUUID().toString();
		int count1 = this.generateRandomInteger(1, Integer.MAX_VALUE);
		String name2 = "[#2]" + UUID.randomUUID().toString();
		int count2 = this.generateRandomInteger(1, Integer.MAX_VALUE);

		// Expected results
		NameCountJsonMessage jsonMessage1 = new NameCountJsonMessage();
		jsonMessage1.setName(name1);
		jsonMessage1.setCount(count1);
		NameCountJsonMessage jsonMessage2 = new NameCountJsonMessage();
		jsonMessage2.setName(name2);
		jsonMessage2.setCount(count2);
		PrimaryListener primaryListener = new PrimaryListener();

		// Mocks
		TextMessage message1 = EasyMock.createNiceMock(TextMessage.class);
		EasyMock.expect(message1.getText()).andReturn(jsonMessage1.toJsonString());
		EasyMock.replay(message1);
		TextMessage message2 = EasyMock.createNiceMock(TextMessage.class);
		EasyMock.expect(message2.getText()).andReturn(jsonMessage2.toJsonString());
		EasyMock.replay(message2);

		// Class under test
		ValidatingMessageListener classUnderTest = new ValidatingMessageListener();
		classUnderTest.setDownstreamListener(primaryListener);

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = 0;
			classUnderTest.onMessage(message1);
			classUnderTest.onMessage(message2);

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
	public void expectATextMessageToBeRejectedAndMoveToTheInvalidQueue()
			throws InterruptedException, JMSException{

		// Test parameters
		String name = UUID.randomUUID().toString();
		int count = this.generateRandomInteger(1, Integer.MAX_VALUE);

		// Expected results
		SomeOtherJsonMessage jsonMessage = new SomeOtherJsonMessage();
		jsonMessage.setNotName(name);
		jsonMessage.setNotCount(count);
		PrimaryListener primaryListener = new PrimaryListener();
		InvalidListener invalidListener = new InvalidListener();
		TestMessageRouter testMessageRouter = new TestMessageRouter();
		testMessageRouter.setMessageListener(invalidListener);

		// Mocks
		TextMessage message = EasyMock.createNiceMock(TextMessage.class);
		EasyMock.expect(message.getText()).andReturn(jsonMessage.toJsonString());
		JmsTemplate invalidTemplate = EasyMock.createMock(JmsTemplate.class);
		invalidTemplate.send(EasyMock.anyObject(MessageCreator.class));
		EasyMock.expectLastCall().andDelegateTo(testMessageRouter);
		EasyMock.replay(message, invalidTemplate);

		// Class under test
		ValidatingMessageListener classUnderTest = new ValidatingMessageListener();
		classUnderTest.setDownstreamListener(primaryListener);
		classUnderTest.setInvalidTemplate(invalidTemplate);

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = 0;
			classUnderTest.onMessage(message);

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
	public void expectAUnknownMessageToBeRejectedAndMoveToTheInvalidQueue()
			throws InterruptedException, JMSException{

		// Test parameters
		String name = UUID.randomUUID().toString();
		int count = this.generateRandomInteger(1, Integer.MAX_VALUE);

		// Expected results
		NameCountJsonMessage jsonMessage = new NameCountJsonMessage();
		jsonMessage.setName(name);
		jsonMessage.setCount(count);
		PrimaryListener primaryListener = new PrimaryListener();
		InvalidListener invalidListener = new InvalidListener();
		TestMessageRouter testMessageRouter = new TestMessageRouter();
		testMessageRouter.setMessageListener(invalidListener);

		// Mocks
		UnknownMessage message = EasyMock.createNiceMock(UnknownMessage.class);
		JmsTemplate invalidTemplate = EasyMock.createMock(JmsTemplate.class);
		invalidTemplate.send(EasyMock.anyObject(MessageCreator.class));
		EasyMock.expectLastCall().andDelegateTo(testMessageRouter);
		EasyMock.replay(message, invalidTemplate);

		// Class under test
		ValidatingMessageListener classUnderTest = new ValidatingMessageListener();
		classUnderTest.setDownstreamListener(primaryListener);
		classUnderTest.setInvalidTemplate(invalidTemplate);

		// Execute
		long savedProcessingTime = PrimaryListener.processingTime;
		try {
			PrimaryListener.received = 0;
			ExpiredListener.received = 0;
			InvalidListener.received = 0;
			PrimaryListener.processingTime = 0;
			classUnderTest.onMessage(message);

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
