/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form.decorator;

import com.wbs.constant.Color;
import com.sun.lwuit.Button;
import com.sun.lwuit.Form;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.wbs.logging.Logger;

/**
 *
 * @author Vikram S
 */
public class FormCommonDecorator {

    public static Form decorate(Form form) {
        form.getSoftButtonStyle().setFont(FontUtil.getMediumNormalFont());
        for (int i = 0; i < 3; i++) {
            try {
                final Button softButton = form.getSoftButton(i);
                softButton.getStyle().setFont(FontUtil.getMediumNormalFont());
            } catch (Exception e) {
                Logger.logError(e.getMessage());
            }
        }
        final DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer(false);
        defaultListCellRenderer.getStyle().setFont(FontUtil.getMediumNormalFont());
        defaultListCellRenderer.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
        defaultListCellRenderer.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        form.setMenuCellRenderer(defaultListCellRenderer);
        return form;
    }
}
