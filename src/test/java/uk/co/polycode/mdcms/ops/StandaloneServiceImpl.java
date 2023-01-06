package uk.co.polycode.mdcms.ops;

/**
 * Accesses page level content
 */
public class StandaloneServiceImpl implements StandaloneService {

   /**
    * Page Content
    */
   private ContentService content;

   /**
    * The path to about content item
    */
   private String aboutContentItemPath = "/content/test/path/TestPage.html";

   /**
    * The path to info content item
    */
   private String infoContentItemPath = "/content/test/path/TestPage.html";

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(PageServiceImpl.class);

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.present.service.StandaloneService#getAbout()
    */
   @Override
   public Object getAbout() throws ContentException {
      String path = this.getAboutContentItemPath();
      return this.content.getTitleBody(path);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.present.service.StandaloneService#getInfo()
    */
   @Override
   public Object getInfo() throws ContentException {
      String path = this.getInfoContentItemPath();
      return this.content.getTitleBody(path);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.present.service.StandaloneService#getAboutContentItemPath()
    */
   @Override
   public String getAboutContentItemPath() {
      return this.aboutContentItemPath;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.present.service.StandaloneService#setAboutContentItemPath(java.lang.String)
    */
   @Override
   public void setAboutContentItemPath(final String aboutContentItemPath) {
      this.aboutContentItemPath = aboutContentItemPath;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.present.service.StandaloneService#getInfoContentItemPath()
    */
   @Override
   public String getInfoContentItemPath() {
      return this.infoContentItemPath;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.present.service.StandaloneService#setInfoContentItemPath(java.lang.String)
    */
   @Override
   public void setInfoContentItemPath(final String infoContentItemPath) {
      this.infoContentItemPath = infoContentItemPath;
   }
}
