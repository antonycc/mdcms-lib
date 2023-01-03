package uk.co.diyaccounting.util.unit;

import org.apache.commons.io.input.BrokenInputStream;
import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import uk.co.diyaccounting.util.io.FileLikePathService;
import uk.co.diyaccounting.util.xml.MapLoader;
import uk.co.diyaccounting.util.xml.XMLException;
import uk.co.diyaccounting.util.xml.XMLHelper;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * Tests cache behaviour
 * 
 * @author Antony
 */
public class MapLoaderTest {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(MapLoaderTest.class);

   // The pre-load file
   private static String empty = "/empty.xml";

   private static String preload = "/cache.xml";

   // The entries
   private static String key1 = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";

   private static String key2 = "http://www.w3.org/TR/xhtml1/DTD/xhtml-lat1.ent";

   private static String key3 = "http://www.w3.org/TR/xhtml1/DTD/xhtml-special.ent";

   private static String key4 = "http://www.w3.org/TR/xhtml1/DTD/xhtml-symbol.ent";

   /**
    * Set up
    */
   @Before
   public void setUp() {
   }

   /**
    * Test a non-existent value in the map
    */
   @Test
   public void testMiss() {

      // Instance to test
      MapLoader mapLoader = new MapLoader();

      // Load the map
      Map<String, String> map = null;
      try {
         map = mapLoader.load(MapLoaderTest.empty);
      } catch (XMLException e) {
         String msg = "Could not load " + MapLoaderTest.empty;
         MapLoaderTest.logger.error(msg, e);
         Assert.fail(msg);
      }

      // get an entry
      String value = map.get("miss");

      // Test
      Assert.assertNull(value);
   }

   /**
    * Test a resource reader that throws an exception
    * @throws XMLException
    */
   @Test(expected = XMLException.class)
   public void testXMLException() throws XMLException {

      // Instance to test
      MapLoader mapLoader = new MapLoader();

      // Prime this method: fileHelper.evaluate(MapLoader.ENTRY_XPATH, document, XPathConstants.NODESET).;
      // to throw: XPathExpressionException
      FileLikePathService fileLikePathService = EasyMock.createMock(FileLikePathService.class);
      BrokenInputStream bis = new BrokenInputStream();
      try {
         EasyMock.expect(fileLikePathService.getResourceAsInputStream(MapLoaderTest.empty)).andReturn(bis);
      } catch (IOException e) {
         Assert.fail("Exception not expected to be thrown until mock is replayed");
      }
      mapLoader.setFileHelper(fileLikePathService);
      EasyMock.replay(fileLikePathService);

      // Try to load the map but encounter an exception
      mapLoader.load(MapLoaderTest.empty);
   }

   /**
    * Test a resource reader that throws a runtime exception
    * @throws XMLException
    */
   @Test(expected = IllegalArgumentException.class)
   public void testUnsupportedEncoding() throws XMLException {

      // Instance to test
      MapLoader mapLoader = new MapLoader();

      // Input parameters
      ByteArrayInputStream bais = new ByteArrayInputStream("test".getBytes());;

      // Try to load the map but encounter an exception
      mapLoader.load(bais, MapLoaderTest.class.getSimpleName() + ".no.such.encoding");
   }

   /**
    * Test a resource reader that throws an exception
    * @throws XMLException
    */
   @Test(expected = XMLException.class)
   public void testIOException() throws IOException, XMLException {

      // Instance to test
      MapLoader mapLoader = new MapLoader();

      // Prime this method: fileHelper.evaluate(MapLoader.ENTRY_XPATH, document, XPathConstants.NODESET).;
      // to throw: XPathExpressionException
      FileLikePathService fileLikePathService = EasyMock.createMock(FileLikePathService.class);
      String expectedMessage = "mock forced exception";
      IOException expectedException = new IOException(expectedMessage);
      EasyMock.expect(fileLikePathService.getResourceAsInputStream(MapLoaderTest.empty)).andThrow(expectedException);
      mapLoader.setFileHelper(fileLikePathService);
      EasyMock.replay(fileLikePathService);

      // Try to load the map but encounter an exception
      mapLoader.load(MapLoaderTest.empty);
   }

   /**
    * Test value we expect to exist in the map
    */
   @Test
   public void testHits() throws XMLException {

      // Instance to test
      MapLoader mapLoader = new MapLoader();

      // Load the map
      Map<String, String> map = null;
      map = mapLoader.load(MapLoaderTest.preload);

      // get an entry the test it
      String value1 = map.get(MapLoaderTest.key1);
      Assert.assertNotNull(value1);

      // get an entry the test it
      String value2 = map.get(MapLoaderTest.key2);
      Assert.assertNotNull(value2);

      // get an entry the test it
      String value3 = map.get(MapLoaderTest.key3);
      Assert.assertNotNull(value3);

      // get an entry the test it
      String value4 = map.get(MapLoaderTest.key4);
      Assert.assertNotNull(value4);
   }

   /**
    * Test Correct node type toString methods are called
    * 
    * @throws IOException
    */
   @Test
   public void testGetEntriesAndThrowExceptionWhenParingXPath() throws XMLException, IOException {

      // Expected results
      String expectedExceptionMessage = "Could not look up entries in map using XPath expression: /map/entry";

      // Set up variables
      String documentToTest = "/entities.xml";
      InputStream is = new FileLikePathService().getResourceAsInputStream(documentToTest);
      InputSource source = new InputSource(is);
      Document document = new XMLHelper().parseXMLDocument(source);

      // Prime this method: xpath.evaluate(MapLoader.ENTRY_XPATH, document, XPathConstants.NODESET).;
      // to throw: XPathExpressionException
      XPath xpath = EasyMock.createMock(XPath.class);
      try {
         EasyMock.expect(xpath.evaluate(MapLoader.ENTRY_XPATH, document, XPathConstants.NODESET)).andThrow(
                  new XPathExpressionException("mock"));
      } catch (XPathExpressionException e) {
         Assert.fail("Exception not expected to be thrown until mock is replayed");
      }
      EasyMock.replay(xpath);

      // Instance to test
      MapLoader mapLoader = new MapLoader();
      mapLoader.setXpath(xpath);

      // Execute and check object
      try {
         mapLoader.getEntries(document);
         Assert.fail("An XML Exception should have been thrown");
      } catch (XMLException e) {
         Assert.assertEquals("Expected exception message", expectedExceptionMessage, e.getMessage());
      }

   }
}