package uk.co.diyaccounting.cms.ops;

import uk.co.diyaccounting.cms.dto.AbstractItem;
import uk.co.diyaccounting.cms.type.Content;
import uk.co.diyaccounting.cms.type.MdContent;

import java.net.URL;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class UrlItem extends AbstractItem {

	private static final long serialVersionUID = 1L;

   /**
    * Is this the URL for an item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='url']/td[last()]")
   @MdContent(path = "/url")
   private URL url;

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
   public URL getUrl() {
      return this.url;
   }

   /**
    * The URL for an item
    * 
    * @param url
    *           the url to set
    */
   public void setUrl(final URL url) {
      this.url = url;
   }

}
