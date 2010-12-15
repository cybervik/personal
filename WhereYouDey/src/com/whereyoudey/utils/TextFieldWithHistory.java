/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.utils;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.List;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.CoordinateLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.list.ListModel;
import com.whereyoudey.form.ResultForm;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class TextFieldWithHistory {

    private static final UIUtils uiUtils = new UIUtils();
    private TextField textField = null;
    private int displayWidth = 0;
    private Container textFieldContainer;
    private List history = null;
    private Container topContainer = null;

    public TextFieldWithHistory(Container topContainer1) {
        this.topContainer = topContainer1;
        createTextField();
//        createHistoryDropdown();
        addTextFieldListeners();
//        addHistoryListeners();
        topContainer.addComponent(textFieldContainer);
    }

    private void addHistoryListeners() {
        addFocusListner();
        addSelectionListner();
    }

    private void addSelectionListner() {
//        history.addActionListener(new ActionListener() {
//
//            public void actionPerformed(ActionEvent ae) {
//                Command command = ae.getCommand();
//                if (command != null && command.getCommandName().equals("Select")) {
//                    Object selectedItem = history.getSelectedItem();
//                    if (selectedItem != null) {
//                        String selectedItemText = (String) selectedItem;
//                        textField.setText(selectedItemText);
//                    }
//                }
//            }
//        });
//        history.addSelectionListener(new SelectionListener() {
//
//            public void selectionChanged(int oldSelected, int newSelected) {
//            }
//        });
    }

    private void addFocusListner() {
        history.addFocusListener(new FocusListener() {

            public void focusGained(Component cmpnt) {
            }

            public void focusLost(Component cmpnt) {
                Form form = topContainer.getComponentForm();
                history.setVisible(false);
                history.setFocusable(false);
                history.repaint();
                if (form != null) {
                    form.show();
                }
            }
        });
    }

    private void addTextFieldListeners() {
        textField.addFocusListener(new FocusListener() {

            public void focusGained(Component cmpnt) {
                history.setVisible(true);
                history.repaint();
                Form form = topContainer.getComponentForm();
                if (form != null) {
                    form.show();
                }
                history.setFocusable(true);
                cmpnt.setNextFocusDown(history);
            }

            public void focusLost(Component cmpnt) {
                Form form = topContainer.getComponentForm();
                if (form.getFocused() != history) {
                    history.setVisible(false);
                    history.repaint();
                    if (form != null) {
                        form.show();
                    }
                }
            }
        });
    }

    private void createHistoryDropdown() {
        history = new List();
        history.setX(0);
        history.setY(22);
        history.setPreferredH(40);
        history.setVisible(false);
        history.setPreferredW(displayWidth);
        final DefaultListCellRenderer renderer = (DefaultListCellRenderer) history.getRenderer();
        renderer.setShowNumbers(false);
        renderer.getSelectedStyle().setBgColor(ResultForm.COLOR_SELECTEDITEM_BACKGROUND);
        renderer.getSelectedStyle().setFgColor(ResultForm.COLOR_BLACK);
        Font smallFont = uiUtils.getFont(Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font smallBoldFont = uiUtils.getFont(Font.STYLE_BOLD, Font.SIZE_SMALL);
        renderer.getStyle().setFont(smallFont);
        renderer.getSelectedStyle().setFont(smallBoldFont);
        renderer.setSmoothScrolling(false);
        history.setItemGap(0);
        renderer.setGap(0);
        renderer.getStyle().setMargin(0, 0, 0, 0);
        renderer.getStyle().setPadding(0, 0, 0, 0);
        renderer.getSelectedStyle().setMargin(0, 0, 0, 0);
        renderer.getSelectedStyle().setPadding(0, 0, 0, 0);
        textFieldContainer.addComponent(history);
    }

    private void createTextField() {
        textField = new TextField();
        Font plainMediumFont = uiUtils.getFont(Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        textField.getStyle().setFont(plainMediumFont);
        textField.getSelectedStyle().setFont(plainMediumFont);
        displayWidth = Display.getInstance().getDisplayWidth();
        textField.setPreferredW(displayWidth);
        textField.setX(0);
        textField.setY(0);
        textFieldContainer = new Container(new CoordinateLayout(displayWidth, 40));
        textFieldContainer.addComponent(textField);
    }

    public void setText(String txt) {
        textField.setText(txt);
    }

    public String getText() {
        return textField.getText();
    }

    public TextField getInnerRepresentation() {
        return textField;
    }

    public void updateHistory() {
        String text = textField.getText().trim();
        ListModel model = history.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            if (model.getItemAt(i).equals(text)) {
                return;
            }
        }
        history.getModel().addItem(text);
    }
}
