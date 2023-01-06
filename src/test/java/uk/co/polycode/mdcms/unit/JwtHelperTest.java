package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.security.BasicJwt;
import uk.co.polycode.mdcms.util.security.RegExListCustomClaimJwt;
import uk.co.polycode.mdcms.util.security.JwtHelper;

import java.io.IOException;
import java.security.Key;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.util.HashMap;
import java.util.UUID;

public class JwtHelperTest {

   private static final Logger logger = LoggerFactory.getLogger(JwtHelperTest.class);

   private final JwtHelper jwtHelper = new JwtHelper();

   private final FileLikePathService fileLikePathService = new FileLikePathService();

   @Test
   public void testSecretKeyGeneration() throws IOException, ClassNotFoundException {

      // Test parameters
      String alg = "HS256";
      String expectedKeyString = this.jwtHelper.generateSecretKeyString(alg);

      // Expected results

      // Instance to test
      Key classUnderTest = this.jwtHelper.deserialiseKeyObject(expectedKeyString);

      // Execute test
      String actualKeyString = this.jwtHelper.secretKeyToSerialisedString(classUnderTest);
      logger.debug("Created secret key: {}", actualKeyString);
      Key idempotentKey = this.jwtHelper.deserialiseKeyObject(actualKeyString);
      String idempotentKeyString = this.jwtHelper.secretKeyToSerialisedString( idempotentKey);

      // Check
      Assert.assertEquals(expectedKeyString, actualKeyString);
      Assert.assertEquals(idempotentKeyString, actualKeyString);
   }

   @Test
   public void testLoadKeyTextFromClasspathUrn() throws IOException, ClassNotFoundException {

      // Test parameters
      String keyResource = "/id_rsa256_test.pub.txt";
      String expectedKeyId = "urn:diyaccounting.co.uk:classpath:" + keyResource;

      // Expected results

      // Instance to test
      String keyString = this.jwtHelper.getStringForUrn(this.fileLikePathService, expectedKeyId);

      // Execute test
      Key classUnderTest = this.jwtHelper.deserialiseKeyObject(keyString);

      // Check
      Assert.assertNotNull(classUnderTest);
   }

   @Test
   public void testLoadTokenFromClasspathUrn() throws IOException, ClassNotFoundException {

      // Test parameters
      String tokenResource = "/jwt.id_rsa256_openssl.token";
      String tokenId = "urn:diyaccounting.co.uk:classpath:" + tokenResource;

      // Expected results

      // Instance to test
      String token = this.jwtHelper.getStringForUrn(this.fileLikePathService, tokenId);

      // Execute test
      logger.debug("Read token {}", token);

      // Check
      Assert.assertNotNull(token);
   }

   @Test
   public void testLoadKeyTextFromFileUrn() throws IOException, ClassNotFoundException {

      // Test parameters
      String localPath = JwtHelperTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
      String keyFilepath = localPath + "id_rsa256_test.pub.txt";
      String expectedKeyId = "urn:diyaccounting.co.uk:file://" + keyFilepath;

      // Expected results

      // Instance to test
      String keyString = this.jwtHelper.getStringForUrn(this.fileLikePathService, expectedKeyId);

      // Execute test
      Key classUnderTest = this.jwtHelper.deserialiseKeyObject(keyString);

      // Check
      Assert.assertNotNull(classUnderTest);
   }

