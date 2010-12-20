/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BoxLayout;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.form.component.Section;
import com.whereyoudey.service.helper.Result;

/**
 *
 * @author Vikram S
 */
public class BusinessDetailsForm extends DetailsForm {

    private Label address;
    private Label phoneNumber;
    private Container ratingContainer;
    private Section hoursOfOperation;
    private Section description;
    private Section productsInformation;
    private Section pricingInformation;
    private Section businessCategory;
    private Section additionalInformation;
    private Section keyWords;
    private Label state;
    private Label city;

    public BusinessDetailsForm(WhereYouDey midlet, ResultForm callingForm) {
        super(midlet, callingForm);
    }

    protected void addFormElements() throws NumberFormatException {
        addBasicInfo();
        setRating("0");
        addSections();
    }

    private void addBasicInfo() {
        address = addSmallFontLabel("");
        city = addSmallFontLabel("");
        state = addSmallFontLabel("");
        phoneNumber = addBigFontLabel("");
        ratingContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        form.addComponent(ratingContainer);
    }

    private void addSections() {
        hoursOfOperation = new Section(form, "Hours Of Operations", "");
        description = new Section(form, "DESCRIPTION", "");
        productsInformation = new Section(form, "Products Information", "");
        pricingInformation = new Section(form, "Pricing Information", "");
        businessCategory = new Section(form, "Business Category", "");
        additionalInformation = new Section(form, "Additional Information", "");
        keyWords = new Section(form, "Keywords", "");
    }

    private void setRating(final String ratingStr) throws NumberFormatException {
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

    protected void initResult(Result result) {
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
        this.address.setText(address);
        this.city.setText(city);
        this.state.setText(state);
        this.phoneNumber.setText(phone);
        setRating(ratingStr);
        this.description.setDetails(description);
        this.businessCategory.setDetails(category);
        this.productsInformation.setDetails(prodServices);
        this.keyWords.setDetails(keyWords);
        this.additionalInformation.setDetails(business);
    }

    protected String getHeaderProperty() {
        return "Name";
    }

    protected String getPhoneProperty() {
        return "Phone";
    }

}
