package uk.co.diyaccounting.util.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.diyaccounting.util.xml.XMLException;

/**
 * Test XMLException - mostly constructors
 * 
 * @author Antony
 */
public class XMLExceptionTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(XMLExceptionTest.class);

   // Input Test data

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test XMLException creation
    */
   @Test
   public void testConstructors() {

      // Expected results
      String expectedMessage = "expectedMessage";
      String expectedCauseMessage = "expectedCauseMessage";
      Exception expectedCause = new Exception(expectedCauseMessage);

      // Instance under test
      XMLException e;

      // Empty constructor
      e = new XMLException();
      Assert.assertNull(e.getMessage());
      Assert.assertNull(e.getCause());

      // Message constructor
      e = new XMLException(expectedMessage);
      Assert.assertEquals("Message should be as set", expectedMessage, e.getMessage());
      Assert.assertNull(e.getCause());

      // Cause constructor
      e = new XMLException(expectedCause);
      Assert.assertEquals("Message should be as set", expectedCause.getClass().getName() + ": " + expectedCauseMessage,
               e.getMessage());
      Assert.assertEquals("Cause should be as set", expectedCause, e.getCause());
      Assert.assertEquals("Cause message should be as set", expectedCauseMessage, e.getCause().getMessage());

      // Message and cause constructor
      e = new XMLException(expectedMessage, expectedCause);
      Assert.assertEquals("Message should be as set", expectedMessage, e.getMessage());
      Assert.assertEquals("Cause should be as set", expectedCause, e.getCause());
      Assert.assertEquals("Cause message should be as set", expectedCauseMessage, e.getCause().getMessage());
   }

}