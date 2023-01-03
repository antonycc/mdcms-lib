package uk.co.diyaccounting.util.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.util.UriUtils;
import uk.co.diyaccounting.util.net.UrlHelper;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class UrlHelperTest {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(UrlHelperTest.class);

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test base name extraction
    */
   @Test
   public void testGetBaseName() {

      // Set up values
      String path = "a/b";

      // Expected values
      String expectedPath = "a/";

      // Test path extracted
      String actualPath = new UrlHelper().getPath(path);
      Assert.assertEquals("Expected to path to be extracted", expectedPath, actualPath);

      // Test path already extracted
      String actualPath2 = new UrlHelper().getPath(actualPath);
      Assert.assertEquals("Expected to path to be extracted", expectedPath, actualPath2);
   }

   /**
    * Test base name extraction
    */
   @Test
   public void testGetBaseNameNoPath() {

      // Expected values
      String expectedPath = "a";

      // Test path extracted
      // Test path already extracted
      String actualPath = new UrlHelper().getPath(expectedPath);
      Assert.assertEquals("Expected to path to be extracted", expectedPath, actualPath);
   }

   /**
    * Test url generation and parsing back to same
    * 
    * @throws UnsupportedEncodingException
    */
   @Test
   public void testQueryParameters() throws UnsupportedEncodingException {

      // Setup values
      String exportPath = "b";
      String expectedContentType = "c";
      String path = "/content/test/path/AboutPage.html";
      String basePath = new UrlHelper().getPath(path);
      String encoding = "UTF-8";
      String expectedImageUrl = "urn:diyaccounting.co.uk:classpath:/" + basePath + exportPath;

      // Expected values
      StringBuilder buf = new StringBuilder();
      buf.append("contentType=");
      buf.append(UriUtils.encodeQueryParam(expectedContentType, encoding));
      buf.append("&");
      buf.append("url=");
      buf.append(UriUtils.encodeQueryParam(expectedImageUrl, encoding));
      String expectedQueryString = buf.toString();

      // Create Object to test
      Map<String, List<String>> queryParams = new UrlHelper().createQueryParams();
      new UrlHelper().addParams(queryParams, "url", expectedImageUrl);
      new UrlHelper().addParams(queryParams, "contentType", expectedContentType);

      // extract the query string
      String actualQueryString = new UrlHelper().buildQueryString(queryParams);
      Assert.assertEquals("URL should be as expected", expectedQueryString, actualQueryString);
      String expectedUrl = "http://127.0.0.1/?" + actualQueryString;

      // Generate the query params from the generated URL
      Map<String, List<String>> actualQueryParams = new UrlHelper().parseQueryParams(expectedUrl);
      List<String> urls = actualQueryParams.get("url");
      Assert.assertNotNull(urls);
      Assert.assertTrue("There should be just one url value", urls.size() == 1);
      String actualImageUrl = urls.get(0);
      Assert.assertEquals("Expecting to find url put in", expectedImageUrl, actualImageUrl);
      List<String> contentTypes = actualQueryParams.get("contentType");
      Assert.assertNotNull(contentTypes);
      Assert.assertTrue("There should be just one contentType value", contentTypes.size() == 1);
      String actualContenType = contentTypes.get(0);
      Assert.assertEquals("Expecting to find contentType put in", expectedContentType, actualContenType);
   }

   /**
    * Test url generation and parsing back to same
    * 
    * @throws UnsupportedEncodingException
    */
   @Test
   public void testQueryParametersComplexParse() throws UnsupportedEncodingException {

      // Setup values

      String expectedName1 = "b";
      String expectedName2 = "c";
      String expectedName2Value1 = "d";
      String expectedName2Value2 = "e";

      // Expected values

      // Create Object to test
      String url = "http://a?" + expectedName1 + "&" + expectedName2 + "=" + expectedName2Value1 + "&" + expectedName2
               + "=" + expectedName2Value2;

      // Generate the query params from the generated URL
      Map<String, List<String>> actualQueryParams = new UrlHelper().parseQueryParams(url);

      // First parameter should be present but with an empty value list
      List<String> actualName1Values = actualQueryParams.get(expectedName1);
      Assert.assertNotNull(actualName1Values);
      Assert.assertTrue("There should be no values", actualName1Values.size() == 0);

      // Second parameter should have 2 values
      List<String> actualName2Values = actualQueryParams.get(expectedName2);
      Assert.assertNotNull(actualName2Values);
      Assert.assertTrue("There should be two values", actualName2Values.size() == 2);
      String actualName2Value1 = actualName2Values.get(0);
      Assert.assertEquals("Expect values inserted", expectedName2Value1, actualName2Value1);
      String actualName2Value2 = actualName2Values.get(1);
      Assert.assertEquals("Expect values inserted", expectedName2Value2, actualName2Value2);

      // Made up parameter should have null value lists
      List<String> actualFakeValues = actualQueryParams.get("actualFakeValues");
      Assert.assertNull("value list should be null", actualFakeValues);
   }

   /**
    * Test url generation and parsing back to same
    * 
    * @throws UnsupportedEncodingException
    */
   @Test
   public void testNoQueryParameters() {

      // Setup values
      // Expected values

      // Create Object to test
      String url = "http://a";

      // Generate the query params from the generated URL
      Map<String, List<String>> actualQueryParams = new UrlHelper().parseQueryParams(url);
      Assert.assertNotNull(actualQueryParams);
      Assert.assertNotNull(actualQueryParams.keySet());
      Set<String> actualKeys = actualQueryParams.keySet();
      // UrlHelperTest.logger.debug("actualKeys: " + actualKeys.toString());
      Assert.assertTrue("key set should be empty", actualKeys.size() == 0);
      Assert.assertNotNull(actualQueryParams.values());
      Assert.assertTrue("key set should be empty", actualQueryParams.values().isEmpty());
   }

   /**
    * Test url generation and parsing back to same
    * 
    * @throws UnsupportedEncodingException
    */
   @Test
   public void testOnlyNullQueryParameters() throws UnsupportedEncodingException {

      // Setup values
      String expectedContentType = "c";
      String encoding = "UTF-8";
      String expectedImageUrl = null;

      // Expected values
      StringBuilder buf = new StringBuilder();
      buf.append("contentType=");
      buf.append(UriUtils.encodeQueryParam(expectedContentType, encoding));
      String expectedQueryString = buf.toString();

      // Create Object to test
      Map<String, List<String>> queryParams = new UrlHelper().createQueryParams();
      new UrlHelper().addParams(queryParams, "url", expectedImageUrl);
      new UrlHelper().addParams(queryParams, "contentType", expectedContentType);

      // extract the query string
      String actualQueryString = new UrlHelper().buildQueryString(queryParams);
      Assert.assertEquals("URL should be as expected", expectedQueryString, actualQueryString);
   }

   /**
    * Test url generation expecting unsupported encoding exception
    * 
    * @throws UnsupportedEncodingException
    */
   @Test(expected = IllegalArgumentException.class)
   public void testQueryParametersWithUnsuppotedEncoding() {

      // Setup values
      String expectedContentType = "c";
      String expectedEncoding = "no.such.encoding";

      // Create Object to test
      UrlHelper classUnderTest = new UrlHelper();

      // Test
      classUnderTest.setEncoding(expectedEncoding);
      Map<String, List<String>> queryParams = classUnderTest.createQueryParams();
      classUnderTest.addParams(queryParams, "contentType", expectedContentType);
      classUnderTest.buildQueryString(queryParams);
   }

   /**
    * Test url generation expecting unsupported encoding exception
    * 
    * @throws UnsupportedEncodingException
    */
   @Test(expected = IllegalArgumentException.class)
   public void testQueryStringWithUnsuppotedEncoding() {

      // Setup values
      String expectedUrl = "http://127.0.1/?a=b%20c";
      String expectedEncoding = "no.such.encoding";

      // Create Object to test
      UrlHelper classUnderTest = new UrlHelper();

      // Test
      classUnderTest.setEncoding(expectedEncoding);
      classUnderTest.parseQueryParams(expectedUrl);
   }

   /**
    * Test url generation expecting unsupported encoding exception
    * Forced because the Spring 5 method signature decalares a checked exception:
    * but throws UnsupportedCharsetException
    *
    * @throws UnsupportedEncodingException
    */
   @Test(expected = IllegalArgumentException.class)
   public void testQueryParametersWithUnsuppotedEncodingForced() {

      // Setup values
      String expectedContentType = "c";
      String expectedEncoding = "no.such.encoding";
      boolean forceUnsupportedEncodingException = true;

      // Create Object to test
      UrlHelper classUnderTest = new UrlHelper();

      // Test
      classUnderTest.setEncoding(expectedEncoding);
      classUnderTest.setForceUnsupportedEncodingException(forceUnsupportedEncodingException);
      Map<String, List<String>> queryParams = classUnderTest.createQueryParams();
      classUnderTest.addParams(queryParams, "contentType", expectedContentType);
      classUnderTest.buildQueryString(queryParams);
   }

   /**
    * Test url generation expecting unsupported encoding exception
    * Forced because the Spring 5 method signature decalares a checked exception:
    * but throws UnsupportedCharsetException
    *
    * @throws UnsupportedEncodingException
    */
   @Test(expected = IllegalArgumentException.class)
   public void testQueryStringWithUnsuppotedEncodingForced() {

      // Setup values
      String expectedUrl = "http://127.0.1/?a=b%20c";
      String expectedEncoding = "no.such.encoding";
      boolean forceUnsupportedEncodingException = true;

      // Create Object to test
      UrlHelper classUnderTest = new UrlHelper();

      // Test
      classUnderTest.setEncoding(expectedEncoding);
      classUnderTest.setForceUnsupportedEncodingException(forceUnsupportedEncodingException);
      classUnderTest.parseQueryParams(expectedUrl);
   }
}