/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Font;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.layouts.BoxLayout;
import java.io.IOException;

/**
 *
 * @author Vikram S
 */
public class UIUtils {

    public void showDialog(String message) {
        Dialog.show(null, message, null, "Ok");
    }

    public TextField addTextFieldWithLabel(Container topContainer, String labelText) {
        addBoldFontLabel(topContainer, labelText);
        final TextField textField = new TextField();
        Font plainMediumFont = getFont(Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        textField.getStyle().setFont(plainMediumFont);
        topContainer.addComponent(textField);
        return textField;
    }

    public void addBoldFontLabel(Container topContainer, String labelText) {
        Label label = new Label(labelText);
        Font boldMediumSizeFont = getFont(Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        label.getStyle().setFont(boldMediumSizeFont);
        topContainer.addComponent(label);
    }

    public Label getLink(String linkText) {
        Label link = new Label(linkText);
        Font underlinedSmallFont = getFont(Font.STYLE_UNDERLINED, Font.SIZE_SMALL);
        link.getStyle().setFont(underlinedSmallFont);
        return link;
    }

    public Label getImage(String imagePath, int imageWidth) {
        Label imageLabel = null;
        try {
            Image img = Image.createImage(imagePath);
            img = img.scaledWidth(imageWidth);
            imageLabel = new Label(img);
            imageLabel.setAlignment(Component.CENTER);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return imageLabel;
    }

    public boolean isEmpty(String str) {
        return "".equals(str);
    }

    public Container getBoxLayoutColumnStyleContainer() {
        int axis = BoxLayout.X_AXIS;
        return geBoxLayoutContainer(axis);
    }

    public Container geBoxLayoutContainer(int axis) {
        return new Container(new BoxLayout(axis));
    }

    public Font getFont(int style, int size) {
        return Font.createSystemFont(Font.FACE_SYSTEM, style, size);
    }
}
