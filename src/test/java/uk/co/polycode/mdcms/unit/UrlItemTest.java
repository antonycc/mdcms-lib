package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Test;

import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.ops.UrlItem;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class UrlItemTest {

   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectUrlToBeReadAsExpected() throws ContentException {

      String expectedUrl = "http://www.google.co.uk/";

      String basePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      String path = baseClasspathUrn + "test1/url.html";

      // instance under test "item" is initialised using constructor
      UrlItem classUnderTest = new UrlItem();
      classUnderTest.setPath(path);
      classUnderTest.populateContent(new FileLikePathService());
      String actualUrl = classUnderTest.getUrl().toExternalForm();

      // Check
      Assert.assertEquals("Content item should be google", expectedUrl, actualUrl);
   }

}