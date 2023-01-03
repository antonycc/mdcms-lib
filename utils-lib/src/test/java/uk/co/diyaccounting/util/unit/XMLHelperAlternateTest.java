package uk.co.diyaccounting.util.unit;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.co.diyaccounting.util.xml.XMLException;
import uk.co.diyaccounting.util.xml.XMLHelper;

/**
 * Text XML helper alternate flows
 * 
 * @author Antony
 */
public class XMLHelperAlternateTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(XMLHelperAlternateTest.class);

   // Input Test data

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test XML Helper handles exceptions as expected
    */
   @Test
   public void testAndFailToLoadSimpleContentParserConfigurationException()
            throws ParserConfigurationException, SAXException, IOException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      DocumentBuilderFactory factory;
      InputSource is = null;
      EntityResolver er = null;
      Document document = null;

      // Create xml helper that fails to ceated a builder with a ParserConfigurationException
      factory = EasyMock.createMock(DocumentBuilderFactory.class);
      ParserConfigurationException pce = new ParserConfigurationException("Mock forced exception");
      EasyMock.expect(factory.newDocumentBuilder()).andThrow(pce);
      xmlHelper.setBuilderFactory(factory);
      EasyMock.replay(factory);

      // Expect exception from a failed parse
      try {
         document = xmlHelper.parseXMLDocument(is, er);
         Assert.fail("parseXMLDocument should have thrown an exception");
      } catch (XMLException xe) {
         Assert.assertNull("Document should have been null because of the SAXException", document);
      }

   }

   /**
    * Test XML Helper handles exceptions as expected
    */
   @Test
   public void testAndFailToLoadSimpleContentSAXException()
            throws ParserConfigurationException, SAXException, IOException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      DocumentBuilderFactory factory;
      InputSource is = null;
      EntityResolver er = null;
      Document document = null;

      // Create xml helper that fails to parse with a SAX Exception
      factory = EasyMock.createMock(DocumentBuilderFactory.class);
      DocumentBuilder builder = EasyMock.createNiceMock(DocumentBuilder.class);
      EasyMock.expect(factory.newDocumentBuilder()).andReturn(builder);
      SAXException se = new SAXException("Mock forced exception");
      EasyMock.expect(builder.parse(is)).andThrow(se);
      xmlHelper.setBuilderFactory(factory);
      EasyMock.replay(factory);
      EasyMock.replay(builder);

      // Expect exception from a failed parse
      try {
         document = xmlHelper.parseXMLDocument(is, er);
         Assert.fail("parseXMLDocument should have thrown an exception");
      } catch (XMLException xe) {
         Assert.assertNull("Document should have been null because of the SAXException", document);
      }
   }

   /**
    * Test XML Helper handles exceptions as expected
    */
   @Test
   public void testAndFailToLoadSimpleContentIOException()
            throws ParserConfigurationException, SAXException, IOException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      DocumentBuilderFactory factory;
      InputSource is = null;
      EntityResolver er = null;
      Document document = null;

      // Create xml helper that fails to parse with a SAX Exception
      factory = EasyMock.createMock(DocumentBuilderFactory.class);
      DocumentBuilder builder = EasyMock.createNiceMock(DocumentBuilder.class);
      EasyMock.expect(factory.newDocumentBuilder()).andReturn(builder);
      IOException ioe = new IOException("Mock forced exception");
      EasyMock.expect(builder.parse(is)).andThrow(ioe);
      xmlHelper.setBuilderFactory(factory);
      EasyMock.replay(factory);
      EasyMock.replay(builder);

      // Expect exception from a failed parse
      try {
         document = xmlHelper.parseXMLDocument(is, er);
         Assert.fail("parseXMLDocument should have thrown an exception");
      } catch (XMLException xe) {
         Assert.assertNull("Document should have been null because of the SAXException", document);
      }
   }

   /**
    * Test XML Helper handles parse misses as expected
    */
   @Test
   public void testAndEvaluateSimpleToNull() throws XMLException, XPathExpressionException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      Node sourceNode = null;
      String xpathQuery = "";

      XPath xpath = EasyMock.createMock(XPath.class);
      EasyMock.expect(xpath.evaluate(xpathQuery, sourceNode, XPathConstants.NODE)).andReturn(null);
      xmlHelper.setXpath(xpath);
      EasyMock.replay(xpath);

      // Expect null back after a failed query
      String result = xmlHelper.evaluateSimpleNodeContentsToString(sourceNode, xpathQuery, "");
      Assert.assertTrue("String should have been empty when query unmatched", result.length() == 0);
   }

   /**
    * Test XML Helper handles parse misses as expected
    */
   @Test
   public void testAndEvaluateXMLToNull() throws XMLException, XPathExpressionException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      Node sourceNode = null;
      String xpathQuery = "";

      XPath xpath = EasyMock.createMock(XPath.class);
      EasyMock.expect(xpath.evaluate(xpathQuery, sourceNode, XPathConstants.NODESET)).andReturn(null);
      xmlHelper.setXpath(xpath);
      EasyMock.replay(xpath);

      // Expect null back after a failed query
      String result = xmlHelper.evaluateXmlNodeContentsToString(sourceNode, xpathQuery, null);
      Assert.assertNull("String should have been null when query unmatched", result);
   }

   /**
    * Test XML Helper handles parse misses as expected
    */
   @Test
   public void testEvaluationHandlingXPathExpressionException() throws XPathExpressionException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      Node sourceNode = null;
      String xpathQuery = "";

      XPath xpath = EasyMock.createMock(XPath.class);
      XPathExpressionException xee = new XPathExpressionException("Mock forced exception");
      EasyMock.expect(xpath.evaluate(xpathQuery, sourceNode, XPathConstants.NODE)).andThrow(xee);
      EasyMock.expect(xpath.evaluate(xpathQuery, sourceNode, XPathConstants.NODESET)).andThrow(xee);
      xmlHelper.setXpath(xpath);
      EasyMock.replay(xpath);

      // Expect null back after a failed query
      Node nodeResult = null;
      try {
         nodeResult = xmlHelper.evaluate(sourceNode, xpathQuery);
         Assert.fail("An exception should have been thrown  during the evaluate function");
      } catch (XMLException e) {
         Assert.assertNull("Node should have been null when exception was thrown", nodeResult);
      }

      // Expect null back after a failed query
      NodeList nodeListResult = null;
      try {
         nodeListResult = xmlHelper.evaluateToNodeList(sourceNode, xpathQuery);
      } catch (XMLException e) {
         Assert.assertNull("Node List should have been null when exception was thrown", nodeListResult);
      }
   }

   /**
    * Test XML Helper handles parse misses as expected
    */
   @Test
   public void testPruningWithXPathExpressionException() throws XPathExpressionException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      Node sourceNode = null;
      String xpathQuery = "test";
      String[] xpathQueries = { xpathQuery };

      XPath xpath = EasyMock.createMock(XPath.class);
      XPathExpressionException xee = new XPathExpressionException("Mock forced exception");
      EasyMock.expect(xpath.evaluate(xpathQuery, sourceNode, XPathConstants.NODESET)).andThrow(xee);
      xmlHelper.setXpath(xpath);
      EasyMock.replay(xpath);

      // Expect null back after a failed query
      try {
         xmlHelper.prune(sourceNode, xpathQueries);
         Assert.fail("An exception should have been thrown during the evaluate function");
      } catch (XMLException e) {
      }

   }

   /**
    * Test XML Helper transformation exception handling
    * 
    * @throws TransformerConfigurationException
    */
   @Test
   public void testTransformerExceptionHandling() throws TransformerConfigurationException {

      // Instance to test
      XMLHelper xmlHelper = new XMLHelper();
      Node node = null;

      TransformerFactory transformerFactory = EasyMock.createMock(TransformerFactory.class);
      TransformerConfigurationException tce = new TransformerConfigurationException("Mock forced exception");
      EasyMock.expect(transformerFactory.newTransformer()).andThrow(tce);
      xmlHelper.setTransformerFactory(transformerFactory);
      EasyMock.replay(transformerFactory);

      String result = null;

      // Expect null back after a failed instantiation
      try {
         result = xmlHelper.elementToString(node);
         Assert.fail("An exception should have been thrown when converting to a string");
      } catch (XMLException e) {
         Assert.assertNull("String should have been null when exception was thrown", result);
      }
   }

}