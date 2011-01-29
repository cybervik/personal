/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.wbs.logging.Logger;
import com.wbs.utils.DialogUtil;
import com.wbs.utils.SortUtil;
import com.wbs.utils.UiUtil;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class ContactsList extends Container implements ActionListener, DataChangedListener {

    private static final int INITIAL_SELECTED_COMPONENT_INDEX = -1;
    private int selectedContactIndex;
    private final TextField phoneNumberField;
    private Command addNewCommand = new Command("Add");

    public ContactsList(TextField phoneNumberField, Vector alreadySelectedRecipients, Vector contacts) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        setScrollableY(true);
        selectedContactIndex = INITIAL_SELECTED_COMPONENT_INDEX;
        this.phoneNumberField = phoneNumberField;
        for (int i = 0; i < alreadySelectedRecipients.size(); i++) {
            PhoneEntry recipient = (PhoneEntry) alreadySelectedRecipients.elementAt(i);
            final ContactRow contactRow = new ContactRow(recipient, true);
            addComponent(contactRow);
        }
        for (int i = 0; i < contacts.size(); i++) {
            PhoneEntry contact = (PhoneEntry) contacts.elementAt(i);
            if (!alreadySelectedRecipients.contains(contact)) {
                addComponent(new ContactRow(contact));
            }
        }
    }

    public void scrollComponentToVisible(Component c) {
        Component toBeVisible = c;
        if (c instanceof Container) {
            final Container subC = (Container) c;
            toBeVisible = subC.getComponentAt(subC.getComponentCount() - 1);
        }
        super.scrollComponentToVisible(toBeVisible);
        repaint();
    }

    public void actionPerformed(ActionEvent evt) {
        final int keyEvent = evt.getKeyEvent();
        switch (keyEvent) {
            case -1:
                if (selectedContactIndex <= 0) {
                    break;
                }
                unHighlightContact();
                selectedContactIndex--;
                highlightContact();
                break;
            case -2:
                if (selectedContactIndex == (getComponentCount() - 1)) {
                    break;
                }
                unHighlightContact();
                selectedContactIndex++;
                highlightContact();
                break;
        }
    }

    public Vector getSelectedPhoneEntries() {
        Vector pel = new Vector();
        for (int i = 0; i < getComponentCount(); i++) {
            final Component c = getComponentAt(i);
            if (c instanceof ContactRow) {
                final ContactRow cr = (ContactRow) c;
                if (cr.isSelected()) {
                    pel.addElement(cr.getPhoneEntry());
                }
            }
        }
//        Component selectedComponent = null;
//        try {
//            selectedComponent = getComponentAt(selectedComponentIndex);
//        } catch (Exception e) {
//        }
//        if (selectedComponent != null) {
//            ContactRow row = (ContactRow) selectedComponent;
//            return row.getPhoneEntry();
//        } else {
//            try {
//                final String phoneNumber = associatedField.getText();
//                return new PhoneEntry("", phoneNumber);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
        SortUtil.sort(pel, SortUtil.SORT_ORDER_ASCENDING);
        return pel;
    }

    public ContactRow getSelectedRow() {
        Component selectedComponent = null;
        try {
            selectedComponent = getComponentAt(selectedContactIndex);
        } catch (Exception e) {
        }
        if (selectedComponent != null) {
            ContactRow row = (ContactRow) selectedComponent;
            return row;
        }
        return null;
    }

    void unHighlightContact() {
        Logger.logInfo("Unhighlighting position " + selectedContactIndex);
        if (selectedContactIndex == -1) {
            return;
        }
        final Component currentSelectedComponent = getComponentAt(selectedContactIndex);
        if (currentSelectedComponent != null) {
            ContactRow row = (ContactRow) currentSelectedComponent;
            row.unhighlight();
            repaint();
        }
    }

    private void highlightContact() {
        final Component newSelectedComponent = getComponentAt(selectedContactIndex);
        if (newSelectedComponent != null) {
            ContactRow row = (ContactRow) newSelectedComponent;
            row.highlight();
            scrollComponentToVisible(newSelectedComponent);
            repaint();
        }
//        setPhoneFieldIntoFocusBack();
    }

    private void setPhoneFieldIntoFocusBack() {
        phoneNumberField.setCursorPosition(phoneNumberField.getText().length());
        phoneNumberField.setInputMode("Abc");
        phoneNumberField.setFocus(true);
    }

    void selectStartingWith(String text) {
        boolean found = false;
        final Form form = getComponentForm();
        if (UiUtil.isEmpty(text)) {
            if (!Display.getInstance().isEdt()) {
                Logger.logInfo("Is this an EDT thread 7 - " + Display.getInstance().isEdt());
                Display.getInstance().callSerially(new Runnable() {

                    public void run() {
                        Logger.logInfo("Is this an EDT thread 8 - " + Display.getInstance().isEdt());
                        unHighlightContact();
                        selectedContactIndex = INITIAL_SELECTED_COMPONENT_INDEX;
//                        setPhoneFieldIntoFocusBack();
//                        form.repaint();
//                        form.show();
                        repaint();
                    }
                });
            } else {
                Logger.logInfo("Is this an EDT thread 9 - " + Display.getInstance().isEdt());
                unHighlightContact();
                selectedContactIndex = INITIAL_SELECTED_COMPONENT_INDEX;
//                setPhoneFieldIntoFocusBack();
//                form.repaint();
//                form.show();
                repaint();
            }
            return;
        }
        for (int i = 0; i < getComponentCount(); i++) {
            final Component c = getComponentAt(i);
            if (c != null) {
                final ContactRow r = (ContactRow) c;
                if (r.startsWith(text)) {
                    selectedContactIndex = i;
                    if (!Display.getInstance().isEdt()) {
                        Logger.logInfo("Is this an EDT thread 1 - " + Display.getInstance().isEdt());
                        Display.getInstance().callSerially(new Runnable() {

                            public void run() {
                                Logger.logInfo("Is this an EDT thread 2 - " + Display.getInstance().isEdt());
                                unHighlightContact();
                                r.highlight();
                                scrollComponentToVisible(c);
//                                form.removeCommand(addNewCommand);
//                                setPhoneFieldIntoFocusBack();
//                                form.repaint();
//                                form.show();
                                repaint();
                            }
                        });
                    } else {
                        Logger.logInfo("Is this an EDT thread 3 - " + Display.getInstance().isEdt());
                        unHighlightContact();
                        r.highlight();
                        scrollComponentToVisible(c);
//                        form.removeCommand(addNewCommand);
//                        setPhoneFieldIntoFocusBack();
//                        form.repaint();
//                        form.show();
                        repaint();
                    }
                    found = true;
                    break;
                }
            }
        }
        if (!found) {
            if (!Display.getInstance().isEdt()) {
                Logger.logInfo("Is this an EDT thread 4 - " + Display.getInstance().isEdt());
                Display.getInstance().callSerially(new Runnable() {

                    public void run() {
                        Logger.logInfo("Is this an EDT thread 5 - " + Display.getInstance().isEdt());
                        unHighlightContact();
                        selectedContactIndex = INITIAL_SELECTED_COMPONENT_INDEX;
//                        form.addCommand(addNewCommand, form.getCommandCount());
//                        setPhoneFieldIntoFocusBack();
//                        form.repaint();
//                        form.show();
                        repaint();
                    }
                });
            } else {
                Logger.logInfo("Is this an EDT thread 6 - " + Display.getInstance().isEdt());
                unHighlightContact();
                selectedContactIndex = INITIAL_SELECTED_COMPONENT_INDEX;
//                form.addCommand(addNewCommand, form.getCommandCount());
//                setPhoneFieldIntoFocusBack();
                repaint();
//                form.show();
            }
        }
    }

    public void dataChanged(int type, int index) {
        final String searchText = phoneNumberField.getText();
        selectStartingWith(searchText);
    }

    void addSearchTextAsContact() {
        final String phoneNumber = phoneNumberField.getText();
        String temp = phoneNumber;
        if (phoneNumber.startsWith("+")) {
            if (phoneNumber.length() == 1) {
                DialogUtil.showInfo("Error", "Not a valid phone number");
                return;
            }
            temp = phoneNumber.substring(1);
        }
        if (!UiUtil.isInteger(temp)) {
            DialogUtil.showInfo("Error", "Not a valid phone number");
            return;
        }
        PhoneEntry recipient = new PhoneEntry("", phoneNumber);
        final ContactRow contactRow = new ContactRow(recipient, true);
        addComponent(0, contactRow);
        selectedContactIndex = 0;
        highlightContact();
        scrollComponentToVisible(contactRow);
        phoneNumberField.setText("");
//        setPhoneFieldIntoFocusBack();
//        getComponentForm().repaint();
//        getComponentForm().show();
        repaint();
    }
}
