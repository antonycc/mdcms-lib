package uk.co.diyaccounting.cms.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.xml.sax.InputSource;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.cms.ops.ChildListItem;
import uk.co.diyaccounting.util.io.FileLikePathService;
import uk.co.diyaccounting.util.xml.XMLException;
import uk.co.diyaccounting.util.xml.XMLHelper;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class ChildListItemTest {

   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectEachItemInAListToBePresentInTheContentItem() throws ContentException {

      String[] expectedChildList = {
               "BasicSoleTraderProduct",
               "CompanyAccountsPayrollProduct",
               "CompanyAccountsProduct",
               "SelfEmployedPayrollProduct",
               "SelfEmployedProduct",
               "TaxiDriverProduct" };

      String childListContent = baseClasspathUrn + "test1/childlist.html";

      // instance under test "item" is initialised using constructor
      ChildListItem classUnderTest = new ChildListItem();
      classUnderTest.setPath(childListContent);
      classUnderTest.populateContent(new FileLikePathService());
      String[] actualChildList = classUnderTest.getProducts();

      // Check
      Assert.assertNotNull("Content item should have products", actualChildList);
      Assert.assertEquals("Content item should have expected products",
               expectedChildList.length,
               actualChildList.length);

      for (int i = 0; i < actualChildList.length; i++) {
         String expectedItem = expectedChildList[i];
         String actualItem = actualChildList[i];
         Assert.assertTrue("Each item should be as expected", expectedItem.equals(actualItem));
      }
   }

   @Test(expected = ContentException.class)
   public void expectContentExceptionWhenAnXMLExceptionOccurs() throws ContentException, XMLException {

      // Expected values
      String expectedMessage = "mock forced exception";

      // Set up variables
      String childListContent = baseClasspathUrn + "test1/childlist.html";

      // Create mocked XML Helper that throws XMLExceptions
      XMLHelper mockXMLHelper = EasyMock.createNiceMock(XMLHelper.class);
	   Document document = EasyMock.createNiceMock(Document.class);
	   EasyMock.expect(mockXMLHelper.parseXMLDocument(EasyMock.anyObject(InputSource.class))).andReturn(document);
      XMLException xe = new XMLException(expectedMessage);
      EasyMock.expect(
               mockXMLHelper.evaluateSimpleNodeListContentsToStringArray(
                        EasyMock.anyObject(Node.class),
                        EasyMock.anyObject(String.class))).andThrow(xe);
      EasyMock.replay(document, mockXMLHelper);

      // Create Object to test
      ChildListItem classUnderTest = new ChildListItem();
      classUnderTest.setPath(childListContent);

      // Execute and check
      classUnderTest.setXmlHelper(mockXMLHelper);
      classUnderTest.populateContent(new FileLikePathService());
   }
}