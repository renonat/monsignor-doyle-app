package com.natalizioapps.monsignordoyle.utils;

import com.natalizioapps.monsignordoyle.enums.HighSchools;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

/**
 * Created by Reno on 15-06-18.
 */
public class SetupUtils {

    public static void updateSchool() {
        PrefSingleton.getInstance().setHighSchool(HighSchools.RESURRECTION);
    }

}
