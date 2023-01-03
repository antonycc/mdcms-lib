package uk.co.diyaccounting.util.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.util.ops.InheritedToStringJsonMessage;
import uk.co.diyaccounting.util.ops.NameCountJsonMessage;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.Random;
import java.util.UUID;

public class DTOBeanTest {

	private Random random = new Random();

   @Test
   public void JsonMessageGettersAndSetters()
      throws JMSException{

      // Test parameters
	   String name = UUID.randomUUID().toString();
	   int count = this.generateRandomInteger(1, Integer.MAX_VALUE);

      // Expected results
	   NameCountJsonMessage jsonMessage = new NameCountJsonMessage();
	   jsonMessage.setName(name);
	   jsonMessage.setCount(count);
	   String messageAsJson = jsonMessage.toJsonString();
	   InheritedToStringJsonMessage inheritedToStringJsonMessage = new InheritedToStringJsonMessage();
	   inheritedToStringJsonMessage.setName(name);

      // Mocks
	   TextMessage message = EasyMock.createNiceMock(TextMessage.class);
	   EasyMock.expect(message.getText()).andReturn(jsonMessage.toJsonString());
	   EasyMock.replay(message);

      // Class under test
	   NameCountJsonMessage classUnderTest = new NameCountJsonMessage(message);

      // Checks
	   Assert.assertSame(message, classUnderTest.getTextMessage());
	   Assert.assertEquals(name, classUnderTest.getName());
	   Assert.assertEquals(count, classUnderTest.getCount());
	   Assert.assertTrue(inheritedToStringJsonMessage.toString().indexOf(name) != -1);
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
