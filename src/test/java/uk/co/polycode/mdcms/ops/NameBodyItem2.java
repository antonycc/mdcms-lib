package uk.co.polycode.mdcms.ops;

import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.MdContent;
import uk.co.polycode.mdcms.cms.type.XmlFieldType;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class NameBodyItem2 extends NameBodyItem {

	private static final long serialVersionUID = 1L;

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(Item.class);

   /**
    * The body (HTML) of this item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/*", type = XmlFieldType.class)
   @MdContent(path = "/0")
   private String body;

   @Override
   public String getFilter() {
      return null;
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
