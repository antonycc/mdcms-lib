package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.polycode.mdcms.ops.ChildListItem;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class MdChildListItemTest {

   private final String localPath = MdChildListItemTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectEachItemInAListToBePresentInTheFileContentItem() throws ContentException {

      String[] expectedChildList = {
               "BasicSoleTraderProduct",
               "CompanyAccountsPayrollProduct",
               "CompanyAccountsProduct",
               "SelfEmployedPayrollProduct",
               "SelfEmployedProduct",
               "TaxiDriverProduct" };

      String childListContent = baseFileUrn + "test1/childlist.md";

      // instance under test "item" is initialised using constructor
      ChildListItem classUnderTest = new ChildListItem();
      classUnderTest.setPath(childListContent);
      classUnderTest.populateContent(new FileLikePathService());
      String[] actualChildList = classUnderTest.getProducts();

      // Check
      Assert.assertNotNull("Content item should have products", actualChildList);
      Assert.assertEquals("Content item should have expected products",
               expectedChildList.length,
               actualChildList.length);

      for (int i = 0; i < actualChildList.length; i++) {
         String expectedItem = expectedChildList[i];
         String actualItem = actualChildList[i];
         Assert.assertTrue("Each item should be as expected", expectedItem.equals(actualItem));
      }
   }

   @Test
   public void expectEachItemInAListToBePresentInTheContentItem() throws ContentException {

      String[] expectedChildList = {
            "BasicSoleTraderProduct",
            "CompanyAccountsPayrollProduct",
            "CompanyAccountsProduct",
            "SelfEmployedPayrollProduct",
            "SelfEmployedProduct",
            "TaxiDriverProduct" };

      String childListContent = baseClasspathUrn + "test1/childlist.md";

      // instance under test "item" is initialised using constructor
      ChildListItem classUnderTest = new ChildListItem();
      classUnderTest.setPath(childListContent);
      classUnderTest.populateContent(new FileLikePathService());
      String[] actualChildList = classUnderTest.getProducts();

      // Check
      Assert.assertNotNull("Content item should have products", actualChildList);
      Assert.assertEquals("Content item should have expected products",
            expectedChildList.length,
            actualChildList.length);

      for (int i = 0; i < actualChildList.length; i++) {
         String expectedItem = expectedChildList[i];
         String actualItem = actualChildList[i];
         Assert.assertTrue("Each item should be as expected", expectedItem.equals(actualItem));
      }
   }
}