package uk.co.polycode.mdcms.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import uk.co.polycode.mdcms.ops.NameBodyItem;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.xml.XMLException;
import uk.co.polycode.mdcms.util.xml.XMLHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class NameBodyItemTest {

   private final String localPath = NameBodyItem.class.getProtectionDomain().getCodeSource().getLocation().getPath();
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
   public void expectNameContentReadFromXHTMLToBeAsExpected() throws ContentException, IOException {

      // Test parameters
      String contentItemXhtml = baseFileUrn + "test1/simple.html";

      // Expected results
      String expectedReadName = "simple";

      // Read content into an Input Source
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemXhtml);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.html");
      InputSource inputSource = new InputSource(is);

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readAttributes(inputSource);

      // Check
      Assert.assertEquals(expectedReadName, classUnderTest.getName());
   }

   @Test
   public void expectReadingTwiceToBeTolerated() throws ContentException, IOException {

      // Test parameters
      String contentItemXhtml = baseFileUrn + "test1/simple.html";

      // Read content into an Input Source
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemXhtml);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.html");
      InputSource inputSource = new InputSource(is);

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readAttributes(inputSource);

      // Read content into an Input Source and parse again
      //is = FileHelper.class.getResourceAsStream(contentItemXhtml);
      is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.html");
      inputSource = new InputSource(is);
      classUnderTest.readAttributes(inputSource);
   }

   @Test
   public void expectBodyContentReadFromXHTMLToBeAsExpected() throws ContentException, IOException {

      // Test parameters
      String contentItemXhtml = baseFileUrn + "test1/simple.html";

      // Expected results
      String expectedReadBody = "<p>A simple piece of content.</p>" +
               System.lineSeparator() +
               "<p>The whole body is used as a single block of content.</p>";

      // Read content into an Input Source
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemXhtml);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.html");
      InputSource inputSource = new InputSource(is);

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readAttributes(inputSource);

      // Check
      String expectedBody = expectedReadBody;
      String actualBody = classUnderTest.getBody().trim();
      Assert.assertEquals(expectedBody, actualBody);
   }

   @Test
   public void expectContentReadFromXHTMLToBeAsExpectedWithSpecificResolver() throws ContentException,
            XMLException,
            IOException {

      // Test parameters
      String contentItemXhtml = baseFileUrn + "test1/simple.html";

      // Expected results
      String expectedReadName = "simple";
      String expectedReadBody = "<p>A simple piece of content.</p>" +
               System.lineSeparator() +
               "<p>The whole body is used as a single block of content.</p>";

      // Read content into an Input Source
      //InputStream is = FileHelper.class.getResourceAsStream(contentItemXhtml);
      InputStream is = new FileLikePathService().getFileAsInputStream(localPath + "test1/simple.html");

      // Create Object to test
      InputSource inputSource = new InputSource(is);
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readAttributes(inputSource);

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
      String content = baseClasspathUrn + "test1/simple.html";
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
      String content = baseClasspathUrn + "test1/simple.html";
      InputStream is = FileLikePathService.class.getResourceAsStream(content);
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