   @Test
   public void testLoadKeyBinaryFromClasspathUrn()
         throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {

      // https://gist.github.com/zenthangplus/55dfbe88c7c6a0633e07c01fd737be70
      // ssh-keygen -t rsa -b 4096 -f id_rsa256_openssl
      // openssl rsa -in id_rsa256_openssl -pubout -outform PEM -out id_rsa256_openssl.pub
      // cat id_rsa256_openssl
      // cat id_rsa256_openssl.pub

      // https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file
      // openssl genrsa -out id_rsa256_openssl.pem 2048
      // openssl pkcs8 -topk8 -inform PEM -outform DER -in id_rsa256_openssl.pem -out id_rsa256_openssl.der -nocrypt
      // openssl rsa -in id_rsa256_openssl.pem -pubout -outform DER -out id_rsa256_openssl.pub.der

      // Test parameters
      String publicKeyResource = "/id_rsa256_openssl.der.pub";
      String privateKeyResource = "/id_rsa256_openssl.der";
      String publicKeyId = "urn:diyaccounting.co.uk:classpath:" + publicKeyResource;
      String privateKeyId = "urn:diyaccounting.co.uk:classpath:" + privateKeyResource;

      // Expected results

      // Instance to test

      // Execute test
      PublicKey publicKey = this.jwtHelper.getRsaPublicKey(this.fileLikePathService, publicKeyId);
      PrivateKey privateKey = this.jwtHelper.getRsaPrivateKey(this.fileLikePathService, privateKeyId);

      // Check
      Assert.assertNotNull(publicKey);
      Assert.assertNotNull(privateKey);
   }

   @Test
   public void testCreateMinimalUnsignedJws() throws IOException, ClassNotFoundException {

      // Test parameters
      int expirySeconds = 60;
      String alg = "none";
      String iss = "https://diyaccounting.co.uk/";

      // Expected results

      // Instance to test
      BasicJwt classUnderTest = this.createBasicJwt(alg, expirySeconds, iss);

      // Execute test
      String token = classUnderTest.buildUnsignedJwt();
      logger.debug("Created token: {}", token);
      BasicJwt actualJwt = new BasicJwt();
      actualJwt.parseUnsignedJwt(token);

      // Check
      Assert.assertNotNull(actualJwt);
      Assert.assertEquals(iss, actualJwt.iss);
   }

   @Test
   public void testCreateMaximalUnsignedJws() throws IOException, ClassNotFoundException {

      // Test parameters
      int expirySeconds = 60;
      String alg = "none";
      String iss = "https://diyaccounting.co.uk/";
      String cty = "JWT";
      String aud = UUID.randomUUID().toString();
      String sub = UUID.randomUUID().toString();
      String jti = UUID.randomUUID().toString();

      // Expected results

      // Instance to test
      BasicJwt classUnderTest = this.createBasicJwt(alg, expirySeconds, iss);
      classUnderTest.cty = cty;
      classUnderTest.aud = aud;
      classUnderTest.sub = sub;
      classUnderTest.jti = jti;

      // Execute test
      String token = classUnderTest.buildUnsignedJwt();
      logger.debug("Created token: {}", token);
      BasicJwt actualJwt = new BasicJwt();
      actualJwt.parseUnsignedJwt(token);

      // Check
      Assert.assertNotNull(actualJwt);
      Assert.assertEquals(iss, actualJwt.iss);
      Assert.assertEquals(cty, actualJwt.cty);
      Assert.assertEquals(aud, actualJwt.aud);
      Assert.assertEquals(sub, actualJwt.sub);
      Assert.assertEquals(jti, actualJwt.jti);
   }

   @Test
   public void testExpiredUnsignedJws() throws IOException, ClassNotFoundException {

      // Test parameters
      int expirySeconds = 60;
      long nowMillis = System.currentTimeMillis();
      String alg = "none";
      String iss = "https://diyaccounting.co.uk/";

      // Expected results

      // Instance to test
      BasicJwt classUnderTest = this.createBasicJwt(alg, expirySeconds, iss);

      // Execute test
      String token = classUnderTest.buildUnsignedJwt();
      logger.debug("Created token: {}", token);
      BasicJwt actualJwt = new BasicJwt();
      actualJwt.parseUnsignedJwt(token);

      // Check
      Assert.assertNotNull(actualJwt);
      Assert.assertEquals(iss, actualJwt.iss);
      Assert.assertFalse(actualJwt.isExpiredJwt(nowMillis));
      Assert.assertTrue(actualJwt.isExpiredJwt(nowMillis + ((expirySeconds+100) * 1000L)));
   }

