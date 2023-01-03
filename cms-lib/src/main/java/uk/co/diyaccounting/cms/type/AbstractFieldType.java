package uk.co.diyaccounting.cms.type;

import uk.co.diyaccounting.util.xml.XMLHelper;

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
    * @see uk.co.diyaccounting.cms.FieldType#setXmlHelper(uk.co.diyaccounting.util.xml.XMLHelper)
    */
   @Override
   public void setXmlHelper(final XMLHelper xmlHelper) {
      this.xmlHelper = xmlHelper;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.cms.FieldType#getXmlHelper()
    */
   @Override
   public XMLHelper getXmlHelper() {
      return this.xmlHelper;
   }
}