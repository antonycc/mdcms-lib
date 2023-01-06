package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Test;

import uk.co.polycode.mdcms.ops.TestMetadata;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class MdMetadataTest {

   private final String localPath = MdMetadataTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectStandardFileMetadataToBeAsExpectedWhenRead() throws ContentException {

      String testPath = baseFileUrn + "test1/TestPage.md";

      // Expected values
      String expectedTitle = "Test Title";
      //String expectedDescription = "Test <a href=\"link\" shape=\"rect\">text</a>Description";
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

   @Test
   public void expectStandardMetadataToBeAsExpectedWhenRead() throws ContentException {

      String testPath = baseClasspathUrn + "test1/TestPage.md";

      // Expected values
      String expectedTitle = "Test Title";
      //String expectedDescription = "Test <a href=\"link\" shape=\"rect\">text</a>Description";
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