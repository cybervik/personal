package com.whereyoudey.form;

import java.io.IOException;
import java.util.Vector;

import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.geom.Dimension;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.GridLayout;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.Property;
import com.whereyoudey.service.Result;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.webservice.ArrayOfString;

public class SearchForm implements ActionListener, Runnable {

    public static final int ICON_WIDTH = 20;
    public static final int MORE_LINK_POSSIBLE_WIDTH = 40;
    public static final int DISPLAY_WIDTH = Display.getInstance().getDisplayWidth();
    public static final int BUTTON_PANEL_BACKGROUND_COLOR = 0xffa500;
    public static final int BUTTON_BACKGROUND_COLOR = 0xff4500;
    public static final int BUTTON_FONT_COLOR = 0xffffff;

    private WhereYouDey midlet;
    private Form form;
    private ResultForm resultForm;
    private TextField business;
    private TextField area;
    private Container topContainer;
    private Label waitLabel;
    private Dialog waitDialog;

    public ResultForm getResultForm() {
        return resultForm;
    }

    public SearchForm(WhereYouDey midlet) {
        this.midlet = midlet;
        initForm();
    }

    private void hideWait() {
        if (waitDialog != null) {
            waitDialog.dispose();
        }
    }

    private void initForm() {
        form = new Form("");
        form.setLayout(new BorderLayout());
        form.getStyle().setBgColor(0xf3f3f3);
        addMainElements();
        addButtons();
        form.show();
    }

    private void addButtons() {
        form.addCommandListener(this);
        form.addCommand(new Command("Find"));
        form.addCommand(new Command("Exit"));
//        Container buttonsPanel = getButtonsPanel();
//        addButton(buttonsPanel, "Options");
//        Button findButton = addButton(buttonsPanel, "Find");
//        addSearchButtonHandler(findButton);
//        Button backButton = addButton(buttonsPanel, "Back");
//        addExitHandler(backButton);
//        form.addComponent(BorderLayout.SOUTH, buttonsPanel);
    }

    private void addSearchButtonHandler(Button findButton) {
    }

    public void search() {
        Thread t = new Thread(this);
        t.start();
        showWait();
    }

    private void showWait() {
        waitDialog = new Dialog();
        waitDialog.setLayout(new BorderLayout());
        waitLabel = getImage("/img/wait.png", ICON_WIDTH);
        waitLabel.getStyle().setMargin(0, 0, 0, 0);
        waitLabel.getStyle().setPadding(0, 0, 0, 0);
        waitDialog.addComponent(BorderLayout.CENTER, waitLabel);
//        waitDialog.setSize(new Dimension(20, 20));
        waitDialog.showPacked(BorderLayout.CENTER, true);
    }

    private void showDialog(String message) {
        Dialog warning = new Dialog();
        warning.setWidth(form.getWidth());
        warning.show(null, message, "Ok", null);
    }

    private Container getButtonsPanel() {
        Container buttonsPanel = new Container(new GridLayout(1, 3));
        paintButtonsPanelBackground(buttonsPanel);
        return buttonsPanel;
    }

    private Button addButton(Container buttons, String buttonName) {
        Button button = new Button(buttonName);
        button.setAlignment(Component.CENTER);
        button.getStyle().setMargin(1, 1, 8, 10);
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        button.getStyle().setFont(smallFont);
        button.getStyle().setBgColor(BUTTON_BACKGROUND_COLOR);
        button.getStyle().setFgColor(BUTTON_FONT_COLOR);
        buttons.addComponent(button);
        return button;
    }

    private void addMainElements() {
        topContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        addLogo(topContainer);
        addIcons(topContainer);
        business = addTextFieldWithLabel(topContainer, "Businesses or Keywords");
        area = addTextFieldWithLabel(topContainer, "Area/City/State");
        form.addComponent(BorderLayout.NORTH, topContainer);
    }

    private TextField addTextFieldWithLabel(Container topContainer, String labelText) {
        addBoldFontLabel(topContainer, labelText);
        final TextField textField = new TextField();
        textField.setHeight(10);
        Font mediumFont = Font.createSystemFont(Font.FACE_SYSTEM,
                Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        textField.getStyle().setFont(mediumFont);
        topContainer.addComponent(textField);
        return textField;
    }

    private void addBoldFontLabel(Container topContainer, String labelText) {
        Label label = new Label(labelText);
        Font boldFont = Font.createSystemFont(Font.FACE_SYSTEM,
                Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        label.getStyle().setFont(boldFont);
        topContainer.addComponent(label);
    }

    private void addIcons(Container topContainer) {
        Container icons = new Container(new BoxLayout(BoxLayout.X_AXIS));
        int noOfIcons = (DISPLAY_WIDTH - MORE_LINK_POSSIBLE_WIDTH) / ICON_WIDTH;
        String[] iconPaths = {"/img/icons/VideosIcon.png", "/img/icons/OffersIcon.png", "/img/icons/EventIcon.png", "/img/icons/MoviesIcon.png", "/img/icons/FlightsIcon.png"};
        for (int i = 0; i < noOfIcons && i < 5; i++) {
            icons.addComponent(getImage(iconPaths[i], ICON_WIDTH));
        }
        icons.addComponent(getLink("More"));
        topContainer.addComponent(icons);
    }

    private void addLogo(Container topContainer) {
        int logoWidth = DISPLAY_WIDTH * 2 / 3;
        Label logo = getImage("/img/logo.png", logoWidth);
        topContainer.addComponent(logo);
    }

    private Label getLink(String linkText) {
        Label link = new Label(linkText);
        Font italicFont = Font.createSystemFont(Font.FACE_SYSTEM,
                Font.STYLE_UNDERLINED, Font.SIZE_SMALL);
        link.getStyle().setFont(italicFont);
        return link;
    }

    private Label getImage(String imagePath, int imageWidth) {
        Label imageLabel = null;
        try {
            Image img = Image.createImage(imagePath);
            img = img.scaledWidth(imageWidth);
            imageLabel = new Label(img);
            imageLabel.setAlignment(Component.CENTER);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageLabel;
    }

    private void paintButtonsPanelBackground(Container buttons) {
        buttons.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle rect) {
                g.setColor(SearchForm.BUTTON_PANEL_BACKGROUND_COLOR);
                g.fillRect(rect.getX(), rect.getY(), rect.getSize().getWidth(),
                        rect.getSize().getHeight());
            }
        });
    }

    private void exit() {
        try {
            midlet.destroyApp(true);
            midlet.notifyDestroyed();
        } catch (MIDletStateChangeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    void show() {
        form.show();
    }

    public void actionPerformed(ActionEvent ae) {
        final String commandName = ae.getCommand().getCommandName();
        if ("Exit".equals(commandName)) {
            exit();
        } else if ("Find".equals(commandName)) {
            search();
        }
    }

    public String getSearchBusiness() {
        return this.business.getText();
    }

    public String getSearchArea() {
        return this.area.getText();
    }

    public void run() {
        String searchString = business.getText().trim();
        if ("".equals(searchString)) {
            showDialog("Please enter business to search");
            return;
        }
        String location = area.getText().trim();
        SearchService searchService = new SearchService();
        ArrayOfString filter = new ArrayOfString();
        filter.setString(new String[]{"", "", "", "", "0", "10"});
        Result[] results = searchService.search(searchString, location, filter);
        hideWait();
        if (resultForm == null) {
            resultForm = new ResultForm(midlet, results);
        } else {
            resultForm.initResults(results);
        }
    }
}
