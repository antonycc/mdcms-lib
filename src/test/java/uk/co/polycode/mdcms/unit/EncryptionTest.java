package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.polycode.mdcms.util.security.EncryptionHelper;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * Tests for the encryption
 * 
 * @author antony
 */
public class EncryptionTest {

	@Test
	public void expectStringToBeTheSameBeforeAndAfterEncode() {

		// Test parameters
		String s = UUID.randomUUID().toString();

		// Expected data

		// Mocks

		// Class under test
		//EncryptionHelper classUnderTest = new EncryptionHelper();

		// Execute
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		String actual = new String(b, StandardCharsets.UTF_8);
		Assert.assertEquals(s, actual);
	}

	@Test
	public void expectStringToBeTheSameBeforeAndAfterEncrypt() {

		// Test parameters
		String s = UUID.randomUUID().toString();
		byte[] keySeed = "89CDBFC265813E97".getBytes(StandardCharsets.UTF_8);
		byte[] secretKey = "E971E998CC1D489C".getBytes(StandardCharsets.UTF_8);
		String encryptionAlgorithm = "AES";
		String encryptionSpecification = "AES/CBC/PKCS5PADDING";

		// Expected data

		// Mocks

		// Class under test
		EncryptionHelper classUnderTest = new EncryptionHelper();

		// Execute
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		byte[] encryptedBytes = classUnderTest.encrypt(keySeed, secretKey, encryptionAlgorithm,
			encryptionSpecification, b);
		byte[] decryptedBytes = classUnderTest.decrypt(keySeed, secretKey, encryptionAlgorithm,
			encryptionSpecification, encryptedBytes);
		String actual = new String(decryptedBytes, StandardCharsets.UTF_8);
		Assert.assertEquals(s, actual);
	}

	@Test(expected = IllegalArgumentException.class)
	public void expectEncryptToFail() {

		// Test parameters
		String s = UUID.randomUUID().toString();
		byte[] keySeed = "89CDBFC265813E97".getBytes(StandardCharsets.UTF_8);
		byte[] secretKey = "E971E998CC1D489C".getBytes(StandardCharsets.UTF_8);
		String encryptionAlgorithm = "AES";
		String encryptionSpecification = "AES/CBC/PKCS5PADDING";

		// Expected data

		// Mocks

		// Class under test
		EncryptionHelper classUnderTest = new EncryptionHelper();

		// Execute
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		classUnderTest.encrypt(keySeed, secretKey, encryptionAlgorithm + "X", encryptionSpecification, b);
	}

	@Test(expected = IllegalArgumentException.class)
	public void expectDecryptToFail() {

		// Test parameters
		String s = UUID.randomUUID().toString();
		byte[] keySeed = "89CDBFC265813E97".getBytes(StandardCharsets.UTF_8);
		byte[] secretKey = "E971E998CC1D489C".getBytes(StandardCharsets.UTF_8);
		String encryptionAlgorithm = "AES";
		String encryptionSpecification = "AES/CBC/PKCS5PADDING";

		// Expected data

		// Mocks

		// Class under test
		EncryptionHelper classUnderTest = new EncryptionHelper();

		// Execute
		byte[] b = s.getBytes(StandardCharsets.UTF_8);
		byte[] encryptedBytes = classUnderTest.encrypt(keySeed, secretKey, encryptionAlgorithm,
			encryptionSpecification, b);
		classUnderTest.decrypt(keySeed, secretKey, encryptionAlgorithm + "X", encryptionSpecification, encryptedBytes);
	}

}
