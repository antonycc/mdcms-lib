package uk.co.diyaccounting.util.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Service;

import uk.co.diyaccounting.util.io.UtilConstants;

/**
 * Utility class to manage data sets
 */
@Service("hash")
public class HashHelper {

   /**
    * A logger from java logging
    */
   // private static Logger logger = LoggerFactory.getLogger(HashHelper.class);

   /**
    * Create a new default instance of the Helper. Normally the instance would be used directly
    */
   public HashHelper() {
   }

   /**
    * Compute SHA-256 has for the given string
    * 
    * @param s
    *           the string to compute the hash from
    * @return a SHA-256 has of the string
    */
   public String getHash(final String s) {

      return this.getHash(UtilConstants.DIGEST_ALGORITHM, s);
   }

   /**
    * Compute a hash using the given algorithm and the given string
    * 
    * @param algorithm
    *           the hashing algorithm to use
    * @param s
    *           the string to compute the hash from
    * @return a SHA-256 has of the string
    */
   public String getHash(final String algorithm, final String s) {
      return this.getHash(algorithm, s, UtilConstants.DEFAULT_ENCODING);
   }

   /**
    * Compute a hash using the given algorithm and the given string
    * 
    * @param algorithm
    *           the hashing algorithm to use
    * @param s
    *           the string to compute the hash from
    * @param encoding the encoding to use
    * @return a SHA-256 has of the string
    */
   public String getHash(final String algorithm, final String s, final String encoding) {
      try {
         MessageDigest md = MessageDigest.getInstance(algorithm);
         md.update(s.getBytes(encoding));
         byte[] hash = md.digest();
         String hex = Hex.encodeHexString(hash);
         return hex;
      } catch (UnsupportedEncodingException e) {
         throw new IllegalArgumentException("Encoding [" + encoding + "] not supported", e);
      } catch (NoSuchAlgorithmException e) {
         throw new IllegalArgumentException("Algorithm [" + algorithm + "] not found", e);
      }
   }

}
