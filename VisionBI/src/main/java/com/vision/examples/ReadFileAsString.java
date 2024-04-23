package com.vision.examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadFileAsString {
	public static void main(String[] args) {
        String filePath = "E:\\Java_Files\\completion.txt";
        try {
        	StringBuffer str = new StringBuffer();
            List<String> content = Files.readAllLines(Paths.get(filePath));
            for(String readFile : content) {
//            	System.out.println(readFile);
            	str.append(readFile);
            }
            String colValue = getValueForXmlTag(str.toString(), "REPORT_ID_MAX_LENGTH");
            System.out.println(colValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public static String getValueForXmlTag(String source, String tagName) {
		try {
			Matcher regexMatcher = Pattern.compile("\\<" + tagName + "\\>(.*?)\\<\\/" + tagName + "\\>", Pattern.DOTALL)
					.matcher(source);
			return regexMatcher.find() ? regexMatcher.group(1) : null;
		} catch (Exception e) {
			return null;
		}
	}
}