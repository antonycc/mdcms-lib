package uk.co.polycode.mdcms.cms.dto;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.polycode.mdcms.util.net.UrlHelper;

import java.io.Serializable;

/**
 * An asset typically attached to a content item
 */
public final class Image implements Serializable {

   private static final Logger logger = LoggerFactory.getLogger(Image.class);
   private static final long serialVersionUID = 1L;
   private transient UrlHelper urlHelper = new UrlHelper();

   private String alt;
   private String srcPath;
   private String img;
   private String srcPrefix;

   /**
    * Generate an asset by parsing the XML fragment
    * # image
    * ![History of DIY Accounting - Accounting and Payroll Software](assets/2720365.png)
    */
   public Image(final String img) {
      Image.logger.debug("Building image from: [{}]", img);
      this.setImg(img);
      if (img.length() != 0) {
         int startOfAlt = img.indexOf("![");
         if (startOfAlt != -1) {
            String altText = img.substring(startOfAlt+2, img.indexOf("]", startOfAlt+2));
            this.setAlt(altText);
         }
         int startOfSrc = Math.max(img.indexOf("!("), img.indexOf("]("));
         if (startOfSrc != -1) {
            String srcText = img.substring(startOfSrc+2, img.indexOf(")", startOfSrc+2));
            Image.logger.info("Built image from: [{}], from [{}]", srcText, img);
            this.setSrcPath(srcText);
         }
      } else {
         this.setAlt("");
         this.setImg("");
         this.setSrcPath("");
      }
   }

   public void setSrcPrefix(final String srcPrefix) {
      this.srcPrefix = srcPrefix;
   }

   public String getSrc() {
      if(StringUtils.isNoneBlank(this.srcPrefix)){
         return this.srcPrefix + this.srcPath;
      }else{
         return srcPath;
      }
   }

   public String getAlt() {
      return this.alt;
   }

   public void setAlt(final String alt) {
      this.alt = alt;
   }

   public String getSrcPath() {
      return this.srcPath;
   }

   public void setSrcPath(final String srcPath) {
      this.srcPath = srcPath;
   }

   public String getImg() {
      return this.img;
   }

   public void setImg(final String img) {
      this.img = img;
   }
}
