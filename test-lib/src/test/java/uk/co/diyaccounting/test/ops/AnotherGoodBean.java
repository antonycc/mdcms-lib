package uk.co.diyaccounting.test.ops;

import java.util.HashMap;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class AnotherGoodBean {

   /**
    * The items hashmapField
    */
   private HashMap<String, String> hashmapField;

   /**
    * Field with a getter and setter
    */
   private Boolean getterAndSetter;

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   public AnotherGoodBean() {
   }

   /**
    * Get the flag that states if this product a getterAndSetter product
    * 
    * @return Is this product a getterAndSetter product
    */
   public Boolean getGetterAndSetter() {
      return this.getterAndSetter;
   }

   /**
    * Set the flag that states if this product a getterAndSetter product
    * 
    * @param getterAndSetter
    *           the new getterAndSetter flag
    */
   public void setGetterAndSetter(final Boolean getterAndSetter) {
      this.getterAndSetter = getterAndSetter;
   }

   /**
    * Get the hashmapField
    * 
    * @return the hashmapField
    */
   public HashMap<String, String> getHashmapField() {
      return this.hashmapField;
   }

   /**
    * Set the hashmapField
    * 
    * @param hashmapField
    *           the new hashmapField
    */
   public void setHashmapField(final HashMap<String, String> hashmapField) {
      this.hashmapField = hashmapField;
   }

}
