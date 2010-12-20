/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.service.helper;

import com.whereyoudey.service.helper.Property;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class Result {
    public static final int INITIAL_SIZE = 20;

    private Vector properties;

    public Vector getProperties() {
        return properties;
    }

    public Result() {
        properties = new Vector(INITIAL_SIZE);
    }

    public void setProperty(String tagName, String text) {
        Property prop = new Property(tagName, text);
        properties.addElement(prop);
    }

    public String getProperty(String string) {
        final int pos = properties.indexOf(new Property(string, ""));
        if (pos >= 0) {
            final Property prop = (Property) properties.elementAt(pos);
            return prop.getText();
        }
        return "";
    }
}
