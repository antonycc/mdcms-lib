package uk.co.diyaccounting.cms.ops;

import uk.co.diyaccounting.cms.dto.Metadata;
import uk.co.diyaccounting.cms.service.ContentException;

/**
 * Key page attributes
 * 
 * @author Antony
 */
public class TestMetadata extends Metadata {

	private static final long serialVersionUID = 1L;

   /**
    * The title of this item
    */
   //@Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='title']/td[last()]")
   //@MdContent(path = "/0@")
   //private String title;

   /**
    * The name of this item
    */
   //@MdContent(path = "/name", trim=true)
   //private String name;

   /**
    * Descriptive text of this item
    */
   //@Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='description']/td[last()]", type = XmlContentsFieldType.class)
   //@MdContent(path ="/description", trim=true)
   //private String description;

   /**
    * Text of this item as it appears in search results
    */
   //@Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='meta-description']/td[last()]")
   //@MdContent(path ="/meta-description", trim=true)
   //private String metaDescription;

   /**
    * Keywords for this item
    */
   //@Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='meta-keywords']/td[last()]")
   //@MdContent(path ="/meta-keywords", trim=true)
   //private String keywords;

   /**
    * The precedence of this item, lower the number the higher the precedence
    */
   //@Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='precedence']/td[last()]", defaultValue = "0")
   //@MdContent(path ="/precedence", defaultValue = "0")
   //private String precedence;

   /**
    * Is this item a featured item
    */
   //@Content(path = "/html/body/div[@id=\"pagebody\"]/div/table/tbody/tr[td='featured']/td[last()]")
   //@MdContent(path = "/featured")
   //private boolean featured;

   /**
    * The body (HTML) of this item
    */
   /*
   @Content(
            path = "/html/body/div[@id=\"pagebody\"]/*",
            prune = { "/html/body/div[@id=\"pagebody\"]/div",
                     "/html/body/div[@id=\"pagebody\"]/ul[@id=\"attachments\"]" },
            type = XmlFieldType.class)
   @MdContent(path = "/body")
   private String trailingBody;
*/

   @Override
   public String getExtension() {
      return ".md";
   }

   @Override
   public String getFilter() {
      return "Page";
   }

   @Override
   public void postPopulationConfig() throws ContentException {
      super.postPopulationConfig();
   }

   /*
   public void setTitle(final String title) {
      this.title = title;
   }

   public String getTitle() {
      return this.title;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   public String getDescription() {
      return this.description;
   }

   public void setDescription(final String description) {
      this.description = description;
   }

   public String getKeywords() {
      return this.keywords;
   }

   public void setKeywords(final String keywords) {
      this.keywords = keywords;
   }

   public boolean getFeatured() {
      return this.featured;
   }

   public void setFeatured(final boolean featured) {
      this.featured = featured;
   }

   public String getTrailingBody() {
      return this.trailingBody;
   }

   public void setTrailingBody(final String trailingBody) {
      this.trailingBody = trailingBody;
   }

   public String getPrecedence() {
      return this.precedence;
   }

   public void setPrecedence(final String precedence) {
      this.precedence = precedence;
   }

   public String getMetaDescription() {
      return this.metaDescription;
   }

   public void setMetaDescription(final String metaDescription) {
      this.metaDescription = metaDescription;
   }
*/
}
