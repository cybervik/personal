/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.service;

import com.wbs.BackgroundProcessor;
import com.wbs.logging.Logger;
import java.util.Date;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.PushRegistry;

/**
 *
 * @author Vikram S
 */
public class SchedulerService {

    public static final int SCHEDULE_INTERVAL_IN_SECONDS = 60 * 5;
    public static final int SCHEDULE_INTERVAL_IN_MILLIS = SCHEDULE_INTERVAL_IN_SECONDS * 1000;

    public static void removeScheduler() {
        schedule(0);
    }

    private static void schedule(final long nextRunTime) {
        try {
            final String schedulerClass = BackgroundProcessor.class.getName();
            Logger.logInfo("Background Scheduler Class - " + schedulerClass);
            long prevTime = PushRegistry.registerAlarm(schedulerClass, nextRunTime);
            Logger.logInfo("Push registery - Will wake up @ " + new Date(nextRunTime) + ", previously suppose to run @ " + new Date(prevTime));
        } catch (ClassNotFoundException ex) {
            Logger.logError(ex.getMessage());
        } catch (ConnectionNotFoundException ex) {
            Logger.logError(ex.getMessage());
        } catch (Exception ex) {
            Logger.logError(ex.getMessage());
        }
    }

    private SchedulerService() {
    }

    public static void scheduleForDefaultInterval() {
        Date currentDate = new Date();
        final long nextRunTime = currentDate.getTime() + SCHEDULE_INTERVAL_IN_MILLIS;
        schedule(nextRunTime);
    }
}
