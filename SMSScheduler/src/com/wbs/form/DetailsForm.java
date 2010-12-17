/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.Button;
import com.sun.lwuit.Calendar;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.layouts.GroupLayout;
import com.sun.lwuit.layouts.GroupLayout.SequentialGroup;
import com.sun.lwuit.layouts.Layout;

/**
 *
 * @author Vikram S
 */
class DetailsForm {

    private Form form = null;
    private TextField eventName;
    private TextArea eventMessage;
    String[] RECURRING_TYPES = {"Day", "Week", "Month", "Year"};
    private TextField recurringPeriod;
    String[] RECIPIENTS_TEST_DATA = {"Abhay Shenvi", "+919945678903"};

    public DetailsForm() {
        initialize();
        addHeader();
        addSpacer();

        eventName = addTextField("Event Name");
        eventMessage = addTextArea("Message");
        form.addComponent(new CheckBox("Self Event"));
        Button button = new Button("...");
        addLabelledComponent("Due", button, new Container(new BoxLayout(BoxLayout.X_AXIS)));
        List list = new List(RECIPIENTS_TEST_DATA);
        addLabelledComponent("To", list, form);
        addRecurring();
        addCommands();
        form.show();
    }

    private void addLabelledComponent(String labelName, Component component, Container container) {
        if (!"".equals(labelName)) {
            addLabel(labelName, container);
        }
        container.addComponent(component);
    }

    private void addRecurring() {
        form.addComponent(new Label("Repeat"));
        TextField count = new TextField();
        ComboBox type = new ComboBox(RECURRING_TYPES);
        form.addComponent(count);
        form.addComponent(type);
        form.addComponent(recurringContainer);
    }

    private void addCommands() {
        form.addCommand(new Command("Exit"));
        form.addCommand(new Command("Help"));
        form.addCommand(new Command("About"));
        form.addCommand(new Command("Back"));
        form.addCommand(new Command("Delete"));
        form.addCommand(new Command("Save"));
    }

    private void addLabel(String fieldName, Container container) {
        container.addComponent(new Label(fieldName));
    }

    private void addSpacer() {
        form.addComponent(new Label(" "));
    }

    private void initialize() {
        form = new Form();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
    }

    private void addHeader() {
        Container container = new Container(new BoxLayout((BoxLayout.X_AXIS)));

        Label logo = new Label("WBS");
        logo.getStyle().setBgColor(0xB40404);
        logo.getStyle().setFgColor(0xFFFFFF);
        Font bigBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
        logo.getStyle().setFont(bigBoldFont);
        logo.setAlignment(Component.RIGHT);
        container.addComponent(logo);

        Label label = new Label(EventsListForm.APP_NAME);
        Font mediumFont = getMediumBoldFont();
        label.getStyle().setFont(mediumFont);
        label.setAlignment(Component.LEFT);
        container.addComponent(label);
        form.addComponent(container);
    }

    private TextField addTextField(String fieldName) {
        return addTextField(fieldName, form);
    }

    private TextField addTextField(String fieldName, Container container) {
        TextField textField = new TextField();
        addLabelledComponent(fieldName, textField, container);
        return textField;
    }

    void show(String name, String due) {
    }

    private Font getSmallBoldFont() {
        Font smallBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        return smallBoldFont;
    }

    private Font getMediumBoldFont() {
        Font mediumFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        return mediumFont;
    }

    private TextArea addTextArea(String name) {
        TextArea textArea = new TextArea();
        addLabelledComponent(name, textArea, form);
        return textArea;
    }
}
