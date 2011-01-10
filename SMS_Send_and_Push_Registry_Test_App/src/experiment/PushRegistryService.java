/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import java.util.Date;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.PushRegistry;

/**
 *
 * @author Vikram S
 */
class PushRegistryService implements Runnable {

    static void registerAlarm() {
        Thread t = new Thread(new PushRegistryService());
        t.start();
    }

    public void run() {
        try {
            Date alarm = new Date();
            final int intervalInSecs = 60;
            final int intervalInMillis = intervalInSecs * 1000;
            final long dateInMillis = alarm.getTime() + intervalInMillis;
            PushRegistry.registerAlarm(SMSSendandPushRegistryTester.class.getName(), dateInMillis);
            log("Push registery - Will wake up @ " + new Date(dateInMillis));
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private void log(String logMsg) {
        System.out.println(new Date() + ": " + logMsg);
    }
}
