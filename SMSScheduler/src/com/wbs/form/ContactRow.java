/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.list.ListCellRenderer;

/**
 *
 * @author Vikram S
 */
public class ContactRow extends Container {

    private final int SELECTED_BACKGROUND_COLOR = 0xF5A9A9;
    private final Font smallNormalFont;
    private final Font smallBoldFont;
    private final Label phoneNumbers;
    private final Label name;
    private PhoneEntry contact;

    public ContactRow() {
        setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        smallNormalFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        smallBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        name = new Label();
        name.getStyle().setFont(smallBoldFont);
        name.getSelectedStyle().setFont(smallBoldFont);
        name.getSelectedStyle().setBgColor(SELECTED_BACKGROUND_COLOR);
        phoneNumbers = new Label();
        phoneNumbers.getStyle().setFont(smallNormalFont);
        phoneNumbers.getSelectedStyle().setFont(smallNormalFont);
        phoneNumbers.getSelectedStyle().setBgColor(SELECTED_BACKGROUND_COLOR);
        addComponent(name);
        addComponent(phoneNumbers);
        getSelectedStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(SELECTED_BACKGROUND_COLOR);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
            }
        });
    }

    ContactRow(PhoneEntry contact) {
        this();
        name.setText(contact.getName());
        phoneNumbers.setText(contact.getTelePhone());
        this.contact = contact;
    }

    void unSelect() {
        setFocussedState(false);
    }

    private void setFocussedState(boolean state) {
        setFocus(state);
        phoneNumbers.setFocus(state);
        name.setFocus(state);
    }

    PhoneEntry select() {
        setFocussedState(true);
        return contact;
    }

    boolean startsWith(String text) {
        return this.name.getText().startsWith(text) || this.phoneNumbers.getText().startsWith(text);
    }

    PhoneEntry getPhoneEntry() {
        return contact;
    }
}
