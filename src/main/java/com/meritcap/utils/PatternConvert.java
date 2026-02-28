package com.meritcap.utils;

public class PatternConvert {
    public static String jumbleSearch(String input){
        StringBuilder queryName = new StringBuilder("%");
        for(int i=0;i<input.length();i++){
            queryName.append(input.charAt(i)).append("%");
        }
        return queryName.toString();
    }
}
