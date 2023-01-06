package uk.co.polycode.mdcms.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.xml.XMLException;
import uk.co.polycode.mdcms.util.xml.XMLHelper;

import java.io.IOException;
import java.io.InputStream;

/**
 * Text XML helper alternate flows
 * 
 * @author Antony
 */
public class XMLHelperTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(XMLHelperTest.class);

   // Input Test data

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test Correct node type toString methods are called
    * 
    * @throws IOException
    */
   @Test
   public void testParseEmptyDocNoDocType() throws XMLException, IOException {

      // Expected results
      String expectedContents = "test";

      // Set up variables
      String documentToTest = "/nodtd.xml";
      InputStream is = new FileLikePathService().getResourceAsInputStream(documentToTest);
      InputSource source = new InputSource(is);

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();

      // Execute and check object
      Document doc = xmlHelper.parseXMLDocument(source);
      Assert.assertNotNull("document should have been parsed", doc);
      // nodeContentsToString(doc)
      String actualContents = xmlHelper.evaluateSimpleNodeContentsToString(doc, "*", "");
      Assert.assertEquals("Expected specific body in document", expectedContents, actualContents);
   }

   /**
    * Test Correct node type toString methods are called
    * 
    * @throws IOException
    */
   @Test
   public void testParseChildListWithNoDocType() throws XMLException, IOException {

      // Expected results
      String[] expectedContents = { "two", "three" };

      // Set up variables
      String documentToTest = "/childrennodtd.xml";
      InputStream is = new FileLikePathService().getResourceAsInputStream(documentToTest);
      InputSource source = new InputSource(is);

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();

      // Execute and check object
      Document doc = xmlHelper.parseXMLDocument(source);
      Assert.assertNotNull("document should have been parsed", doc);
      // nodeContentsToString(doc)
      String[] actualContents = xmlHelper.evaluateSimpleNodeListContentsToStringArray(doc, "*/child");
      Assert.assertEquals("Expected same number of items", expectedContents.length, actualContents.length);
      for (int i = 0; i < expectedContents.length; i++) {
         Assert.assertEquals("Expected specific body in document", expectedContents[i], actualContents[i]);
      }
   }

   /**
    * Test Correct node type toString methods are called
    * 
    * @throws IOException
    */
   @Test
   public void testParseXmlDoc() throws XMLException, IOException {

      // Expected results
      String expectedContents = "<child>two</child>" + System.lineSeparator() + "<child>three</child>";

      // Set up variables
      String documentToTest = "/childrennodtd.xml";
      InputStream is = new FileLikePathService().getResourceAsInputStream(documentToTest);
      InputSource source = new InputSource(is);

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();

      // Execute and check object
      Document doc = xmlHelper.parseXMLDocument(source);
      Assert.assertNotNull("document should have been parsed", doc);
      // nodeContentsToString(doc)
      String actualContents = xmlHelper.evaluateXmlNodeContentsToString(doc, "/root/child", "");
      Assert.assertNotNull("text should be present", actualContents);
      Assert.assertEquals("Expected specific body in document", expectedContents, actualContents.trim());
   }

   /**
    * Test Correct node type toString methods are called
    * 
    * @throws IOException
    */
   @Test
   public void testParseComplexDocWithPrune() throws XMLException, IOException {

      // Expected results
      String expectedContents = "twothree";

      // Set up variables
      String documentToTest = "/childrennodtd.xml";
      InputStream is = new FileLikePathService().getResourceAsInputStream(documentToTest);
      InputSource source = new InputSource(is);

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();

      // Execute and check object
      Document doc = xmlHelper.parseXMLDocument(source);
      Assert.assertNotNull("document should have been parsed", doc);
      String actualContents = xmlHelper.evaluateSimpleNodeContentsToString(doc, "*", "", "/root/child1");
      Assert.assertNotNull("text should be present", actualContents);
      Assert.assertEquals("Expected specific body in document", expectedContents, actualContents.trim());
   }

   /**
    * Test Correct node type toString methods are called
    */
   @Test
   public void testNodeContentsToString() throws XMLException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      String expectedResult = "expected";

      Node node = EasyMock.createMock(Node.class);
      EasyMock.expect(node.getNodeType()).andReturn(Node.TEXT_NODE);
      EasyMock.expect(node.getTextContent()).andReturn(expectedResult);
      EasyMock.expect(node.getNodeType()).andReturn(Node.ELEMENT_NODE);
      EasyMock.replay(node);

      // execute with sequence of events tested from the mocked
      String actualResult = xmlHelper.nodeContentsToString(node);
      Assert.assertEquals("Expeced mocked response", expectedResult, actualResult);
   }

}