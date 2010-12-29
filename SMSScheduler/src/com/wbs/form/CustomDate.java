/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Vikram S
 */
public class CustomDate {

    private static final String DATE_SEPERATOR = "/";
    private Date date;

    public CustomDate() {
        date = new Date();
    }

    CustomDate(Date date) {
        this.date = date;
    }

    void setDate(Date date) {
        this.date = date;
    }

    public String toString() {
        return toString(false);
    }

    public Date getDate() {
        return date;
    }

    String getFullTime() {
        return toString(true);
    }

    private String toString(boolean showTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int month = cal.get(Calendar.MONTH) + 1;
        final int year = cal.get(Calendar.YEAR);
        String dateStr = day + DATE_SEPERATOR + month + DATE_SEPERATOR + year;
        if (showTime) {
            final int hour = cal.get(Calendar.HOUR_OF_DAY);
            final int minute = cal.get(Calendar.MINUTE);
            dateStr = dateStr + " " + hour + ":" + minute;
        }
        return dateStr;
    }
}
