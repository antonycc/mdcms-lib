package uk.co.polycode.mdcms.util.net;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Utility class to manage urls
 */
@Service("url")
public class UrlHelper {

   /**
    * A logger from java logging
    */
   //private static Logger logger = LoggerFactory.getLogger(UrlHelper.class);

   /**
    * The character encoding being used for text
    */
   private String encoding = "UTF-8";

   /**
    * Should the encoding methods force an exception
    * This is because the Spring 5 method signature decalares a checked exception:
    * but throws UnsupportedCharsetException
    */
   private boolean forceUnsupportedEncodingException = false;

   /**
    * Create a new default instance of the URL Helper. Normally the instance would be used directly
    */
   public UrlHelper() {
   }

   /**
    * Decode a single parameter wrapping any unsupported encoding exception in a runtime exception
    * 
    * @param param
    *           the value to decode
    * 
    * @return the parameter decoded
    */
   public String decodeQueryParam(final String param) {
      try {
         if(this.isForceUnsupportedEncodingException()){
            throw new UnsupportedEncodingException("Locally triggered by property forceUnsupportedEncodingException");
         }
         return UriUtils.decode(param, this.getEncoding());
      } catch (UnsupportedEncodingException e) {
         throw new IllegalArgumentException(
                  "Invalid character set rejected by the decoder: " + this.getEncoding(), e);
      }
   }

   /**
    * Encode a single parameter wrapping any unsupported encoding exception in a runtime exception
    * 
    * @param param
    *           the value to encode
    * 
    * @return the parameter encoded
    */
   public String encodeQueryParam(final String param) {
      try {
         if(this.isForceUnsupportedEncodingException()){
            throw new UnsupportedEncodingException("Locally triggered by property forceUnsupportedEncodingException");
         }
         return UriUtils.encodeQueryParam(param, this.getEncoding());
      //} catch (java.nio.charset.UnsupportedCharsetException e){
      //   e.printStackTrace();
      //   throw e;
      } catch (UnsupportedEncodingException e) {
         throw new IllegalArgumentException(
                  "Invalid character set rejected by the encoder: " + this.getEncoding(), e);
      }
   }

   /**
    * Extract the path element from a URL path that may include a file and a path
    * 
    * @param path
    * 
    * @returns the path element up to the file
    * 
    */
   public String getPath(final String path) {
      int endOfBaseName = path.lastIndexOf('/');
      if (endOfBaseName == -1) {
         return path;
      } else {
         return path.substring(0, endOfBaseName + 1);
      }
   }

   /**
    * Create a new structure for query parameters which supports recurrences of parameters
    * 
    * @returns map of lists key's on the parameter names
    */
   public Map<String, List<String>> createQueryParams() {
      return new HashMap<String, List<String>>();
   }

   /**
    * Add one or more parameters to the query parameters. I an instance of a parameter already exists an additional
    * parameter is added
    * 
    * @param queryParams
    *           the map of parameters
    * @param name
    *           the name of the parameter to update
    * @param values
    *           the value (or values) to add
    */
   public void addParams(final Map<String, List<String>> queryParams, final String name, final String... values) {
      // Ensure there is a list for the values to be added to
      List<String> currentValues = queryParams.get(name);
      if (currentValues == null) {
         currentValues = new ArrayList<String>();
         queryParams.put(name, currentValues);
      }

      // Add each parameter value to the list
      for (String value : values) {
         currentValues.add(value);
      }
   }

   /**
    * Build a string encoded for use as a URL query string
    * 
    * @param queryParams
    *           the map of parameter names and value lists
    * 
    * @return the query string
    */
   public String buildQueryString(final Map<String, List<String>> queryParams) {
      StringBuilder buf = new StringBuilder();
      boolean first = true;
      TreeSet<String> names = new TreeSet<String>(queryParams.keySet());
      for (String name : names) {
         List<String> valuesIncludingDuplicates = queryParams.get(name);
         TreeSet<String> values = new TreeSet<String>();
         // Remove duplicates and filter nulls
         for (String value : valuesIncludingDuplicates) {
            if(!StringUtils.isBlank(value)){
               values.add(value);
            }
         }
         // Build query string by appending populated unique values
         for (String value : values) {
            if (first) {
               first = false;
            } else {
               buf.append("&");
            }
            buf.append(name);
            buf.append("=");
            buf.append(this.encodeQueryParam(value));
         }
      }
      return buf.toString();
   }

   /**
    * Get URL query parameters as a map of String lists. Multiple occurrences of the same named parameter are supported
    * 
    * @param url
    *           the URL as a string
    * 
    * @return the parameter map
    */
   public Map<String, List<String>> parseQueryParams(final String url) {
      Map<String, List<String>> queryParams = this.createQueryParams();
      String[] urlParts = url.split("\\?");
      if (urlParts.length > 1) {
         String query = urlParts[urlParts.length - 1];
         for (String param : query.split("&")) {
            String[] pair = param.split("=");
            String name = this.decodeQueryParam(pair[0]);

            // If there is a value, add it; otherwise ensure there is an empty list
            if (pair.length > 1) {
               String value = this.decodeQueryParam(pair[1]);
               this.addParams(queryParams, name, value);
            } else {
               this.addParams(queryParams, name);
            }
         }
      }
      return queryParams;
   }

   /**
    * The character encoding being used for text
    * 
    * @return the encoding
    */
   public String getEncoding() {
      return this.encoding;
   }

   /**
    * The character encoding being used for text
    * 
    * @param encoding
    *           the encoding to set
    */
   public void setEncoding(final String encoding) {
      this.encoding = encoding;
   }

   /**
    * Should an exception be forced
    *
    * @return should an exception be forced
    */
   public boolean isForceUnsupportedEncodingException() {
      return forceUnsupportedEncodingException;
   }

   /**
    * Should an exception be forced
    *
    * @param forceUnsupportedEncodingException should an exception be forced
    */
   public void setForceUnsupportedEncodingException(final boolean forceUnsupportedEncodingException) {
      this.forceUnsupportedEncodingException = forceUnsupportedEncodingException;
   }

}
