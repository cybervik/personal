/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wbs.utils;

import com.wbs.form.PhoneEntry;
import java.util.Vector;

/**
 *
 * @author Vikram S
 */
public class SortUtil {

    public static final String SORT_ORDER_ASCENDING = "ASC";
    public static final String SORT_ORDER_DESCENDING = "DSC";

    private static boolean shouldSwap(String sortOrder, String a, String b) {
        return (sortOrder.equals(SORT_ORDER_ASCENDING) && a.compareTo(b) < 0) || (sortOrder.equals(SORT_ORDER_DESCENDING) && a.compareTo(b) > 0);
    }

    public static void sort(Vector data, String sortOrder) {
        for (int k = 0; k < data.size() - 1; k++) {
            if (data.elementAt(k) == null) {
                break;
            }
            boolean isSorted = true;
            for (int i = 1; i < data.size() - k; i++) {
                final PhoneEntry pe1 = (PhoneEntry) data.elementAt(i);
                final PhoneEntry pe2 = (PhoneEntry) data.elementAt(i - 1);
                String a = pe1.getName();
                String b = pe2.getName();
                if (shouldSwap(sortOrder, a, b)) {
                    data.setElementAt(pe1, i - 1);
                    data.setElementAt(pe2, i);
                    isSorted = false;
                }
            }
            if (isSorted) {
                break;
            }
        }
        for (int i = 0; i < data.size(); i++) {
            final Object obj = data.elementAt(i);
            if (obj == null) {
                break;
            }
            PhoneEntry pe = (PhoneEntry) obj;
            System.out.println(pe.getDisplayableText());
        }

    }
}
