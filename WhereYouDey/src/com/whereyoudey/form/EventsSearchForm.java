/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.TextField;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.Result;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.utils.TextFieldWithHistory;

/**
 *
 * @author Vikram S
 */
class EventsSearchForm extends SearchForm {

    private TextFieldWithHistory city;

    EventsSearchForm(WhereYouDey midlet) {
        super(midlet);
    }

    protected void addFormFields() {
        uiUtils.addBoldFontLabel(topContainer, "Search Events");
        city = uiUtils.addTextFieldWithLabel(topContainer, "City");
        city.setText("Port Harcourt");
    }

    protected void moreActionPerformed() {
    }

    protected boolean isFormValid() {
        return !uiUtils.isEmpty(city.getText().trim());
    }

    protected void searchAction() {
        results = searchService.searchEvents(city.getText().trim());
        city.updateHistory();
    }

    protected int getSelectedIconPos() {
        return 2;
    }

    protected void setFocus() {
        form.setFocused(city.getInnerRepresentation());
    }

    protected String getFormInvalidMessage() {
        return "Please enter city to search";
    }

    protected ResultForm getResultForm(Result[] results) {
        return new EventsResultsForm(midlet, results, this);
    }
}
