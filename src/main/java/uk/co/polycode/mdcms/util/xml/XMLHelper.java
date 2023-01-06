package uk.co.polycode.mdcms.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.ErrorListener;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.io.StringWriter;

/**
 * An XML helper library
 * 
 * @author Antony
 */
public class XMLHelper {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(XMLHelper.class);

   /**
    * The factory for all document builder creation
    */
   private DocumentBuilderFactory builderFactory;

   /**
    * The xpath for all parsing
    */
   private XPath xpath;

   /**
    * Transformers, robots in disguise!
    */
   private TransformerFactory transformerFactory;

   /**
    * Create a new default instance of the XML Helper. Normally the instance would be used directly
    */
   public XMLHelper() {
      DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
      // builderFactory.setExpandEntityReferences(true);
      // builderFactory.setValidating(true);

      builderFactory.setIgnoringComments(true);
      builderFactory.setIgnoringElementContentWhitespace(true);
      builderFactory.setXIncludeAware(false);

      builderFactory.setExpandEntityReferences(false);
      builderFactory.setValidating(false);
      this.setBuilderFactory(builderFactory);
      TransformerFactory transformerFactory = TransformerFactory.newInstance();
      ErrorListener listener = new XMLErrorListener();
      transformerFactory.setErrorListener(listener);
      this.setTransformerFactory(transformerFactory);
      XPath xpath = XPathFactory.newInstance().newXPath();
      this.setXpath(xpath);
   }

   /**
    * Get the factory
    * 
    * @return the factory
    */
   public DocumentBuilderFactory getBuilderFactory() {
      return this.builderFactory;
   }

   /**
    * Set the factory
    * 
    * @param builderFactory
    *           the factory to set
    */
   public void setBuilderFactory(final DocumentBuilderFactory builderFactory) {
      this.builderFactory = builderFactory;
   }

   /**
    * Get the XPath
    * 
    * @return the xpath
    */
   public XPath getXpath() {
      return this.xpath;
   }

   /**
    * Set the XPath
    * 
    * @param xpath
    *           the xpath to set
    */
   public void setXpath(final XPath xpath) {
      this.xpath = xpath;
   }

   /**
    * Get the transformer
    * 
    * @return the transformer
    */
   public TransformerFactory getTransformerFactory() {
      return this.transformerFactory;
   }

   /**
    * Set the transformer
    * 
    * @param transformerFactory
    *           the transformer to set
    */
   public void setTransformerFactory(final TransformerFactory transformerFactory) {
      this.transformerFactory = transformerFactory;
   }

   /**
    * Read the InputSource into a an XML Document The document is parsed while reading
    * 
    * @param is
    *           - the InputSource to read
    * 
    * @returns an XML Document
    */
   public Document parseXMLDocument(final InputSource is) throws XMLException {
      //return this.parseXMLDocument(is, new ClasspathEntityResolver());
	   return this.parseXMLDocument(is, new NonValidatingEntityResolver());
   }

