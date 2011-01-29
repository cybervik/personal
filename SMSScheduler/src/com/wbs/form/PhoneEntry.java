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

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PhoneEntry other = (PhoneEntry) obj;
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.telePhone == null) ? (other.telePhone != null) : !this.telePhone.equals(other.telePhone)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 79 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 79 * hash + (this.telePhone != null ? this.telePhone.hashCode() : 0);
        return hash;
    }

}
