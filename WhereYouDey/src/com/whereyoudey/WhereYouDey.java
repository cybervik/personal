package com.whereyoudey;

import com.sun.lwuit.Dialog;
import java.io.IOException;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import com.whereyoudey.form.SearchForm;

public class WhereYouDey extends MIDlet {

    public static final int FLASH_LOGO_DURATION = 5 * 1000;
    SearchForm searchForm;
    private Form flashForm;

    public SearchForm getSearchForm() {
        return searchForm;
    }

    public WhereYouDey() {
        // TODO Auto-generated constructor stub
    }

    public void destroyApp(boolean unconditional)
            throws MIDletStateChangeException {
        // TODO Auto-generated method stub
    }

    protected void pauseApp() {
        // TODO Auto-generated method stub
    }

    protected void startApp() throws MIDletStateChangeException {
        init();
        Image flashLogo;
        try {
            flashLogo = Image.createImage("/img/Intropage.jpg");
            flashLogo = flashLogo.scaledWidth(Display.getInstance().getDisplayWidth());
            flashForm = new Form();
            flashForm.setLayout(new BorderLayout());
            flashForm.addComponent(BorderLayout.CENTER, new Label(flashLogo));
            flashForm.show();
            try {
                Thread.sleep(FLASH_LOGO_DURATION);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        searchForm = new SearchForm(this);
    }

    private void init() {
        Display.init(this);
    }

    public void exit() {
        try {
            if (userConfirmsExit()) {
                destroyApp(true);
                notifyDestroyed();
            }
        } catch (MIDletStateChangeException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private boolean userConfirmsExit() {
        return Dialog.show("Confirm Exit", "Do you really want to exit?", "Ok", "Cancel");
    }
}
