/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.whereyoudey.form.component.Header;
import com.whereyoudey.utils.SortUtil;
import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Form;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.form.component.SearchResultsContainer;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.UiUtil;
import javax.microedition.io.ConnectionNotFoundException;

/**
 *
 * @author Vikram S
 */
public abstract class ResultForm implements ActionListener {

    private static final String OPTION_BACK = "Back";
    private static final String OPTION_CALL = "Call";
    private static final String OPTION_EXIT = "Exit";
    private static final String OPTION_HELP = "Help";
    private static final String OPTION_HOME = "Home";
    private static final String OPTION_SELECT = "Select";
    protected static final String OPTION_FILTER_BY_VIDEOS = "Filter by videos";
    protected static final String OPTION_SORT_BY_AREA = "Sort by area";
    protected static final String OPTION_SORT_BY_CITY = "Sort by city";
    protected static final String OPTION_SORT_BY_RELEVANCE = "Sort by relevance";
    protected WhereYouDey midlet;
    protected Form form;
    protected Result[] results;
    private final SearchForm callingForm;
    private Header header;
    private int MAX_RESULTS = 10;
    private SearchResultsContainer resultsList;
    private DetailsForm detailsForm;

    ResultForm(WhereYouDey midlet, Result[] results, SearchForm callingForm) {
        initVariables(midlet);
        initForm();
        addHeader();
        addResultsSection(results);
        addCommands();
        this.callingForm = callingForm;
        form.show();
    }

    protected abstract DetailsForm getDetailsForm();

    private void addCommands() {
        form.addCommand(new Command(OPTION_EXIT));
        form.addCommand(new Command(OPTION_BACK));
        form.addCommand(new Command(OPTION_HOME));
        form.addCommand(new Command(OPTION_HELP));
        addFormSpecificCommands();
        form.addCommand(new Command(OPTION_CALL));
        form.addCommand(new Command(OPTION_SELECT));
        form.addCommandListener(this);
    }

    protected abstract void addFormSpecificCommands();

    protected abstract String getPhoneNumberProperty();

    private void call() {
        String phoneNumber = getSelectedItemPhoneNumber();
        try {
            if (phoneNumber.trim().equals("")) {
                UiUtil.showDialog("Phone number not found in this result.");
            } else {
                midlet.platformRequest("tel:" + phoneNumber);
            }
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private String getSelectedItemPhoneNumber() {
        Result selectedItem = resultsList.getSelectedItemResultRecord();
        return selectedItem.getProperty(getPhoneNumberProperty());
    }

    private void handleKeyEventAction(ActionEvent ae) {
        final int keyEvent = ae.getKeyEvent();
        switch (keyEvent) {
            case -1:
                resultsList.selectItemUp();
                break;
            case -2:

                resultsList.selectItemDown();
                break;
        }
    }

    private void setTitle(Container selectedItem) {
//        if (isAdvertisement()) {
//            header.setText("Advertisement Space");
//        } else {
//            final Component title = selectedItem.getComponentAt(0);
//            header.setText(((Label) title).getText());
//        }
    }

    private void showDetailsForm() {
        if (detailsForm == null) {
            detailsForm = getDetailsForm();
        }
        detailsForm.init(resultsList.getSelectedItemResultRecord());
    }

    protected abstract String getInitialSortProperty();

    private void goBack() {
        callingForm.show();
    }

    protected void initProcessedResults(Result[] results) throws NumberFormatException {
        this.results = results;
        resultsList.reset();
        for (int i = 0; i < results.length; i++) {
            Result result = results[i];
            if (result != null) {
                Container itemContainer = new ResultItem(result);
                renderResult(result, itemContainer);
                resultsList.addComponent(itemContainer);
            }
        }
        header.setResultCount(resultsList.getCount());
        form.show();
    }

    protected abstract void renderResult(Result result, Container itemContainer) throws NumberFormatException;

    private void initVariables(WhereYouDey midlet) {
        this.midlet = midlet;
    }

    private void initForm() {
        form = new Form();
        form.setScrollableY(false);
        form.setLayout(new BorderLayout());
        form.addKeyListener(-1, this);
        form.addKeyListener(-2, this);
        form.setScrollableX(true);
    }

    private void addHeader() {
        header = new Header(form);
    }

    private void addResultsSection(Result[] results) throws NumberFormatException {
        resultsList = new SearchResultsContainer();
        form.addComponent(BorderLayout.CENTER, resultsList);
        form.setFocused(resultsList);
        initResults(results);
    }

    public void initResults(Result[] results) throws NumberFormatException {
        SortUtil.sort(getInitialSortProperty(), results, SortUtil.SORT_ORDER_DESCENDING);
        initProcessedResults(results);
    }

    public void actionPerformed(ActionEvent ae) {
        final Command command = ae.getCommand();
        if (command != null) {
            handleCommandAction(command);
        } else {
            handleKeyEventAction(ae);
        }
        ae.consume();
    }

    void show() {
        form.show();
    }

    protected void filter(String filterProperty) {
        Result[] filteredResults = new Result[MAX_RESULTS];
        int j = 0;
        for (int i = 0; i < results.length; i++) {
            Result result = results[i];
            if (!UiUtil.isEmpty(result.getProperty(filterProperty))) {
                filteredResults[j] = result;
                j++;
            }
        }
        initProcessedResults(filteredResults);
    }

    private void handleCommandAction(Command command) {
        final String commandName = command.getCommandName();
        if (commandName.equals(OPTION_BACK)) {
            goBack();
        } else if (commandName.equals(OPTION_EXIT)) {
            midlet.exit();
        } else if (commandName.equals(OPTION_HELP)) {
            UiUtil.showHelp();
        } else if (commandName.equals(OPTION_HOME)) {
            goBack();
        } else if (commandName.equals(OPTION_SELECT)) {
            showDetailsForm();
        } else if (commandName.equals(OPTION_CALL)) {
            call();
        } else {
            handleFormSpecificCommandAction(commandName);
        }
    }

    protected abstract void handleFormSpecificCommandAction(String commandName);
}
