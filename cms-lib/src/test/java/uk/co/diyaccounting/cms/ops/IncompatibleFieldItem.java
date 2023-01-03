package uk.co.diyaccounting.cms.ops;

import uk.co.diyaccounting.cms.dto.AbstractItem;
import uk.co.diyaccounting.cms.type.Content;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class IncompatibleFieldItem extends AbstractItem {

	private static final long serialVersionUID = 1L;

   /**
    * Is this the URL for an item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='url']/td[last()]")
   private IncompatibleField url;

   @Override
   public String getExtension() {
      return null;
   }

   @Override
   public String getFilter() {
      return null;
   }

   /**
    * The URL for an item
    * 
    * @return the url
    */
   public IncompatibleField getUrl() {
      return this.url;
   }

   /**
    * The URL for an item
    * 
    * @param url
    *           the url to set
    */
   public void setUrl(final IncompatibleField url) {
      this.url = url;
   }

}
