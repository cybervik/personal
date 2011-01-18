/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Label;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BoxLayout;

/**
 *
 * @author Vikram S
 */
public class DrivingDirectionsSummary extends Container {

    public DrivingDirectionsSummary() {
        super(new BoxLayout(BoxLayout.Y_AXIS));
    }

    void addLine(String lineData) {
        final Label lbl = new Label(lineData);
        lbl.getStyle().setMargin(1, 0, 1, 0);
        lbl.getSelectedStyle().setMargin(1, 0, 1, 0);
        lbl.getStyle().setFont(FontUtil.getMediumNormalFont());
        lbl.getSelectedStyle().setFont(FontUtil.getMediumNormalFont());
        addComponent(lbl);
    }

    void addHorizontalLine() {
        Label line = new Label(" ");
        line.setWidth(Display.getInstance().getDisplayWidth());
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        line.getStyle().setFont(smallFont);
        line.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(0x000000);
                g.fillRect(r.getX(), r.getY() + 10, r.getSize().getWidth(), 1);
            }
        });
        line.getStyle().setMargin(1, 0, 1, 0);
        line.getSelectedStyle().setMargin(1, 0, 1, 0);
        addComponent(line);
    }
}
