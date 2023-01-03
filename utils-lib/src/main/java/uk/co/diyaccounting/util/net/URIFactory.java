package uk.co.diyaccounting.util.net;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Create URIs for the caching classes
 * 
 * @author Antony
 */
public final class URIFactory {

   /**
    * Private constructor - factory only
    */
   public URIFactory() {
   }

   /**
    * Create a new instance of the CachingHttpClient
    * 
    * @param uri
    *           a string to create a URI from
    */
   public URI getURI(final String uri) {
      try {
         return new URI(uri);
      } catch (URISyntaxException e) {
         throw new IllegalStateException("Internal request line could not be used to create a URI", e);
      }
   }
}
