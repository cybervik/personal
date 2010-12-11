package com.whereyoudey.form;

import com.sun.lwuit.Component;
import com.sun.lwuit.List;
import com.whereyoudey.utils.UIUtils;
import javax.microedition.io.ConnectionNotFoundException;

import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.lwuit.Command;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.Result;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.webservice.ArrayOfString;

public class SearchForm implements ActionListener, Runnable {

    public static final int COLOR_BACKGROUND = 15987699;
    public static final String FLIGHTS_URL = "http://www.wakanow.com";
    public static final int ICON_WIDTH = 20;
    public static final int DISPLAY_WIDTH = Display.getInstance().getDisplayWidth();
    public static final String LINK_MORE = "More";
    public static final String LOGO_PATH = "/img/logo.png";
    public static final int MORE_LINK_POSSIBLE_WIDTH = 40;
    public static final String OPTION_ABOUT_US = "About Us";
    public static final String OPTION_CHECK_FOR_UPDATES = "Check For Updates";
    public static final String OPTION_EXIT = "Exit";
    public static final String OPTION_FIND = "Find";
    public static final String OPTION_HELP = "Help";
    public static final String OPTION_SELECT = "Select";
    public static final String UPDATE_URL = "http://www.whereyoudey.com/whereyoudey.jad";
    public static final String VIDEO_URL = "http://www.youtube.com/whereyoudey";
    public static final String LINK_SELECT_CITY = "Select City";
    private static final String[] iconPaths = {
        "/img/icons/VideosIcon.png",
        "/img/icons/OffersIcon.png",
        "/img/icons/EventIcon.png",
        "/img/icons/MoviesIcon.png",
        "/img/icons/FlightsIcon.png"
    };
    private static final String[] iconIds = {
        "Videos",
        "Offers",
        "Events",
        "Movies",
        "Flights"
    };
    private WhereYouDey midlet;
    private Form searchForm;
    private ResultForm resultForm;
    private TextField business;
    private TextField area;
    private Container topContainer;
    private Dialog waitDialog;
    private static final UIUtils uiUtils = new UIUtils();
    private String focussed;
    private ListForm cityOptionsform;

    public ResultForm getResultForm() {
        return resultForm;
    }

    public SearchForm(WhereYouDey midlet) {
        this.midlet = midlet;
        initForm();
    }

    private void addAreaTextField() {
        area = uiUtils.addTextFieldWithLabel(midlet, topContainer, "Area/City/State");
    }

    private void addBusinessTextField() {
        business = uiUtils.addTextFieldWithLabel(midlet, topContainer, "Businesses or Keywords");
//        business.setFocus(true);
        searchForm.setFocused(business);
    }

    private void createForm() {
        searchForm = new Form("");
        searchForm.setLayout(new BorderLayout());
        searchForm.getStyle().setBgColor(COLOR_BACKGROUND);
    }

    private void createCitiesOptionsForm() {
        if (cityOptionsform == null) {
            cityOptionsform = new ListForm(midlet, ListForm.CITIES, area);
        }
    }

    private void hideWait() {
        if (waitDialog != null) {
            waitDialog.dispose();
        }
    }

    private void initForm() {
        createForm();
        addMainElements();
        addMenuActions();
        show();
    }

    private void addMenuActions() {
        searchForm.addCommandListener(this);
        searchForm.addCommand(new Command(OPTION_FIND));
        searchForm.addCommand(new Command(OPTION_EXIT));
        searchForm.addCommand(new Command(OPTION_ABOUT_US));
        searchForm.addCommand(new Command(OPTION_HELP));
        searchForm.addCommand(new Command(OPTION_SELECT));
        searchForm.addCommand(new Command(OPTION_CHECK_FOR_UPDATES));
    }

    public void search() {
        String businessText = getSearchBusinessText();
        if (uiUtils.isEmpty(businessText)) {
            uiUtils.showDialog("Please enter business to search");
            return;
        }
        Thread t = new Thread(this);
        t.start();
        showWait();
    }

    private void requestPlatFormService(String vu) {
        try {
            midlet.platformRequest(vu);
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
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
        addSelectCityLink();
        searchForm.addComponent(BorderLayout.NORTH, topContainer);
    }

    private void addIcons() {
        Container icons = uiUtils.getBoxLayoutColumnStyleContainer();
        int noOfIconsFittingInScreenWidth = (DISPLAY_WIDTH - MORE_LINK_POSSIBLE_WIDTH) / ICON_WIDTH;
        for (int i = 0; i < noOfIconsFittingInScreenWidth && i < iconPaths.length; i++) {
            final int pos = i;
            Label image = uiUtils.getImage(iconPaths[i], ICON_WIDTH);
            image.setFocusable(true);
            image.addFocusListener(new FocusListener() {

                public void focusGained(Component cmpnt) {
                    focussed = iconIds[pos];
                }

                public void focusLost(Component cmpnt) {
                    if (focussed.equals(iconIds[pos])) {
                        focussed = "";
                    }
                }
            });
            if (i == 0) {
                image.getStyle().setBorder(Border.createBevelRaised());
            }
            image.getSelectedStyle().setBorder(Border.createLineBorder(1));
            icons.addComponent(image);
        }
        Label moreLink = uiUtils.getLink(LINK_MORE);
        moreLink.addFocusListener(new FocusListener() {

            public void focusGained(Component cmpnt) {
                focussed = LINK_MORE;
            }

            public void focusLost(Component cmpnt) {
                if (LINK_MORE.equals(focussed)) {
                    focussed = "";
                }
            }
        });
        icons.addComponent(moreLink);
        topContainer.addComponent(icons);
    }

    private void addLogo() {
        int logoWidth = DISPLAY_WIDTH * 2 / 3;
        Label logo = uiUtils.getImage(LOGO_PATH, logoWidth);
        topContainer.addComponent(logo);
    }

    public void show() {
        searchForm.show();
    }

    public void actionPerformed(ActionEvent ae) {
        final String commandName = ae.getCommand().getCommandName();
        if (OPTION_EXIT.equals(commandName)) {
            midlet.exit();
        } else if (OPTION_FIND.equals(commandName)) {
            search();
        } else if (OPTION_ABOUT_US.equals(commandName)) {
            uiUtils.showAbout();
        } else if (OPTION_HELP.equals(commandName)) {
            uiUtils.showHelp();
        } else if (OPTION_CHECK_FOR_UPDATES.equals(commandName)) {
            requestPlatFormService(UPDATE_URL);
        } else if (OPTION_SELECT.equals(commandName)) {
            System.out.println("Focussed = "+focussed);
            if ("Videos".equals(focussed)) {
                requestPlatFormService(VIDEO_URL);
            } else if (LINK_MORE.equals(focussed)) {
                uiUtils.showDialog("Comming soon...");
            } else if ("Flights".equals(focussed)) {
                requestPlatFormService(FLIGHTS_URL);
            } else if ("Maps".equals(focussed)) {
            } else if (LINK_SELECT_CITY.equals(focussed)) {
                createCitiesOptionsForm();
            }
        }
    }

    public String getSearchBusinessText() {
        return this.business.getText().trim();
    }

    public String getSearchAreaText() {
        return this.area.getText().trim();
    }

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        String businessText = getSearchBusinessText();
        String areaText = getSearchAreaText();
        SearchService searchService = new SearchService();
        ArrayOfString filter = new ArrayOfString();
        filter.setString(new String[]{"", "", "", "", "0", "10"});
        Result[] results = searchService.search(businessText, areaText, filter);
        hideWait();
        showResultForm(results);
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

    public Component getFocussed() {
        return searchForm.getFocused();
    }
}
