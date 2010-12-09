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
import com.whereyoudey.webservice.ArrayOfString;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;

/**
 *
 * @author Vikram S
 */
class ResultForm implements ActionListener {

    public static final int COLOR_ADV_BACKGROUND = 0xffa500;
    public static final int COLOR_BLACK = 0x000000;
    public static final int COLOR_WHITE = 0xffffff;
    public static final int COLOR_SELECTEDITEM_BACKGROUND = 0x9999ff;
    private WhereYouDey midlet;
    private Form form;
    private Result[] results;
    private Label header;
    private Label resultCounter;
    private int selectItemPos = -1;
    private ResultContainer resultContainer;
    private int resultCount;
    private DetailsForm detailsForm;
    private int startIndex;
    private int MAX_RESULTS = 10;

    ResultForm(WhereYouDey midlet, Result[] results) {
        initVariables(midlet);
        initForm();
        addHeader();
        addResultsSection(results);
        form.addCommand(new Command("Back"));
        form.addCommand(new Command("Select"));
        form.addCommand(new Command("Call"));
//        form.addCommand(new Command("Next"));
//        form.addCommand(new Command("Previous"));
//        form.addCommand(new Command("First"));
//        form.addCommand(new Command("Sort by relevenace"));
//        form.addCommand(new Command("Filter by city"));
//        form.addCommand(new Command("Filter by area"));
//        form.addCommand(new Command("Help"));
//        form.addCommand(new Command("Home"));
        form.addCommandListener(this);
        form.show();
    }

    private DetailsForm getDetailsForm() {
        if (detailsForm == null) {
            detailsForm = new DetailsForm(midlet);
        }
        return detailsForm;
    }

    private void initVariables(WhereYouDey midlet) {
        this.midlet = midlet;
        this.selectItemPos = -1;
        this.startIndex = 1;
    }

    private void initForm() {
        form = new Form();
        form.setScrollableY(false);
        form.setLayout(new BorderLayout());
        form.addKeyListener(-1, this);
        form.addKeyListener(-2, this);
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
        this.results = results;
        this.resultCount = 0;
        this.selectItemPos = -1;
        this.resultContainer.removeAll();
        for (int i = 0; i < results.length; i++) {
            Result result = results[i];
            if (result != null) {
                resultCount++;
                final String bizName = result.getProperty("Name");
                final String address = result.getProperty("Address");
//                final String street = result.getProperty("Street");
//                final String area = result.getProperty("Area");
                final String city = result.getProperty("City");
                final String state = result.getProperty("State");
                final String phone = result.getProperty("Phone");
                final String ratingStr = result.getProperty("StarReview");
                Container itemContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
                addBoldFontLabel(bizName, itemContainer);
                addSmallFontLabel(address, itemContainer);
                addSmallFontLabel(city + ", " + state, itemContainer);
                addRating(ratingStr, itemContainer);
                addSmallFontLabel(phone, itemContainer);
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
                        g.setColor(0x000000);
                        g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), 1);
                    }
                });
            }
        }
        if (resultCount > 0) {
            resultCounter.setText("Result 1 - " + resultCount);
        } else {
            resultCounter.setText("No results found");
        }
        form.show();
    }

    private Container selectItem() {
        Container selectedItem = (Container) resultContainer.getComponentAt(selectItemPos);
        final Component title = selectedItem.getComponentAt(0);
        title.getStyle().setBgColor(COLOR_SELECTEDITEM_BACKGROUND);
        title.getStyle().setFgColor(COLOR_BLACK);
        header.setText(((Label) title).getText());
        return selectedItem;
    }

    private void deSelectItem() {
        Container selectedItem = (Container) resultContainer.getComponentAt(selectItemPos);
        final Component heading = selectedItem.getComponentAt(0);
        heading.getStyle().setBgColor(COLOR_WHITE);
        heading.getStyle().setFgColor(COLOR_BLACK);
    }

    private void addAdvLabel(Container listingContainer) {
        final Label advLabel = new Label("Advertisement");
        listingContainer.addComponent(advLabel);
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        advLabel.getStyle().setBgColor(COLOR_ADV_BACKGROUND);
        advLabel.getStyle().setFgColor(COLOR_BLACK);
        advLabel.getStyle().setFont(smallFont);
    }

    private void addRating(final String ratingStr, Container listingContainer) throws NumberFormatException {
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

    private void addSmallFontLabel(final String address, Container listingContainer) {
        final Label addrLabel = new Label(address);
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        addrLabel.getStyle().setFont(smallFont);
        addrLabel.getStyle().setMargin(1, 1, 1, 1);
        addrLabel.setHeight(smallFont.getHeight());
        listingContainer.addComponent(addrLabel);
    }

    private void addBoldFontLabel(final String bizName, Container listingContainer) {
        final Label bizNameLabel = new Label(bizName);
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
            final String commandName = command.getCommandName();
            if (commandName.equals("Back")) {
                midlet.getSearchForm().show();
            } else if (commandName.equals("Select")) {
                if (selectItemPos == -1) {
                    showDialog("Please select a result");
                    return;
                }
                DetailsForm detailsForm = getDetailsForm();
                detailsForm.init(results[selectItemPos]);
            } else if (commandName.equals("Call")) {
                try {
                    final Result selectedItem = results[selectItemPos];
                    final String phoneNumber = selectedItem.getProperty("Phone");
                    if (phoneNumber.trim().equals("")) {
                        showDialog("Phone number not found in this result.");
                    } else {
                        midlet.platformRequest("tel:" + phoneNumber);
                    }
                } catch (ConnectionNotFoundException ex) {
                    ex.printStackTrace();
                }
            } else if (commandName.equals("Next")) {
                startIndex = startIndex + resultCount;
                int endIndex = startIndex + MAX_RESULTS;
                search(startIndex, endIndex);
            } else if (commandName.equals("Prev")) {
            } else if (commandName.equals("First")) {
            }
        } else {
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
        ae.consume();
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

    private void search(int startIndex, int endIndex) {
        final SearchForm searchForm = midlet.getSearchForm();
        final String searchBusinessText = searchForm.getSearchBusinessText().trim();
        final String searchAreaText = searchForm.getSearchAreaText().trim();
        SearchService searchService = new SearchService();
        ArrayOfString filter = new ArrayOfString();
        filter.setString(new String[]{"", "", "", "", String.valueOf(startIndex), String.valueOf(endIndex)});
        Result[] results = searchService.search(searchBusinessText, searchAreaText, filter);
        initResults(results);
    }
}
