package fr.test.apigoeuro;

import org.junit.Assert;
import org.junit.Test;

public class ApiGoEuroTest {

	@Test
	public void testGetTrustManager() {
		ApiGoEuro apiTest = new ApiGoEuro();
		Assert.assertNotNull(apiTest.getTrustManager());
	}

	@Test
	public void testCheckPatternIsCorrect() {
		ApiGoEuro apiTest = new ApiGoEuro();
		boolean check = apiTest.checkPattern("Paris");
		Assert.assertTrue(check);
	}
	
	@Test
	public void testCheckPatternIsNotCorrect() {
		ApiGoEuro apiTest = new ApiGoEuro();
		boolean check = apiTest.checkPattern("Paris1");
		Assert.assertFalse(check);
	}

	@Test
	public void testGetJsonDataFromApiGoEuroIsNotNull() {
		ApiGoEuro apiTest = new ApiGoEuro();
		String jsonFromApiGoEuroString = apiTest.getJsonDataFromApiGoEuro("Paris");
		Assert.assertNotNull(jsonFromApiGoEuroString);
	}
	
	@Test
	public void testCreateCsvFile() {
		ApiGoEuro apiTest = new ApiGoEuro();
		boolean isFileCreate = false;
		String jsonFromApiGoEuroString = apiTest.getJsonDataFromApiGoEuro("Paris");
		isFileCreate = apiTest.createCsvFile(jsonFromApiGoEuroString);
		Assert.assertTrue(isFileCreate);
	}

}
