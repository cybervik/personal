/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.utils;

import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.plaf.Border;
import com.whereyoudey.form.FontUtil;

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
