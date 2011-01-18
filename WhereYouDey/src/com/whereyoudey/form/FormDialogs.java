/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.whereyoudey.utils.DialogUtil;

/**
 *
 * @author Vikram S
 */
public class FormDialogs {

    public static void showAbout() {
        DialogUtil.showInfo("About", "WhereYouDey Mobile (Version 1.0)\n" 
                + "WhereYouDey (c) 2010 Naija Pages Ltd, Inc.\n"
                + "All rights reserved.\n"
                + "For help contact:\n"
                + "support@whereyoudey.com");
    }

    public static void showHelp() {
        DialogUtil.showInfo("Help", "Please contact: support@whereyoudey.com");
    }

    public static boolean showFeatureUnavialbleMessage() {
        return DialogUtil.showConfirm("Help", "This feature is currently unavailable.\n"
                + "Press ok to visit www.whereyoudey.com for upcomming release or more info.");
    }
}
