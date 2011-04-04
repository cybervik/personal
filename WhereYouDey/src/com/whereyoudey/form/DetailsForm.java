/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.whereyoudey.utils.FontUtil;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.form.component.Section;
import com.whereyoudey.maps.directions.DrivingDirections;
import com.whereyoudey.maps.directions.Route;
import com.whereyoudey.maps.directions.Steps;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.utils.DialogUtil;
import com.whereyoudey.utils.ExtendedDialog;
import com.whereyoudey.utils.UiUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Vikram S
 */
abstract class DetailsForm implements ActionListener {

    public static final String OPTION_BACK = "Back";
    public static final String OPTION_CALL = "Call";
    public static final String OPTION_EXIT = "Exit";
    public static final String OPTION_HOME = "Home";
    public static final String OPTION_SELECT = "Select";
    public static final String LINK_AUDIO_VIDEO_URL = "http://youtube.com/whereyoudey";
    public static final String LINK_DRIVING_DIRECTIONS = "Driving Directions";
    public static final String LINK_MAPS = "Maps";
    public static final String LINK_MAPS_URL = "http://maps.google.com";
    public static final String LINK_OFFERS = "Offers";
    public static final String LINK_OFFERS_URL = "http://whereyoudey.com/offers";
    public static final String LINK_VIDEO_AUDIO = "Video/Audio";
    protected ExtendedForm form;
    private WrappingLabel header;
    protected final WhereYouDey midlet;
    protected Result result;
    private final ResultForm callingForm;
    String focussed = "";
    private Section links;
    private Container drivingDirections;
    private TextField fromDriving;
    private TextField toDriving;
    private Button getDrivingDirectionsButton;
    private Label mapImage;
    private ExtendedDialog mapImageDialog;
    final Command selectCommand = new Command(OPTION_SELECT);
    private int mapWidth;
    private int mapHeight;
    private ExtendedDialog drivingDirectionsDialog;
    private TextArea drivingDirectionsArea;
    private static final String VIDEO_LINK = "Video";
    protected String videoUrl;

    public DetailsForm(WhereYouDey midlet, ResultForm callingForm) {
        this.midlet = midlet;
        this.callingForm = callingForm;
        initForm();
        addHeader();
        addFormElements();
        addCommands();
        form.show();
    }

    protected void addFormElements() throws NumberFormatException {
        addBasicInfo();
        addSections();
        final Label dummy = new Label(" ");
        dummy.setFocusable(true);
        dummy.setNextFocusDown(header);
        form.addComponent(dummy);
    }

    protected abstract String getHeaderProperty();

    protected abstract String getPhoneProperty();

