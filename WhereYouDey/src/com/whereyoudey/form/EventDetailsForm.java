/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Label;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.form.helper.Section;
import com.whereyoudey.service.Result;
import com.whereyoudey.utils.UIUtils;

/**
 *
 * @author Vikram S
 */
public class EventDetailsForm extends DetailsForm {

    private Label date;
    private Label address1;
    private Label address2;
    private Label telephone;
    private Label venue;
    private Label website;
    private Section description;
    private Section keywords;
    private String primaryPhoneProperty;

    public EventDetailsForm(WhereYouDey midlet, ResultForm callingForm) {
        super(midlet, callingForm);
    }

    protected void addFormElements() throws NumberFormatException {
        date = addSmallFontLabel("");
        address1 = addSmallFontLabel("");
        address2 = addSmallFontLabel("");
        telephone = addSmallFontLabel("");
        venue = addSmallFontLabel("");
        website = addSmallFontLabel("");
        description = new Section(form, "Description", "");
        keywords = new Section(form, "Key Words", "");
    }

    protected String getHeaderProperty() {
        return "EventName";
    }

    protected String getPhoneProperty() {
        return primaryPhoneProperty;
    }

    protected void initResult(Result result) {
        final String date = result.getProperty("DatesAndTimes");
        final String street = result.getProperty("Street");
        final String area = result.getProperty("Area");
        final String city = result.getProperty("City");
        final String state = result.getProperty("State");
        final String telephone1 = result.getProperty("Telephone1");
        final String telephone2 = result.getProperty("Telephone2");
        final String venue = result.getProperty("Venue");
        final String keywords = result.getProperty("KeyWords");
        final String description = result.getProperty("Description");
        final String lattitude = result.getProperty("Lattitude");
        final String longitude = result.getProperty("Longitude");
        final String website = result.getProperty("Website");
        setPrimaryPhoneProperty(telephone1, telephone2);
        this.date.setText(date);
        this.venue.setText(venue);
        address1.setText(uiUtils.getCommaSepFormat(street, area));
        address2.setText(uiUtils.getCommaSepFormat(city, state));
        telephone.setText(uiUtils.getCommaSepFormat(telephone1, telephone2));
        this.website.setText(website);
        this.keywords.setDetails(keywords);
        this.description.setDetails(description);
    }

    private void setPrimaryPhoneProperty(final String telephone1, final String telephone2) {
        primaryPhoneProperty = (uiUtils.isEmpty(telephone1) ? "Telephone2" : "Telephone1");
    }
}
