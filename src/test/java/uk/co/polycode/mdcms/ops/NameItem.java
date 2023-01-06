package uk.co.polycode.mdcms.ops;

import uk.co.polycode.mdcms.cms.dto.AbstractItem;
import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.MdContent;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class NameItem extends AbstractItem {

	private static final long serialVersionUID = 1L;

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(Item.class);

   /**
    * The name of this item
    */
   @Content(path = "/html/head/title")
   @MdContent(path = "/0@")
   private String name;

   @Override
   public String getExtension() {
      return null;
   }

   @Override
   public String getFilter() {
      return null;
   }

   /**
    * Set the name of this item
    * 
    * @param name
    *           - the new name of this item
    */
   public void setName(final String name) {
      this.name = name;
   }

   /**
    * Get the name of this item
    * 
    * @returns - the current name of this item
    */
   public String getName() {
      return this.name;
   }

}
