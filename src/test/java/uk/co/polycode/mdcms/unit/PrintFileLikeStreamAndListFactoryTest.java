package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.polycode.mdcms.util.io.PrintStreamFactory;
import uk.co.polycode.mdcms.util.io.FileLikeStreamAndListFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * Create a print stream with valid and invalid character sets
 * 
 * @author Antony
 */
public class PrintFileLikeStreamAndListFactoryTest {

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   @Test
   public void expectTheStreamToBeCreated() throws IOException {

      // Instance to test
      PrintStreamFactory classUnderTest = new PrintStreamFactory();

      // Execute
      PrintStream ps = classUnderTest.createPrintStream(System.out);

      // Check
      Assert.assertNotNull(ps);
   }

   @Test(expected = IllegalArgumentException.class)
   public void expectTheStreamToFailToBeCreated() throws IOException {

      // Test parameters
      String encoding = "no.such.encoding";

      // Instance to test
      PrintStreamFactory classUnderTest = new PrintStreamFactory();

      // Execute
      PrintStream ps = classUnderTest.createPrintStream(System.out, encoding);

      // Check
      Assert.assertNotNull(ps);
   }

   @Test
   public void expectToGetNonNullJavaHome()
      throws IOException{

      // Test parameters
      String file = System.getProperty("java.home") +
            System.getProperty("file.separator") + "lib" +
            System.getProperty("file.separator") + "classlist";;

      // Class under test
      FileLikeStreamAndListFactory classUnderTest = new FileLikeStreamAndListFactory();

      // Execute
      InputStream is = classUnderTest.getFileAsInputStream(file);

      // Checks
      Assert.assertNotNull(is);
   }
}