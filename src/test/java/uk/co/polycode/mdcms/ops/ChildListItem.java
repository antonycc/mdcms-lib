package uk.co.polycode.mdcms.ops;

import uk.co.polycode.mdcms.cms.dto.AbstractItem;
import uk.co.polycode.mdcms.cms.type.ChildListFieldType;
import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.MdContent;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class ChildListItem extends AbstractItem {

   /**
    * The ID for serialisation
    */
   private static final long serialVersionUID = 1L;

   /**
    * Products for this item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/ul/li", type = ChildListFieldType.class)
   @MdContent(path = "/products/", type = ChildListFieldType.class)
   private String[] products;

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
    * 
    * @return the products
    */
   public String[] getProducts() {
      return this.products;
   }

   /**
    * Products for this item
    * 
    * @param products
    *           the products to set
    */
   public void setProducts(final String[] products) {
      this.products = products;
   }

}
