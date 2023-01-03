package uk.co.diyaccounting.cms.dto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.diyaccounting.cms.type.Content;
import uk.co.diyaccounting.cms.type.MdContent;
import uk.co.diyaccounting.cms.type.XmlContentsFieldType;
import uk.co.diyaccounting.cms.type.XmlFieldType;
import uk.co.diyaccounting.util.net.UrlHelper;

/**
 * Key page attributes
 * 
 * @author Antony
 */
public abstract class Metadata extends NameBody implements Comparable<Metadata> {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(Metadata.class);

	private static final long serialVersionUID = 1L;

   /**
    * The Url helper to use
    */
   private transient UrlHelper urlHelper = new UrlHelper();

   /**
    * The title of this item
    */
   @Content(path = "/html/head/title")
   @MdContent(path = "/title", trim = true)
   private String title;

   /**
    * The shorter version of title of this item (defaults to title)
    */
   @Content(path = NameBody.MAIN_CONTENT + "/div/table/tbody/tr[td='short-title']/td[last()]")
   @MdContent(path = "/shortTitle", trim = true)
   private String shortTitle;

   /**
    * Descriptive text of this item
    */
   @Content(path = "/html/body/div/div/table/tbody/tr[td='description']/td[last()]", type = XmlContentsFieldType.class)
   @MdContent(path = "/description", trim = true)
   private String description;

   /**
    * Text of this item as it appears in search results
    */
   @Content(path = "/html/body/div/div/table/tbody/tr[td='meta-description']/td[last()]")
   @MdContent(path = "/metaDescription", trim = true)
   private String metaDescription;

   /**
    * Keywords for this item
    */
   @Content(path = "/html/body/div/div/table/tbody/tr[td='meta-keywords']/td[last()]")
   @MdContent(path = "/keywords", trim = true)
   private String keywords;

   /**
    * The precedence of this item, lower the number the higher the precedence
    */
   @Content(path = NameBody.MAIN_CONTENT + "/div/table/tbody/tr[td='precedence']/td[last()]", defaultValue = "0")
   @MdContent(path = "/precedence")
   private String precedence;

   /**
    * Is this item a featured item
    */
   @Content(path = NameBody.MAIN_CONTENT + "/div/table/tbody/tr[td='featured']/td[last()]")
   @MdContent(path = "/featured")
   private boolean featured;

   /**
    * Is this item a featured partner item
    */
   @Content(path = NameBody.MAIN_CONTENT + "/div/table/tbody/tr[td='featuredPartner']/td[last()]")
   private boolean featuredPartner;

   /**
    * Image for this item
    */
   @Content(path = NameBody.MAIN_CONTENT + "/div/table/tbody/tr[td='image']/td[last()]//img", type = XmlFieldType.class)
   @MdContent(path = "/image")
   private Image image;

   /**
    * The body (HTML) of this item
    */
   @Content(
            path = NameBody.MAIN_CONTENT + "/*",
            prune = {NameBody.MAIN_CONTENT + "/div",
                     NameBody.MAIN_CONTENT + "/ul[@id=\"attachments\"]"},
            type = XmlFieldType.class)
   @MdContent(path = "/trailingBody")
   private String trailingBody;

   @Override
   public void postPopulationConfig() {
      super.postPopulationConfig();

      // if the meta-description is missing default it to the description
      if (StringUtils.isBlank(this.getMetaDescription())) {
         this.setMetaDescription(this.getDescription());
      }

      // if the keywords are missing default them to the description
      if (StringUtils.isBlank(this.getKeywords())) {
         this.setKeywords(this.getDescription());
      }

      // if the short title is still blank default it to the title
      if (StringUtils.isBlank(this.getShortTitle())) {
         this.setShortTitle(this.getTitle());
      }

      // Add a prefix to the image src
      this.updateImagePrefix(super.getPath());
      this.updateTextImagePrefix(super.getPath());
   }

   private void updateImagePrefix(final String path) {
      String prefix = super.generatePrefixString(path);
      if(this.image != null && StringUtils.isNoneBlank(this.image.getSrc())) {
         this.image.setSrcPrefix(prefix);
         logger.info("Created image path {} from prefix {}", this.image.getSrc(), prefix);
      }
   }

   private void updateTextImagePrefix(final String path) {
      String prefix = super.generatePrefixString(path);
      if(StringUtils.isNoneBlank(this.description)){
         this.description = super.replaceImgSrc(prefix, this.description);
      }
      if(StringUtils.isNoneBlank(this.trailingBody)){
         this.trailingBody = super.replaceImgSrc(prefix, this.trailingBody);
      }
   }

   public void setTitle(final String title) {
      this.title = title;
   }

   public String getTitle() {
      return this.title;
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

   public boolean getFeaturedPartner() {
      return this.featuredPartner;
   }

   public void setFeaturedPartner(final boolean featuredPartner) {
      this.featuredPartner = featuredPartner;
   }

   public String getTrailingBody() {
      return this.trailingBody;
   }

   public void setTrailingBody(final String trailingBody) {
      this.trailingBody = trailingBody;
   }

   public Image getImage() {
      return this.image;
   }

   public void setImage(final Image image) {
      this.image = image;
   }

   public String getPrecedence() {
      return this.precedence;
   }

   public void setPrecedence(final String precedence) {
      this.precedence = precedence;
   }

   /**
    * Is this object of greater precedence than the passed in object?
    * 
    * @param o
    *           the object to compare against
    * 
    * @return the value 0 if this object's precedence value is equal to the argument object's precedence value; a value
    *         less than 0 if this object's precedence value is numerically less than the argument object's precedence
    *         value; and a value greater than 0 if this object's precedence value is numerically greater than the
    *         argument object's precedence value (signed comparison).
    */
   @Override
   public int compareTo(final Metadata o) {
      if(StringUtils.isNoneBlank(this.getPrecedence())){
         return this.getPrecedence().compareTo(o.getPrecedence());
      }else if (this.getName() == null) {
         return 0;
      } else {
         return this.getName().compareTo(o.getName());
      }
   }

   @Override
   public boolean equals(final Object o) {
      if (o == null) {
         return false;
      } else if (!(o instanceof Metadata)) {
         return false;
      } else {
         return this.compareTo((Metadata) o) == 0;
      }
   }

   @Override
   public int hashCode() {
      return super.hashCode();
   }

   public String getMetaDescription() {
      return this.metaDescription;
   }

   public void setMetaDescription(final String metaDescription) {
      this.metaDescription = metaDescription;
   }

   public String getShortTitle() {
      return this.shortTitle;
   }

   public void setShortTitle(final String shortTitle) {
      this.shortTitle = shortTitle;
   }
}
