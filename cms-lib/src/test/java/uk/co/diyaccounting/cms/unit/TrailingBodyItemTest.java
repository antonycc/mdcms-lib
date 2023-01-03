package uk.co.diyaccounting.cms.unit;

import org.junit.Assert;
import org.junit.Test;

import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.cms.ops.TrailingBodyItem;
import uk.co.diyaccounting.util.io.FileLikePathService;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class TrailingBodyItemTest {

   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectTrailingBodyToBeRead() throws ContentException {

      // Set-up values
      String path = baseClasspathUrn + "test1/trailingBody.html";

      // expected values
      String expectedStartOfTrailingBody = "<p>Lorem ipsum dolor sit amet, consectetur adipiscing elit.";

      // instance under test "item" is initialised using constructor
      TrailingBodyItem classUnderTest = null;
      classUnderTest = new TrailingBodyItem();
      classUnderTest.setPath(path);
      classUnderTest.populateContent(new FileLikePathService());

      // Check
      String actualStartOfTrailingBody = classUnderTest.getTrailingBody().substring(0,
               expectedStartOfTrailingBody.length());
      Assert.assertEquals("Expected trailing body to start with this text", expectedStartOfTrailingBody,
               actualStartOfTrailingBody);
   }

}