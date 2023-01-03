package uk.co.diyaccounting.persistence.system;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import uk.co.diyaccounting.persistence.ops.TestEntity;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * Test the entity persistence using java instantiated entity manager
 *
 * @author Antony
 */
public class CodeInstantiationOfJPA {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CodeInstantiationOfJPA.class);

   /**
    * The factory finds the persistence.xml, creates the persistence layer and as this is hibernate.hbm2ddl.auto =
    * create, the ddl is exported and database created.
    */
   private static EntityManagerFactory emf = null;

   @BeforeClass
   public static void startDB() throws Exception {
      HibernatePersistenceProvider provider = new HibernatePersistenceProvider();
      String persistenceUnitName = "testpersistence";
      Properties properties = new Properties();
      // Removed because this doesn't compile with libraries using jakarta. instead of javax.
      //CodeInstantiationOfJPA.emf = provider.createEntityManagerFactory(persistenceUnitName, properties);
   }

   @Test
   public void testNOP() {
      EntityManager entityManager = CodeInstantiationOfJPA.emf.createEntityManager();
      Assert.assertNotNull(entityManager);
      entityManager.close();
   }

   @SuppressWarnings("unchecked")
   @Test
   public void expectEntityToReturnWithAGeneratedPrimaryKey() throws Exception {

      EntityManager entityManager = CodeInstantiationOfJPA.emf.createEntityManager();
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

   @AfterClass
   public static void stopDB() throws Exception {
      if (CodeInstantiationOfJPA.emf != null) {
         CodeInstantiationOfJPA.emf.close();
         CodeInstantiationOfJPA.emf = null;
      }
   }

}