package uk.co.diyaccounting.persistence;

import uk.co.diyaccounting.util.lang.ComparableUsingString;

/**
 * Type safe data access methods interacting with the transaction manager
 * 
 * @author Antony
 * 
 */
public interface Dao {

	TransactionManager getTransactionManager();

   /**
    * Retrieve the entity from persistence using its alternate key
    * 
    * @param entity
    *           the entity to retrieve (look up on alternate key attributes)
    * 
    * @return The original entity with any additional attributes from persistence populated
    */
   ComparableUsingString retrieve(UniqueQuery entity);

   /**
    * Retrieve the entity from persistence using its alternate key if it exists
    *
    * @param entity
    *           the entity to retrieve (look up on alternate key attributes)
    *
    * @return The original entity with any additional attributes from persistence populated,
    *          unless the entity does not exist in which case null is returned.
    */
   ComparableUsingString retrieveIfExists(UniqueQuery entity);

   /**
    * Retrieve the entity from persistence using its hashed key
    * 
    * @param entity
    *           the entity to retrieve (look up on hash)
    * 
    * @return The original entity with attributes from persistence populated
    */
   ComparableUsingString retrieveByHash(UniqueQuery entity);

   /**
    * Persist an entity if it doesn't already exist otherwise obtain the one that already exists
    * 
    * @param entity
    *           the entity to persist
    * 
    * @return either original entity with any generated values populated or a matching value already stored
    */
   ComparableUsingString createOrUpdateAndRetrieve(UniqueQuery entity);

	/**
	 * Persist an entity that is expected already to exist
	 *
	 * @param entity
	 *           the entity to persist
	 */
	void persist(ComparableUsingString entity);

	void remove(ComparableUsingString entity);

   void removeAll(Class<?> clazz);
}