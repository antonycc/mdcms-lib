package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.polycode.mdcms.util.jms.InvalidMessageException;

/**
 * Test XMLException - mostly constructors
 * 
 * @author Antony
 */
public class InvalidMessageExceptionTest {

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
      InvalidMessageException e;

      // Empty constructor
      e = new InvalidMessageException();
      Assert.assertNull(e.getMessage());
      Assert.assertNull(e.getCause());

      // Message constructor
      e = new InvalidMessageException(expectedMessage);
      Assert.assertEquals(expectedMessage, e.getMessage());
      Assert.assertNull(e.getCause());

      // Cause constructor
      e = new InvalidMessageException(expectedCause);
      Assert.assertEquals(expectedCause.getClass().getName() + ": " + expectedCauseMessage,
               e.getMessage());
      Assert.assertEquals(expectedCause, e.getCause());
      Assert.assertEquals(expectedCauseMessage, e.getCause().getMessage());

      // Message and cause constructor
      e = new InvalidMessageException(expectedMessage, expectedCause);
      Assert.assertEquals(expectedMessage, e.getMessage());
      Assert.assertEquals(expectedCause, e.getCause());
      Assert.assertEquals(expectedCauseMessage, e.getCause().getMessage());
   }

}