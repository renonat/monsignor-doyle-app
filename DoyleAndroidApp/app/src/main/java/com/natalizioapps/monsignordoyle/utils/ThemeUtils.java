package com.natalizioapps.monsignordoyle.utils;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

/**
 * Created by Reno on 15-06-18.
 */
public class ThemeUtils {

    /**
     * Get the theme for the currently selected high school
     *
     * @return {int} : the id for the themed style
     */
    public static int themeForSchool() {
        switch (PrefSingleton.getInstance().getHighSchool()) {
            case MONSIGNORDOYLE:
                return R.style.MonDoyle_Base;
            case STDAVIDS:
                return R.style.Davids_Base;
            case STBENEDICTS:
                return R.style.Benedicts_Base;
            case STMARYS:
                return R.style.Marys_Base;
            case RESURRECTION:
                return R.style.Resurrection_Base;
            case UNDEF:
                return R.style.Welcome_Base;
        }
        return R.style.Welcome_Base;
    }

}
