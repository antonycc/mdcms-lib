package uk.co.diyaccounting.util.ops;


/**
 * A simple proxy for feeds
 * 
 * @author Antony
 */
public class TestBean {

   /**
    * Is this item a featured item
    */
   private boolean featured;

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   public TestBean(final String initArg) throws TestBeanException {
      if (initArg.equals("I.do.not.exist")) {
         String message = "Argument was " + initArg;
         throw new TestBeanException(message);
      }
   }

   /**
    * Set the input source used to obtain the document then read the attributes
    * 
    * @param path
    *           the path to a classpath resource to use for the inputSource
    */
   protected TestBean(final Boolean bool) {
      // NOP
   }

   /**
    * A protected method for testing illegal access exceptions
    */
   protected void protectedMethod() {
      // NOP
   }

   /**
    * Get the flag that states if this product a featured product
    * 
    * @return Is this product a featured product
    */
   public boolean getFeatured() {
      return this.featured;
   }

   /**
    * Set the flag that states if this product a featured product
    * 
    * @param featured
    *           the new featured flag
    */
   public void setFeatured(final boolean featured) {
      this.featured = featured;
   }

}
