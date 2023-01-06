package uk.co.polycode.mdcms.cms.dto;

import org.apache.commons.lang3.StringUtils;
import uk.co.polycode.mdcms.cms.service.ContentException;
import uk.co.polycode.mdcms.cms.type.Content;
import uk.co.polycode.mdcms.cms.type.MdContent;
import uk.co.polycode.mdcms.cms.type.XmlFieldType;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.io.IOException;

/**
 * A content item with a name and a body
 *
 * @author Antony
 */
public abstract class NameBody extends AbstractItem {

	private static final long serialVersionUID = 1L;

	/**
	 * The location of the body in the Confluence HTML export template
	 */
	public static final String MAIN_CONTENT = "/html/body/div[@id=\"page\"]/div[@id=\"main\"]/div[@id=\"content\"]/div[@id=\"main-content\"]";

   /**
    * The name of this item
    */
                   @Content(path = "/html/head/title")
                   @MdContent(path = "/name", trim = true)
   private String name;

   /**
    * The name of this item without a postfix
    */
   private String nameWithoutPostfix;

   /**
    * The body (HTML) of this item
    */
   @SuppressWarnings("checkstyle:sizes")
   @Content(path = MAIN_CONTENT + "/*", type = XmlFieldType.class)
   @MdContent(path = "/body")
   private String body;

   //@XmlTransient
   //@Transient
   private transient FileLikePathService fileLikePathService = new FileLikePathService();

   private String imagePrefix;

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.cms.AbstractItem#postPopulationConfig()
    */
   @Override
   public void postPopulationConfig() {
      super.postPopulationConfig();
      this.updateNameWithoutPostfix();
      this.updateBodyImagePrefix(super.getPath());
   }

   private void updateBodyImagePrefix(final String path) {
      String prefix = this.generatePrefixString(path);
      if(StringUtils.isNoneBlank(this.body)){
         this.body = this.replaceImgSrc(prefix, this.body);
      }
   }

   public void setImagePrefix(final String imagePrefix){
      this.imagePrefix = imagePrefix;
   }

   public String generatePrefixString(final String path) {
      // Resolve prefix
      String prefix = this.imagePrefix;
      String hostTemplate = "[CONTENT-HOST]";
      if (StringUtils.isNoneBlank(prefix) && prefix.contains(hostTemplate)) {
         try {
            String host = this.fileLikePathService.getHostForUrn(path);
            prefix = prefix.replace(hostTemplate, host);
         }catch (IOException e){
            throw new ContentException("Could not extract host from path for prefix: " + prefix, path, e);
         }
      }
      return prefix;
   }

   public String replaceImgSrc(final String prefix, final String text) {
      String target = "<img src=\"assets";
      String replacement = "<img src=\"" + prefix + "assets";
      return text.replace(target, replacement);
   }

   public void setName(final String name) {
      this.name = name;
   }

   public String getName() {
      return this.name;
   }

   /**
    * Update the name of this item without a postfix
    */
   private void updateNameWithoutPostfix() {
      if (this.getName() == null) {
         this.setNameWithoutPostfix(null);
         return;
      }
      int lastCapital = 0;
      int length = this.getName().length();
      for (int i = 0; i < length; i++) {
         lastCapital = Character.isUpperCase(this.getName().charAt(i)) ? i : lastCapital;
      }
      this.setNameWithoutPostfix(this.getName().substring(0, lastCapital == 0 ? length : lastCapital));
   }

   public void setBody(final String body) {
      this.body = body;
   }

   public String getBody() {
      return this.body;
   }

   public String getNameWithoutPostfix() {
      return this.nameWithoutPostfix;
   }

   public void setNameWithoutPostfix(final String nameWithoutPostfix) {
      this.nameWithoutPostfix = nameWithoutPostfix;
   }
}
