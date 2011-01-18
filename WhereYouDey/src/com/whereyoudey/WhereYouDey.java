package com.whereyoudey;

import com.sun.lwuit.Dialog;
import java.io.IOException;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.midlet.MIDlet;
import javax.microedition.midlet.MIDletStateChangeException;

import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import com.whereyoudey.form.BusinessSearchForm;
import com.whereyoudey.service.SearchService;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.DialogUtil;
import java.security.spec.X509EncodedKeySpec;
import java.util.Random;

public class WhereYouDey extends MIDlet {

    public static final int FLASH_LOGO_DURATION = 5 * 1000;
    BusinessSearchForm searchForm;
    private Form flashForm;
    private Result[] banners;
    private int bannerPos = 0;

    public BusinessSearchForm getSearchForm() {
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
        showFlashLogo();
        searchForm = new BusinessSearchForm(this);
    }

    private void showFlashLogo() {
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
    }

    private void init() {
        Display.init(this);
        final Display display = Display.getInstance();
        int x = display.getDisplayWidth();
        int y = display.getDisplayHeight();
        System.out.println("Display size - " + x + " x " + y);
        display.setThirdSoftButton(true);
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
        return DialogUtil.showConfirm("Confirm Exit", "Do you really want to exit?");
    }

    public void requestPlatformService(String url) {
        try {
            platformRequest(url);
        } catch (Exception e) {
        }
    }

    public void setBanners(Result[] banners) {
        this.banners = banners;
    }

    public Result getRandomBanner() {
        if (banners != null && banners.length > 0) {
            final Result banner = banners[bannerPos];
            bannerPos++;
            if (bannerPos == SearchService.MAX_RESULTS) {
                bannerPos = 0;
            }
            return banner;
        }
        return null;
    }

    public boolean hasBanners() {
        return (banners != null && banners.length > 0);
    }
}
