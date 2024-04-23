package examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

public class Testing300 {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		JSONParser parser = new JSONParser();
		try {
			String resourceName = "E:\\testjson/300test.json";
			JSONObject jsonObject = parseJSONFile(resourceName);

			String test = jsonObject.get("OWNACCOUNT").toString();
			JSONObject jsonObject1 = new JSONObject(test);
			JSONArray ja_data = jsonObject1.getJSONArray("TRANSACTIONS");
			int length = jsonObject1 .length(); 
			for(int i=0; i<length; i++) 
			{
			    JSONObject jObj = ja_data.getJSONObject(i);
			    String currency = jObj.getString("CURRENCY");
			    System.out.println(currency);
			} 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
		String content = new String(Files.readAllBytes(Paths.get(filename)));
		return new JSONObject(content);
	}
}
