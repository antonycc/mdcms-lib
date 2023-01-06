package uk.co.polycode.mdcms.unit;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import uk.co.polycode.mdcms.util.model.Site;

import java.util.Random;
import java.util.UUID;

/**
 * Test the test helper
 * 
 * @author Antony
 */
public class SiteBeanTest {

   /**
    * Create random expected values
    */
   @Before
   public void setUp() {
   }

   @Test
   public void testSiteGetAndSettersSet() {

	   // Test parameters
	   float vat = new Random().nextFloat();
	   String vatNumber = UUID.randomUUID().toString();
	   String language = UUID.randomUUID().toString();
	   String logo = UUID.randomUUID().toString();
	   String url = UUID.randomUUID().toString();
	   String name = UUID.randomUUID().toString();
	   String fb = UUID.randomUUID().toString();
	   String organisation = UUID.randomUUID().toString();
	   String email = UUID.randomUUID().toString();

	   // Class under test
	   Site classUnderTest = new Site();
	   classUnderTest.setVat(vat);
	   classUnderTest.setVatNumber(vatNumber);
	   classUnderTest.setLanguage(language);
	   classUnderTest.setLogo(logo);
	   classUnderTest.setUrl(url);
	   classUnderTest.setName(name);
	   classUnderTest.setFb(fb);
	   classUnderTest.setOrganisation(organisation);
	   classUnderTest.setEmail(email);

	   Assert.assertTrue(vat == classUnderTest.getVat());
	   Assert.assertEquals(vatNumber, classUnderTest.getVatNumber());
	   Assert.assertEquals(language, classUnderTest.getLanguage());
	   Assert.assertEquals(logo, classUnderTest.getLogo());
	   Assert.assertEquals(url, classUnderTest.getUrl());
	   Assert.assertEquals(name, classUnderTest.getName());
	   Assert.assertEquals(fb, classUnderTest.getFb());
	   Assert.assertEquals(organisation, classUnderTest.getOrganisation());
	   Assert.assertEquals(email, classUnderTest.getEmail());
   }

	@Test
	public void expectNullFromNullURl(){

		// Parameters
		String url = null;

		// Expected data

		// Mocks

		// Class under test
		Site classUnderTest = new Site();
		classUnderTest.setUrl(url);

		// Checks
		String host = classUnderTest.getHostFromUrl();
		Assert.assertNull(host);
	}

	@Test
	public void expectHostFromURl(){

		// Parameters
		String host = "diyaccounting.co.uk";
		String url = "http://" + host + "/";

		// Expected data
		String expectedHost = host;

		// Mocks

		// Class under test
		Site classUnderTest = new Site();
		classUnderTest.setUrl(url);

		// Checks
		String actualHost = classUnderTest.getHostFromUrl();
		Assert.assertEquals(expectedHost, actualHost);
	}

	@Test(expected = IllegalArgumentException.class)
	public void expectExceptionFromNonURI(){

		// Parameters
		String url = "::";

		// Expected data

		// Mocks

		// Class under test
		Site classUnderTest = new Site();
		classUnderTest.setUrl(url);

		// Checks
		classUnderTest.getHostFromUrl();
	}
}
