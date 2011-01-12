/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.wbs.utils.DialogUtil;
import com.wbs.form.decorator.FormCommonDecorator;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BoxLayout;
import com.wbs.SMSScheduler;
import com.wbs.constant.Color;
import com.wbs.form.decorator.FontUtil;
import com.wbs.service.PersistenceService;
import java.util.Date;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

/**
 *
 * @author Vikram S
 */
public class EventsListForm implements ActionListener {

    public static final int ALIGNMENT_CENTER = 1;
    public static final String APP_NAME = "SMS Scheduler";
    public static final String COMMAND_ADD = "Add";
    public static final String COMMAND_DELETE = "Delete";
    public static final String COMMAND_EXIT = "Exit";
    public static final String COMMAND_HELP = "Help";
    public static final String COMMAND_OPEN = "Open";
    public static final String COMMAND_ABOUT = "About";
    private Form form;
    private Container eventsList;
    private Vector eventsData;
    private int selectedPos;
    private EventEditForm eventEditForm;
    private SMSScheduler smsScheduler;

    public EventsListForm(SMSScheduler smsScheduler) {
        this.smsScheduler = smsScheduler;
        initialize();
        addHeader();
        addSpacer();
        Event e = new Event("Test", "Tester", new Date());
        eventsData.addElement(e);
        e = new Event("India", "Where is it", new Date());
        eventsData.addElement(e);
        e = new Event("World", "Bengaluru is best", new Date());
        eventsData.addElement(e);
        e = new Event("Heloo", "Dude are u mad?", new Date());
        eventsData.addElement(e);
        addEventsList();
        addMenuOptions();
        initEventForm();
        form.show();
        selectEvent();
    }

    public void deleteEvent() {
        if (selectedPos <= 0) {
            DialogUtil.showInfo("Error", "Please select an event to delete");
            return;
        }
        if (DialogUtil.showConfirm("Delete Cofirm", "Are you sure you want to delete?")) {
            deleteEventAt(selectedPos);
        }
    }

    private void deleteEventAt(int p) {
        eventsData.removeElementAt(p - 1);
        final Component selectedRow = eventsList.getComponentAt(p);
        eventsList.removeComponent(selectedRow);
        if (p == eventsData.size()) {
            p--;
            selectUp();
        } else {
            selectEvent();
        }
    }

    private void loadEvents() {
        addEventRow("Scheduled Events (" + eventsData.size() + ")", "", Color.WHITE);
        int bgColor;
        for (int i = 0; i < eventsData.size(); i++) {
            final Event event = (Event) eventsData.elementAt(i);
            String name = event.getName();
            String due = event.getDueDate().getFullTime();
            bgColor = getBgColor(i);
            addEventRow(name, due, bgColor);
        }
    }

    private int getBgColor(int i) {
        int bgColor;
        if (i % 2 == 0) {
            bgColor = Color.EVENTLIST_ALTERNATE_BACKGROUND;
        } else {
            bgColor = Color.WHITE;
        }
        return bgColor;
    }

    private void reloadEvents() {
        eventsList.removeAll();
        loadEvents();
        selectEvent();
    }

    private void initialize() {
        form = new Form();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.addKeyListener(-1, this);
        form.addKeyListener(-2, this);
        form.setScrollableX(true);
        form.setScrollableY(true);
        form = FormCommonDecorator.decorate(form);
        this.eventsData = PersistenceService.readEvents();
        selectedPos = (eventsData.size() > 0) ? 1 : 0;
    }

    private void addEventsList() {
        eventsList = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        loadEvents();
        form.addComponent(eventsList);
    }

