/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.service;

import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.wbs.form.Event;
import com.wbs.form.PhoneEntry;
import com.wbs.form.decorator.FormCommonDecorator;
import com.wbs.logging.Logger;
import com.wbs.utils.DialogUtil;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class EventsService {

    public static void processEvents(Vector events) {
        final int eventsCount = events.size();
        Logger.logInfo("Processing " + eventsCount + " events");
        for (int i = 0; i < eventsCount; i++) {
            final Event e = (Event) events.elementAt(i);
            if (!e.isDue()) {
                return;
            }
            final String message = e.getMessage();
            if (e.isSelfEvent()) {
                Logger.logInfo("Showing alert - "+message);
                DialogUtil.showInfo("Alert", message);
            } else {
                sendSmsToRecipients(e);
            }
            if (e.isRecurring()) {
                e.updateDueDate();
            } else {
                events.removeElementAt(i);
            }
        }
    }

    private static void sendSmsToRecipients(final Event e) {
        final Vector recipients = e.getRecipients();
        final String message = e.getMessage();
        for (int j = 0; j < recipients.size(); j++) {
            final PhoneEntry p = (PhoneEntry) recipients.elementAt(j);
            final String telePhone = p.getTelePhone();
            SMSService.sendSms(telePhone, message);
            DialogUtil.showInfo("Alert", "Sent sms to " + telePhone + " with message \"" + message + "\"");
        }
    }
}
