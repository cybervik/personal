/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.utils;

import com.sun.lwuit.Button;
import com.sun.lwuit.ComboBox;
import com.sun.lwuit.Command;
import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Dialog;
import com.sun.lwuit.Display;
import com.sun.lwuit.Font;
import com.sun.lwuit.Form;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.List;
import com.sun.lwuit.TextField;
import com.sun.lwuit.events.ActionEvent;
import com.sun.lwuit.events.ActionListener;
import com.sun.lwuit.events.DataChangedListener;
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.events.SelectionListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.CoordinateLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.plaf.Border;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.form.ListForm;
import com.whereyoudey.form.ResultForm;
import com.whereyoudey.form.BusinessSearchForm;
import java.io.IOException;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class UIUtils {

    public void showDialog(String message) {
        Dialog.show(null, message, null, "Ok");
    }

    public TextFieldWithHistory addTextFieldWithLabelAndHistory(final Container container, String labelText) {
        addBoldFontLabel(container, labelText);
        TextFieldWithHistory textFieldWithHistory = new TextFieldWithHistory(container);
        return textFieldWithHistory;
    }

    public TextField addTextFieldWithLabel(final Container container, String labelText) {
        addBoldFontLabel(container, labelText);
        TextField textField = new TextField();
        Font plainMediumFont = getFont(Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        textField.getStyle().setFont(plainMediumFont);
        textField.getSelectedStyle().setFont(plainMediumFont);
        container.addComponent(textField);
        return textField;
    }

    public void addBoldFontLabel(Container container, String labelText) {
        Label label = new Label(labelText);
        Font boldMediumSizeFont = getFont(Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        label.getStyle().setFont(boldMediumSizeFont);
        container.addComponent(label);
    }

    public Label getLink(String linkText) {
        Label link = new Label(linkText);
        Font underlinedSmallFont = getFont(Font.STYLE_UNDERLINED, Font.SIZE_SMALL);
        link.getStyle().setFont(underlinedSmallFont);
        link.setFocusable(true);
        link.getSelectedStyle().setBorder(Border.createLineBorder(1));
        link.getSelectedStyle().setFont(link.getStyle().getFont());
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

    public void showAbout() {
        showDialog("WhereYouDey Mobile (Version 1.0)\n" + "WhereYouDey (c) 2010 Naija Pages Ltd, Inc. " + "All rights reserved. For help contact: " + "support@whereyoudey.com");
    }

    public void showHelp() {
        showDialog("Please conatct: support@whereyoudey.com");
    }

    public String getCommaSepFormat(final String val1, final String val2) {
        return (!isEmpty(val1) && !isEmpty(val2)
                ? val1 + ", " + val2
                : (!isEmpty(val1) ? val1 : val2));
    }
}
