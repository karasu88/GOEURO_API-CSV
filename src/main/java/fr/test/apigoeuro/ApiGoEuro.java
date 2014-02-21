package fr.test.apigoeuro;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.json.CDL;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * @author Anthony Juanes
 * goal: get information from the api go Euro
 */
public class ApiGoEuro {	
	/**
	 * constructor of ApiGoEuro class
	 */
	public ApiGoEuro() {}	
	
	/*
	 * getTrustManager
	 * input: none
	 * output: certificat TrustManager[]
	 * goal: get a valid X509 certificate
	 */
	public TrustManager[] getTrustManager() {
		TrustManager[] certificat = new TrustManager[] {
			new X509TrustManager() {				
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] certificats, String s)
						throws CertificateException {						
				}
				
				@Override
				public void checkClientTrusted(X509Certificate[] certificats, String s)
						throws CertificateException {						
				}
			}
		};
		return certificat;
	}

	/*
	 * createSocketTLS
	 * input: none
	 * output: none
	 * goal: create a secure socket in order to receive data from go euro API
	 */
	public void createSocketTLS() {
		try {
			TrustManager[] trustManager = getTrustManager();
			SSLContext sslcontext = SSLContext.getInstance("TLS");
			sslcontext.init(null, trustManager, null);
			HttpsURLConnection.setDefaultSSLSocketFactory(sslcontext.getSocketFactory());
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * checkPattern
	 * input: locationInput String
	 * output: boolean
	 * goal: check the pattern of the locationInput String contain only letters
	 */
	public boolean checkPattern(String locationInput) {
		String locationInputPattern = "[a-zA-Z&&[^\\W]]*";
		Pattern patternString = Pattern.compile(locationInputPattern);
		Matcher regexMatcher = patternString.matcher(locationInput);
		if(!regexMatcher.matches()) {
			return false;
		}
		return true;
	}
	
	/*
	 * getJsonDataFromApiGoEuro
	 * input: locationInput String
	 * output: jsonString String
	 * goal: get a String containing JSON data from go euro API, return null if the location input isn't valid
	 */
	public String getJsonDataFromApiGoEuro(String locationInput) {
		boolean check = false;
		check = checkPattern(locationInput);
		if(check == false) {
			System.out.println("Please enter a valid location, only letters are allowed ");
		} else {
			try {
				URL url = null;
				BufferedReader jsonFromApiGoEuro = null;
				StringBuilder jsonString = new StringBuilder();
				String line = null;
				createSocketTLS();
				url = new URL("https://api.goeuro.com/api/v1/suggest/position/en/name/" +locationInput);
				jsonFromApiGoEuro = new BufferedReader(new InputStreamReader(url.openStream()));
				while((line = jsonFromApiGoEuro.readLine()) != null) {
					jsonString.append(line);
				}
				return jsonString.toString();
			} catch(Exception ex) {
				System.out.println("Error during the connection with the API, check your internet connection");
			}
		}
		return null;
	}
	
	/*
	 * createCsvFile
	 * input: jsonString String
	 * output: boolean
	 * goal: create a CSV file from a String
	 */
	public boolean createCsvFile(String jsonString) {
		try {
			File csvFile = new File("dataFromApiGoEuro.csv");
			JSONObject resultsjsonObject = new JSONObject(jsonString);
			JSONArray resultsJsonArray = resultsjsonObject.getJSONArray("results");
			
			for(int i=0; i<resultsJsonArray.length(); i++) {
				JSONObject position = resultsJsonArray.getJSONObject(i).getJSONObject("geo_position");
				resultsJsonArray.getJSONObject(i).put("latitude", position.get("latitude"));
				resultsJsonArray.getJSONObject(i).put("longitude", position.get("longitude"));
				resultsJsonArray.getJSONObject(i).remove("geo_position");
			}
			String csv = CDL.toString(resultsJsonArray);
			FileUtils.writeStringToFile(csvFile, csv);
			System.out.println("dataFromApiGoEuro.csv create");
			return true;
		} catch(Exception ex) {
			System.out.println("dataFromApiGoEuro.csv could not be create");
			return false;	
		}
	}
}
