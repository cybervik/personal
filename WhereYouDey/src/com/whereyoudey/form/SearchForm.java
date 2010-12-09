package com.whereyoudey.form;

import java.io.IOException;

import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.Result;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.webservice.ArrayOfString;

public class SearchForm implements ActionListener, Runnable {

    public static final int COLOR_BACKGROUND = 15987699;
    public static final int ICON_WIDTH = 20;
    public static final int DISPLAY_WIDTH = Display.getInstance().getDisplayWidth();
    public static final String LOGO_PATH = "/img/logo.png";
    public static final int MORE_LINK_POSSIBLE_WIDTH = 40;
    private static final String[] iconPaths = {
        "/img/icons/VideosIcon.png",
        "/img/icons/OffersIcon.png",
        "/img/icons/EventIcon.png",
        "/img/icons/MoviesIcon.png",
        "/img/icons/FlightsIcon.png"
    };

    private WhereYouDey midlet;
    private Form searchForm;
    private ResultForm resultForm;
    private TextField business;
    private TextField area;
    private Container topContainer;
    private Dialog waitDialog;
    private static final UIUtils uiUtils = new UIUtils();

    public ResultForm getResultForm() {
        return resultForm;
    }

    public SearchForm(WhereYouDey midlet) {
        this.midlet = midlet;
        initForm();
    }

    private void addAreaTextField() {
        area = uiUtils.addTextFieldWithLabel(topContainer, "Area/City/State");
    }

    private void addBusinessTextField() {
        business = uiUtils.addTextFieldWithLabel(topContainer, "Businesses or Keywords");
    }

    private void hideWait() {
        if (waitDialog != null) {
            waitDialog.dispose();
        }
    }

    private void initForm() {
        searchForm = new Form("");
        searchForm.setLayout(new BorderLayout());
        searchForm.getStyle().setBgColor(COLOR_BACKGROUND);
        addMainElements();
        addButtons();
        searchForm.show();
    }

    private void addButtons() {
        searchForm.addCommandListener(this);
        searchForm.addCommand(new Command("Find"));
        searchForm.addCommand(new Command("Exit"));
    }

    public void search() {
        Thread t = new Thread(this);
        t.start();
        showWait();
    }

    private void showResultForm(Result[] results) throws NumberFormatException {
        if (resultForm == null) {
            resultForm = new ResultForm(midlet, results);
        } else {
            resultForm.initResults(results);
        }
    }

    private void showWait() {
        waitDialog = new Dialog();
        waitDialog.setLayout(new BorderLayout());
        waitDialog.getStyle().setBgColor(COLOR_BACKGROUND);
        Label waitLabel = uiUtils.getImage("/img/wait.png", ICON_WIDTH);
        waitLabel.setText("Searching");
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM,
                Font.STYLE_PLAIN, Font.SIZE_SMALL);
        waitLabel.getStyle().setFont(smallFont);
        waitLabel.getStyle().setMargin(0, 0, 0, 0);
        waitLabel.getStyle().setPadding(0, 0, 0, 0);
        waitLabel.getStyle().setBgColor(COLOR_BACKGROUND);
        waitDialog.addComponent(BorderLayout.CENTER, waitLabel);
        waitDialog.showPacked(BorderLayout.CENTER, true);
    }

    private void addMainElements() {
        topContainer = uiUtils.geBoxLayoutContainer(BoxLayout.Y_AXIS);
        addLogo();
        addIcons();
        addBusinessTextField();
        addAreaTextField();
        searchForm.addComponent(BorderLayout.NORTH, topContainer);
    }

    private void addIcons() {
        Container icons = uiUtils.getBoxLayoutColumnStyleContainer();
        int noOfIconsFittingInScreenWidth = (DISPLAY_WIDTH - MORE_LINK_POSSIBLE_WIDTH) / ICON_WIDTH;
        for (int i = 0; i < noOfIconsFittingInScreenWidth && i < iconPaths.length; i++) {
            icons.addComponent(uiUtils.getImage(iconPaths[i], ICON_WIDTH));
        }
        icons.addComponent(uiUtils.getLink("More"));
        topContainer.addComponent(icons);
    }

    private void addLogo() {
        int logoWidth = DISPLAY_WIDTH * 2 / 3;
        Label logo = uiUtils.getImage(LOGO_PATH, logoWidth);
        topContainer.addComponent(logo);
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

    public void show() {
        searchForm.show();
    }

    public void actionPerformed(ActionEvent ae) {
        final String commandName = ae.getCommand().getCommandName();
        if ("Exit".equals(commandName)) {
            exit();
        } else if ("Find".equals(commandName)) {
            search();
        }
    }

    public String getSearchBusinessText() {
        return this.business.getText().trim();
    }

    public String getSearchAreaText() {
        return this.area.getText().trim();
    }

    public void run() {
        String businessText = getSearchBusinessText();
        if (uiUtils.isEmpty(businessText)) {
            uiUtils.showDialog("Please enter business to search");
            return;
        }
        String areaText = getSearchAreaText();
        SearchService searchService = new SearchService();
        ArrayOfString filter = new ArrayOfString();
        filter.setString(new String[]{"", "", "", "", "0", "10"});
        Result[] results = searchService.search(businessText, areaText, filter);
        hideWait();
        showResultForm(results);
    }
}
