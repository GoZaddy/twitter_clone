package com.faruq.apps.twitter.utils;

public class FormatNumbers {
    public static String ShortenNumber(Integer number){
        if (number < 10000){
            return number.toString();
        } else {
            if (number < 1000000){
                double num = number/1000.0;
                return String.format("%.1f", num)+"k";
            } else if (number < 1000000000){
                double num = number/1000000.0;
                return String.format("%.1f", num)+"k";
            } else {
                return "1b";
            }
        }
    }
}
