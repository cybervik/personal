/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Container;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextArea;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import com.whereyoudey.utils.FontUtil;

/**
 *
 * @author Vikram S
 */
public class WrappingLabel extends Container {

    WrappingLabel(String txt, int maxWidth, final Font font) {
        super(new BoxLayout((BoxLayout.Y_AXIS)));
        initStyle();
        setText(txt, maxWidth, font);
    }

    public WrappingLabel(String txt, Font font) {
        this(txt, Display.getInstance().getDisplayWidth() - 5, font);

    }

    public void setText(String txt, int maxWidth, final Font font) {
        addLine(txt, maxWidth, font);
    }

    WrappingLabel(String txt) {
        this(txt, Display.getInstance().getDisplayWidth() - 5);
    }

    public void setText(String txt) {
        addLine(txt, Display.getInstance().getDisplayWidth() - 5);
    }

    private void initStyle() {
        getStyle().setMargin(0, 0, 0, 0);
        getStyle().setPadding(0, 0, 0, 0);
        getSelectedStyle().setMargin(0, 0, 0, 0);
        getSelectedStyle().setPadding(0, 0, 0, 0);
    }

    WrappingLabel(String txt, int maxWidth) {
        this(txt, maxWidth, FontUtil.getMediumNormalFont());
    }

    private void addLine(String txt, int maxWidth, final Font font) {
        System.out.println(txt);
        int txtLength = txt.length();
        int i = txtLength;
        String temp = txt;
        while (doesNotfitInWidth(temp, maxWidth, font)) {
            temp = reduceStringByOneFromEnd(temp);
        }
        if (notEmpty(temp)) {
            addLabel(temp, font);
            if (isNotSame(temp, txt)) {
                txt = getTheRemainingPart(txt, temp);
                addLine(txt, maxWidth, font);
            }
        }
    }

    private String getTheRemainingPart(String originalText, String renderedText) {
        originalText = originalText.substring(renderedText.length());
        return originalText;
    }

    private boolean isNotSame(String temp, String txt) {
        return !temp.equals(txt);
    }

    private boolean notEmpty(String temp) {
        return isNotSame("", temp);
    }

    private String reduceStringByOneFromEnd(String temp) {
        return temp.substring(0, temp.length() - 1);
    }

    private void addLabel(final String lineTxt, final Font mediumNormalFont) {
        Label lbl = new Label(lineTxt.trim());
        lbl.getStyle().setFont(FontUtil.getMediumNormalFont());
        lbl.getStyle().setBorder(Border.createEmpty());
        lbl.getSelectedStyle().setFont(mediumNormalFont);
        lbl.getSelectedStyle().setBorder(Border.createEmpty());
        initStyle();
        addComponent(lbl);
    }

    private boolean doesNotfitInWidth(String temp, int screenWidth, Font font) {
        return font.stringWidth(temp) > screenWidth;
    }

    private void addLine(String txt, int maxWidth) {
        addLine(txt, maxWidth, FontUtil.getMediumNormalFont());
    }

    public void setText(String txt, Font font) {
        addLine(txt, Display.getInstance().getDisplayWidth() - 5, font);
    }
}
