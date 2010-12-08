
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;
import org.kxml2.io.KXmlParser;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

/**
 * @author Vikram Subbarao
 */
public class RssSearch extends MIDlet implements CommandListener, Runnable {

    private boolean midletPaused = false;
    //<editor-fold defaultstate="collapsed" desc=" Generated Fields ">//GEN-BEGIN:|fields|0|
    private Command exitCommandFromSearch;
    private Command searchCommand;
    private Command backCommand;
    private Command exitCommandFromResult;
    private Form searchForm;
    private TextField textField;
    private TextField searchString;
    private StringItem message;
    private List resultsList;
    private Font font;
    //</editor-fold>//GEN-END:|fields|0|

    /**
     * The RssSearch constructor.
     */
    public RssSearch() {
    }

    //<editor-fold defaultstate="collapsed" desc=" Generated Methods ">//GEN-BEGIN:|methods|0|
    //</editor-fold>//GEN-END:|methods|0|
    //<editor-fold defaultstate="collapsed" desc=" Generated Method: initialize ">//GEN-BEGIN:|0-initialize|0|0-preInitialize
    /**
     * Initilizes the application.
     * It is called only once when the MIDlet is started. The method is called before the <code>startMIDlet</code> method.
     */
    private void initialize() {//GEN-END:|0-initialize|0|0-preInitialize
        // write pre-initialize user code here
//GEN-LINE:|0-initialize|1|0-postInitialize
        // write post-initialize user code here
    }//GEN-BEGIN:|0-initialize|2|
    //</editor-fold>//GEN-END:|0-initialize|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: startMIDlet ">//GEN-BEGIN:|3-startMIDlet|0|3-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Started point.
     */
    public void startMIDlet() {//GEN-END:|3-startMIDlet|0|3-preAction
        // write pre-action user code here
        switchDisplayable(null, getSearchForm());//GEN-LINE:|3-startMIDlet|1|3-postAction
        // write post-action user code here
    }//GEN-BEGIN:|3-startMIDlet|2|
    //</editor-fold>//GEN-END:|3-startMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resumeMIDlet ">//GEN-BEGIN:|4-resumeMIDlet|0|4-preAction
    /**
     * Performs an action assigned to the Mobile Device - MIDlet Resumed point.
     */
    public void resumeMIDlet() {//GEN-END:|4-resumeMIDlet|0|4-preAction
        // write pre-action user code here
//GEN-LINE:|4-resumeMIDlet|1|4-postAction
        // write post-action user code here
    }//GEN-BEGIN:|4-resumeMIDlet|2|
    //</editor-fold>//GEN-END:|4-resumeMIDlet|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: switchDisplayable ">//GEN-BEGIN:|5-switchDisplayable|0|5-preSwitch
    /**
     * Switches a current displayable in a display. The <code>display</code> instance is taken from <code>getDisplay</code> method. This method is used by all actions in the design for switching displayable.
     * @param alert the Alert which is temporarily set to the display; if <code>null</code>, then <code>nextDisplayable</code> is set immediately
     * @param nextDisplayable the Displayable to be set
     */
    public void switchDisplayable(Alert alert, Displayable nextDisplayable) {//GEN-END:|5-switchDisplayable|0|5-preSwitch
        // write pre-switch user code here
        Display display = getDisplay();//GEN-BEGIN:|5-switchDisplayable|1|5-postSwitch
        if (alert == null) {
            display.setCurrent(nextDisplayable);
        } else {
            display.setCurrent(alert, nextDisplayable);
        }//GEN-END:|5-switchDisplayable|1|5-postSwitch
        // write post-switch user code here
    }//GEN-BEGIN:|5-switchDisplayable|2|
    //</editor-fold>//GEN-END:|5-switchDisplayable|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: commandAction for Displayables ">//GEN-BEGIN:|7-commandAction|0|7-preCommandAction
    /**
     * Called by a system to indicated that a command has been invoked on a particular displayable.
     * @param command the Command that was invoked
     * @param displayable the Displayable where the command was invoked
     */
    public void commandAction(Command command, Displayable displayable) {//GEN-END:|7-commandAction|0|7-preCommandAction
        // write pre-action user code here
        if (displayable == resultsList) {//GEN-BEGIN:|7-commandAction|1|43-preAction
            if (command == List.SELECT_COMMAND) {//GEN-END:|7-commandAction|1|43-preAction
                // write pre-action user code here
                resultsListAction();//GEN-LINE:|7-commandAction|2|43-postAction
                // write post-action user code here
            } else if (command == backCommand) {//GEN-LINE:|7-commandAction|3|46-preAction
                // write pre-action user code here
                switchDisplayable(null, getSearchForm());//GEN-LINE:|7-commandAction|4|46-postAction
                // write post-action user code here
            } else if (command == exitCommandFromResult) {//GEN-LINE:|7-commandAction|5|48-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|6|48-postAction
                // write post-action user code here
            }//GEN-BEGIN:|7-commandAction|7|19-preAction
        } else if (displayable == searchForm) {
            if (command == exitCommandFromSearch) {//GEN-END:|7-commandAction|7|19-preAction
                // write pre-action user code here
                exitMIDlet();//GEN-LINE:|7-commandAction|8|19-postAction
                // write post-action user code here
            } else if (command == searchCommand) {//GEN-LINE:|7-commandAction|9|26-preAction
//GEN-LINE:|7-commandAction|10|26-postAction
                String rssUrl = textField.getString();
                String searchString = this.searchString.getString();
                if (isNullOrEmpty(rssUrl) || isNullOrEmpty(searchString)) {
                    Alert errorMessage = new Alert("Error", "Please enter a valid Rss Url and search text", null, AlertType.ERROR);
                    errorMessage.setTimeout(Alert.FOREVER);
                    getDisplay().setCurrent(errorMessage);
                } else {
                    Thread thread = new Thread(this);
                    thread.start();
                }
            }//GEN-BEGIN:|7-commandAction|11|7-postCommandAction
        }//GEN-END:|7-commandAction|11|7-postCommandAction
        // write post-action user code here
    }//GEN-BEGIN:|7-commandAction|12|
    //</editor-fold>//GEN-END:|7-commandAction|12|
    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommandFromSearch ">//GEN-BEGIN:|18-getter|0|18-preInit
    /**
     * Returns an initiliazed instance of exitCommandFromSearch component.
     * @return the initialized component instance
     */
    public Command getExitCommandFromSearch() {
        if (exitCommandFromSearch == null) {//GEN-END:|18-getter|0|18-preInit
            // write pre-init user code here
            exitCommandFromSearch = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|18-getter|1|18-postInit
            // write post-init user code here
        }//GEN-BEGIN:|18-getter|2|
        return exitCommandFromSearch;
    }
    //</editor-fold>//GEN-END:|18-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: searchForm ">//GEN-BEGIN:|14-getter|0|14-preInit
    /**
     * Returns an initiliazed instance of searchForm component.
     * @return the initialized component instance
     */
    public Form getSearchForm() {
        if (searchForm == null) {//GEN-END:|14-getter|0|14-preInit
            // write pre-init user code here
            searchForm = new Form("Rss Search", new Item[] { getTextField(), getSearchString(), getMessage() });//GEN-BEGIN:|14-getter|1|14-postInit
            searchForm.addCommand(getExitCommandFromSearch());
            searchForm.addCommand(getSearchCommand());
            searchForm.setCommandListener(this);//GEN-END:|14-getter|1|14-postInit
            // write post-init user code here
        }//GEN-BEGIN:|14-getter|2|
        return searchForm;
    }
    //</editor-fold>//GEN-END:|14-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: searchCommand ">//GEN-BEGIN:|25-getter|0|25-preInit
    /**
     * Returns an initiliazed instance of searchCommand component.
     * @return the initialized component instance
     */
    public Command getSearchCommand() {
        if (searchCommand == null) {//GEN-END:|25-getter|0|25-preInit
            // write pre-init user code here
            searchCommand = new Command("Search", Command.OK, 0);//GEN-LINE:|25-getter|1|25-postInit
            // write post-init user code here
        }//GEN-BEGIN:|25-getter|2|
        return searchCommand;
    }
    //</editor-fold>//GEN-END:|25-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: textField ">//GEN-BEGIN:|23-getter|0|23-preInit
    /**
     * Returns an initiliazed instance of textField component.
     * @return the initialized component instance
     */
    public TextField getTextField() {
        if (textField == null) {//GEN-END:|23-getter|0|23-preInit
            // write pre-init user code here
            textField = new TextField("Rss Url", "", 300, TextField.ANY);//GEN-LINE:|23-getter|1|23-postInit
            // write post-init user code here
        }//GEN-BEGIN:|23-getter|2|
        return textField;
    }
    //</editor-fold>//GEN-END:|23-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: searchString ">//GEN-BEGIN:|24-getter|0|24-preInit
    /**
     * Returns an initiliazed instance of searchString component.
     * @return the initialized component instance
     */
    public TextField getSearchString() {
        if (searchString == null) {//GEN-END:|24-getter|0|24-preInit
            // write pre-init user code here
            searchString = new TextField("Search Text", "", 32, TextField.ANY);//GEN-LINE:|24-getter|1|24-postInit
            // write post-init user code here
        }//GEN-BEGIN:|24-getter|2|
        return searchString;
    }
    //</editor-fold>//GEN-END:|24-getter|2|






