/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.utils;

import com.sun.lwuit.Command;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.plaf.Border;
import com.wbs.SMSScheduler;
import com.wbs.form.decorator.FontUtil;
import com.wbs.form.decorator.FormCommonDecorator;

/**
 *
 * @author Vikram S
 */
public class DialogUtil {

    public static void showInfo(final String title, final String message) {
        Dialog d = getBasicDialog(title, message);
        d.addCommand(new Command("Ok"));
        d = (Dialog) FormCommonDecorator.decorate(d);
        d.show();
    }

    private static Dialog getBasicDialog(final String title, final String message) {
        Dialog d = new Dialog();
        final Label titelLabel = new Label(title);
        titelLabel.getStyle().setFont(FontUtil.getMediumBoldFont());
        d.setTitleComponent(titelLabel);
        TextArea bodyArea = new TextArea(message);
        bodyArea.getStyle().setFont(FontUtil.getMediumNormalFont());
        bodyArea.getStyle().setBorder(Border.createEmpty());
        bodyArea.getSelectedStyle().setFont(FontUtil.getMediumNormalFont());
        bodyArea.getSelectedStyle().setBorder(Border.createEmpty());
        bodyArea.setGrowByContent(true);
        bodyArea.setEditable(false);
        bodyArea.setRows(3);
        d.addComponent(bodyArea);
        d.setAutoDispose(true);
        d.setDialogType(Dialog.TYPE_INFO);
        d.setScrollable(true);
        return d;
    }

    public static boolean showConfirm(final String title, final String message) {
        Dialog d = getBasicDialog(title, message);
        d.setDialogType(Dialog.TYPE_CONFIRMATION);
        final Command okCmd = new Command("Ok");
        final Command cancelCmd = new Command("Cancel");
        d.addCommand(cancelCmd);
        d.addCommand(okCmd);
        d = (Dialog) FormCommonDecorator.decorate(d);
        final Command cmd = d.showPacked(BorderLayout.SOUTH, true);
        return cmd == okCmd ? true : false;
    }
}
