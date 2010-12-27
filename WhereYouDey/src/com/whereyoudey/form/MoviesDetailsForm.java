/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Label;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.UiUtil;
import javax.microedition.io.ConnectionNotFoundException;

/**
 *
 * @author Vikram S
 */
public class MoviesDetailsForm extends DetailsForm {

    public static final String LINK_SHOW_TIMES = "Show Times";
    private Label address1;
    private Label address2;
    private Label showTimesLink;
    private String showTimesLinkUrl;

    public MoviesDetailsForm(WhereYouDey midlet, ResultForm callingForm) {
        super(midlet, callingForm);
    }

    protected String getHeaderProperty() {
        return "CompanyName";
    }

    protected String getPhoneProperty() {
        return "";
    }

    protected void initResult(Result result) {
        final String street = result.getProperty("Street");
        final String area = result.getProperty("Area");
        final String city = result.getProperty("City");
        showTimesLinkUrl = result.getProperty("RSSURL");
        address1.setText(UiUtil.getCommaSepFormat(street, area));
        address2.setText(city);
    }

    protected void selectActionPerformed(String focussedName) {
        if (LINK_SHOW_TIMES.equals(focussedName)) {
            try {
                midlet.platformRequest(showTimesLinkUrl);
            } catch (ConnectionNotFoundException ex) {
                ex.printStackTrace();
            }
        }
    }

    protected void addBasicInfo() {
        address1 = addSmallFontLabel("");
        address2 = addSmallFontLabel("");
        showTimesLink = UiUtil.getLink(LINK_SHOW_TIMES);
        form.addComponent(showTimesLink);
    }

    protected void addFormSpecificSections() {
    }

    protected String getAddress() {
        final String street = result.getProperty("Street");
        final String area = result.getProperty("Area");
        final String city = result.getProperty("City");
        return UiUtil.getCommaSepFormat(UiUtil.getCommaSepFormat(street, area), city);
    }
}
