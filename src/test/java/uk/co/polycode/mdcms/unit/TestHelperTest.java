package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.polycode.mdcms.test.TestHelper;

import java.io.IOException;

/**
 * Test the test helper
 * 
 * @author Antony
 */
public class TestHelperTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(TestHelperTest.class);

   /**
    * Non existant file
    */
   private String NoSutchFile = "afile";

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   /**
    * Test file existence
    */
   @Test
   public void testFileExistance() throws IOException {

      TestHelper testHelper = new TestHelper();

      Assert.assertFalse("Should not exist: " + this.NoSutchFile, testHelper.exists(this.NoSutchFile));
   }

   /**
    * Test file existence exception handling
    */
   // TODO: Fails with: java.lang.IllegalArgumentException: Unsupported class file major version 63
   /*@Test
   public void testFileExistanceExceptions() throws IOException {

      TestHelper testHelper = new TestHelper();
      Boolean exists;
      File file;

      // Create file that throws a SecurityException
      file = EasyMock.createMock(File.class);
      SecurityException se = new SecurityException();
      EasyMock.expect(file.exists()).andThrow(se);
      EasyMock.expect(file.getPath()).andReturn(this.NoSutchFile);
      EasyMock.replay(file);

      // Check existence is false
      exists = testHelper.exists(file);
      Assert.assertFalse("SecurityException forces existance to be false", exists);

      // Create file that throws Throwable
      file = EasyMock.createMock(File.class);
      NullPointerException npe = new NullPointerException();
      EasyMock.expect(file.exists()).andThrow(npe);
      EasyMock.expect(file.getPath()).andReturn(this.NoSutchFile);
      EasyMock.replay(file);

      // Check existence is false
      exists = testHelper.exists(file);
      Assert.assertFalse("Throwable forces existance to be false", exists);
   }*/
}