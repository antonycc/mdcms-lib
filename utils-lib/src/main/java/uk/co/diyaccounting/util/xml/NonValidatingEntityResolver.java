package uk.co.diyaccounting.util.xml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.StringReader;

/**
 * An entity resolver which does not attempt to access the internet to resolve XML entities
 */
public class NonValidatingEntityResolver implements EntityResolver {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(NonValidatingEntityResolver.class);

   /*
    * (non-Javadoc)
    *
    * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
    */
   @Override
   public InputSource resolveEntity(String publicId, String systemId) {
	   logger.debug("Skipping entity resolution for public/system {}/{}", publicId, systemId);
	   return new InputSource(new StringReader("")); // Returns a valid dummy source
   }

}
