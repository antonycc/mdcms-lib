package uk.co.diyaccounting.cms.type;

import org.w3c.dom.Node;
import uk.co.diyaccounting.cms.dto.DocumentTitle;

/**
 * XML content with enclosing elements stripped
 */
public class XmlContentsFieldType extends AbstractFieldType {

   /**
    * Delegate to the field type for ChildList for common functions
    */
   private transient XmlFieldType xmlFieldType = new XmlFieldType();

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
      return this.readXmlNodeStrippingOpeningAndClosingElementsIntoStringForXmlContents(contentPath, document,
               xpathQuery,
               defaultValue, prune);
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
    * Read an XML node from an XML document and strip it's enclosing element
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
   public String readXmlNodeStrippingOpeningAndClosingElementsIntoStringForXmlContents(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {

	   this.xmlFieldType.setXmlHelper(this.getXmlHelper());
      String s = this.xmlFieldType.readXmlNodeIntoStringForXml(contentPath, document, xpathQuery, defaultValue, prune);

      // Strip any opening element
      s = s.replaceFirst("<[^>]*>", "");

      // Strip any closing element by reversing the string
      StringBuilder buf = new StringBuilder(s);
      s = buf.reverse().toString();
      s = s.replaceFirst(">[^<]*<", "");
      buf.setLength(0);
      s = buf.append(s).reverse().toString();

      return s.trim();
   }
}