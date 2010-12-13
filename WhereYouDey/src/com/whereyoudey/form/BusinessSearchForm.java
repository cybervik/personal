package com.whereyoudey.form;

import com.sun.lwuit.Component;


import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.FocusListener;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.Result;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.webservice.ArrayOfString;

public class BusinessSearchForm extends SearchForm {

    private ResultForm resultForm;
    private TextField business;
    private TextField area;
    private ListForm cityOptionsform;

    public BusinessSearchForm(WhereYouDey midlet) {
        super(midlet);
    }

    private void createCitiesOptionsForm() {
        if (cityOptionsform == null) {
            cityOptionsform = new ListForm(midlet, ListForm.CITIES, area);
        }
    }

    protected boolean isFormValid() {
        String businessText = getSearchBusinessText();
        if (uiUtils.isEmpty(businessText)) {
            uiUtils.showDialog("Please enter business to search");
            return false;
        }
        return true;
    }

    protected void searchAction() {
        String businessText = getSearchBusinessText();
        String areaText = getSearchAreaText();
        SearchService searchService = new SearchService();
        ArrayOfString filter = new ArrayOfString();
        filter.setString(new String[]{"", "", "", "", "0", "10"});
        Result[] results = searchService.search(businessText, areaText, filter);
        hideWait();
        showResultForm(results);
    }

    private void showResultForm(Result[] results) throws NumberFormatException {
        if (resultForm == null) {
            resultForm = new ResultForm(midlet, results);
        } else {
            resultForm.initResults(results);
        }
    }

    protected void addFormFields() {
        addBusinessTextField();
        addAreaTextField();
        addSelectCityLink();
    }

    private void addAreaTextField() {
        area = uiUtils.addTextFieldWithLabel(midlet, topContainer, "Area/City/State");
    }

    private void addBusinessTextField() {
        business = uiUtils.addTextFieldWithLabel(midlet, topContainer, "Businesses or Keywords");
        form.setFocused(business);
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

    public ResultForm getResultForm() {
        return resultForm;
    }

    public String getSearchBusinessText() {
        return this.business.getText().trim();
    }

    public String getSearchAreaText() {
        return this.area.getText().trim();
    }
}
