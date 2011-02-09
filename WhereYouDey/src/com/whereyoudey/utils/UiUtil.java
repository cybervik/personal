/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.utils;

import com.sun.lwuit.Component;
import com.sun.lwuit.Container;
import com.sun.lwuit.Font;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.sun.lwuit.TextField;
import com.sun.lwuit.layouts.BoxLayout;
import com.sun.lwuit.plaf.Border;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;

/**
 *
 * @author Vikram S
 */
public class UiUtil {

    public static Image getImage(String imagePath) {
        Image img = null;
        try {
            img = Image.createImage(imagePath);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return img;
    }

    public static Image getImageFromUrl(String url) {
        Image img = null;
        try {
            HttpConnection conn = (HttpConnection) Connector.open(url);
            conn.setRequestMethod(HttpConnection.GET);
            InputStream in = conn.openInputStream();
            try {
                img = Image.createImage(in);
            } finally {
                in.close();
                conn.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return img;
    }

    public static Label getLabel(Image img) {
        Label imageLabel = new Label(img);
        imageLabel.setAlignment(Component.CENTER);
        return imageLabel;
    }

    public static Label getImageLabelFromUrl(String imageUrl, int width) {
        Image img = getImageFromUrl(imageUrl);
        img = img.scaledWidth(width);
        return getLabel(img);
    }

    private UiUtil() {
    }

    public static Image getImage(String imagePath, int imageWidth) {
        Image img = getImage(imagePath);
        if (img != null) {
            img = img.scaledWidth(imageWidth);
        }
        return img;
    }

    public static TextField addTextFieldWithLabel(final Container container, String labelText) {
        addBoldMediumFontLabel(container, labelText);
        TextField textField = new TextField();
        Font plainMediumFont = getFont(Font.STYLE_PLAIN, Font.SIZE_MEDIUM);
        textField.getStyle().setFont(plainMediumFont);
        textField.getSelectedStyle().setFont(plainMediumFont);
        container.addComponent(textField);
        return textField;
    }

    public static void addBoldMediumFontLabel(Container container, String labelText) {
        Label label = new Label(labelText);
        Font boldMediumSizeFont = getFont(Font.STYLE_BOLD, Font.SIZE_MEDIUM);
        label.getStyle().setFont(boldMediumSizeFont);
        container.addComponent(label);
    }

    public static Label addSmallFontLabel(Container listingContainer, final String text) {
        final Label addrLabel = new Label(text);
        Font smallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL);
        addrLabel.getStyle().setFont(smallFont);
        addrLabel.getStyle().setMargin(1, 1, 1, 1);
        addrLabel.setHeight(smallFont.getHeight());
        listingContainer.addComponent(addrLabel);
        return addrLabel;
    }

    public static void addRating(Container listingContainer, final String ratingStr, String reviewCount) throws NumberFormatException {
        int rating = Integer.parseInt(ratingStr);
        Container ratingContainer = new Container(new BoxLayout(BoxLayout.X_AXIS));
        for (int j = 1; j <= rating; j++) {
            Label ratingIcon = new Label(getImage("/img/rating_mark.png", 8));
            ratingContainer.addComponent(ratingIcon);
        }
        for (int j = rating + 1; j <= 5; j++) {
            Label ratingIcon = new Label(getImage("/img/rating_empty.png", 8));
            ratingContainer.addComponent(ratingIcon);
        }
        if (UiUtil.isEmpty(reviewCount)) {
            reviewCount = "0";
        }
        reviewCount = reviewCount.trim();
        final Label l = addSmallFontLabel(ratingContainer, "(" + reviewCount + ")");
        l.getSelectedStyle().setBgColor(Colors.SELECTEDITEM_BACKGROUND);
        l.getSelectedStyle().setFont(Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_PLAIN, Font.SIZE_SMALL));
        listingContainer.addComponent(ratingContainer);
    }

    public static void addBoldSmallFontLabel(Container listingContainer, final String text) {
        final Label label = new Label(text);
        Font boldSmallFont = Font.createSystemFont(Font.FACE_SYSTEM, Font.STYLE_BOLD, Font.SIZE_SMALL);
        label.getStyle().setFont(boldSmallFont);
        listingContainer.addComponent(label);
    }

    public static Label getLink(String linkText) {
        Label link = new Label(linkText);
        Font underlinedSmallFont = getFont(Font.STYLE_UNDERLINED, Font.SIZE_SMALL);
        link.getStyle().setFont(underlinedSmallFont);
        link.setFocusable(true);
        link.getSelectedStyle().setBorder(Border.createLineBorder(1));
        link.getSelectedStyle().setFont(link.getStyle().getFont());
        return link;
    }

    public static Label getImageLabel(String imagePath, int imageWidth) {
        Image img = getImage(imagePath, imageWidth);
        return getLabel(img);
    }

    public static Label getImageLabel(String imagePath) {
        Image img = getImage(imagePath);
        return getLabel(img);
    }

    public static Label getImageLabelFromUrl(String url) {
        Image img = getImageFromUrl(url);
        return getLabel(img);
    }

    public static boolean isEmpty(String str) {
        return str == null || "".equals(str.trim());
    }

    public static Container getBoxLayoutColumnStyleContainer() {
        int axis = BoxLayout.X_AXIS;
        return geBoxLayoutContainer(axis);
    }

    public static Container geBoxLayoutContainer(int axis) {
        return new Container(new BoxLayout(axis));
    }

    public static Font getFont(int style, int size) {
        return Font.createSystemFont(Font.FACE_SYSTEM, style, size);
    }

    public static String getCommaSepFormat(final String val1, final String val2) {
        return (!isEmpty(val1) && !isEmpty(val2)
                ? val1 + ", " + val2
                : (!isEmpty(val1) ? val1 : val2));
    }

    public static void add(Container itemContainer, final String text) {
        add(itemContainer, text, false);
    }

    public static void add(Container itemContainer, final String text, boolean isBold) {
        if (!isEmpty(text)) {
            if (isBold) {
                addBoldSmallFontLabel(itemContainer, text);
            } else {
                addSmallFontLabel(itemContainer, text);
            }
        }
    }

    static public String urlEncode(String url, boolean limitedEncoding) {
        StringBuffer encodedUrl = new StringBuffer();
        for (int i = 0; i < url.length(); i++) {
            char ch = url.charAt(i);
            switch (ch) {
                case '<':
                    encodedUrl.append("%3C");
                    break;
                case '>':
                    encodedUrl.append("%3E");
                    break;
                case '/':
                    if (!limitedEncoding) {
                        encodedUrl.append("%2F");
                    } else {
                        encodedUrl.append(ch);
                    }
                    break;
                case ' ':
                case '\t':
                    encodedUrl.append("%20");
                    break;
                case ':':
                    if (!limitedEncoding) {
                        encodedUrl.append("%3A");
                    } else {
                        encodedUrl.append(ch);
                    }
                    break;
                case '-':
                    encodedUrl.append("%2D");
                    break;
                case '|':
                    encodedUrl.append("%7C");
                    break;
                case '$':
                    encodedUrl.append("%24");
                    break;
                case '&':
                    if (!limitedEncoding) {
                        encodedUrl.append("%26");
                    } else {
                        encodedUrl.append(ch);
                    }
                    break;
                case '`':
                    encodedUrl.append("%60");
                    break;
                case '[':
                    encodedUrl.append("%5B");
                    break;
                case ']':
                    encodedUrl.append("%5D");
                    break;
                case '{':
                    encodedUrl.append("%7B");
                    break;
                case '}':
                    encodedUrl.append("%7D");
                    break;
                case '"':
                    encodedUrl.append("%22");
                    break;
                case '+':
                    encodedUrl.append("%2B");
                    break;
                case '#':
                    encodedUrl.append("%23");
                    break;
                case '%':
                    encodedUrl.append("%25");
                    break;
                case '@':
                    encodedUrl.append("%40");
                    break;
                case ';':
                    encodedUrl.append("%3B");
                    break;
                case '=':
                    encodedUrl.append("%3D");
                    break;
                case '?':
                    if (!limitedEncoding) {
                        encodedUrl.append("%3F");
                    } else {
                        encodedUrl.append(ch);
                    }
                    break;
                case '\\':
                    encodedUrl.append("%5C");
                    break;
                case '^':
                    encodedUrl.append("%5E");
                    break;
                case '~':
                    encodedUrl.append("%7E");
                    break;
                case '\'':
                    encodedUrl.append("%27");
                    break;
                case ',':
                    encodedUrl.append("%2C");
                    break;
                case '\n':
                    break;
                default:
                    encodedUrl.append(ch);
                    break;
            }
        }
        return encodedUrl.toString();
    }
}
