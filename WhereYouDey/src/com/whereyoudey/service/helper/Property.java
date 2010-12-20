/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.service.helper;

/**
 *
 * @author Vikram S
 */
public class Property {

    private final String tagName;
    private final String text;

    Property(String tagName, String text) {
        this.tagName = tagName;
        this.text = text;
    }

    public String getTagName() {
        return tagName;
    }

    public String getText() {
        return text;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Property other = (Property) obj;
        if ((this.tagName == null) ? (other.tagName != null) : !this.tagName.equals(other.tagName)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (this.tagName != null ? this.tagName.hashCode() : 0);
        return hash;
    }
    
}
