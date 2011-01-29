/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Label;
import com.wbs.service.SchedulerService;
import com.wbs.form.EventsListForm;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.wbs.form.decorator.FontUtil;
import com.wbs.service.EventsService;
import com.wbs.service.PersistenceService;
import com.wbs.utils.DialogUtil;
import com.wbs.utils.UiUtil;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.midlet.*;

/**
 * @author Vikram S
 */
public class SMSScheduler extends MIDlet {

    public static final String STORE_NAME = "SMSScheduler";
    public int displayWidth;
    private EventsListForm eventsListForm;

    public void startApp() {
        init();
//        scheduleEventProcessing();
    }

    private void init() {
        Display.init(this);
        Display.getInstance().setThirdSoftButton(true);
        displayWidth = Display.getInstance().getDisplayWidth();
        showSplashScreen();
        eventsListForm = new EventsListForm(this);
        removeBackgroundProcessing();
        scheduleForegroundProcessing();
    }

    private void removeBackgroundProcessing() {
        SchedulerService.removeScheduler();
    }

    private void scheduleForegroundProcessing() {
        TimerTask timerTask = new TimerTask() {

            public void run() {
                EventsService.processEvents(eventsListForm.getEvents());
                eventsListForm.reloadEventsIfNecessary();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, SchedulerService.SCHEDULE_INTERVAL_IN_MILLIS);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        persist();
        scheduleBackgroundProcess();
    }

    private void scheduleBackgroundProcess() {
        SchedulerService.scheduleForDefaultInterval();
    }

    public EventsListForm getListForm() {
        return this.eventsListForm;
    }

    private void persist() {
        final Vector events = eventsListForm.getEvents();
        PersistenceService.storeEvents(events);
    }

    public void exit() {
        if (DialogUtil.showConfirm("Exit Confirm", "Are you sure you want to exit?")) {
            destroyApp(true);
            notifyDestroyed();
        }
    }

    private void showSplashScreen() {
        Form flashForm = new Form();
        flashForm.setLayout(new BorderLayout());
        Container flashContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        final Label logo = UiUtil.getImageLabel("/img/logo.jpg", Display.getInstance().getDisplayWidth() - 10);
        logo.setAlignment(Label.CENTER);
        flashContainer.addComponent(logo);

        Label applicationName = new Label(EventsListForm.APP_NAME);
        Font mediumFont = FontUtil.getLargeBoldFont();
        applicationName.getStyle().setFont(mediumFont);
        applicationName.setAlignment(Component.CENTER);
        applicationName.setAlignment(Label.CENTER);
        flashContainer.addComponent(applicationName);

        flashForm.addComponent(BorderLayout.NORTH, new Label(" "));
        flashForm.addComponent(BorderLayout.CENTER, flashContainer);
        flashForm.show();
        try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
