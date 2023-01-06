package uk.co.polycode.mdcms.util.security;


import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.polycode.mdcms.util.io.FileLikePathService;
import uk.co.polycode.mdcms.util.io.UtilConstants;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyRep;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;

// See: https://github.com/jwtk/jjwt
public class JwtHelper {

   private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

   public static final String testPrefix = "urn:diyaccounting.co.uk:test:";

   public String generateSecretKeyString(final String alg) throws IOException {
      Key key = Keys.secretKeyFor(SignatureAlgorithm.valueOf(alg));
      String keyString = secretKeyToSerialisedString(key);
      return keyString;
   }

   public KeyPair generateKeyPair(final String alg) throws IOException {
      KeyPair keyPair = Keys.keyPairFor(SignatureAlgorithm.valueOf(alg));
      return keyPair;
   }

   public PublicKey getRsaPublicKey(final FileLikePathService fileLikePathService, final String publicKeyId)
         throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
      InputStream is = fileLikePathService.getInputStreamForUrn(publicKeyId);
      byte[] keyBytes = is.readAllBytes();
      X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      PublicKey publicKey = kf.generatePublic(spec);
      return publicKey;
   }

   public PrivateKey getRsaPrivateKey(final FileLikePathService fileLikePathService, final String privateKeyId)
         throws IOException, NoSuchAlgorithmException, InvalidKeySpecException {
      InputStream is = fileLikePathService.getInputStreamForUrn(privateKeyId);
      byte[] keyBytes = is.readAllBytes();
      PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
      KeyFactory kf = KeyFactory.getInstance("RSA");
      PrivateKey privateKey = kf.generatePrivate(spec);
      return privateKey;
   }

   public Key deserialiseKeyObject(final String keyString) throws IOException, ClassNotFoundException {
      byte [] data = Base64.decodeBase64(keyString);
      ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
      Object o = ois.readObject();
      ois.close();
      Key key = (Key)o;
      return key;
   }

   public String secretKeyToSerialisedString(final Key key) throws IOException {
      KeyRep keyRep = new KeyRep(KeyRep.Type.SECRET, key.getAlgorithm(), key.getFormat(), key.getEncoded());
      String keyString = keyToSerialisedString(keyRep);
      return keyString;
   }

   public String privateKeyToSerialisedString(final Key key) throws IOException {
      KeyRep keyRep = new KeyRep(KeyRep.Type.PRIVATE, key.getAlgorithm(), key.getFormat(), key.getEncoded());
      String keyString = keyToSerialisedString(keyRep);
      return keyString;
   }

   public String publicKeyToSerialisedString(final Key key) throws IOException {
      KeyRep keyRep = new KeyRep(KeyRep.Type.PUBLIC, key.getAlgorithm(), key.getFormat(), key.getEncoded());
      String keyString = keyToSerialisedString(keyRep);
      return keyString;
   }

   private String keyToSerialisedString(final KeyRep keyRep) throws IOException {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      ObjectOutputStream oos = new ObjectOutputStream( baos );
      oos.writeObject(keyRep);
      oos.close();
      String keyString = Base64.encodeBase64String(baos.toByteArray());
      return keyString;
   }

   public String getStringForUrn(final FileLikePathService fileLikePathService, final String keyId, final HashMap<String, String> testKeys) throws IOException {
      if(keyId.startsWith(this.testPrefix)){
         return testKeys.get(keyId);
      }else {
         return this.getStringForUrn(fileLikePathService, keyId);
      }
   }

   public String getStringForUrn(final FileLikePathService fileLikePathService, final String keyId) throws IOException {
      StringWriter writer = this.getWriterForUrn(fileLikePathService, keyId);
      return writer.toString();
   }

   // TODO: Java Key Store support
   public StringWriter getWriterForUrn(final FileLikePathService fileLikePathService, final String keyId) throws IOException {
      InputStream is;
      StringWriter writer = new StringWriter();
      is = fileLikePathService.getInputStreamForUrn(keyId);
      IOUtils.copy(is, writer, UtilConstants.DEFAULT_ENCODING);
      return writer;
   }
}
