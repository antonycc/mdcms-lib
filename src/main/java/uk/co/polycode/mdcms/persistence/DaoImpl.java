package uk.co.polycode.mdcms.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.List;

/**
 * Type safe data access methods interacting with the transaction manager
 *
 * @author Antony
 */
@Service("dao")
public class DaoImpl implements Dao {

   /**
    * The logger for this class.
    */
   private static final Logger logger = LoggerFactory.getLogger(DaoImpl.class);

   /**
    * The transaction manager to use with access to a persistence entity manager
    */
   @Autowired
   @Qualifier("transactionmanager")
   private TransactionManager transactionManager;

   @Override
   public ComparableUsingString retrieve(final UniqueQuery entity) {
	   logger.trace("retrieve: {}", entity.toString());
      Query query = this.getTransactionManager().createFindUniqueQuery(entity.getClass());
      entity.setUniqueParameters(query);
      return (ComparableUsingString) query.getSingleResult();
   }

   @Override
   @SuppressWarnings("unchecked")
   public ComparableUsingString retrieveIfExists(final UniqueQuery entity) {
	   logger.trace("retrieveIfExists: {}", entity.toString());
      Query query = this.getTransactionManager().createFindUniqueQuery(entity.getClass());
      entity.setUniqueParameters(query);
      List<ComparableUsingString> results = (List<ComparableUsingString>)query.getResultList();
      if(results.size() < 1){
         return null;
      }else if(results.size() == 1){
         return results.get(0);
      }else{
         throw new NonUniqueResultException("More than one result matched: " + entity.toString());
      }
   }

   @Override
   public ComparableUsingString retrieveByHash(final UniqueQuery entity) {
	   logger.trace("retrieveByHash: {}", entity.toString());
      Query query = this.getTransactionManager().createFindByHashQuery(entity.getClass());
      entity.setHashParameters(query);
      return (ComparableUsingString) query.getSingleResult();
   }

   @Override
   public ComparableUsingString createOrUpdateAndRetrieve(final UniqueQuery entity) {
	   logger.trace("createOrUpdateAndRetrieve: {}", entity.toString());

      // if(entity instanceof CommercialProduct){
      // logger.debug("creating or retrieving: {}", entity.toString());
      // }

      // Build the query expecting to find a named query
      Query query = this.getTransactionManager().createFindUniqueQuery(entity.getClass());
      entity.setUniqueParameters(query);

      // Persist a new item only if there are no matching entities, otherwise update the persisted entity
      UniqueQuery populatedEntity;
      List<?> entities = query.getResultList();
	   logger.trace("createOrUpdateAndRetrieve: found {}", entities.size());
      if (entities.size() == 0) {
         populatedEntity = entity;
      } else {
         populatedEntity = (UniqueQuery) entities.get(0);
         populatedEntity.copyAttributesForUpdate(entity);
      }
      // Leave this in, customers report that they cannot complete a sale
      try {
         this.getTransactionManager().persist(populatedEntity);
      }catch(Exception e){
         logger.warn("Error ignored, proceeding. createOrUpdateAndRetrieve: {} {}", entity.toString(), e.getMessage());
   /*
The exception we see in the logs:

2018-04-05 11:50:05,957 [ajp-nio-8009-exec-9] WARN  SqlExceptionHelper - SQL Error: 1062, SQLState: 23000
2018-04-05 11:50:05,957 [ajp-nio-8009-exec-9] ERROR SqlExceptionHelper - Duplicate entry
'\xFEb\xA2\xEB\xF2aN\x89\xA5V\xDF\xA8\xC6T\xD5\x85' for key 'uk_pc_customer_id'
2018-04-05 11:50:05,960 [ajp-nio-8009-exec-9] ERROR DebugInterceptor - exception: /confirm.do
javax.persistence.PersistenceException: org.hibernate.exception.ConstraintViolationException:
      could not execute statement
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:149)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:157)
	at org.hibernate.internal.ExceptionConverterImpl.convert(ExceptionConverterImpl.java:164)
	at org.hibernate.internal.SessionImpl.doFlush(SessionImpl.java:1443)
	at org.hibernate.internal.SessionImpl.flush(SessionImpl.java:1423)
	at sun.reflect.GeneratedMethodAccessor111.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.springframework.orm.jpa.ExtendedEntityManagerCreator$ExtendedEntityManagerInvocationHandler.invoke
	   (ExtendedEntityManagerCreator.java:350)
	at com.sun.proxy.$Proxy63.flush(Unknown Source)
	at uk.co.polycode.mdcms.persistence.TransactionManagerImpl.persist(TransactionManagerImpl.java:193)
	at uk.co.polycode.mdcms.persistence.DaoImpl.createOrUpdateAndRetrieve(DaoImpl.java:89)
	at uk.co.polycode.mdcms.crm.dao.PurchaseDaoImpl.createPayPalNewCustomer(PurchaseDaoImpl.java:135)
	at uk.co.polycode.mdcms.crm.service.OrderServiceConsumerImpl.persistCustomerAndPurchase
	   (OrderServiceConsumerImpl.java:104)
	at uk.co.polycode.mdcms.crm.messaging.OrderServiceDispatcherStubImpl.persistCustomerAndPurchase
	   (OrderServiceDispatcherStubImpl.java:30)
	at uk.co.polycode.mdcms.crm.service.OrderServiceProducerImpl.createOrder(OrderServiceProducerImpl.java:133)
	at uk.co.polycode.mdcms.present.controller.CompletionController.confirm(CompletionController.java:270)
	at sun.reflect.GeneratedMethodAccessor236.invoke(Unknown Source)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
    */
      }
      return populatedEntity;
   }

   @Override
   public void persist(final ComparableUsingString entity) {
	   logger.trace("persist {}", entity.toString());
	   this.getTransactionManager().persist(entity);
   }

   @Override
   public void remove(final ComparableUsingString entity) {
	   logger.trace("remove {}", entity.toString());
	   this.getTransactionManager().remove(entity);
   }

   @Override
   @SuppressWarnings("unchecked")
   public void removeAll(Class<?> clazz) {
      List<ComparableUsingString> all;
      all = (List<ComparableUsingString>)this.getTransactionManager().getAll(clazz);
      for(ComparableUsingString entity : all){
         this.remove(entity);
      }
   }

   /**
    * Set the transaction manager
    *
    * @return the transactionManager
    */
   @Override
   public TransactionManager getTransactionManager() {
      return this.transactionManager;
   }

   /**
    * Set the transaction manager
    *
    * @param transactionManager
    *           the transactionManager to set
    */
   public void setTransactionManager(final TransactionManager transactionManager) {
      this.transactionManager = transactionManager;
   }
}