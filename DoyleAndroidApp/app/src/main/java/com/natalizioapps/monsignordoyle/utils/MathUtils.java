package com.natalizioapps.monsignordoyle.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Reno on 2014-06-30.
 */
public class MathUtils {

    /**
     * Rounds a decimal number to a certain number of places.
     * Checks that the value is not zero.
     *
     * @param value  double : the original decimal value
     * @param places int : the number of decimal places to round to
     * @return double : the rounded decimal number
     */
    public static double round(double value, int places) {
        if (value != 0) {
            if (places < 0) {
                throw new IllegalArgumentException();
            }
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        } else {
            return 0;
        }
    }


}
