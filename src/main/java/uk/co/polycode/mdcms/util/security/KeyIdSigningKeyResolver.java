package uk.co.polycode.mdcms.util.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.SigningKeyResolverAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.security.Key;
import java.util.HashMap;

// See: https://github.com/jwtk/jjwt
public class KeyIdSigningKeyResolver extends SigningKeyResolverAdapter {

   private static final Logger logger = LoggerFactory.getLogger(KeyIdSigningKeyResolver.class);

   // Tests can directly add an id:keyText pair which will match if the key id is the expected key id.
   public HashMap<String, String> testKeys = new HashMap<String, String>();

   public JwtHelper jwtHelper = new JwtHelper();
   public FileLikePathService fileLikePathService;

   @Override
   public Key resolveSigningKey(JwsHeader jwsHeader, Claims claims) {
      //inspect the header or claims, lookup and return the signing key
      String keyId = jwsHeader.getKeyId(); //or any other field that you need to inspect
      try {
         if(this.testKeys.isEmpty()){
            return jwtHelper.getRsaPublicKey(fileLikePathService, keyId);
         }else {
            String keyString = this.jwtHelper.getStringForUrn(fileLikePathService, keyId, this.testKeys);
            return this.jwtHelper.deserialiseKeyObject(keyString);
         }
      }catch (Exception e){
         logger.error("Could not extract key for id " + keyId, e);
         return null;
      }
   }
}
