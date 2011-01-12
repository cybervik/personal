/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.service;

import com.wbs.SMSScheduler;
import com.wbs.form.Event;
import com.wbs.form.EventsListForm;
import com.wbs.logging.Logger;
import java.util.Vector;
import javax.microedition.rms.InvalidRecordIDException;
import javax.microedition.rms.RecordEnumeration;
import javax.microedition.rms.RecordStore;
import javax.microedition.rms.RecordStoreException;
import javax.microedition.rms.RecordStoreFullException;
import javax.microedition.rms.RecordStoreNotFoundException;
import javax.microedition.rms.RecordStoreNotOpenException;

/**
 *
 * @author Vikram S
 */
public class PersistenceService {

    private static void closeEventsStore(RecordStore rs) {
        if (rs != null) {
            try {
                rs.closeRecordStore();
            } catch (RecordStoreNotOpenException ex) {
                Logger.logError("RecordStoreNotOpenException - " + ex.getMessage());
            } catch (RecordStoreException ex) {
                Logger.logError("RecordStoreException - " + ex.getMessage());
            } catch (Exception e) {
                Logger.logError("Exception - " + e.getMessage());
            }
        }
    }

    private static void deleteEventsStore() {
        try {
            RecordStore.deleteRecordStore(SMSScheduler.STORE_NAME);
        } catch (RecordStoreException ex) {
            Logger.logInfo(ex.getMessage());
        }
    }

    private static RecordEnumeration getIterator(RecordStore rs) {
        RecordEnumeration iterEnumeration = null;
        try {
            iterEnumeration = rs.enumerateRecords(null, null, true);
            Logger.logInfo("There are - " + iterEnumeration.numRecords() + " records");
        } catch (RecordStoreNotOpenException ex) {
            Logger.logError(ex.getMessage());
        }
        return iterEnumeration;
    }

    private static byte[] getNextRecord(RecordEnumeration iterator) {
        byte[] data = null;
        try {
            data = iterator.nextRecord();
        } catch (InvalidRecordIDException ex) {
            Logger.logError(ex.getMessage());
        } catch (RecordStoreNotOpenException ex) {
            Logger.logError(ex.getMessage());
        } catch (RecordStoreException ex) {
            Logger.logError(ex.getMessage());
        }
        return data;
    }

    private static RecordStore openEventsStore() {
        Logger.logInfo("Opening record store");
        RecordStore rs = null;
        try {
            rs = RecordStore.openRecordStore(SMSScheduler.STORE_NAME, true);
        } catch (RecordStoreFullException ex) {
            Logger.logError(ex.getMessage());
        } catch (RecordStoreNotFoundException ex) {
            Logger.logError(ex.getMessage());
        } catch (RecordStoreException ex) {
            Logger.logError(ex.getMessage());
        }
        return rs;
    }

    private static Vector readEvents(RecordStore rs) {
        Logger.logInfo("Reading events");
        Vector events = new Vector();
        if (rs == null) {
            Logger.logError("Record store is null");
            return events;
        }
        RecordEnumeration iterator = getIterator(rs);
        if (iterator == null) {
            Logger.logError("Record iterator is null");
            return events;
        }
        do {
            byte[] data = getNextRecord(iterator);
            if (data == null) {
                Logger.logError("Record data is null");
                break;
            }
            Event e = createEvent(data);
            events.addElement(e);
        } while (iterator.hasNextElement());

        return events;
    }

    public static void storeEvents(Vector events) {
        deleteEventsStore();
        RecordStore rs = null;
        rs = openEventsStore();
        for (int i = 0; i < events.size(); i++) {
            final Event event = (Event) events.elementAt(i);
            final String eventAsStr = event.getStorableFormat();
            storeRecord(rs, eventAsStr);
        }
        closeEventsStore(rs);
    }

    private static void storeRecord(RecordStore rs, final String data) {
        Logger.logInfo("Persisting - " + data);
        try {
            rs.addRecord(data.getBytes(), 0, data.getBytes().length);
        } catch (RecordStoreNotOpenException ex) {
            Logger.logError(ex.getMessage());
        } catch (RecordStoreFullException ex) {
            Logger.logError(ex.getMessage());
        } catch (RecordStoreException ex) {
            Logger.logError(ex.getMessage());
        }
    }

    private PersistenceService() {
    }

    public static Vector readEvents() {
        Vector events = new Vector();
        RecordStore rs = null;
        try {
            rs = openEventsStore();
            events = readEvents(rs);
        } catch (Exception e) {
            Logger.logError("Exception - " + e.getMessage());
        } finally {
            closeEventsStore(rs);
        }
        Logger.logInfo("Loaded events");
        return events;
    }

    private static Event createEvent(final byte[] data) {
        final String dataStr = new String(data);
        Logger.logInfo("Loading - " + dataStr);
        Event e = new Event();
        e.setFromFormat(dataStr);
        Logger.logInfo("Event generated");
        return e;
    }
}
