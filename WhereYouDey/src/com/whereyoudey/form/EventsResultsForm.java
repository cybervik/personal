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
import com.whereyoudey.utils.FontUtil;
import com.whereyoudey.utils.UiUtil;

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
        result.addProperty("_Type", "Event");
        final String eventName = result.getProperty("EventName");
        final String date = result.getProperty("DatesAndTimes");
        final String street = result.getProperty("Street");
        final String area = result.getProperty("Area");
        final String city = result.getProperty("City");
        final String state = result.getProperty("State");
        final String telephone1 = result.getProperty("Telephone1");
        final String telephone2 = result.getProperty("Telephone2");
        final String venue = result.getProperty("Venue");
        final String category = result.getProperty("CategoryName");
        setPrimaryPhoneProperty(telephone1, telephone2);
        UiUtil.add(itemContainer, eventName, true);
//        UiUtil.add(itemContainer, date);
        UiUtil.add(itemContainer, "Venue: " + venue);
//        UiUtil.add(itemContainer, UiUtil.getCommaSepFormat(street, area));
        itemContainer.addComponent(new WrappingLabel(UiUtil.getCommaSepFormat(city, state), FontUtil.getSmallNormalFont()));
//        UiUtil.add(itemContainer, UiUtil.getCommaSepFormat(city, state));
        UiUtil.add(itemContainer, "Category:" + category);
//        UiUtil.add(itemContainer, UiUtil.getCommaSepFormat(telephone1, telephone2));
    }

    private void setPrimaryPhoneProperty(final String telephone1, final String telephone2) {
        primaryPhoneProperty = (UiUtil.isEmpty(telephone1) ? "Telephone2" : "Telephone1");
    }

    protected void addFormSpecificCommands() {
        form.addCommand(new Command(OPTION_SORT_BY_AREA));
    }

    protected String getPhoneNumberProperty() {
        return primaryPhoneProperty;
    }

    protected void handleFormSpecificCommandAction(String commandName) {
        if (commandName.equals(OPTION_SORT_BY_AREA)) {
            SortUtil.sort("Area", results, SortUtil.SORT_ORDER_ASCENDING);
            initProcessedResults(results);
        }
    }

    protected DetailsForm getDetailsForm() {
        return new EventDetailsForm(midlet, this);
    }

    protected String getTitleProperty() {
        return "EventName";
    }

    protected String getTitle() {
        EventsSearchForm form = (EventsSearchForm) callingForm;
        return "Events near " + form.city.getText();
    }
}
