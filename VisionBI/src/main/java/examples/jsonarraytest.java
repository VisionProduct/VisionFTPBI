package examples;

import org.json.JSONArray;
import org.json.JSONObject;

public class jsonarraytest {

	public static void main(String[] args) {
		try {
			JSONObject myObject = new JSONObject();
			String merchantpaymentsjsonkey = myObject.getString("MERCHANTPAYMENTS");
			JSONArray obj = new JSONArray(merchantpaymentsjsonkey);
			JSONArray jsonarray = new JSONArray(obj.toString());
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
