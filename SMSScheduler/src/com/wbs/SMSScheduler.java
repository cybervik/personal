/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs;

import com.wbs.service.SchedulerService;
import com.wbs.form.EventsListForm;
import com.sun.lwuit.Display;
import com.wbs.service.EventsService;
import com.wbs.service.PersistenceService;
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
        scheduleEventProcessing();
    }

    private void init() {
        Display.init(this);
        Display.getInstance().setThirdSoftButton(true);
        displayWidth = Display.getInstance().getDisplayWidth();
        eventsListForm = new EventsListForm(this);
        SchedulerService.removeScheduler();
    }

    private void scheduleEventProcessing() {
        TimerTask timerTask = new TimerTask() {

            public void run() {
                Display.getInstance().callSerially(new Runnable() {

                    public void run() {
                        EventsService.processEvents(eventsListForm.getEvents());
                        eventsListForm.reloadEventsIfNecessary();
                    }
                });
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 60 * 1000, 60 * 1000);
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
        Runnable r = new Runnable() {

            public void run() {
                destroyApp(true);
                notifyDestroyed();
            }
        };
        final Display display = Display.getInstance();
        display.callSeriallyAndWait(r);
        display.deinitialize();
    }
}
