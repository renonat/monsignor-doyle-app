package com.natalizioapps.monsignordoyle.ui.views;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.natalizioapps.monsignordoyle.R;
import com.natalizioapps.monsignordoyle.objects.SchoolClass;
import com.natalizioapps.monsignordoyle.utils.ClassesUtils;
import com.natalizioapps.monsignordoyle.utils.singletons.PrefSingleton;

public class ClassCard extends CardView implements View.OnClickListener {

    ClassesCardCallback callback;

    Context mContext;
    View mView, mCircle;
    TextView mClass, mPeriod, mRoom, mTeacher, mTime;
    Button mEdit;

    int period;

    public ClassCard(Context context) {
        this(context, null);
    }

    public ClassCard(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClassCard(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        mView = inflater.inflate(R.layout.cardview_class, null, false);
        addView(mView);

        mCircle = findViewById(R.id.card_class_circle);

        mClass = (TextView) findViewById(R.id.card_class_header_text);
        mPeriod = (TextView) findViewById(R.id.card_class_period);
        mRoom = (TextView) findViewById(R.id.card_class_room);
        mTeacher = (TextView) findViewById(R.id.card_class_teacher);
        mTime = (TextView) findViewById(R.id.card_class_time);

        mEdit = (Button) findViewById(R.id.card_class_edit);
        mEdit.setOnClickListener(this);
    }

    public void setCallback(ClassesCardCallback callback) {
        this.callback = callback;
    }

    public void setClass(int p) {
        SchoolClass c = PrefSingleton.getInstance().getClass(p);
        if (c != null) {
            int color = c.getColor();

            // Create a circular background with the color of choice
            GradientDrawable gradient = new GradientDrawable(
                    GradientDrawable.Orientation.BOTTOM_TOP, new int[]{color, color});
            gradient.setShape(GradientDrawable.OVAL);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mCircle.setBackground(gradient);
            } else {
                mCircle.setBackgroundDrawable(gradient);
            }

            mClass.setText(c.getName());
            mClass.setTextColor(color);
            mTeacher.setText(c.getTeacher());
            period = p;
            mPeriod.setText(String.valueOf(period));
            mTime.setText(ClassesUtils.getTimesForPeriod(c, mContext));
            mRoom.setText(c.getRoom());

            mEdit.setTextColor(color);
        } else {
            Log.d("MonDoyle", "CLass is null @ setClass() in ClassesCard");
        }
    }

    @Override
    public void onClick(View v) {
        callback.onCardEdit(period);
    }

    public interface ClassesCardCallback {
        void onCardEdit(int period);
    }
}
