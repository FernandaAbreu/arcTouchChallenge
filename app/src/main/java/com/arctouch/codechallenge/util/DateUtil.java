package com.arctouch.codechallenge.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fernanda on 19/08/2018.
 */

public class DateUtil {
    public static String getDate(String date_s){
        try
        {

            SimpleDateFormat simpledateformat = new SimpleDateFormat("yyyy-MM-dd");
            Date tempDate=simpledateformat.parse(date_s);
            SimpleDateFormat outputDateFormat = new SimpleDateFormat("dd/MM/YYYY");
            return outputDateFormat.format(tempDate);
        } catch (ParseException ex)
        {
            return "";
        }
    }
}
