package uk.co.diyaccounting.cms.unit;

import org.junit.Assert;
import org.junit.Test;
import uk.co.diyaccounting.cms.ops.NameBodyItem2;
import uk.co.diyaccounting.cms.service.ContentException;
import uk.co.diyaccounting.util.io.FileLikePathService;

/**
 * Test the name body content item
 */
public class MdNameBodyTest {

	private final String localPath = MdNameBodyTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
	private final String baseFileUrn = "urn:diyaccounting.co.uk:file://" + localPath;
	private final String baseClasspathUrn = "urn:diyaccounting.co.uk:classpath:/";

	@Test
	public void testFileTitleWithoutPostfix() throws ContentException {

		String homePath = baseFileUrn + "content/test/path/TestPage.md";

		// Expected values
		String expectedTitle = "expectedTitle";
		String expectedPostfix = "Postfix";

		// Set up object
		NameBodyItem2 article = new NameBodyItem2();
		article.setPath(homePath);
		article.populateContent(new FileLikePathService());
		article.setName(expectedTitle + expectedPostfix);
		article.postPopulationConfig();

		// Check values
		Assert.assertEquals("Expected title with post fix", expectedTitle + expectedPostfix, article.getName());
		//Assert.assertEquals("Expected title without post fix", expectedTitle, article.getNameWithoutPostfix());

	}

	@Test
	public void testTitleWithoutPostfix() throws ContentException {

		String homePath = baseClasspathUrn + "content/test/path/TestPage.md";

		// Expected values
		String expectedTitle = "expectedTitle";
		String expectedPostfix = "Postfix";

		// Set up object
		NameBodyItem2 article = new NameBodyItem2();
		article.setPath(homePath);
		article.populateContent(new FileLikePathService());
		article.setName(expectedTitle + expectedPostfix);
		article.postPopulationConfig();

		// Check values
		Assert.assertEquals("Expected title with post fix", expectedTitle + expectedPostfix, article.getName());
	}

	@Test
	public void testTitleFileWithoutPostfixNoPostfix() throws ContentException {

		String homePath = baseFileUrn + "content/test/path/TestPage.md";

		// Expected values
		String expectedTitle = "expectedtitle";

		// Set up object
		NameBodyItem2 article = new NameBodyItem2();
		article.setPath(homePath);
		article.populateContent(new FileLikePathService());
		article.setName(expectedTitle);
		article.postPopulationConfig();

		// Check values
		Assert.assertEquals("Expected title with post fix", expectedTitle, article.getName());
	}

	@Test
	public void testTitleWithoutPostfixNoPostfix() throws ContentException {

		String homePath = baseClasspathUrn + "content/test/path/TestPage.md";

		// Expected values
		String expectedTitle = "expectedtitle";

		// Set up object
		NameBodyItem2 article = new NameBodyItem2();
		article.setPath(homePath);
		article.populateContent(new FileLikePathService());
		article.setName(expectedTitle);
		article.postPopulationConfig();

		// Check values
		Assert.assertEquals("Expected title with post fix", expectedTitle, article.getName());
	}

	@Test
	public void testNullFileTitleWithoutPostfix() throws ContentException {

		String homePath = baseFileUrn + "content/test/path/TestPage.md";

		// Set up object
		NameBodyItem2 article = new NameBodyItem2();
		article.setPath(homePath);
		article.populateContent(new FileLikePathService());
		article.setName(null);
		article.postPopulationConfig();

		// Check values
		Assert.assertNull(article.getName());
		//Assert.assertNull(article.getNameWithoutPostfix());
	}

	@Test
	public void testNullTitleWithoutPostfix() throws ContentException {

		String homePath = baseClasspathUrn + "content/test/path/TestPage.md";

		// Set up object
		NameBodyItem2 article = new NameBodyItem2();
		article.setPath(homePath);
		article.populateContent(new FileLikePathService());
		article.setName(null);
		article.postPopulationConfig();

		// Check values
		Assert.assertNull(article.getName());
		//Assert.assertNull(article.getNameWithoutPostfix());
	}
}
