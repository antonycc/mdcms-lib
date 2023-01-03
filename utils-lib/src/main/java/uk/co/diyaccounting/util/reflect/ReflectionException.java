package uk.co.diyaccounting.util.reflect;

/**
 * An exception to be thrown when content cannot be read. This typically occurs when populating a content item because
 * either the underlying source cannot be read at all or the content path does not exist within that document
 */
public final class ReflectionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

   /**
    * Constructs a new exception with null as its detail message.
    * 
    * @param message
    *           the detail message
    * @param cause
    *           the underlying cause to be wrapped by this exception
    * 
    * @see java.lang.Exception#Exception(Throwable)
    */
   public ReflectionException(final String message, final Throwable cause) {
      super(message, cause);
   }
}
