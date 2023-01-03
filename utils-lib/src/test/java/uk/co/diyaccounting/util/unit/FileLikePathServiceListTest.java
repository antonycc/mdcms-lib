package uk.co.diyaccounting.util.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import software.amazon.awssdk.services.s3.S3Client;
import uk.co.diyaccounting.util.io.FileLikePathService;
import uk.co.diyaccounting.util.io.FileLikeStreamAndListFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;

public class FileLikePathServiceListTest {

   private final String basePath = FileLikePathServiceListTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private final String baseFilePath = basePath;
   private final String baseClasspath = "/";

   String directoryListPayload = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 3.2 Final//EN\">\n" +
         "<html>\n" +
         " <head>\n" +
         "  <title>Index of /sub</title>\n" +
         " </head>\n" +
         " <body>\n" +
         "<h1>Index of /sub</h1>\n" +
         "<ul><li><a href=\"/\"> Parent Directory</a></li>\n" +
         "<li><a href=\"1.txt\"> 1.txt</a></li>\n" +
         "<li><a href=\"2.txt\"> 2.txt</a></li>\n" +
         "</ul>\n" +
         "</body></html>";

   @Test
   public void expectPagesFromFilePath() {

      String[] expectedPageList = {"FirstPage", "TestPage", "LastPage"};
      String path = baseFilePath + "items";
      String filter = "Page.md";

      // instance under test "item" is initialised using constructor
      FileLikePathService classUnderTest = new FileLikePathService();
      String[] actualPageList = classUnderTest.getContentListForFilepath(path, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
               expectedPageList.length,
               actualPageList.length);
   }

   @Test
   public void expectPagesFromClasspath() {

      String[] expectedPageList = {"FirstPage", "TestPage", "LastPage"};
      String path = baseClasspath + "items";
      String filter = "Page.md";

      // instance under test "item" is initialised using constructor
      FileLikePathService classUnderTest = new FileLikePathService();
      String[] actualPageList = classUnderTest.getContentListForClasspath(path, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
            expectedPageList.length,
            actualPageList.length);
   }

   @Test
   public void expectPagesFromUrl() throws IOException {

      String[] expectedPageList = {"1.txt", "2.txt"};
      String url = "http://localhost:8081/sub/";
      String filter = ".txt";

      // Test parameters
      InputStream is = new ByteArrayInputStream(this.directoryListPayload.getBytes());

      // Create file a helper instance that returns a broken input stream
      FileLikeStreamAndListFactory streamFactory = EasyMock.createNiceMock(FileLikeStreamAndListFactory.class);
      EasyMock.expect(streamFactory.getHttpObjectAsInputStream(url)).andReturn(is).anyTimes();
      EasyMock.replay(streamFactory);

      // instance under test "item" is initialised using constructor
      FileLikePathService classUnderTest = new FileLikePathService();
      classUnderTest.setStreamFactory(streamFactory);
      String[] actualPageList = classUnderTest.getContentListForUrl(url, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
            expectedPageList.length,
            actualPageList.length);
   }

   @Test
   public void expectPagesFromBucket() {

      String[] expectedPageList = {"s3resource.txt", "s3resource2.txt"};
      String bucket = "bucket";
      String filter = ".txt";

      // Test parameters

      // Create file a helper instance that returns a broken input stream
      S3Client amazonS3 = EasyMock.createNiceMock(S3Client.class);
      FileLikeStreamAndListFactory streamFactory = EasyMock.createNiceMock(FileLikeStreamAndListFactory.class);
      ArrayList<String> objectKeys = new ArrayList<>();
      Collections.addAll(objectKeys, expectedPageList);
      EasyMock.expect(streamFactory.getObjectKeysForBucket(amazonS3, bucket, filter)).andReturn(objectKeys).anyTimes();
      EasyMock.replay(amazonS3, streamFactory);

      // instance under test "item" is initialised using constructor
      FileLikePathService classUnderTest = new FileLikePathService();
      classUnderTest.bucketSupported = true;
      classUnderTest.setAmazonS3Client(amazonS3);
      classUnderTest.setStreamFactory(streamFactory);
      String[] actualPageList = classUnderTest.getContentListForBucket(bucket, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
            expectedPageList.length,
            actualPageList.length);
   }
}