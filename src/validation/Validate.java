package validation;

import javax.swing.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.regex.Pattern;

public class Validate {
    private static Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    public static boolean isValidEmail(String email) {
        return email.matches(emailPattern.pattern());
    }

    public static boolean isValidPhone(String phone) {
        return phone.matches("^0\\d{9}$");
    }

    public static boolean isValidDate(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        sdf.setLenient(false);
        try {
            Date d = sdf.parse(date);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean isValidDOB(String date) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        try {
            LocalDate dob = LocalDate.parse(date, dateFormatter);
            LocalDate currentDate = LocalDate.now();

            Period age = Period.between(dob, currentDate);

           if (age.getYears() < 18) {
               return false;
           }

           return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean isValidNumber(String number, String text) {
        try {
            int num = Integer.parseInt(number);

            if (num <= 0) {
                JOptionPane.showMessageDialog(null, text + " phải là số dương", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            return true;
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, text + " phải là chữ số", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}