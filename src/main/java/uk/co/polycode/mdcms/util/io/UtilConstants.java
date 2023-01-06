package uk.co.polycode.mdcms.util.io;

import java.nio.charset.StandardCharsets;

/**
 * Encodings and algorithms used throughout the utils package
 * 
 * @author antony
 */
public class UtilConstants {

   /**
    * Files are expected to be UTF-8
    */
   public static final String DEFAULT_ENCODING = StandardCharsets.UTF_8.name();

   /**
    * Digest algorithm to used when computing hashes
    */
   public static final String DIGEST_ALGORITHM = "SHA-256";
}
