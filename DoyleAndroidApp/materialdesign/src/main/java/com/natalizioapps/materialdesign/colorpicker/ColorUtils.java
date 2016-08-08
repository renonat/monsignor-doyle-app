package com.natalizioapps.materialdesign.colorpicker;

import android.content.Context;
import android.graphics.Color;

public class ColorUtils {

    /**
     * Create an array of int with colors
     *
     * @param context {Context}
     * @return {int[]} : array of chosen colors
     */
    public static int[] colorChoice(Context context) {
        int[] mColorChoices = null;
        String[] color_array = context.getResources().getStringArray(R.array
                .default_color_choice_values);

        if (color_array != null && color_array.length > 0) {
            mColorChoices = new int[color_array.length];
            for (int i = 0; i < color_array.length; i++) {
                mColorChoices[i] = Color.parseColor(color_array[i]);
            }
        }
        return mColorChoices;
    }

    public static int getColorFromChoice(Context context, int color) {
        String[] color_array = context.getResources().getStringArray(R.array
                .default_color_choice_values);
        if (color_array != null && color_array.length > 0) {
            return Color.parseColor(color_array[color]);
        }
        return parseWhiteColor();
    }

    /**
     * Parse whiteColor
     *
     * @return {int} : color as an int
     */
    public static int parseWhiteColor() {
        return Color.parseColor("#FFFFFF");
    }

}