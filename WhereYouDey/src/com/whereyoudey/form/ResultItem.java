/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.whereyoudey.utils.Colors;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Graphics;
import com.sun.lwuit.Painter;
import com.sun.lwuit.geom.Rectangle;
import com.sun.lwuit.layouts.BoxLayout;
import com.whereyoudey.service.helper.Result;

/**
 *
 * @author Vikram S
 */
public class ResultItem extends Container {

    protected Painter defaultStylePainter;
    private Painter selectedStylePainter;
    private final Result result;

    public ResultItem(Result result) {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        this.result = result;
        initPainters();
        getStyle().setBgPainter(defaultStylePainter);
    }

    private void initPainters() {
        initDefaultStylePainter();
        initSelectedStylePainter();
    }

    protected void initDefaultStylePainter() {
        defaultStylePainter = new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(Colors.WHITE);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
                g.setColor(Colors.BLACK);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), 1);
            }
        };
    }

    public void select() {
        getStyle().setBgPainter(selectedStylePainter);
        select(this);
    }

    private void select(Container c) {
        final int componentCount = c.getComponentCount();
        for (int i = 0; i < componentCount; i++) {
            Component item = c.getComponentAt(i);
            if (item instanceof Container) {
                select((Container) item);
            }
            setSelectedStyle(item);
        }
    }

    private void setSelectedStyle(Component item) {
        item.getStyle().setBgColor(Colors.SELECTEDITEM_BACKGROUND);
        item.getStyle().setFgColor(Colors.BLACK);
    }

    public void deSelect() {
        getStyle().setBgPainter(defaultStylePainter);
        deSelect(this);
    }

    private void deSelect(Container c) {
        for (int i = 0; i < c.getComponentCount(); i++) {
            Component item = c.getComponentAt(i);
            if (item instanceof Container) {
                deSelect((Container) item);
            }
            setDefaultStyle(item);
        }
    }

    protected void setDefaultStyle(Component item) {
        item.getStyle().setBgColor(Colors.WHITE);
        item.getStyle().setFgColor(Colors.BLACK);
    }

    private void initSelectedStylePainter() {
        selectedStylePainter = new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(Colors.SELECTEDITEM_BACKGROUND);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
                g.setColor(Colors.BLACK);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), 1);
            }
        };
    }

    public Result getResultRecord() {
        return this.result;
    }
}
