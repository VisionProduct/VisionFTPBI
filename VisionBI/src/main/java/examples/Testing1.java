package examples;

import java.io.FileReader;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Testing1 {

	public static void main(String[] args) throws IOException {
	
		/*OkHttpClient client = new OkHttpClient().newBuilder().build();
		Request request = new Request.Builder().url("https://reqres.in/api/users?page=2").method("GET", null)
				.addHeader("Content-Type", "application/json")
				.addHeader("VisionAuthenticate",
						"VISIONZDZlNTIyMzZmMDdlY2M3YTAyM2JkZWI5ZjBjYzI4YWI3ZmE0MTdkZjdiNzczYzZiNmY4OTg3N2ViNjBmZTA1ZTcxZDk1ZmUxOWMzYWVlOGEyYjVhYmI5MzYzMTViZTQzNmNlYWMxMTQ5ZmZkZGFiNThhOGU2NWIyNzVkZWEwMzk=")
				.addHeader("Cookie", "__cfduid=d01d737316dec5d02df7424ee35d3ab751599670639").build();
		Response response = client.newCall(request).execute();
		String test = response.body().string();*/
		
		try {
			OkHttpClient client = new OkHttpClient().newBuilder().build();
		JSONParser parser = new JSONParser();
		//Request request = (Request) parser.parse(new FileReader("E:\\testjson/test.json"));
		
		Request request = new Request.Builder().build();
				
		Response response = client.newCall(request).execute();
		String test = response.body().string();
		JSONObject myObject = new JSONObject(test);
		
		myObject.get("per_page");
	   if(myObject.has("data")) {
		}
		Object  s = myObject.get("data");

		
		JSONArray jsonarray = new JSONArray(s.toString());
		for (int i = 0; i < jsonarray.length(); i++) {
			JSONObject jsonobject = jsonarray.getJSONObject(i);

			if (jsonobject.has("email")) {

			}

			String name = jsonobject.getString("email");
			System.out.println("testting " + name); // String url =
			//jsonobject.getString("url");
		}

		}catch (Exception e) {
			e.printStackTrace();
		}
	
	
	
	
	}

}