    private void addDrivingDirectionsInfo(final String text, int fontSize, int fontStyle) {
        final Label label = new Label(text);
        label.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, fontStyle, fontSize));
        label.getSelectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, fontStyle, fontSize));
        drivingDirectionsDialog.addComponent(label);
    }

    protected void addLink(final String linkName) {
        final Label link = UiUtil.getLink(linkName);
        links.addComponent(link);
        link.addFocusListener(new FocusListener() {

            public void focusGained(Component cmpnt) {
                form.addCommand(selectCommand, form.getCommandCount());
            }

            public void focusLost(Component cmpnt) {
                form.removeCommand(selectCommand);
            }
        });
    }

    private void call() {
        String phoneNumber = result.getProperty(getPhoneProperty());
        if (UiUtil.isEmpty(phoneNumber)) {
            DialogUtil.showInfo("Error", "Phone number not found in this result.");
            return;
        }
        if (phoneNumber.indexOf(",") >= 0) {
            phoneNumber = phoneNumber.substring(0, phoneNumber.indexOf(","));
        }
        phoneNumber = phoneNumber.trim();
        if (UiUtil.isEmpty(phoneNumber)) {
            DialogUtil.showInfo("Error", "Phone number not found in this result.");
            return;
        }
        midlet.requestPlatformService("tel:" + phoneNumber);
    }

    private void exit() {
        midlet.exit();
    }

    private void goBack() {
        callingForm.show();
    }

    private void goHome() {
        midlet.getSearchForm().resetAndShow();
    }

    private void initForm() {
        form = new ExtendedForm();
        form.setWidth(Display.getInstance().getDisplayWidth());
        form.setLayout(new BoxLayout(BoxLayout.Y_AXIS));
        form.setScrollableX(true);
    }

    private void addCommands() {
        form.addCommand(new Command(OPTION_BACK));
        form.addCommand(new Command(OPTION_EXIT));
        form.addCommand(new Command(OPTION_HOME));
        form.addCommand(new Command(OPTION_CALL));
        form.addCommandListener(this);
    }

    protected Label addBigFontLabel(final String ph) {
        Label phoneNumber = new Label(ph);
        Font bigFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
        phoneNumber.getStyle().setFont(bigFont);
        form.addComponent(phoneNumber);
        return phoneNumber;
    }

    private void addHeader() {
        header = new WrappingLabel("", FontUtil.getMediumBoldFont());
        header.getStyle().setBgColor(0x000000);
        header.getStyle().setFgColor(0xffffff);
        header.getSelectedStyle().setBgColor(0x000000);
        header.getSelectedStyle().setFgColor(0xffffff);
        header.setFocusable(true);
        form.addComponent(header);
    }

    protected Label addSmallFontLabel(final String txt) {
        Label label = new Label(txt);
        setSmallFont(label);
        form.addComponent(label);
        return label;
    }

    protected abstract void initResult(Result result);

    private boolean isBanner(Result result) {
        final String banner = result.getProperty("Banner");
        return !UiUtil.isEmpty(banner);
    }

    private void setHeader(Result result) {
        final String bizName = result.getProperty(getHeaderProperty());
        final String additionalHeaderText = getAdditionalHeaderText();
        header.setText(bizName + additionalHeaderText, FontUtil.getMediumBoldFont());
    }

    private void setSmallFont(Component comp) {
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        comp.getStyle().setFont(smallFont);
    }

    protected Image getImage(String imagePath, int imageWidth) {
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

    public void init(Result result) {
        this.result = result;
        if (!isEvent(result)) {
            final String resultId = result.getProperty("ID");
            this.result = new SearchService().getDetailsFromHelper(resultId);
            if (this.result == null) {
                throw new ApplicationException("Could not retrieve details");
            }
        }
        setHeader(this.result);
        initResult(this.result);
        header.setFocus(true);
        form.setFocused(header);
        form.scrollComponentToVisible(header);
        form.show();
    }

    public void actionPerformed(ActionEvent ae) {
        final Command command = ae.getCommand();
        final String commandName = command.getCommandName();
        if (OPTION_EXIT.equals(commandName)) {
            exit();
        } else if (OPTION_HOME.equals(commandName)) {
            goHome();
        } else if (OPTION_CALL.equals(commandName)) {
            call();
        } else if (OPTION_BACK.equals(commandName)) {
            goBack();
        } else if (OPTION_SELECT.equals(commandName)) {
            final Component focused = form.getFocused();
            if (focused != null && focused instanceof Label) {
                Label focussedLabel = (Label) focused;
                final String focussedName = focussedLabel.getText();
                selectAction(focussedName);
            }
        }
    }

    protected void selectAction(String focussedName) {
        if (focussedName.equals(LINK_MAPS)) {
            showStaticMap();
        } else if (focussedName.equals(VIDEO_LINK)) {
            if (UiUtil.isEmpty(videoUrl)) {
                DialogUtil.showInfo("Error", "No videos found for this result.");
            } else {
                midlet.requestPlatformService(videoUrl);
            }
        } else if (focussedName.equals(LINK_OFFERS)) {
            midlet.requestPlatformService(LINK_OFFERS_URL);
        } else if (focussedName.equals(LINK_DRIVING_DIRECTIONS)) {
            showDrivingDirections();
        } else {
            selectActionPerformed(focussedName);
        }
    }

    private void showStaticMap() {
        Thread t = new Thread(new Runnable() {

            public void run() {
                try {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    final String address = UiUtil.urlEncode(getAddress(), false);
                    String url = "http://maps.google.com/maps/api/staticmap?"
                                 + "center=" + address
                                 + "&zoom=15"
                                 + "&maptype=roadmap"
                                 + "&" + UiUtil.urlEncode("feature:all", false)
                                 + "&markers=" + UiUtil.urlEncode("color:orange|label:1|", false) + address
                                 + "&size=" + (mapWidth) + "x" + (mapHeight)
                                 + "&sensor=false";
//                    url = "http://maps.google.com/maps/api/staticmap?"
//                          + "center=" + UiUtil.urlEncode(getAddress())
//                          + "&zoom=12"
//                          + "&size=" + (mapWidth)
//                          + "x" + (mapHeight)
//                          + "&sensor=false";
//                    url = "http://www.whereyoudey.com/mainpagebanners/jo_blez_banner.jpg";
                    System.out.println("URL for static map - " + url);
                    HttpConnection conn = (HttpConnection) Connector.open(url);
                    conn.setRequestMethod(HttpConnection.GET);
                    InputStream input = conn.openInputStream();
                    final Image img;
                    try {
                        img = Image.createImage(input);
                    } finally {
                        input.close();
                        conn.close();
                    }
                    mapImage.setIcon(img);
                    if (Display.getInstance().isEdt()) {
                        System.out.println("Show static map - this is edt");
                        DialogUtil.hideWait();
                        mapImageDialog.showExtendedDialog();
                    } else {
                        System.out.println("Show static map - this is not edt");
                        Display.getInstance().callSerially(new Runnable() {

                            public void run() {
                                System.out.println("Show static map - in edt now");
                                DialogUtil.hideWait();
                                mapImageDialog.showExtendedDialog();
                            }
                        });
                    }
                } catch (Exception ex) {
                    DialogUtil.hideWait();
                    ex.printStackTrace();
                }
            }
        });
        t.start();
        DialogUtil.showWait();
    }

    private void showDrivingDirections() {
        try {
            drivingDirectionsDialog.removeAll();
            ExtendedDialog drivingDirectionsInfo = new ExtendedDialog("Get driving directions", "", Dialog.TYPE_CONFIRMATION);
            drivingDirectionsInfo.setScrollable(false);
            drivingDirectionsInfo.setLayout(new BoxLayout((BoxLayout.Y_AXIS)));
            final Label fromLabel = new Label("From: ");
            fromLabel.getStyle().setFont(FontUtil.getMediumBoldFont());
            fromLabel.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
            drivingDirectionsInfo.addComponent(fromLabel);
            final TextField fromAddrField = new TextField();
            fromAddrField.getStyle().setFont(FontUtil.getMediumNormalFont());
            fromAddrField.getSelectedStyle().setFont(FontUtil.getMediumNormalFont());
            drivingDirectionsInfo.addComponent(fromAddrField);
            fromAddrField.setText("Abuja");
            final Label toLabel = new Label("To: ");
            toLabel.getStyle().setFont(FontUtil.getMediumBoldFont());
            toLabel.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
            drivingDirectionsInfo.addComponent(toLabel);

            TextArea toAddr = new TextArea(getAddress());
            toAddr.getStyle().setFont(FontUtil.getMediumNormalFont());
            toAddr.getStyle().setBorder(Border.createEmpty());
            final Font mediumNormalFont = FontUtil.getMediumNormalFont();
            final int columns = ((Display.getInstance().getDisplayWidth() - 10) / mediumNormalFont.stringWidth("W"));
            toAddr.getSelectedStyle().setFont(mediumNormalFont);
            toAddr.getSelectedStyle().setBorder(Border.createEmpty());
            toAddr.setRows(3);
            toAddr.setGrowByContent(false);
            toAddr.setEditable(false);
            toAddr.setColumns(columns);
            drivingDirectionsInfo.addComponent(toAddr);
            boolean userChoice = drivingDirectionsInfo.showExtendedDialog();
            do {
                if (!userChoice) {
                    return;
                }
                if (UiUtil.isEmpty(fromAddrField.getText())) {
                    DialogUtil.showInfo("Error", "Please enter from address.");
                    userChoice = drivingDirectionsInfo.showExtendedDialog();
                }
            } while (UiUtil.isEmpty(fromAddrField.getText()));
            Thread t = new Thread(new Runnable() {

                public void run() {
                    try {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException ex) {
                            ex.printStackTrace();
                        }
                        DrivingDirections dd = new DrivingDirections(fromAddrField.getText(), getAddress());
                        final Vector routes = dd.getRoutes();
                        Route r = (Route) dd.getRoutes().elementAt(0);
                        addDrivingDirectionsTitle("Driving directions to " + r.getEndAddress());
                        //            addDrivingDirectionsInfo("Driving directions to " + r.getEndAddress(), Font.SIZE_MEDIUM, Font.STYLE_BOLD);
                        DrivingDirectionsSummary summary = new DrivingDirectionsSummary();
                        summary.addLine("Route Summary: " + r.getSummary());
                        summary.addLine("Total Time: " + r.getDuration().getText());
                        summary.addLine("Total Distance: " + r.getDistance().getText());
                        summary.addHorizontalLine();
                        addDrivingDirectionsSummary(summary);
                        //            addDrivingDirectionsInfo("Route Summary: " + r.getSummary(), Font.SIZE_SMALL, Font.STYLE_PLAIN);
                        //            addDrivingDirectionsInfo("Total Time: "+r.getDuration().getText(), Font.SIZE_SMALL, Font.STYLE_PLAIN);
                        //            addDrivingDirectionsInfo("Total Distance: "+r.getDistance().getText(), Font.SIZE_SMALL, Font.STYLE_PLAIN);
                        //            addDrivingDirectionsInfo(" ", Font.SIZE_MEDIUM, Font.STYLE_PLAIN);
                        addDrivingDirectionsMarker(r.getStartAddress(), "a");
                        //            addDrivingDirectionsInfo(r.getStartAddress(), Font.SIZE_SMALL, Font.STYLE_BOLD);
                        //            addDrivingDirectionsInfo("  ", Font.SIZE_MEDIUM, Font.STYLE_PLAIN);
                        Vector v = r.getSteps();
                        for (int i = 0; i < v.size(); i++) {
                            Steps s = (Steps) v.elementAt(i);
                            final String stepDetails = (i + 1) + ". " + s.getHtmlInstructions();
                            final String stepDetails2 = s.getDistance().getText() + "             " + s.getDuration().getText();
                            addDrivingDirectionsRoute(i + 1, s.getHtmlInstructions(), s.getDistance().getText(), s.getDuration().getText());
                            //                addDrivingDirectionsInfo(stepDetails, Font.SIZE_SMALL, Font.STYLE_BOLD);
                            //                addDrivingDirectionsInfo(stepDetails2, Font.SIZE_SMALL, Font.STYLE_PLAIN);
                            //                addDrivingDirectionsInfo("  ", Font.SIZE_MEDIUM, Font.STYLE_PLAIN);
                        }
                        addDrivingDirectionsMarker(r.getEndAddress(), "b");
                        //            addDrivingDirectionsInfo(r.getEndAddress(), Font.SIZE_SMALL, Font.STYLE_BOLD);
                        if (Display.getInstance().isEdt()) {
                            DialogUtil.hideWait();
                            drivingDirectionsDialog.showExtendedDialog();
                            form.show();
                        } else {
                            Display.getInstance().callSerially(new Runnable() {

                                public void run() {
                                    DialogUtil.hideWait();
                                    drivingDirectionsDialog.showExtendedDialog();
                                    form.show();
                                }
                            });
                        }
                    } catch (Exception ex) {
                        DialogUtil.hideWait();
                        ex.printStackTrace();
                        DialogUtil.showInfo("Error", "No routes found for this search.");
                    }
                }
            });
            t.start();
            DialogUtil.showWait();
        } catch (Exception ex) {
            DialogUtil.showInfo("Error", "No routes found for this search.");
        }
    }

    protected abstract void addBasicInfo();

    protected abstract void addFormSpecificSections();

    private void addSections() {
        addLinksSection();
        addFormSpecificSections();
    }

    private void addLinksSection() {
        links = new Section(form, "", "");
        addLink(LINK_MAPS);
        addMapSection();
        addAddtionalLinks();
        addLink(LINK_DRIVING_DIRECTIONS);
        addLink(VIDEO_LINK);
        addDrivingDirectionsSection();
    }

    protected void addDrivingDirectionsSection() {
        drivingDirectionsDialog = new ExtendedDialog();
        drivingDirectionsDialog.setLayout(new BoxLayout((BoxLayout.Y_AXIS)));
        drivingDirectionsDialog.setScrollable(true);
    }

    protected void addMapSection() {
        mapWidth = Display.getInstance().getDisplayWidth() - 2;
        mapHeight = Display.getInstance().getDisplayHeight() - 8 - FontUtil.getMediumNormalFont().getHeight();
        mapImageDialog = new ExtendedDialog();
        mapImageDialog.setScrollable(false);
        mapImage = new Label();
//        mapImage.setX(0);
//        mapImage.setY(0);
        mapImage.getStyle().setMargin(0, 0, 0, 0);
        mapImage.getStyle().setPadding(0, 0, 0, 0);
        mapImage.getSelectedStyle().setMargin(0, 0, 0, 0);
        mapImage.getSelectedStyle().setPadding(0, 0, 0, 0);
        mapImage.setPreferredW(mapWidth);
        mapImage.setPreferredH(mapHeight);
        mapImage.setWidth(mapWidth);
        mapImage.setHeight(mapHeight);
        mapImageDialog.addComponent(mapImage);
        mapImageDialog.getStyle().setMargin(0, 0, 0, 0);
        mapImageDialog.getStyle().setPadding(0, 0, 0, 0);
        mapImageDialog.getSelectedStyle().setMargin(0, 0, 0, 0);
        mapImageDialog.getSelectedStyle().setPadding(0, 0, 0, 0);
        mapImageDialog.setPreferredW(mapWidth);
        mapImageDialog.setPreferredH(mapHeight);
        mapImageDialog.setWidth(mapWidth);
        mapImageDialog.setHeight(mapHeight);
        final Container contentPane = mapImageDialog.getContentPane();
        contentPane.getStyle().setMargin(0, 0, 0, 0);
        contentPane.getStyle().setPadding(0, 0, 0, 0);
        contentPane.getSelectedStyle().setMargin(0, 0, 0, 0);
        contentPane.getSelectedStyle().setPadding(0, 0, 0, 0);
        contentPane.setPreferredW(mapWidth);
        contentPane.setPreferredH(mapHeight);
        contentPane.setWidth(mapWidth);
        contentPane.setHeight(mapHeight);
    }

    protected void hide(Component cmp) {
        cmp.setVisible(false);
        cmp.setPreferredH(0);
        cmp.setHeight(0);
    }

    protected void selectActionPerformed(String focussedName) {
    }

    protected abstract String getAddress();

    private void addDrivingDirectionsTitle(String titleMessage) {
        final WrappingLabel title = new WrappingLabel(titleMessage, Display.getInstance().getDisplayWidth() - 25, FontUtil.getMediumBoldFont());
//        title.getStyle().setFont(FontUtil.getMediumBoldFont());
//        title.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
        drivingDirectionsDialog.addComponent(title);
        final Label dummy = new Label(" ");
        dummy.getStyle().setFont(FontUtil.getSmallNormalFont());
        drivingDirectionsDialog.addComponent(dummy);
    }

    private void addDrivingDirectionsSummary(DrivingDirectionsSummary summary) {
        drivingDirectionsDialog.addComponent(summary);
    }

    private void addDrivingDirectionsMarker(String markerData, String markerLabel) {
        Container c = new Container(new BoxLayout((BoxLayout.X_AXIS)));
        c.addComponent(UiUtil.getImageLabel("/img/marker" + markerLabel + ".jpg"));
        final WrappingLabel dataLabel = new WrappingLabel(markerData, Display.getInstance().getDisplayWidth() - FontUtil.getMediumBoldFont().stringWidth("999") - 10, FontUtil.getMediumBoldFont());
        dataLabel.getStyle().setMargin(1, 0, 1, 0);
        dataLabel.getSelectedStyle().setMargin(1, 0, 1, 0);
        dataLabel.getStyle().setFont(FontUtil.getMediumBoldFont());
        dataLabel.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
        c.addComponent(dataLabel);
        drivingDirectionsDialog.addComponent(c);
        drivingDirectionsDialog.addComponent(getHorizontalLine());
    }

    private Label getHorizontalLine() {
        Label line = new Label(" ");
        line.setWidth(Display.getInstance().getDisplayWidth() - 10);
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        line.getStyle().setFont(smallFont);
        line.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(0x000000);
                g.fillRect(r.getX(), r.getY() + 10, r.getSize().getWidth(), 1);
            }
        });
        line.getStyle().setMargin(1, 0, 1, 0);
        line.getSelectedStyle().setMargin(1, 0, 1, 0);
        return line;
    }

    private void addDrivingDirectionsRoute(int step, String instructions, String distance, String duration) {
        Container routeContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        Label stepLabel = new Label(String.valueOf(step));
        stepLabel.getStyle().setMargin(1, 0, 1, 0);
        stepLabel.getSelectedStyle().setMargin(1, 0, 1, 0);
        stepLabel.getStyle().setFont(FontUtil.getMediumBoldFont());
        stepLabel.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
        final int indexWidth = FontUtil.getMediumBoldFont().stringWidth("999");
        stepLabel.setPreferredW(indexWidth);
        routeContainer.addComponent(stepLabel);

        Container detailsContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        WrappingLabel instructionsLabel = new WrappingLabel(instructions, Display.getInstance().getDisplayWidth() - indexWidth - 20, FontUtil.getMediumBoldFont());
        instructionsLabel.getStyle().setMargin(1, 0, 1, 0);
        instructionsLabel.getSelectedStyle().setMargin(1, 0, 1, 0);
//        instructionsLabel.getStyle().setFont(FontUtil.getMediumBoldFont());
//        instructionsLabel.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
        detailsContainer.addComponent(instructionsLabel);

        Container distanceDurationContainer = new Container(new BoxLayout((BoxLayout.X_AXIS)));
        Label distanceLabel = new Label(distance);
        distanceLabel.getStyle().setMargin(1, 0, 1, 0);
        distanceLabel.getSelectedStyle().setMargin(1, 0, 1, 0);
        distanceLabel.getStyle().setFont(FontUtil.getMediumNormalFont());
        distanceLabel.getSelectedStyle().setFont(FontUtil.getMediumNormalFont());
        distanceLabel.setPreferredW(FontUtil.getMediumNormalFont().stringWidth("0.35 KMSSSS"));
        distanceDurationContainer.addComponent(distanceLabel);

        Label durationLabel = new Label(duration);
        durationLabel.getStyle().setMargin(1, 0, 1, 0);
        durationLabel.getSelectedStyle().setMargin(1, 0, 1, 0);
        durationLabel.getStyle().setFont(FontUtil.getMediumNormalFont());
        durationLabel.getSelectedStyle().setFont(FontUtil.getMediumNormalFont());
        distanceDurationContainer.addComponent(durationLabel);
        detailsContainer.addComponent(distanceDurationContainer);
        routeContainer.addComponent(detailsContainer);

        drivingDirectionsDialog.addComponent(routeContainer);
        drivingDirectionsDialog.addComponent(getHorizontalLine());
    }

    protected String getAdditionalHeaderText() {
        return "";
    }

    protected void addAddtionalLinks() {
    }

    private boolean isMovie(Result result) {
        final String type = result.getProperty("_Type");
        return "Movie".equals(type);
    }

    private boolean isEvent(Result result) {
        final String type = result.getProperty("_Type");
        return "Event".equals(type);
    }
}
