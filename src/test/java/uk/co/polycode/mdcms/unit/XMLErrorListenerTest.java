package uk.co.polycode.mdcms.unit;

import javax.xml.transform.TransformerException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.polycode.mdcms.util.xml.XMLErrorListener;

/**
 * Test XMLErrorListener - mostly methods that just re-throw
 * 
 * @author Antony
 */
public class XMLErrorListenerTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(XMLErrorListenerTest.class);

   // Input Test data

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test XMLErrorListener - mostly methods that just re-throw
    */
   @Test
   public void testConstructors() {

      // Expected results
      String expectedMessage = "expectedMessage";
      TransformerException expectedCause = new TransformerException(expectedMessage);

      // Instance under test
      XMLErrorListener errorListener = new XMLErrorListener();

      // Warning
      try {
         errorListener.warning(expectedCause);
         Assert.fail("An exception should have been thrown");
      } catch (TransformerException e) {
         Assert.assertEquals("Message should be as set in the original", expectedMessage, e.getMessage());
      }

      // Error
      try {
         errorListener.error(expectedCause);
         Assert.fail("An exception should have been thrown");
      } catch (TransformerException e) {
         Assert.assertEquals("Message should be as set in the original", expectedMessage, e.getMessage());
      }

      // Fatal
      try {
         errorListener.fatalError(expectedCause);
         Assert.fail("An exception should have been thrown");
      } catch (TransformerException e) {
         Assert.assertEquals("Message should be as set in the original", expectedMessage, e.getMessage());
      }
   }

}