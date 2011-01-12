/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.wbs.utils.DialogUtil;
import com.sun.lwuit.Button;
import com.sun.lwuit.CheckBox;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.list.DefaultListModel;
import com.sun.lwuit.list.ListCellRenderer;
import com.sun.lwuit.list.ListModel;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.wbs.SMSScheduler;
import com.wbs.constant.Color;
import com.wbs.form.decorator.FormCommonDecorator;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.pim.Contact;
import javax.microedition.pim.ContactList;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMList;

/**
 *
 * @author Vikram S
 */
class EventEditForm implements ActionListener {

    public static final String OPTION_SELECT = "Select";
    public static final String RECURRING_DAY = "Day";
    public static final String RECURRING_HOUR = "Hour";
    public static final String RECURRING_MINUTE = "Minute";
    public static final String RECURRING_MONTH = "Month";
    public static final String RECURRING_NONE = "None";
    public static final String RECURRING_WEEK = "Week";
    public static final String RECURRING_YEAR = "Year";
    public static final String SELECT_DUE_DATE = "DueDate";
    private static final String[] RECURRING_TYPES = {RECURRING_NONE, RECURRING_MINUTE, RECURRING_HOUR, RECURRING_DAY, RECURRING_WEEK, RECURRING_MONTH, RECURRING_YEAR};
    private static final String[] RECIPIENTS_TEST_DATA = {"Abhay Shenvi", "+919945678903", "Vikram Subbarao", "Ramesh Home"};
    private final SMSScheduler smsScheduler;
    private Form form = null;
    private TextField eventName;
    private TextArea eventMessage;
    private final CheckBox selfEvent;
    private Container recipientsContainer;
    private Button addRecpientsButton;
    private DateLabel due;
    private Label dueButton;
    private TextField recurringPeriod;
    private ComboBox recurringType;
    private boolean editMode = false;
    private String focussed;
    final Command selectCommand = new Command(OPTION_SELECT);
    private TextField phoneNumberField;
    private List recipientsList;
    private Vector recipients = new Vector();
    private Container header;
    private ComboBox hour;
    private ComboBox minute;

    public EventEditForm(SMSScheduler smsScheduler) {
        this.smsScheduler = smsScheduler;
        initialize();
        addHeader();
        addSpacer();
        eventName = addTextField("Event Name");
        eventMessage = addTextArea("Message");
        selfEvent = addCheckBox("Self Event");
        addRecipients();
        addDueDate();
        addRecurring();
        addCommands();
        addSelfEventActionListener();
        setFocusNavigations();
        form = FormCommonDecorator.decorate(form);
    }

    protected String getContactField(final Contact c, final int field) {
        String v = "";
        try {
            v = c.getString(field, 0);
        } catch (Exception e) {
        }
        return v;
    }

