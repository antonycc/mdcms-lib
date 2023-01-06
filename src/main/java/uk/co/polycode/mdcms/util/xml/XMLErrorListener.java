package uk.co.polycode.mdcms.util.xml;

import javax.xml.transform.ErrorListener;
import javax.xml.transform.TransformerException;

/**
 * Error listener that maps errors and warnings to throw an Exception
 */
public final class XMLErrorListener implements ErrorListener {

   /*
    * (non-Javadoc)
    * 
    * @see javax.xml.transform.ErrorListener#warning(javax.xml.transform.TransformerException)
    */
   @Override
   public void warning(TransformerException exception) throws TransformerException {
      throw exception;
   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.xml.transform.ErrorListener#error(javax.xml.transform.TransformerException)
    */
   @Override
   public void error(TransformerException exception) throws TransformerException {
      throw exception;
   }

   /*
    * (non-Javadoc)
    * 
    * @see javax.xml.transform.ErrorListener#fatalError(javax.xml.transform.TransformerException)
    */
   @Override
   public void fatalError(TransformerException exception) throws TransformerException {
      throw exception;
   }

}
