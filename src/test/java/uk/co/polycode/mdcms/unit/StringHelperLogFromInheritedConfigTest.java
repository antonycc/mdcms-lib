package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import uk.co.polycode.mdcms.util.io.StringHelper;

import java.util.UUID;

/**
 * Check if the logging framework is active in the main classes
 * (Manual check for now)
 * 
 * @author Antony
 */
public class StringHelperLogFromInheritedConfigTest {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(StringHelperLogFromInheritedConfigTest.class);

   @Test
   public void expectStringToBeLoggedAtInfo() {

      // Test parameters
      String s1 = UUID.randomUUID().toString();

      // Class under test
      StringHelper classUnderTest = new StringHelper();

      // Execute
      Logger loggerUnderTest = classUnderTest.logString(s1);
      Assert.assertTrue("Info should be at least enabled", loggerUnderTest.isInfoEnabled());
   }
}