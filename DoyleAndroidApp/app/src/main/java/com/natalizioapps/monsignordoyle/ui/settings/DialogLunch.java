package com.natalizioapps.monsignordoyle.ui.settings;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.RadioGroup;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

/**
 * Created by Reno on 15-05-16.
 */
public class DialogLunch extends AlertDialog {

    private RadioGroup mRadiogroup;

    public DialogLunch(Context context) {
        super(context);

        setTitle("Select your lunch period");

        // Create a single Ok buton, to save the user's choices
        setButton(BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Get the id of the checked button
                int id = mRadiogroup.getCheckedRadioButtonId();

                // Get the school based on the id of the radiobutton
                if (id == R.id.dialog_lunch_first)
                    PrefSingleton.getInstance().setLunch(1);
                else if (id == R.id.dialog_lunch_second)
                    PrefSingleton.getInstance().setLunch(2);
            }
        });

        // Inflate the content view
        LayoutInflater li = LayoutInflater.from(context);
        View content = li.inflate(R.layout.dialog_lunch, null);

        setView(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        mRadiogroup = (RadioGroup) findViewById(R.id.dialog_lunch_radiogroup);

        int lunch = PrefSingleton.getInstance().getLunch();

        // Precheck the bubble based on the saved preference
        if (lunch == 1) {
            mRadiogroup.check(R.id.dialog_lunch_first);
        } else {
            mRadiogroup.check(R.id.dialog_lunch_second);
        }
    }
}
