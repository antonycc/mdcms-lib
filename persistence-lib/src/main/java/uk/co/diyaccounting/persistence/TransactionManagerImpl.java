package uk.co.diyaccounting.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.List;

/**
 * JPA convenience class for transactional operations within a single thread
 *
 * @author Antony
 */
@Service("transactionmanager")
public class TransactionManagerImpl implements TransactionManager {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(TransactionManagerImpl.class);

   /**
    * Create an entity manager factory for the catalogue database
    */
   @Autowired
   @Qualifier("emf")
   private EntityManagerFactory emf;

   /**
    * Define thread local entity manager wrapper
    */
   private static class ThreadLocalEntityManager extends ThreadLocal<EntityManager> {

      /**
       * The initial value will be null
       *
       * @return the initial value
       */
      @Override
      protected EntityManager initialValue() {
         return null;
      }
   }

   /**
    * Entity manager for each thread
    */
   private final ThreadLocalEntityManager entityManager = new ThreadLocalEntityManager();

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.dao.CatalogueTransactionManager#beginTransaction()
    */
   @Override
   public void beginTransaction() {

      // Check if the EMF is wired in
      if (this.getEmf() == null) {
         throw new IllegalStateException("Cannot start transaction. The entity manager factory \"emf\" has not been wired in");
      }

      // Check if a transaction has already started
      if (this.entityManager.get() != null) {
         throw new IllegalStateException("A transaction has already been started");
      }

      // Create and begin a new transaction
      try {
         this.entityManager.set(this.getEmf().createEntityManager());
         this.entityManager.get().setFlushMode(FlushModeType.COMMIT);
         this.entityManager.get().getTransaction().begin();
      }catch(Throwable th){
         try {
            if (this.entityManager.get() != null && this.entityManager.get().isOpen()) {
               this.entityManager.get().getTransaction().rollback();
               this.closeEntityManager();
            }
         }catch(PersistenceException pe2){
            logger.error("Exception trying to clean up after failed attempt to start a transaction: {}", pe2.getMessage());
         }finally {
            this.entityManager.set(null);
         }
         throw th;
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.dao.CatalogueTransactionManager#beginReadOnlyTransaction()
    */
   @Override
   public void beginReadOnlyTransaction() {
      this.beginTransaction();
      this.entityManager.get().getTransaction().setRollbackOnly();
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.dao.CatalogueTransactionManager#endTransaction()
    */
   @Override
   public void endTransaction(final boolean success) {
      if (success) {
         this.endTransaction();
      } else {
         this.endFailingTransation();
      }
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.dao.CatalogueTransactionManager#endTransaction()
    */
   @Override
   public void endTransaction() {
      this.endSuccessfulTransation();
   }

   /**
    * End a transaction successfully, only rolling back for read only transactions
    */
   private void endSuccessfulTransation() {
      // this.logContext();
      this.ensureTransactionIsStarted();
      this.entityManager.get().flush();
      this.commitOrRollback();
      this.closeEntityManager();
   }

   /**
    * End a transaction where an exception occurred, rolling back each time
    */
   private void endFailingTransation() {
      // this.logContext();
      this.ensureTransactionIsStarted();
      this.entityManager.get().getTransaction().setRollbackOnly();
      this.commitOrRollback();
      this.closeEntityManager();
   }

   /**
    * Check that a transaction is started ad if not, throw and exception
    */
   private void ensureTransactionIsStarted() {
      if (this.entityManager.get() == null) {
         throw new IllegalStateException("A transaction has not been started");
      }
   }

   /**
    * If the transaction is primed for rollback, roll it back, otherwise commit.
    */
   private void commitOrRollback() {
      // Commit transaction
      if (!this.entityManager.get().getTransaction().getRollbackOnly()) {
         // CatalogueTransactionManagerImpl.logger.debug("Committing");
         this.entityManager.get().getTransaction().commit();
      } else {
         TransactionManagerImpl.logger.debug("Rolling back");
         this.entityManager.get().getTransaction().rollback();
      }
   }

   /**
    * Close the entity manager connection and set the threadlocal instance to null.
    */
   private void closeEntityManager() {
      // Close entity manager
      this.entityManager.get().close();
      this.entityManager.set(null);
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.dao.CatalogueTransactionManager#persist(java.lang.Object)
    */
   @Override
   public void persist(final Object entity) {
      this.entityManager.get().persist(entity);
      this.entityManager.get().flush();
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.dao.CatalogueTransactionManager#createNamedQuery(java.lang.Class)
    */
   @Override
   public Query createNamedQuery(final String namedQuery) {
      //try {
         return this.entityManager.get().createNamedQuery(namedQuery);
      //}catch(NullPointerException e){
      //   String message = "Error creating named query: " + namedQuery;
      //   logger.error(message, e);
      //   throw e;
      //}
   }

   @Override
   public Query createFindUniqueQuery(final Class<?> clazz) {

      String entityName = clazz.getSimpleName();

      // Build the query expecting to find a named query
      String namedQuery = entityName + "." + TransactionManager.FIND_UNIQUE_QUERY;
      Query query = this.createNamedQuery(namedQuery);

      return query;
   }

   @Override
   public Query createFindByHashQuery(final Class<?> clazz) {

      String entityName = clazz.getSimpleName();

      // Build the query expecting to find a named query
      String namedQuery = entityName + "." + TransactionManager.FIND_BY_HASH_QUERY;
      Query query = this.createNamedQuery(namedQuery);

      return query;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.dao.CatalogueTransactionManager#getAll(java.lang.Class)
    */
   @Override
   public List<?> getAll(final Class<?> clazz) {

      String entityName = clazz.getSimpleName();

      // Build the query expecting to find a named query
      String namedQuery = entityName + "." + TransactionManager.FIND_ALL_QUERY;
      EntityManager em = this.entityManager.get();
      Query query = em.createNamedQuery(namedQuery);

      // Extract the results list
      List<?> allEntities = query.getResultList();
      return allEntities;
   }

   /*
    * (non-Javadoc)
    * 
    * @see uk.co.diyaccounting.persistence.dao.CatalogueTransactionManager#remove(java.lang.Object)
    */
   @Override
   public void remove(final Object entity) {
      this.entityManager.get().remove(entity);
      this.entityManager.get().flush();
   }

   /**
    * Get the EMF attached to this object
    *
    * @return the emf
    */
   public EntityManagerFactory getEmf() {
      return this.emf;
   }

   /**
    * Set the EMF for this object to use
    *
    * @param emf
    *           the emf to set
    */
   public void setEmf(final EntityManagerFactory emf) {
      this.emf = emf;
   }
}