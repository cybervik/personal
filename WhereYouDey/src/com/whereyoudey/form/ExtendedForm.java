/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.whereyoudey.utils.FontUtil;
import com.sun.lwuit.Button;
import com.sun.lwuit.Command;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.whereyoudey.utils.Colors;
import com.whereyoudey.utils.UiUtil;

/**
 *
 * @author Vikram S
 */
class ExtendedForm extends Form {

    public ExtendedForm(String title) {
        super(title);
        getStyle().setBgColor(Colors.FORM_BACKGROUND);
        getSoftButtonStyle().setFont(FontUtil.getMediumNormalFont());
        for (int i = 0; i < 3; i++) {
            try {
                final Button softButton = getSoftButton(i);
                softButton.getStyle().setFont(FontUtil.getMediumNormalFont());
            } catch (Exception e) {
//                Logger.logError(e.getMessage());
            }
        }
        final DefaultListCellRenderer defaultListCellRenderer = new DefaultListCellRenderer(false);
        defaultListCellRenderer.getStyle().setFont(FontUtil.getMediumNormalFont());
        defaultListCellRenderer.getSelectedStyle().setFont(FontUtil.getMediumBoldFont());
        defaultListCellRenderer.getSelectedStyle().setBgColor(Colors.SELECTEDITEM_BACKGROUND);
        setMenuCellRenderer(defaultListCellRenderer);
    }

    public ExtendedForm() {
        this("");
    }

    void addMenuItem(String menuName) {
        addCommand(new Command(menuName));
    }
}
