package uk.co.diyaccounting.util.unit;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import uk.co.diyaccounting.util.ops.StubApplicationContext;
import uk.co.diyaccounting.util.reflect.ReflectionException;
import uk.co.diyaccounting.util.spring.ApplicationContextFactory;

/**
 * Create an application context using reflection so a mock can be injected
 * 
 * @author Antony
 */
public class ApplicationContextFactoryTest {

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   @Test
   public void expectTheContextToBeCreated() throws IOException {

      // Test parameters
      Class<?> clazz = StubApplicationContext.class;

      // Instance to test
      ApplicationContextFactory classUnderTest = new ApplicationContextFactory();
      classUnderTest.setClazz(clazz);

      // Execute
      ApplicationContext context = classUnderTest.createApplicationContext(null);

      // Check
      Assert.assertNotNull(context);
   }

   @Test(expected = ReflectionException.class)
   public void expectTheContextToFailToBeCreated() throws IOException {

      // Test parameters
      Class<?> clazz = this.getClass();

      // Instance to test
      ApplicationContextFactory classUnderTest = new ApplicationContextFactory();

      // Execute
      classUnderTest.createApplicationContext(null, clazz);

      // Check
   }
}