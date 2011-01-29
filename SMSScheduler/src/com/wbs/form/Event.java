/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class Event {

    private String name;
    private String message;
    private CustomDate dueDate;
    private boolean selfEvent;
    private String recurringPeriod;
    private String recurringType;
    private Vector recipients;

    public Event() {
        name = "";
        message = "";
        dueDate = new CustomDate();
        selfEvent = true;
        recurringPeriod = EventEditForm.BLANK_VALUE;
        recurringType = EventEditForm.RECURRING_NONE;
        recipients = new Vector();
    }

    public Event(String name, String message, Date dueDate) {
        this();
        this.name = name;
        this.message = message;
        this.dueDate.setDate(dueDate);
    }

    public CustomDate getDueDate() {
        return dueDate;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    void setMessage(String eventMessage) {
        this.message = eventMessage;
    }

    void setSelfEvent(boolean selfEvent) {
        this.selfEvent = selfEvent;
    }

    void setDueDate(Date dueDate) {
        this.dueDate.setDate(dueDate);
    }

    public String getRecurringType() {
        return recurringType;
    }

    public void setRecurringType(String setRecurringType) {
        this.recurringType = setRecurringType;
    }

    public boolean isSelfEvent() {
        return selfEvent;
    }

    void setRecipients(Vector recipients) {
        this.recipients = recipients;
    }

    public Vector getRecipients() {
        return recipients;
    }

    public String getStorableFormat() {
        String format = "[" + this.name + "][" + this.message + "][" + this.dueDate.getDate().getTime() + "][" + this.selfEvent + "][" + this.recurringPeriod + "][" + this.recurringType + "]";
        String rp = "";
        for (int i = 0; i < recipients.size(); i++) {
            final PhoneEntry e = (PhoneEntry) recipients.elementAt(i);
            rp += "{" + e.getName() + "_" + e.getTelePhone() + "}";
        }
        return format + "[" + rp + "]";
    }

    public void setFromFormat(String dataStr) {
        final char[] cArr = dataStr.toCharArray();
        String ele = null;
        int pos = 0;
        for (int i = 0; i < cArr.length; i++) {
            final char c = cArr[i];
            switch (c) {
                case '[':
                    ele = "";
                    pos++;
                    break;
                case ']':
                    setValue(pos, ele);
                    break;
                default:
                    ele += c;
                    break;
            }
        }
    }

    private void setValue(int pos, String ele) {
        switch (pos) {
            case 1:
                setName(ele);
                break;
            case 2:
                setMessage(ele);
                break;
            case 3:
                setDueDate(new Date(Long.parseLong(ele)));
                break;
            case 4:
                setSelfEvent("true".equals(ele) ? true : false);
                break;
            case 5:
                setRecurringPeriod(ele);
                break;
            case 6:
                setRecurringType(ele);
                break;
            case 7:
                recipientsFromFormat(ele);
                break;
        }
    }

    public boolean isRecurring() {
        return !this.recurringType.equals(EventEditForm.RECURRING_NONE);
    }

    public void updateDueDate() {
        dueDate.add(recurringPeriod, recurringType);
    }

    public boolean isDue() {
        final Date now = new Date();
        return (this.dueDate.getDate().getTime() <= now.getTime());
    }

    private void recipientsFromFormat(String dataStr) {
        final char[] cArr = dataStr.toCharArray();
        String ele = null;
        int pos = 0;
        for (int i = 0; i < cArr.length; i++) {
            final char c = cArr[i];
            switch (c) {
                case '{':
                    ele = "";
                    pos++;
                    break;
                case '}':
                    int len = ele.length();
                    int p = ele.indexOf("_");
                    if (p != -1 && p != len - 1) {
                        String name = ele.substring(0, p);
                        String tel = ele.substring(p + 1, len);
                        this.recipients.addElement(new PhoneEntry(name, tel));
                    }
                    break;
                default:
                    ele += c;
                    break;
            }
        }
    }

    void setRecurringPeriod(String recurringPeriod) {
        this.recurringPeriod = recurringPeriod;
    }

    public String getRecurringPeriod() {
        return recurringPeriod;
    }
}
