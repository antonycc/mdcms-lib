package uk.co.diyaccounting.cms.ops;

import uk.co.diyaccounting.cms.dto.AbstractItem;
import uk.co.diyaccounting.cms.type.Content;
import uk.co.diyaccounting.cms.type.MdContent;
import uk.co.diyaccounting.cms.type.XmlFieldType;

/**
 * Key page attributes
 * 
 * @author Antony
 */
public final class TrailingBodyItem extends AbstractItem {

   /**
    * The ID for serialisation
    */
   private static final long serialVersionUID = 1L;

   /**
    * The title of this item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='title']/td[last()]")
   @MdContent(path = "/0@")
   private String title;

   /**
    * Descriptive text of this item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='description']/td[last()]")
   @MdContent(path = "/description")
   private String description;

   /**
    * The body (HTML) of this item without the metadata table
    */
   @Content(
            path = "/html/body/div[@id=\"pagebody\"]/*",
            prune = { "/html/body/div[@id=\"pagebody\"]/div" },
            type = XmlFieldType.class)
   @MdContent(path = "/body")
   private String trailingBody;

   @Override
   public String getExtension() {
      return null;
   }

   @Override
   public String getFilter() {
      return null;
   }

   /**
    * Set the title of this item
    * 
    * @param title
    *           - the new title of this item
    */
   public void setTitle(final String title) {
      this.title = title;
   }

   /**
    * Get the title of this item
    * 
    * @returns - the current title of this item
    */
   public String getTitle() {
      return this.title;
   }

   /**
    * Descriptive text of this item
    * 
    * @return the description
    */
   public String getDescription() {
      return this.description;
   }

   /**
    * Descriptive text of this item
    * 
    * @param description
    *           the description to set
    */
   public void setDescription(final String description) {
      this.description = description;
   }

   /**
    * The body (HTML) of this item without the metadata table
    * 
    * @return the trailingBody
    */
   public String getTrailingBody() {
      return this.trailingBody;
   }

   /**
    * The body (HTML) of this item without the metadata table
    * 
    * @param trailingBody
    *           the trailingBody to set
    */
   public void setTrailingBody(final String trailingBody) {
      this.trailingBody = trailingBody;
   }

}
