package xyz.devdeejay.notespace.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

//Type-Converter Class for Room - Specialized class to make data types compatible between Java and Room
public class DateConverter {

    @TypeConverter
    // Long value to Date value
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    // Date value to Long value
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

}
