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
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.plaf.Border;
import com.sun.lwuit.plaf.Style;
import com.wbs.SMSScheduler;
import com.wbs.constant.Color;
import com.wbs.form.decorator.FontUtil;
import com.wbs.form.decorator.FormCommonDecorator;
import com.wbs.logging.Logger;
import com.wbs.utils.UiUtil;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
class EventEditForm implements ActionListener {

    public static final String BLANK_VALUE = "  ";
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
    private final SMSScheduler smsScheduler;
    private Form form = null;
    private TextField eventName;
    private TextArea eventMessage;
    private final CheckBox selfEvent;
    private Button addRecpientsButton;
    private DateLabel due;
    private Label dueButton;
    private ComboBox recurringType;
    private boolean editMode = false;
    private String focussed;
    final Command selectCommand = new Command(OPTION_SELECT);
    private RecipientsContainer recipientsList;
    private Vector recipientsData = new Vector();
    private Container header;
    private ComboBox hour;
    private ComboBox minute;
    private ComboBox recurringPeriod;
    private Event event;
    private Vector addressBook;

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

    private void applyDefaultComboxBoxStyle(ComboBox cb) {
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) cb.getRenderer();
        renderer.getStyle().setFont(getSmallNormalFont());
        renderer.getStyle().setBgColor(Color.WHITE);
        renderer.getSelectedStyle().setFont(getSmallBoldFont());
        renderer.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        cb.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        cb.setSelectedItem(RECURRING_NONE);
    }

    protected void save() {
        Event event = generateEvent();
        EventsListForm listForm = smsScheduler.getListForm();
        if (editMode) {
            listForm.saveEvent(event);
        } else {
            listForm.addEvent(event);
        }
        listForm.show(true);
    }

    private void delete() {
        smsScheduler.getListForm().deleteEvent();
        smsScheduler.getListForm().show();
    }

    private Event generateEvent() {
        EventsListForm listForm = smsScheduler.getListForm();
        final String eventName = this.eventName.getText();
        final String eventMessage = this.eventMessage.getText();
        final boolean selfEvent = this.selfEvent.isSelected();
        Calendar cal = Calendar.getInstance();
        cal.setTime(due.getDate());
        cal.set(Calendar.HOUR_OF_DAY, getInt(hour.getSelectedItem().toString()));
        cal.set(Calendar.MINUTE, getInt(minute.getSelectedItem().toString()));
        Date dueDate = cal.getTime();
        final String recurringPeriod = this.recurringPeriod.getSelectedItem().toString();
        final String recurringType = this.recurringType.getSelectedItem().toString();
        Event event = new Event();
        event.setName(eventName);
        event.setMessage(eventMessage);
        event.setSelfEvent(selfEvent);
        event.setRecipients(recipientsData);
        event.setDueDate(dueDate);
        event.setRecurringType(recurringType);
        event.setRecurringPeriod(recurringPeriod);
        return event;
    }

    protected boolean valid() {
        final EventsListForm listForm = smsScheduler.getListForm();
        final String eventNameTxt = this.eventName.getText();
        final String eventMessageTxt = this.eventMessage.getText();
        Logger.logInfo("this.recurringPeriod.getSelectedItem().toString() - " + this.recurringPeriod.getSelectedItem().toString());
        Logger.logInfo("this.recurringType.getSelectedItem().toString() - " + this.recurringType.getSelectedItem().toString());
        Logger.logInfo("RECURRING_NONE - " + RECURRING_NONE);
        Logger.logInfo("isEmpty(this.recurringPeriod.getSelectedItem().toString()) - " + isEmpty(this.recurringPeriod.getSelectedItem().toString())
                       + "- !this.recurringType.getSelectedItem().toString().equals(RECURRING_NONE) - " + !this.recurringType.getSelectedItem().toString().equals(RECURRING_NONE));
        if (eventNameTxt.trim().equalsIgnoreCase("")) {
            DialogUtil.showInfo("Error", "Please enter event name");
            form.setFocused(this.eventName);
            return false;
        } else if (eventMessageTxt.trim().equalsIgnoreCase("")) {
            DialogUtil.showInfo("Error", "Please enter event message");
            form.setFocused(this.eventMessage);
            return false;
        } else if (!selfEvent.isSelected() && recipientsData.isEmpty()) {
            DialogUtil.showInfo("Error", "Please add a recipient");
            form.setFocused(this.addRecpientsButton);
            form.scrollComponentToVisible(this.addRecpientsButton);
            return false;
        } else if (isEmpty(this.recurringPeriod.getSelectedItem().toString())
                       && !this.recurringType.getSelectedItem().toString().equals(RECURRING_NONE)) {
            DialogUtil.showInfo("Error", "Please select recurring period");
            form.setFocused(this.recurringPeriod);
            return false;
        } else {
            return true;
        }
    }

    private void goBack() {
        if (isDirty() && DialogUtil.showConfirm("Alert", "Do you want to save data?")) {
            validateAndSave();
        } else {
            final EventsListForm listForm = smsScheduler.getListForm();
            listForm.show();
        }
    }

    private boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    private void clearRecipientsList() {
        recipientsList.clear();
//        for (int i = 0; i < this.recipientsList.size(); i++) {
//            this.recipientsList.getModel().removeItem(i);
//        }
        addRecpientsButton.setEnabled(false);
    }

    private ComboBox getComboBox(final int size, boolean startWithZero, boolean defaultIsBlank) {
        final ComboBox cb = new ComboBox();
//        DefaultListCellRenderer renderer = (DefaultListCellRenderer) cb.getRenderer();
//        renderer.getStyle().setFont(getSmallNormalFont());
//        renderer.getStyle().setBgColor(Color.WHITE);
//        renderer.getSelectedStyle().setFont(smallBoldFont);
//        renderer.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
//        cb.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        applyDefaultComboxBoxStyle(cb);
        if (defaultIsBlank) {
            cb.addItem(BLANK_VALUE);
            cb.setSelectedIndex(0);
        }
        for (int i = startWithZero ? 0 : 1; i < size; i++) {
            String data = String.valueOf(i);
            if (data.length() == 1) {
                data = "0" + data;
            }
            cb.addItem(data);
        }
        return cb;
    }

    private void removeRecipient() {
        final int deletedPos = this.recipientsList.removeSelected();
        this.recipientsData.removeElementAt(deletedPos);
    }

    private void setAddRecipientsState() {
        if (!selfEvent.isSelected()) {
            addRecpientsButton.setEnabled(true);
//            addRecpientsButton.getStyle().setBorder(Border.getDefaultBorder());
//            addRecpientsButton.getSelectedStyle().setBorder(Border.getDefaultBorder());
        } else {
            addRecpientsButton.setEnabled(false);
//            addRecpientsButton.getStyle().setBorder(Border.createEmpty());
//            addRecpientsButton.getSelectedStyle().setBorder(Border.createEmpty());
        }
    }

    private void setFocusNavigations() {
        setFocusNavigation(eventName, eventMessage, recurringType);
        setFocusNavigation(eventMessage, selfEvent, eventName);
        setSelfEventFocusNavigations();
        setFocusNavigation(hour, minute, dueButton);
        setFocusNavigation(minute, recurringPeriod, hour);
        setFocusNavigation(recurringPeriod, recurringType, minute);
        setFocusNavigation(recurringType, eventName, recurringPeriod);
        eventName.addFocusListener(new FocusListener() {

            public void focusGained(Component cmp) {
                form.scrollComponentToVisible(header);
            }

            public void focusLost(Component cmp) {
            }
        });
//        setFocusNavigation(header, eventName, recurringType);
    }

    private void setFocusNavigation(final Component comp, final Component nextFocusComp, final Component prevFocusComp) {
        comp.setNextFocusDown(nextFocusComp);
        comp.setNextFocusUp(prevFocusComp);
    }

    private void addSelfEventActionListener() {
        selfEvent.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                setSelfEventFocusNavigations();
//                recipientsContainer.refreshTheme();
//                recipientsContainer.repaint();
                form.show();
            }
        });
    }

    private void setSelfEventFocusNavigations() {
        if (!selfEvent.isSelected()) {
            addRecpientsButton.setEnabled(true);
            if (recipientsData.isEmpty()) {
                setFocusNavigation(selfEvent, addRecpientsButton, eventMessage);
                setFocusNavigation(addRecpientsButton, dueButton, selfEvent);
            } else {
                setFocusNavigation(selfEvent, recipientsList, eventMessage);
                setFocusNavigation(recipientsList, addRecpientsButton, selfEvent);
                setFocusNavigation(addRecpientsButton, dueButton, recipientsList);
            }
            setFocusNavigation(dueButton, hour, addRecpientsButton);
        } else {
            addRecpientsButton.setEnabled(false);
            setFocusNavigation(selfEvent, dueButton, eventMessage);
            setFocusNavigation(dueButton, hour, selfEvent);
        }
    }

    private void addRecipients() {
        Container recipientsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        recipientsList = new RecipientsContainer();
//        DefaultListCellRenderer dc = (DefaultListCellRenderer) recipientsList.getRenderer();
//        dc.setShowNumbers(false);
        final Style style = recipientsList.getStyle();
        style.setFont(getSmallNormalFont());
        style.setBgColor(Color.WHITE);
        style.setMargin(0, 0, 0, 0);
        style.setPadding(0, 0, 0, 0);
        final Style selectedStyle = recipientsList.getSelectedStyle();
        selectedStyle.setFont(getSmallBoldFont());
        selectedStyle.setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        selectedStyle.setMargin(0, 0, 0, 0);
        selectedStyle.setPadding(0, 0, 0, 0);
        recipientsList.setFocusable(true);
//        recipientsList.setPreferredW(com.sun.lwuit.Display.getInstance().getDisplayWidth());
        form.addKeyListener(-1, recipientsList);
        form.addKeyListener(-2, recipientsList);
        addLabelledComponent("To: ", recipientsList, recipientsContainer);
        addRecpientsButton = new Button("Add");
        addRecpientsButton.getStyle().setFont(getSmallNormalFont());
        final Border buttonBorder = Border.createEtchedRaised(Color.EVENTLIST_HEADER_BACKGROUND, Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        addRecpientsButton.getStyle().setBorder(buttonBorder, false);
        addRecpientsButton.getStyle().setMargin(2, 2, 2, 2);
        addRecpientsButton.getStyle().setPadding(2, 2, 2, 2);
        addRecpientsButton.getSelectedStyle().setFont(getSmallBoldFont());
        addRecpientsButton.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        addRecpientsButton.getSelectedStyle().setBorder(buttonBorder, false);
        addRecpientsButton.getSelectedStyle().setMargin(2, 2, 2, 2);
        addRecpientsButton.getSelectedStyle().setPadding(2, 2, 2, 2);
        addRecpientsButton.setScrollAnimationSpeed(0);
        recipientsContainer.addComponent(addRecpientsButton);
        addRecpientsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                showContactsDialog();
            }
        });
        form.addComponent(recipientsContainer);
    }

    private void setInitialFocus() {
//        form.scrollComponentToVisible(header);
//        form.scrollComponentToVisible(eventName);
        eventName.setFocus(true);
        form.setFocused(eventName);
    }

    private void showContactsDialog() {
        if (addressBook == null) {
            addressBook = AddressBookService.retrieveContactsFromAddressBook();
        }
        final Dialog dialog = new Dialog();
        dialog.setLayout(new BorderLayout());
        dialog.setScrollable(false);
        dialog.setAutoDispose(false);
        dialog.addCommand(new Command("Save"));
        dialog.addCommand(new Command("Back"));
        dialog.addCommand(new Command("Add"));
        final TextField phoneNumber = new TextField();
        phoneNumber.getStyle().setFont(getSmallNormalFont());
        phoneNumber.getSelectedStyle().setFont(getSmallNormalFont());
        final ContactsList list = new ContactsList(phoneNumber, recipientsData, addressBook);
        phoneNumber.addDataChangeListener(list);
        dialog.addKeyListener(-1, list);
        dialog.addKeyListener(-2, list);
        dialog.addComponent(BorderLayout.NORTH, phoneNumber);
        dialog.addComponent(BorderLayout.CENTER, list);
        dialog.addCommandListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                final Command cmd = evt.getCommand();
                if (cmd != null) {
                    final String commandName = cmd.getCommandName();
                    if ("Save".equals(commandName)) {
                        recipientsData = list.getSelectedPhoneEntries();
                        recipientsList.clear();
                        for (int i = 0; i < recipientsData.size(); i++) {
                            final PhoneEntry pe = (PhoneEntry) recipientsData.elementAt(i);
                            recipientsList.addRecipient(pe.getDisplayableText());
                        }
                        setSelfEventFocusNavigations();
                        dialog.dispose();
                    } else if ("Select".equals(commandName)) {
                        final ContactRow r = list.getSelectedRow();
                        r.select();
                    } else if ("Unselect".equals(commandName)) {
                        final ContactRow r = list.getSelectedRow();
                        r.unSelect();
                    } else if ("Back".equals(commandName)) {
                        dialog.dispose();
                    } else if ("Add".equals(commandName)) {
                        if (!Display.getInstance().isEdt()) {
                            Display.getInstance().callSerially(new Runnable() {

                                public void run() {
                                    list.addSearchTextAsContact();
                                }
                            });
                        } else {
                            list.addSearchTextAsContact();
                        }
                    }
                }
            }
        });
        FormCommonDecorator.decorate(dialog);
        dialog.show(
                2, 1, 2, 1, true);
    }

    private void addDueDate() {
        Container buttonContainer = new Container(new FlowLayout());
        addLabel("Due Date: ", buttonContainer);
        due = new DateLabel();
        buttonContainer.addComponent(due);
        Image buttonImage = getImage("/img/icons/calendar.png");
        Label button = new Label(buttonImage);
        button.setFocusable(true);
        button.setFocusPainted(true);
        button.getSelectedStyle().setBorder(Border.createEtchedRaised());
        button.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
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
        Container timeContainer = new Container(new FlowLayout());
        addLabel("Time: ", timeContainer);
        hour = getComboBox(24, true, false);
        timeContainer.addComponent(hour);
        minute = getComboBox(60, true, false);
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
        cb.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
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
        Container recurringContainer = new Container(new FlowLayout());
        addLabel("Repeat ", recurringContainer);
        recurringPeriod = getComboBox(100, false, true);
        recurringContainer.addComponent(recurringPeriod);
        recurringType = new ComboBox(RECURRING_TYPES);
        applyDefaultComboxBoxStyle(recurringType);
        recurringType.setSelectedItem(RECURRING_NONE);
        recurringContainer.addComponent(recurringType);
        form.addComponent(recurringContainer);
    }

    private void addCommands() {
        form.addCommand(new Command("Save"));
        form.addCommand(new Command("Back"));
        form.addCommand(new Command("Exit"));
        form.addCommand(new Command("About"));
        form.addCommand(new Command("Help"));
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

//        Label logo = new Label("WBS");
//        logo.getStyle().setBgColor(0xB40404);
//        logo.getStyle().setFgColor(0xFFFFFF);
//        Font bigNormalFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_LARGE);
//        logo.getStyle().setFont(bigNormalFont);
//        logo.setAlignment(Component.RIGHT);
        final int logoWidth = Display.getInstance().getDisplayWidth() / 100 * 40;
        final Label logoLabel = UiUtil.getImageLabel("/img/logo.jpg", logoWidth);
//        logoLabel.setAlignment(Label.RIGHT);
        header.addComponent(logoLabel);

//        Container appNameContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        Label appNameFirstLine = new Label(EventsListForm.APP_NAME);
        appNameFirstLine.getStyle().setFont(FontUtil.getMediumBoldFont());
//        appNameFirstLine.setAlignment(Component.LEFT);
//        appNameFirstLine.getStyle().setMargin(0, 0, 0, 0);
//        appNameFirstLine.getStyle().setPadding(0, 0, 0, 0);
        header.addComponent(appNameFirstLine);

//        Label appNameSecondLine = new Label("Scheduler");
//        appNameSecondLine.getStyle().setFont(FontUtil.getMediumBoldFont());
//        appNameSecondLine.setAlignment(Component.LEFT);
//        appNameSecondLine.getStyle().setMargin(0, 0, 0, 0);
//        appNameSecondLine.getStyle().setPadding(0, 0, 0, 0);
//        appNameContainer.addComponent(appNameSecondLine);

//        container.addComponent(appNameContainer);

        form.addComponent(header);
    }

    private TextField addTextField(String fieldName) {
        return addTextField(fieldName, form);
    }

    private TextField addTextField(String fieldName, Container container) {
        TextField textField = new TextField();
        textField.getStyle().setFont(getSmallNormalFont());
        textField.getSelectedStyle().setFont(getSmallNormalFont());
        textField.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        addLabelledComponent(fieldName, textField, container);
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
        this.recurringPeriod.setSelectedItem(event.getRecurringPeriod());
        this.recurringType.setSelectedItem(event.getRecurringType());
        this.recipientsData = event.getRecipients();
        clearRecipientsList();
        if (!recipientsData.isEmpty()) {
            addRecpientsButton.setEnabled(true);
        }
        for (int i = 0; i < recipientsData.size(); i++) {
            PhoneEntry r = (PhoneEntry) recipientsData.elementAt(i);
            recipientsList.addRecipient(r.getDisplayableText());
        }
        setAddRecipientsState();
        setInitialFocus();
        setFocusNavigations();
        this.event = event;
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
        textArea.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
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
            if (commandName.equals("Back")) {
                goBack();
            } else if (commandName.equals("Save")) {
                validateAndSave();
            } else if ("Exit".equals(commandName)) {
                smsScheduler.exit();
            } else if ("About".equals(commandName)) {
                DialogUtil.showInfo("About", "SMS Schedule by WBS");
            } else if ("Help".equals(commandName)) {
                DialogUtil.showInfo("Help", "Help is comming soon.");
            } else if (commandName.equals(OPTION_SELECT)) {
                if (SELECT_DUE_DATE.equals(focussed)) {
                    showCalendar();
                }
            } else if (commandName.equals("Remove")) {
                removeRecipient();
            } else if (commandName.equals("Delete")) {
                delete();
            }
        }
    }

    private void validateAndSave() {
        if (valid()) {
            save();
        }
    }

    void show() {
        this.editMode = false;
        this.eventName.setText("");
        this.eventMessage.setText("");
        this.selfEvent.setSelected(true);
        this.due.setText("dd/mm/yyyy");
        this.hour.setSelectedItem("0");
        this.minute.setSelectedItem("0");
        this.recurringType.setSelectedItem(RECURRING_NONE);
        this.recipientsData.removeAllElements();
        clearRecipientsList();
        setAddRecipientsState();
        setFocusNavigations();
        setInitialFocus();
        this.event = new Event();
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

    private boolean isDirty() {
        final Event modifiedEvent = generateEvent();
        final String modifiedData = modifiedEvent.getStorableFormat();
        final String originalData = this.event.getStorableFormat();
        Logger.logInfo("Dirty Check - " + originalData + " - " + modifiedData + " - " + modifiedData.equals(originalData));
        return !modifiedData.equals(originalData);
    }
}