   /**
    * Read the InputSource into a an XML Document The document is parsed while reading
    * 
    * @param is
    *           - the InputSource to read
    * @param er
    *           - the entity resolver to use for any entities in the document
    * 
    * @returns an XML Document
    */
   public Document parseXMLDocument(final InputSource is, final EntityResolver er) throws XMLException {

      // Create a W3C Document parser
      Document document = null;
      try {
         // Create the builder
         //System.setProperty("jaxp.debug","1");
         //this.outputJaxpImplementationInfo();
         DocumentBuilder builder = this.getBuilderFactory().newDocumentBuilder();
         if (er != null) {
            builder.setEntityResolver(er);
         }
         XMLErrorHandler errorHandler = new XMLErrorHandler();
         builder.setErrorHandler(errorHandler);

         // Parse the content type into a document
         try {
            document = builder.parse(is);
         } catch (SAXException e) {
            throw new XMLException("Could not parse (using SAX): " + e.getClass().getSimpleName(), e);
         } catch (IOException e) {
            throw new XMLException("Could not read InputSource: " + e.getClass().getSimpleName(), e);
         }

      } catch (ParserConfigurationException e) {
         throw new XMLException("Could not configure XML parser: " + e.getClass().getSimpleName(), e);
      }

      return document;
   }

/*
   private void outputJaxpImplementationInfo() {
      logger.debug(this.getJaxpImplementationInfo("DocumentBuilderFactory", DocumentBuilderFactory.newInstance().getClass()));
      logger.debug(this.getJaxpImplementationInfo("XPathFactory", XPathFactory.newInstance().getClass()));
      logger.debug(this.getJaxpImplementationInfo("TransformerFactory", TransformerFactory.newInstance().getClass()));
      logger.debug(this.getJaxpImplementationInfo("SAXParserFactory", SAXParserFactory.newInstance().getClass()));
   }

   private String getJaxpImplementationInfo(String componentName, Class componentClass) {
      //CodeSource source = componentClass.getProtectionDomain().getCodeSource();
      return MessageFormat.format(
            "{0} implementation: {1} loaded from: {2}",
            componentName,
            componentClass.getName(),
//            source == null ? "Java Runtime" : source.getLocation());
            "Java Runtime");
//            source.getLocation());
   }
*/

   /**
    * Read a text node and return it as the result
    * 
    * @param sourceNode
    *           - the Node to evaluate
    * @param xpathQuery
    *           - the query to execute
    * 
    * @return the value read from the node (or null)
    */
   public String evaluateSimpleNodeContentsToString(
            final Node sourceNode,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) throws XMLException {

      // Prune before parsing
      this.prune(sourceNode, prune);

      //logger.debug("xpathQuery: " + xpathQuery);
      //logger.debug("sourceNode: " + this.elementToString(sourceNode));

      Node node = this.evaluate(sourceNode, xpathQuery);
      String value = defaultValue;
      if (node != null) {
         value = this.textNodeToString(node);
      }
      XMLHelper.logger.debug("Evaluted: \"" + xpathQuery + "\" to \"" + value + "\"");
      return value;
   }

   /**
    * Read an element and return it as the result
    * 
    * @param sourceNode
    *           - the Node to evaluate
    * @param xpathQuery
    *           - the query to execute
    * 
    * @return the value read from the node (or null)
    */
   public String evaluateXmlNodeContentsToString(
            final Node sourceNode,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) throws XMLException {

      // Prune before parsing
      this.prune(sourceNode, prune);

      //logger.debug("xpathQuery: " + xpathQuery);
      //logger.debug("sourceNode: " + this.elementToString(sourceNode));

      NodeList nodes = this.evaluateToNodeList(sourceNode, xpathQuery);
      String value = defaultValue;
      if (nodes != null) {
         NodeEnumeration e = new NodeEnumeration(nodes);
         StringBuilder buf = new StringBuilder();
         while (e.hasMoreElements()) {
            Node node = e.nextElement();
            String text = this.nodeContentsToString(node);
            buf.append(text);
         }
         value = buf.toString();
      }

      return value;
   }

   /**
    * Read an element and return it as the result
    * 
    * @param node
    *           - the Node to evaluate
    * 
    * @return the value read from the node (or null)
    */
   public String nodeContentsToString(final Node node) throws XMLException {

      String s = null;
      if (node.getNodeType() == Node.TEXT_NODE) {
         XMLHelper.logger.debug("TEXT_NODE");
         s = this.textNodeToString(node);
      } else {
         s = this.elementToString(node);
      }

      return s;
   }

