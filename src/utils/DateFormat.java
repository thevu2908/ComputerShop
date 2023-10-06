package utils;

public class DateFormat {
    public static String formatDate(String date) {
        String newDate = "";
        String[] arr = date.split("-");
        newDate = arr[2] + "-" + arr[1] + "-" + arr[0];
        return newDate;
    }
}