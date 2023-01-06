package uk.co.polycode.mdcms.ops;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class BadBean {

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
    */
   public BadBean() {
   }

   /**
    * Get the flag that states if this product a getterAndSetter product
    * 
    * @return Is this product a getterAndSetter product
    */
   public boolean getGetterAndSetter() {
      return !this.getterAndSetter;
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
      return this.stringField + "X";
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
