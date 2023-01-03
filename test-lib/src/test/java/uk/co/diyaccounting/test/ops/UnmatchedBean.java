package uk.co.diyaccounting.test.ops;

/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class UnmatchedBean {

   /**
    * The items stringField
    */
   private String stringField;

   /**
    * Field which is not set or got
    */
   protected final boolean noGetterOrSetter = false;

   /**
    * Field which is just set
    */
   protected final boolean noSetter = false;

   /**
    * Field which is just got
    */
   protected boolean noGetter;

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
   public UnmatchedBean() {
   }

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   protected UnmatchedBean(final Boolean bool) {
      // NOP
   }

   /**
    * A protected method for testing illegal access exceptions
    */
   // protected void protectedMethod() {
   // NOP
   // }

   /**
    * Get the flag that states if this product a getterAndSetter product
    * 
    * @return Is this product a getterAndSetter product
    */
   public boolean getFeatured() {
      return this.getterAndSetter;
   }

   /**
    * Set the flag that states if this product a getterAndSetter product
    * 
    * @param getterAndSetter
    *           the new getterAndSetter flag
    */
   public void setFeatured(final boolean featured) {
      this.getterAndSetter = featured;
   }

   /**
    * Get the stringField
    * 
    * @return the stringField
    */
   public String getName() {
      return this.stringField;
   }

   /**
    * Set the stringField
    * 
    * @param stringField
    *           the new stringField
    */
   public void setName(final String name) {
      this.stringField = name;
   }

   /**
    * @return the noSetter
    */
   public boolean getNoSetter() {
      return this.noSetter;
   }

   /**
    * @param noGetter
    *           the noGetter to set
    */
   public void setNoGetter(final boolean noGetter) {
      this.noGetter = noGetter;
   }
}
