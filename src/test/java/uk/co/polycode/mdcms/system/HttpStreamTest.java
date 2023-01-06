package uk.co.polycode.mdcms.system;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.UUID;

public class HttpStreamTest {

   private static final Logger logger = LoggerFactory.getLogger(HttpStreamTest.class);

   // Use Docker nginx endpoint, created with:
   // docker compose build
   // docker compose up

   // Input Test data
   private String httpResource = "http://localhost:8081/resource.txt";

   // Expected values - undefined values set in Setup
   private String expectedHttpResource = "HTTP resource";

   @Test
   public void testHttpObject() throws IOException {

      // Test parameters
      InputStream is = new ByteArrayInputStream(this.expectedHttpResource.getBytes());

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Read content into an Input Source
      InputStream actualIS = classUnderTest.getHttpObjectAsInputStream(this.httpResource);
      Reader r = new InputStreamReader(actualIS);
      BufferedReader br = new BufferedReader(r);
      String line;

      // Read the resource into a list parsing each line
      String resource = null;
      StringBuilder buf = new StringBuilder();
      while ((line = br.readLine()) != null) {
         buf.append(line);
      }
      resource = buf.toString();
      HttpStreamTest.logger.debug("Read: {}", resource);

      // Check
      Assert.assertEquals(this.expectedHttpResource, resource);
   }

   @Test(expected = IOException.class)
   public void testAndFailToLoadHttpObject() throws IOException {

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Read content into an Input Source
      classUnderTest.getHttpObjectAsInputStream(this.httpResource + UUID.randomUUID().toString());
   }

   @Test
   public void expectPagesFromUrl() throws IOException {

      String resourcePath = "urn:diyaccounting.co.uk:url:http://localhost:8090/content/";
      String filter = "Page.md";
      String url = "http://localhost:8090/content/";
      int expectedPageListLength = 12;

      FileLikePathService classUnderTest = new FileLikePathService();

      // Execute
      String[] actualPageList = classUnderTest.getContentListForUrn(resourcePath, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
              expectedPageListLength,
              actualPageList.length);
   }

   @Test
   public void expectProductsFromUrn() throws IOException {

      String resourcePath = "urn:diyaccounting.co.uk:url:http://localhost:8090/content/";
      String filter = "Product.md";
      String url = "http://localhost:8090/content/";
      int expectedPageListLength = 6;

      FileLikePathService classUnderTest = new FileLikePathService();

      // Execute
      String[] actualPageList = classUnderTest.getContentListForUrn(resourcePath, filter);

      // Check
      Assert.assertNotNull("Content item should have products", actualPageList);
      Assert.assertEquals("Content item should have expected products",
              expectedPageListLength,
              actualPageList.length);
   }
}