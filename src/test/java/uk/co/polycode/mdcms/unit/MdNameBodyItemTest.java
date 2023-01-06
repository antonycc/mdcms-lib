package uk.co.polycode.mdcms.unit;

import org.apache.commons.io.IOUtils;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import uk.co.polycode.mdcms.ops.NameBodyItem;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.io.UtilConstants;
import uk.co.polycode.mdcms.util.xml.XMLException;
import uk.co.polycode.mdcms.util.xml.XMLHelper;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Random;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class MdNameBodyItemTest {

   private final String localPath = MdNameBodyItemTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectDefaultAttributeValuesToBeNull() {

      // Create Object to test
      NameBodyItem item = new NameBodyItem();

      // Check
      Assert.assertNull(item.getName());
      Assert.assertNull(item.getBody());
   }

   @Test
   public void expectAccessorsAndMutatorsToChangeTheSameAttributes() {

      // Expected values - undefined values set in Setup
      Random generator = new Random();
      byte[] random = new byte[32];
      generator.nextBytes(random);
      String expectedRandomName = new String(random);
      generator.nextBytes(random);
      String expectedRandomBody = new String(random);

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.setName(expectedRandomName);
      classUnderTest.setBody(expectedRandomBody);

      // Check
      Assert.assertTrue(expectedRandomName.equals(classUnderTest.getName()));
      Assert.assertTrue(expectedRandomBody.equals(classUnderTest.getBody()));
   }

   @Test
   public void expectNameFileContentReadFromMarkdownToBeAsExpected() throws ContentException, IOException {

      // Test parameters
      String contentItemMd = baseFileUrn + "test1/simple.md";

      // Expected results
      String expectedReadName = "simple";

      // Read content into a String
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemMd);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.md");
      StringWriter writer = new StringWriter();
      IOUtils.copy(is, writer, UtilConstants.DEFAULT_ENCODING);
      String mdDocument= writer.toString();

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readMdAttributes(contentItemMd, mdDocument);

      // Check
      Assert.assertEquals(expectedReadName, classUnderTest.getName());
   }

   @Test
   public void expectNameContentReadFromMarkdownToBeAsExpected() throws ContentException, IOException {

      // Test parameters
      String contentItemMd = baseClasspathUrn + "test1/simple.md";

      // Expected results
      String expectedReadName = "simple";

      // Read content into a String
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemMd);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.md");
      StringWriter writer = new StringWriter();
      IOUtils.copy(is, writer, UtilConstants.DEFAULT_ENCODING);
      String mdDocument= writer.toString();

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readMdAttributes(contentItemMd, mdDocument);

      // Check
      Assert.assertEquals(expectedReadName, classUnderTest.getName());
   }

   @Test
   public void expectFileReadingTwiceToBeTolerated() throws ContentException, IOException {

      // Test parameters
      String contentItemMd = baseFileUrn + "test1/simple.md";

      // Read content into an Input Source
      // Read content into a String
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemMd);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.md");
      StringWriter writer = new StringWriter();
      IOUtils.copy(is, writer, UtilConstants.DEFAULT_ENCODING);
      String mdDocument= writer.toString();

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readMdAttributes(contentItemMd, mdDocument);

      // Read content into an Input Source and parse again
      classUnderTest.readMdAttributes(contentItemMd, mdDocument);
   }

   @Test
   public void expectReadingTwiceToBeTolerated() throws ContentException, IOException {

      // Test parameters
      String contentItemMd = baseClasspathUrn + "test1/simple.md";

      // Read content into an Input Source
      // Read content into a String
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemMd);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.md");
      StringWriter writer = new StringWriter();
      IOUtils.copy(is, writer, UtilConstants.DEFAULT_ENCODING);
      String mdDocument= writer.toString();

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readMdAttributes(contentItemMd, mdDocument);

      // Read content into an Input Source and parse again
      classUnderTest.readMdAttributes(contentItemMd, mdDocument);
   }

   @Test
   public void expectBodyContentReadFromMarkdownToBeAsExpected() throws ContentException, IOException {

      // Test parameters
      String localPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      String contentItemMd = baseClasspathUrn + "test1/simple.md";

      // Expected results
      String expectedReadBody = "<p>A simple piece of content.</p>" +
               System.lineSeparator() +
               "<p>The whole body is used as a single block of content.</p>";

      // Read content into an Input Source
      // Read content into a String
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemMd);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.md");
      StringWriter writer = new StringWriter();
      IOUtils.copy(is, writer, UtilConstants.DEFAULT_ENCODING);
      String mdDocument= writer.toString();

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readMdAttributes(contentItemMd, mdDocument);

      // Check
      String expectedBody = expectedReadBody;
      String actualBody = classUnderTest.getBody().trim();
      Assert.assertEquals(expectedBody, actualBody);
   }

   @Test
   public void expectContentReadFromMarkdownToBeAsExpectedWithSpecificResolver() throws ContentException,
            IOException {

      // Test parameters
      String localPath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      String contentItemMd = baseClasspathUrn + "test1/simple.md";

      // Expected results
      String expectedReadName = "simple";
      String expectedReadBody = "<p>A simple piece of content.</p>" +
               System.lineSeparator() +
               "<p>The whole body is used as a single block of content.</p>";

      // Read content into an Input Source
      // Read content into a String
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemMd);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.md");
      StringWriter writer = new StringWriter();
      IOUtils.copy(is, writer, UtilConstants.DEFAULT_ENCODING);
      String mdDocument= writer.toString();

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readMdAttributes(contentItemMd, mdDocument);

      // Check
      Assert.assertEquals(expectedReadName, classUnderTest.getName());

      String expectedBody = expectedReadBody;
      String actualBody = classUnderTest.getBody().trim();
      Assert.assertEquals(expectedBody, actualBody);
   }

   @Test(expected = ContentException.class)
   public void expectContentExceptionwhenReadingXmlElement() throws ContentException,
            XMLException {

      // Expected values
      String expectedMessage = "mock forced exception";

      // Set up variables
      String basePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      String content = baseClasspathUrn + "test1/simple.md";
      InputStream is = FileLikePathService.class.getResourceAsStream(content);
      InputSource inputSource = new InputSource(is);

      // Create mocked XML Helper that throws XMLExceptions
      XMLHelper mockXMLHelper = EasyMock.createNiceMock(XMLHelper.class);
	   Document document = EasyMock.createNiceMock(Document.class);
	   EasyMock.expect(mockXMLHelper.parseXMLDocument(EasyMock.anyObject(InputSource.class))).andReturn(document);
      XMLException xe = new XMLException(expectedMessage);
      EasyMock.expect(
               mockXMLHelper.evaluateXmlNodeContentsToString(
                        EasyMock.anyObject(Node.class),
                        EasyMock.anyObject(String.class),
                        EasyMock.anyObject(String.class))).andThrow(xe);
      EasyMock.replay(document, mockXMLHelper);

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
	   classUnderTest.setXmlHelper(mockXMLHelper);

      // Execute and check
      classUnderTest.readAttributes(inputSource);
   }

   @Test(expected = ContentException.class)
   public void expectContentExceptionwhenReadingStringElement() throws ContentException,
            XMLException {

      // Expected values
      String expectedMessage = "mock forced exception";

      // Set up variables
      String basePath = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
      String content = "urn:diyaccounting.co.uk:file://" + basePath + "test1/simple.md";
      InputStream is = FileLikePathService.class.getResourceAsStream(basePath + "test1/simple.md");
      InputSource inputSource = new InputSource(is);

      // Create mocked XML Helper that throws XMLExceptions
	   XMLHelper mockXMLHelper = EasyMock.createMock(XMLHelper.class);
      XMLException xe = new XMLException(expectedMessage);
	   Document document = EasyMock.createNiceMock(Document.class);
	   EasyMock.expect(mockXMLHelper.parseXMLDocument(EasyMock.anyObject(InputSource.class))).andReturn(document);
      EasyMock.expect(
               mockXMLHelper.evaluateXmlNodeContentsToString(
                        EasyMock.anyObject(Node.class),
                        EasyMock.anyObject(String.class),
                        EasyMock.anyObject(String.class))).andReturn("");
      EasyMock.expect(
               mockXMLHelper.evaluateSimpleNodeContentsToString(
                        EasyMock.anyObject(Node.class),
                        EasyMock.anyObject(String.class),
                        EasyMock.anyObject(String.class))).andThrow(xe);
      EasyMock.replay(document, mockXMLHelper);

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();

      // Execute and check
      classUnderTest.setXmlHelper(mockXMLHelper);
      classUnderTest.readAttributes(inputSource);
   }
}