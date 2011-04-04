/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Label;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.form.component.Section;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.FontUtil;
import com.whereyoudey.utils.UiUtil;

/**
 *
 * @author Vikram S
 */
public class EventDetailsForm extends DetailsForm {

    private Section date;
    private WrappingLabel address1;
    private WrappingLabel address2;
    private WrappingLabel telephone;
    private Label website;
    private Section description;
    private Section venue;
    private Section keywords;
    private String primaryPhoneProperty;
    private Section category;

    public EventDetailsForm(WhereYouDey midlet, ResultForm callingForm) {
        super(midlet, callingForm);
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
        final String description = result.getProperty("Description") + "\n" + result.getProperty("GeneralInfoP1") + "\n" + result.getProperty("GeneralInfoP2");
        final String lattitude = result.getProperty("Lattitude");
        final String longitude = result.getProperty("Longitude");
        final String website = result.getProperty("Website");
        final String category = result.getProperty("CategoryName");
        setPrimaryPhoneProperty(telephone1, telephone2);
        this.date.setDetails(date);
        this.venue.setDetails(venue);
        address1.setText(UiUtil.getCommaSepFormat(street, area), FontUtil.getSmallNormalFont());
        address2.setText(UiUtil.getCommaSepFormat(city, state), FontUtil.getSmallNormalFont());
        telephone.setText(UiUtil.getCommaSepFormat(telephone1, telephone2), FontUtil.getSmallNormalFont());
        this.website.setText(website);
        this.keywords.setDetails(keywords);
        this.description.setDetails(description);
        this.category.setDetails(category);
        this.videoUrl = result.getProperty("YouTubeVideoLink");
    }

    private void setPrimaryPhoneProperty(final String telephone1, final String telephone2) {
        primaryPhoneProperty = (UiUtil.isEmpty(telephone1) ? "Telephone2" : "Telephone1");
    }

    protected void addBasicInfo() {
        address1 = new WrappingLabel("", FontUtil.getSmallNormalFont());
        form.addComponent(address1);
        address2 = new WrappingLabel("", FontUtil.getSmallNormalFont());
        form.addComponent(address2);
        //date = addSmallFontLabel("");
        //venue = addSmallFontLabel("");
        //category = addSmallFontLabel("");
        telephone = new WrappingLabel("", FontUtil.getSmallNormalFont());
        form.addComponent(telephone);
        website = UiUtil.getLink(" ");//addSmallFontLabel("");
        form.addComponent(website);
    }

    protected void addFormSpecificSections() {
        description = new Section(form, "Description", "");
        venue = new Section(form, "Venue Details", "");
        date = new Section(form, "Date and Time", "");
        category = new Section(form, "Event Category", "");
        keywords = new Section(form, "Keywords", "");
    }

    protected String getAddress() {
        final String street = result.getProperty("Street");
        final String area = result.getProperty("Area");
        final String city = result.getProperty("City");
        return UiUtil.getCommaSepFormat(UiUtil.getCommaSepFormat(street, area), city);
    }

    protected void selectActionPerformed(String focussedName) {
        if (focussedName.equals(website.getText())) {
            midlet.requestPlatformService(website.getText());
        }
    }
}
