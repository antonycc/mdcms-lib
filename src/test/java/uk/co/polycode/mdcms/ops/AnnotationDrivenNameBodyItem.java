package uk.co.polycode.mdcms.ops;

import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.XmlFieldType;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class AnnotationDrivenNameBodyItem extends AnnotationDrivenNameItem {

   /**
    * The ID for serialisation
    */
   private static final long serialVersionUID = 1L;
   /**
    * The body (HTML) of this item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/*", type = XmlFieldType.class)
   private String body;

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
