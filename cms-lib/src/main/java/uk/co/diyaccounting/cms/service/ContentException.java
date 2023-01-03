package uk.co.diyaccounting.cms.service;

/**
 * An exception to be thrown when content cannot be read. This typically occurs when populating a content item because
 * either the underlying source cannot be read at all or the content path does not exist within that document
 */
public final class ContentException extends RuntimeException {

	private static final long serialVersionUID = 1L;

   /**
    * The content path that was being accessed when the exception occurred
    */
   private String path;

   /**
    * Constructs a new exception with null as its detail message.
    * 
    * @param message
    *           the detail message
    * @param path
    *           the content path being accessed when the exception was generated
    * 
    * @see java.lang.Exception#Exception(Throwable)
    */
   public ContentException(final String message, final String path) {
      super(message);
      this.setPath(path);
   }

   /**
    * Constructs a new exception with null as its detail message.
    * 
    * @param message
    *           the detail message
    * @param path
    *           the content path being accessed when the exception was generated
    * @param cause
    *           the underlying cause to be wrapped by this exception
    * 
    * @see java.lang.Exception#Exception(Throwable)
    */
   public ContentException(final String message, final String path, final Throwable cause) {
      super(message, cause);
      this.setPath(path);
   }

   /**
    * The content path that was being accessed when the exception occurred
    * 
    * @return the path
    */
   @Override
   public String getMessage() {
      if (this.getCause() != null) {
         return super.getMessage() + ": [" + this.getPath() + "], because of: " + this.getCause().getMessage();
      } else {
         return super.getMessage() + ": [" + this.getPath() + "]";
      }
   }

   /**
    * The content path that was being accessed when the exception occurred
    * 
    * @return the path
    */
   public String getPath() {
      return this.path;
   }

   /**
    * The content path that was being accessed when the exception occurred
    * 
    * @param path
    *           the path to set
    */
   public void setPath(final String path) {
      this.path = path;
   }
}
