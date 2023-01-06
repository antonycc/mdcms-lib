package uk.co.polycode.mdcms.ops;

import uk.co.polycode.mdcms.cms.dto.AbstractItem;
import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.MdContent;
import uk.co.polycode.mdcms.cms.type.TableFieldType;

import java.util.HashMap;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class TableItem extends AbstractItem {

	private static final long serialVersionUID = 1L;;

   @Override
   public String getExtension() {
      return null;
   }

   @Override
   public String getFilter() {
      return null;
   }

   /**
    * Products for this item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/div/table", type = TableFieldType.class)
   @MdContent(path = "/URLMappings/", type = TableFieldType.class)
   private HashMap<String, HashMap<String, String>> table;

   /**
    * Products for this item
    * 
    * @return the contentList
    */
   public HashMap<String, HashMap<String, String>> getTable() {
      return this.table;
   }

   /**
    * Products for this item
    * 
    * @param urlMappingsTable
    *           the urlMappingsTable to set
    */
   public void setTable(final HashMap<String, HashMap<String, String>> table) {
      this.table = table;
   }

}
