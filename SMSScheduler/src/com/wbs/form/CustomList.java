/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.layouts.BoxLayout;
import javax.microedition.lcdui.Canvas;

/**
 *
 * @author Vikram S
 */
public class CustomList extends Container implements ActionListener {

    private int selectedComponentIndex;
    private final TextField associatedField;
    private boolean selectedItem = false;
    private PhoneEntry selectedPhoneEntry;

    public CustomList(TextField associatedField) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        setScrollableY(true);
        selectedComponentIndex = -1;
        this.associatedField = associatedField;
    }

    public void scrollComponentToVisible(Component c) {
        super.scrollComponentToVisible(c);
        repaint();
    }

    public void actionPerformed(ActionEvent evt) {
        final int keyEvent = evt.getKeyEvent();
        switch (keyEvent) {
            case -1:
                if (selectedComponentIndex <= 0) {
                    break;
                }
                unSelectComponent();
                selectedComponentIndex--;
                selectComponent();
                break;
            case -2:
                if (selectedComponentIndex == (getComponentCount() - 1)) {
                    break;
                }
                unSelectComponent();
                selectedComponentIndex++;
                selectComponent();
                break;
        }
    }

    public PhoneEntry getSelectedPhoneEntry() {
        Component selectedComponent = null;
        try {
            selectedComponent = getComponentAt(selectedComponentIndex);
        } catch (Exception e) {
        }
        if (selectedComponent != null) {
            ContactRow row = (ContactRow) selectedComponent;
            return row.getPhoneEntry();
        } else {
            try {
                final String phoneNumber = associatedField.getText();
                return new PhoneEntry("", phoneNumber);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    void unSelectComponent() {
        if (selectedComponentIndex == -1) {
            return;
        }
        final Component currentSelectedComponent = getComponentAt(selectedComponentIndex);
        if (currentSelectedComponent != null) {
            ContactRow row = (ContactRow) currentSelectedComponent;
            row.unSelect();
        }
    }

    private void selectComponent() {
        final Component newSelectedComponent = getComponentAt(selectedComponentIndex);
        if (newSelectedComponent != null) {
            ContactRow row = (ContactRow) newSelectedComponent;
            row.select();
            scrollComponentToVisible(newSelectedComponent);
        }
    }

    PhoneEntry selectStartingWith(String text) {
        PhoneEntry p = null;
        for (int i = 0; i < getComponentCount(); i++) {
            final Component c = getComponentAt(i);
            if (c != null) {
                ContactRow r = (ContactRow) c;
                if (r.startsWith(text)) {
                    unSelectComponent();
                    selectedComponentIndex = i;
                    p = r.select();
                    scrollComponentToVisible(c);
                    break;
                }
            }
        }
        return p;
    }
}
