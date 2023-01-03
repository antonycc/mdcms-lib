package uk.co.diyaccounting.util.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.util.time.TimeService;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Basic time checks
 *
 * @author Antony
 */
public class TimeTest {

   @Test
   public void expectThereToBeATimeInThePastAndFuture() {

      // Test parameters

      // Expected values

      // Create Object to test
      TimeService classUnderTest = new TimeService();

      // Check
      ZonedDateTime t1 = classUnderTest.getTime();
      ZonedDateTime t2 = classUnderTest.getTime();

      Assert.assertNotNull(t1);
      Assert.assertNotNull(t2);
      Assert.assertFalse(ChronoUnit.MINUTES.between(t2, t1) > 0);
   }

}
