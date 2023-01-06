package uk.co.polycode.mdcms.test;

import uk.co.polycode.mdcms.util.io.FileLikePathService;

import java.io.File;

/**
 * Utility class to manage data sets
 */
public class TestHelper {

   /**
    * The singleton instance of the TestHelper
    */
   public static final TestHelper instance = new TestHelper();

   /**
    * Underlying file helper instance
    */
   private FileLikePathService fileLikePathService = new FileLikePathService();

   /**
    * Create a new default instance of the TestHelper. Normally the instance would be used directly
    */
   public TestHelper() {
   }

   /**
    * Check if a file exists catching any exceptions and warning
    * 
    * @param path
    *           the path to check
    * @return true is the path can be confirmed to exist, false otherwise
    */
   public boolean exists(final String path) {
      return this.fileLikePathService.exists(path);
   }

   /**
    * Check if a file exists catching any exceptions and warning
    * 
    * @param file
    *           the file to check
    * @return true is the path can be confirmed to exist, false otherwise
    */
   public boolean exists(final File file) {
      return this.fileLikePathService.exists(file);
   }
}
