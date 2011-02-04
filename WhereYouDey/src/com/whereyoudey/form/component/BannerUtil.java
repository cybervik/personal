/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.form.component;

import com.sun.lwuit.Display;
import com.sun.lwuit.Image;
import com.sun.lwuit.Label;
import com.whereyoudey.utils.UiUtil;
import java.util.Hashtable;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class BannerUtil {

    private static Hashtable banners = new Hashtable();

    static Label getBanner(String bannerId) {
        Image img = null;
        final Object storedImage = banners.get(bannerId);
        if (storedImage == null) {
            img = retrieveBannerFromServer(bannerId, img);
        } else {
            System.out.println("Banner cached");
            img = (Image) storedImage;
        }
        Label advLabel = UiUtil.getLabel(img);
        advLabel.setAlignment(Label.LEFT);
        return advLabel;
    }

    private static Image retrieveBannerFromServer(String bannerId, Image img) {
        final String imageUrl = "http://www.whereyoudey.com/mainpagebanners/" + bannerId;
        System.out.println("Banner image url" + imageUrl);
        img = UiUtil.getImageFromUrl(imageUrl);
        img = img.scaledWidth(Display.getInstance().getDisplayWidth());
        banners.put(bannerId, img);
        return img;
    }
}
