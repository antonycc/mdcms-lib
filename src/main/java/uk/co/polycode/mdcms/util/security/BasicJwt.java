package uk.co.polycode.mdcms.util.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.io.IOException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;
import java.util.HashMap;

// See: https://github.com/jwtk/jjwt
public class BasicJwt {

   private JwtHelper jwtHelper = new JwtHelper();

   private static final Logger logger = LoggerFactory.getLogger(BasicJwt.class);

   // https://en.wikipedia.org/wiki/JSON_Web_Token#Standard_fields
   // Header fields
   public String typ = "JWT";
   public String cty = null;
   public String alg = "none";
   public String kid = null;
   //public String x5c = null; // <x.509 Certificate Chain>;
   //public String x5u = null; // <x.509 Certificate Chain URL>;
   //public String[] crit = null; // A list of headers that must be understood by the server
   // Body fields
   public String iss = null;
   public String aud = null;
   public String sub = null;
   public long iat = System.currentTimeMillis() / 1000L;
   public long nbf = this.iat;
   public long exp = this.iat + 60L;
   public String jti = null;

   public Claims parseUnsignedJwt(final String token){
      Header header = Jwts.parserBuilder().build().parseClaimsJwt(token).getHeader();
      Claims claims = Jwts.parserBuilder().build().parseClaimsJwt(token).getBody();
      this.typ = header.getType();
      this.cty = header.getContentType();
      this.parseJwtBodyAttributes(claims);
      return claims;
   }

   public Jws<Claims> parseSignedJwt(final FileLikePathService fileLikePathService, final String token){
      KeyIdSigningKeyResolver signingKeyResolver = new KeyIdSigningKeyResolver();
      signingKeyResolver.fileLikePathService = fileLikePathService;
      Jws<Claims> jwt = this.parseSignedJwt(token, signingKeyResolver);
      return jwt;
   }

   public Jws<Claims> parseSignedJwtUsingTestKeys(final FileLikePathService fileLikePathService,
                                                  final String token, final HashMap<String, String> testKeys){
      KeyIdSigningKeyResolver signingKeyResolver = new KeyIdSigningKeyResolver();
      signingKeyResolver.fileLikePathService = fileLikePathService;
      signingKeyResolver.testKeys = testKeys;
      Jws<Claims> jwt = this.parseSignedJwt(token, signingKeyResolver);
      return jwt;
   }

   private Jws<Claims> parseSignedJwt(final String token, final KeyIdSigningKeyResolver signingKeyResolver) {
      Jws<Claims> jwt = Jwts.parserBuilder().setSigningKeyResolver(signingKeyResolver).build().parseClaimsJws(token);
      this.typ = jwt.getHeader().getType();
      this.cty = jwt.getHeader().getContentType();
      this.alg = jwt.getHeader().getAlgorithm();
      this.kid = jwt.getHeader().getKeyId();
      parseJwtBodyAttributes(jwt.getBody());
      return jwt;
   }

   public void parseJwtBodyAttributes(final Claims body) {
      this.iss = body.getIssuer();
      this.aud = body.getAudience();
      this.sub = body.getSubject();
      if(body.getIssuedAt() != null){
         this.iat = body.getIssuedAt().getTime() / 1000L;
      }
      if(body.getNotBefore() != null){
         this.nbf = body.getNotBefore().getTime() / 1000L;
      }
      if(body.getExpiration() != null){
         this.exp = body.getExpiration().getTime() / 1000L;
      }
      this.jti = body.getId();
   }

   public boolean isExpiredJwt(final long currentTimeMillis){
      long nowSeconds = currentTimeMillis / 1000L;
      logger.trace("Is current time {} after the token expiry {}?", nowSeconds, this.exp);
      return nowSeconds > this.exp;
   }

   public String buildAndSignJwtWithPrivateKey(final FileLikePathService fileLikePathService, final String signingKeyId)
         throws IOException, ClassNotFoundException, NoSuchAlgorithmException, InvalidKeySpecException {
      this.kid = signingKeyId;
      PrivateKey signingKey = this.jwtHelper.getRsaPrivateKey(fileLikePathService, signingKeyId);
      return this.buildAndSignJwt(signingKey);
   }

   public String buildAndSignJwtUsingTestKeys(final FileLikePathService fileLikePathService, final String signingKeyId, final HashMap<String, String> testKeys)
         throws IOException, ClassNotFoundException {
      this.kid = signingKeyId;
      String signingKeyString = this.jwtHelper.getStringForUrn(fileLikePathService, signingKeyId, testKeys);
      Key signingKey = this.jwtHelper.deserialiseKeyObject(signingKeyString);
      return this.buildAndSignJwt(signingKey);
   }

   /**
    * If this is PKI, identify the validation key as <signingKey>.pub
    */
   public String buildAndSignJwt(final Key signingKey)
         throws IOException, ClassNotFoundException {
      JwtBuilder builder = this.populateUnsignedJwt();
      String validationKeyId = (signingKey instanceof PrivateKey ? this.kid + ".pub" : this.kid);
      return builder.setHeaderParam("kid", validationKeyId).signWith(signingKey).compact();
   }

   public String buildUnsignedJwt()
         throws IOException, ClassNotFoundException {
      JwtBuilder builder = this.populateUnsignedJwt();
      return builder.compact();
   }

   public JwtBuilder populateUnsignedJwt() {
      JwtBuilder builder = this.populateJwtHeader();
      builder = this.populateJwtBody(builder);
      return builder;
   }

   public JwtBuilder populateJwtHeader() {
      return Jwts.builder()
            .setHeaderParam("typ", this.typ)
            .setHeaderParam("cty", this.cty)
            .setHeaderParam("alg", this.alg)
            .setHeaderParam("kid", this.kid)
            ;
   }

   public JwtBuilder populateJwtBody(final JwtBuilder builder) {
      return builder
            .setIssuer(this.iss)
            .setAudience(this.aud)
            .setSubject(this.sub)
            .setIssuedAt(new Date(this.iat * 1000L))
            .setNotBefore(new Date(this.nbf * 1000L))
            .setExpiration(new Date(this.exp * 1000L))
            .setId(this.jti)
            ;
   }
}
