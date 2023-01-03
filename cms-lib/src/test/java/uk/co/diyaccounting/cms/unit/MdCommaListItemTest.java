package uk.co.diyaccounting.cms.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.cms.ops.CommaListItem;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.util.io.FileLikePathService;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class MdCommaListItemTest {

   private final String localPath = MdBooleanItemTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CommaListItemTest.class);

   @Test
   public void expectFileCommaListReadIntoStringArray() throws ContentException {

      String[] expectedCommaList = {
               "ProfitAndLoss",
               "TestFeatureOne",
               "TestFeatureTwo" };

      String commaListContent = baseFileUrn + "test1/commalist.md";

      // instance under test "item" is initialised using constructor
      CommaListItem classUnderTest = new CommaListItem();
      classUnderTest.setPath(commaListContent);
      classUnderTest.populateContent(new FileLikePathService());
      String[] actualCommaList = classUnderTest.getFeatures();

      // Check
      Assert.assertNotNull("Content item should have features", actualCommaList);
      Assert.assertEquals("Content item should have expected features",
               expectedCommaList.length,
               actualCommaList.length);

      for (int i = 0; i < actualCommaList.length; i++) {
         String expectedItem = expectedCommaList[i];
         String actualItem = actualCommaList[i];
         // CommaListItemTest.logger.debug("Actual item {}: [{}]", i, actualItem);
         Assert.assertEquals(expectedItem, actualItem);
      }
   }

   @Test
   public void expectCommaListReadIntoStringArray() throws ContentException {

      String[] expectedCommaList = {
            "ProfitAndLoss",
            "TestFeatureOne",
            "TestFeatureTwo" };

      String commaListContent = baseClasspathUrn + "test1/commalist.md";

      // instance under test "item" is initialised using constructor
      CommaListItem classUnderTest = new CommaListItem();
      classUnderTest.setPath(commaListContent);
      classUnderTest.populateContent(new FileLikePathService());
      String[] actualCommaList = classUnderTest.getFeatures();

      // Check
      Assert.assertNotNull("Content item should have features", actualCommaList);
      Assert.assertEquals("Content item should have expected features",
            expectedCommaList.length,
            actualCommaList.length);

      for (int i = 0; i < actualCommaList.length; i++) {
         String expectedItem = expectedCommaList[i];
         String actualItem = actualCommaList[i];
         // CommaListItemTest.logger.debug("Actual item {}: [{}]", i, actualItem);
         Assert.assertEquals(expectedItem, actualItem);
      }
   }
}