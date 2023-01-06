package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Test;

import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.ops.TestMetadata;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class MetadataTest {

   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   /**
    * Test read values
    * 
    * @throws ContentException
    */
   @Test
   public void expectStandardMetadataToBeAsExpectedWhenRead() throws ContentException {

      String testPath = baseClasspathUrn + "test1/TestPage.html";

      // Expected values
      String expectedTitle = "Test Title";
	   String expectedDescription = "Test <a href=\"link\">text</a>Description";
      String expectedMetaDescription = "Description for search results";
      String expectedKeywords = "Test Keywords";

      // Create Object to test
      TestMetadata classUnderTest = new TestMetadata();
      classUnderTest.setPath(testPath);
      classUnderTest.populateContent(new FileLikePathService());

      // Check
      Assert.assertEquals(expectedTitle, classUnderTest.getTitle());
      Assert.assertEquals(expectedDescription, classUnderTest.getDescription());
      Assert.assertEquals(expectedMetaDescription, classUnderTest.getMetaDescription());
      Assert.assertEquals(expectedKeywords, classUnderTest.getKeywords());
   }
}