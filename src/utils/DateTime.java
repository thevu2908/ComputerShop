package utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTime {
    public static String formatDate(String date) {
        String newDate = "";
        String[] arr = date.split("-");
        newDate = arr[2] + "-" + arr[1] + "-" + arr[0];
        return newDate;
    }

    public static String getStringCurrentDate() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static Date getCurrentDate() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        return dateFormat.parse(dateFormat.format(date));
    }

    public static Date parseDate(String date) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.parse(date);
    }
}