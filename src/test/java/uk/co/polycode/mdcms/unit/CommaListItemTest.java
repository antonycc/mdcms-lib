package uk.co.polycode.mdcms.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import org.xml.sax.InputSource;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.ops.CommaListItem;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.xml.XMLException;
import uk.co.polycode.mdcms.util.xml.XMLHelper;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class CommaListItemTest {

   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CommaListItemTest.class);

   @Test
   public void expectCommaListReadIntoStringArray() throws ContentException {

      String[] expectedCommaList = {
               "ProfitAndLoss",
               "TestFeatureOne",
               "TestFeatureTwo" };

      String commaListContent = baseClasspathUrn + "test1/commalist.html";

      // instance under test "item" is initialised using constructor
      CommaListItem classUnderTest = new CommaListItem();
      classUnderTest.setPath(commaListContent);
      classUnderTest.populateContent(new FileLikePathService());
      String[] actualCommaList = classUnderTest.getFeatures();

      // Check
      Assert.assertNotNull("Content item should have features", actualCommaList);
      Assert.assertEquals("Content item should have expected features",
               expectedCommaList.length,
               actualCommaList.length);

      for (int i = 0; i < actualCommaList.length; i++) {
         String expectedItem = expectedCommaList[i];
         String actualItem = actualCommaList[i];
         // CommaListItemTest.logger.debug("Actual item {}: [{}]", i, actualItem);
         Assert.assertEquals(expectedItem, actualItem);
      }
   }

   @Test(expected = ContentException.class)
   public void expectContentExceptionWhenReadingContentAndEncounteringAnXMLException() throws ContentException,
            XMLException {

      // Expected values
      String expectedMessage = "mock forced exception";

      // Set up variables
      String commaListContent = baseClasspathUrn + "test1/commalist.html";

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
      CommaListItem classUnderTest = new CommaListItem();
      classUnderTest.setPath(commaListContent);
	   classUnderTest.setXmlHelper(mockXMLHelper);

      // Execute and check
      classUnderTest.populateContent(new FileLikePathService());
   }

}