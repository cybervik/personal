/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.utils.UIUtils;
import javax.microedition.io.ConnectionNotFoundException;

/**
 *
 * @author Vikram S
 */
public abstract class SearchForm implements ActionListener, Runnable {

    public static final int COLOR_BACKGROUND = 15987699;
    public static final int DISPLAY_WIDTH = Display.getInstance().getDisplayWidth();
    public static final String FLIGHTS_URL = "http://www.wakanow.com";
    public static final String ICON_NAME_EVENTS = "Events";
    public static final String ICON_NAME_FLIGHTS = "Flights";
    public static final String ICON_NAME_VIDEOS = "Videos";
    public static final int ICON_WIDTH = 20;
    public static final String LINK_MORE = "More";
    public static final String LINK_SELECT_CITY = "Select City";
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
    private static final String[] iconIds = {
        ICON_NAME_VIDEOS,
        "Offers",
        ICON_NAME_EVENTS,
        "Movies",
        ICON_NAME_FLIGHTS};
    private static final String[] iconPaths = {
        "/img/icons/VideosIcon.png",
        "/img/icons/OffersIcon.png",
        "/img/icons/EventIcon.png",
        "/img/icons/MoviesIcon.png",
        "/img/icons/FlightsIcon.png"
    };
    protected static final UIUtils uiUtils = new UIUtils();
    protected String focussed;
    protected Form form;
    protected WhereYouDey midlet;
    protected Container topContainer;
    protected Dialog waitDialog;

    public SearchForm(WhereYouDey midlet) {
        this.midlet = midlet;
        initForm();
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
            System.out.println("Focussed = " + focussed);
            if (ICON_NAME_VIDEOS.equals(focussed)) {
                requestPlatFormService(VIDEO_URL);
            } else if (LINK_MORE.equals(focussed)) {
                uiUtils.showDialog("Comming soon...");
            } else if (ICON_NAME_FLIGHTS.equals(focussed)) {
                requestPlatFormService(FLIGHTS_URL);
            } else if (ICON_NAME_EVENTS.equals(focussed)) {
                showEventsForm();
            } else {
                moreActionPerformed();
            }
        }
    }

    private void showEventsForm() {
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

    private void addMainElements() {
        topContainer = uiUtils.geBoxLayoutContainer(BoxLayout.Y_AXIS);
        addLogo();
        addIcons();
        addFormFields();
        form.addComponent(BorderLayout.NORTH, topContainer);
    }

    private void addMenuActions() {
        form.addCommandListener(this);
        form.addCommand(new Command(OPTION_FIND));
        form.addCommand(new Command(OPTION_EXIT));
        form.addCommand(new Command(OPTION_ABOUT_US));
        form.addCommand(new Command(OPTION_HELP));
        form.addCommand(new Command(OPTION_SELECT));
        form.addCommand(new Command(OPTION_CHECK_FOR_UPDATES));
    }

    private void createForm() {
        form = new Form("");
        form.setLayout(new BorderLayout());
        form.getStyle().setBgColor(COLOR_BACKGROUND);
    }

    public Component getFocussed() {
        return form.getFocused();
    }

    protected void hideWait() {
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

    private void requestPlatFormService(String vu) {
        try {
            midlet.platformRequest(vu);
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    public void show() {
        form.show();
    }

    protected void showWait() {
        waitDialog = new Dialog();
        waitDialog.setLayout(new BorderLayout());
        waitDialog.getStyle().setBgColor(COLOR_BACKGROUND);
        Label waitLabel = uiUtils.getImage("/img/wait.png", ICON_WIDTH);
        waitLabel.setText("Searching");
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        waitLabel.getStyle().setFont(smallFont);
        waitLabel.getStyle().setMargin(0, 0, 0, 0);
        waitLabel.getStyle().setPadding(0, 0, 0, 0);
        waitLabel.getStyle().setBgColor(COLOR_BACKGROUND);
        waitDialog.addComponent(BorderLayout.CENTER, waitLabel);
        waitDialog.showPacked(BorderLayout.CENTER, true);
    }

    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        searchAction();
    }

    public void search() {
        if (!isFormValid()) {
            return;
        }
        Thread t = new Thread(this);
        t.start();
        showWait();
    }

    protected abstract void addFormFields();

    protected abstract void moreActionPerformed();

    protected abstract boolean isFormValid();

    protected abstract void searchAction();
}
