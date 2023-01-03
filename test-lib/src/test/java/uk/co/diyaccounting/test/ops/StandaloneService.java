package uk.co.diyaccounting.test.ops;


/**
 * Content operations for standalone content blocks
 * 
 * @author Antony
 */
public interface StandaloneService {

   /**
    * Return the About content
    * 
    * @return the title body content
    * @throws ContentException
    */
   Object getAbout() throws ContentException;

   /**
    * Return the Info block content
    * 
    * @return the title body content
    * @throws ContentException
    */
   Object getInfo() throws ContentException;

   /**
    * Get the content path
    * 
    * @return the aboutContentItemPath
    */
   String getAboutContentItemPath();

   /**
    * Set the content path
    * 
    * @param aboutContentItemPath
    *           the aboutContentItemPath to set
    */
   void setAboutContentItemPath(final String aboutContentItemPath);

   /**
    * Get the content path
    * 
    * @return the infoContentItemPath
    */
   String getInfoContentItemPath();

   /**
    * Set the content path
    * 
    * @param infoContentItemPath
    *           the aboutContentItemPath to set
    */
   void setInfoContentItemPath(final String infoContentItemPath);
}