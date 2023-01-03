package uk.co.diyaccounting.persistence.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.persistence.FlywayConfigStub;

/**
 * Test the doa mocking the transaction manager
 *
 * @author Antony
 */
public class FlywayStubTest {

   /**
    * The logger for this class.
    */
   // private static final Logger logger = LoggerFactory.getLogger(CatalogueServiceImplTest.class);

   @Test
   public void exerciseStubMethods() {

      // Test parameters

      // Mocks

      // Class under test
      FlywayConfigStub classUnderTest = new FlywayConfigStub();

      // Execute
      classUnderTest.setDataSource(null);

      // Checks - used with inherited implementations
      Assert.assertEquals(0, classUnderTest.migrate());
      Assert.assertNull(classUnderTest.getClassLoader());
      Assert.assertNull(classUnderTest.getDataSource());
      Assert.assertNotNull(classUnderTest.getBaselineVersion());
      Assert.assertNotNull(classUnderTest.getBaselineDescription());
      Assert.assertNotNull(classUnderTest.getResolvers());
      Assert.assertFalse(classUnderTest.isSkipDefaultResolvers());
      Assert.assertNotNull(classUnderTest.getCallbacks());
      Assert.assertFalse(classUnderTest.isSkipDefaultCallbacks());
      Assert.assertNotNull(classUnderTest.getRepeatableSqlMigrationPrefix());
      Assert.assertNotNull(classUnderTest.getSqlMigrationSeparator());
      Assert.assertNotNull(classUnderTest.getSqlMigrationPrefix());
      Assert.assertTrue(classUnderTest.isPlaceholderReplacement());
      Assert.assertNotNull(classUnderTest.getPlaceholderSuffix());
      Assert.assertNotNull(classUnderTest.getPlaceholderPrefix());
      Assert.assertNotNull(classUnderTest.getPlaceholders());
      Assert.assertNull(classUnderTest.getTarget());
      Assert.assertNotNull(classUnderTest.getTable());
      Assert.assertNotNull(classUnderTest.getSchemas());
      Assert.assertNotNull(classUnderTest.getEncoding());
      Assert.assertNotNull(classUnderTest.getLocations());
      Assert.assertFalse(classUnderTest.isOutOfOrder());
      Assert.assertFalse(classUnderTest.isCleanDisabled());
      Assert.assertFalse(classUnderTest.isGroup());
      Assert.assertNull(classUnderTest.getInstalledBy());
      Assert.assertTrue(classUnderTest.isIgnoreMissingMigrations());
      Assert.assertFalse(classUnderTest.isCleanOnValidationError());
      Assert.assertFalse(classUnderTest.isMixed());
      Assert.assertFalse(classUnderTest.isBaselineOnMigrate());
      Assert.assertTrue(classUnderTest.isIgnoreFutureMigrations());
      Assert.assertFalse(classUnderTest.isValidateOnMigrate());
      //Assert.assertNull(classUnderTest.getErrorHandlers());
      //Assert.assertNotNull(classUnderTest.getDryRunOutput());
      Assert.assertNotNull(classUnderTest.getSqlMigrationSuffixes());
      //Assert.assertNull(classUnderTest.getUndoSqlMigrationPrefix());

      // Checks - used with implemented stubs
      /*
      Assert.assertEquals(0, classUnderTest.migrate());
      Assert.assertNull(classUnderTest.getClassLoader());
      Assert.assertNull(classUnderTest.getDataSource());
      Assert.assertNull(classUnderTest.getBaselineVersion());
      Assert.assertNull(classUnderTest.getBaselineDescription());
      Assert.assertNotNull(classUnderTest.getResolvers());
      Assert.assertFalse(classUnderTest.isSkipDefaultResolvers());
      Assert.assertNotNull(classUnderTest.getCallbacks());
      Assert.assertFalse(classUnderTest.isSkipDefaultCallbacks());
      Assert.assertNull(classUnderTest.getRepeatableSqlMigrationPrefix());
      Assert.assertNull(classUnderTest.getSqlMigrationSeparator());
      Assert.assertNull(classUnderTest.getSqlMigrationPrefix());
      Assert.assertFalse(classUnderTest.isPlaceholderReplacement());
      Assert.assertNull(classUnderTest.getPlaceholderSuffix());
      Assert.assertNull(classUnderTest.getPlaceholderPrefix());
      Assert.assertNull(classUnderTest.getPlaceholders());
      Assert.assertNull(classUnderTest.getTarget());
      Assert.assertNull(classUnderTest.getTable());
      Assert.assertNotNull(classUnderTest.getSchemas());
      Assert.assertNotNull(classUnderTest.getEncoding());
      Assert.assertNotNull(classUnderTest.getLocations());
      Assert.assertFalse(classUnderTest.isOutOfOrder());
      Assert.assertFalse(classUnderTest.isCleanDisabled());
      Assert.assertFalse(classUnderTest.isGroup());
      Assert.assertNull(classUnderTest.getInstalledBy());
      Assert.assertFalse(classUnderTest.isIgnoreMissingMigrations());
      Assert.assertFalse(classUnderTest.isCleanOnValidationError());
      Assert.assertFalse(classUnderTest.isMixed());
      Assert.assertFalse(classUnderTest.isBaselineOnMigrate());
      Assert.assertFalse(classUnderTest.isIgnoreFutureMigrations());
      Assert.assertFalse(classUnderTest.isValidateOnMigrate());
      //Assert.assertNull(classUnderTest.getErrorHandlers());
      Assert.assertNull(classUnderTest.getDryRunOutput());
      Assert.assertNotNull(classUnderTest.getSqlMigrationSuffixes());
      Assert.assertNull(classUnderTest.getUndoSqlMigrationPrefix());
      */
   }

}