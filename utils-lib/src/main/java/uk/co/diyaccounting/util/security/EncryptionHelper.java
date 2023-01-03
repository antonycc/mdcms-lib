package uk.co.diyaccounting.util.security;

import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;

/**
 * Utility class to manage encryption
 */
@Service("encrypt")
public class EncryptionHelper {

   /**
    * A logger from java logging
    */
   // private static Logger logger = LoggerFactory.getLogger(EncryptionHelper.class);

   /**
    * Create a new default instance of the Helper. Normally the instance would be used directly
    */
   public EncryptionHelper() {
   }

	/**
	 * Encrypt a string for storage in the cookie
	 *
	 * @param keySeed the seed to initialise the cypher at a known state
	 * @param secretKey the key for use in this encryption
	 * @param encryptionAlgorithm the encryption algorthim, e.g. AES
	 * @param encryptionSpecification the encryption specification
	 * @param bytesToEncrypt the bytes to encrypt
	 *
	 * @return the supplied bytes encrypted as requested
	 */
	public byte[] encrypt(final byte[] keySeed, final byte[] secretKey, final String encryptionAlgorithm, final String encryptionSpecification,
	                      final byte[] bytesToEncrypt) {
		try {
			IvParameterSpec iv = new IvParameterSpec(keySeed);
			SecretKeySpec skeySpec = new SecretKeySpec(secretKey, encryptionAlgorithm);
			Cipher cipher = Cipher.getInstance(encryptionSpecification);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);
			return cipher.doFinal(bytesToEncrypt);
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException("Could not encrypt using: ("+encryptionAlgorithm+","+encryptionSpecification+")", e);
		}
	}

	/**
	 * Decrypt a string for storage in the cookie
	 *
	 * @param keySeed the seed to initialise the cypher at a known state
	 * @param secretKey the key for use in this encryption
	 * @param encryptionAlgorithm the encryption algorthim, e.g. AES
	 * @param encryptionSpecification the encryption specification
	 * @param bytesToDecrypt the bytes to decrypt
	 *
	 * @return the supplied bytes encrypted as requested
	 */
	public byte[] decrypt(final byte[] keySeed, final byte[] secretKey, final String encryptionAlgorithm, final String encryptionSpecification,
	                      final byte[] bytesToDecrypt) {
		try {
			IvParameterSpec iv = new IvParameterSpec(keySeed);
			SecretKeySpec skeySpec = new SecretKeySpec(secretKey, encryptionAlgorithm);
			Cipher cipher = Cipher.getInstance(encryptionSpecification);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			return cipher.doFinal(bytesToDecrypt);
		} catch (GeneralSecurityException e) {
			throw new IllegalArgumentException("Could not decrypt using: ("+encryptionAlgorithm+","+encryptionSpecification+")", e);
		}

	}
}
