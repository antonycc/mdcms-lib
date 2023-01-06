package uk.co.polycode.mdcms.cms.type;

import uk.co.polycode.mdcms.util.xml.XMLHelper;

/**
 * Common elements of a Field Type
 */
public abstract class AbstractFieldType implements FieldType {

   /**
    * The Xml helper to use
    */
   private transient XMLHelper xmlHelper = new XMLHelper();

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.cms.FieldType#setXmlHelper(uk.co.polycode.mdcms.util.xml.XMLHelper)
    */
   @Override
   public void setXmlHelper(final XMLHelper xmlHelper) {
      this.xmlHelper = xmlHelper;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.polycode.mdcms.cms.FieldType#getXmlHelper()
    */
   @Override
   public XMLHelper getXmlHelper() {
      return this.xmlHelper;
   }
}