package com.faruq.apps.twitter.utils;

import java.util.List;

public class DateFormatter {
    public static String getTime(String createdAtFullDate){
        String[] tokens = createdAtFullDate.split(" ");
        String time = tokens[3];
        String[] timeTokens = time.split(":");
        return timeTokens[0]+":"+timeTokens[1];
    }

    public static String getDate(String createdAtFullDate){
        String[] tokens = createdAtFullDate.split(" ");
        String day = tokens[2];
        String month = tokens[1];
        String year = tokens[5].substring(2);
        return String.format("%s %s %s", day, month, year);
    }
}
