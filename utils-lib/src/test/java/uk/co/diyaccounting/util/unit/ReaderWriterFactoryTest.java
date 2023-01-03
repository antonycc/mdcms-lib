package uk.co.diyaccounting.util.unit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import uk.co.diyaccounting.util.io.PreloadedStringWriter;
import uk.co.diyaccounting.util.io.ReaderWriterFactory;

/**
 * Test file factory
 */
public class ReaderWriterFactoryTest {

   /**
    * The logger for this class.
    */
   //private static final Logger logger = LoggerFactory.getLogger(ReaderWriterFactoryTest.class);

   /**
    * Read and write to mocked writers
    */
   @Test
   public void testMockedFiles() throws IOException {

      // Set up params
      String expectedName = "name";
      String expectedContents = "contents";

      // Mock reader and writer
      StringReader reader = new StringReader(expectedContents);
      StringWriter writer = new StringWriter();

      // Class under test
      ReaderWriterFactory factory = new ReaderWriterFactory();
      factory.putReader(expectedName, reader);
      factory.putWriter(expectedName, writer);

      // Execute
      Reader actualReader = factory.getReader(expectedName);
      String actualContents = IOUtils.toString(actualReader);
      Assert.assertEquals(expectedContents, actualContents);

      Writer actualWriter = factory.getWriter(expectedName);
      IOUtils.write(expectedContents, actualWriter);
      Assert.assertEquals(expectedContents, writer.toString());
   }

   /**
    * Open an an alternate sub-class of writer
    */
   @Test
   public void testAlternateReaderAndWriter() throws IOException {

      // Set up params
      String expectedContents = "contents";

      // Class under test
      ReaderWriterFactory factory = new ReaderWriterFactory();

      // Execute
      factory.setReaderClass(StringReader.class);
      Reader actualReader = factory.getReader(expectedContents);
      String actualContents = IOUtils.toString(actualReader);
      Assert.assertEquals(expectedContents, actualContents);

      factory.setWriterClass(PreloadedStringWriter.class);
      Writer actualWriter = factory.getWriter(expectedContents);
      Assert.assertEquals(expectedContents, actualWriter.toString());
   }

   /**
    * Fail opening an alternate sub-class of reader
    */
   @Test(expected = IOException.class)
   public void testAlternateReaderWithException() throws IOException {

      // Set up params
      String expectedContents = "contents";

      // Class under test
      ReaderWriterFactory factory = new ReaderWriterFactory();

      // Execute
      factory.setReaderClass(InputStreamReader.class);
      factory.getReader(expectedContents);
   }

   /**
    * Fail opening an alternate sub-class of writer
    */
   @Test(expected = IOException.class)
   public void testAlternateWriterWithException() throws IOException {

      // Set up params
      String expectedContents = "contents";

      // Class under test
      ReaderWriterFactory factory = new ReaderWriterFactory();

      // Execute
      factory.setWriterClass(OutputStreamWriter.class);
      factory.getWriter(expectedContents);
   }
}
