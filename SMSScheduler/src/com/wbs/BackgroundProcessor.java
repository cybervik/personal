/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs;

import java.util.Date;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.PushRegistry;
import javax.microedition.midlet.*;

/**
 * @author Vikram S
 */
public class BackgroundProcessor extends MIDlet {

    public void startApp() {
        final String msg = "Running";
        log(msg);
    }

    private void log(final String msg) {
        System.out.println("BackgroundProcessor: "+msg);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        try {
            String cn = BackgroundProcessor.class.getName();
            Date alarm = new Date();
            long t = PushRegistry.registerAlarm(cn, alarm.getTime() + 60000);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
        log("Exiting");
    }
}