   @Test
   public void testCreateUnsignedJwsWithCustomPayload() throws IOException, ClassNotFoundException {

      // Test parameters
      int expirySeconds = 60;
      String alg = "none";
      String iss = "https://diyaccounting.co.uk/";
      String s1 = UUID.randomUUID().toString();

      // Expected results
      String s2 = UUID.randomUUID().toString();
      String s3 = UUID.randomUUID().toString();

      // Instance to test
      RegExListCustomClaimJwt classUnderTest = this.createRegExCustomClaimJwt(alg, expirySeconds, iss);
      classUnderTest.custom.add(s1);
      classUnderTest.custom.add(s2);

      // Execute test
      String token = classUnderTest.buildUnsignedJwt();
      logger.debug("Created token: {}", token);
      RegExListCustomClaimJwt actualJwt = new RegExListCustomClaimJwt();
      actualJwt.parseUnsignedJwt(token);

      // Check
      Assert.assertNotNull(actualJwt);
      Assert.assertEquals(iss, actualJwt.iss);
      Assert.assertEquals(2, actualJwt.custom.size());
      Assert.assertEquals(s1, actualJwt.custom.get(0));
      Assert.assertNotEquals(s2, actualJwt.custom.get(0));

      Assert.assertEquals(s1, actualJwt.custom.get(0));
      Assert.assertNotEquals(s3, actualJwt.custom.get(0));
      Assert.assertEquals(s2, actualJwt.custom.get(1));
      Assert.assertNotEquals(s3, actualJwt.custom.get(1));
   }

   @Test
   public void testCreateUnsignedJwsWithCustomPayloadApiRegEx() throws IOException, ClassNotFoundException {

      // Test parameters
      int expirySeconds = 60;
      String alg = "none";
      String iss = "https://diyaccounting.co.uk/";
      String matchingPath = "/my-api/endpoint";
      String nonMatchingPath = "/nonmatching/endpoint";
      String allowedPath = "/my-api/.*";
      String extraPath = "/extra-api/.*";

      // Expected results

      // Instance to test
      RegExListCustomClaimJwt classUnderTest = this.createRegExCustomClaimJwt(alg, expirySeconds, iss);
      classUnderTest.custom.add(allowedPath);
      classUnderTest.custom.add(extraPath);

      // Execute test
      String token = classUnderTest.buildUnsignedJwt();
      logger.debug("Created token: {}", token);
      RegExListCustomClaimJwt actualJwt = new RegExListCustomClaimJwt();
      actualJwt.parseUnsignedJwt(token);

      // Check
      Assert.assertEquals(actualJwt.custom.size(), 2);
      Assert.assertTrue(actualJwt.matchesAtLeastOneCustomClaim(matchingPath));
      Assert.assertFalse(actualJwt.matchesAtLeastOneCustomClaim(nonMatchingPath));
   }

   @Test
   public void testSymmetricallySignedBasicJwt() throws IOException, ClassNotFoundException {

      // Test parameters
      int expirySeconds = 60;
      String alg = "HS256";
      String iss = "https://diyaccounting.co.uk/";
      String signingKeyId = "urn:diyaccounting.co.uk:test:" + UUID.randomUUID().toString();
      String signingKeyString = this.jwtHelper.generateSecretKeyString(alg);
      logger.debug("Created signing key: {}", signingKeyString);
      HashMap<String, String> signingKeys = new HashMap<>();
      signingKeys.put(signingKeyId, signingKeyString);
      Key signingKey = this.jwtHelper.deserialiseKeyObject(signingKeyString);

      // Expected results

      // Instance to test
      BasicJwt classUnderTest = this.createBasicJwt(alg, expirySeconds, iss);

      // Execute test
      String token = classUnderTest.buildAndSignJwtUsingTestKeys(this.fileLikePathService, signingKeyId, signingKeys);
      logger.debug("Created token: {}", token);
      BasicJwt actualJwt = new BasicJwt();
      actualJwt.parseSignedJwtUsingTestKeys(this.fileLikePathService, token, signingKeys);
      String validationKeyId = actualJwt.kid;
      String validationKeyString = signingKeys.get(validationKeyId);
      Assert.assertEquals("Symmetric keys should match", signingKeyString, validationKeyString);

      // Check
      Assert.assertNotNull(actualJwt);
      Assert.assertEquals(iss, actualJwt.iss);
   }

