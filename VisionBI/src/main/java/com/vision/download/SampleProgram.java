package com.vision.download;

public class SampleProgram {
	
	public static boolean isValid(String pInput) {
		return !((pInput == null) || (pInput.trim().length() == 0) || ("".equals(pInput)));
	}
	public static boolean isValidId(String pInput) {
		if (!isValid(pInput)) {
			return false;
		}
		try {
			Integer.parseInt(pInput);
		} catch (NumberFormatException lNFE) {
			return false;
		}
		return true;
	}

	public static void main(String[] args) {

		String phNumber = "Text         Text";
		String commaSeperatedValues= "\n!@#\t!@#\r!@#,!@#\"!@#";
		String[] csvRemLst = null;
		if (isValid(commaSeperatedValues)) {
			csvRemLst = commaSeperatedValues.split("!@#");

		}
		
		
		String val = null;
		for (String replaceVal : csvRemLst) {
			val = phNumber.toString().replaceAll(replaceVal, "").replaceAll("\\"+replaceVal,"").replaceAll("\\s", "").trim();
		}
		if (val == null) {
			val = phNumber.toString();
		}
		System.out.println(val);
		
	}

}
