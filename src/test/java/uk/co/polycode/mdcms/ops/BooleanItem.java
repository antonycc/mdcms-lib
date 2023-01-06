package uk.co.polycode.mdcms.ops;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.polycode.mdcms.cms.dto.AbstractItem;
import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.MdContent;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class BooleanItem extends AbstractItem {

   /**
    * The ID for serialisation
    */
   private static final long serialVersionUID = 1L;

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(BooleanItem.class);

   /**
    * Is this item a featured item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='featured']/td[last()]")
   @MdContent(path = "/featured")
   private boolean featured;

   @Override
   public String getExtension() {
      return null;
   }

   @Override
   public String getFilter() {
      return null;
   }

   /**
    * A protected method for testing illegal access exceptions
    */
   protected void protectedMethod() {
      // NOP
   }

   /**
    * Get the flag that states if this product a featured product
    * 
    * @return Is this product a featured product
    */
   public boolean getFeatured() {
      return this.featured;
   }

   /**
    * Set the flag that states if this product a featured product
    * 
    * @param featured
    *           the new featured flag
    */
   public void setFeatured(final boolean featured) {
      BooleanItem.logger.trace("Path is {}", super.getPath());
      this.featured = featured;
   }

}