   @Test
   public void testSymmetricallySignedCustomJwt() throws IOException, ClassNotFoundException {

      // Test parameters
      long nowMillis = System.currentTimeMillis();
      int expirySeconds = 60;
      String alg = "HS256";
      String iss = "https://diyaccounting.co.uk/";
      String s1 = UUID.randomUUID().toString();
      String s2 = UUID.randomUUID().toString();
      String signingKeyId = "urn:diyaccounting.co.uk:test:" + UUID.randomUUID().toString();
      String signingKeyString = this.jwtHelper.generateSecretKeyString(alg);
      logger.debug("Created signing key: {}", signingKeyString);
      HashMap<String, String> signingKeys = new HashMap<>();
      signingKeys.put(signingKeyId, signingKeyString);
      Key signingKey = this.jwtHelper.deserialiseKeyObject(signingKeyString);

      // Expected results

      // Instance to test
      RegExListCustomClaimJwt classUnderTest = this.createRegExCustomClaimJwt(alg, expirySeconds, iss);
      classUnderTest.custom.add(s1);

      // Execute test
      String token = classUnderTest.buildAndSignJwtUsingTestKeys(this.fileLikePathService, signingKeyId, signingKeys);
      logger.debug("Created token: {}", token);
      RegExListCustomClaimJwt actualJwt = new RegExListCustomClaimJwt();
      actualJwt.parseSignedJwtUsingTestKeys(this.fileLikePathService, token, signingKeys);
      String validationKeyId = actualJwt.kid;
      String validationKeyString = signingKeys.get(validationKeyId);
      Assert.assertEquals("Symmetric keys should match", signingKeyString, validationKeyString);

      // Check
      Assert.assertFalse(actualJwt.isExpiredJwt(nowMillis));
      Assert.assertTrue(actualJwt.isExpiredJwt(nowMillis + ((expirySeconds+100) * 1000L)));
      Assert.assertEquals(1, actualJwt.custom.size());
      Assert.assertEquals(s1, actualJwt.custom.get(0));
      Assert.assertNotEquals(s2, actualJwt.custom.get(0));
   }

   @Test
   public void testAsymmetricallySignedBasicJwt() throws IOException, ClassNotFoundException {

      // Test parameters
      int expirySeconds = 60;
      String alg = "RS256";
      String iss = "https://diyaccounting.co.uk/";
      String signingKeyId = "urn:diyaccounting.co.uk:test:" + UUID.randomUUID().toString();
      KeyPair keyPair = this.jwtHelper.generateKeyPair(alg);
      PrivateKey privateKey = keyPair.getPrivate();
      PublicKey publicKey = keyPair.getPublic();
      String privateKeyString = this.jwtHelper.privateKeyToSerialisedString(privateKey);
      String publicKeyString = this.jwtHelper.publicKeyToSerialisedString(publicKey);
      logger.debug("Created validation key: {}", publicKeyString);
      HashMap<String, String> signingKeys = new HashMap<>();
      signingKeys.put(signingKeyId, privateKeyString);
      HashMap<String, String> validationKeys = new HashMap<>();
      validationKeys.put(signingKeyId + ".pub", publicKeyString);
      String signingKeyString = privateKeyString;
      logger.debug("Created signing key: {}", signingKeyString);
      Key signingKey = this.jwtHelper.deserialiseKeyObject(signingKeyString);

      // Expected results

      // Instance to test
      BasicJwt classUnderTest = this.createBasicJwt(alg, expirySeconds, iss);
      classUnderTest.kid = signingKeyId;

      // Execute test
      String token = classUnderTest.buildAndSignJwtUsingTestKeys(this.fileLikePathService, signingKeyId, signingKeys);
      logger.debug("Created token: {}", token);
      BasicJwt actualJwt = new BasicJwt();
      actualJwt.parseSignedJwtUsingTestKeys(this.fileLikePathService, token, validationKeys);
      String validationKeyId = actualJwt.kid;
      String validationKeyString = signingKeys.get(validationKeyId);
      logger.debug("Obtained validation key: {}", validationKeyString);
      Assert.assertNotEquals("Asymmetric keys should not match", signingKeyString, validationKeyString);

      // Check
      Assert.assertNotNull(actualJwt);
      Assert.assertEquals(iss, actualJwt.iss);
   }

