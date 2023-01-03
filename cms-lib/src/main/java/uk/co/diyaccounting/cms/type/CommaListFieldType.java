package uk.co.diyaccounting.cms.type;

import org.w3c.dom.Node;
import uk.co.diyaccounting.cms.dto.DocumentTitle;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Comma separated text content to be translated into a list
 */
public class CommaListFieldType extends AbstractFieldType {

   /**
    * Delegate to the field type for XmlContents for common functions
    */
   private transient XmlContentsFieldType xmlContents = new XmlContentsFieldType();

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
      return this.readCommaSeparatedTextNodeIntoStringArrayForCommaList(contentPath, document, xpathQuery,
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
      String valueAsString  = DocumentTitle.evaluate(resourceName, documentText, documentTitleQuery, defaultValue);
      Pattern lineEndPattern = Pattern.compile("\\r\\n?|\\n");
      Matcher lineEndMatcher = lineEndPattern.matcher(valueAsString);
      String valueWithoutLineEnds = lineEndMatcher.replaceAll("");
      Pattern commaPattern = Pattern.compile("[\\p{Space}]*,[\\p{Space}]*");
      String[] valueAsList = commaPattern.split(valueWithoutLineEnds);
      return valueAsList;
   }

   /**
    * Read a text node from an XML document into an array, taking commas as delimiters between each array item
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
   private String[] readCommaSeparatedTextNodeIntoStringArrayForCommaList(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {
	   this.xmlContents.setXmlHelper(this.getXmlHelper());
      String rawList = this.xmlContents.readXmlNodeStrippingOpeningAndClosingElementsIntoStringForXmlContents(
               contentPath,
               document,
               xpathQuery,
               defaultValue,
               prune);
      StringTokenizer tokenizer = new StringTokenizer(rawList, ",");
      int count = tokenizer.countTokens();
      String[] list = new String[count];
      for (int i = 0; i < count; i++) {
         list[i] = tokenizer.nextToken().trim();
      }
      return list;
   }
}