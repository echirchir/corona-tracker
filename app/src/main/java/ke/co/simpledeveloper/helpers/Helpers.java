package ke.co.simpledeveloper.helpers;

import java.util.Calendar;

public class Helpers {

    public static final String DECIMAL_FORMAT = "#,###.00";

    public static String getCurrentDateFormatted(){

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        if ((month + 1) < 10){
            return year +"-" + "0" + (month+1) +"-" + day;
        }else{
            return day +"-" + (month+1) +"-" + year;
        }

    }
}
