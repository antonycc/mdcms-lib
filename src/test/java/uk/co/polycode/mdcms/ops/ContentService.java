package uk.co.polycode.mdcms.ops;

/**
 * Cache-able content item retrieval
 * 
 * @author Antony
 */
public interface ContentService {

   /**
    * Return the page content for the specified item
    * 
    * @param path
    *           the path to the content item
    * 
    * @return the page level content
    * @throws ContentException
    */
   Object getPage(String path) throws ContentException;

   /**
    * Return the product list
    * 
    * @param path
    *           the path to the content item
    * 
    * @return the a products content item that has a list of products
    * @throws ContentExceptionem
    */
   Object getProducts(String path) throws ContentException;

   /**
    * Return a simple piece of content
    * 
    * @param path
    *           the path to the content item
    * 
    * @return the page level content for the home page
    * @throws ContentException
    */
   Object getTitleBody(String path) throws ContentException;

   /**
    * Return the product content
    * 
    * @param path
    *           the path to the content item
    * 
    * @return the product content
    * @throws ContentException
    */
   Object getProduct(String path) throws ContentException;
}
