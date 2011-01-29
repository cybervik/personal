/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.CheckBox;
import com.sun.lwuit.Command;
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
import com.wbs.utils.UiUtil;
import javax.microedition.lcdui.Form;

/**
 *
 * @author Vikram S
 */
public class ContactRow extends Container {

    private final int SELECTED_BACKGROUND_COLOR = 0xF5A9A9;
    private Command selectCommand;
    private final Font smallNormalFont;
    private final Font smallBoldFont;
    private final Label phoneNumbers;
    private final Label name;
    private PhoneEntry contact;
    private final CheckBox status;
    private Command unselectCommand;
    private final Container subContainer;
    private final Font smallItalicFont;
    private final Font smallItalicBoldFont;

    public ContactRow() {
        setLayout(new BoxLayout(BoxLayout.X_AXIS));
        subContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        smallNormalFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        smallBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        smallItalicFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_SMALL);
        smallItalicBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC | Font.STYLE_BOLD, Font.SIZE_SMALL);
        name = new Label();
        name.getStyle().setFont(smallBoldFont);
        name.getSelectedStyle().setFont(smallBoldFont);
        name.getSelectedStyle().setBgColor(SELECTED_BACKGROUND_COLOR);
        phoneNumbers = new Label();
        phoneNumbers.getStyle().setFont(smallNormalFont);
        phoneNumbers.getSelectedStyle().setFont(smallNormalFont);
        phoneNumbers.getSelectedStyle().setBgColor(SELECTED_BACKGROUND_COLOR);
        subContainer.addComponent(name);
        subContainer.addComponent(phoneNumbers);
        status = new CheckBox();
        status.setFocusable(false);
        addComponent(status);
        addComponent(subContainer);
        subContainer.getSelectedStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(SELECTED_BACKGROUND_COLOR);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
            }
        });
        getSelectedStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(SELECTED_BACKGROUND_COLOR);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
            }
        });
        selectCommand = new Command("Select");
        unselectCommand = new Command("Unselect");
    }

    ContactRow(PhoneEntry contact) {
        this();
        if (UiUtil.isEmpty(contact.getName())) {
            name.setText("No Name");
            name.getStyle().setFont(smallItalicFont);
            name.getSelectedStyle().setFont(smallItalicFont);
        } else {
            name.setText(contact.getName());
        }
        phoneNumbers.setText(contact.getTelePhone());
        this.contact = contact;
    }

    ContactRow(PhoneEntry contact, boolean selected) {
        this(contact);
        status.setSelected(selected);
    }

    void unhighlight() {
        setFocussedState(false);
    }

    private void setFocussedState(boolean state) {
        setFocus(state);
        subContainer.setFocus(state);
        phoneNumbers.setFocus(state);
        name.setFocus(state);
    }

    PhoneEntry highlight() {
        setFocussedState(true);
        toggleSelectCommand();
        return contact;
    }

    private void toggleSelectCommand() {
        if (status.isSelected()) {
            getComponentForm().removeCommand(selectCommand);
            getComponentForm().addCommand(unselectCommand, getComponentForm().getCommandCount());
        } else {
            getComponentForm().removeCommand(unselectCommand);
            getComponentForm().addCommand(selectCommand, getComponentForm().getCommandCount());
        }
    }

    boolean startsWith(String text) {
        return this.name.getText().startsWith(text) || this.phoneNumbers.getText().startsWith(text);
    }

    PhoneEntry getPhoneEntry() {
        return contact;
    }

    void select() {
        status.setSelected(true);
        toggleSelectCommand();
    }

    void unSelect() {
        status.setSelected(false);
        toggleSelectCommand();
    }

    boolean isSelected() {
        return status.isSelected();
    }
}
