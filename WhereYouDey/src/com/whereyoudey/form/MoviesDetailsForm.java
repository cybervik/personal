/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.nokia.mid.impl.isa.bluetooth.URLParser;
import com.sun.lwuit.Label;
import com.sun.midp.io.HttpUrl;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.UiUtil;
import java.util.Enumeration;
import java.util.Hashtable;

/**
 *
 * @author Vikram S
 */
public class MoviesDetailsForm extends BusinessDetailsForm {
//

    public static final String LINK_SHOW_TIMES = "Show Times";
//    private Label address1;
//    private Label address2;
//    private Label showTimesLink;
    private String showTimesLinkUrl;
//

    public MoviesDetailsForm(WhereYouDey midlet, ResultForm callingForm) {
        super(midlet, callingForm);
    }

    protected void addAddtionalLinks() {
        addLink(LINK_SHOW_TIMES);
    }

//
//    protected String getHeaderProperty() {
//        return "CompanyName";
//    }
//
//    protected String getPhoneProperty() {
//        return "";
//    }
//
    protected void initResult(Result result) {
        super.initResult(result);
        showTimesLinkUrl = result.getProperty("RSSURL");
//        showTimesLinkUrl = "http://google.com/hello/world?antida=:1:2+crap/&hello=name";
        log("Original URL -> " + showTimesLinkUrl);
        showTimesLinkUrl = UiUtil.urlEncode(showTimesLinkUrl, true);
        log("Encoded Url -> " + showTimesLinkUrl);
    }

    private void log(final String txt) {
        System.out.println(txt);
    }
//

    protected void selectActionPerformed(String focussedName) {
        if (LINK_SHOW_TIMES.equals(focussedName)) {
            midlet.requestPlatformService(showTimesLinkUrl);
        }
    }
//
//    protected void addBasicInfo() {
//        address1 = addSmallFontLabel("");
//        address2 = addSmallFontLabel("");
//        showTimesLink = UiUtil.getLink(LINK_SHOW_TIMES);
//        form.addComponent(showTimesLink);
//    }
//
//    protected void addFormSpecificSections() {
//    }
//
//    protected String getAddress() {
//        final String street = result.getProperty("Street");
//        final String area = result.getProperty("Area");
//        final String city = result.getProperty("City");
//        return UiUtil.getCommaSepFormat(UiUtil.getCommaSepFormat(street, area), city);
//    }
}
