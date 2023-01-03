package uk.co.diyaccounting.cms.unit;

import org.junit.Assert;
import org.junit.Test;

import uk.co.diyaccounting.cms.ops.BooleanItem;
import uk.co.diyaccounting.cms.ops.BooleanWithMissingItem;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.util.io.FileLikePathService;

/**
 * Reads a content item
 * 
 * @author Antony
 */
public class MdBooleanItemTest {

	private final String localPath = MdBooleanItemTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	private final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
	private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

   @Test
   public void expectBooleanContentToBeTrue() throws ContentException {

      boolean expectedFeatured = true;

      String booleanContent = baseClasspathUrn + "test1/boolean.md";

      // instance under test "item" is initialised using constructor
      BooleanItem classUnderTest = new BooleanItem();
      classUnderTest.setPath(booleanContent);
      classUnderTest.populateContent(new FileLikePathService());
      boolean actualFeatured = classUnderTest.getFeatured();

      // Check
      Assert.assertEquals("Content item should be true", expectedFeatured, actualFeatured);
   }

	@Test
	public void expectBooleanFileContentToBeTrue() throws ContentException {

		boolean expectedFeatured = true;

		String booleanContent = baseFileUrn + "test1/boolean.md";

		// instance under test "item" is initialised using constructor
		BooleanItem classUnderTest = new BooleanItem();
		classUnderTest.setPath(booleanContent);
		classUnderTest.populateContent(new FileLikePathService());
		boolean actualFeatured = classUnderTest.getFeatured();

		// Check
		Assert.assertEquals("Content item should be true", expectedFeatured, actualFeatured);
	}

   @Test
   public void expectBooleanContentToBeFalse() throws ContentException {

      boolean expectedFeatured = false;

      String booleanContent = baseClasspathUrn + "test1/booleanfalse.md";

      // instance under test "item" is initialised using constructor
      BooleanItem classUnderTest = new BooleanItem();
      classUnderTest.setPath(booleanContent);
      classUnderTest.populateContent(new FileLikePathService());
      boolean actualFeatured = classUnderTest.getFeatured();

      // Check
      Assert.assertEquals("Content item should be true", expectedFeatured, actualFeatured);
   }

   @Test
   public void expectUnpopulatedBooleanToDefaultToFalse() throws ContentException {

      boolean expectedFeatured = false;

      String booleanContent = baseClasspathUrn + "test1/noboolean.md";

      // instance under test "item" is initialised using constructor
      BooleanItem classUnderTest = new BooleanItem();
      classUnderTest.setPath(booleanContent);
      classUnderTest.populateContent(new FileLikePathService());
      boolean actualFeatured = classUnderTest.getFeatured();

      // Check
      Assert.assertEquals("Content item should be true", expectedFeatured, actualFeatured);
   }

	@Test
	public void expectMissingBooleanToDefaultToFalse() throws ContentException {

		boolean expectedFeatured = false;

		String booleanContent = baseClasspathUrn + "test1/boolean.md";

		// instance under test "item" is initialised using constructor
		BooleanWithMissingItem classUnderTest = new BooleanWithMissingItem();
		classUnderTest.setPath(booleanContent);
		classUnderTest.populateContent(new FileLikePathService());
		boolean actualFeatured = classUnderTest.getFeatured();

		// Check
		Assert.assertEquals("Content item should be true", expectedFeatured, actualFeatured);
	}
}