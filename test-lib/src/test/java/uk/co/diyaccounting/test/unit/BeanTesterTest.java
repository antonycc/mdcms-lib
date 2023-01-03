package uk.co.diyaccounting.test.unit;

import org.junit.Assert;
import junit.framework.AssertionFailedError;

import org.junit.Before;
import org.junit.Test;

import uk.co.diyaccounting.test.BeanTester;
import uk.co.diyaccounting.test.ops.AnotherGoodBean;
import uk.co.diyaccounting.test.ops.BadBean;
import uk.co.diyaccounting.test.ops.GoodBean;
import uk.co.diyaccounting.test.ops.InterfaceFieldBean;
import uk.co.diyaccounting.test.ops.NoDefaultConstructorFieldBean;
import uk.co.diyaccounting.test.ops.ReflectionOnGetBean;
import uk.co.diyaccounting.test.ops.ReflectionOnSetBean;
import uk.co.diyaccounting.test.ops.StandaloneServiceImpl;
import uk.co.diyaccounting.test.ops.TestBeanException;
import uk.co.diyaccounting.test.ops.UnmatchedBean;
import uk.co.diyaccounting.util.reflect.ReflectionException;

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
    * @throws TestBeanException
    */
   @Test
   public void testGoodBeanWithAssert() throws ReflectionException, TestBeanException {

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
    * @throws TestBeanException
    */
   @Test
   public void testReflectionOnBeanWithAssert() throws ReflectionException, TestBeanException {

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
    * @throws TestBeanException
    */
   @Test
   public void testNoDefaultConstructorFieldBeanWithAssert() throws ReflectionException, TestBeanException {

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
    * @throws TestBeanException
    */
   @Test
   public void testUnmatchedBeanWithAssert() throws ReflectionException, TestBeanException {

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
    * @throws TestBeanException
    */
   @Test
   public void testBadBeanWithAssert() throws ReflectionException, TestBeanException {

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