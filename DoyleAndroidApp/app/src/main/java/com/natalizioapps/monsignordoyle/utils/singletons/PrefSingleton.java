package com.natalizioapps.monsignordoyle.utils.singletons;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;

import com.natalizioapps.materialdesign.colorpicker.ColorUtils;
import com.natalizioapps.monsignordoyle.enums.HighSchools;
import com.natalizioapps.monsignordoyle.objects.SchoolClass;
import com.natalizioapps.monsignordoyle.utils.ObjectSerializer;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Handles all shared-preference related methods.
 * <br>This is a Singleton, so the first call initializes the class, and
 * all others access the same instance
 * <br>Includes pre-made methods for accessing and changing all shared-preferences
 */
public class PrefSingleton {
    private static PrefSingleton mInstance;
    private Context mContext;

    private SharedPreferences mMyPreferences;

    private final String ID_PERIOD1 = "PERIOD1",
            ID_PERIOD2 = "PERIOD2",
            ID_PERIOD3 = "PERIOD3",
            ID_PERIOD4 = "PERIOD4",
            ID_SHOW_ROOMS = "SHOWROOMS",
            ID_SHOW_LOCKERS = "SHOWLOCKERS",
            ID_SHOW_WASHROOMS = "SHOWWASHROOMS",
            ID_SHOW_CLASSES = "SHOWCLASSES",
            ID_SHOW_WELCOME = "SHOWWELCOME",
            ID_HIGHSCHOOL = "HIGHSCHOOL",
            ID_LUNCH = "LUNCH",
            ID_WCDSBAPP = "WCDSBAPP";

    /**
     * A private constructor for the class
     */
    private PrefSingleton() {
    }

    /**
     * Gets the current instance of the class, and creates it if it does not exist
     *
     * @return {@link PrefSingleton} : the instance
     * of PrefSingleton
     */
    public static PrefSingleton getInstance() {
        if (mInstance == null) {
            mInstance = new PrefSingleton();
        }
        return mInstance;
    }

    /**
     * Initializes the variables from string resources, sets the global context,
     * and preferencemanager
     *
     * @param ctxt {@link android.content.Context} : The context of the parent activity
     */
    public void Initialize(Context ctxt) {
        mContext = ctxt;
        mMyPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);

        // The default information used for a class if there is none currently saved
        if (getClass(1) == null) {
            SchoolClass period1 = new SchoolClass.Builder()
                    .color(ColorUtils.getColorFromChoice(ctxt, 0))
                    .name("")
                    .period(1)
                    .room("")
                    .teacher("")
                    .build();
            saveClass(period1);
        }

        if (getClass(2) == null) {
            SchoolClass period2 = new SchoolClass.Builder()
                    .color(ColorUtils.getColorFromChoice(ctxt, 5))
                    .name("")
                    .period(2)
                    .room("")
                    .teacher("")
                    .build();
            saveClass(period2);
        }

        if (getClass(3) == null) {
            SchoolClass period3 = new SchoolClass.Builder()
                    .color(ColorUtils.getColorFromChoice(ctxt, 8))
                    .name("")
                    .period(3)
                    .room("")
                    .teacher("")
                    .build();
            saveClass(period3);
        }

        if (getClass(4) == null) {
            SchoolClass period4 = new SchoolClass.Builder()
                    .color(ColorUtils.getColorFromChoice(ctxt, 12))
                    .name("")
                    .period(4)
                    .room("")
                    .teacher("")
                    .build();
            saveClass(period4);
        }

        if (!mMyPreferences.contains(ID_SHOW_ROOMS)) {
            setShowRooms(false);
        }

        if (!mMyPreferences.contains(ID_SHOW_LOCKERS)) {
            setShowLockers(false);
        }

        if (!mMyPreferences.contains(ID_SHOW_WASHROOMS)) {
            setShowWashrooms(true);
        }

        if (!mMyPreferences.contains(ID_SHOW_CLASSES)) {
            setShowClasses(true);
        }

