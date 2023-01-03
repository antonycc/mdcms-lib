package uk.co.diyaccounting.cms.type;

import org.w3c.dom.Node;

import uk.co.diyaccounting.cms.dto.DocumentTitle;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.util.xml.XMLException;

/**
 * XML content for which document structure and contents are retrieved
 */
public class XmlFieldType extends AbstractFieldType {

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.cms.FieldType#readXmlNodeIntoFieldTypeSpecificObject(org.w3c.dom.Node, java.lang.String,
    * java.lang.String, java.lang.String[])
    */
   @Override
   public Object readXmlNodeIntoFieldTypeSpecificObject(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {
      return this.readXmlNodeIntoStringForXml(contentPath, document, xpathQuery, defaultValue, prune);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.diyaccounting.cms.FieldType#readMdNodeIntoFieldTypeSpecificObject(DocumentText,
    * java.lang.String, java.lang.String)
    */
   @Override
   public Object readMdNodeIntoFieldTypeSpecificObject(
         final String resourceName,
         final String documentText,
         final String documentTitleQuery,
         final String defaultValue) {
      return DocumentTitle.evaluate(resourceName, documentText, documentTitleQuery, defaultValue);
   }

   /**
    * Wrap any XML exception with a Content Exception
    * 
    * @param contentPath
    *           the path to the resource the document was parsed from
    * @param document
    *           the XML document to parse
    * @param xpathQuery
    *           the path to read as a string
    * @param defaultValue
    * @param prune
    *           an array of Xpaths of items to prune from the result
    * @return the node contents as a string
    */
   public String readXmlNodeIntoStringForXml(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {
      try {
         return this.getXmlHelper().evaluateXmlNodeContentsToString(document, xpathQuery, defaultValue, prune);
      } catch (XMLException e) {
         throw new ContentException("Could not read node using path " + xpathQuery, contentPath, e);
      }
   }

}