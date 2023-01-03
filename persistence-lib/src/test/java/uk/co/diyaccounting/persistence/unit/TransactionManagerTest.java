package uk.co.diyaccounting.persistence.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.persistence.TransactionManager;
import uk.co.diyaccounting.persistence.TransactionManagerImpl;
import uk.co.diyaccounting.persistence.ops.TestEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Test the transaction manager mocking the entity manager factory
 *
 * @author Antony
 */
public class TransactionManagerTest {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CatalogueServiceImplTest.class);

   @Test
   public void expectATransactionToBeStarted() {

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
   }

   @Test(expected = IllegalStateException.class)
   public void expectExceptionAsThereIsNoEMF() {

      // Mocking

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(null);

      // Execute
      classUnderTest.beginTransaction();
   }

   @Test(expected = PersistenceException.class)
   public void expectExceptionDuringCreateEMF() {

      // Test parameters
      String message = "Mock forced exception";
      PersistenceException pe = new PersistenceException(message);

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andThrow(pe).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
   }

   @Test(expected = PersistenceException.class)
   public void expectExceptionDuringBeginTransactionWithTransactionNotOpen() {

      // Test parameters
      String message = "Mock forced exception";
      PersistenceException pe = new PersistenceException(message);
      boolean transactionState = false;

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      EasyMock.expect(em.isOpen()).andReturn(transactionState).anyTimes();
      et.begin();
      EasyMock.expectLastCall().andThrow(pe);
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
   }

   @Test(expected = PersistenceException.class)
   public void expectExceptionDuringBeginTransactionWithTransactionOpen() {

      // Test parameters
      String message = "Mock forced exception";
      PersistenceException pe = new PersistenceException(message);
      boolean transactionState = true;

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      EasyMock.expect(em.isOpen()).andReturn(transactionState).anyTimes();
      em.close();
      EasyMock.expectLastCall();
      et.begin();
      EasyMock.expectLastCall().andThrow(pe);
      et.rollback();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
   }

   @Test(expected = PersistenceException.class)
   public void expectExceptionDuringBeginTransactionWithTransactionOpenAndFailToRollback() {

      // Test parameters
      String message = "Mock forced exception";
      PersistenceException pe = new PersistenceException(message);
      boolean transactionState = true;
      String message2 = "Mock forced exception from catch block";
      PersistenceException pe2 = new PersistenceException(message);

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      EasyMock.expect(em.isOpen()).andReturn(transactionState).anyTimes();
      em.close();
      EasyMock.expectLastCall();
      et.begin();
      EasyMock.expectLastCall().andThrow(pe);
      et.rollback();
      EasyMock.expectLastCall().andThrow(pe2);
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
   }

   @Test(expected = IllegalStateException.class)
   public void expectATransactionToBeAlreadyStarted() {

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      classUnderTest.beginTransaction();
   }

   @Test
   public void expectAReadOnlyTransactionToBeStarted() {

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      et.begin();
      EasyMock.expectLastCall();
      et.setRollbackOnly();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginReadOnlyTransaction();
   }

   @Test
   public void expectATransactionToEndSuccessfully() {
      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.expect(et.getRollbackOnly()).andReturn(false);
      et.commit();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      classUnderTest.endTransaction(true);
   }

   @Test
   public void expectATransactionToEndWithRollBack() {

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createNiceMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      et.begin();
      EasyMock.expectLastCall();
      et.setRollbackOnly();
      EasyMock.expectLastCall();
      EasyMock.expect(et.getRollbackOnly()).andReturn(true);
      et.rollback();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      classUnderTest.endTransaction(false);
   }

   @Test(expected = IllegalStateException.class)
   public void expectATransactionToFailAsItHasNotStarted() {

      // Mocking

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();

      // Execute
      classUnderTest.endTransaction(false);
   }

   @Test
   public void expectAStartedTransactionPersist() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntity entity = new TestEntity();
      entity.setName(name);

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createStrictMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      em.setFlushMode(FlushModeType.COMMIT);
      EasyMock.expectLastCall();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      em.persist(entity);
      EasyMock.expectLastCall();
      em.flush();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      classUnderTest.persist(entity);
   }

   @Test
   public void expectAStartedTransactionRemove() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntity entity = new TestEntity();
      entity.setName(name);

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createStrictMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      em.setFlushMode(FlushModeType.COMMIT);
      EasyMock.expectLastCall();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      em.remove(entity);
      EasyMock.expectLastCall();
      em.flush();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      classUnderTest.remove(entity);
   }

   @Test
   public void expectANamedQuery() {

      // Test parameters;
      String entityName = TestEntity.class.getSimpleName();
      String queryName = entityName + "." + TransactionManager.FIND_ALL_QUERY;

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createStrictMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      Query query = EasyMock.createNiceMock(Query.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      em.setFlushMode(FlushModeType.COMMIT);
      EasyMock.expectLastCall();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      EasyMock.expect(em.createNamedQuery(queryName)).andReturn(query);
      EasyMock.replay(emf, em, et, query);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      Query actualQuery = classUnderTest.createNamedQuery(queryName);
      Assert.assertEquals(query, actualQuery);
   }

   @Test
   public void expectAFindUniqueNamedQuery() {

      // Test parameters;
      String entityName = TestEntity.class.getSimpleName();
      String queryName = entityName + "." + TransactionManager.FIND_UNIQUE_QUERY;
      TestEntity entity = new TestEntity();

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createStrictMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      Query query = EasyMock.createNiceMock(Query.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      em.setFlushMode(FlushModeType.COMMIT);
      EasyMock.expectLastCall();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      EasyMock.expect(em.createNamedQuery(queryName)).andReturn(query);
      EasyMock.replay(emf, em, et, query);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      Query actualQuery = classUnderTest.createFindUniqueQuery(entity.getClass());
      Assert.assertEquals(query, actualQuery);
   }

   @Test
   public void expectAFindByHashNamedQuery() {

      // Test parameters;
      String entityName = TestEntity.class.getSimpleName();
      String queryName = entityName + "." + TransactionManager.FIND_BY_HASH_QUERY;
      TestEntity entity = new TestEntity();

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createStrictMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      Query query = EasyMock.createNiceMock(Query.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      em.setFlushMode(FlushModeType.COMMIT);
      EasyMock.expectLastCall();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      EasyMock.expect(em.createNamedQuery(queryName)).andReturn(query);
      EasyMock.replay(emf, em, et, query);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      Query actualQuery = classUnderTest.createFindByHashQuery(entity.getClass());
      Assert.assertEquals(query, actualQuery);
   }

   @Test
   public void expectAListOfEntitiesFromANamedQuery() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntity entity = new TestEntity();
      entity.setName(name);
      String entityName = TestEntity.class.getSimpleName();
      String queryName = entityName + "." + TransactionManager.FIND_ALL_QUERY;
      List<TestEntity> entities = new ArrayList<TestEntity>();
      entities.add(entity);

      // Mocking
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createStrictMock(EntityManager.class);
      EntityTransaction et = EasyMock.createStrictMock(EntityTransaction.class);
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      em.setFlushMode(FlushModeType.COMMIT);
      EasyMock.expectLastCall();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      EasyMock.expect(em.createNamedQuery(queryName)).andReturn(query);
      EasyMock.expect(query.getResultList()).andReturn(entities);
      EasyMock.replay(emf, em, et, query);

      // Class under test
      TransactionManagerImpl classUnderTest = new TransactionManagerImpl();
      classUnderTest.setEmf(emf);

      // Execute
      classUnderTest.beginTransaction();
      List<?> actualRegions = classUnderTest.getAll(TestEntity.class);
      Assert.assertEquals(entities.size(), actualRegions.size());
      Assert.assertEquals(entities.get(0), actualRegions.get(0));
   }
}