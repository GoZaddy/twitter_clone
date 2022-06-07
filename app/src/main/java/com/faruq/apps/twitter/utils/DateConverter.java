package com.faruq.apps.twitter.utils;

import androidx.room.TypeConverter;

import java.sql.Date;

public class DateConverter
{
    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
