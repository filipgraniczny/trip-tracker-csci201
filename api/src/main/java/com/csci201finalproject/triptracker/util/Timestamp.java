package com.csci201finalproject.triptracker.util;

import java.sql.Date;
import java.time.LocalDate;

public class Timestamp {
    public static Date timestampToDate(String timestamp) {
        String[] arr = timestamp.split("-");
        LocalDate localDate = LocalDate.of(Integer.valueOf(arr[0]), Integer.valueOf(arr[1]), Integer.valueOf(arr[2]));
        Date date = Date.valueOf(localDate);
        return date;
    }
}