   /**
    * Evaluate an XPath query return the node as the result
    * 
    * @param sourceNode
    *           - the Node to evaluate
    * @param xpathQuery
    *           - the query to execute
    * 
    * @return the note (or null)
    */
   public Node evaluate(final Node sourceNode, final String xpathQuery) throws XMLException {
      // Read field using XPath
      Node node = null;
      try {
         node = (Node) this.getXpath().evaluate(xpathQuery, sourceNode, XPathConstants.NODE);
      } catch (XPathExpressionException e) {
         throw new XMLException("Could not resolve: " + xpathQuery, e);
      }
      return node;
   }

   /**
    * Read a list of tect node and return them as a string array
    * 
    * @param sourceNode
    *           - the Node to evaluate
    * @param xpathQuery
    *           - the query to execute
    * 
    * @return the value read from the node (or null)
    */
   public String[] evaluateSimpleNodeListContentsToStringArray(
            final Node sourceNode,
            final String xpathQuery,
            final String... prune) throws XMLException {

      // Prune before parsing
      this.prune(sourceNode, prune);

      // Obtain a list of items as nodes
      NodeList nodeList = this.evaluateToNodeList(sourceNode, xpathQuery);
      int length = nodeList.getLength();
      String[] list = new String[length];

      // Iterate through each item converting it to a string
      for (int i = 0; i < length; i++) {
         Node node = nodeList.item(i);
         list[i] = this.textNodeToString(node).trim();
      }

      return list;
   }

   /**
    * Evaluate an XPath query return the node list as the result
    * 
    * @param sourceNode
    *           - the Node to evaluate
    * @param xpathQuery
    *           - the query to execute
    * 
    * @return the note (or null)
    */
   public NodeList evaluateToNodeList(final Node sourceNode, final String xpathQuery) throws XMLException {
      // Read field using XPath
      NodeList node = null;
      try {
         node = (NodeList) this.getXpath().evaluate(xpathQuery, sourceNode, XPathConstants.NODESET);
      } catch (XPathExpressionException e) {
         throw new XMLException("Could not resolve: " + xpathQuery, e);
      }
      return node;
   }

   /**
    * Prune elements from a node using an XPath query
    * 
    * @param sourceNode
    *           - the Node to pune
    * @param xpathQueries
    *           - the queries to execute
    * 
    * @throws XMLException
    *            if the evaluate query throws an XPathExpressionException
    */
   public void prune(final Node sourceNode, final String... xpathQueries) throws XMLException {

      // Execute each query pruning any children returned
      for (String query : xpathQueries) {
         XMLHelper.logger.debug("pruning using {}", query);

         // Obtain the list of children to prune
         try {
            NodeList childrenToPrune = (NodeList) this.getXpath().evaluate(query, sourceNode, XPathConstants.NODESET);

            // prune each child individually
            NodeEnumeration nodes = new NodeEnumeration(childrenToPrune);
            while (nodes.hasMoreElements()) {
               Node childToPrune = nodes.nextElement();
               Node parentOfChildToPrune = childToPrune.getParentNode();
               parentOfChildToPrune.removeChild(childToPrune);
            }
         } catch (XPathExpressionException e) {
            throw new XMLException("Could not resolve: " + query, e);
         }
      }
   }

   /**
    * Render and XML node as a string
    * 
    * @param node
    *           - the note to render
    * 
    * @return the value of the node
    */
   public String textNodeToString(final Node node) {
      return node.getTextContent();
   }

   /**
    * Render and XML node as a string
    * 
    * @param node
    *           - the note to render
    * 
    * @return the value of the node
    */
   public String elementToString(final Node node) throws XMLException {
      try {
         Transformer transformer = this.getTransformerFactory().newTransformer();
         StringWriter buffer = new StringWriter();
         transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
         transformer.setOutputProperty(OutputKeys.STANDALONE, "no");
         transformer.setOutputProperty(OutputKeys.METHOD, "html");
         transformer.setOutputProperty(OutputKeys.INDENT, "yes");
         transformer.transform(new DOMSource(node), new StreamResult(buffer));
         return buffer.toString();
      } catch (TransformerException e) {
         throw new XMLException("Could not transform node", e);
      }
   }
}
