package com.EventDetection.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author      Junting Ye <yejuntingben@gmail.com>
 */
public class ParseTime {
    /**
     * Transform format time to Unix time
     * @param  time, format time
     * @return Unix time
     */
    public static long string2Long(String time) {
	long res = -1;
	if (time.length() < 15) {
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	    Date date = null;
	    try {
		date = format.parse(time);
		res = date.getTime();
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	} else {
	    DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    Date date = null;
	    try {
		date = format.parse(time);
		res = date.getTime();
	    } catch (ParseException e) {
		e.printStackTrace();
	    }
	}
	return res;
    }
    /**
     * Transform Unix time to format time 
     * @param  time, Unix time
     * @return format time
     */
    public static String long2String(long time) {
	SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	String dateString = formatter.format(time);
	return dateString;
    }
}
