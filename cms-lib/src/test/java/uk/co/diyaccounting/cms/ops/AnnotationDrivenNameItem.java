package uk.co.diyaccounting.cms.ops;

import uk.co.diyaccounting.cms.dto.AbstractItem;
import uk.co.diyaccounting.cms.type.Content;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class AnnotationDrivenNameItem extends AbstractItem {

   /**
    * The ID for serialisation
    */
   private static final long serialVersionUID = 1L;

   /**
    * The name of this item
    */
   @Content(path = "/html/head/title")
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
