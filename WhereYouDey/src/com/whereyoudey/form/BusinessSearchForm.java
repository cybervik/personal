package com.whereyoudey.form;

import com.sun.lwuit.Component;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.FocusListener;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.Result;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.utils.TextFieldWithHistory;
import com.whereyoudey.webservice.ArrayOfString;

public class BusinessSearchForm extends SearchForm {

    private TextFieldWithHistory business;
    private TextFieldWithHistory area;
    private ListForm cityOptionsform;

    public BusinessSearchForm(WhereYouDey midlet) {
        super(midlet);
    }

    private void createCitiesOptionsForm() {
        if (cityOptionsform == null) {
            cityOptionsform = new ListForm(midlet, ListForm.CITIES, area.getInnerRepresentation());
        }
    }

    protected boolean isFormValid() {
        return !uiUtils.isEmpty(getSearchBusinessText());
    }

    protected void searchAction() {
        String businessText = getSearchBusinessText();
        String areaText = getSearchAreaText();
        ArrayOfString filter = new ArrayOfString();
        filter.setString(new String[]{"", "", "", "", "0", "10"});
        results = searchService.searchBusinessData(businessText, areaText, filter);
        business.updateHistory();
        area.updateHistory();
    }

    protected void addFormFields() {
        addBusinessTextField();
        addAreaTextField();
        addSelectCityLink();
    }

    private void addAreaTextField() {
        area = uiUtils.addTextFieldWithLabel(topContainer, "Area/City/State");
    }

    private void addBusinessTextField() {
        business = uiUtils.addTextFieldWithLabel(topContainer, "Businesses or Keywords");
    }

    private void addSelectCityLink() {
        Label chooseCityLink = uiUtils.getLink("or Choose City");
        chooseCityLink.setFocusable(true);
        chooseCityLink.addFocusListener(new FocusListener() {

            public void focusGained(Component cmpnt) {
                focussed = LINK_SELECT_CITY;
            }

            public void focusLost(Component cmpnt) {
                if (LINK_SELECT_CITY.equals(focussed)) {
                    focussed = "";
                }
            }
        });
        topContainer.addComponent(chooseCityLink);
    }

    void setAreaText(String area) {
        this.area.setText(area);
    }

    protected void moreActionPerformed() {
        if (LINK_SELECT_CITY.equals(focussed)) {
            createCitiesOptionsForm();
        }
    }

    public String getSearchBusinessText() {
        return this.business.getText().trim();
    }

    public String getSearchAreaText() {
        return this.area.getText().trim();
    }

    protected int getSelectedIconPos() {
        return 0;
    }

    protected void setFocus() {
        form.setFocused(business.getInnerRepresentation());
    }

    protected String getFormInvalidMessage() {
        return "Please enter business to search";
    }

    protected ResultForm getResultForm(Result[] results) {
        return new BusinessResultsForm(midlet, results, this);
    }
}
