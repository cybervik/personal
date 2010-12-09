/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form.helper;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.layouts.Layout;

/**
 *
 * @author Vikram S
 */
public class ResultContainer extends Container {

    public ResultContainer(Layout boxLayout) {
        super(boxLayout);
        setScrollableY(true);
    }

    public void scrollComponentToVisible(Component c) {
        super.scrollComponentToVisible(c);
        repaint();
    }
}
