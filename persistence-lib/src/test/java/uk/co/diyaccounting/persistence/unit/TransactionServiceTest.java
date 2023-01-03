package uk.co.diyaccounting.persistence.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.persistence.TransactionService;

import java.util.UUID;

/**
 * Tests for the completion controller
 *
 * @author antony
 */
public class TransactionServiceTest {

   @Test
   public void ensureTransactionGuidsAreNotTheSame() {

      // Test parameters
      String firstUuidAsString = UUID.randomUUID().toString();

      // Expected results

      // Mocks

      // Class under test
	   TransactionService classUnderTest = new TransactionService();

      // Loop 100 times to check
      for (int i = 0; i < 100; i++) {
         byte[] actualUuid = classUnderTest.getTransactionUuidAsByteArray();
         String actualUuidAsString = new String(actualUuid);
         Assert.assertNotEquals(firstUuidAsString, actualUuidAsString);
      }
   }

}
