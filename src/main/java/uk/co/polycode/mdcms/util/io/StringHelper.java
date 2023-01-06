package uk.co.polycode.mdcms.util.io;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Utility class to assist with string operations
 */
@Service("string")
public class StringHelper {

   /**
    * A logger from java logging
    */
   private static Logger logger = LoggerFactory.getLogger(StringHelper.class);

   /**
    * Log a simple string
    *
    * @param s the string to log
    */
   public Logger logString(final String s) {
      logger.info(s);
      return logger;
   }

   /**
    * Return a substring ensuring the start and end index fall within the bounds of the string
    * 
    * @param s
    *           the string to perform a substring on
    * @param begining
    *           the index of the start of the substring
    * 
    * @return the substring or the whole string if the index is too long ot too short
    */
   public String safeSubString(final String s, final int begining) {
      return this.safeSubString(s, begining, s.length());
   }

   /**
    * Return a substring ensuring the start and end index fall within the bounds of the string
    * 
    * @param s
    *           the string to perform a substring on
    * @param begining
    *           the index of the start of the substring
    * @param end
    *           the index of the extent of the substring
    * 
    * @return the substring or the whole string if the index is too long ot too short
    */
   public String safeSubString(final String s, final int begining, final int end) {
      int safeBegining;
      safeBegining = begining < 0 ? 0 : begining;
      safeBegining = safeBegining > s.length() ? s.length() : safeBegining;
      int safeEnd;
      safeEnd = end < 0 ? 0 : end;
      safeEnd = safeEnd > s.length() ? s.length() : safeEnd;
      safeEnd = safeEnd < safeBegining ? safeBegining : safeEnd;
      return s.substring(safeBegining, safeEnd);
   }

}
