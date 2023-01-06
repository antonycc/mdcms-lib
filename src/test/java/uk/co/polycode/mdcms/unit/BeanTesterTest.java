package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

import uk.co.polycode.mdcms.ops.BadBean;
import uk.co.polycode.mdcms.ops.TestBeanExceptionTwo;
import uk.co.polycode.mdcms.ops.UnmatchedBean;
import uk.co.polycode.mdcms.test.BeanTester;
import uk.co.polycode.mdcms.ops.AnotherGoodBean;
import uk.co.polycode.mdcms.ops.GoodBean;
import uk.co.polycode.mdcms.ops.InterfaceFieldBean;
import uk.co.polycode.mdcms.ops.NoDefaultConstructorFieldBean;
import uk.co.polycode.mdcms.ops.ReflectionOnGetBean;
import uk.co.polycode.mdcms.ops.ReflectionOnSetBean;
import uk.co.polycode.mdcms.ops.StandaloneServiceImpl;
import uk.co.polycode.mdcms.util.reflect.ReflectionException;

/**
 * Test the test helper
 * 
 * @author Antony
 */
public class BeanTesterTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(BeanTesterTest.class);

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test bean getters and setters
    * 
    * @throws ReflectionException
    * @throws TestBeanExceptionTwo
    */
   @Test
   public void testGoodBeanWithAssert() throws ReflectionException, TestBeanExceptionTwo {

      // successful test
      BeanTester.assertGettersGetAndSettersSet(new GoodBean());

      // successful test
      BeanTester.assertGettersGetAndSettersSet(new AnotherGoodBean());
   }

   /**
    * Tests getters and setters that throw null pointer exceptions
    * 
    * @throws ReflectionException
    */
   @Test
   public void testGettersGetAndSettersSet() throws ReflectionException {

      BeanTester.assertGettersGetAndSettersSet(new StandaloneServiceImpl());
   }

   /**
    * Test a bean who's fields cause exceptions when got or set
    * 
    * @throws ReflectionException
    * @throws TestBeanExceptionTwo
    */
   @Test
   public void testReflectionOnBeanWithAssert() throws ReflectionException, TestBeanExceptionTwo {

      // Expected results
      String expectedMessage = "Paired Getters and Setters do not match for " +
               ReflectionOnSetBean.class.getName();

      // negative test
      try {
         BeanTester.assertGettersGetAndSettersSet(new ReflectionOnSetBean());
      } catch (AssertionFailedError e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }

      // Expected results
      expectedMessage = "Paired Getters and Setters do not match for " +
               ReflectionOnGetBean.class.getName();

      // negative test
      try {
         BeanTester.assertGettersGetAndSettersSet(new ReflectionOnGetBean());
      } catch (AssertionFailedError e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }
   }

   /**
    * Test a bean who's fields can't be created
    * 
    * @throws ReflectionException
    * @throws TestBeanExceptionTwo
    */
   @Test
   public void testNoDefaultConstructorFieldBeanWithAssert() throws ReflectionException, TestBeanExceptionTwo {

      // Expected results
      String expectedMessage = "Null pointer exception";

      // negative test
      try {
         BeanTester.assertGettersGetAndSettersSet(new NoDefaultConstructorFieldBean());
      } catch (AssertionFailedError e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }

      // negative test
      try {
         BeanTester.assertGettersGetAndSettersSet(new InterfaceFieldBean());
      } catch (AssertionFailedError e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }
   }

   /**
    * Test unmatched bean that has no matching getters and setters
    * 
    * @throws ReflectionException
    * @throws TestBeanExceptionTwo
    */
   @Test
   public void testUnmatchedBeanWithAssert() throws ReflectionException, TestBeanExceptionTwo {

      // Expected results
      String expectedMessage = "Bean contains no paired Getters and Setters for " + UnmatchedBean.class.getName();

      // negative test
      try {
         BeanTester.assertGettersGetAndSettersSet(new UnmatchedBean());
         Assert.fail("An " + AssertionFailedError.class.getName() + " should have been thrown");
      } catch (AssertionFailedError e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }
   }

   /**
    * Test a bean with some matches that return unmatched values
    * 
    * @throws ReflectionException
    * @throws TestBeanExceptionTwo
    */
   @Test
   public void testBadBeanWithAssert() throws ReflectionException, TestBeanExceptionTwo {

      // Expected results
      String expectedMessage = "Paired Getters and Setters do not match for " + BadBean.class.getName();

      // negative test
      try {
         BeanTester.assertGettersGetAndSettersSet(new BadBean());
         Assert.fail("An " + AssertionFailedError.class.getName() + " should have been thrown");
      } catch (AssertionFailedError e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }
   }

}