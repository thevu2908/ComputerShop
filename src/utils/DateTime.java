package utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String formatDate(String date) {
        String newDate = "";
        String[] arr = date.split("-");
        newDate = arr[2] + "-" + arr[1] + "-" + arr[0];
        return newDate;
    }

    public static String getCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}