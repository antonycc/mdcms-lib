package uk.co.diyaccounting.cms.type;

import org.w3c.dom.Node;

import uk.co.diyaccounting.cms.dto.DocumentTitle;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.util.xml.XMLException;

/**
 * Text content for which only the contents of text nodes is processed
 */
public class TextFieldType extends AbstractFieldType {

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.cms.FieldType#readXmlNodeIntoFieldTypeSpecificObject(java.lang.String, org.w3c.dom.Node,
    * java.lang.String, java.lang.String, java.lang.String[])
    */
   @Override
   public Object readXmlNodeIntoFieldTypeSpecificObject(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {
      return this.readSimpleNodeIntoStringForText(contentPath, document, xpathQuery, defaultValue, prune);
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
    * @throws uk.co.diyaccounting.cms.service.ContentException
    */
   private String readSimpleNodeIntoStringForText(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {
      try {
         return this.getXmlHelper().evaluateSimpleNodeContentsToString(document, xpathQuery, defaultValue,
                  prune);
      } catch (XMLException e) {
         throw new ContentException("Could not read node using path " + xpathQuery, contentPath, e);
      }
   }

}