/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.Component;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import java.util.Date;

/**
 *
 * @author Vikram S
 */
public class DateLabel extends Label {

    private CustomDate date;

    public DateLabel(CustomDate date) {
        super(date.toString());
        this.date = date;
        Font font = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_SMALL);
        getStyle().setFont(font);
    }

    public DateLabel() {
        super("dd/mm/yyyy");
        this.date = new CustomDate();
        Font font = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_SMALL);
        getStyle().setFont(font);
    }

    public Date getDate() {
        return this.date.getDate();
    }

    void setDate(CustomDate date) {
        this.date = date;
        this.setText(this.date.toString());
    }
}
