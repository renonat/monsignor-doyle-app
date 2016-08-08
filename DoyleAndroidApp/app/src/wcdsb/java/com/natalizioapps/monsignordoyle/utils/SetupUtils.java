package com.natalizioapps.monsignordoyle.utils;

import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

/**
 * Created by Reno on 15-06-18.
 */
public class SetupUtils {


    public static void updateSchool() {
        // WCDSB App allows the user to change schools in the settings
        PrefSingleton.getInstance().setIsWCDSBapp(true);
    }

}
