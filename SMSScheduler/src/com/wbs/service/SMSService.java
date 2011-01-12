/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.service;

import java.io.IOException;
import java.util.Date;
import javax.microedition.io.Connector;
import javax.wireless.messaging.MessageConnection;
import javax.wireless.messaging.TextMessage;

/**
 *
 * @author Vikram S
 */
public class SMSService implements Runnable {

    private final String phNum;
    private final String msg;

    private SMSService(String phNum, String msg) {
        this.phNum = phNum;
        this.msg = msg;
    }

    public static void sendSms(String phNum, String message) {
        final SMSService bgSmsSender = new SMSService(phNum, message);
        Thread t = new Thread(bgSmsSender);
        t.start();
    }

    public void run() {
        try {
            final String addr = "sms://" + phNum;
            final MessageConnection smsConnection = (MessageConnection) Connector.open(addr);
            final TextMessage sms = (TextMessage) smsConnection.newMessage(MessageConnection.TEXT_MESSAGE, addr);
            sms.setPayloadText(msg);
            smsConnection.send(sms);
        } catch (IOException ex) {
            log("Error occurred - " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void log(String logMsg) {
        System.out.println(new Date() + ": " + logMsg);
    }
}
