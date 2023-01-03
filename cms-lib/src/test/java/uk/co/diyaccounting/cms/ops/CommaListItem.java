package uk.co.diyaccounting.cms.ops;

import uk.co.diyaccounting.cms.dto.AbstractItem;
import uk.co.diyaccounting.cms.type.CommaListFieldType;
import uk.co.diyaccounting.cms.type.Content;
import uk.co.diyaccounting.cms.type.MdContent;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class CommaListItem extends AbstractItem {

	private static final long serialVersionUID = 1L;

   /**
    * Features for this item
    */
   @Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='features']/td[last()]", type = CommaListFieldType.class)
   @MdContent(path = "/features", type = CommaListFieldType.class, trim = true)
   private String[] features;

   @Override
   public String getExtension() {
      return null;
   }

   @Override
   public String getFilter() {
      return null;
   }

   /**
    * Features for this item
    * 
    * @return the features
    */
   public String[] getFeatures() {
      return this.features;
   }

   /**
    * features for this item
    * 
    * @param features
    *           the features to set
    */
   public void setFeatures(final String[] features) {
      this.features = features;
   }

}
