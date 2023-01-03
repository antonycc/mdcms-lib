package uk.co.diyaccounting.util.unit;

import java.io.IOException;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import uk.co.diyaccounting.util.io.StringHelper;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class StringHelperTest {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(StringHelperTest.class);

   @Test
   public void expectSubstringAndSafeSubstringToMatch() throws IOException {

      // Test parameters
      String s1 = "";
      String s2 = "abcd";
      String s3 = "efgh";
      String s4 = "1";
      String s5 = UUID.randomUUID().toString() + UUID.randomUUID().toString();

      // Class under test
      StringHelper classUnderTest = new StringHelper();

      // Execute
      Assert.assertEquals(s1.substring(0), classUnderTest.safeSubString(s1, 0));
      Assert.assertEquals(s2.substring(2, 3), classUnderTest.safeSubString(s2, 2, 3));
      Assert.assertEquals(s3.substring(0, 0), classUnderTest.safeSubString(s3, 0, 0));
      Assert.assertEquals(s4.substring(1), classUnderTest.safeSubString(s4, 1));
      Assert.assertEquals(s5.substring(20, 30), classUnderTest.safeSubString(s5, 20, 30));
   }

   @Test
   public void expectNegativeStartAndEndsToBeZeroed() throws IOException {

      // Test parameters
      String s1 = "";
      String s2 = "abcd";
      String s3 = "efgh";
      String s4 = "1";
      String s5 = UUID.randomUUID().toString() + UUID.randomUUID().toString();

      // Class under test
      StringHelper classUnderTest = new StringHelper();

      // Execute
      Assert.assertEquals(s1.substring(0), classUnderTest.safeSubString(s1, -1));
      Assert.assertEquals(s2.substring(2, 3), classUnderTest.safeSubString(s2, 2, 3));
      Assert.assertEquals(s3.substring(0, 0), classUnderTest.safeSubString(s3, 0, -1));
      Assert.assertEquals(s4.substring(0), classUnderTest.safeSubString(s4, -1));
      Assert.assertEquals(s5.substring(20, 30), classUnderTest.safeSubString(s5, 20, 30));
   }

   @Test
   public void expectExcessStartAndEndsToBeBroughtInRange() throws IOException {

      // Test parameters
      String s1 = "";
      String s2 = "abcd";
      String s3 = "efgh";

      // Class under test
      StringHelper classUnderTest = new StringHelper();

      // Execute
      Assert.assertEquals(s1.substring(0), classUnderTest.safeSubString(s1, 1));
      Assert.assertEquals(s2.substring(2, 4), classUnderTest.safeSubString(s2, 2, 8));
      Assert.assertEquals(s3.substring(2, 2), classUnderTest.safeSubString(s3, 4, 2));
   }
}