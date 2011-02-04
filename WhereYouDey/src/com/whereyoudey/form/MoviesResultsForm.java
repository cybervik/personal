/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.DialogUtil;
import com.whereyoudey.utils.SortUtil;
import com.whereyoudey.utils.UiUtil;
import javax.microedition.io.ConnectionNotFoundException;

/**
 *
 * @author Vikram S
 */
class MoviesResultsForm extends ResultForm {

    public MoviesResultsForm(WhereYouDey midlet, Result[] results, SearchForm callingForm) {
        super(midlet, results, callingForm);
    }

    protected DetailsForm getDetailsForm() {
        return new MoviesDetailsForm(midlet, this);
    }

    protected void addFormSpecificCommands() {
        form.addCommand(new Command(OPTION_SORT_BY_AREA));
    }

    protected String getPhoneNumberProperty() {
        return "";
    }

    protected String getInitialSortProperty() {
        return "CompanyName";
    }

    protected void renderResult(Result result, Container itemContainer) throws NumberFormatException {
        result.addProperty("_Type","Movie");
        final String companyName = result.getProperty("CompanyName");
        final String street = result.getProperty("Street");
        final String area = result.getProperty("Area");
        final String city = result.getProperty("City");
        UiUtil.add(itemContainer, companyName, true);
        UiUtil.add(itemContainer, UiUtil.getCommaSepFormat(street, area));
        UiUtil.add(itemContainer, city);
    }

    protected void handleFormSpecificCommandAction(String commandName) {
        if (commandName.equals(OPTION_SORT_BY_AREA)) {
            SortUtil.sort("Area", results, SortUtil.SORT_ORDER_ASCENDING);
            initProcessedResults(results);
        }
    }

    protected String getTitleProperty() {
        return "CompanyName";
    }

    protected String getTitle() {
        final MoviesSearchForm form = (MoviesSearchForm) callingForm;
        return "Movies near " + form.city.getText();
    }

//    protected void handleSelect() {
//        String url = resultsList.getSelectedItemResultRecord().getProperty("RSSURL");
//        if (!UiUtil.isEmpty(url)) {
//            if (!url.trim().startsWith("http://")) {
//                url = "http://" + url.trim();
//            }
//            midlet.requestPlatformService(url);
//        } else {
//            DialogUtil.showInfo("Error", "Movie listing does not have an url.");
//        }
//    }
}