   @Test
   public void testAsymmetricallySignedBasicJwtWithGeneratedKey()
         throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {

      // https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file
      // openssl genrsa -out id_rsa256_openssl.pem 2048
      // openssl pkcs8 -topk8 -inform PEM -outform DER -in id_rsa256_openssl.pem -out id_rsa256_openssl.der -nocrypt
      // openssl rsa -in id_rsa256_openssl.pem -pubout -outform DER -out id_rsa256_openssl.der.pub

      // Test parameters
      int expirySeconds = 60;
      String alg = "RS256";
      String iss = "https://diyaccounting.co.uk/";
      String publicKeyResource  = "/id_rsa256_openssl.der.pub";
      String privateKeyResource = "/id_rsa256_openssl.der";
      String publicKeyId = "urn:diyaccounting.co.uk:classpath:" + publicKeyResource;
      String privateKeyId = "urn:diyaccounting.co.uk:classpath:" + privateKeyResource;
      String signingKeyId = privateKeyId;
      PrivateKey signingKey = this.jwtHelper.getRsaPrivateKey(this.fileLikePathService, signingKeyId);
      Assert.assertNotNull(signingKey);
      String signingKeyString = this.jwtHelper.privateKeyToSerialisedString(signingKey);

      // Expected results

      // Instance to test
      BasicJwt classUnderTest = this.createBasicJwt(alg, expirySeconds, iss);
      classUnderTest.kid = signingKeyId;

      // Execute test
      String token = classUnderTest.buildAndSignJwtWithPrivateKey(this.fileLikePathService, signingKeyId);
      logger.debug("Created token: {}", token);
      BasicJwt actualJwt = new BasicJwt();
      actualJwt.parseSignedJwt(this.fileLikePathService, token);
      String validationKeyId = actualJwt.kid;
      Assert.assertEquals("JWT should have the public key id", publicKeyId, validationKeyId);
      Assert.assertNotEquals("JWT should not have the private key id", privateKeyId, validationKeyId);
      PublicKey validationKey = this.jwtHelper.getRsaPublicKey(this.fileLikePathService, validationKeyId);
      Assert.assertNotNull(validationKey);
      String validationKeyString = this.jwtHelper.publicKeyToSerialisedString(validationKey);
      Assert.assertNotEquals("Asymmetric keys should not match", signingKeyString, validationKeyString);

      // Check
      Assert.assertNotNull(actualJwt);
      Assert.assertEquals(iss, actualJwt.iss);
   }

