/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
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
import com.wbs.SMSScheduler;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class EventsListForm implements ActionListener {

    public static final int ALIGNMENT_CENTER = 1;
    public static final int COLOR_SCHEDULES_HEADER_BACKGROUND = 0x58ACFA;
    public static final int COLOR_SCHEDULES_SELECTED_BACKGROUND = 0xF5A9A9;
    public static final String COMMAND_ADD = "Add";
    public static final String COMMAND_DELETE = "Delete";
    public static final String COMMAND_EXIT = "Exit";
    public static final String COMMAND_HELP = "Help";
    public static final String COMMAND_OPEN = "Open";
    public static final String APP_NAME = "SMS Scheduler";
    public static final String COMMAND_ABOUT = "About";
    private Form form;
    private Container eventsList;
//    String[][] schedulesData = {
//        {"Event Name", "Reminder"},
//        {"Nitin's Anniv", "Today 2:30 PM"},
//        {"Pay bills", "10 Dec,2010 12:00 AM"},
//        {"Pay LIC", "21 Jan,2010 12:00 AM"},
//        {"Mediclaim Renew", "22 Dec,2010 12:00 AM"},
//        {"Ramesh's Bday", "30 Apr,2010 12:00 AM"},
//        {"Pay Vikram", "Tomorrow 1:00 AM"}
//    };
    Vector eventsData;
    private int selectedPos;
    private EventEditForm eventEditForm;
    private SMSScheduler smsScheduler;
    private int COLOR_WHITE = 0xFFFFFF;

    public EventsListForm(SMSScheduler smsScheduler) {
        this.smsScheduler = smsScheduler;
        initialize();
        addHeader();
        addSpacer();
        addEventsList();
        addMenuOptions();
        initEventForm();
        form.show();
        selectSchedule();
    }

    protected void addTestData() {
        addTestEvent("Ramesh's Bday", "Happy birthday Ramesh", new Date());
        addTestEvent("Nitin's Anniv", "Happy anniversary Nitin", new Date());
        addTestEvent("Pay Electricy", "Pay or go black", new Date());
        addTestEvent("Refund XYZ", "Please refund the poor soul", new Date());
        addTestEvent("Meet Vikram", "For supporting him on timely completion of project", new Date());
        addTestEvent("Call Harish", "To wish him on his new baby", new Date());
        addTestEvent("Health Checkup", "Health is Wealth", new Date());
        addTestEvent("Invite for Wedding", "Please confirm your presence for my cousin's wedding on 10, Feb 2011", new Date());
        addTestEvent("Decline new offer", "From new vendor", new Date());
    }

    protected void addTestEvent(final String name, final String message, final Date due) {
        Event schedule = new Event(name, message, due);
        eventsData.addElement(schedule);
    }

    public void deleteEvent() {
        if (showConfirm("Delete Cofirm", "Are you sure you want to delete?")) {
            eventsData.removeElementAt(selectedPos);
            final Component selectedRow = eventsList.getComponentAt(selectedPos);
            eventsList.removeComponent(selectedRow);
            if (selectedPos == eventsData.size()) {
                selectedPos--;
                selectUp();
            } else {
                selectSchedule();
            }
        }
    }

    protected void loadEvents() {
        addSchedulesRow("Name", "Due", true);
        for (int i = 1; i < eventsData.size(); i++) {
            final Event event = (Event) eventsData.elementAt(i);
            String name = event.getName();
            String due = event.getDueDate().toString();
            addSchedulesRow(name, due, false);
        }
    }

    protected void reloadEvents() {
        eventsList.removeAll();
        loadEvents();
        selectSchedule();
    }

    protected void showInfo(final String title, final String message) {
        Dialog.show(title, message, "Back", null);
    }

    protected boolean showConfirm(final String title, final String message) {
        return Dialog.show(title, message, "Ok", "Cancel");
    }

    private void initialize() {
        form = new Form();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.addKeyListener(-1, this);
        form.addKeyListener(-2, this);
//        form.setScrollable(true);
//        form.setScrollableX(true);
        form.setScrollableY(true);
        eventsData = new Vector();
        addTestData();
        selectedPos = (eventsData.size() > 0) ? 1 : 0;
    }

    private void addEventsList() {
        eventsList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        loadEvents();
        form.addComponent(eventsList);
    }

    private void addSchedulesRow(String name, String due, boolean isHeader) {
        Container rowContainer = new Container(new GridLayout(1, 2));
        addSchedulesColumn(name, rowContainer, isHeader);
        addSchedulesColumn(due, rowContainer, isHeader);
        eventsList.addComponent(rowContainer);
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
        addCommand(COMMAND_EXIT);
        addCommand(COMMAND_HELP);
        addCommand(COMMAND_ABOUT);
        addCommand(COMMAND_DELETE);
        addCommand(COMMAND_ADD);
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

    private void selectSchedule() {
        if (selectedPos <= 0 || selectedPos >= eventsData.size()) {
            return;
        }
        Container scheduleRow = (Container) eventsList.getComponentAt(selectedPos);
        scheduleRow.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle rect) {
                g.setColor(COLOR_SCHEDULES_SELECTED_BACKGROUND);
                g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), rect.getSize().getHeight());
            }
        });
        for (int i = 0; i < scheduleRow.getComponentCount(); i++) {
            Component scheduleCol = scheduleRow.getComponentAt(i);
            scheduleCol.getStyle().setBgColor(COLOR_SCHEDULES_SELECTED_BACKGROUND);
        }
        scheduleRow.repaint();
        eventsList.repaint();
        form.show();
    }

    public void actionPerformed(ActionEvent evt) {
        Command command = evt.getCommand();
        if (command != null) {
            String commandName = command.getCommandName();
            if (COMMAND_OPEN.equals(commandName)) {
                editSchedule();
            } else if (COMMAND_EXIT.equals(commandName)) {
                if (showConfirm("Exit Confirm", "Are you sure you want to exit?")) {
                    smsScheduler.notifyDestroyed();
                }
            } else if (COMMAND_ABOUT.equals(commandName)) {
                showInfo("About", "SMS Schedule by WBS");
            } else if (COMMAND_HELP.equals(commandName)) {
                showInfo("Help", "Help is comming soon.");
            } else if (COMMAND_ADD.equals(commandName)) {
                eventEditForm.show();
            } else if (COMMAND_DELETE.equals(commandName)) {
                deleteEvent();
            }
        } else {
            final int keyEvent = evt.getKeyEvent();
            switch (keyEvent) {
                case -1:
                    selectUp();
                    break;
                case -2:
                    selectDown();
                    break;
            }
        }
    }

    private void editSchedule() {
        final Event event = (Event) eventsData.elementAt(selectedPos);
        eventEditForm.show(event);
    }

    void show() {
        show(false);
    }

    private void selectUp() {
        if (selectedPos > 1) {
            unSelectSchedule();
            selectedPos--;
            selectSchedule();
        }
    }

    private void selectDown() {
        if (selectedPos < (eventsData.size() - 1)) {
            unSelectSchedule();
            selectedPos++;
            selectSchedule();
        }
    }

    private void unSelectSchedule() {
        if (selectedPos <= 0) {
            return;
        }
        Container scheduleRow = (Container) eventsList.getComponentAt(selectedPos);
        scheduleRow.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle rect) {
                g.setColor(COLOR_WHITE);
                g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), rect.getSize().getHeight());
            }
        });
        for (int i = 0; i < scheduleRow.getComponentCount(); i++) {
            Component scheduleCol = scheduleRow.getComponentAt(i);
            scheduleCol.getStyle().setBgColor(COLOR_WHITE);
        }
        scheduleRow.repaint();
        eventsList.repaint();
        form.show();
    }

    private void initEventForm() {
        if (eventEditForm == null) {
            eventEditForm = new EventEditForm(smsScheduler);
        }
    }

    void show(boolean reloadEvents) {
        if (reloadEvents) {
            reloadEvents();
        }
        form.show();
    }

    void addEvent(Event event) {
        this.eventsData.addElement(event);
    }

    void saveEvent(Event event) {
        eventsData.setElementAt(event, selectedPos);
    }
}