    //</editor-fold>
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: backCommand ">//GEN-BEGIN:|45-getter|0|45-preInit
    /**
     * Returns an initiliazed instance of backCommand component.
     * @return the initialized component instance
     */
    public Command getBackCommand() {
        if (backCommand == null) {//GEN-END:|45-getter|0|45-preInit
            // write pre-init user code here
            backCommand = new Command("Back", Command.BACK, 0);//GEN-LINE:|45-getter|1|45-postInit
            // write post-init user code here
        }//GEN-BEGIN:|45-getter|2|
        return backCommand;
    }
    //</editor-fold>//GEN-END:|45-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: exitCommandFromResult ">//GEN-BEGIN:|47-getter|0|47-preInit
    /**
     * Returns an initiliazed instance of exitCommandFromResult component.
     * @return the initialized component instance
     */
    public Command getExitCommandFromResult() {
        if (exitCommandFromResult == null) {//GEN-END:|47-getter|0|47-preInit
            // write pre-init user code here
            exitCommandFromResult = new Command("Exit", Command.EXIT, 0);//GEN-LINE:|47-getter|1|47-postInit
            // write post-init user code here
        }//GEN-BEGIN:|47-getter|2|
        return exitCommandFromResult;
    }
    //</editor-fold>//GEN-END:|47-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: resultsList ">//GEN-BEGIN:|41-getter|0|41-preInit
    /**
     * Returns an initiliazed instance of resultsList component.
     * @return the initialized component instance
     */
    public List getResultsList() {
        if (resultsList == null) {//GEN-END:|41-getter|0|41-preInit
            // write pre-init user code here
            resultsList = new List("Results", Choice.IMPLICIT);//GEN-BEGIN:|41-getter|1|41-postInit
            resultsList.addCommand(getBackCommand());
            resultsList.addCommand(getExitCommandFromResult());
            resultsList.setCommandListener(this);
            resultsList.setFitPolicy(Choice.TEXT_WRAP_DEFAULT);
            resultsList.setSelectedFlags(new boolean[] {  });//GEN-END:|41-getter|1|41-postInit
            // write post-init user code here
        }//GEN-BEGIN:|41-getter|2|
        return resultsList;
    }
    //</editor-fold>//GEN-END:|41-getter|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Method: resultsListAction ">//GEN-BEGIN:|41-action|0|41-preAction
    /**
     * Performs an action assigned to the selected list element in the resultsList component.
     */
    public void resultsListAction() {//GEN-END:|41-action|0|41-preAction
        // enter pre-action user code here
        String __selectedString = getResultsList().getString(getResultsList().getSelectedIndex());//GEN-LINE:|41-action|1|41-postAction
        // enter post-action user code here
    }//GEN-BEGIN:|41-action|2|
    //</editor-fold>//GEN-END:|41-action|2|

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: font ">//GEN-BEGIN:|56-getter|0|56-preInit
    /**
     * Returns an initiliazed instance of font component.
     * @return the initialized component instance
     */
    public Font getFont() {
        if (font == null) {//GEN-END:|56-getter|0|56-preInit
            // write pre-init user code here
            font = Font.getDefaultFont();//GEN-LINE:|56-getter|1|56-postInit
            // write post-init user code here
        }//GEN-BEGIN:|56-getter|2|
        return font;
    }
    //</editor-fold>//GEN-END:|56-getter|2|
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc=" Generated Getter: message ">//GEN-BEGIN:|57-getter|0|57-preInit
    /**
     * Returns an initiliazed instance of message component.
     * @return the initialized component instance
     */
    public StringItem getMessage() {
        if (message == null) {//GEN-END:|57-getter|0|57-preInit
            // write pre-init user code here
            message = new StringItem("", "");//GEN-LINE:|57-getter|1|57-postInit
            // write post-init user code here
        }//GEN-BEGIN:|57-getter|2|
        return message;
    }
    //</editor-fold>//GEN-END:|57-getter|2|

