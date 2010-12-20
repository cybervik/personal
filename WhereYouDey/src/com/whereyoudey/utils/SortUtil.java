/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.whereyoudey.utils;

import com.whereyoudey.service.helper.Result;

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

    public static void sort(String sortProperty, Result[] results, String sortOrder) {
        for (int k = 0; k < results.length - 1; k++) {
            if (results[k] == null) {
                break;
            }
            boolean isSorted = true;

            for (int i = 1; i < results.length - k; i++) {
                if (results[i] == null) {
                    break;
                }
                String a = results[i].getProperty(sortProperty);
                String b = results[i - 1].getProperty(sortProperty);
                if (shouldSwap(sortOrder, a, b)) {
                    Result tempVariable = results[i];
                    results[i] = results[i - 1];
                    results[i - 1] = tempVariable;

                    isSorted = false;

                }
            }

            if (isSorted) {
                break;
            }
        }
        for (int i = 0; i < results.length; i++) {
            if (results[i] == null) {
                break;
            }
            System.out.println(results[i].getProperty(sortProperty));
        }

    }
}
