package com.natalizioapps.monsignordoyle.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.enums.HighSchools;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

/**
 * Created by Reno on 15-05-16.
 */
public class DialogSchools extends AlertDialog {

    private RadioGroup mRadiogroup;

    public DialogSchools(Context context) {
        super(context);

        // Set the title of the dialog
        setTitle("Choose your school");

        // Create a single Ok buton, to save the user's choices
        setButton(BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Get the id of the checked button
                int id = mRadiogroup.getCheckedRadioButtonId();

                // If none is selected, save as UNDEF
                HighSchools school = HighSchools.UNDEF;

                // Get the school based on the id of the radiobutton
                if (id == R.id.dialog_schools_mondoyle)
                    school = HighSchools.MONSIGNORDOYLE;
                else if (id == R.id.dialog_schools_benedicts)
                    school = HighSchools.STBENEDICTS;
                else if (id == R.id.dialog_schools_marys)
                    school = HighSchools.STMARYS;
                else if (id == R.id.dialog_schools_davids)
                    school = HighSchools.STDAVIDS;
                else if (id == R.id.dialog_schools_resurrection)
                    school = HighSchools.RESURRECTION;

                // Save it in preferences
                PrefSingleton.getInstance().setHighSchool(school);
            }
        });

        // Inflate the content view
        LayoutInflater li = LayoutInflater.from(context);
        View content = li.inflate(R.layout.dialog_schools, null);

        setView(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mRadiogroup = (RadioGroup) findViewById(R.id.dialog_schools_radiogroup);

        // Precheck the bubble based on already selected school from preferences
        HighSchools school = PrefSingleton.getInstance().getHighSchool();

        if (school == HighSchools.MONSIGNORDOYLE) {
            mRadiogroup.check(R.id.dialog_schools_mondoyle);
        } else if (school == HighSchools.STBENEDICTS) {
            mRadiogroup.check(R.id.dialog_schools_benedicts);
        } else if (school == HighSchools.STMARYS) {
            mRadiogroup.check(R.id.dialog_schools_marys);
        } else if (school == HighSchools.STDAVIDS) {
            mRadiogroup.check(R.id.dialog_schools_davids);
        } else if (school == HighSchools.RESURRECTION) {
            mRadiogroup.check(R.id.dialog_schools_resurrection);
        }
    }
}
