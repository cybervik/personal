/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Font;
import com.sun.lwuit.List;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.wbs.constant.Color;

/**
 *
 * @author Vikram S
 */
class ComboBox1 extends ComboBox {

    private final int size;

    ComboBox1(int size) {
        super();
        this.size = size;
    }

    protected List createPopupList() {
        List l = new List();
        for (int i = 0; i < size; i++) {
            l.addItem(String.valueOf(i));
        }
        DefaultListCellRenderer renderer = (DefaultListCellRenderer) l.getRenderer();
        renderer.getStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        renderer.getStyle().setBgColor(Color.WHITE);
        final Font smallBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        renderer.getSelectedStyle().setFont(smallBoldFont);
        renderer.getSelectedStyle().setBgColor(Color.EVENTLIST_SELECTEDITEM_BACKGROUND);
        renderer.setPreferredW(smallBoldFont.stringWidth("1000"));
        repaint();
        calcPreferredSize();
        return l;
    }
}
