/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.wbs.constant.Color;
import com.wbs.logging.Logger;
import com.wbs.utils.UiUtil;

/**
 *
 * @author Vikram S
 */
class RecipientsContainer extends Container implements ActionListener {

    public static final int KEY_DOWN = -2;
    public static final int KEY_UP = -1;
    private int selectedPos = 0;
    private boolean firstFocus = true;
    final Command removeContactCmd = new Command("Remove");

    void addRecipient(String displayableText) {
        final Label label = new Label(displayableText);
        label.getStyle().setFont(UiUtil.getFont(Font.STYLE_PLAIN, Font.SIZE_SMALL));
        label.getSelectedStyle().setFont(UiUtil.getFont(Font.STYLE_BOLD, Font.SIZE_SMALL));
        label.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        label.setFocusable(true);
        label.setPreferredW(Display.getInstance().getDisplayWidth());
        label.setNextFocusDown(this);
        label.setNextFocusUp(this);
        addComponent(label);
    }

    public void actionPerformed(ActionEvent evt) {
        final Form form = getComponentForm();
        final Component focused = form.getFocused();
        if (focused == this) {
            Logger.logInfo("Focus is IN recipients list. hasRecipients()=" + hasRecipients() + ",hasReachedTop()=" + hasReachedTop() + ",hasReachedBottom()=" + hasReachedBottom() + ",firstFocus=" + firstFocus + ",selectedPos=" + selectedPos);
            final int key = evt.getKeyEvent();
            switch (key) {
                case KEY_UP:
                    Logger.logInfo("Recipients List Key Pressed -1");
                    if (!hasRecipients() || (hasReachedTop() && !firstFocus)) {
                        delegateFocusToPreviousComponentInForm();
                        return;
                    }
                    if (firstFocus) {
                        getComponentForm().addCommand(removeContactCmd, getComponentForm().getCommandCount());
                        selectedPos = getComponentCount() - 1;
                        firstFocus = false;
                    } else if (!hasReachedTop()) {
                        selectedPos--;
                    }
                    Logger.logInfo("Recipients List i = " + selectedPos);
                    form.setFocused(getComponentAt(selectedPos));
                    getComponentAt(selectedPos).setFocus(true);
                    break;
                case KEY_DOWN:
                    Logger.logInfo("Recipients List Key Pressed -2");
                    if (!hasRecipients() || (hasReachedBottom() && !firstFocus)) {
                        delegateFocusToNextComponentInForm();
                        return;
                    }
                    if (firstFocus) {
                        getComponentForm().addCommand(removeContactCmd, getComponentForm().getCommandCount());
                        selectedPos = 0;
                        firstFocus = false;
                    } else if (!hasReachedBottom()) {
                        selectedPos++;
                    }
                    Logger.logInfo("Recipients List i = " + selectedPos);
                    form.setFocused(getComponentAt(selectedPos));
                    getComponentAt(selectedPos).setFocus(true);
                    break;
            }
        } else {
            Logger.logInfo("Focus is NOT IN recipients list");
        }
    }

    private void deletegateFocusToComponentInForm(final Component componentToBeFocussed) {
        componentToBeFocussed.setFocus(true);
        getComponentForm().setFocused(componentToBeFocussed);
        firstFocus = true;
        getComponentForm().removeCommand(removeContactCmd);
    }

    private boolean hasReachedTop() {
        return selectedPos == 0;
    }

    private boolean hasReachedBottom() {
        return (getComponentCount() - 1) == selectedPos;
    }

    private boolean hasRecipients() {
        return getComponentCount() > 0;
    }

    private void delegateFocusToPreviousComponentInForm() {
        final Component componentToBeFocussed = getNextFocusUp();
        deletegateFocusToComponentInForm(componentToBeFocussed);
    }

    private void delegateFocusToNextComponentInForm() {
        final Component componentToBeFocussed = getNextFocusDown();
        deletegateFocusToComponentInForm(componentToBeFocussed);
    }

    void clear() {
        removeAll();
        selectedPos = 0;
    }

    int removeSelected() {
        removeComponent(getComponentAt(selectedPos));
        final int removedPosition = selectedPos;
        if (!hasRecipients()) {
            selectedPos = 0;
        } else {
            if (selectedPos > getComponentCount() - 1) {
                selectedPos--;
            }
            Form form = getComponentForm();
            form.setFocused(getComponentAt(selectedPos));
            getComponentAt(selectedPos).setFocus(true);
        }
        return removedPosition;
    }
}