    /**
     * Returns a display instance.
     * @return the display instance.
     */
    public Display getDisplay() {
        return Display.getDisplay(this);
    }

    /**
     * Exits MIDlet.
     */
    public void exitMIDlet() {
        switchDisplayable(null, null);
        destroyApp(true);
        notifyDestroyed();
    }

    /**
     * Called when MIDlet is started.
     * Checks whether the MIDlet has been already started and initialize/starts or resumes the MIDlet.
     */
    public void startApp() {
        if (midletPaused) {
            resumeMIDlet();
        } else {
            initialize();
            startMIDlet();
        }
        midletPaused = false;
    }

    /**
     * Called when MIDlet is paused.
     */
    public void pauseApp() {
        midletPaused = true;
    }

    /**
     * Called to signal the MIDlet to terminate.
     * @param unconditional if true, then the MIDlet has to be unconditionally terminated and all resources has to be released.
     */
    public void destroyApp(boolean unconditional) {
    }

    private boolean isNullOrEmpty(String str) {
        return str == null || "".equals(str);
    }

    public void run() {
        String rssUrl = textField.getString();
        HttpConnection conn;
        try {
            message.setText("Processing. Please wait...");
            conn = (HttpConnection) Connector.open(rssUrl);
            conn.setRequestMethod(HttpConnection.GET);
            processResponse(conn);
            message.setText("Displaying Results");
            getDisplay().setCurrent(getResultsList());
            message.setText("");
        } catch (Exception e) {
            message.setText("Error occured - " + e.toString());
            getDisplay().setCurrent(searchForm);
        }
    }

