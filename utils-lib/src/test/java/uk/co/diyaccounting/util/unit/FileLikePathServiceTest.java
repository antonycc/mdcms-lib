package uk.co.diyaccounting.util.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.services.s3.S3Client;
import uk.co.diyaccounting.util.io.FileLikePathService;
import uk.co.diyaccounting.util.io.FileLikeStreamAndListFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.UUID;

public class FileLikePathServiceTest {

   private static final Logger logger = LoggerFactory.getLogger(FileLikePathServiceTest.class);

   // Input Test data
   //private String simpleResource = "/resource.txt";
   private String fileResource = "target/test-classes/path1/file.txt";

   private String fileResourceLocation = "target/test-classes/path1";

   private String classpathResource = "/test1/resource.txt";

   private String httpResource = "https://example.com/test1/resource.txt";

   private String s3Resource = "test1/resource.txt";

   // Expected values - undefined values set in Setup
   private String expectedSimpleResource = "Simple resource";

   private String expectedClasspathResource = "Classpath resource";

   private String expectedHttpResource = "Http resource";

   private String expectedS3Resource = "S3 resource";

   private String NoSutchFile = "afile";

   private String productContentBasePath = "urn:diyaccounting.co.uk:bucket:s3://content/";

   @Before
   public void setUp() {
   }

   @Test
   public void testStaticInstance() throws IOException {

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Read content into an Input Source
      Reader r = classUnderTest.loadFileFromClasspath(this.classpathResource);
      BufferedReader br = new BufferedReader(r);
      String line;

      // Read the resource into a list parsing each line
      String resource = null;
      StringBuilder buf = new StringBuilder();
      while ((line = br.readLine()) != null) {
         buf.append(line);
      }
      resource = buf.toString();
      FileLikePathServiceTest.logger.debug("Read: {}", resource);

      // Check
      Assert.assertEquals(this.expectedClasspathResource, resource);
   }

   @Test
   public void testSimpleContent() throws IOException {

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Read content into an Input Source
      Reader r = classUnderTest.loadFileFromClasspath(this.classpathResource);
      BufferedReader br = new BufferedReader(r);
      String line;

      // Read the resource into a list parsing each line
      String resource = null;
      StringBuilder buf = new StringBuilder();
      while ((line = br.readLine()) != null) {
         buf.append(line);
      }
      resource = buf.toString();
      FileLikePathServiceTest.logger.debug("Read: {}", resource);

      // Check
      Assert.assertEquals(this.expectedClasspathResource, resource);
   }

   @Test(expected = IOException.class)
   public void testAndFailToLoadSimpleContent() throws IOException {

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Create file a helper instance that returns a broken input stream
      FileLikeStreamAndListFactory streamFactory = EasyMock.createMock(FileLikeStreamAndListFactory.class);
      EasyMock.expect(streamFactory.getResourceAsInputStream(this.fileResource)).andReturn(null);
      classUnderTest.setStreamFactory(streamFactory);
      EasyMock.replay(streamFactory);

      // Read content into an Input Source
      classUnderTest.loadFileFromClasspath(this.fileResource);
   }

   @Test
   public void testAndLoadSimpleContentFromFile() throws IOException {

      logger.debug(System.getProperty("user.dir"));

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Create file a helper instance that returns a broken input stream
      //FileLikeStreamAndListFactory streamFactory = EasyMock.createMock(FileLikeStreamAndListFactory.class);
      //EasyMock.expect(streamFactory.getFileAsInputStream(this.simpleResource)).andReturn(null);
      //classUnderTest.setStreamFactory(streamFactory);
      //EasyMock.replay(streamFactory);

      // Execute
      classUnderTest.getFileAsInputStream(this.fileResource);
   }

   @Test
   public void testAndWriteSimpleContentFromFile() throws IOException {

      // Test parameters
      String objectKey = UUID.randomUUID().toString() + ".txt";
      String fileContents = UUID.randomUUID().toString();
      String resource = this.fileResourceLocation + File.separator + objectKey;

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Execute
      OutputStream os = classUnderTest.getFileAsOutputStream(resource);
      OutputStreamWriter osw = new OutputStreamWriter(os);
      PrintWriter pw = new PrintWriter(osw);
      pw.println(fileContents);
      pw.close();
   }

   @Test
   public void testHostExtraction() throws IOException {

      // Test parameters
      String objectKey = UUID.randomUUID().toString() + ".txt";
      String fileContents = UUID.randomUUID().toString();
      String resource = this.productContentBasePath + objectKey;
      String expectedHost = "content";

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Execute
      String actualHost = classUnderTest.getHostForUrn(resource);
      Assert.assertEquals(expectedHost, actualHost);

   }

