package uk.co.diyaccounting.util.xml;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * Error handler that maps errors and warnings to throw an Exception
 */
public final class XMLErrorHandler implements ErrorHandler {

   /*
    * (non-Javadoc)
    * 
    * @see org.xml.sax.ErrorHandler#warning(org.xml.sax.SAXParseException)
    */
   @Override
   public void warning(SAXParseException exception) throws SAXException {
      throw exception;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.xml.sax.ErrorHandler#error(org.xml.sax.SAXParseException)
    */
   @Override
   public void error(SAXParseException exception) throws SAXException {
      throw exception;
   }

   /*
    * (non-Javadoc)
    * 
    * @see org.xml.sax.ErrorHandler#fatalError(org.xml.sax.SAXParseException)
    */
   @Override
   public void fatalError(SAXParseException exception) throws SAXException {
      throw exception;
   }

}