    private void processResponse(HttpConnection conn) throws IOException, XmlPullParserException {
        InputStream input = conn.openInputStream();
        KXmlParser parser = new KXmlParser();
        parser.setInput(new InputStreamReader(input));
        parser.nextTag();
        parser.require(XmlPullParser.START_TAG, null, "rss");
        parser.nextTag();

        int eventCode = parser.getEventType();

        boolean item = false;
        boolean titleTag = false;
        boolean descriptionTag = false;

        String tagName = "";
        String title = "";
        String description = "";

        getResultsList().deleteAll();

        while (eventCode != XmlPullParser.END_DOCUMENT) {
            switch (parser.getEventType()) {
                case XmlPullParser.START_TAG:
                    tagName = parser.getName();
                    if ("item".equals(tagName)) {
                        item = true;
                    } else if (item && "title".equals(tagName)) {
                        titleTag = true;
                    } else if (item && "description".equals(tagName)) {
                        descriptionTag = true;
                    }
                    break;
                case XmlPullParser.END_TAG:
                    tagName = parser.getName();
                    if ("item".equals(tagName)) {
                        item = false;
                        titleTag = false;
                        descriptionTag = false;
                        if (title.toLowerCase().indexOf(searchString.getString().toLowerCase()) > -1) {
                            List res = getResultsList();
                            int pos = res.append(title, null);
                            resultsList.append(" - "+description, null);
                            resultsList.append("", null);
                        }
                        title = "";
                        description = "";
                    } else if ("title".equals(tagName)) {
                        titleTag = false;
                    } else if ("description".equals(tagName)) {
                        descriptionTag = false;
                    }
                    break;
                case XmlPullParser.TEXT:
                    String text = parser.getText();
                    if (titleTag) {
                        title = text;
                    } else if (descriptionTag) {
                        description = text;
                    }
                    break;
            }
            parser.next();
            eventCode = parser.getEventType();
        }
        //return res.toString();
    }
}
