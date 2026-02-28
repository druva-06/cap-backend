package com.meritcap.utils;

public class Generators {
    public static String  generateCampusCode(String country, long count){
        String campusCode = "";
        if(country.equals("Australia")){
            campusCode = "AUS";
        }
        else if(country.equals("Canada")){
            campusCode = "CAN";
        }
        return campusCode + count;
    }
}
