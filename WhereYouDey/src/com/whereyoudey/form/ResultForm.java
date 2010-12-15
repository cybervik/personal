/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.form.helper.ResultContainer;
import com.whereyoudey.service.Result;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.utils.UIUtils;
import com.whereyoudey.webservice.ArrayOfString;
import java.io.IOException;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;

/**
 *
 * @author Vikram S
 */
public abstract class ResultForm implements ActionListener {

    public static final int COLOR_ADV_BACKGROUND = 0xffa500;
    public static final int COLOR_BLACK = 0x000000;
    public static final int COLOR_WHITE = 0xffffff;
    public static final int COLOR_SELECTEDITEM_BACKGROUND = 0x9999ff;
    public static final String OPTION_BACK = "Back";
    public static final String OPTION_CALL = "Call";
    public static final String OPTION_EXIT = "Exit";
    public static final String OPTION_FILTER_BY_VIDEOS = "Filter by videos";
    public static final String OPTION_HELP = "Help";
    public static final String OPTION_HOME = "Home";
    public static final String OPTION_SELECT = "Select";
    public static final String OPTION_SORT_BY_AREA = "Sort by area";
    public static final String OPTION_SORT_BY_CITY = "Sort by city";
    public static final String OPTION_SORT_BY_RELEVANCE = "Sort by relevance";
    public static final String SORT_ORDER_ASCENDING = "ASC";
    public static final String SORT_ORDER_DESCENDING = "DSC";
    protected WhereYouDey midlet;
    protected Form form;
    protected Result[] results;
    private Label header;
    private Label resultCounter;
    private int selectItemPos = -1;
    private ResultContainer resultContainer;
    private int resultCount;
    private DetailsForm detailsForm;
    private int MAX_RESULTS = 10;
    protected UIUtils uiUtils;
    private final SearchForm callingForm;

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
        form.addCommand(new Command(OPTION_BACK));
        form.addCommand(new Command(OPTION_SELECT));
        form.addCommand(new Command(OPTION_HELP));
        form.addCommand(new Command(OPTION_HOME));
        form.addCommand(new Command(OPTION_EXIT));
        form.addCommand(new Command(OPTION_CALL));
        form.addCommandListener(this);
        addFormSpecificCommands();
    }

    protected abstract void addFormSpecificCommands();

    protected abstract String getPhoneNumberProperty();

    private void handleKeyEventAction(ActionEvent ae) {
        final int keyEvent = ae.getKeyEvent();
        switch (keyEvent) {
            case -1:
                deSelectItem();
                selectItemPos = (selectItemPos == 0) ? selectItemPos : selectItemPos - 1;
                selectItemUp();
                break;
            case -2:
                deSelectItem();
                selectItemPos = (selectItemPos == (resultCount - 1)) ? selectItemPos : selectItemPos + 1;
                selectItemDown();
                break;
        }
        System.out.println("Key Pressed - " + keyEvent + " - " + selectItemPos);
    }

    private DetailsForm showDetailsForm(Result result) {
        if (detailsForm == null) {
            detailsForm = getDetailsForm();
        }
        detailsForm.init(results[selectItemPos]);
        return detailsForm;
    }

    protected abstract String getInitialSortProperty();

    private void goBack() {
        callingForm.show();
    }

    protected void initProcessedResults(Result[] results) throws NumberFormatException {
        this.results = results;
        this.resultCount = 0;
        this.selectItemPos = -1;
        this.resultContainer.removeAll();
        for (int i = 0; i < results.length; i++) {
            Result result = results[i];
            if (result != null) {
                resultCount++;
                Container itemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                renderResult(result, itemContainer);
                if ((resultCount % 3) == 0) {
                    addAdvLabel(itemContainer);
                }
                resultContainer.addComponent(itemContainer);
                if (selectItemPos == -1) {
                    selectItemPos = 0;
                    selectItem();
                }
                itemContainer.getStyle().setBgPainter(new Painter() {

                    public void paint(Graphics g, Rectangle r) {
                        g.setColor(COLOR_BLACK);
                        g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), 1);
                    }
                });
            }
        }
        if (resultCount > 0) {
            resultCounter.setText("Result 1 - " + resultCount);
            selectItemUp();
        } else {
            resultCounter.setText("No results found");
        }
        form.show();
    }

    protected abstract void renderResult(Result result, Container itemContainer) throws NumberFormatException;

    private void initVariables(WhereYouDey midlet) {
        this.midlet = midlet;
        this.selectItemPos = -1;
        this.uiUtils = new UIUtils();
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
        final Container headerContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        header = new Label();
        header.setIcon(getImage("/img/small_logo.png", 20));
        header.getStyle().setBgColor(0x000000);
        header.getStyle().setFgColor(0xffffff);
        headerContainer.addComponent(header);
        resultCounter = new Label();
        Font mediumFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        resultCounter.getStyle().setFont(mediumFont);
        resultCounter.getStyle().setBgColor(0xf3f3f3);
        headerContainer.addComponent(resultCounter);
        form.addComponent(BorderLayout.NORTH, headerContainer);
    }

    private void addResultsSection(Result[] results) throws NumberFormatException {
        resultContainer = new ResultContainer(new BoxLayout(BoxLayout.Y_AXIS));
        form.addComponent(BorderLayout.CENTER, resultContainer);
        form.setFocused(resultContainer);
        initResults(results);
    }

    public void initResults(Result[] results) throws NumberFormatException {
        sort(getInitialSortProperty(), results, SORT_ORDER_DESCENDING);
        initProcessedResults(results);
    }

    private Container selectItem() {
        Container selectedItem = (Container) resultContainer.getComponentAt(selectItemPos);
        final Component title = selectedItem.getComponentAt(0);
        header.setText(((Label) title).getText());
        selectedItem.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(COLOR_SELECTEDITEM_BACKGROUND);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
                g.setColor(COLOR_BLACK);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), 1);
            }
        });
        for (int i = 0; i < selectedItem.getComponentCount(); i++) {
            Component item = selectedItem.getComponentAt(i);
            item.getStyle().setBgColor(COLOR_SELECTEDITEM_BACKGROUND);
            item.getStyle().setFgColor(COLOR_BLACK);
        }
        return selectedItem;
    }

    private void deSelectItem() {
        Container selectedItem = (Container) resultContainer.getComponentAt(selectItemPos);
        selectedItem.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(COLOR_WHITE);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
                g.setColor(COLOR_BLACK);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), 1);
            }
        });
        for (int i = 0; i < selectedItem.getComponentCount(); i++) {
            Component item = selectedItem.getComponentAt(i);
            item.getStyle().setBgColor(COLOR_WHITE);
            item.getStyle().setFgColor(COLOR_BLACK);
        }
    }

    private void addAdvLabel(Container listingContainer) {
        final Label advLabel = new Label("Advertisement");
        listingContainer.addComponent(advLabel);
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        advLabel.getStyle().setBgColor(COLOR_ADV_BACKGROUND);
        advLabel.getStyle().setFgColor(COLOR_BLACK);
        advLabel.getStyle().setFont(smallFont);
    }

    protected void addRating(final String ratingStr, Container listingContainer) throws NumberFormatException {
        int rating = Integer.parseInt(ratingStr);
        Container ratingContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        for (int j = 1; j <= rating; j++) {
            Label ratingIcon = new Label(getImage("/img/rating_mark.png", 8));
            ratingContainer.addComponent(ratingIcon);
        }
        for (int j = rating + 1; j <= 5; j++) {
            Label ratingIcon = new Label(getImage("/img/rating_empty.png", 8));
            ratingContainer.addComponent(ratingIcon);
        }
        listingContainer.addComponent(ratingContainer);
    }

    protected void addSmallFontLabel(final String text, Container listingContainer) {
        final Label addrLabel = new Label(text);
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        addrLabel.getStyle().setFont(smallFont);
        addrLabel.getStyle().setMargin(1, 1, 1, 1);
        addrLabel.setHeight(smallFont.getHeight());
        listingContainer.addComponent(addrLabel);
    }

    protected void addBoldFontLabel(final String text, Container listingContainer) {
        final Label bizNameLabel = new Label(text);
        Font boldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        bizNameLabel.getStyle().setFont(boldFont);
        listingContainer.addComponent(bizNameLabel);
    }

    private Image getImage(String imagePath, int imageWidth) {
        Image img = null;
        try {
            img = Image.createImage(imagePath);
            img = img.scaledHeight(imageWidth);

        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
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

    private boolean shouldSwap(String sortOrder, String a, String b) {
        return (sortOrder.equals(SORT_ORDER_ASCENDING) && a.compareTo(b) < 0) || (sortOrder.equals(SORT_ORDER_DESCENDING) && a.compareTo(b) > 0);
    }

    private void showDialog(String message) {
        Dialog warning = new Dialog();
        warning.show(null, message, "Ok", null);
    }

    private void selectItemDown() {
        final Container selectedItem = selectItem();
        final Component lastComponent = selectedItem.getComponentAt(selectedItem.getComponentCount() - 1);
        resultContainer.scrollComponentToVisible(lastComponent);
    }

    private void selectItemUp() {
        final Container selectedItem = selectItem();
        final Component firstComponent = selectedItem.getComponentAt(0);
        resultContainer.scrollComponentToVisible(firstComponent);
    }

    void show() {
        form.show();
    }

    protected void sort(String sortProperty, Result[] results, String sortOrder) {
        for (int k = 0; k < results.length - 1; k++) {
            if (results[k] == null) {
                break;
            }
            boolean isSorted = true;

            for (int i = 1; i < results.length - k; i++) {
                if (results[i] == null) {
                    break;
                }
                String a = results[i].getProperty(sortProperty);
                String b = results[i - 1].getProperty(sortProperty);
                if (shouldSwap(sortOrder, a, b)) {
                    Result tempVariable = results[i];
                    results[i] = results[i - 1];
                    results[i - 1] = tempVariable;

                    isSorted = false;

                }
            }

            if (isSorted) {
                break;
            }
        }
        for (int i = 0; i < results.length; i++) {
            if (results[i] == null) {
                break;
            }
            System.out.println(results[i].getProperty(sortProperty));
        }

    }

    protected void filter(String filterProperty) {
        Result[] filteredResults = new Result[MAX_RESULTS];
        int j = 0;
        for (int i = 0; i < results.length; i++) {
            Result result = results[i];
            if (!uiUtils.isEmpty(result.getProperty(filterProperty))) {
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
            uiUtils.showHelp();
        } else if (commandName.equals(OPTION_HOME)) {
            goBack();
        } else if (commandName.equals(OPTION_SELECT)) {
            if (selectItemPos == -1) {
                showDialog("Please select a result");
                return;
            }
            showDetailsForm(results[selectItemPos]);
        } else if (commandName.equals(OPTION_CALL)) {
            try {
                final Result selectedItem = results[selectItemPos];
                final String phoneNumber = selectedItem.getProperty(getPhoneNumberProperty());
                if (phoneNumber.trim().equals("")) {
                    showDialog("Phone number not found in this result.");
                } else {
                    midlet.platformRequest("tel:" + phoneNumber);
                }
            } catch (ConnectionNotFoundException ex) {
                ex.printStackTrace();
            }
        } else {
            handleFormSpecificCommandAction(commandName);
        }
    }

    protected abstract void handleFormSpecificCommandAction(String commandName);

    protected void show(final String text, Container itemContainer) {
        show(text, itemContainer, false);
    }

    protected void show(final String text, Container itemContainer, boolean isBold) {
        if (!uiUtils.isEmpty(text)) {
            if (isBold) {
                addBoldFontLabel(text, itemContainer);
            } else {
                addSmallFontLabel(text, itemContainer);
            }
        }
    }
}
