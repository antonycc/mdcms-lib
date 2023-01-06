package uk.co.polycode.mdcms.unit;

import org.easymock.EasyMock;
import org.junit.Test;
import org.springframework.jms.JmsException;
import org.springframework.jms.UncategorizedJmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import uk.co.polycode.mdcms.util.jms.ValidatingMessageListener;

import javax.jms.JMSException;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

/**
 * High level message flow
 */
public class ValidatingMessageListenerTest {

	@Test
	public void expectNonExpiringMessageToBeProcessedByDownstreamListener()
		throws JMSException{

		// Test parameters
		long expiration = 0L;

		// Expected results

		// Mocks
		TextMessage message = EasyMock.createNiceMock(TextMessage.class);
		EasyMock.expect(message.getJMSExpiration()).andReturn(expiration);
		MessageListener messageListener = EasyMock.createStrictMock(MessageListener.class);
		messageListener.onMessage(message);
		EasyMock.expectLastCall();
		EasyMock.replay(message, messageListener);

		// Class under test
		ValidatingMessageListener classUnderTest = new ValidatingMessageListener();
		classUnderTest.setDownstreamListener(messageListener);

		// Execute
		classUnderTest.onMessage(message);

		// Checks via mocks
	}

	@Test
	public void expectRejectedMessageToBePutOnTheInvalidQueue()
			throws JMSException{

		// Test parameters
		long expiration = 0L;
		String exceptionMessage = "Mock forced exception";

		// Expected results
		JmsException jmsException = new UncategorizedJmsException(exceptionMessage);

		// Mocks
		TextMessage message = EasyMock.createNiceMock(TextMessage.class);
		EasyMock.expect(message.getJMSExpiration()).andReturn(expiration);
		MessageListener messageListener = EasyMock.createStrictMock(MessageListener.class);
		messageListener.onMessage(message);
		EasyMock.expectLastCall();
		JmsTemplate jmsTemplate = EasyMock.createStrictMock(JmsTemplate.class);
		jmsTemplate.send(EasyMock.anyObject(MessageCreator.class));
		EasyMock.expectLastCall().andThrow(jmsException);
		EasyMock.replay(message, messageListener, jmsTemplate);

		// Class under test
		ValidatingMessageListener classUnderTest = new ValidatingMessageListener();
		classUnderTest.setDownstreamListener(messageListener);
		classUnderTest.setInvalidTemplate(jmsTemplate);

		// Execute
		classUnderTest.onMessage(message);

		// Checks via mocks
	}
}
