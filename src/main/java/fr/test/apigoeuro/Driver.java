package fr.test.apigoeuro;

/**
 * @author Anthony Juanes
 * goal: Driver class
 */
public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ApiGoEuro apiGoEuro = new ApiGoEuro();
		String locationInputFromUser = args[0];
		String jsonFromApiGoEuroString = apiGoEuro.getJsonDataFromApiGoEuro(locationInputFromUser);
		apiGoEuro.createCsvFile(jsonFromApiGoEuroString);
	}

}
