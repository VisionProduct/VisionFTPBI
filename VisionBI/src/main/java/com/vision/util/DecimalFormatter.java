package com.vision.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
public class DecimalFormatter {
    public static String formatToTwoDecimalPlaces(String numberString) throws ParseException {
        // Create a NumberFormat instance to handle comma separation (Locale.US assumes comma as thousands separator)
        NumberFormat format = NumberFormat.getNumberInstance(Locale.US);
        
        // Parse the string to a number
        Number number = format.parse(numberString);
        
        // Convert the Number to a double
        double doubleValue = number.doubleValue();
        
        // Use DecimalFormat to format the number to two decimal places
        DecimalFormat df = new DecimalFormat("###,###,###,###,###,###,##0.00");
        return df.format(doubleValue);
    }
    
    public static void main(String[] args) throws InterruptedException {
    	int index = 0;
    	boolean customeFlag = false;
    	while(!customeFlag) {
    		if(index>10)
    			customeFlag = true;
    		Thread.sleep(1000);
    		System.out.println(index);
    		index++;
    	}
    	/*boolean isCompletionFileExits = false;
		for (int i = 1; i <= 20; i++) {
			isCompletionFileExits = isFileExistForSparkSubmit(sftpChannel, currentDirectory1, "completion.txt");
			if(isCompletionFileExits) {
				System.out.println("Current Directory Completed -> "+currentDirectory1);
			}
			Thread.sleep(1000*i);
		}*/
		
        /*String numberString = "12334312.34567";
        try {
            System.out.println(formatToTwoDecimalPlaces(numberString));  // Output: 34,312.35
        } catch (ParseException e) {
            e.printStackTrace();
        }*/
    }
}