        if (!mMyPreferences.contains(ID_SHOW_WELCOME)) {
            setShowWelcome(true);
        }
    }

    /**
     * Takes a single {@link com.natalizioapps.monsignordoyle.objects.SchoolClass} and turns it into
     * a {@link java.util.Set} for saving into shared preferences.
     * The period is read automatically
     *
     * @param c {SchoolClass} : the class
     */
    public void saveClass(@NonNull SchoolClass c) {
        SharedPreferences.Editor mEdit = mMyPreferences.edit();
        ArrayList<String> info = new ArrayList<String>();
        info.add(c.getName());
        info.add(c.getTeacher());
        info.add(c.getRoom());
        info.add(String.valueOf(c.getColor()));

        String key = keyForPeriod(c.getPeriod());
        try {
            mEdit.putString(key, ObjectSerializer.serialize(info));
        } catch (IOException e) {
            e.printStackTrace();
        }
        mEdit.apply();
    }

    /**
     * Fetches the {@link com.natalizioapps.monsignordoyle.objects.SchoolClass} for a specific
     * period
     * from shared preferences.
     *
     * @param period {int} : the period of the class
     * @return {SchoolClass} : the object containing the class details
     */
    public SchoolClass getClass(@IntRange(from = 1, to = 4) int period) {
        if (mMyPreferences != null) {
            String key = keyForPeriod(period);
            ArrayList<String> info = null;
            try {
                info = (ArrayList<String>) ObjectSerializer.deserialize(mMyPreferences.getString
                        (key, null));

            } catch (IOException e) {
                e.printStackTrace();
            }

            if (info == null) {
                return null;
            }

            return new SchoolClass.Builder()
                    .name(info.get(0))
                    .teacher(info.get(1))
                    .room(info.get(2))
                    .color(Integer.valueOf(info.get(3)))
                    .period(period).build();
        }
        return null;
    }

    /**
     * Returns the key used for each period in shared preferences
     *
     * @param period {int} : the period
     * @return {String} : the key
     */
    private String keyForPeriod(@IntRange(from = 1, to = 4) int period) {
        switch (period) {
            case 1:
                return ID_PERIOD1;
            case 2:
                return ID_PERIOD2;
            case 3:
                return ID_PERIOD3;
            case 4:
                return ID_PERIOD4;
            default:
                return "";
        }
    }

    private void saveBoolean(String key, boolean value) {
        SharedPreferences.Editor mEdit = mMyPreferences.edit();
        mEdit.putBoolean(key, value);
        mEdit.apply();
    }

    public boolean getShowRooms() {
        return mMyPreferences != null && mMyPreferences.getBoolean(ID_SHOW_ROOMS, false);
    }

    public boolean getShowLockers() {
        return mMyPreferences != null && mMyPreferences.getBoolean(ID_SHOW_LOCKERS, false);
    }

    public boolean getShowWashrooms() {
        return mMyPreferences != null && mMyPreferences.getBoolean(ID_SHOW_WASHROOMS, true);
    }

    public boolean getShowClasses() {
        return mMyPreferences != null && mMyPreferences.getBoolean(ID_SHOW_CLASSES, true);
    }

    public boolean getShowWelcome() {
        return mMyPreferences != null && mMyPreferences.getBoolean(ID_SHOW_WELCOME, true);
    }

    @NonNull
    public HighSchools getHighSchool() {
        if (mMyPreferences != null) {
            // Get the value from the HighSchools Enum
            int value = mMyPreferences.getInt(ID_HIGHSCHOOL, 0);
            return HighSchools.values()[value];
        } else
            return HighSchools.UNDEF;
    }

    public int getLunch() {
        if (mMyPreferences != null) {
            // Get the value from the HighSchools Enum
            return mMyPreferences.getInt(ID_LUNCH, 1);
        } else
            return 1;
    }

    public boolean isWCDSBapp() {
        // Get the value from the HighSchools Enum
        return mMyPreferences != null && mMyPreferences.getBoolean(ID_WCDSBAPP, false);
    }

    public void setShowRooms(boolean value) {
        saveBoolean(ID_SHOW_ROOMS, value);
    }

    public void setShowLockers(boolean value) {
        saveBoolean(ID_SHOW_LOCKERS, value);
    }

    public void setShowWashrooms(boolean value) {
        saveBoolean(ID_SHOW_WASHROOMS, value);
    }

    public void setShowClasses(boolean value) {
        saveBoolean(ID_SHOW_CLASSES, value);
    }

    public void setShowWelcome(boolean value) {saveBoolean(ID_SHOW_WELCOME, value);}

    public void setHighSchool(HighSchools value) {
        SharedPreferences.Editor mEdit = mMyPreferences.edit();
        mEdit.putInt(ID_HIGHSCHOOL, value.ordinal());
        mEdit.apply();
    }

    public void setLunch(@IntRange(from = 1, to = 2) int value) {
        SharedPreferences.Editor mEdit = mMyPreferences.edit();
        mEdit.putInt(ID_LUNCH, value);
        mEdit.apply();
    }

    public void setIsWCDSBapp(Boolean value) {
        saveBoolean(ID_WCDSBAPP, value);
    }
}