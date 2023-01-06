package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.polycode.mdcms.ops.TestEntityTwo;
import uk.co.polycode.mdcms.ops.TestEntityWithoutHash;

import java.util.UUID;

public class UniqueTestTwo {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(UniqueTestTwo.class);

   @Test
   public void expectSameAttributesToCreateEqualObjects() {

      // Test parameters
      String sameName = UUID.randomUUID().toString();
      String differentName = UUID.randomUUID().toString();
      // String nullName = null;

      TestEntityTwo sameNameEntity = new TestEntityTwo();
      sameNameEntity.setName(sameName);

      TestEntityWithoutHash sameNameEntityDifferentType = new TestEntityWithoutHash();
      sameNameEntityDifferentType.setName(sameName);

      TestEntityTwo differentNameEntity = new TestEntityTwo();
      differentNameEntity.setName(differentName);

      TestEntityTwo nullNameEntity = new TestEntityTwo();
      // nullNameEntity.setName(nullName);

      TestEntityTwo nullEntity = null;

      String differentClass = new String();

      // Mocks

      // Class under test
      TestEntityTwo classUnderTest = new TestEntityTwo();
      classUnderTest.setName(sameName);

      // Execute
      Assert.assertTrue(classUnderTest.equals(sameNameEntity));
      Assert.assertTrue(sameNameEntity.equals(classUnderTest));
      Assert.assertTrue(classUnderTest.equals(classUnderTest));
      Assert.assertFalse(sameNameEntityDifferentType.equals(classUnderTest));
      Assert.assertFalse(classUnderTest.equals(sameNameEntityDifferentType));
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
