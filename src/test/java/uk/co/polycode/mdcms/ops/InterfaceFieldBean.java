package uk.co.polycode.mdcms.ops;

import java.io.Serializable;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class InterfaceFieldBean {

   /**
    * The items stringField
    */
   private String stringField;

   /**
    * The items hashmapField
    */
   private Serializable hashmapField;

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   public InterfaceFieldBean() {
   }

   /**
    * Get the stringField
    * 
    * @return the stringField
    */
   public String getStringField() {
      return this.stringField;
   }

   /**
    * Set the stringField
    * 
    * @param stringField
    *           the new stringField
    */
   public void setStringField(final String stringField) {
      this.stringField = stringField;
   }

   /**
    * Get the hashmapField
    * 
    * @return the hashmapField
    */
   public Serializable getHashmapField() {
      return this.hashmapField;
   }

   /**
    * Set the hashmapField
    * 
    * @param hashmapField
    *           the new hashmapField
    */
   public void setHashmapField(final Serializable hashmapField) {
      this.hashmapField = hashmapField;
   }

}
