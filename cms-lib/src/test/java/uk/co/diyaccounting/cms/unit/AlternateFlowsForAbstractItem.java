package uk.co.diyaccounting.cms.unit;

import org.easymock.EasyMock;
import org.junit.Test;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import uk.co.diyaccounting.cms.ops.IncompatibleFieldItem;
import uk.co.diyaccounting.cms.ops.NameBodyItem;
import uk.co.diyaccounting.cms.ops.UrlItem;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.util.io.FileLikePathService;
import uk.co.diyaccounting.util.xml.XMLException;
import uk.co.diyaccounting.util.xml.XMLHelper;

import java.io.InputStream;

/**
 * Reads a content item
 *
 * @author Antony
 */
public class AlternateFlowsForAbstractItem {

   private final String localPath = AlternateFlowsForAbstractItem.class.getProtectionDomain().getCodeSource().getLocation().getPath();
   private final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
   private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test(expected = ContentException.class)
   public void expectExceptionFromNullSource() throws ContentException {

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.readAttributes(null);
   }

   @Test(expected = ContentException.class)
   public void expectExceptionWhereFieldDoesNotHaveMatchingConstructor() throws ContentException {

      String path = baseFileUrn + "test1/url.html";

      // instance under test "item" is initialised using constructor
      IncompatibleFieldItem classUnderTest = new IncompatibleFieldItem();
      classUnderTest.setPath(path);
      classUnderTest.populateContent(new FileLikePathService());
   }

   @Test(expected = ContentException.class)
   public void expectExceptionFromNonExistentItem() throws ContentException {

      String path = baseFileUrn + "test1/badurl.html";

      // instance under test "item" is initialised using constructor
      UrlItem classUnderTest = new UrlItem();
      classUnderTest.setPath(path);
      classUnderTest.populateContent(new FileLikePathService());
   }

   @Test(expected = ContentException.class)
   public void expectExceptionFromNonExistentItemClasspath() throws ContentException {

      String path = baseClasspathUrn + "test1/badurl.html";

      // instance under test "item" is initialised using constructor
      UrlItem classUnderTest = new UrlItem();
      classUnderTest.setPath(path);
      classUnderTest.populateContent(new FileLikePathService());
   }

   @Test(expected = ContentException.class)
   public void expectFailureToParseXmlDocResutsInWrappedException() throws ContentException, XMLException {

      // Test parameters
      String contentItemXhtml = baseFileUrn + "test1/simple.html";

      // Expected results

      // Read content into an Input Source
      InputStream is = FileLikePathService.class.getResourceAsStream(contentItemXhtml);
      InputSource inputSource = new InputSource(is);

      // Mock the XMLHelper to throw an exception
      XMLHelper mockHelper = EasyMock.createNiceMock(XMLHelper.class);
      XMLException xe = new XMLException("Mock forced exception");
      EasyMock.expect(
               mockHelper.parseXMLDocument((InputSource) EasyMock.anyObject(), (EntityResolver) EasyMock.anyObject()))
               .andThrow(xe);
      EasyMock.replay(mockHelper);

      // Create Object to test
      NameBodyItem classUnderTest = new NameBodyItem();
      classUnderTest.setXmlHelper(mockHelper);
      classUnderTest.readAttributes(inputSource);
   }

}