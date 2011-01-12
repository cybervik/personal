/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs;

import com.wbs.service.EventsService;
import com.wbs.service.PersistenceService;
import com.wbs.logging.Logger;
import com.wbs.service.SMSService;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.wbs.form.Event;
import com.wbs.form.PhoneEntry;
import com.wbs.service.SchedulerService;
import java.util.Vector;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * @author Vikram S
 */
public class BackgroundProcessor extends MIDlet {

    private Vector events;

    public BackgroundProcessor() {
        events = new Vector();
    }

    public void startApp() {
        init();
        load();
        process();
        persist();
        exit();
    }

    private void exit() {
        destroyApp(true);
        notifyDestroyed();
    }

    private void init() {
        Display.init(this);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        scheduleNextRun();
        Logger.logInfo("Background processor exiting");
    }

    private void scheduleNextRun() {
        SchedulerService.scheduleForDefaultInterval();
    }

    private void persist() {
        PersistenceService.storeEvents(events);
    }

    private void load() {
        events = PersistenceService.readEvents();
    }

    private void process() {
        EventsService.processEvents(events);
    }
}
