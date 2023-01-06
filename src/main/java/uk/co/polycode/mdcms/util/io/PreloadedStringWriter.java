package uk.co.polycode.mdcms.util.io;

import java.io.StringWriter;

/**
 * A string writer which (can) pre-load the build string with a string provided at construction
 */
public class PreloadedStringWriter extends StringWriter {

   /**
    * Initial size constructor - delegates to super class to create, then loads
    * 
    * @param preload
    *           the data to be pre-loaded into the writer
    */
   public PreloadedStringWriter(final String preload) {
      super(preload.length());
      this.write(preload);
   }
}
