/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs;

import com.wbs.form.EventsListForm;
import com.sun.lwuit.Display;
import com.sun.lwuit.Form;
import javax.microedition.midlet.*;

/**
 * @author Vikram S
 */
public class SMSScheduler extends MIDlet {

    public int DISPLAY_WIDTH;
    private EventsListForm eventsListForm;

    public void startApp() {
        initialize();
    }

    private void initialize() {
        Display.init(this);
        DISPLAY_WIDTH = Display.getInstance().getDisplayWidth();
        eventsListForm = new EventsListForm(this);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
    }

    public EventsListForm getListForm() {
        return this.eventsListForm;
    }
}
