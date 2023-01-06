package uk.co.polycode.mdcms.ops;

import uk.co.polycode.mdcms.cms.dto.AbstractItem;
import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.XmlFieldType;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class TestAnnotationDrivenNameBodyItem extends AbstractItem {

	private static final long serialVersionUID = 1L;

   /**
    * The name of this item
    */
   //@Theory
   @Content(path = "/html/head/title")
   private String name;

   /**
    * The body (HTML) of this item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/*", type = XmlFieldType.class)
   private String body;

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

   /**
    * Set the body of this item
    * 
    * @param body
    *           - the new body of this item
    */
   public void setBody(final String body) {
      this.body = body;
   }

   /**
    * Get the body of this item
    * 
    * @returns - the current body of this item
    */
   public String getBody() {
      return this.body;
   }

}
