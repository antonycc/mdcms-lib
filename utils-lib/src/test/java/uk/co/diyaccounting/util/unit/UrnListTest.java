package uk.co.diyaccounting.util.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.util.io.FileLikePathService;

import java.io.IOException;

public class UrnListTest {

   private final String basePath = FileLikePathServiceListTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private final String baseFilePath = basePath;
   private final String baseClasspath = "/";

   @Test
   public void expectPageFromFilePath() throws IOException {

      String[] expectedPageList = {"FirstPage", "TestPage", "LastPage"};
      String path = baseFilePath + "content/test/path";
      String filter = "Page.md";
      String urn = FileLikePathService.filePrefix + path;

      // instance under test "item" is initialised using constructor
      FileLikePathService classUnderTest = new FileLikePathService();
      String[] actualPageList = classUnderTest.getContentListForUrn(urn, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
               expectedPageList.length,
               actualPageList.length);

      /*
      ArrayList<TestMetadata> contentItems = new ArrayList<>();
      for(String contentName : actualPageList) {
         TestMetadata content = new TestMetadata();
         String contentPath = "urn:diyaccounting.co.uk:file://" + path + "/" + contentName + content.getExtension();
         content.setPath(contentPath);
         content.populateContent();
         contentItems.add(content);
      }
      Collections.sort(contentItems);

      for (int i = 0; i < expectedPageList.length; i++) {
         String expectedItem = expectedPageList[i];
         String actualItem = contentItems.get(i).getName();
         Assert.assertEquals("Each item should be as expected", expectedItem, actualItem);
      }
      */

   }

   @Test
   public void expectPageFromClasspath() throws IOException {

      String[] expectedPageList = {"FirstPage", "TestPage", "LastPage"};
      String path = baseClasspath + "content/test/path";
      String filter = "Page.md";
      FileLikePathService fileLikePathService = new FileLikePathService();
      String urn = FileLikePathService.classpathPrefix + path;

      // instance under test "item" is initialised using constructor
      FileLikePathService classUnderTest = new FileLikePathService();
      //String[] actualPageList = classUnderTest.getContentListForFilepath(path, filter);
      String[] actualPageList = classUnderTest.getContentListForUrn(urn, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
            expectedPageList.length,
            actualPageList.length);

      /*ArrayList<TestMetadata> contentItems = new ArrayList<>();
      for(String contentName : actualPageList) {
         TestMetadata content = new TestMetadata();
         String contentPath = "urn:diyaccounting.co.uk:classpath:" + path + "/" + contentName + content.getExtension();
         content.setPath(contentPath);
         content.populateContent();
         contentItems.add(content);
      }
      Collections.sort(contentItems);

      for (int i = 0; i < expectedPageList.length; i++) {
         String expectedItem = expectedPageList[i];
         String actualItem = contentItems.get(i).getName();
         Assert.assertEquals("Each item should be as expected", expectedItem, actualItem);
      }
      */
   }
}