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
class PushRegistryService {

    public long registerAlarm() {
        try {
            Date alarm = new Date();
            final int intervalInSecs = 60;
            final int intervalInMillis = intervalInSecs * 1000;
            final long dateInMillis = alarm.getTime() + intervalInMillis;
            final String cname = Background.class.getName();
            log("Class name for alarm - " + cname);
            long prevTime = PushRegistry.registerAlarm(cname, dateInMillis);
            log("Push registery - Will wake up @ " + new Date(dateInMillis) + ", previously suppose to run @ " + new Date(prevTime));
            return prevTime;
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        } catch (ConnectionNotFoundException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    private void log(String logMsg) {
        System.out.println(new Date() + ": " + logMsg);
    }

    public long registerAlarm(boolean threaded) {
        if (threaded) {
            final Runnable r = new Runnable() {

                public void run() {
                    PushRegistryService prs = new PushRegistryService();
                    prs.registerAlarm();
                }
            };
            new Thread(r).start();
        } else {
            return registerAlarm();
        }
        return 0;
    }
}
