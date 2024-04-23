package examples;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Testing {

	public static void main(String[] args) throws IOException {
		try {
			// OkHttpClient client = new OkHttpClient();
			/*
			 * MediaType mediaType = MediaType.parse("application/json"); //RequestBody body
			 * = RequestBody.create(mediaType, "{\n    \"type\": \"500\"\n}"); //Request
			 * request1 = new Request.Builder().url(
			 * "http://10.16.1.2:8083/DashboardApp/dashboards/getCallPay").post(body) //
			 * .addHeader("content-type", "application/json").build();
			 * 
			 * Response response = client.newCall(request1).execute();
			 * 
			 * String jsonStr = response.body().string();
			 */
			OkHttpClient client = new OkHttpClient().newBuilder().build();
			Request request = new Request.Builder().url("https://reqres.in/api/users?page=2").method("GET", null)
					.addHeader("Content-Type", "application/json")
					.addHeader("VisionAuthenticate",
							"VISIONZDZlNTIyMzZmMDdlY2M3YTAyM2JkZWI5ZjBjYzI4YWI3ZmE0MTdkZjdiNzczYzZiNmY4OTg3N2ViNjBmZTA1ZTcxZDk1ZmUxOWMzYWVlOGEyYjVhYmI5MzYzMTViZTQzNmNlYWMxMTQ5ZmZkZGFiNThhOGU2NWIyNzVkZWEwMzk=")
					.addHeader("Cookie", "__cfduid=d01d737316dec5d02df7424ee35d3ab751599670639").build();
			Response response = client.newCall(request).execute();
			String test = response.body().string();
			//System.out.println(test);
			JSONObject myObject = new JSONObject(test);
			myObject.get("page");
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
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
