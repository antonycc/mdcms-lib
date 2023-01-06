package uk.co.polycode.mdcms.util.time;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * Current server time according to Java - mainly to allow for mocking but also for future abstraction
 */
public class TimeService {

   public ZonedDateTime getTime() {
      LocalDateTime ldt = LocalDateTime.now();
      ZonedDateTime zdt = ldt.atZone(ZoneOffset.UTC);
      return zdt;
   }
}