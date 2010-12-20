/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form.component;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.layouts.Layout;
import com.whereyoudey.utils.Colors;
import com.whereyoudey.form.ResultItem;
import com.whereyoudey.service.helper.Result;
import com.whereyoudey.utils.UiUtil;

/**
 *
 * @author Vikram S
 */
public class Advertisement extends ResultItem {

    private Label advLabel;

    public Advertisement() {
        super(new Result());
        addAdvLabel();
    }

    private void addAdvLabel() {
        advLabel = new Label("Advertisement");
        setDefaultStyle(advLabel);
        addComponent(advLabel);
    }

    protected void setDefaultStyle(Component comp) {
        Font smallFont = UiUtil.getFont(Font.STYLE_PLAIN, Font.SIZE_SMALL);
        comp.getStyle().setFont(smallFont);
        comp.getStyle().setBgColor(Colors.ADVERTISEMENT_BACKGROUND);
        comp.getStyle().setFgColor(Colors.BLACK);
    }

    protected void initDefaultStylePainter() {
        defaultStylePainter = new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(Colors.ADVERTISEMENT_BACKGROUND);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
            }
        };
    }
}
