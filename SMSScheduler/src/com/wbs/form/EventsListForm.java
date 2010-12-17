/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.sun.lwuit.painter.BackgroundPainter;

/**
 *
 * @author Vikram S
 */
public class EventsListForm implements ActionListener {

    public static final int ALIGNMENT_CENTER = 1;
    public static final int COLOR_SCHEDULES_HEADER_BACKGROUND = 0x58ACFA;
    public static final int COLOR_SCHEDULES_SELECTED_BACKGROUND = 0xF5A9A9;
    public static final String COMMAND_OPEN = "Open";
    public static final String APP_NAME = "SMS Scheduler";
    private Form form;
    private Container schedules;
    String[][] testData = {
        {"Event Name", "Reminder"},
        {"Nitin's Anniv", "Today 2:30 PM"},
        {"Pay bills", "10 Dec,2010 12:00 AM"},
        {"Pay LIC", "21 Jan,2010 12:00 AM"},
        {"Mediclaim Renew", "22 Dec,2010 12:00 AM"},
        {"Ramesh's Bday", "30 Apr,2010 12:00 AM"},
        {"Pay Vikram", "Tomorrow 1:00 AM"}
    };
    private int selectedPos = -1;
    private DetailsForm detailsForm;

    public EventsListForm() {
        initialize();
        addHeader();
        addSpacer();
        addSchedulesList();
        addMenuOptions();
        form.show();
    }

    private void initialize() {
        form = new Form();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
    }

    private void addSchedulesList() {
        schedules = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        addSchedulesRow(testData[0][0], testData[0][1], true);
        for (int i = 1; i < testData.length; i++) {
            String name = testData[i][0];
            String due = testData[i][1];
            addSchedulesRow(name, due, false);
        }
        selectSchedule(3);
        form.addComponent(schedules);
    }

    private void addSchedulesRow(String name, String due, boolean isHeader) {
        Container rowContainer = new Container(new GridLayout(1, 2));
        addSchedulesColumn(name, rowContainer, isHeader);
        addSchedulesColumn(due, rowContainer, isHeader);
        schedules.addComponent(rowContainer);
    }

    private void addSchedulesColumn(String name, Container rowContainer, boolean isHeader) {
        Label messageName = new Label(name);
        setSchedulesStyle(messageName, isHeader);
        rowContainer.addComponent(messageName);
    }

    private void setSchedulesStyle(Label label, boolean isHeader) {
        Font smallFont = getSmallNormalFont();
        label.getStyle().setFont(smallFont);
        if (isHeader) {
            label.getStyle().setBgColor(COLOR_SCHEDULES_HEADER_BACKGROUND);
        }
    }

    private Font getSmallBoldFont() {
        Font smallBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        return smallBoldFont;
    }

    private Font getMediumBoldFont() {
        Font mediumFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        return mediumFont;
    }

    private Font getSmallNormalFont() {
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        return smallFont;
    }

    private void addMenuOptions() {
        addCommand("Exit");
        addCommand("Help");
        addCommand("About");
        addCommand("Delete");
        addCommand("Add");
        addCommand(COMMAND_OPEN);
        form.addCommandListener(this);
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

        Label label = new Label(this.APP_NAME);
        Font mediumFont = getMediumBoldFont();
        label.getStyle().setFont(mediumFont);
        label.setAlignment(Component.LEFT);
        container.addComponent(label);
        form.addComponent(container);
    }

    private void addCommand(String aboutCommandName) {
        form.addCommand(new Command(aboutCommandName));
    }

    private void addSpacer() {
        form.addComponent(new Label(" "));
    }

    private void selectSchedule(int pos) {
        Container scheduelRow = (Container) schedules.getComponentAt(pos);
        scheduelRow.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle rect) {
                g.setColor(COLOR_SCHEDULES_SELECTED_BACKGROUND);
                g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), rect.getSize().getHeight());
            }
        });
        for (int i=0 ; i<scheduelRow.getComponentCount();i++) {
            Component scheduleCol = scheduelRow.getComponentAt(i);
            scheduleCol.getStyle().setBgColor(COLOR_SCHEDULES_SELECTED_BACKGROUND);
        }
        selectedPos = pos;
    }

    public void actionPerformed(ActionEvent evt) {
        Command command = evt.getCommand();
        if (command != null) {
            String commandName = command.getCommandName();
            if (COMMAND_OPEN.equals(commandName)) {
                initDetailsForm(testData[selectedPos]);
            }
        }
    }

    private void initDetailsForm(String[] data) {
        String name = data[0];
        String due = data[1];
        if (detailsForm == null) {
            detailsForm = new DetailsForm();
        }
        detailsForm.show(name, due);
    }
}
