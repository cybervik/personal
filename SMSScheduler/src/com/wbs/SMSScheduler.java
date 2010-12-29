/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs;

import com.wbs.form.EventsListForm;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import com.wbs.form.Event;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.PushRegistry;
import javax.microedition.midlet.*;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 * @author Vikram S
 */
public class SMSScheduler extends MIDlet {

    public static final String STORE_NAME = "SMSScheduler";
    public int DISPLAY_WIDTH;
    private EventsListForm eventsListForm;
    Vector events = new Vector();

    public Vector getEvents() {
        return events;
    }

    public void startApp() {
        initialize();
    }

    private void createEvent(final byte[] data) {
        final String dataStr = new String(data);
        log(dataStr);
        Event ev = new Event();
        ev.setFromFormat(dataStr);
        events.addElement(ev);
    }

    private void initialize() {
        Display.init(this);
        DISPLAY_WIDTH = Display.getInstance().getDisplayWidth();
        loadData();
        eventsListForm = new EventsListForm(this);
        TimerTask timerTask = new TimerTask() {

            public void run() {
                eventsListForm.processPendingEvents();
            }
        };
        Timer timer = new Timer();
        timer.schedule(timerTask, 0, 60 * 1000);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        persist();
        loadData();
        try {
            String cn = BackgroundProcessor.class.getName();
            Date alarm = new Date();
            long t = PushRegistry.registerAlarm(cn, alarm.getTime() + 60000);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void persist() {
        try {
            RecordStore.deleteRecordStore(STORE_NAME);
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        }
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(STORE_NAME, true);
            final Vector events = eventsListForm.getEvents();
            for (int i = 0; i < events.size(); i++) {
                final Event event = (Event) events.elementAt(i);
                final String dataStr = event.getStorableFormat();
                log("Persisting - " + dataStr);
                rs.addRecord(dataStr.getBytes(), 0, dataStr.getBytes().length);
            }
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreNotOpenException ex) {
                ex.printStackTrace();
            } catch (RecordStoreException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void log(String msg) {
        System.out.println(msg);
    }

    public EventsListForm getListForm() {
        return this.eventsListForm;
    }

    private void loadData() {
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(STORE_NAME, true);
            final RecordEnumeration e = rs.enumerateRecords(null, null, true);
            do {
                byte[] data = e.nextRecord();
                createEvent(data);
            } while (e.hasNextElement());
        } catch (RecordStoreException ex) {
            ex.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.closeRecordStore();
                } catch (RecordStoreNotOpenException ex) {
                    ex.printStackTrace();
                } catch (RecordStoreException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