   @Test
   public void testClasspathContent() throws IOException {

      FileLikePathService classUnderTest = new FileLikePathService();

      // Read content into an Input Source
      Reader r = classUnderTest.loadFileFromClasspath(this.classpathResource);
      BufferedReader br = new BufferedReader(r);
      String line;

      // Read the resource into a list parsing each line
      String resource = null;
      StringBuilder buf = new StringBuilder();
      while ((line = br.readLine()) != null) {
         buf.append(line);
      }
      resource = buf.toString();
      FileLikePathServiceTest.logger.debug("Read: {}", resource);

      // Check
      Assert.assertEquals(this.expectedClasspathResource, resource);
   }

   @Test
   public void testFileExistence() throws IOException {

      FileLikePathService classUnderTest = new FileLikePathService();

      Assert.assertFalse("Should not exist: " + this.NoSutchFile, classUnderTest.exists(this.NoSutchFile));
   }

   @Test
   public void testFileExistenceExceptions() throws IOException {

      FileLikePathService classUnderTest = new FileLikePathService();
      Boolean exists;
      File file;

      // Create file that throws a SecurityException
      file = EasyMock.createMock(File.class);
      SecurityException se = new SecurityException();
      EasyMock.expect(file.exists()).andThrow(se);
      EasyMock.expect(file.getPath()).andReturn(this.NoSutchFile);
      EasyMock.replay(file);

      // Check existence is false
      exists = classUnderTest.exists(file);
      Assert.assertFalse("SecurityException forces existance to be false", exists);

      // Create file that throws Throwable
      file = EasyMock.createMock(File.class);
      NullPointerException npe = new NullPointerException();
      EasyMock.expect(file.exists()).andThrow(npe);
      EasyMock.expect(file.getPath()).andReturn(this.NoSutchFile);
      EasyMock.replay(file);

      // Check existence is false
      exists = classUnderTest.exists(file);
      Assert.assertFalse("Throwable forces existance to be false", exists);
   }

   @Test
   public void testHttpObject() throws IOException {

      // Test parameters
      InputStream is = new ByteArrayInputStream(this.expectedHttpResource.getBytes());

      // Create file a helper instance that returns a broken input stream
      FileLikeStreamAndListFactory streamFactory = EasyMock.createMock(FileLikeStreamAndListFactory.class);
      EasyMock.expect(streamFactory.getHttpObjectAsInputStream(this.httpResource)).andReturn(is);
      EasyMock.replay(streamFactory);

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();
      classUnderTest.setStreamFactory(streamFactory);

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
      FileLikePathServiceTest.logger.debug("Read: {}", resource);

      // Check
      Assert.assertEquals(this.expectedHttpResource, resource);
   }

   @Test(expected = IOException.class)
   public void testAndFailToLoadHttpObject() throws IOException {

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();

      // Create file a helper instance that returns a broken input stream
      FileLikeStreamAndListFactory streamFactory = EasyMock.createMock(FileLikeStreamAndListFactory.class);
      EasyMock.expect(streamFactory.getHttpObjectAsInputStream(this.httpResource)).andReturn(null);
      classUnderTest.setStreamFactory(streamFactory);
      EasyMock.replay(streamFactory);

      // Read content into an Input Source
      classUnderTest.getHttpObjectAsInputStream(this.httpResource);
   }

   @Test
   public void testS3Object() throws IOException {

      // Test parameters
      InputStream is = new ByteArrayInputStream(this.expectedS3Resource.getBytes());

      // Create file a helper instance that returns a broken input stream
      S3Client amazonS3 = EasyMock.createNiceMock(S3Client.class);
      FileLikeStreamAndListFactory streamFactory = EasyMock.createNiceMock(FileLikeStreamAndListFactory.class);
      EasyMock.expect(streamFactory.getS3ObjectAsInputStream(amazonS3, this.s3Resource)).andReturn(is).anyTimes();
      EasyMock.replay(amazonS3, streamFactory);

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();
      classUnderTest.bucketSupported = true;
      classUnderTest.setAmazonS3Client(amazonS3);
      classUnderTest.setStreamFactory(streamFactory);

      // Read content into an Input Source
      InputStream actualIS = classUnderTest.getS3ObjectAsInputStream(this.s3Resource);
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
      FileLikePathServiceTest.logger.debug("Read: {}", resource);

      // Check
      Assert.assertEquals(this.expectedS3Resource, resource);
   }

   @Test(expected = IOException.class)
   public void testAndFailToLoadS3Object() throws IOException {

      // Create file a helper instance that returns a broken input stream
      S3Client amazonS3 = EasyMock.createNiceMock(S3Client.class);
      FileLikeStreamAndListFactory streamFactory = EasyMock.createNiceMock(FileLikeStreamAndListFactory.class);
      EasyMock.expect(streamFactory.getS3ObjectAsInputStream(null, this.s3Resource)).andReturn(null).anyTimes();
      EasyMock.replay(amazonS3, streamFactory);

      // Instance to test
      FileLikePathService classUnderTest = new FileLikePathService();
      classUnderTest.bucketSupported = true;
      classUnderTest.setAmazonS3Client(amazonS3);
      classUnderTest.setStreamFactory(streamFactory);

      // Read content into an Input Source
      classUnderTest.getS3ObjectAsInputStream(this.s3Resource);
   }

}