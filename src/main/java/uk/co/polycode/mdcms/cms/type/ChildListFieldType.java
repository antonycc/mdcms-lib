package uk.co.polycode.mdcms.cms.type;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.w3c.dom.Node;

import uk.co.polycode.mdcms.cms.dto.DocumentTitle;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.util.xml.XMLException;

/**
 * Child pages to be translated into a list
 */
public class ChildListFieldType extends AbstractFieldType {

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.cms.FieldType#readXmlNodeIntoFieldTypeSpecificObject(java.lang.String, org.w3c.dom.Node,
    * java.lang.String, java.lang.String, java.lang.String[])
    */
   @Override
   public Object readXmlNodeIntoFieldTypeSpecificObject(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {
      return this.readNodeListIntoStringArrayForChildList(contentPath, document, xpathQuery, defaultValue, prune);
   }

   /*
    * (non-Javadoc)
    *
    * @see uk.co.polycode.mdcms.cms.FieldType#readMdNodeIntoFieldTypeSpecificObject(DocumentText,
    * java.lang.String, java.lang.String)
    */
   @Override
   public Object readMdNodeIntoFieldTypeSpecificObject(
         final String resourceName,
         final String documentText,
         final String documentTitleQuery,
         final String defaultValue) {
      String valueAsString  = DocumentTitle.evaluate(resourceName, documentText, documentTitleQuery, defaultValue);
      ListOrderedMap<String, String> sections = DocumentTitle.split(valueAsString);
      String[] valueAsList = {};
      valueAsList = sections.keyList().toArray(valueAsList);
      return valueAsList;
   }

   /**
    * Read a text node from an XML document
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
    * @return the node contents as a string array
    */
   public String[] readNodeListIntoStringArrayForChildList(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {
      try {
         return this.getXmlHelper().evaluateSimpleNodeListContentsToStringArray(document, xpathQuery, prune);
      } catch (XMLException e) {
         throw new ContentException("Could not read node using path " + xpathQuery, contentPath, e);
      }
   }

}