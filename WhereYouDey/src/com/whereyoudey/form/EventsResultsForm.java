/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.Result;
import com.whereyoudey.utils.UIUtils;

/**
 *
 * @author Vikram S
 */
public class EventsResultsForm extends ResultForm {

    private String primaryPhoneProperty;

    public EventsResultsForm(WhereYouDey midlet, Result[] results, SearchForm callingForm) {
        super(midlet, results, callingForm);
    }

    protected String getInitialSortProperty() {
        return "EventName";
    }

    protected void renderResult(Result result, Container itemContainer) throws NumberFormatException {
        final String eventName = result.getProperty("EventName");
        final String date = result.getProperty("DatesAndTimes");
        final String street = result.getProperty("Street");
        final String area = result.getProperty("Area");
        final String city = result.getProperty("City");
        final String state = result.getProperty("State");
        final String telephone1 = result.getProperty("Telephone1");
        final String telephone2 = result.getProperty("Telephone2");
        setPrimaryPhoneProperty(telephone1, telephone2);
        show(eventName, itemContainer, true);
        show(date, itemContainer);
        show(uiUtils.getCommaSepFormat(street, area), itemContainer);
        show(uiUtils.getCommaSepFormat(city, state), itemContainer);
        show(uiUtils.getCommaSepFormat(telephone1, telephone2), itemContainer);
    }



    private void setPrimaryPhoneProperty(final String telephone1, final String telephone2) {
        primaryPhoneProperty = (uiUtils.isEmpty(telephone1) ? "Telephone2" : "Telephone1");
    }

    protected void addFormSpecificCommands() {
        form.addCommand(new Command(OPTION_SORT_BY_AREA));
    }

    protected String getPhoneNumberProperty() {
        return primaryPhoneProperty;
    }

    protected void handleFormSpecificCommandAction(String commandName) {
        if (commandName.equals(OPTION_SORT_BY_AREA)) {
            sort("Area", results, SORT_ORDER_ASCENDING);
            initProcessedResults(results);
        }
    }

    protected DetailsForm getDetailsForm() {
        return new EventDetailsForm(midlet, this);
    }
}
