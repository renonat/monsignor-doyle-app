package com.natalizioapps.monsignordoyle.ui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.natalizioapps.monsignordoyle.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Reno on 14-10-14.
 */
public class AvatarListItem extends RelativeLayout {

    @InjectView(R.id.v_listitem_circle) View mVListitemCircle;
    @InjectView(R.id.iv_listitem_circle) ImageView mIvListitemCircle;
    @InjectView(R.id.tv_listitem_main) TextView mTvListitemMain;
    @InjectView(R.id.tv_listitem_sub) TextView mTvListitemSub;


    public AvatarListItem(Context context) {
        this(context, null);
    }

    public AvatarListItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AvatarListItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // Get the variables from the xml layout
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.AvatarListItem,
                0, 0);

        String mTextMain, mTextSub;
        int mAvatarColor;
        Drawable mAvatar;

        try {
            mAvatar = a.getDrawable(R.styleable.AvatarListItem_avatarSrc);
            mAvatarColor = a.getColor(R.styleable.AvatarListItem_avatarColor,
                    R.color.material_blue_grey_500);
            mTextMain = a.getString(R.styleable.AvatarListItem_textMain);
            mTextSub = a.getString(R.styleable.AvatarListItem_textSub);
        } finally {
            a.recycle();
        }

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context
                .LAYOUT_INFLATER_SERVICE);
        View mView = inflater.inflate(R.layout.listitem_material, null, false);
        addView(mView);

        ButterKnife.inject(this, mView);

        // If the provided avatar is not null set it in the circle
        setAvatar(mAvatar);
        setAvatarColor(mAvatarColor);
        setSubText(mTextSub);
        setText(mTextMain);
    }

    public void setAvatar(int id) {
        setAvatar(getResources().getDrawable(id));
    }

    public void setAvatar(Drawable avatar) {
        if (avatar != null)
            mIvListitemCircle.setImageDrawable(avatar);
    }

    public void setAvatarColor(int color) {
        // Create a circular background with the color of choice
        GradientDrawable gradient = new GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP,
                new int[]
                        {color, color});
        gradient.setShape(GradientDrawable.OVAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mVListitemCircle.setBackground(gradient);
        } else {
            mVListitemCircle.setBackgroundDrawable(gradient);
        }
    }

    public void setText(String text) {
        if (text != null)
            mTvListitemMain.setText(text);
    }

    public void setSubText(String text) {
        if (text == null || text.isEmpty()) {
            mTvListitemSub.setVisibility(GONE);
        } else {
            mTvListitemSub.setVisibility(VISIBLE);
            mTvListitemSub.setText(text);
        }
    }

}
