package uk.co.diyaccounting.persistence.integration;

import java.util.List;
import java.util.UUID;

import javax.persistence.Query;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.co.diyaccounting.persistence.TransactionManager;
import uk.co.diyaccounting.persistence.ops.TestEntity;

/**
 * Test the entity persistence using the Data Access Object
 *
 * @author Antony
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-context.xml" })
public class ObjectLevelPersistenceUsingTransactionManager {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(ObjectLevelPersistenceUsingTransactionManager.class);

   /**
    * The transaction manager to use with access to a persistence entity manager
    */
   @Autowired
   @Qualifier("transactionmanager")
   private TransactionManager transactionManager;

   @Test
   public void nop() {
      Assert.assertTrue(true);
   }

   @Test(expected = IllegalStateException.class)
   public void expectAnExceptionWhenATransactionEndsWithoutStarting() {
      this.transactionManager.endTransaction();
      Assert.fail("An exception should have been thrown");
   }

   @Test(expected = IllegalStateException.class)
   public void expectAnExceptionWhenATransactionIsStartedTwice() {
      this.transactionManager.beginTransaction();
      try {
         this.transactionManager.beginTransaction();
         Assert.fail("An exception should have been thrown");
      } finally {
         this.transactionManager.endTransaction();
      }
   }

   @Test
   @SuppressWarnings("unchecked")
   public void expectEntityToReturnWithAGeneratedPrimaryKey() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      String value = UUID.randomUUID().toString();

      // Expect results
      TestEntity newEntity = new TestEntity();
      newEntity.setName(name);
      newEntity.setValue(value);
      String expectedName = name;

      // Create a new entity
      this.transactionManager.beginTransaction();
      try {
         List<TestEntity> entities = (List<TestEntity>) this.transactionManager.getAll(TestEntity.class);
         int entitiesBeforeAdd = entities.size();
         Assert.assertNull(newEntity.getId());

         this.transactionManager.persist(newEntity);

         // Checks
         Assert.assertEquals(expectedName, newEntity.getName());
         Assert.assertNotNull(newEntity.getId());
         entities = (List<TestEntity>) this.transactionManager.getAll(TestEntity.class);
         Assert.assertNotNull(entities);
         int addedEntities = entities.size() - entitiesBeforeAdd;
         Assert.assertEquals(1, addedEntities);
      } finally {
         this.transactionManager.endTransaction();
      }

   }

   @Test
   public void expectReadEntityToMatchPersistedEntity() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      String value = UUID.randomUUID().toString();

      // Expect results
      TestEntity newEntity = new TestEntity();
      newEntity.setName(name);
      newEntity.setValue(value);
      String expectedName = name;

      // Create a new entity
      this.transactionManager.beginTransaction();
      try {
         Assert.assertNull(newEntity.getId());
         this.transactionManager.persist(newEntity);
      } finally {
         this.transactionManager.endTransaction();
      }

      // Read
      this.transactionManager.beginReadOnlyTransaction();
      try {
         Query query = this.transactionManager.createFindUniqueQuery(TestEntity.class);
         query.setParameter("name", name);
         TestEntity readEntity = (TestEntity) query.getSingleResult();
         Assert.assertNotNull(readEntity);
         Assert.assertNotNull(readEntity.getId());
         Assert.assertEquals(expectedName, readEntity.getName());
         Assert.assertEquals(newEntity, readEntity);
      } finally {
         this.transactionManager.endTransaction();
      }
   }

   @SuppressWarnings("unchecked")
   @Test
   public void expectEntityToMeMissingAfterRemove() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      String value = UUID.randomUUID().toString();

      // Expect results
      TestEntity newEntity = new TestEntity();
      newEntity.setName(name);
      newEntity.setValue(value);
      int entitiesBeforeRemove;

      // Create a new entity
      this.transactionManager.beginTransaction();
      try {
         this.transactionManager.persist(newEntity);
      } finally {
         this.transactionManager.endTransaction();
      }

      // Count current entities
      this.transactionManager.beginReadOnlyTransaction();
      try {
         List<TestEntity> entities = (List<TestEntity>) this.transactionManager.getAll(TestEntity.class);
         entitiesBeforeRemove = entities.size();
      } finally {
         this.transactionManager.endTransaction();
      }

      // Read and remove
      this.transactionManager.beginTransaction();
      try {
         Query query = this.transactionManager.createFindUniqueQuery(TestEntity.class);
         query.setParameter("name", name);
         TestEntity readEntity = (TestEntity) query.getSingleResult();
         this.transactionManager.remove(readEntity);
      } finally {
         this.transactionManager.endTransaction();
      }

      // Count remaining entities
      this.transactionManager.beginTransaction();
      try {
         List<TestEntity> entities = (List<TestEntity>) this.transactionManager.getAll(TestEntity.class);
         int entitiesAfterRemove = entities.size();
         int removedEntities = entitiesBeforeRemove - entitiesAfterRemove;
         Assert.assertEquals(1, removedEntities);
      } finally {
         this.transactionManager.endTransaction();
      }
   }

   @Test
   public void expectUpdatedEntityToRetainNewValue() {

      // Test parameters
      String name = UUID.randomUUID().toString();
      String value = UUID.randomUUID().toString();
      String updatedValue = UUID.randomUUID().toString();

      // Expect results
      TestEntity newEntity = new TestEntity();
      newEntity.setName(name);
      newEntity.setValue(value);
      String expectedUpdatedValue = updatedValue;

      // Create a new entity
      this.transactionManager.beginTransaction();
      try {
         this.transactionManager.persist(newEntity);
      } finally {
         this.transactionManager.endTransaction();
      }

      // Read and update
      this.transactionManager.beginTransaction();
      try {
         Query query = this.transactionManager.createFindByHashQuery(TestEntity.class);
         query.setParameter("hash", newEntity.getHash());
         TestEntity readEntity = (TestEntity) query.getSingleResult();
         readEntity.setValue(updatedValue);
         this.transactionManager.persist(readEntity);
      } finally {
         this.transactionManager.endTransaction();
      }

      // Read and check
      this.transactionManager.beginReadOnlyTransaction();
      try {
         Query query = this.transactionManager.createFindUniqueQuery(TestEntity.class);
         query.setParameter("name", name);
         TestEntity readEntity = (TestEntity) query.getSingleResult();
         Assert.assertEquals(expectedUpdatedValue, readEntity.getValue());
      } finally {
         this.transactionManager.endTransaction();
      }
   }

   @Test
   public void expectBasicCrudOperationsToOccurInSequence() {
      this.expectEntityToReturnWithAGeneratedPrimaryKey();
      this.expectReadEntityToMatchPersistedEntity();
      this.expectEntityToMeMissingAfterRemove();
      this.expectUpdatedEntityToRetainNewValue();
   }
}