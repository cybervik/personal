/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form.decorator;

import com.sun.lwuit.Font;

/**
 *
 * @author Vikram S
 */
public class FontUtil {

    public static Font getSmallBoldFont() {
        Font smallBoldFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        return smallBoldFont;
    }

    public static Font getMediumBoldFont() {
        Font mediumFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        return mediumFont;
    }

    public static Font getSmallNormalFont() {
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        return smallFont;
    }

    public static Font getSmallItalicFont() {
        return Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_ITALIC, Font.SIZE_SMALL);
    }

    public static Font getMediumNormalFont() {
        return Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
    }

    public static Font getLargeBoldFont() {
        return Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_LARGE);
    }
}
