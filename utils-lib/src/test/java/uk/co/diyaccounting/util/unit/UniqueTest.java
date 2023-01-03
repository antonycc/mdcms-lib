package uk.co.diyaccounting.util.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.util.ops.TestEntity;

import java.util.UUID;

public class UniqueTest {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(UniqueTest.class);

   @Test
   public void expectSameAttributesToCreateEqualObjects() {

      // Test parameters
      String sameName = UUID.randomUUID().toString();
      String differentName = UUID.randomUUID().toString();
      // String nullName = null;

      TestEntity sameNameEntity = new TestEntity();
      sameNameEntity.setName(sameName);

      TestEntity differentNameEntity = new TestEntity();
      differentNameEntity.setName(differentName);

      TestEntity nullNameEntity = new TestEntity();
      // nullNameEntity.setName(nullName);

      TestEntity nullEntity = null;

      String differentClass = new String();

      // Mocks

      // Class under test
      TestEntity classUnderTest = new TestEntity();
      classUnderTest.setName(sameName);

      // Execute
      Assert.assertTrue(classUnderTest.equals(sameNameEntity));
      Assert.assertTrue(sameNameEntity.equals(classUnderTest));
      Assert.assertTrue(classUnderTest.equals(classUnderTest));
      Assert.assertEquals(classUnderTest.hashCode(), classUnderTest.hashCode());

      Assert.assertFalse(classUnderTest.equals(differentNameEntity));
      Assert.assertFalse(differentNameEntity.equals(classUnderTest));
      Assert.assertTrue(differentNameEntity.equals(differentNameEntity));
      Assert.assertNotEquals(classUnderTest.hashCode(), differentNameEntity.hashCode());
      Assert.assertEquals(differentNameEntity.hashCode(), differentNameEntity.hashCode());

      Assert.assertFalse(classUnderTest.equals(nullNameEntity));
      Assert.assertFalse(nullNameEntity.equals(classUnderTest));
      Assert.assertTrue(nullNameEntity.equals(nullNameEntity));
      Assert.assertNotEquals(classUnderTest.hashCode(), nullNameEntity.hashCode());
      Assert.assertEquals(nullNameEntity.hashCode(), nullNameEntity.hashCode());

      Assert.assertFalse(classUnderTest.equals(nullEntity));
      // Assert.assertFalse(nullEntity.equals(classUnderTest));
      // Assert.assertTrue(nullEntity.equals(nullEntity));
      // Assert.assertNotEquals(classUnderTest.hashCode(), nullEntity.hashCode());
      // Assert.assertEquals(nullEntity.hashCode(), nullEntity.hashCode());

      Assert.assertFalse(classUnderTest.equals(differentClass));
      Assert.assertFalse(differentClass.equals(classUnderTest));
      Assert.assertTrue(differentClass.equals(differentClass));
      Assert.assertNotEquals(classUnderTest.hashCode(), differentClass.hashCode());
      Assert.assertEquals(differentClass.hashCode(), differentClass.hashCode());
   }

}
