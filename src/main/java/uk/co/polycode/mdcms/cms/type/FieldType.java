package uk.co.polycode.mdcms.cms.type;

import org.w3c.dom.Node;
import uk.co.polycode.mdcms.util.xml.XMLHelper;

/**
 * Content types should annotate fields with a type which implements this interface.
 */
public interface FieldType {

   /**
    * Extract the data from the document at the specified XPath, if it is not found use a default value and prune
    * 
    * any unwanted data as specified in XPaths listed in the prune parameter.
    * 
    * @param contentPath
    *           the path to the resource the document was parsed from
    * @param document
    *           the XML document to parse
    * @param xpathQuery
    *           the path to read as a string
    * @param defaultValue
    *           a default value to use if the XPath query finds nothing in the document
    * @param prune
    *           an array of Xpaths of items to prune from the result (optional as vargs)
    * @return the node contents in a type specific t the field type
    */
   Object readXmlNodeIntoFieldTypeSpecificObject(
            String contentPath,
            Node document,
            String xpathQuery,
            String defaultValue,
            String... prune);

   /**
    * Extract the data from the document at the specified XPath, if it is not found use a default value and prune
    *
    * any unwanted data as specified in XPaths listed in the prune parameter.
    *
    * @param resourceName
    *           the top level name of the resource
    * @param documentText
    *           the document text to parse
    * @param documentTitleQuery
    *           the path to read as a string
    * @param defaultValue
    *           a default value to use if the XPath query finds nothing in the document
    *
    * @return the node contents in a type specific t the field type
    */
   Object readMdNodeIntoFieldTypeSpecificObject(
         String resourceName,
         String documentText,
         String documentTitleQuery,
         String defaultValue);

   /**
    * Return the XML Helper
    * 
    * @return xmlHelper
    */
   XMLHelper getXmlHelper();

   /**
    * Change the default implementation of the XML Helper
    * 
    * @param xmlHelper
    */
   void setXmlHelper(XMLHelper xmlHelper);
}