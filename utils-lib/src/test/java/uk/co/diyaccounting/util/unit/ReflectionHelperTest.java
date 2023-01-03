package uk.co.diyaccounting.util.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.diyaccounting.util.ops.TestAbstract;
import uk.co.diyaccounting.util.ops.TestBean;
import uk.co.diyaccounting.util.ops.TestBeanException;
import uk.co.diyaccounting.util.reflect.ReflectionException;
import uk.co.diyaccounting.util.reflect.ReflectionHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Check negative and positive scenarios for using reflection
 * 
 * @author Antony
 */
public class ReflectionHelperTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(ReflectionHelperTest.class);

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Get a method successfully
    */
   @Test
   public void testGetMethod() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String name = "trim";

      // Instance to test
      Method method = reflect.getMethod(String.class, name);
      Assert.assertEquals("Method should be equals method", name, method.getName());
   }

   /**
    * Get a method unsuccessfully
    */
   @Test
   public void testGetMethodFails() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String name = "equalsX";
      String expectedMessage = "The class should define an accessible method called " + name;

      // Instance to test
      try {
         reflect.getMethod(String.class, name, Object.class);
         Assert.fail("Exception should have been thrown");
      } catch (ReflectionException e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }
   }

   /**
    * Get a constructor successfully
    */
   @Test
   public void testGetConstructor() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String name = "java.lang.String";

      // Instance to test
      Constructor<?> constructor = reflect.getConstructor(String.class, String.class);
      Assert.assertEquals("Method should be equals method", name, constructor.getName());
   }

   /**
    * Get a constructor unsuccessfully
    */
   @Test
   public void testConstructorFails() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String expectedMessage = "The class " + String.class.getName()
               + " should define an accessible constructor with argument supplied{"
               + Object.class + "}";

      // Instance to test
      try {
         reflect.getConstructor(String.class, Object.class);
         Assert.fail("Exception should have been thrown");
      } catch (ReflectionException e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }
   }

   /**
    * Get a constructor successfully
    */
   @Test
   public void testInvokeConstructor() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String expectedName = "java.lang.String";
      String expectedValue = "test";

      // Constructor to test
      Constructor<?> constructor = reflect.getConstructor(String.class, String.class);
      Assert.assertEquals("Method name should be constructor name", expectedName, constructor.getName());

      // Invoke
      Object obj = reflect.newInstance(constructor, expectedValue);
      Assert.assertEquals("Value should be the same as passed in argument", expectedValue, obj);

   }

   /**
    * Get a constructor unsuccessfully - invocation target
    */
   @Test
   public void testInvokeConstructorInvocationTargetExceptionFails() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String expectedName = TestBean.class.getName();
      Class<?> expectedException = InvocationTargetException.class;
      Class<?> expectedCauseException = TestBeanException.class;
      String initArg = "I.do.not.exist";
      String expectedMessage = "Argument was " + initArg;

      // Constructor to test
      Constructor<?> constructor = reflect.getConstructor(TestBean.class, String.class);
      Assert.assertEquals("Method name should be constructor name", expectedName, constructor.getName());

      // Invoke
      try {
         reflect.newInstance(constructor, initArg);
         Assert.fail("Exception should have been thrown");
      } catch (ReflectionException e) {
         Assert.assertEquals("Cause should be Invocation exception", expectedException.getName(), e.getCause()
                  .getClass().getName());
         Assert.assertEquals("Cause of cause should be " + expectedCauseException.getName(),
                  expectedCauseException.getName(), e
                  .getCause().getCause().getClass().getName());
         String actualMessage = e.getCause().getCause().getMessage();
         Assert.assertEquals("Message should be as expected", expectedMessage, actualMessage);
      }
   }

   /**
    * Get a constructor unsuccessfully - illegal access
    */
   @Test
   public void testInvokeConstructorIllegalAccessExceptionFails() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String expectedName = TestBean.class.getName();
      Class<?> expectedException = IllegalAccessException.class;
      String expectedMessage = "class " + reflect.getClass().getName() + " cannot access a member of class "
               + TestBean.class.getName()
               + " with modifiers \"protected\"";

      // Constructor to test
      Constructor<?> constructor = reflect.getConstructor(TestBean.class, Boolean.class);
      Assert.assertEquals("Method name should be constructor name", expectedName, constructor.getName());

      // Invoke
      try {
         reflect.newInstance(constructor, true);
         Assert.fail("Exception should have been thrown");
      } catch (ReflectionException e) {
         Assert.assertEquals("Cause should be IllegalAccessException", expectedException.getName(), e.getCause()
                  .getClass().getName());
         String actualMessage = e.getCause().getMessage();
         Assert.assertEquals("Message should be as expected", expectedMessage, actualMessage);
      }
   }

   /**
    * Get a constructor unsuccessfully - instantiation exception access
    */
   @Test
   public void testInvokeConstructorInstantiationExceptionFails() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String expectedName = TestAbstract.class.getName();
      Class<?> expectedException = InstantiationException.class;

      // Constructor to test
      Constructor<?> constructor = reflect.getConstructor(TestAbstract.class, String.class);
      Assert.assertEquals("Method name should be constructor name", expectedName, constructor.getName());

      // Invoke
      try {
         reflect.newInstance(constructor, "I.do.not.exist");
         Assert.fail("Exception should have been thrown");
      } catch (ReflectionException e) {
         Assert.assertEquals("Cause should be InstantiationException", expectedException.getName(), e.getCause()
                  .getClass().getName());
         String actualMessage = e.getCause().getMessage();
         Assert.assertNull("Message should be null", actualMessage);
      }
   }

   /**
    * Get a method successfully
    */
   @Test
   public void testInvokeMethod() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String expectedName = "trim";
      String expectedValue = "test";
      String obj = new String(" " + expectedValue + " ");

      // Method to test
      Method method = reflect.getMethod(obj.getClass(), expectedName);
      Assert.assertEquals("Method name should be as expected", expectedName, method.getName());

      // Invoke
      String result = (String) reflect.invoke(method, obj);
      Assert.assertEquals("Value should be the trimed version of as passed in argument", expectedValue, result);
   }

   /**
    * Get a method unsuccessfully - invocation target
    */
   @Test
   public void testInvokeMethodInvocationTargetExceptionFails() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String expectedName = "charAt";
      String obj = new String();
      Class<?> expectedException = InvocationTargetException.class;

      // Method to test
      Method method = reflect.getMethod(obj.getClass(), expectedName, int.class);
      Assert.assertEquals("Method name should be as expected", expectedName, method.getName());

      // Invoke
      try {
         reflect.invoke(method, obj, 0);
         Assert.fail("Exception should have been thrown");
      } catch (ReflectionException e) {
         Assert.assertEquals("Cause should be Invocation exception", expectedException.getName(), e.getCause()
                  .getClass().getName());
         String actualMessage = e.getCause().getMessage();
         Assert.assertNull("Message should be null", actualMessage);
      }
   }

   /**
    * Get a method unsuccessfully - illegal access
    */
   @Test
   public void testInvokeMethodIllegalAccessExceptionFails() throws TestBeanException, ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String expectedName = "protectedMethod";
      String booleanContent = "/test1/boolean.html";
      TestBean obj = new TestBean(booleanContent);
      Class<?> expectedException = IllegalAccessException.class;
      String expectedMessage = "class " + reflect.getClass().getName() + " cannot access a member of class "
               + TestBean.class.getName()
               + " with modifiers \"protected\"";

      // Method to test
      Method method = reflect.getMethod(obj.getClass(), expectedName);
      Assert.assertEquals("Method name should be as expected", expectedName, method.getName());

      // Invoke
      try {
         reflect.invoke(method, obj);
         Assert.fail("Exception should have been thrown");
      } catch (ReflectionException e) {
         Assert.assertEquals("Cause should be IllegalAccessException", expectedException.getName(), e.getCause()
                  .getClass().getName());
         String actualMessage = e.getCause().getMessage();
         Assert.assertEquals("Message should be as expected", expectedMessage, actualMessage);
      }
   }

   /**
    * Get a boxed type
    */
   /*@Test
   public void testGetBoxedType() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();
      ReflectionHelper.setInstance(reflect);
      reflect = ReflectionHelper.getInstance();

      // Expected results
      Class<?> expectedBoxedType = Boolean.class;

      // Instance to test
      Class<?> actualBoxedType = reflect.getBoxedType(boolean.class);
      Assert.assertEquals("Class should be as expected", expectedBoxedType, actualBoxedType);
   }*/

   /**
    * Get a class successfully
    */
   @Test
   public void testGetClass() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String name = String.class.getName();

      // Instance to test
      Class<?> clazz = reflect.getClass(name);
      Assert.assertEquals("Class should be as expected", name, clazz.getName());
   }

   /**
    * Get a method unsuccessfully
    */
   @Test
   public void testGetClassFails() throws ReflectionException {

      // Class under test
      ReflectionHelper reflect = new ReflectionHelper();

      // Expected results
      String name = "equalsX";
      String expectedMessage = name;

      // Instance to test
      try {
         reflect.getClass(name);
         Assert.fail("Exception should have been thrown");
      } catch (ReflectionException e) {
         Assert.assertEquals("Message should be as expected", expectedMessage, e.getMessage());
      }
   }

}