   @Test
   public void testAsymmetricallySignedCustomJwtWithGeneratedRegEx()
         throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {

      // https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file
      // openssl genrsa -out id_rsa256_openssl.pem 2048
      // openssl pkcs8 -topk8 -inform PEM -outform DER -in id_rsa256_openssl.pem -out id_rsa256_openssl.der -nocrypt
      // openssl rsa -in id_rsa256_openssl.pem -pubout -outform DER -out id_rsa256_openssl.der.pub

      // Test parameters
      int expirySeconds = 60;
      String alg = "RS256";
      String iss = "https://diyaccounting.co.uk/";
      String matchingPath = "/my-api/endpoint";
      String nonMatchingPath = "/nonmatching/endpoint";
      String allowedPath = "/my-api/.*";
      String extraPath = "/my-extra-api/.*";
      String publicKeyResource  = "/id_rsa256_openssl.der.pub";
      String privateKeyResource = "/id_rsa256_openssl.der";
      String publicKeyId = "urn:diyaccounting.co.uk:classpath:" + publicKeyResource;
      String privateKeyId = "urn:diyaccounting.co.uk:classpath:" + privateKeyResource;
      String signingKeyId = privateKeyId;

      // Expected results

      // Instance to test
      RegExListCustomClaimJwt classUnderTest = this.createRegExCustomClaimJwt(alg, expirySeconds, iss);
      classUnderTest.kid = signingKeyId;
      classUnderTest.custom.add(allowedPath);
      classUnderTest.custom.add(extraPath);

      // Execute test
      String generatedToken = classUnderTest.buildAndSignJwtWithPrivateKey(this.fileLikePathService, signingKeyId);
      logger.debug("Created token: {}", generatedToken);
      RegExListCustomClaimJwt actualJwtFromGeneratedToken = new RegExListCustomClaimJwt();
      actualJwtFromGeneratedToken.parseSignedJwt(this.fileLikePathService, generatedToken);

      // Check
      Assert.assertEquals(2, actualJwtFromGeneratedToken.custom.size());
      Assert.assertTrue(actualJwtFromGeneratedToken.matchesAtLeastOneCustomClaim(matchingPath));
      Assert.assertFalse(actualJwtFromGeneratedToken.matchesAtLeastOneCustomClaim(nonMatchingPath));
   }

   /**
    * This test demonstrates typical usage with paths matched against RegEx custom claims and keys located via urn
    */
   @Test
   public void testAsymmetricallySignedCustomJwtWithRegEx()
         throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {

      // https://stackoverflow.com/questions/11410770/load-rsa-public-key-from-file
      // openssl genrsa -out id_rsa256_openssl.pem 2048
      // openssl pkcs8 -topk8 -inform PEM -outform DER -in id_rsa256_openssl.pem -out id_rsa256_openssl.der -nocrypt
      // openssl rsa -in id_rsa256_openssl.pem -pubout -outform DER -out id_rsa256_openssl.der.pub

      // https://stackoverflow.com/questions/58313106/create-rs256-jwt-in-bash
      // ./generate-jwt.sh > jwt.id_rsa256_openssl.token

      // Test parameters
      String tokenResource = "/jwt.id_rsa256_openssl.token";
      String tokenId = "urn:diyaccounting.co.uk:classpath:" + tokenResource;
      String matchingPath = "/my-api/endpoint";
      String nonMatchingPath = "/nonmatching/endpoint";

      // Expected results

      // Instance to test

      // Execute test
      String actualToken = this.jwtHelper.getStringForUrn(this.fileLikePathService, tokenId);
      logger.debug("Read token: {}", actualToken);
      RegExListCustomClaimJwt actualJwtFromToken = new RegExListCustomClaimJwt();
      actualJwtFromToken.parseSignedJwt(this.fileLikePathService, actualToken);

      // Check
      Assert.assertEquals(1, actualJwtFromToken.custom.size());
      Assert.assertTrue(actualJwtFromToken.matchesAtLeastOneCustomClaim(matchingPath));
      Assert.assertFalse(actualJwtFromToken.matchesAtLeastOneCustomClaim(nonMatchingPath));
   }

   public BasicJwt createBasicJwt(final String alg, final long expirySeconds, final String iss) {
      BasicJwt jwt = new BasicJwt();
      populateBasicJwt(alg, expirySeconds, iss, jwt);
      return jwt;
   }

   public RegExListCustomClaimJwt createRegExCustomClaimJwt(final String alg,
                                                            final long expirySeconds,
                                                            final String iss) {
      RegExListCustomClaimJwt jwt = new RegExListCustomClaimJwt();
      populateBasicJwt(alg, expirySeconds, iss, jwt);
      return jwt;
   }

   private void populateBasicJwt(final String alg, final long expirySeconds, final String iss, final BasicJwt jwt) {
      jwt.alg = alg;
      jwt.typ = "JWT";
      jwt.iss = iss;
      jwt.aud = iss;
      jwt.sub = iss;
      jwt.exp = jwt.iat + expirySeconds;
   }
}
