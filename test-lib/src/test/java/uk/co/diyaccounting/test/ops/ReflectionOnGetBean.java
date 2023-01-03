package uk.co.diyaccounting.test.ops;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class ReflectionOnGetBean {

   /**
    * The items stringField
    */
   private String stringField;

   /**
    * Field with a getter and setter
    */
   private boolean getterAndSetter;

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   public ReflectionOnGetBean() {
   }

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   protected ReflectionOnGetBean(final Boolean bool) {
      // NOP
   }

   /**
    * Get the flag that states if this product a getterAndSetter product
    * 
    * @return Is this product a getterAndSetter product
    */
   public boolean getGetterAndSetter() {
      ((String) null).toString();
      return this.getterAndSetter;
   }

   /**
    * Set the flag that states if this product a getterAndSetter product
    * 
    * @param getterAndSetter
    *           the new getterAndSetter flag
    */
   public void setGetterAndSetter(final boolean getterAndSetter) {
      this.getterAndSetter = getterAndSetter;
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

}
