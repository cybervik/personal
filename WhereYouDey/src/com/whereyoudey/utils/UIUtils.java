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
import com.sun.lwuit.events.FocusListener;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.layouts.CoordinateLayout;
import com.sun.lwuit.layouts.FlowLayout;
import com.sun.lwuit.list.DefaultListCellRenderer;
import com.sun.lwuit.plaf.Border;
import com.whereyoudey.WhereYouDey;
import com.whereyoudey.form.ListForm;
import com.whereyoudey.form.ResultForm;
import com.whereyoudey.form.SearchForm;
import java.io.IOException;

/**
 *
 * @author Vikram S
 */
public class UIUtils {

    public void showDialog(String message) {
        Dialog.show(null, message, null, "Ok");
    }

    public TextField addTextFieldWithLabel(final WhereYouDey midlet, final Container topContainer, String labelText) {
        addBoldFontLabel(topContainer, labelText);
        final TextField textField = new TextField();
        Font plainMediumFont = getFont(Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        textField.getStyle().setFont(plainMediumFont);
        textField.getSelectedStyle().setFont(plainMediumFont);
        final int displayWidth = Display.getInstance().getDisplayWidth();
        textField.setPreferredW(displayWidth);
        textField.setX(0);
        textField.setY(0);
        final int textFieldHeight = textField.getPreferredH();
        Container textFieldContainer = new Container(new CoordinateLayout(displayWidth, 60));
        textFieldContainer.addComponent(textField);
        String[] data = {"india", "Us"};
        final List history = new List(data);
        history.setX(0);
        history.setY(21);
        history.setPreferredH(40);
        history.setVisible(false);
        final DefaultListCellRenderer renderer = (DefaultListCellRenderer) history.getRenderer();
        renderer.setShowNumbers(false);
        renderer.getSelectedStyle().setBgColor(ResultForm.COLOR_SELECTEDITEM_BACKGROUND);
        renderer.getSelectedStyle().setFgColor(ResultForm.COLOR_BLACK);
        Font smallFont = getFont(Font.STYLE_PLAIN, Font.SIZE_SMALL);
        Font smallBoldFont = getFont(Font.STYLE_BOLD, Font.SIZE_SMALL);
        renderer.getStyle().setFont(smallFont);
        renderer.getSelectedStyle().setFont(smallBoldFont);
        renderer.setSmoothScrolling(false);
        textFieldContainer.addComponent(history);
        textField.addFocusListener(new FocusListener() {

            public void focusGained(Component cmpnt) {
                history.setVisible(true);
                history.repaint();
                final SearchForm searchForm = midlet.getSearchForm();
                if (searchForm != null) {
                    searchForm.show();
                }
                history.setFocusable(true);
                cmpnt.setNextFocusDown(history);

            }

            public void focusLost(Component cmpnt) {
                final SearchForm searchForm = midlet.getSearchForm();
                if (searchForm.getFocussed() != history) {
                    history.setVisible(false);
                    history.repaint();
                    if (searchForm != null) {
                        searchForm.show();
                    }
                }
            }
        });
        history.addFocusListener(new FocusListener() {

            public void focusGained(Component cmpnt) {
            }

            public void focusLost(Component cmpnt) {
                final SearchForm searchForm = midlet.getSearchForm();
                history.setVisible(false);
                history.setFocusable(false);
                history.repaint();
                if (searchForm != null) {
                    searchForm.show();
                }
            }
        });
//        final Button historyButton = new Button("abc");
//        historyButton.addActionListener(new ActionListener() {
//            private ListForm listForm;
//
//            public void actionPerformed(ActionEvent ae) {
//                String[] data = {"india", "Us"};
//                if (listForm == null) {
//                    listForm = new ListForm(midlet, data, textField);
//                } else {
//                    listForm.show();
//                }
//            }
//        });
//        textFieldContainer.addComponent(historyButton);
        topContainer.addComponent(textFieldContainer);
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
}