    private void addEventRow(String name, String due, final int backgroundColor) {
        Container rowContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        rowContainer.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle rect) {
                g.setColor(backgroundColor);
                g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), rect.getSize().getHeight());
            }
        });
        addEventRowItem(name, rowContainer, true, backgroundColor);
        addEventRowItem(due, rowContainer, false, backgroundColor);
        eventsList.addComponent(rowContainer);
    }

    private void addEventRowItem(String name, Container rowContainer, boolean highlightItem, final int backgroundColor) {
        Label messageName = new Label(name);
        setStyle(messageName, highlightItem, backgroundColor);
        rowContainer.addComponent(messageName);
    }

    private void log(final String s) {
        System.out.println("SMS Scheduler " + new CustomDate().getFullTime() + ":" + s);
    }

    public void reloadEventsIfNecessary() {
        if (this.form.isVisible()) {
            reloadEvents();
        }
    }

    private void setStyle(Label label, boolean highlightItem, final int backgroundColor) {
        label.getStyle().setBgColor(backgroundColor);
        if (highlightItem) {
            label.getStyle().setFont(FontUtil.getMediumBoldFont());
        } else {
            label.getStyle().setFont(FontUtil.getSmallItalicFont());
        }
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
        Font bigNormalFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
        logo.getStyle().setFont(bigNormalFont);
        logo.setAlignment(Component.RIGHT);
        container.addComponent(logo);

        Label label = new Label(this.APP_NAME);
        Font mediumFont = FontUtil.getLargeBoldFont();
        label.getStyle().setFont(mediumFont);
        label.setAlignment(Component.LEFT);
        container.addComponent(label);
        form.addComponent(container);
    }

    private void addCommand(String aboutCommandName) {
        form.addCommand(new Command(aboutCommandName));
    }

    private void addSpacer() {
        final Label label = new Label(" ");
        label.getStyle().setFont(FontUtil.getSmallNormalFont());
        label.setPreferredH(20);
        form.addComponent(label);
    }

    private void selectEvent() {
        if (selectedPos <= 0 || selectedPos > eventsData.size()) {
            return;
        }
        Container scheduleRow = (Container) eventsList.getComponentAt(selectedPos);
        scheduleRow.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle rect) {
                g.setColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
                g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), rect.getSize().getHeight());
            }
        });
        for (int i = 0; i < scheduleRow.getComponentCount(); i++) {
            Component scheduleCol = scheduleRow.getComponentAt(i);
            scheduleCol.getStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
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
                if (DialogUtil.showConfirm("Exit Confirm", "Are you sure you want to exit?")) {
                    smsScheduler.exit();
                }
            } else if (COMMAND_ABOUT.equals(commandName)) {
                DialogUtil.showInfo("About", "SMS Schedule by WBS");
            } else if (COMMAND_HELP.equals(commandName)) {
                DialogUtil.showInfo("Help", "Help is comming soon.");
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
        if (selectedPos <= 0) {
            DialogUtil.showInfo("Error", "Please select an event to open");
            return;
        }
        final Event event = (Event) eventsData.elementAt(selectedPos - 1);
        eventEditForm.show(event);
    }

    public void show() {
        show(false);
    }

    private void selectUp() {
        if (selectedPos > 1) {
            unSelectSchedule();
            selectedPos--;
            selectEvent();
        }
    }

    private void selectDown() {
        if (selectedPos < (eventsData.size())) {
            unSelectSchedule();
            selectedPos++;
            selectEvent();
        }
    }

    private void unSelectSchedule() {
        if (selectedPos <= 0) {
            return;
        }
        Container scheduleRow = (Container) eventsList.getComponentAt(selectedPos);
        final int bgColor = getBgColor(selectedPos - 1);
        scheduleRow.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle rect) {
                g.setColor(bgColor);
                g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(), rect.getSize().getHeight());
                g.setColor(Color.BLACK);
                final int x1 = rect.getX();
                final int y1 = rect.getY() + rect.getSize().getHeight();
                final int x2 = x1 + rect.getSize().getWidth();
                final int y2 = y1;
                g.drawLine( x1, y1, x2, y2);
            }
        });
        for (int i = 0; i < scheduleRow.getComponentCount(); i++) {
            Component scheduleCol = scheduleRow.getComponentAt(i);
            scheduleCol.getStyle().setBgColor(bgColor);
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
        eventsData.setElementAt(event, selectedPos - 1);
    }

    private void sendSms(String message, String telePhone) {
        try {
            final String msg = "Sending sms to " + telePhone;
            log(msg);
            final String addr = "sms://" + telePhone;
            final MessageConnection smsConnection = (MessageConnection) Connector.open(addr);
            final TextMessage sms = (TextMessage) smsConnection.newMessage(MessageConnection.TEXT_MESSAGE, addr);
            sms.setPayloadText(message);
            smsConnection.send(sms);
            DialogUtil.showInfo("Alert", "Sent sms to " + telePhone + " with message \"" + message + "\"");
        } catch (Exception ex) {
            DialogUtil.showInfo("Error", "Delivery of sms to " + telePhone + " with message \"" + message + "\" failed with error " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public Vector getEvents() {
        return this.eventsData;
    }
}
