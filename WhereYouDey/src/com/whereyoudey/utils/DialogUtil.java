/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.utils;

import com.sun.lwuit.Dialog;

/**
 *
 * @author Vikram S
 */
public class DialogUtil {

    public static void showInfo(final String title, final String message) {
        ExtendedDialog d = new ExtendedDialog(title, message);
        d.showExtendedDialog();
    }


    public static boolean showConfirm(final String title, final String message) {
        ExtendedDialog d = new ExtendedDialog(title, message, Dialog.TYPE_CONFIRMATION);
        return d.showExtendedDialog();
    }

    private DialogUtil() {
    }
}
