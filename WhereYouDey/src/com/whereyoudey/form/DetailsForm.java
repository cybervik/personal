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
import com.whereyoudey.service.Result;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.midlet.MIDletStateChangeException;

/**
 *
 * @author Vikram S
 */
class DetailsForm implements ActionListener {

    private Form form;
    private Label header;
    private Label address;
    private Label phoneNumber;
    private Container ratingContainer;
    private final WhereYouDey midlet;
    private final Section hoursOfOperation;
    private final Section description;
    private final Section productsInformation;
    private final Section pricingInformation;
    private final Section businessCategory;
    private final Section additionalInformation;
    private final Section keyWords;
    private Result result;
    private final Label state;
    private final Label city;

    public DetailsForm(WhereYouDey midlet) {
        form = new Form();
        form.setWidth(Display.getInstance().getDisplayWidth());
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        addHeader();
        address = addSmallFontLabel("");
        city = addSmallFontLabel("");
        state = addSmallFontLabel("");
        phoneNumber = addBigFontLabel("");
        ratingContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        form.addComponent(ratingContainer);
        addRating("0");
        hoursOfOperation = new Section(form, "Hours Of Operations", "");
        description = new Section(form, "DESCRIPTION", "");
        productsInformation = new Section(form, "Products Information", "");
        pricingInformation = new Section(form, "Pricing Information", "");
        businessCategory = new Section(form, "Busienss Category", "");
        additionalInformation = new Section(form, "Additional Information", "");
        keyWords = new Section(form, "Keywords", "");
        form.addCommand(new Command("Back"));
        form.addCommand(new Command("Exit"));
        form.addCommand(new Command("Home"));
        form.addCommand(new Command("Call"));
        form.addCommandListener(this);
        form.show();
        this.midlet = midlet;
    }

    private Label addBigFontLabel(final String ph) {
        Label phoneNumber = new Label(ph);
        Font bigFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        phoneNumber.getStyle().setFont(bigFont);
        form.addComponent(phoneNumber);
        return phoneNumber;
    }

    private void addHeader() {
        header = new Label("Heloo");
        header.getStyle().setBgColor(0x000000);
        header.getStyle().setFgColor(0xffffff);
        form.addComponent(header);
    }

    private Label addSmallFontLabel(final String txt) {
        Label label = new Label(txt);
        setSmallFont(label);
        form.addComponent(label);
        return label;
    }

    private void setSmallFont(Component comp) {
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        comp.getStyle().setFont(smallFont);
    }

    private Image getImage(String imagePath, int imageWidth) {
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

    private void addRating(final String ratingStr) throws NumberFormatException {
        int rating = Integer.parseInt(ratingStr);
        ratingContainer.removeAll();
        for (int j = 1; j <= rating; j++) {
            Label ratingIcon = new Label(getImage("/img/rating_mark.png", 5));
            ratingContainer.addComponent(ratingIcon);
        }
        for (int j = rating + 1; j <= 5; j++) {
            Label ratingIcon = new Label(getImage("/img/rating_empty.png", 5));
            ratingContainer.addComponent(ratingIcon);
        }
    }

    void init(Result result) {
        this.result = result;
        final String bizName = result.getProperty("Name");
        final String address = result.getProperty("Address");
        final String street = result.getProperty("Street");
        final String area = result.getProperty("Area");
        final String city = result.getProperty("City");
        final String state = result.getProperty("State");
        final String phone = result.getProperty("Phone");
        final String ratingStr = result.getProperty("StarReview");
        final String description = result.getProperty("Description");
        final String category = result.getProperty("Category");
        final String business = result.getProperty("Business");
        final String prodServices = result.getProperty("ProdServices");
        final String keyWords = result.getProperty("KeyWords");
        header.setText(bizName);
        this.address.setText(address);
        this.city.setText(city);
        this.state.setText(state);
        this.phoneNumber.setText(phone);
        addRating(ratingStr);
        this.description.setDetails(description);
        this.businessCategory.setDetails(category);
        this.productsInformation.setDetails(prodServices);
        this.keyWords.setDetails(keyWords);
        this.additionalInformation.setDetails(business);
        form.show();
    }

    public void actionPerformed(ActionEvent ae) {
        final Command command = ae.getCommand();
        final String commandName = command.getCommandName();
        if ("Exit".equals(commandName)) {
            try {
                midlet.destroyApp(true);
                midlet.notifyDestroyed();
            } catch (MIDletStateChangeException ex) {
                ex.printStackTrace();
            }
        } else if ("Home".equals(commandName)) {
            midlet.getSearchForm().show();
        } else if ("Call".equals(commandName)) {
            try {
                if (result == null) {
                    showDialog("No data found");
                    return;
                }
                final String phoneNumber = result.getProperty("Phone");
                if ("".equals(phoneNumber.trim())) {
                    showDialog("Phone number not found in this result.");
                } else {
                    midlet.platformRequest("tel:" + phoneNumber);
                }
            } catch (ConnectionNotFoundException ex) {
                ex.printStackTrace();
            }
        } else if ("Back".equals(commandName)) {
            midlet.getSearchForm().getResultForm().show();
        }
    }

    private void showDialog(String message) {
        Dialog invalidPhoneWarning = new Dialog();
        invalidPhoneWarning.setWidth(form.getWidth());
        invalidPhoneWarning.show(null, message, "Ok", null);
    }
}
