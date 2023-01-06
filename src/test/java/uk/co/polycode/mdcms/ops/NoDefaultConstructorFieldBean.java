package uk.co.polycode.mdcms.ops;

import java.util.Map;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class NoDefaultConstructorFieldBean {

   /**
    * The items stringField
    */
   private String stringField;

   /**
    * The items hashmapField
    */
   private Map<String, String> hashmapField;

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   public NoDefaultConstructorFieldBean() {
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
   public Map<String, String> getHashmapField() {
      return this.hashmapField;
   }

   /**
    * Set the hashmapField
    * 
    * @param hashmapField
    *           the new hashmapField
    */
   public void setHashmapField(final Map<String, String> hashmapField) {
      this.hashmapField = hashmapField;
   }

}
