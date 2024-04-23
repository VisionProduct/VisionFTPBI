package com.vision.examples;

import org.apache.commons.lang.StringUtils;

public class CharacterRangeReplacement {
    public static void main(String[] args) {
        String input = "bB.satyapraksah@gmail";
        String replaced = replaceCharacterRanges(input);
        System.out.println(replaced); 
        System.out.println();
        
        String vall = "Bala|!#Satya Prakash|!#banaganapalli|!#asdfas|!#asdfasd|!#ssdfd|!#wr23432|!#234234";
        int count = StringUtils.countMatches(vall, "|!#");
        System.out.println(count);
        String valll = StringUtils.replace(vall, "|!#", "','");
        System.out.println("'"+valll+"'");
    }

    public static String replaceCharacterRanges(String input) {
        // Replace A-Z with 'A'
        String replaced = input.replaceAll("[A-Z]", "A");

        // Replace a-z with 'a'
        replaced = replaced.replaceAll("[a-z]", "a");

        // Replace 0-9 with '#'
        replaced = replaced.replaceAll("[0-9]", "#");

        return replaced;
    }
}