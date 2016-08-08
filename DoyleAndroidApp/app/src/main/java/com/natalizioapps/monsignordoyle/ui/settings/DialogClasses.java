package com.natalizioapps.monsignordoyle.ui.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.natalizioapps.materialdesign.colorpicker.ColorPickerDialog;
import com.natalizioapps.materialdesign.colorpicker.ColorPickerSwatch;
import com.natalizioapps.materialdesign.colorpicker.ColorUtils;
import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.objects.SchoolClass;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;
import com.rengwuxian.materialedittext.MaterialAutoCompleteTextView;
import com.rengwuxian.materialedittext.MaterialEditText;

/**
 * Created by Reno on 15-05-16.
 */
public class DialogClasses extends AlertDialog implements View.OnClickListener,
        ColorPickerSwatch.OnColorSelectedListener {

    View mCircle;
    MaterialEditText mClass, mTeacher;
    MaterialAutoCompleteTextView mRoom;

    int _color;

    int _period;
    Context _context;

    public DialogClasses(Context context, int period) {
        super(context);
        this._period = period;
        this._context = context;

        setTitle("Period " + _period);

        // Create a single Ok buton, to save the user's choices
        setButton(BUTTON_POSITIVE, "Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Save their data to preferences
                SchoolClass c = new SchoolClass.Builder()
                        .period(_period)
                        .color(_color)
                        .name(mClass.getText().toString())
                        .room(mRoom.getText().toString())
                        .teacher(mTeacher.getText().toString())
                        .build();
                PrefSingleton.getInstance().saveClass(c);
            }
        });

        // Inflate the content view
        LayoutInflater li = LayoutInflater.from(context);
        View content = li.inflate(R.layout.dialog_classes, null);

        setView(content);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        SchoolClass c = PrefSingleton.getInstance().getClass(_period);
        mCircle = findViewById(R.id.edit_class_circle);
        mClass = (MaterialEditText) findViewById(R.id.edit_class_name);
        mTeacher = (MaterialEditText) findViewById(R.id.edit_class_teacher);
        mRoom = (MaterialAutoCompleteTextView)
                findViewById(R.id.edit_class_room);

        /* // Set the array adapter for the room TextView
        List<String> data = new ArrayList<>(HotspotDataSingleton.getInstance().getRoomNames());
        Collections.sort(data);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(_context, R.layout
        .autocomplete_list_item, data);
        mRoom.setAdapter(adapter);
        mRoom.setThreshold(1); */

        _color = c.getColor();

        // Set the text from the class
        mClass.setText(c.getName());
        mTeacher.setText(c.getTeacher());
        mRoom.setText(c.getRoom());

        mCircle.setOnClickListener(this);

        refreshColor();
    }

    @Override
    public void onClick(View v) {
        ColorPickerDialog colorPickerDialog = ColorPickerDialog.newInstance(
                R.string.color_picker_default_title,
                ColorUtils.colorChoice(_context),
                _color, 4,
                ColorPickerDialog.SIZE_SMALL);
        colorPickerDialog.setOnColorSelectedListener(this);

        // Try to show the colorpicker
        try {
            final Activity activity = (Activity) _context;
            // Return the fragment manager
            colorPickerDialog.show(activity.getFragmentManager(), "cal");
        } catch (ClassCastException e) {
            Log.d("WCDSB", "Can't get the fragment manager with this");
        }
    }


    protected void refreshColor() {
        // Draw the custom color circle drawable
        GradientDrawable gradient = new GradientDrawable(
                GradientDrawable.Orientation.BOTTOM_TOP, new int[]{_color, _color});
        gradient.setShape(GradientDrawable.OVAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mCircle.setBackground(gradient);
        } else {
            mCircle.setBackgroundDrawable(gradient);
        }

        // Color the textviews
        mClass.setPrimaryColor(_color);
        mTeacher.setPrimaryColor(_color);
        mRoom.setPrimaryColor(_color);
    }

    @Override
    public void onColorSelected(int c) {
        _color = c;
        refreshColor();
    }
}
