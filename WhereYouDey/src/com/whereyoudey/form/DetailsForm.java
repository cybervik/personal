/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.form.component.Section;
import com.whereyoudey.utils.UiUtil;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 *
 * @author Vikram S
 */
abstract class DetailsForm implements ActionListener {
    public static final String OPTION_BACK = "Back";
    public static final String OPTION_CALL = "Call";
    public static final String OPTION_EXIT = "Exit";
    public static final String OPTION_HOME = "Home";

    protected Form form;
    private Label header;
    private final WhereYouDey midlet;
    private Result result;
    private final ResultForm callingForm;

    public DetailsForm(WhereYouDey midlet, ResultForm callingForm) {
        this.midlet = midlet;
        this.callingForm = callingForm;
        initForm();
        addHeader();
        addFormElements();
        addCommands();
        form.show();
    }

    protected abstract void addFormElements() throws NumberFormatException;

    protected abstract String getHeaderProperty();

    protected abstract String getPhoneProperty();

    private void call() {
        try {
            final String phoneNumber = result.getProperty(getPhoneProperty());
            if ("".equals(phoneNumber.trim())) {
                showDialog("Phone number not found in this result.");
            } else {
                midlet.platformRequest("tel:" + phoneNumber);
            }
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void exit() {
        try {
            midlet.destroyApp(true);
            midlet.notifyDestroyed();
        } catch (MIDletStateChangeException ex) {
            ex.printStackTrace();
        }
    }

    private void goBack() {
        callingForm.show();
    }

    private void goHome() {
        midlet.getSearchForm().show();
    }

    private void initForm() {
        form = new Form();
        form.setWidth(Display.getInstance().getDisplayWidth());
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.setScrollableX(true);
    }

    private void addCommands() {
        form.addCommand(new Command(OPTION_BACK));
        form.addCommand(new Command(OPTION_EXIT));
        form.addCommand(new Command(OPTION_HOME));
        form.addCommand(new Command(OPTION_CALL));
        form.addCommandListener(this);
    }

    protected Label addBigFontLabel(final String ph) {
        Label phoneNumber = new Label(ph);
        Font bigFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        phoneNumber.getStyle().setFont(bigFont);
        form.addComponent(phoneNumber);
        return phoneNumber;
    }

    private void addHeader() {
        header = new Label("");
        header.getStyle().setBgColor(0x000000);
        header.getStyle().setFgColor(0xffffff);
        form.addComponent(header);
    }

    protected Label addSmallFontLabel(final String txt) {
        Label label = new Label(txt);
        setSmallFont(label);
        form.addComponent(label);
        return label;
    }

    protected abstract void initResult(Result result);

    private void setHeader(Result result) {
        final String bizName = result.getProperty(getHeaderProperty());
        header.setText(bizName);
    }

    private void setSmallFont(Component comp) {
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        comp.getStyle().setFont(smallFont);
    }

    protected Image getImage(String imagePath, int imageWidth) {
        Image img = null;
        try {
            img = Image.createImage(imagePath);
            img = img.scaledHeight(imageWidth);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

    public void init(Result result) {
        this.result = result;
        setHeader(result);
        initResult(result);
        form.show();
    }

    public void actionPerformed(ActionEvent ae) {
        final Command command = ae.getCommand();
        final String commandName = command.getCommandName();
        if (OPTION_EXIT.equals(commandName)) {
            exit();
        } else if (OPTION_HOME.equals(commandName)) {
            goHome();
        } else if (OPTION_CALL.equals(commandName)) {
            call();
        } else if (OPTION_BACK.equals(commandName)) {
            goBack();
        }
    }

    private void showDialog(String message) {
        Dialog.show(null, message, "Ok", null);
    }
}
