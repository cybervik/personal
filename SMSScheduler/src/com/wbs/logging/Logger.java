/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.logging;

import com.wbs.form.CustomDate;

/**
 *
 * @author Vikram S
 */
public class Logger {

    private static final int CURRENT_LOG_LEVEL = 2;
    private static final int LEVEL_ERROR = 0;
    private static final int LEVEL_INFO = 2;

    public static void logInfo(String message) {
        log(LEVEL_INFO, message);
    }

    private static void log(int level, String message) {
        if (CURRENT_LOG_LEVEL >= level) {
            logIntoConsole(message);
        }
    }

    private static void logIntoConsole(String message) {
        System.out.println(new CustomDate().getFullTime() + ":" + message);
    }

    public static void logError(String message) {
        log(LEVEL_ERROR, message);
    }
}
