package uk.co.polycode.mdcms.unit;

import org.easymock.EasyMock;
import org.junit.Assert;
import org.junit.Test;
import uk.co.polycode.mdcms.ops.TestEntityTwo;
import uk.co.polycode.mdcms.ops.TestEntityWithoutHash;
import uk.co.polycode.mdcms.persistence.DaoImpl;
import uk.co.polycode.mdcms.persistence.TransactionManager;
import uk.co.polycode.mdcms.persistence.TransactionManagerImpl;
import uk.co.polycode.mdcms.util.lang.ComparableUsingString;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FlushModeType;
import javax.persistence.NonUniqueResultException;
import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Test the doa mocking the transaction manager
 *
 * @author Antony
 */
public class DaoImplTest {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CatalogueServiceImplTest.class);

   @Test
   public void expectSingleResultTakenFromMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.setParameter("name", entity.getName())).andReturn(query);
      EasyMock.expect(query.getSingleResult()).andReturn(entity);
      TransactionManager transactionManager = EasyMock.createNiceMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindUniqueQuery(entity.getClass())).andReturn(query);
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      TestEntityTwo actualEntity = (TestEntityTwo) classUnderTest.retrieve(entity);
      Assert.assertEquals(entity, actualEntity);
   }

   @Test
   public void expectSingleResultFromRetrieveIfExistsTakenFromMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);
      List<ComparableUsingString> entities = new ArrayList<ComparableUsingString>();
      entities.add(entity);

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.setParameter("name", entity.getName())).andReturn(query);
      EasyMock.expect(query.getResultList()).andReturn(entities);
      TransactionManager transactionManager = EasyMock.createNiceMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindUniqueQuery(entity.getClass())).andReturn(query);
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      TestEntityTwo actualEntity = (TestEntityTwo) classUnderTest.retrieveIfExists(entity);
      Assert.assertEquals(entity, actualEntity);
   }

   @Test
   public void expectNoResultFromRetrieveIfExistsTakenFromMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);
      List<ComparableUsingString> entities = new ArrayList<ComparableUsingString>();

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.setParameter("name", entity.getName())).andReturn(query);
      EasyMock.expect(query.getResultList()).andReturn(entities);
      TransactionManager transactionManager = EasyMock.createNiceMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindUniqueQuery(entity.getClass())).andReturn(query);
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      TestEntityTwo actualEntity = (TestEntityTwo) classUnderTest.retrieveIfExists(entity);
      Assert.assertNull(actualEntity);
   }

   @Test(expected = NonUniqueResultException.class)
   public void expectMultipleResultsFromRetrieveIfExistsTakenFromMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);
      List<ComparableUsingString> entities = new ArrayList<ComparableUsingString>();
      entities.add(entity);
      entities.add(entity);

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.setParameter("name", entity.getName())).andReturn(query);
      EasyMock.expect(query.getResultList()).andReturn(entities);
      TransactionManager transactionManager = EasyMock.createNiceMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindUniqueQuery(entity.getClass())).andReturn(query);
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      classUnderTest.retrieveIfExists(entity);
   }

   @Test
   public void expectSingleResultByHashTakenFromMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.setParameter("hash", entity.getHash())).andReturn(query);
      EasyMock.expect(query.getSingleResult()).andReturn(entity);
      TransactionManager transactionManager = EasyMock.createNiceMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindByHashQuery(entity.getClass())).andReturn(query);
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      TestEntityTwo actualEntity = (TestEntityTwo) classUnderTest.retrieveByHash(entity);
      Assert.assertEquals(actualEntity, actualEntity);
   }

   @Test
   public void expectNoResultFromNoneHashedEntityTakenFromMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityWithoutHash entity = new TestEntityWithoutHash();
      entity.setName(name);

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.getSingleResult()).andReturn(null);
      TransactionManager transactionManager = EasyMock.createNiceMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindByHashQuery(entity.getClass())).andReturn(query);
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      TestEntityTwo actualEntity = (TestEntityTwo) classUnderTest.retrieveByHash(entity);
      Assert.assertNull(actualEntity);
   }

   @Test
   public void expectSingleResultWhichDoesNotNeedCreatingWithMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);
      List<TestEntityTwo> entities = new ArrayList<TestEntityTwo>();
      entities.add(entity);

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.setParameter("name", entity.getName())).andReturn(query);
      EasyMock.expect(query.getResultList()).andReturn(entities);
      TransactionManager transactionManager = EasyMock.createStrictMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindUniqueQuery(entity.getClass())).andReturn(query);
      transactionManager.persist(entity);
      EasyMock.expectLastCall();
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      TestEntityTwo actualEntity = (TestEntityTwo) classUnderTest.createOrUpdateAndRetrieve(entity);
      Assert.assertEquals(entity, actualEntity);
   }

   @Test
   public void expectSingleResultWhichNeededToBeCreatedWithMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);
      List<TestEntityTwo> entities = new ArrayList<TestEntityTwo>();

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.setParameter("name", entity.getName())).andReturn(query);
      EasyMock.expect(query.getResultList()).andReturn(entities);
      TransactionManager transactionManager = EasyMock.createStrictMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindUniqueQuery(entity.getClass())).andReturn(query);
      transactionManager.persist(entity);
      EasyMock.expectLastCall();
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      TestEntityTwo actualRegion = (TestEntityTwo) classUnderTest.createOrUpdateAndRetrieve(entity);
      Assert.assertEquals(entity, actualRegion);
   }

   @Test
   public void expectSingleResultWhichThrowsAnExceptionOnPersistWithMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);
      List<TestEntityTwo> entities = new ArrayList<TestEntityTwo>();
      RuntimeException e = new RuntimeException("Mock forced exception.");

      // Mocks
      Query query = EasyMock.createStrictMock(Query.class);
      EasyMock.expect(query.setParameter("name", entity.getName())).andReturn(query);
      EasyMock.expect(query.getResultList()).andReturn(entities);
      TransactionManager transactionManager = EasyMock.createStrictMock(TransactionManager.class);
      EasyMock.expect(transactionManager.createFindUniqueQuery(entity.getClass())).andReturn(query);
      transactionManager.persist(entity);
      EasyMock.expectLastCall().andThrow(e);
      EasyMock.replay(query, transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      TestEntityTwo actualRegion = (TestEntityTwo) classUnderTest.createOrUpdateAndRetrieve(entity);
      Assert.assertEquals(entity, actualRegion);
   }

   @Test
   public void expectPersistToCallMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);

      // Mocks
      TransactionManager transactionManager = EasyMock.createStrictMock(TransactionManager.class);
      transactionManager.persist(entity);
      EasyMock.expectLastCall();
      EasyMock.replay(transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      classUnderTest.persist(entity);
   }

   @Test
   public void expectRemoveToCallMockedTransactionManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);

      // Mocks
      TransactionManager transactionManager = EasyMock.createStrictMock(TransactionManager.class);
      transactionManager.remove(entity);
      EasyMock.expectLastCall();
      EasyMock.replay(transactionManager);

      // Class under test
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      classUnderTest.remove(entity);
   }

   @Test
   public void expectRemoveAllToCallMockedEntityManager() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      TestEntityTwo entity = new TestEntityTwo();
      entity.setName(name);
      List<TestEntityTwo> entities = new ArrayList<TestEntityTwo>();
      entities.add(entity);
      String entityName = TestEntityTwo.class.getSimpleName();
      String queryName = entityName + "." + TransactionManager.FIND_ALL_QUERY;

      // Mocks
      EntityManagerFactory emf = EasyMock.createNiceMock(EntityManagerFactory.class);
      EntityManager em = EasyMock.createStrictMock(EntityManager.class);
      EntityTransaction et = EasyMock.createNiceMock(EntityTransaction.class);
      Query query = EasyMock.createNiceMock(Query.class);
      EasyMock.expect(emf.createEntityManager()).andReturn(em).anyTimes();
      em.setFlushMode(FlushModeType.COMMIT);
      EasyMock.expectLastCall();
      EasyMock.expect(em.getTransaction()).andReturn(et).anyTimes();
      et.begin();
      EasyMock.expectLastCall();
      EasyMock.expect(em.createNamedQuery(queryName)).andReturn(query).anyTimes();
      EasyMock.expect(query.getResultList()).andReturn(entities);
      em.remove(entity);
      EasyMock.expectLastCall();
      em.flush();
      EasyMock.expectLastCall();
      EasyMock.replay(emf, em, et, query);

      // Class under test
      TransactionManagerImpl transactionManager = new TransactionManagerImpl();
      transactionManager.setEmf(emf);
      DaoImpl classUnderTest = new DaoImpl();
      classUnderTest.setTransactionManager(transactionManager);

      // Execute
      transactionManager.beginTransaction();
      classUnderTest.removeAll(TestEntityTwo.class);
   }}