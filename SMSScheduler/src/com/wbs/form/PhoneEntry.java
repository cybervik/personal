/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

/**
 *
 * @author Vikram S
 */
public class PhoneEntry {

    private final String name;
    private final String telePhone;

    public PhoneEntry(String name, String telePhone) {
        this.name = name;
        this.telePhone = telePhone;
    }

    public String getName() {
        return name;
    }

    public String getTelePhone() {
        return telePhone;
    }

    private boolean isEmpty(String s) {
        return s == null || "".equals(s.trim());
    }

    public String getDisplayableText() {
        return (!isEmpty(name) ? name : "")+"["+telePhone+"]";
    }
}
