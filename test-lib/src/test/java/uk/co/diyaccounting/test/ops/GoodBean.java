package uk.co.diyaccounting.test.ops;

import java.io.Serializable;
import java.net.URL;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class GoodBean implements Serializable {

	private static final long serialVersionUID = 1L;

   /**
    * The items stringField
    */
   private String stringField;

   /**
    * Int field with a getter and setter
    */
   private int intGetterAndSetter;

   /**
    * Boolean field with a getter and setter
    */
   private boolean booleanGetterAndSetter;

   /**
    * Long field with a getter and setter
    */
   private Long longGetterAndSetter;

   /**
    * Url field with a getter and setter
    */
   private URL urlGetterAndSetter;

   /**
    * Get the URL
    * 
    * @return the urlGetterAndSetter
    */
   public URL getUrlGetterAndSetter() {
      return urlGetterAndSetter;
   }

   /**
    * Set the URL
    * 
    * @param urlGetterAndSetter
    *           the urlGetterAndSetter to set
    */
   public void setUrlGetterAndSetter(final URL urlGetterAndSetter) {
      this.urlGetterAndSetter = urlGetterAndSetter;
   }

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   public GoodBean() {
   }

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   protected GoodBean(final Boolean bool) {
      // NOP
   }

   /**
    * Get the flag that states if this product a getterAndSetter product
    * 
    * @return Is this product a getterAndSetter product
    */
   public int getIntGetterAndSetter() {
      return intGetterAndSetter;
   }

   /**
    * Set the flag that states if this product a getterAndSetter product
    * 
    * @param intGetterAndSetter
    *           the new getterAndSetter flag
    */
   public void setIntGetterAndSetter(final int intGetterAndSetter) {
      this.intGetterAndSetter = intGetterAndSetter;
   }

   /**
    * Get the stringField
    * 
    * @return the stringField
    */
   public String getStringField() {
      return stringField;
   }

   /**
    * The boolean getter
    * 
    * @return the booleanGetterAndSetter
    */
   public boolean isBooleanGetterAndSetter() {
      return booleanGetterAndSetter;
   }

   /**
    * The boolean setter
    * 
    * @param booleanGetterAndSetter
    *           the booleanGetterAndSetter to set
    */
   public void setBooleanGetterAndSetter(final boolean booleanGetterAndSetter) {
      this.booleanGetterAndSetter = booleanGetterAndSetter;
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
    * Field with a getter and setter
    * 
    * @return the longGetterAndSetter
    */
   public Long getLongGetterAndSetter() {
      return longGetterAndSetter;
   }

   /**
    * Field with a getter and setter
    * 
    * @param longGetterAndSetter
    *           the longGetterAndSetter to set
    */
   public void setLongGetterAndSetter(final Long longGetterAndSetter) {
      this.longGetterAndSetter = longGetterAndSetter;
   }

}
