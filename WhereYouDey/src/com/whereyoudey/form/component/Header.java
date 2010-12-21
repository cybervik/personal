/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.whereyoudey.form.component;

import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Label;
import com.sun.lwuit.layouts.BorderLayout;
import com.sun.lwuit.layouts.BoxLayout;
import com.whereyoudey.utils.UiUtil;

/**
 *
 * @author Vikram S
 */
public class Header {
    private final Label title;
    private final Label resultCounter;

    public Header(Form form) {
        final Container headerContainer = new Container(new BoxLayout(BoxLayout.Y_AXIS));
        title = new Label();
        title.setIcon(UiUtil.getImage("/img/small_logo.png", 20));
        title.getStyle().setBgColor(0x000000);
        title.getStyle().setFgColor(0xffffff);
        headerContainer.addComponent(title);
        resultCounter = new Label();
        Font mediumFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        resultCounter.getStyle().setFont(mediumFont);
        resultCounter.getStyle().setBgColor(0xf3f3f3);
        headerContainer.addComponent(resultCounter);
        form.addComponent(BorderLayout.NORTH, headerContainer);
    }

    void setText(String text) {
        title.setText(text);
    }

    public void setResultCount(int resultCount) {
        if (resultCount > 0) {
            resultCounter.setText("Result 1 - " + resultCount);
        } else {
            resultCounter.setText("No results found");
        }
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

}
