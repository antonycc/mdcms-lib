package uk.co.diyaccounting.util.unit;

import java.net.URI;
import java.net.URISyntaxException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import uk.co.diyaccounting.util.net.URIFactory;

/**
 * Tests uri factory behaviour
 * 
 * @author Antony
 */
public class URIFactoryTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(URIFactoryTest.class);

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test URI Factory creating an expected URI
    */
   @Test
   public void testValidURI() {

      String expectedURL = "http://www.diyaccounting.co.uk";
      URI uri = new URIFactory().getURI(expectedURL);

      Assert.assertEquals("URL should be unchanged", expectedURL, uri.toString());
   }

   /**
    * Test URI Factory failing to a URI
    */
   @Test
   public void testMalformedURI() {

      String expectedURL = "http://www.diyaccounting.co.uk^";
      try {
         new URIFactory().getURI(expectedURL);
         Assert.fail("Exception should have been thrown");
      } catch (IllegalStateException e) {
         Throwable th = e.getCause();
         Assert.assertTrue("cause should be URISyntaxException", th instanceof URISyntaxException);
      }
   }
}