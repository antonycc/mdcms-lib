package uk.co.diyaccounting.cms.unit;

import org.junit.Assert;
import org.junit.Test;

import uk.co.diyaccounting.cms.service.ContentException;

/**
 * Test ContentException - mostly constructors
 * 
 * @author Antony
 */
public class ContentExceptionTest {

   @Test
   public void expectExceptionAttributesToBeSetAsExpectedAfterConstructions() {

      // Expected results
      String expectedPath = "expectedPath";
      String expectedCauseMessage = "expectedCauseMessage";
      String message = "Could not parse underlying document from InputSource";
      Exception expectedCause = new Exception(expectedCauseMessage);

      // Instance under test
      ContentException e;

      // Message constructor
      String expectedMessage = message + ": [" + expectedPath + "]";
      e = new ContentException(message, expectedPath);
      Assert.assertEquals("Message should be as set", expectedMessage, e.getMessage());
      Assert.assertEquals("Path should be as set", expectedPath, e.getPath());
      Assert.assertNull(e.getCause());

      // Message and cause constructor
      expectedMessage = message + ": [" + expectedPath + "], because of: " + expectedCauseMessage;
      e = new ContentException(message, expectedPath, expectedCause);
      Assert.assertEquals("Message should be as set", expectedMessage, e.getMessage());
      Assert.assertEquals("Path should be as set", expectedPath, e.getPath());
      Assert.assertEquals("Cause should be as set", expectedCause, e.getCause());
      Assert.assertEquals("Cause message should be as set", expectedCauseMessage, e.getCause().getMessage());
   }

}