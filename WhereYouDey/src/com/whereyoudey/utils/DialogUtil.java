/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.utils;

import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import com.whereyoudey.constants.AppConstants;

/**
 *
 * @author Vikram S
 */
public class DialogUtil {

    private static Dialog waitDialog;

    public static void showInfo(final String title, final String message) {
        ExtendedDialog d = new ExtendedDialog(title, message);
        d.showExtendedDialog();
    }

    public static boolean showConfirm(final String title, final String message) {
        ExtendedDialog d = new ExtendedDialog(title, message, Dialog.TYPE_CONFIRMATION);
        return d.showExtendedDialog();
    }

    public static void showWait() {
        if (waitDialog == null) {
            createWaitDialog();
        }
        try {
            waitDialog.showPacked(BorderLayout.CENTER, true);
        } catch (Exception e) {
        }
    }

    private static void createWaitDialog() {
        waitDialog = new Dialog();
        waitDialog.setLayout(new BorderLayout());
        waitDialog.getStyle().setBgColor(Colors.FORM_BACKGROUND);
        Label waitLabel = UiUtil.getImageLabel("/img/wait.png", AppConstants.ICON_WIDTH);
        waitLabel.setText("Searching");
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        waitLabel.getStyle().setFont(smallFont);
        waitLabel.getStyle().setMargin(0, 0, 0, 0);
        waitLabel.getStyle().setPadding(0, 0, 0, 0);
        waitLabel.getStyle().setBgColor(Colors.FORM_BACKGROUND);
        waitDialog.addComponent(BorderLayout.CENTER, waitLabel);
    }

    public static void hideWait() {
        if (waitDialog != null) {
            waitDialog.dispose();
        }
    }

    private DialogUtil() {
    }
}
