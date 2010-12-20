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
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.layouts.Layout;
import com.whereyoudey.form.ResultItem;
import com.whereyoudey.service.helper.Result;

/**
 *
 * @author Vikram S
 */
public class SearchResultsContainer extends Container {

    private int resultCount;
    private int selectItemPos;
    private static final int ADVERTISEMENT_INTERVAL = 3;
    private static final int ADVERTISEMENT_POSITION = 4;
    public static final int COLOR_ADV_BACKGROUND = 0xffa500;
    public static final int COLOR_BLACK = 0x000000;
    public static final int COLOR_WHITE = 0xffffff;
    public static final int COLOR_SELECTEDITEM_BACKGROUND = 0x9999ff;

    public SearchResultsContainer() {
        super(new BoxLayout(BoxLayout.Y_AXIS));
        setScrollableY(true);
    }

    public void scrollComponentToVisible(Component c) {
        super.scrollComponentToVisible(c);
        repaint();
    }

    public void addComponent(Component cmp) {
        super.addComponent(cmp);
        if (selectItemPos == -1) {
            selectItemPos = 0;
            selectItem();
        }
        resultCount++;
        if ((resultCount % ADVERTISEMENT_INTERVAL) == 0) {
            addAdvertisment();
        }
    }

    private void addAdvertisment() {
        Advertisement adv = new Advertisement();
        super.addComponent(adv);
    }

    private void setAdvLabelBackground(final Component advLabel) {
        advLabel.getStyle().setBgColor(COLOR_ADV_BACKGROUND);
        advLabel.getStyle().setFgColor(COLOR_BLACK);
    }

    public void reset() {
        this.resultCount = 0;
        this.selectItemPos = -1;
        removeAll();
    }

    public Container selectItem() {
        ResultItem selectedItem = (ResultItem) getComponentAt(selectItemPos);
        selectedItem.select();
//        setSelectedContainerBackground(selectedItem);

        return selectedItem;
    }

    public void deSelectItem() {
        ResultItem selectedItem = (ResultItem) getComponentAt(selectItemPos);
        selectedItem.deSelect();
//        setDeselectedContainerBackground(selectedItem);

    }

    private void setDeselectedContainerBackground(Container selectedItem) {
        selectedItem.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                if (isAdvertisement()) {
                    g.setColor(COLOR_ADV_BACKGROUND);
                } else {
                    g.setColor(COLOR_WHITE);
                }
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
                g.setColor(COLOR_BLACK);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), 1);
            }
        });
    }

    private boolean isAdvertisement() {
        return ((selectItemPos + 1) % ADVERTISEMENT_POSITION == 0);
    }

    private void setDeselectedItemStyle(Component item) {
        if (isAdvertisement()) {
            setAdvLabelBackground(item);
        } else {
            item.getStyle().setBgColor(COLOR_WHITE);
            item.getStyle().setFgColor(COLOR_BLACK);
        }
    }

    private void setSelectedContainerBackground(Container selectedItem) {
        selectedItem.getStyle().setBgPainter(new Painter() {

            public void paint(Graphics g, Rectangle r) {
                g.setColor(COLOR_SELECTEDITEM_BACKGROUND);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), r.getSize().getHeight());
                g.setColor(COLOR_BLACK);
                g.fillRect(r.getX(), r.getY(), r.getSize().getWidth(), 1);
            }
        });
    }

    public void selectItemDown() {
        if (resultCount <= 0) {
            return;
        }
        deSelectItem();
        selectItemPos = (selectItemPos == (this.getComponentCount() - 1)) ? selectItemPos : selectItemPos + 1;
        final Container selectedItem = selectItem();
        final Component lastComponent = selectedItem.getComponentAt(selectedItem.getComponentCount() - 1);
        scrollComponentToVisible(lastComponent);
    }

    public void selectItemUp() {
        if (resultCount <= 0) {
            return;
        }
        deSelectItem();
        selectItemPos = (selectItemPos == 0) ? selectItemPos : selectItemPos - 1;
        final Container selectedItem = selectItem();
        final Component firstComponent = selectedItem.getComponentAt(0);
        scrollComponentToVisible(firstComponent);
    }

    public int getCount() {
        return resultCount;
    }

    public Result getSelectedItemResultRecord() {
        ResultItem selectedItem = (ResultItem) getComponentAt(selectItemPos);
        return selectedItem.getResultRecord();
    }
}
