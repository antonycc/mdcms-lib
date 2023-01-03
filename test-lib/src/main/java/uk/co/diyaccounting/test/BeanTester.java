package uk.co.diyaccounting.test;

import junit.framework.AssertionFailedError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

/**
 * Exercises bean methods
 */
public class BeanTester {

   /**
    * A logger from java logging
    */
   private static Logger logger = LoggerFactory.getLogger(BeanTester.class);

   /**
    * Extend test framework by supplying an assert to check for each field with a getter and a setter the value set it
    * retrieved when the get method is called
    * 
    * @throws AssertionFailedError
    */
   public static void assertGettersGetAndSettersSet(final Object obj) {
      // Populate a bean tester with an instance of the class under test
      BeanTester tester = new BeanTester(obj);

      // Check that there are bean methods
      int pairedCount = tester.countGetSettersSet();
      if (pairedCount == 0) {
         throw new AssertionFailedError("Bean contains no paired Getters and Setters for " + obj.getClass().getName());
      } else {
         BeanTester.logger.debug("Bean contains {} paired Getters and Setters for {}",
                  pairedCount, obj.getClass().getName());
      }

      // Check each matched pair of getters and setters
      boolean gettersGetAndSettersSet = tester.getsGetSettersSet();
      if (!gettersGetAndSettersSet) {
         throw new AssertionFailedError("Paired Getters and Setters do not match for " + obj.getClass().getName());
      }

   }

   /**
    * Pairs of getters and setters extracted from the object
    */
   private BeanGetterSetter[] gettersAndSetters;

   /**
    * Introspect the bean extracting setters and getters
    */
   public BeanTester(final Object obj) {

      // for each field in the object, introspect to obtain getters and setters
      Field[] fields = obj.getClass().getDeclaredFields();
      ArrayList<BeanGetterSetter> gettersAndSetterArray = new ArrayList<BeanGetterSetter>();
      for (Field field : fields) {

         // Skip statics
         if (Modifier.isStatic(field.getModifiers())) {
            continue;
         }

         // Add getter and setter pairs
         BeanTester.logger.debug("Checking getters and setters for field: {}", field.getName());
         BeanGetterSetter getterAndSetter = new BeanGetterSetter(obj, field);
         gettersAndSetterArray.add(getterAndSetter);
         BeanTester.logger.debug("Field has getter setter pair: {}: {}", field.getName(),
                  getterAndSetter.isGetterSetterPair());
      }
      this.gettersAndSetters = new BeanGetterSetter[gettersAndSetterArray.size()];
      gettersAndSetterArray.toArray(this.gettersAndSetters);
   }

   /**
    * Count the matching getters and setters
    * 
    * @return the number of matching getters and setters
    */
   public int countGetSettersSet() {

      // Increment for each bean method pair
      int count = 0;
      for (BeanGetterSetter getterAndSetter : this.gettersAndSetters) {
         if (getterAndSetter.isGetterSetterPair()) {
            count++;
         }
      }

      return count;
   }

   /**
    * Check each setter and getter once with an expected value
    * 
    * @return true if all the setters and getters behave as expected
    */
   public boolean getsGetSettersSet() {

      boolean allEqual = true;

      // for each bean method pair and check the change comes back
      for (BeanGetterSetter getterAndSetter : this.gettersAndSetters) {

         // generate random value
         Object expectedValue = getterAndSetter.getTestValue(getterAndSetter.getFieldType());

         // apply to setter
         try {
            getterAndSetter.setFieldValue(expectedValue);
            // } catch (NullPointerException e) {
            // continue;
            // } catch (ReflectionException e) {
            // continue;
         } catch (Throwable e) {
            BeanTester.logger.warn("Exception during setter invocation {} {}", e.getClass().getSimpleName(), e.getMessage());
         }

         // get with getter
         Object actualValue = null;
         try {
            actualValue = getterAndSetter.getFieldValue();
            // } catch (NullPointerException e) {
            // continue;
            // } catch (ReflectionException e) {
            // continue;
         } catch (Throwable e) {
            BeanTester.logger.warn("Exception during getter invocation {} {}", e.getClass().getSimpleName(), e.getMessage());
         }

         BeanTester.logger.debug("Expected [{}]", expectedValue);
         BeanTester.logger.debug("Actual   [{}]", actualValue);

         // Skip check for unpaired getters and setter
         if (getterAndSetter.isGetterSetterPair()) {
            // compare to random value
            allEqual &= expectedValue.equals(actualValue);
         }
      }

      return allEqual;
   }

}
