package uk.co.polycode.mdcms.unit;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.polycode.mdcms.util.io.UtilConstants;
import uk.co.polycode.mdcms.util.security.HashHelper;

import java.util.UUID;

/**
 * test hashing
 * 
 * @author Antony
 */
public class HashHelperTest {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(HashHelperTest.class);

   /**
    * Test simple 1 generation
    */
   @Test
   public void testSimple() {

      // Test parameters
      String s1 = UUID.randomUUID().toString();

      // Expected results

      // Instance to test
      HashHelper hash = new HashHelper();

      // Execute test
      HashHelperTest.logger.debug("Hashing: [" + s1 + "]");
      String h1 = hash.getHash(s1);
      HashHelperTest.logger.debug("Result:  [" + h1 + "]");

      // Check
      Assert.assertEquals("Re-hash should have the same result", hash.getHash(s1), h1);
      Assert.assertFalse("Generated data should not be the same as the hash", s1.equalsIgnoreCase(h1));
   }

	@Test
	public void expectTwoDifferentStringsToHaveDifferentHashes() {

		// Test parameters
		String s1 = "CommercialProduct:ProductComponent:Region:GBProductType:CompanyAccountingPeriod:2012-01-31 (Jan12) ProductComponent:Region:GBProductType:Payroll 05AccountingPeriod:2011-04-05 (Apr11) ProductComponent:Region:GBProductType:Payroll 05AccountingPeriod:2012-04-05 (Apr12)";
	   String s2 = "CommercialProduct:ProductComponent:Region:GBProductType:CompanyAccountingPeriod:2012-01-31 (Jan12)";

		// Expected results

		// Instance to test
		HashHelper classUnderTest = new HashHelper();

		// Execute test
		String h1 = classUnderTest.getHash(s1);
		String h1again = classUnderTest.getHash(s1);
		String h2 = classUnderTest.getHash(s2);
		String h2again = classUnderTest.getHash(s2);

		// Check
		Assert.assertEquals(h1, h1again);
		Assert.assertNotEquals(h1, h2);
		Assert.assertEquals(h2, h2again);
	}

   /**
    * Test with none existent algorithm
    */
   @Test(expected = IllegalArgumentException.class)
   public void testNoAlgorithm() {

      // Test parameters
      String algorithm = "no.such.algorithm";
      String s = "1234";

      // Expected results

      // Instance to test
      HashHelper hash = new HashHelper();

      // Execute test
      hash.getHash(algorithm, s);
   }

   /**
    * Test with none existent algorithm
    */
   @Test(expected = IllegalArgumentException.class)
   public void testUnSupportednEncoding() {

      new UtilConstants();
      // Test parameters
      String algorithm = UtilConstants.DIGEST_ALGORITHM;
      String s = "1234";

      // Expected results

      // Instance to test
      HashHelper hash = new HashHelper();

      // Execute test
      hash.getHash(algorithm, s, HashHelperTest.class.getSimpleName() + ".no.such.encoding");
   }

   /**
    * Test hashes are consistent
    */
   @Test
   public void testConsistency() {

      // Test parameters
      int iterations = 1000;
      String s1 = UUID.randomUUID().toString();

      // Expected results

      // Instance to test
      HashHelper hash = new HashHelper();

      // Execute test
      for (int i = 0; i < iterations; i++) {
         String s2 = s1;
         s1 = UUID.randomUUID().toString();

         // Check
         Assert.assertEquals(hash.getHash(s1), hash.getHash(s1));
         Assert.assertEquals(hash.getHash(s2), hash.getHash(s2));
         Assert.assertFalse(hash.getHash(s1).equalsIgnoreCase(hash.getHash(s2)));
      }

   }

   @Test
   public void printNullString() {
      String s = null;
      System.out.println(s);
   }

   @Test
   public void printNullStringConcatenated() {
      String s = null;
      System.out.println("" + s);
   }

   @Test
   public void printNullObject() {
      Object o = null;
      System.out.println(o);
   }

   @Test
   public void printNullObjectConcatenated() {
      Object o = null;
      System.out.println("" + o);
   }

   @Test
   public void printNullStringLength() {
      String s = null;
      System.out.println(StringUtils.length(s));
   }

   @Test
   public void printNullStringLengthConcatenated() {
      String s = null;
      System.out.println(StringUtils.length("" + s));
   }

   @Test
   public void printNullStringLengthConcatenated2() {
      String s = null;
      System.out.println(StringUtils.length("") + StringUtils.length(s));
   }

}
