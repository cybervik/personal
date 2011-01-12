/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import javax.microedition.lcdui.Display;
import javax.microedition.midlet.*;

/**
 * @author Vikram S
 */
public class Background extends MIDlet {
    private final PushRegistryService pushRegistryService;

    public Background() {
        pushRegistryService = new PushRegistryService();
    }

    public void startApp() {
        setToRunInBackground();
        doSomething();
        exit();
    }

    private void doSomething() {
        SMSService.sendSms("9999999999", "Helo Vikram");
    }

    private void setToRunInBackground() {
        final Display display = Display.getDisplay(this);
        display.setCurrent(null);
    }

    private void exit() {
        destroyApp(true);
        notifyDestroyed();
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        pushRegistryService.registerAlarm(false);
    }
}
