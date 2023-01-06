package uk.co.polycode.mdcms.util.io;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

/**
 * Create Spring context in a separate class so mocks can prevent Spring initialisation
 * 
 * @author antony
 */
public class PrintStreamFactory {

   /**
    * Create a print stream specifying the encoding
    * 
    * @param out
    *           the output stream to wrap in a print stream
    * 
    * @return a print stream
    */
   public PrintStream createPrintStream(final OutputStream out) {
      return this.createPrintStream(out, UtilConstants.DEFAULT_ENCODING);
   }

   /**
    * Create a print stream using the encoding
    * 
    * @param out
    *           the output stream to wrap in a print stream
    * @param charset
    *           the charset to use when outputting characters
    * 
    * @return a print stream
    */
   public PrintStream createPrintStream(final OutputStream out, final String charset) {
      try {
         return new PrintStream(out, true, charset);
      } catch (UnsupportedEncodingException uee) {
         throw new IllegalArgumentException("It is suprising this UTF-8 is not supported.", uee);
      }
   }
}