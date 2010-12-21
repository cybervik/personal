/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.service.helper;

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

    public void setProperty(String name, String value) {
        Property p = new Property(name, value);
        properties.addElement(p);
    }

    public String getProperty(String name) {
        final int pos = properties.indexOf(new Property(name));
        if (pos >= 0) {
            try {
                final Property prop = (Property) properties.elementAt(pos);
                return prop.getValue();
            } catch (Exception e) {
                System.out.println("There was an error in Property class");
                e.printStackTrace();
            }
        }
        return "";
    }
}
