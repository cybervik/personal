/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.TextField;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.UiUtil;

/**
 *
 * @author Vikram S
 */
class EventsSearchForm extends SearchForm {

    private TextField city;
    private String OPTION_HOME = "Home";

    EventsSearchForm(WhereYouDey midlet) {
        super(midlet);
    }

    protected void addFormFields() {
        UiUtil.addBoldMediumFontLabel(topContainer, "Search Events");
        city = UiUtil.addTextFieldWithLabel(topContainer, "City");
        addSelectCityLink();
        city.setText("Port Harcourt");
    }

    protected void moreSelectActionPerformed() {
    }

    protected boolean isFormValid() {
        return !UiUtil.isEmpty(city.getText().trim());
    }

    protected void searchAction() {
        results = searchService.searchEvents(city.getText().trim());
    }

    protected int getSelectedIconPos() {
        return 2;
    }

    protected void setFocus() {
        form.setFocused(city);
    }

    protected String getFormInvalidMessage() {
        return "Please enter city to search";
    }

    protected ResultForm getResultForm(Result[] results) {
        return new EventsResultsForm(midlet, results, this);
    }

    protected TextField getCityDependentField() {
        return city;
    }

    protected void addFormSpecificCommands() {
        form.addCommand(new Command(OPTION_HOME));
    }

    protected void moreActionPerformed(String commandName) {
        if (OPTION_HOME.equals(commandName)) {
            midlet.getSearchForm().show();
        }
    }
}