    protected Vector getContacts() {
        final PIM pim = PIM.getInstance();
        final String[] listPIMLists = pim.listPIMLists(PIM.CONTACT_LIST);
        boolean nameSupported = false;
        boolean mobileSupported = false;
        boolean homePhoneSupported = false;
        boolean workPhoneSupported = false;
        boolean telSupported = false;
        String name = null;
        String mobile = null;
        String homePhone = null;
        String workPhone = null;
        String telePhone = null;
        Vector phoneEntries = new Vector();
        for (int i = 0; i < listPIMLists.length; i++) {
            try {
                System.out.println("Displaying contacts from - " + listPIMLists[i]);
                final PIMList contactList = pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY, listPIMLists[i]);
                if (contactList.isSupportedField(Contact.FORMATTED_NAME)) {
                    nameSupported = true;
                } else {
                    nameSupported = false;
                }
                if (contactList.isSupportedField(Contact.ATTR_MOBILE)) {
                    mobileSupported = true;
                } else {
                    mobileSupported = false;
                }
                if (contactList.isSupportedField(Contact.ATTR_HOME)) {
                    homePhoneSupported = true;
                } else {
                    homePhoneSupported = false;
                }
                if (contactList.isSupportedField(Contact.ATTR_WORK)) {
                    workPhoneSupported = true;
                } else {
                    workPhoneSupported = false;
                }
                if (contactList.isSupportedField(Contact.TEL)) {
                    telSupported = true;
                } else {
                    telSupported = false;
                }
                final Enumeration items = contactList.items();
                while (items.hasMoreElements()) {
                    final Contact c = (Contact) items.nextElement();
                    if (nameSupported) {
                        name = getContactField(c, Contact.FORMATTED_NAME);
                    }
                    if (mobileSupported) {
                        mobile = getContactField(c, Contact.ATTR_MOBILE);
                        addPhoneEntry(name, mobile, phoneEntries);
                    }
                    if (homePhoneSupported) {
                        homePhone = getContactField(c, Contact.ATTR_HOME);
                        addPhoneEntry(name, homePhone, phoneEntries);
                    }
                    if (workPhoneSupported) {
                        workPhone = getContactField(c, Contact.ATTR_WORK);
                        addPhoneEntry(name, workPhone, phoneEntries);
                    }
                    if (telSupported) {
                        telePhone = getContactField(c, Contact.TEL);
                        addPhoneEntry(name, telePhone, phoneEntries);
                    }
                    System.out.println("Name: " + name + ", mobile: " + mobile + ", home: " + homePhone + ", work: " + workPhone + ", tel: " + telePhone);
                }
            } catch (PIMException ex) {
                ex.printStackTrace();
            }
        }
        return phoneEntries;
    }

    private void addPhoneEntry(String name, String telePhone, Vector phoneEntries) {
        PhoneEntry phoneEntry = new PhoneEntry(name, telePhone);
        phoneEntries.addElement(phoneEntry);
    }

    protected void save() {
        EventsListForm listForm = smsScheduler.getListForm();
        final String eventName = this.eventName.getText();
        final String eventMessage = this.eventMessage.getText();
        final boolean selfEvent = this.selfEvent.isSelected();
        Calendar cal = Calendar.getInstance();
        cal.setTime(due.getDate());
        cal.set(Calendar.HOUR_OF_DAY, getInt(hour.getSelectedItem().toString()));
        cal.set(Calendar.MINUTE, getInt(minute.getSelectedItem().toString()));
        Date dueDate = cal.getTime();
        final String period = this.recurringPeriod.getText();
        final String type = this.recurringType.getSelectedItem().toString();
        Event event = new Event();
        event.setName(eventName);
        event.setMessage(eventMessage);
        event.setSelfEvent(selfEvent);
        event.setRecipients(recipients);
        event.setDueDate(dueDate);
        event.setRecurring(type, period);
        if (editMode) {
            listForm.saveEvent(event);
        } else {
            listForm.addEvent(event);
        }
        listForm.show(true);
    }

    protected boolean valid() {
        final EventsListForm listForm = smsScheduler.getListForm();
        final String eventName = this.eventName.getText();
        final String eventMessage = this.eventMessage.getText();
        if (eventName.trim().equalsIgnoreCase("")) {
            DialogUtil.showInfo("Error", "Please enter event name");
            return false;
        } else if (eventMessage.trim().equalsIgnoreCase("")) {
            DialogUtil.showInfo("Error", "Please enter event message");
            return false;
        } else if (!selfEvent.isSelected() && recipients.isEmpty()) {
            DialogUtil.showInfo("Error", "Please add a recipient");
            return false;
        } else if (!isInt(recurringPeriod.getText())) {
            DialogUtil.showInfo("Error", "Please enter a valid recurring period");
            return false;
        } else {
            return true;
        }
    }

    private void clearRecipientsList() {
        for (int i = 0; i < this.recipientsList.size(); i++) {
            this.recipientsList.getModel().removeItem(i);
        }
        addRecpientsButton.setEnabled(false);
    }

    private ComboBox getComboBox(final int size) {
        final Font smallBoldFont = getSmallBoldFont();
        final ComboBox cb = new ComboBox();
//        cb.getStyle().setFont(getSmallNormalFont());
//        cb.getStyle().setBgColor(Colors.WHITE);
//        cb.getSelectedStyle().setFont(smallBoldFont);
//        cb.getSelectedStyle().setBgColor(EventsListForm.COLOR_SCHEDULES_SELECTED_BACKGROUND);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) cb.getRenderer();
        final ListCellRenderer renderer1 = cb.getRenderer();
        renderer.getStyle().setFont(getSmallNormalFont());
        renderer.getStyle().setBgColor(Color.WHITE);
        renderer.getSelectedStyle().setFont(smallBoldFont);
        renderer.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
//        renderer.setPreferredW(smallBoldFont.stringWidth("1000"));
        for (int i = 0; i < size; i++) {
            cb.addItem(String.valueOf(i));
        }
        return cb;
    }

    private void setAddRecipientsState() {
        if (!selfEvent.isSelected()) {
            addRecpientsButton.setEnabled(true);
        } else {
            addRecpientsButton.setEnabled(false);
        }
    }

    private void setFocusNavigations() {
        setFocusNavigation(eventName, eventMessage, recurringType);
        setFocusNavigation(eventMessage, selfEvent, eventName);
        setSelfEventFocusNavigations();
        setFocusNavigation(hour, minute, dueButton);
        setFocusNavigation(minute, recurringPeriod, hour);
        setFocusNavigation(recurringPeriod, recurringType, minute);
        setFocusNavigation(recurringType, header, recurringPeriod);
        setFocusNavigation(header, eventName, recurringType);
    }

    private void setFocusNavigation(final Component comp, final Component nextFocusComp, final Component prevFocusComp) {
        comp.setNextFocusDown(nextFocusComp);
        comp.setNextFocusUp(prevFocusComp);
    }

    private void addSelfEventActionListener() {
        selfEvent.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                setSelfEventFocusNavigations();
                recipientsContainer.refreshTheme();
                recipientsContainer.repaint();
                form.show();
            }
        });
    }

    private void setSelfEventFocusNavigations() {
        if (!selfEvent.isSelected()) {
            addRecpientsButton.setEnabled(true);
            setFocusNavigation(selfEvent, addRecpientsButton, eventMessage);
            setFocusNavigation(addRecpientsButton, dueButton, selfEvent);
            setFocusNavigation(dueButton, recurringPeriod, addRecpientsButton);
        } else {
            addRecpientsButton.setEnabled(false);
            setFocusNavigation(selfEvent, dueButton, eventMessage);
            setFocusNavigation(dueButton, recurringPeriod, selfEvent);
        }
    }

    private void addRecipients() {
        recipientsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        recipientsList = new List();
        DefaultListCellRenderer dc = (DefaultListCellRenderer) recipientsList.getRenderer();
        dc.setShowNumbers(false);
        final Style style = dc.getStyle();
        style.setFont(getSmallNormalFont());
        style.setBgColor(Color.WHITE);
        style.setMargin(0, 0, 0, 0);
        style.setPadding(0, 0, 0, 0);
        final Style selectedStyle = dc.getSelectedStyle();
        selectedStyle.setFont(getSmallNormalFont());
        selectedStyle.setBgColor(Color.WHITE);
        selectedStyle.setMargin(0, 0, 0, 0);
        selectedStyle.setPadding(0, 0, 0, 0);
        recipientsList.setFocusable(false);
        addLabelledComponent("To: ", recipientsList, recipientsContainer);
        addRecpientsButton = new Button("Add");
        addRecpientsButton.getStyle().setFont(getSmallNormalFont());
        addRecpientsButton.getSelectedStyle().setFont(getSmallBoldFont());
        addRecpientsButton.setScrollAnimationSpeed(0);
        recipientsContainer.addComponent(addRecpientsButton);
        addRecpientsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showRecipientsDialog();
            }
        });
        form.addComponent(recipientsContainer);
    }

    private void setInitialFocus() {
        form.scrollComponentToVisible(header);
        form.scrollComponentToVisible(eventName);
    }

    private void showRecipientsDialog() {
        final Vector contacts = getContacts();
        Dialog dialog = new Dialog();
        dialog.setLayout(new BorderLayout());
        dialog.setScrollable(false);
        dialog.setAutoDispose(true);
        dialog.addCommand(new Command("Select"));
        dialog.addCommand(new Command("Cancel"));
        final TextField phoneNumber = new TextField();
        final CustomList list = new CustomList(phoneNumber);
        for (int i = 0; i < contacts.size(); i++) {
            PhoneEntry contact = (PhoneEntry) contacts.elementAt(i);
            final Component contactRow = new ContactRow(contact);
            list.addComponent(contactRow);
        }
        dialog.addKeyListener(-1, list);
        dialog.addKeyListener(-2, list);
        phoneNumber.getStyle().setFont(getSmallNormalFont());
        phoneNumber.getSelectedStyle().setFont(getSmallNormalFont());
        phoneNumber.addDataChangeListener(new DataChangedListener() {

            public void dataChanged(int type, int index) {
                final String text = phoneNumber.getText();
                list.selectStartingWith(text);
            }
        });
        dialog.addComponent(BorderLayout.NORTH, phoneNumber);
        dialog.addComponent(BorderLayout.CENTER, list);
        dialog.addCommandListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                final Command cmd = evt.getCommand();
                if (cmd != null) {
                    final String commandName = cmd.getCommandName();
                    if ("Select".equals(commandName)) {
                        final PhoneEntry pe = list.getSelectedPhoneEntry();
                        recipients.addElement(pe);
                        recipientsList.addItem(pe.getDisplayableText());
                    }
                }
            }
        });
        dialog.show(
                2, 1, 2, 1, true);
    }

    private void addDueDate() {
        addLabel("Due", form);
        Container buttonContainer = new Container(new FlowLayout());
        addLabel("Date: ", buttonContainer);
        due = new DateLabel();
        buttonContainer.addComponent(due);
        Image buttonImage = getImage("/img/icons/calendar.png");
        Label button = new Label(buttonImage);
        button.setFocusable(true);
        button.setFocusPainted(true);
        button.getSelectedStyle().setBorder(Border.createEtchedRaised());
        button.addFocusListener(new FocusListener() {

            public void focusGained(Component cmp) {
                focussed = SELECT_DUE_DATE;
                form.addCommand(selectCommand, form.getCommandCount());
            }

            public void focusLost(Component cmp) {
                if (focussed.equals(SELECT_DUE_DATE)) {
                    focussed = "";
                    form.removeCommand(selectCommand);
                }
            }
        });
        buttonContainer.addComponent(button);
        this.dueButton = button;
        form.addComponent(buttonContainer);
        Container timeContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        addLabel("Time: ", timeContainer);
        hour = getComboBox(24);
        timeContainer.addComponent(hour);
        minute = getComboBox(60);
        timeContainer.addComponent(minute);
        form.addComponent(timeContainer);
    }

    private Image getImage(String imagePath) {
        Image buttonImage = null;
        try {
            buttonImage = Image.createImage(imagePath).scaledWidth(20);
        } catch (IOException iOException) {
        }
        return buttonImage;
    }

    private CheckBox addCheckBox(final String Self_Event) {
        CheckBox cb = new CheckBox(Self_Event);
        cb.getStyle().setFont(getSmallNormalFont());
        cb.getSelectedStyle().setFont(getSmallBoldFont());
        cb.setSelected(true);
        form.addComponent(cb);
        return cb;
    }

    private void addLabelledComponent(String labelName, Component component, Container container) {
        if (!"".equals(labelName)) {
            addLabel(labelName, container);
        }
        container.addComponent(component);
    }

    private void addRecurring() {
        Container recurringContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        recurringPeriod = addTextField("Repeat Every   ", recurringContainer);
        form.addComponent(recurringContainer);
        recurringType = new ComboBox(RECURRING_TYPES);
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) recurringType.getRenderer();
        renderer.getStyle().setFont(getSmallNormalFont());
        renderer.getStyle().setBgColor(Color.WHITE);
        renderer.getSelectedStyle().setFont(getSmallBoldFont());
        renderer.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        recurringType.setSelectedItem(RECURRING_NONE);
        form.addComponent(recurringType);
    }

    private void addCommands() {
        form.addCommand(new Command("Save"));
        form.addCommand(new Command("Exit"));
        form.addCommand(new Command("Help"));
        form.addCommand(new Command("About"));
        form.addCommand(new Command("Back"));
        form.addCommand(new Command("Delete"));
        form.addCommandListener(this);
    }

    private void addSpacer() {
        form.addComponent(new Label(" "));
    }

    private void initialize() {
        form = new Form();
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
    }

    private void addHeader() {
        header = new Container(new BoxLayout((BoxLayout.X_AXIS)));
        Label logo = new Label("WBS");
        logo.getStyle().setBgColor(0xB40404);
        logo.getStyle().setFgColor(Color.WHITE);
        Font bigBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
        logo.getStyle().setFont(bigBoldFont);
        logo.setAlignment(Component.RIGHT);
        header.addComponent(logo);
        Label label = new Label(EventsListForm.APP_NAME);
        Font mediumFont = getMediumBoldFont();
        label.getStyle().setFont(mediumFont);
        label.setAlignment(Component.LEFT);
        header.addComponent(label);
        header.setFocusable(true);
        form.addComponent(header);
    }

    private TextField addTextField(String fieldName) {
        return addTextField(fieldName, form);
    }

    private TextField addTextField(String fieldName, Container container) {
        TextField textField = new TextField();
        textField.getStyle().setFont(getSmallNormalFont());
        textField.getSelectedStyle().setFont(getSmallNormalFont());
        addLabelledComponent(
                fieldName, textField, container);
        return textField;
    }

    void show(Event event) {
        this.editMode = true;
        this.eventName.setText(event.getName());
        this.due.setDate(event.getDueDate());
        Calendar c = Calendar.getInstance();
        c.setTime(event.getDueDate().getDate());
        String hourStr = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
        String minStr = String.valueOf(c.get(Calendar.MINUTE));
        this.hour.setSelectedItem(hourStr);
        this.minute.setSelectedItem(minStr);
        this.eventMessage.setText(event.getMessage());
        this.selfEvent.setSelected(event.isSelfEvent());
        this.recurringPeriod.setText(event.getRecurringPeriod());
        this.recurringType.setSelectedItem(event.getRecurringType());
        this.recipients = event.getRecipients();
        clearRecipientsList();
        if (!recipients.isEmpty()) {
            addRecpientsButton.setEnabled(true);
        }
        for (int i = 0; i < recipients.size(); i++) {
            PhoneEntry r = (PhoneEntry) recipients.elementAt(i);
            recipientsList.addItem(r.getDisplayableText());
        }
        setAddRecipientsState();
        setFocusNavigations();
        setInitialFocus();
        this.form.show();
    }

    private Font getSmallBoldFont() {
        Font smallBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        return smallBoldFont;
    }

    private Font getSmallNormalFont() {
        Font smallNormalFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        return smallNormalFont;
    }

    private Font getMediumBoldFont() {
        Font mediumFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        return mediumFont;
    }

    private TextArea addTextArea(String name) {
        TextArea textArea = new TextArea();
        textArea.setRows(3);
        textArea.getStyle().setFont(getSmallNormalFont());
        textArea.getSelectedStyle().setFont(getSmallNormalFont());
        textArea.setEditable(true);
        textArea.setGrowByContent(false);
        textArea.setFocusable(true);
        textArea.setText("Sample\nText\nDone.");
        addLabelledComponent(
                name, textArea, form);
        return textArea;
    }

    private Label addLabel(String fieldName, Container container) {
        return addLabel(fieldName, container, Font.STYLE_BOLD);
    }

    private Label addLabel(String fieldName, Container container, int style) {
        Label label = new Label(fieldName);
        Font font = Font.createSystemFont(Font.FACE_SYSTEM, style, Font.SIZE_SMALL);
        label.getStyle().setFont(font);
        container.addComponent(label);
        return label;
    }

    public void actionPerformed(ActionEvent evt) {
        final Command command = evt.getCommand();
        if (command != null) {
            final String commandName = command.getCommandName();
            final EventsListForm listForm = smsScheduler.getListForm();
            if (commandName.equals("Back")) {
                listForm.show();
            } else if (commandName.equals("Save")) {
                if (valid()) {
                    save();
                }
            } else if ("Exit".equals(commandName)) {
                if (showConfirm("Exit Confirm", "Are you sure you want to exit?")) {
                    smsScheduler.exit();
                }
            } else if ("About".equals(commandName)) {
                showInfo("About", "SMS Schedule by WBS");
            } else if ("Help".equals(commandName)) {
                showInfo("Help", "Help is comming soon.");
            } else if (commandName.equals(OPTION_SELECT)) {
                if (SELECT_DUE_DATE.equals(focussed)) {
                    showCalendar();
                }
            }
        }
    }

    private void showInfo(final String title, final String message) {
        Dialog.show(title, message, "Back", null);
    }

    private boolean showConfirm(final String title, final String message) {
        return Dialog.show(title, message, "Ok", "Cancel");
    }

    void show() {
        this.editMode = false;
        this.eventName.setText("");
        this.eventMessage.setText("");
        this.selfEvent.setSelected(true);
        this.due.setText("dd/mm/yyyy");
        this.hour.setSelectedItem("0");
        this.minute.setSelectedItem("0");
        this.recurringPeriod.setText("");
        this.recurringType.setSelectedItem(RECURRING_NONE);
        this.recipients.removeAllElements();
        clearRecipientsList();
        setAddRecipientsState();
        setFocusNavigations();
        setInitialFocus();
        this.form.show();
    }

    private void showCalendar() {
        final Dialog calDialog = new Dialog();
        calDialog.setLayout(new BorderLayout());
        final CalendarWidget calendar = new CalendarWidget(due.getDate());
        calDialog.addComponent(BorderLayout.CENTER, calendar);
        calDialog.setFocused(calendar);
        calDialog.addCommand(new Command("Ok"));
        calDialog.addCommand(new Command("Cancel"));
        calDialog.setAutoDispose(true);
        calDialog.addCommandListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                final Command command = evt.getCommand();
                if (command != null) {
                    final String commandName = command.getCommandName();
                    if ("Ok".equals(commandName)) {
                        due.setDate(new CustomDate(calendar.getSelectedDate()));
                    }
                }
            }
        });
        calDialog.showPacked(BorderLayout.CENTER, true);
    }

    private int getInt(String s) {
        return Integer.parseInt(s);
    }

    private boolean isInt(String recurringPeriod) {
        if (recurringPeriod == null || "".equals(recurringPeriod.trim())) {
            return true;
        }
        try {
            Integer.parseInt(recurringPeriod);
            return true;
        } catch (NumberFormatException numberFormatException) {
        }
        return false;
    }
}
