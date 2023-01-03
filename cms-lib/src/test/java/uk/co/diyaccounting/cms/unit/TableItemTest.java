package uk.co.diyaccounting.cms.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.cms.ops.TableItem;
import uk.co.diyaccounting.util.io.FileLikePathService;
import uk.co.diyaccounting.util.xml.XMLException;
import uk.co.diyaccounting.util.xml.XMLHelper;

import java.util.HashMap;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class TableItemTest {

   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectTableContentToPopulateAHashMapofHashMaps() throws ContentException {

      // Test parameters
      String tableContent = baseClasspathUrn + "test1/table.html";

      // Expected values
      int expectedEntries = 9;
      String expectedKey = "basic.htm";
      String expectedHeader1 = "Destination";
      String expectedValue1 = "BasicSoleTraderProduct";
      String expectedHeader2 = "Action";
      String expectedValue2 = "forward";

      // instance under test "item" is initialised using constructor
      TableItem classUnderTest = new TableItem();
      classUnderTest.setPath(tableContent);
      classUnderTest.populateContent(new FileLikePathService());
      HashMap<String, HashMap<String, String>> actualTable = classUnderTest.getTable();

      // Check
      Assert.assertNotNull("Table should not be null", actualTable);
      Assert.assertEquals("Table should have expected number of items",
               expectedEntries,
               actualTable.size());
      HashMap<String, String> actualRow = actualTable.get(expectedKey);
      Assert.assertNotNull("Row should not be null", actualRow);

      String actualValue1 = actualRow.get(expectedHeader1);
      Assert.assertNotNull("Destination should not be null", actualValue1);
      Assert.assertEquals("Destination should be as expected", expectedValue1, actualValue1);

      String actualValue2 = actualRow.get(expectedHeader2);
      Assert.assertNotNull("Action should not be null", actualValue2);
      Assert.assertEquals("Action should be as expected", expectedValue2, actualValue2);

   }

   @Test(expected = ContentException.class)
   public void expectAContentExceptionWhenEncounteringAnXMLException() throws ContentException, XMLException {

      // Test parameters
      String tableContent = baseClasspathUrn + "test1/table.html";

      // Expected values
      String expectedMessage = "mock forced exception";

      // Create mocked XML Helper that throws XMLExceptions
      XMLHelper mockXMLHelper = EasyMock.createNiceMock(XMLHelper.class);
	   Document document = EasyMock.createNiceMock(Document.class);
	   EasyMock.expect(mockXMLHelper.parseXMLDocument(EasyMock.anyObject(InputSource.class))).andReturn(document);
      XMLException xe = new XMLException(expectedMessage);
      EasyMock.expect(
               mockXMLHelper.evaluate(
                        EasyMock.anyObject(Node.class),
                        EasyMock.anyObject(String.class))).andThrow(xe);
      EasyMock.replay(document, mockXMLHelper);

      // Create Object to test
      TableItem classUnderTest = new TableItem();
      classUnderTest.setPath(tableContent);

      // Execute and check
      classUnderTest.setXmlHelper(mockXMLHelper);
      classUnderTest.populateContent(new FileLikePathService());
   }
}