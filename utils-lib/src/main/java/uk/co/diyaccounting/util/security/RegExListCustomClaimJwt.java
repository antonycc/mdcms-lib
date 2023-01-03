package uk.co.diyaccounting.util.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExListCustomClaimJwt extends BasicJwt {

   private static final Logger logger = LoggerFactory.getLogger(RegExListCustomClaimJwt.class);

   public ArrayList<String> custom = new ArrayList<>();

   @Override
   public JwtBuilder populateJwtBody(final JwtBuilder builder) {
      if(CollectionUtils.isEmpty(this.custom)) {
         return super.populateJwtBody(builder);
      }else{
         Map<String,ArrayList<String>> claims = new HashMap<>();
         claims.put(this.iss, this.custom);
         return super.populateJwtBody(builder.setClaims(claims));
      }
   }

   @Override
   public void parseJwtBodyAttributes(final Claims claims) {
      super.parseJwtBodyAttributes(claims);
      this.custom = (ArrayList<String>)claims.get(this.iss);
   }

   public boolean matchesAtLeastOneCustomClaim(final String s){
      for(String claim : this.custom){
         Pattern p = Pattern.compile(claim);
         Matcher m = p.matcher(s);
         if(m.matches()){
            return true;
         }
      }
      return false;
   }
}
