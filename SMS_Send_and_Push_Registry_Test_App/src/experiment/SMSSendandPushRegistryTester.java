/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package experiment;

import java.util.Date;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.PushRegistry;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.TextField;
import javax.microedition.midlet.*;

/**
 * @author Vikram S
 */
public class SMSSendandPushRegistryTester extends MIDlet {

    private Form form;
    private TextField phoneNumber;
    private TextField message;
    private Command okCmd;
    private Command exitCmd;
    private PushRegistryService pushRegistryService;

    public SMSSendandPushRegistryTester() {
        pushRegistryService = new PushRegistryService();
    }

    public void startApp() {
        final Display display = Display.getDisplay(this);
        form = new Form("Send SMS");
        phoneNumber = new TextField("Phone Number: ", "", 15, TextField.NUMERIC);
        phoneNumber.setString("9999999");
        form.append(phoneNumber);
        message = new TextField("Message: ", "Hello World", 50, TextField.ANY);
        form.append(message);
        okCmd = new Command("Send", Command.OK, 1);
        form.addCommand(okCmd);
        exitCmd = new Command("Exit", Command.EXIT, 1);
        form.addCommand(exitCmd);
        form.setCommandListener(new CommandListener() {

            public void commandAction(Command c, Displayable d) {
                if (c == okCmd) {
                    sendSms();
                } else if (c == exitCmd) {
                    Runnable r = new Runnable() {

                        public void run() {
                            exit();
                        }
                    };
                    new Thread(r).start();
                }
            }
        });
        display.setCurrent(form);
    }

    private void exit() {
        log("Exiting App");
        destroyApp(true);
        notifyDestroyed();
    }

    private void registerForWakeUp() {
        pushRegistryService.registerAlarm(false);
    }

    private void sendSms() {
        final String phNum = phoneNumber.getString();
        final String msg = message.getString();
        log("Sending SMS to " + phNum + " with message " + msg);
        SMSService.sendSms(phNum, msg);
    }

    public void pauseApp() {
    }

    public void destroyApp(boolean unconditional) {
        registerForWakeUp();
    }

    private void log(String logMsg) {
        System.out.println(new Date() + ": " + logMsg);
    }
}
