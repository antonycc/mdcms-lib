package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.LocatorImpl;

import uk.co.polycode.mdcms.util.xml.XMLErrorHandler;

/**
 * Test XMLErrorHandler - mostly methods that just re-throw
 * 
 * @author Antony
 */
public class XMLErrorHandlerTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(XMLErrorHandlerTest.class);

   // Input Test data

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test XMLErrorHandler - mostly methods that just re-throw
    */
   @Test
   public void testConstructors() {

      // Expected results
      String expectedMessage = "expectedMessage";
      Locator locator = new LocatorImpl();
      SAXParseException expectedCause = new SAXParseException(expectedMessage, locator);

      // Instance under test
      XMLErrorHandler errorHandler = new XMLErrorHandler();

      // Warning
      try {
         errorHandler.warning(expectedCause);
         Assert.fail("An exception should have been thrown");
      } catch (SAXException e) {
         Assert.assertEquals("Message should be as set in the original", expectedMessage, e.getMessage());
      }

      // Error
      try {
         errorHandler.error(expectedCause);
         Assert.fail("An exception should have been thrown");
      } catch (SAXException e) {
         Assert.assertEquals("Message should be as set in the original", expectedMessage, e.getMessage());
      }

      // Fatal
      try {
         errorHandler.fatalError(expectedCause);
         Assert.fail("An exception should have been thrown");
      } catch (SAXException e) {
         Assert.assertEquals("Message should be as set in the original", expectedMessage, e.getMessage());
      }
   }

}