/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.midp.io.HttpUrl;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.form.component.Section;
import com.whereyoudey.utils.UiUtil;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
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
    public static final String OPTION_SELECT = "Select";
    public static final String LINK_AUDIO_VIDEO_URL = "http://youtube.com/whereyoudey";
    public static final String LINK_DRIVING_DIRECTIONS = "Driving Directions";
    public static final String LINK_MAPS = "Maps";
    public static final String LINK_MAPS_URL = "http://maps.google.com";
    public static final String LINK_OFFERS = "Offers";
    public static final String LINK_OFFERS_URL = "http://whereyoudey.com/offers";
    public static final String LINK_VIDEO_AUDIO = "Video/Audio";
    protected Form form;
    private Label header;
    protected final WhereYouDey midlet;
    protected Result result;
    private final ResultForm callingForm;
    String focussed = "";
    private Section links;
    private Container drivingDirections;
    private TextField fromDriving;
    private TextField toDriving;
    private Button getDrivingDirectionsButton;
    private Label mapImage;
    private Dialog mapImageDialog;

    public DetailsForm(WhereYouDey midlet, ResultForm callingForm) {
        this.midlet = midlet;
        this.callingForm = callingForm;
        initForm();
        addHeader();
        addFormElements();
        addCommands();
        form.show();
    }

    protected void addFormElements() throws NumberFormatException {
        addBasicInfo();
        addSections();
    }

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
        form.addCommand(new Command(OPTION_SELECT));
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
        } else if (OPTION_SELECT.equals(commandName)) {
            final Component focused = form.getFocused();
            if (focused != null && focused instanceof Label) {
                Label focussedLabel = (Label) focused;
                final String focussedName = focussedLabel.getText();
                selectAction(focussedName);
            }
        }
    }

    private void showDialog(String message) {
        Dialog.show(null, message, "Ok", null);
    }

    protected void selectAction(String focussedName) {
        if (focussedName.equals(LINK_MAPS)) {
            try {
                HttpConnection conn = (HttpConnection) Connector.open("http://maps.google.com/maps/api/staticmap?"
                                                                      + "center=" + UiUtil.urlEncode(getAddress())
                                                                      + "&zoom=13"
                                                                      + "&size=" + Display.getInstance().getDisplayWidth() + "x" + Display.getInstance().getDisplayHeight()
                                                                      + "&sensor=false");
                conn.setRequestMethod(HttpConnection.GET);
                InputStream input = conn.openInputStream();
                Image img = Image.createImage(input);
                mapImage.setIcon(img);
                mapImageDialog.showPacked(BorderLayout.CENTER, true);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } else if (focussedName.equals(LINK_VIDEO_AUDIO)) {
            platformRequest(LINK_AUDIO_VIDEO_URL);
        } else if (focussedName.equals(LINK_OFFERS)) {
            platformRequest(LINK_OFFERS_URL);
        } else if (focussedName.equals(LINK_DRIVING_DIRECTIONS)) {
        } else {
            selectActionPerformed(focussedName);
        }
    }

    protected void platformRequest(final String url) {
        try {
            this.midlet.platformRequest(url);
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    protected abstract void addBasicInfo();

    protected abstract void addFormSpecificSections();

    private void addSections() {
        addLinksSection();
        addFormSpecificSections();
    }

    private void addLinksSection() {
        links = new Section(form, "", "");
        links.addComponents(UiUtil.getLink(LINK_MAPS));
        addMapSection();
        links.addComponents(UiUtil.getLink(LINK_VIDEO_AUDIO));
        links.addComponents(UiUtil.getLink(LINK_OFFERS));
        links.addComponents(UiUtil.getLink(LINK_DRIVING_DIRECTIONS));
        addDrivingDirectionsSection();
    }

    protected void addDrivingDirectionsSection() {
        drivingDirections = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        fromDriving = UiUtil.addTextFieldWithLabel(drivingDirections, "From");
        toDriving = UiUtil.addTextFieldWithLabel(drivingDirections, "To");
        getDrivingDirectionsButton = new Button("Go");
        getDrivingDirectionsButton.getStyle().setFont(UiUtil.getFont(Font.STYLE_PLAIN, Font.SIZE_SMALL));
        getDrivingDirectionsButton.getSelectedStyle().setFont(UiUtil.getFont(Font.STYLE_BOLD, Font.SIZE_SMALL));
        getDrivingDirectionsButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
            }
        });
        drivingDirections.addComponent(getDrivingDirectionsButton);
        links.addComponents(drivingDirections);
        hide(drivingDirections);
    }

    protected void addMapSection() {
        Container mapContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        mapImage = new Label();
        mapContainer.addComponent(mapImage);
        mapContainer.setScrollable(true);
        mapContainer.setScrollableX(true);
        mapContainer.setScrollableY(true);
        mapImageDialog = new Dialog();
        mapImageDialog.addComponent(mapContainer);
        mapImageDialog.addCommand(new Command("Ok"));
        mapImageDialog.setAutoDispose(true);
        mapImageDialog.setScrollable(true);
        mapImageDialog.setScrollableX(true);
        mapImageDialog.setScrollableY(true);
    }

    protected void hide(Component cmp) {
        cmp.setVisible(false);
        cmp.setPreferredH(0);
        cmp.setHeight(0);
    }

    protected void selectActionPerformed(String focussedName) {
    }

    protected abstract String getAddress();
}
