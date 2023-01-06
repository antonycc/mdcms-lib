package uk.co.polycode.mdcms.util.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.util.Hashtable;

import uk.co.polycode.mdcms.util.reflect.ReflectionException;
import uk.co.polycode.mdcms.util.reflect.ReflectionHelper;

/**
 * Factory for address allowing injection of mock for InternetAddress
 * 
 * @author antony
 */
public class ReaderWriterFactory {

   /**
    * hash of readers
    */
   private Hashtable<String, Reader> readers = new Hashtable<String, Reader>();

   /**
    * hash of writers
    */
   private Hashtable<String, Writer> writers = new Hashtable<String, Writer>();

   /**
    * Class of reader to use
    */
   private Class<?> readerClass = FileReader.class;

   /**
    * Class of writer to use
    */
   private Class<?> writerClass = FileWriter.class;

   /**
    * The reflection helper delegate
    */
   private transient ReflectionHelper reflect = new ReflectionHelper();

   /**
    * Store it in the instance cache
    * 
    * @param reader
    *           the reader to cache
    * 
    * @throws IOException
    */
   public void putReader(final String name, final Reader reader) throws IOException {
      this.readers.put(name, reader);
   }

   /**
    * Create a new input stream for the named file system resource
    * 
    * @param name
    *           the name of the file to create a File object for
    * 
    * @return a reader object
    * @throws IOException
    */
   public Reader getReader(final String name) throws IOException {
      synchronized (this.readers) {
         if (this.readers.get(name) == null) {
            try {
               Constructor<?> constructor = this.reflect.getConstructor(this.getReaderClass(), String.class);
               Reader reader = (Reader) this.reflect.newInstance(constructor, name);
               this.putReader(name, reader);
            } catch (ReflectionException e) {
               throw new IOException("No constructor for String", e);
            }
         }
      }

      Reader reader = this.readers.get(name);
      this.readers.remove(name);
      return reader;
   }

   /**
    * Store it in the instance cache
    * 
    * @param writer
    *           the reader to cache
    * 
    * @throws IOException
    */
   public void putWriter(final String name, final Writer writer) throws IOException {
      this.writers.put(name, writer);
   }

   /**
    * Create a new input stream for the named file system resource
    * 
    * @param name
    *           the name of the file to create a File object for
    * 
    * @return a writer object
    * @throws IOException
    */
   public Writer getWriter(final String name) throws IOException {
      synchronized (this.writers) {
         if (this.writers.get(name) == null) {
            try {
               Constructor<?> constructor = this.reflect.getConstructor(this.getWriterClass(), String.class);
               Writer writer = (Writer) this.reflect.newInstance(constructor, name);
               this.putWriter(name, writer);
            } catch (ReflectionException e) {
               throw new IOException("No constructor for String", e);
            }
         }
      }

      Writer writer = this.writers.get(name);
      this.writers.remove(name);
      return writer;
   }

   /**
    * Class of reader to use
    * 
    * @return the readerClass
    */
   public Class<?> getReaderClass() {
      return this.readerClass;
   }

   /**
    * Class of reader to use
    * 
    * @param readerClass
    *           the readerClass to set
    */
   public void setReaderClass(final Class<?> readerClass) {
      this.readerClass = readerClass;
   }

   /**
    * Class of writer to use
    * 
    * @return the writerClass
    */
   public Class<?> getWriterClass() {
      return this.writerClass;
   }

   /**
    * Class of writer to use
    * 
    * @param writerClass
    *           the writerClass to set
    */
   public void setWriterClass(final Class<?> writerClass) {
      this.writerClass = writerClass;
   }
}
