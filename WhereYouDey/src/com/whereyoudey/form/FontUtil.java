/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.whereyoudey.form;

import com.sun.lwuit.Font;

/**
 *
 * @author Vikram S
 */
public class FontUtil {

    public static Font getMediumNormalFont() {
        return getFont(Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    }

    private static Font getFont(int style, int size) {
        return Font.createSystemFont(Font.FACE_SYSTEM, style, size);
    }

    public static Font getMediumBoldFont() {
        return getFont(Font.STYLE_BOLD, Font.SIZE_MEDIUM);
    }

    public static Font getSmallBoldFont() {
        return getFont(Font.STYLE_BOLD, Font.SIZE_SMALL);
    }

    public static Font getSmallNormalFont() {
        return getFont(Font.STYLE_PLAIN, Font.SIZE_SMALL);
    }
}
