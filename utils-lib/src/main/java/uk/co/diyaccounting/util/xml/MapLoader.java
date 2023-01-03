package uk.co.diyaccounting.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import uk.co.diyaccounting.util.io.FileLikePathService;
import uk.co.diyaccounting.util.io.UtilConstants;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Create a map from an XML based resource
 * 
 * @author Antony
 */
public final class MapLoader {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(MapLoader.class);

   /**
    * The XPath location of the content for the entry elements
    */
   public static final String ENTRY_XPATH = "/map/entry";

   /**
    * The XPath location of the content for the key
    */
   public static final String KEY_NAME = "key";

   /**
    * The XPath location of the content for the value
    */
   public static final String VALUE_NAME = "value";

   /**
    * The Xpath used by instances of the MapLoader
    */
   private XPath xpath;

   /**
    * The File Helper used by instances of the MapLoader
    */
   private FileLikePathService fileLikePathService = new FileLikePathService();

   /**
    * Create a new default instance of the Map Loader Normally the instance would be used directly
    */
   public MapLoader() {
      this.setXpath(XPathFactory.newInstance().newXPath());
   }

   /**
    * Load a map from a classpath resource The source document is expected to conform to local DTD map.dtd
    * 
    * @param resource
    *           - the name of the classpath resource to load
    * @return a map populated with the entries from the resource
    * @throws XMLException
    *            if the ,map cannot be create for any reason
    */
   public Map<String, String> load(final String resource) throws XMLException {
      MapLoader.logger.debug("Loading: {}", resource);
      Map<String, String> map = null;
      InputStream is = null;
      try {
         is = this.fileLikePathService.getResourceAsInputStream(resource);
         map = this.load(is);
         is.close();
         return map;
      } catch (IOException e) {
         String message = "Failed to load classpath resource using path: " + resource;
         throw new XMLException(message, e);
      }
   }

   /**
    * Load a map from an input stream The source document is expected to conform to local DTD map.dtd
    * 
    * @param is
    *           - the input stream to load the map from
    * @return a map populated with the entries from the stream
    * @throws XMLException
    *            if the ,map cannot be create for any reason
    */
   public Map<String, String> load(final InputStream is) throws XMLException {
      return this.load(is, UtilConstants.DEFAULT_ENCODING);
   }

   /**
    * Load a map from an input stream The source document is expected to conform to local DTD map.dtd
    * 
    * @param is
    *           - the input stream to load the map from
    * @param encoding the encoding to use
    * @return a map populated with the entries from the stream
    * @throws XMLException
    *            if the ,map cannot be create for any reason
    */
   public Map<String, String> load(final InputStream is, final String encoding) throws XMLException {
      try{
         return this.load(new InputStreamReader(is, encoding));
      }catch(UnsupportedEncodingException e){
         throw new IllegalArgumentException("Encoding [" + encoding + "] not supported", e);
      }
   }

   /**
    * Load a map from a reader The source document is expected to conform to local DTD map.dtd
    * 
    * @param r
    *           - the reader to load the map from
    * @return a map populated with the entries from the stream
    * @throws XMLException
    *            if the ,map cannot be create for any reason
    */
   public Map<String, String> load(final Reader r) throws XMLException {

      // Load the document
      InputSource is = new InputSource(r);
      XMLHelper xmlHelper = new XMLHelper();
      Document document = xmlHelper.parseXMLDocument(is);

      // Get the entries
      HashMap<String, String> map = new HashMap<String, String>();
      NodeList entries;
      entries = this.getEntries(document);
      MapLoader.logger.debug("Found {} nodes.", entries.getLength());

      // Check each entry
      for (int i = 0; i < entries.getLength(); i++) {
         Node node = entries.item(i);
         NodeList entryChildren = node.getChildNodes();

         // Search the children for the key and value
         String key = null;
         String value = null;
         for (int j = 0; j < entryChildren.getLength(); j++) {
            Node entryChild = entryChildren.item(j);
            if (MapLoader.KEY_NAME.equals(entryChild.getNodeName())) {
               key = entryChild.getTextContent();
            }
            if (MapLoader.VALUE_NAME.equals(entryChild.getNodeName())) {
               value = entryChild.getTextContent();
            }
         }

         // Populate the map
         MapLoader.logger.debug("Adding [{}]: {}", key, value);
         map.put(key, value);
      }

      return map;
   }

   /**
    * Read a document and evaluate an XPath query to obtain the map entries
    * 
    * @param document
    *           - the XML document to load the map from
    * @return a list of nodes from the document
    * @throws XMLException
    *            if the entries cannot be read from the document
    */
   public NodeList getEntries(final Document document) throws XMLException {

      // Extract the entry elements
      NodeList entries = null;
      try {
         entries = (NodeList) this.xpath.evaluate(MapLoader.ENTRY_XPATH, document, XPathConstants.NODESET);
      } catch (XPathExpressionException e) {
         throw new XMLException("Could not look up entries in map using XPath expression: " + MapLoader.ENTRY_XPATH, e);
      }

      return entries;
   }

   /**
    * Change the XPath resolver instance to the specified one Added to allow mocking of the XPath implementation
    * 
    * @param xpath
    *           the new instance of the XPath to use
    */
   public void setXpath(final XPath xpath) {
      this.xpath = xpath;
   }

   /**
    * Change the HashHelper instance to the specified one Added to allow mocking of the Stream retrieval
    * 
    * @param fileLikePathService
    *           the new instance of the HashHelper
    */
   public void setFileHelper(final FileLikePathService fileLikePathService) {
      this.fileLikePathService = fileLikePathService;
   }
}
