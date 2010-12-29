/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

/**
 *
 * @author Vikram S
 */
/***
 *
 * Copyright (C) 2008 Alessandro La Rosa
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 * Contact: alessandro.larosa@gmail.com
 *
 * Author: Alessandro La Rosa
 */
import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import java.util.Calendar;
import java.util.Date;

public class CalendarWidget extends Component {

    static final String[] MONTH_LABELS = new String[]{
        "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
    };
    static final String[] WEEKDAY_LABELS = new String[]{
        "M", "T", "W", "T", "F", "S", "S"
    };
    /* starting week day: 0 for monday, 6 for sunday */
    public int startWeekday = 0;
    /* elements padding */
    public int padding = 1;
    /* cells border properties */
    public int borderWidth = 4;
    public int borderColor = 0x0000ff;
    /* weekday labels properties */
    public Font weekdayFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
    public int weekdayBgColor = 0x0000ff;
    public int weekdayColor = 0xffffff;
    /* header (month-year label) properties */
    public Font headerFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    public int headerBgColor = 0x0000ff;
    public int headerColor = 0xffffff;
    /* cells properties */
    public Font font = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
    public int foreColor = 0x000000;
    public int bgColor = 0x9999ff;
    public int selectedBgColor = 0xffff00;
    public int selectedForeColor = 0xff0000;
    /* internal properties */
    int width = 0;
    int height = 0;
    int headerHeight = 0;
    int weekHeight = 0;
    int cellWidth = 0;
    int cellHeight = 0;
    /* internal time properties */
    long currentTimestamp = 0;
    Calendar calendar = null;
    int weeks = 0;

    public CalendarWidget(Date date) {
        super();
        calendar = Calendar.getInstance();

        //we'll see these 2 methods later
        setDate(date);

        initialize();
    }

    public Date getSelectedDate() {
        return calendar.getTime();
    }

    public void setDate(Date d) {
        currentTimestamp = d.getTime();

        calendar.setTime(d);

        //weeks number can change, depending on week starting day and month total days
        this.weeks = (int) Math.ceil(((double) getStartWeekday() + getMonthDays()) / 7);
    }

    public void setDate(long timestamp) {
        setDate(new Date(timestamp));
    }

    void initialize() {
        //let's initialize calendar size
        this.cellWidth = font.stringWidth("MM") + 2 * padding;
        this.cellHeight = font.getHeight() + 2 * padding;

        this.headerHeight = headerFont.getHeight() + 2 * padding;
        this.weekHeight = weekdayFont.getHeight() + 2 * padding;

        this.width = 7 * (cellWidth + borderWidth) + borderWidth;
        setWidth(width);
        setPreferredW(width);
        initHeight();
    }

    void initHeight() {
        this.height =
        headerHeight + weekHeight
        + this.weeks * (cellHeight + borderWidth) + borderWidth;
        setHeight(height);
        setPreferredH(height);
    }

    int getMonthDays() {
        int month = calendar.get(Calendar.MONTH);

        switch (month) {
            case 3:
            case 5:
            case 8:
            case 10:
                return 30;
            case 1:
                return calendar.get(Calendar.YEAR) % 4 == 0 && calendar.get(Calendar.YEAR) % 100 != 0 ? 29 : 28;
            default:
                return 31;
        }
    }

    int getStartWeekday() {
        //let's create a new calendar with same month and year, but with day 1
        Calendar c = Calendar.getInstance();

        c.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        c.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        c.set(Calendar.DAY_OF_MONTH, 1);

        //we must normalize DAY_OF_WEEK returned value
        return (c.get(Calendar.DAY_OF_WEEK) + 5) % 7;
    }

    public void keyPressed(int key) {
        switch (key) {
            case -1:
                go(-7);
                break;
            case -2:
                go(7);
                break;
            case -4:
                go(1);
                break;
            case -3:
                go(-1);
                break;
        }
    }

    void go(int delta) {
        int prevMonth = calendar.get(Calendar.MONTH);

        setDate(currentTimestamp + 86400000 * delta);

        //we have to check if month has changed
        //if yes, we have to recalculate month height
        //since weeks number could be changed
        if (calendar.get(Calendar.MONTH) != prevMonth) {
            initHeight();
        }
    }

    public void paint(Graphics g) {
        //painting background
        g.setColor(bgColor);
        g.fillRect(0, 0, width, height);

        //painting header (month-year label)
        g.setFont(headerFont);
        g.setColor(headerColor);
        final int calMonthPos = calendar.get(Calendar.MONTH);
        final String header = MONTH_LABELS[calMonthPos] + " " + calendar.get(Calendar.YEAR);
        g.drawString(header, (width / 2) - (headerFont.stringWidth(header) / 2), padding);

        //painting week days labels
        g.translate(0, headerHeight);

        g.setColor(weekdayBgColor);
        g.fillRect(0, 0, width, weekHeight);

        g.setColor(weekdayColor);
        g.setFont(weekdayFont);

        for (int i = 0; i < 7; i++) {
            final String weekLabel = WEEKDAY_LABELS[(i + startWeekday) % 7];
            g.drawString(weekLabel,
                         (borderWidth + i * (cellWidth + borderWidth) + cellWidth / 2) - (weekdayFont.stringWidth(weekLabel) / 2),
                         padding);
        }

        //painting cells borders
        g.translate(0, weekHeight);

        g.setColor(borderColor);

        for (int i = 0; i <= weeks; i++) {
            g.fillRect(0, i * (cellHeight + borderWidth), width, borderWidth);
        }
        for (int i = 0; i <= 7; i++) {
            g.fillRect(i * (cellWidth + borderWidth), 0, borderWidth, height - headerHeight - weekHeight);
        }

        //painting days
        int days = getMonthDays();
        int dayIndex = (getStartWeekday() - this.startWeekday + 7) % 7;

        g.setColor(foreColor);

        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        g.setFont(font);
        for (int i = 0; i < days; i++) {
            int weekday = (dayIndex + i) % 7;
            int row = (dayIndex + i) / 7;

            int x = borderWidth + weekday * (cellWidth + borderWidth) + cellWidth / 2;
            int y = borderWidth + row * (cellHeight + borderWidth) + padding;

            //if this is the current day, we'll use selected bg and fore colors
            if (i + 1 == currentDay) {
                g.setColor(selectedBgColor);
                g.fillRect(
                        borderWidth + weekday * (cellWidth + borderWidth),
                        borderWidth + row * (cellHeight + borderWidth),
                        cellWidth, cellHeight);
                g.setColor(selectedForeColor);
            }
            final String dateCell = "" + (i + 1);

            g.drawString(dateCell, x - (font.stringWidth(dateCell) / 2), y);

            //if this is the current day, we must restore standard fore color
            if (i + 1 == currentDay) {
                g.setColor(foreColor);
            }
        }
        //let's traslate back!
        g.translate(0, -headerHeight - weekHeight);
    }
}
