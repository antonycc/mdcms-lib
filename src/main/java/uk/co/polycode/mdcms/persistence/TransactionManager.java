package uk.co.polycode.mdcms.persistence;

import javax.persistence.Query;
import java.util.List;

/**
 * JPA convenience class for transactional operations within a single thread
 * 
 * @author Antony
 */
public interface TransactionManager {

   /**
    * The named query for returning all entities
    */
   String FIND_ALL_QUERY = "findAll";

   /**
    * The named query for returning unique entities
    */
   String FIND_UNIQUE_QUERY = "findUnique";

   /**
    * Name for find by hash query
    */
   String FIND_BY_HASH_QUERY = "findByHash";

   /**
    * Start a new transaction in this thread
    */
   void beginTransaction();

   /**
    * Start a new transaction in this thread which cannot be committed to
    */
   void beginReadOnlyTransaction();

   /**
    * Close any transaction in this thread and commit
    */
   void endTransaction();

   /**
    * Close any transaction in this thread and commit
    * 
    * @param success whether the transaction should be closed as a successful one and if necessary committed
    */
   void endTransaction(boolean success);

   /**
    * Persist an entity
    * 
    * @param entity
    *           the entity to persist
    */
   void persist(Object entity);

   /**
    * Create an instance of Query for executing a Java Persistence query language statement.
    * 
    * @param queryName
    *           a Java Persistence named query
    * @return the new query instance
    */
   Query createNamedQuery(String queryName);

	Query createFindUniqueQuery(Class<?> clazz);

	Query createFindByHashQuery(Class<?> clazz);

   /**
    * Execute a query
    * 
    * @return a list of all the entities
    */
   List<?> getAll(Class<?> clazz);

   /**
    * Remove an entity
    * 
    * @param entity
    *           the entity to remove
    */
   void remove(Object entity);
}