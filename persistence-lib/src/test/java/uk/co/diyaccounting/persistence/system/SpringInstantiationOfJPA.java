package uk.co.diyaccounting.persistence.system;

import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import uk.co.diyaccounting.persistence.ops.TestEntity;

/**
 * Test the entity persistence using Spring wired entity manager
 *
 * @author Antony
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring-context.xml" })
public class SpringInstantiationOfJPA {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CodeInstantiationOfJPA.class);

   /**
    * The factory finds the persistence.xml, creates the persistence layer and as this is hibernate.hbm2ddl.auto =
    * create, the ddl is exported and database created.
    */
   @Autowired
   @Qualifier("emf")
   private EntityManagerFactory emf;

   /**
    * Do nothing in the test but start and stop the database before and after
    */
   @Test
   public void testNOP() {
      EntityManager entityManager = this.emf.createEntityManager();
      Assert.assertNotNull(entityManager);
      entityManager.close();
   }

   /**
    * Attempt to persist and remove entities without using the Data Access Object
    *
    * @throws Exception
    */
   @SuppressWarnings("unchecked")
   @Test
   public void expectEntityToReturnWithAGeneratedPrimaryKey() throws Exception {

      EntityManager entityManager = this.emf.createEntityManager();
      Assert.assertNotNull(entityManager);

      entityManager.getTransaction().begin();

      // Test parameters
      String name = UUID.randomUUID().toString();
      String value = UUID.randomUUID().toString();
      String query = "SELECT te FROM TestEntity te";

      // Expected results
      TestEntity newEntity = new TestEntity();
      newEntity.setName(name);
      newEntity.setValue(value);
      String expectedName = name;
      String expectedValue = value;

      // Count current entities
      List<TestEntity> entities = entityManager.createQuery(query).getResultList();
      Assert.assertNotNull(entities);
      int entitiesBeforeAdd = entities.size();

      // Create a new entity
      Assert.assertNull(newEntity.getId());
      entityManager.persist(newEntity);
      Assert.assertEquals(expectedName, newEntity.getName());
      Assert.assertNotNull(newEntity.getId());
      Assert.assertTrue(entityManager.contains(newEntity));

      // Count current entities expecting to find 1 more
      entities = entityManager.createQuery(query).getResultList();
      Assert.assertNotNull(entities);
      int entitiesAfterAdd = entities.size();
      int addedEntities = entitiesAfterAdd - entitiesBeforeAdd;
      Assert.assertEquals(1, addedEntities);

      // Check retrieved entity
      TestEntity readEntity = entities.get(0);
      Assert.assertNotNull(readEntity);
      Assert.assertEquals(expectedName, readEntity.getName());
      Assert.assertEquals(expectedValue, readEntity.getValue());
      Assert.assertEquals(newEntity, readEntity);

      // Remove it
      entityManager.remove(readEntity);
      Assert.assertFalse(entityManager.contains(readEntity));

      // Count current entities expecting to find the original number
      entities = entityManager.createQuery(query).getResultList();
      Assert.assertNotNull(entities);
      int entitiesAfterRemove = entities.size();
      addedEntities = entitiesBeforeAdd - entitiesAfterRemove;
      Assert.assertEquals(0, addedEntities);

      entityManager.getTransaction().commit();

      entityManager.close();
   }
}