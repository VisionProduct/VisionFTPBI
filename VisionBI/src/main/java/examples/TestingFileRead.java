package examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

public class TestingFileRead {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {

		JSONParser parser = new JSONParser();
		try {
			String resourceName = "E:\\testjson/test.json";
			JSONObject jsonObject = parseJSONFile(resourceName);
			String test = jsonObject.get("MERCHANTPAYMENTS").toString();
			JSONArray mainObj = new JSONArray(test);
			for (int i = 0; i < mainObj.length(); i++) {
				JSONObject jsonobject = mainObj.getJSONObject(i);
				String totalpaidamount = jsonobject.getString("TOTALPAIDAMOUNT");
				System.out.println("totalpaidamount "+totalpaidamount);
				
				/*String unpaidtransactions = jsonobject.get("UNPAIDTRANSACTIONS").toString();
				JSONArray innerarray = new JSONArray(unpaidtransactions);
				for (int j = 0; j < innerarray.length(); j++) {
					JSONObject jsonobjectarray = innerarray.getJSONObject(j);
					String currency = jsonobjectarray.get("CURRENCY").toString();
					String mvisarrnStatus = jsonobjectarray.get("MVISARRNSTATUS").toString();
					String mvisarrn = jsonobjectarray.get("MVISARRN").toString();
					String description = jsonobjectarray.get("DESCRIPTION").toString();
					String amount = jsonobjectarray.get("AMOUNT").toString();
					String receiptid = jsonobjectarray.get("RECEIPTID").toString();
					String transactionType = jsonobjectarray.get("TRANSACTIONTYPE").toString();
					String paymentMode = jsonobjectarray.get("PAYMENTMODE").toString();
					String stan = jsonobjectarray.get("STAN").toString();
					String debitAccount = jsonobjectarray.get("DEBITACCOUNT").toString();
					String cuauthorizattionCode = jsonobjectarray.get("AUTHORIZATTIONCODE").toString();
					String transactionDate = jsonobjectarray.get("TRANSACTIONDATE").toString();
			        	  
					System.out.println("CURRENCY "+currency);
					System.out.println("MVISARRNSTATUS "+mvisarrnStatus);
					System.out.println("MVISARRN "+mvisarrn);
					System.out.println("DESCRIPTION "+description);
					System.out.println("AMOUNT "+amount);
					System.out.println("RECEIPTID "+receiptid);
					System.out.println("TRANSACTIONTYPE "+transactionType);
					System.out.println("PAYMENTMODE "+paymentMode);
					System.out.println("STAN "+stan);
					System.out.println("DEBITACCOUNT "+debitAccount);
					System.out.println("AUTHORIZATTIONCODE "+cuauthorizattionCode);
					System.out.println("TRANSACTIONDATE "+transactionDate);
					
				}
			}*/
				//Unpaid Transactions
				String unpaidtransactions = jsonobject.get("UNPAIDTRANSACTIONS").toString();
				JSONArray unpaidinnerarray = new JSONArray(unpaidtransactions);
				for (int j = 0; j < unpaidinnerarray.length(); j++) {
					JSONObject jsonobjectarray = unpaidinnerarray.getJSONObject(j);
					String currency = jsonobjectarray.get("CURRENCY").toString();
					String mvisarrnStatus = jsonobjectarray.get("MVISARRNSTATUS").toString();
					String mvisarrn = jsonobjectarray.get("MVISARRN").toString();
					String description = jsonobjectarray.get("DESCRIPTION").toString();
					String amount = jsonobjectarray.get("AMOUNT").toString();
					String receiptid = jsonobjectarray.get("RECEIPTID").toString();
					String transactionType = jsonobjectarray.get("TRANSACTIONTYPE").toString();
					String paymentMode = jsonobjectarray.get("PAYMENTMODE").toString();
					String stan = jsonobjectarray.get("STAN").toString();
					String debitAccount = jsonobjectarray.get("DEBITACCOUNT").toString();
					String cuauthorizattionCode = jsonobjectarray.get("AUTHORIZATTIONCODE").toString();
					String transactionDate = jsonobjectarray.get("TRANSACTIONDATE").toString();
			        	  //UNPAIDTRANSACTIONS Print values
					System.out.println("UNPAIDTRANSACTIONS Print Values ");
					System.out.println("CURRENCY "+currency);
					System.out.println("MVISARRNSTATUS "+mvisarrnStatus);
					System.out.println("MVISARRN "+mvisarrn);
					System.out.println("DESCRIPTION "+description);
					System.out.println("AMOUNT "+amount);
					System.out.println("RECEIPTID "+receiptid);
					System.out.println("TRANSACTIONTYPE "+transactionType);
					System.out.println("PAYMENTMODE "+paymentMode);
					System.out.println("STAN "+stan);
					System.out.println("DEBITACCOUNT "+debitAccount);
					System.out.println("AUTHORIZATTIONCODE "+cuauthorizattionCode);
					System.out.println("TRANSACTIONDATE "+transactionDate);
					
				}
				
				String failedtransactions = jsonobject.get("FAILEDTRANSACTIONS").toString();
				JSONArray failedinnerarray = new JSONArray(failedtransactions);
				for (int j = 0; j < failedinnerarray.length(); j++) {
					JSONObject jsonobjectarray = failedinnerarray.getJSONObject(j);
					String failedCurrency = jsonobjectarray.get("CURRENCY").toString();
					String failedMvisarrnStatus = jsonobjectarray.get("MVISARRNSTATUS").toString();
					String failedMvisarrn = jsonobjectarray.get("MVISARRN").toString();
					String failedDscription = jsonobjectarray.get("DESCRIPTION").toString();
					String failedAmount = jsonobjectarray.get("AMOUNT").toString();
					String failedReceiptid = jsonobjectarray.get("RECEIPTID").toString();
					String failedTransactionType = jsonobjectarray.get("TRANSACTIONTYPE").toString();
					String failedPaymentMode = jsonobjectarray.get("PAYMENTMODE").toString();
					String failedStan = jsonobjectarray.get("STAN").toString();
					String failedDebitAccount = jsonobjectarray.get("DEBITACCOUNT").toString();
					String failedCuauthorizattionCode = jsonobjectarray.get("AUTHORIZATTIONCODE").toString();
					String failedTransactionDate = jsonobjectarray.get("TRANSACTIONDATE").toString();
			        	  //FAILEDTRANSACTIONS Print Values
					System.out.println("FAILEDTRANSACTIONS Print Values ");
					System.out.println("CURRENCY "+failedCurrency);
					System.out.println("MVISARRNSTATUS "+failedMvisarrnStatus);
					System.out.println("MVISARRN "+failedMvisarrn);
					System.out.println("DESCRIPTION "+failedDscription);
					System.out.println("AMOUNT "+failedAmount);
					System.out.println("RECEIPTID "+failedReceiptid);
					System.out.println("TRANSACTIONTYPE "+failedTransactionType);
					System.out.println("PAYMENTMODE "+failedPaymentMode);
					System.out.println("STAN "+failedStan);
					System.out.println("DEBITACCOUNT "+failedDebitAccount);
					System.out.println("AUTHORIZATTIONCODE "+failedCuauthorizattionCode);
					System.out.println("TRANSACTIONDATE "+failedTransactionDate);
					
				}
				
				String paidtransactions = jsonobject.get("PAIDTRANSACTIONS").toString();
				JSONArray paidinnerarray = new JSONArray(paidtransactions);
				for (int j = 0; j < paidinnerarray.length(); j++) {
					JSONObject jsonobjectarray = paidinnerarray.getJSONObject(j);
					String paidCurrency = jsonobjectarray.get("CURRENCY").toString();
					String paidMvisarrnStatus = jsonobjectarray.get("MVISARRNSTATUS").toString();
					String paidMvisarrn = jsonobjectarray.get("MVISARRN").toString();
					String paidDscription = jsonobjectarray.get("DESCRIPTION").toString();
					String paidAmount = jsonobjectarray.get("AMOUNT").toString();
					String paidReceiptid = jsonobjectarray.get("RECEIPTID").toString();
					String paidTransactionType = jsonobjectarray.get("TRANSACTIONTYPE").toString();
					String paidPaymentMode = jsonobjectarray.get("PAYMENTMODE").toString();
					String paidStan = jsonobjectarray.get("STAN").toString();
					String paidDebitAccount = jsonobjectarray.get("DEBITACCOUNT").toString();
					String paidCuauthorizattionCode = jsonobjectarray.get("AUTHORIZATTIONCODE").toString();
					String paidTransactionDate = jsonobjectarray.get("TRANSACTIONDATE").toString();
			        	  //FAILEDTRANSACTIONS Print Values
					System.out.println("PAIDTRANSACTIONS Print Values ");
					System.out.println("CURRENCY "+paidCurrency);
					System.out.println("MVISARRNSTATUS "+paidMvisarrnStatus);
					System.out.println("MVISARRN "+paidMvisarrn);
					System.out.println("DESCRIPTION "+paidDscription);
					System.out.println("AMOUNT "+paidAmount);
					System.out.println("RECEIPTID "+paidReceiptid);
					System.out.println("TRANSACTIONTYPE "+paidTransactionType);
					System.out.println("PAYMENTMODE "+paidPaymentMode);
					System.out.println("STAN "+paidStan);
					System.out.println("DEBITACCOUNT "+paidDebitAccount);
					System.out.println("AUTHORIZATTIONCODE "+paidCuauthorizattionCode);
					System.out.println("TRANSACTIONDATE "+paidTransactionDate);
					
				}
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
