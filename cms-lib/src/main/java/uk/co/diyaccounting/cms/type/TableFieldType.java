package uk.co.diyaccounting.cms.type;

import org.apache.commons.collections4.map.ListOrderedMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import uk.co.diyaccounting.cms.dto.DocumentTitle;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.util.xml.XMLException;

import java.util.HashMap;

/**
 * Tabular data key's on the first column with a map of values in the other columns
 */
public class TableFieldType extends AbstractFieldType {

   /**
    * Delegate to the field type for ChildList for common functions
    */
   private transient ChildListFieldType childList = new ChildListFieldType();

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
      return this.readXHTMLTableIntoMapOfMapsForTable(contentPath, document, xpathQuery, defaultValue, prune);
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
      String valueAsString = DocumentTitle.evaluate(resourceName, documentText, documentTitleQuery, defaultValue);
      HashMap<String, HashMap<String, String>> valueAsTable = new HashMap<>();
      ListOrderedMap<String, String> rows = DocumentTitle.split(valueAsString);
      rows.forEach((rowName, rowValue) -> this.addTableRow(valueAsTable, rowName.trim(), rowValue.trim()));
      return valueAsTable;
   }

   private void addTableRow(final HashMap<String, HashMap<String, String>> valueAsTable, final String rowName, final String rowValue) {
      ListOrderedMap<String, String> columns = DocumentTitle.split(rowValue);
      HashMap<String, String> tableRow = new HashMap<>();
      columns.forEach((columnName, columnValue) -> tableRow.put(columnName.trim(), columnValue.trim()));
      valueAsTable.put(rowName, tableRow);
   }

   /**
    * Read an XHTML table from an XML document and convert the contents to a map
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
   private HashMap<String, HashMap<String, String>> readXHTMLTableIntoMapOfMapsForTable(
            final String contentPath,
            final Node document,
            final String xpathQuery,
            final String defaultValue,
            final String... prune) {

      Node tableNode = null;
      String xpath = "";
      try {
         xpath = xpathQuery;
         tableNode = this.getXmlHelper().evaluate(document, xpathQuery);
         // String nodeAsXml;
         // nodeAsXml = xmlHelper.evaluateXmlNodeContentsToString(tableNode, ".", defaultValue, prune);
         // AbstractItem.logger.debug("Read [{}] to get:[{}]", path, nodeAsXml);

         // Headers
         String[] headers = null;
         String headersPath = "//tr[1]/td";
         xpath = headersPath;
	      this.childList.setXmlHelper(this.getXmlHelper());
         headers = this.childList.readNodeListIntoStringArrayForChildList(contentPath, tableNode, headersPath, null,
                  prune);
         // String headersTxt = new ToStringBuilder(headers).append("headers", headers).toString();
         // AbstractItem.logger.debug("Read [{}] to get:[{}]", headersPath, headersTxt);

         // Table rows
         NodeList dataRows = null;
         String dataPath = "//tr";
         xpath = dataPath;
         dataRows = this.getXmlHelper().evaluateToNodeList(tableNode, dataPath);
         // AbstractItem.logger.debug("Read [{}] to get: {} items", dataPath, dataRows.getLength());

         // Walk each row extracting the data
         HashMap<String, HashMap<String, String>> table = new HashMap<String, HashMap<String, String>>();
         boolean skippedFirst = false;
         for (int i = 0; i < dataRows.getLength(); i++) {
            Node dataRow = dataRows.item(i);

            // Skip first item (headers)
            if (!skippedFirst) {
               skippedFirst = true;
               continue;
            }

            // Row data
            String[] dataItems = null;
            String dataItemPath = "td";
            xpath = dataItemPath;
            dataItems = this.childList
                     .readNodeListIntoStringArrayForChildList(contentPath, dataRow, dataItemPath, null);
            // AbstractItem.logger.debug("Read [{}] to get:[{}]", dataItemPath, dataItemsTxt);

            // Add each item in sequence
            HashMap<String, String> row = new HashMap<String, String>();
            for (int j = 0; j < dataItems.length; j++) {
               row.put(headers[j], dataItems[j]);
            }

            // Add the row to the whole table
            table.put(dataItems[0], row);
         }

         return table;
      } catch (XMLException e) {
         throw new ContentException("Could not read node using path " + xpath, contentPath, e);
      }
   }
}