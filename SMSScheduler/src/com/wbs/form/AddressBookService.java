/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.form;

import com.wbs.logging.Logger;
import com.wbs.utils.SortUtil;
import java.util.Enumeration;
import java.util.Vector;
import javax.microedition.pim.Contact;
import javax.microedition.pim.PIM;
import javax.microedition.pim.PIMException;
import javax.microedition.pim.PIMList;

/**
 *
 * @author Vikram S
 */
public class AddressBookService {

    private static final int NAME_NOT_SUPPORTED = -999;

    static public Vector retrieveContactsFromAddressBook() {
        Vector phoneEntries = new Vector();
        boolean mobileSupported = false;
        boolean homePhoneSupported = false;
        boolean workPhoneSupported = false;
        boolean telSupported = false;
        String name = "";
        String phone = "";

        Logger.logInfo("Getting PIM instance");
        final PIM pim = PIM.getInstance();

        Logger.logInfo("Getting PIM lists");
        final String[] listPIMLists = pim.listPIMLists(PIM.CONTACT_LIST);
        int nameField = NAME_NOT_SUPPORTED;

        for (int i = 0; i < listPIMLists.length; i++) {
            try {
                Logger.logInfo("Openning PIM list - " + listPIMLists[i]);
                final PIMList contactList = pim.openPIMList(PIM.CONTACT_LIST, PIM.READ_ONLY, listPIMLists[i]);
                nameField = getNameField(contactList);
                mobileSupported = isFieldSupported(contactList, Contact.ATTR_MOBILE);
                homePhoneSupported = isFieldSupported(contactList, Contact.ATTR_HOME);
                workPhoneSupported = isFieldSupported(contactList, Contact.ATTR_WORK);
                telSupported = isFieldSupported(contactList, Contact.TEL);
                final Enumeration items = contactList.items();
                while (items.hasMoreElements()) {
                    name = "";
                    phone = "";
                    final Contact c = (Contact) items.nextElement();
                    if (nameField != NAME_NOT_SUPPORTED) {
                        name = getContactField(c, nameField);
                    }
                    if (mobileSupported) {
                        phone = getContactField(c, Contact.ATTR_MOBILE);
                        addPhoneEntry(name, phone, phoneEntries);
                    }
                    if (homePhoneSupported) {
                        phone = getContactField(c, Contact.ATTR_HOME);
                        addPhoneEntry(name, phone, phoneEntries);
                    }
                    if (workPhoneSupported) {
                        phone = getContactField(c, Contact.ATTR_WORK);
                        addPhoneEntry(name, phone, phoneEntries);
                    }
                    if (telSupported) {
                        phone = getContactField(c, Contact.TEL);
                        addPhoneEntry(name, phone, phoneEntries);
                    }
                }
            } catch (PIMException ex) {
                ex.printStackTrace();
            }
        }
        SortUtil.sort(phoneEntries, SortUtil.SORT_ORDER_ASCENDING);
        return phoneEntries;
    }

    private static int getNameField(final PIMList contactList) {
        int abc;
        if (isFieldSupported(contactList, Contact.FORMATTED_NAME)) {
            abc = Contact.FORMATTED_NAME;
        } else if (isFieldSupported(contactList, Contact.NAME)) {
            abc = Contact.NAME;
        } else if (isFieldSupported(contactList, Contact.NICKNAME)) {
            abc = Contact.FORMATTED_NAME;
        } else {
            abc = NAME_NOT_SUPPORTED;
        }
        return abc;
    }

    private static boolean isFieldSupported(final PIMList contactList, final int field) {
        if (contactList.isSupportedField(field)) {
            return true;
        } else {
            return false;
        }
    }

    private static void addPhoneEntry(String name, String telePhone, Vector phoneEntries) {
        Logger.logInfo("Adding phone entry - " + name + " - " + telePhone);
        PhoneEntry phoneEntry = new PhoneEntry(name, telePhone);
        phoneEntries.addElement(phoneEntry);
    }

    private static String getContactField(final Contact c, final int field) {
        String v = "";
        try {
            v = c.getString(field, 0);
        } catch (Exception e) {
        }
        return v;
    }
}
