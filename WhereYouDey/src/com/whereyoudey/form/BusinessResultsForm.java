/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.whereyoudey.utils.SortUtil;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.UiUtil;

/**
 *
 * @author Vikram S
 */
public class BusinessResultsForm extends ResultForm {

    BusinessResultsForm(WhereYouDey midlet, Result[] results, SearchForm callingForm) {
        super(midlet, results, callingForm);
    }

    protected String getInitialSortProperty() {
        return "ReviewCount";
    }

    protected void renderResult(Result result, Container itemContainer) throws NumberFormatException {
        String bizName = result.getProperty("Name");
        final String address = result.getProperty("Address");
        final String city = result.getProperty("City");
        final String state = result.getProperty("State");
        final String phone = result.getProperty("Phone");
        final String ratingStr = result.getProperty("StarReview");
        final String reviewCountStr = result.getProperty("ReviewCount");
        try {
            int reviewCount = Integer.parseInt(reviewCountStr);
            for (int j = 0; j < reviewCount; j++) {
                bizName += "*";
            }
        } catch (Exception e) {
        }
        UiUtil.addBoldSmallFontLabel(itemContainer, bizName);
        UiUtil.addSmallFontLabel(itemContainer, address);
        UiUtil.addSmallFontLabel(itemContainer, city + ", " + state);
        UiUtil.addSmallFontLabel(itemContainer, phone);
        UiUtil.addRating(itemContainer, ratingStr);
    }

    protected void addFormSpecificCommands() {
        form.addCommand(new Command(OPTION_SORT_BY_RELEVANCE));
        form.addCommand(new Command(OPTION_SORT_BY_CITY));
        form.addCommand(new Command(OPTION_SORT_BY_AREA));
        form.addCommand(new Command(OPTION_FILTER_BY_VIDEOS));
    }

    protected String getPhoneNumberProperty() {
        return "Phone";
    }

    protected void handleFormSpecificCommandAction(String commandName) {
        if (commandName.equals(OPTION_SORT_BY_RELEVANCE)) {
            initResults(results);
        } else if (commandName.equals(OPTION_SORT_BY_CITY)) {
            SortUtil.sort("City", results, SortUtil.SORT_ORDER_ASCENDING);
            initProcessedResults(results);
        } else if (commandName.equals(OPTION_SORT_BY_AREA)) {
            SortUtil.sort("Area", results, SortUtil.SORT_ORDER_ASCENDING);
            initProcessedResults(results);
        } else if (commandName.equals(OPTION_FILTER_BY_VIDEOS)) {
            filter("VideoName");
        }
    }

    protected DetailsForm getDetailsForm() {
        return new BusinessDetailsForm(midlet, this);
    }
}
