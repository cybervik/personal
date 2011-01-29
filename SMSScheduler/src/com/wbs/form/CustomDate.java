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

    public String getFullTime() {
        return toString(true);
    }

    private Date addDate(int rType, int rValue) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(this.date);
        final int cVal = cal.get(rType);
        cal.set(rType, cVal + rValue);
        return cal.getTime();
    }

    private int getIntValue(String s) throws NumberFormatException {
        int rValue = 0;
        try {
            rValue = Integer.parseInt(s);
        } catch (NumberFormatException numberFormatException) {
        }
        return rValue;
    }

    private String toString(boolean showTime) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final int day = cal.get(Calendar.DAY_OF_MONTH);
        final int month = cal.get(Calendar.MONTH) + 1;
        final int year = cal.get(Calendar.YEAR);
        String dateStr = pad(day) + DATE_SEPERATOR + pad(month) + DATE_SEPERATOR + year;
        if (showTime) {
            final int hour = cal.get(Calendar.HOUR_OF_DAY);
            final int minute = cal.get(Calendar.MINUTE);
            dateStr = dateStr + " " + pad(hour) + ":" + pad(minute);
        }
        return dateStr;
    }

    void add(String recurringPeriod, String recurringType) {
        if (recurringType == null || "".equals(recurringType.trim())) {
            return;
        }
        int rType = 0;
        int rValue = getIntValue(recurringPeriod);
        if (EventEditForm.RECURRING_MINUTE.equals(recurringType)) {
            rType = Calendar.MINUTE;
        } else if (EventEditForm.RECURRING_HOUR.equals(recurringType)) {
            rType = Calendar.HOUR_OF_DAY;
        } else if (EventEditForm.RECURRING_DAY.equals(recurringType)) {
            rType = Calendar.DAY_OF_MONTH;
        } else if (EventEditForm.RECURRING_WEEK.equals(recurringType)) {
            rType = Calendar.DAY_OF_MONTH;
            rValue *= 7;
        } else if (EventEditForm.RECURRING_MONTH.equals(recurringType)) {
            rType = Calendar.MONTH;
        } else if (EventEditForm.RECURRING_YEAR.equals(recurringType)) {
            rType = Calendar.YEAR;
        } else {
            return;
        }
        final Date newDate = addDate(rType, rValue);
        final Date now = new Date();
        if (newDate.getTime() < now.getTime()) {
            this.date = now;
        }
        this.date = addDate(rType, rValue);
    }

    private String pad(int hour) {
        String s = String.valueOf(hour);
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }
}
