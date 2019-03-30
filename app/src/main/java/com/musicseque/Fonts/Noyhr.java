package com.musicseque.Fonts;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by afzal on 29/8/16.
 */
public class Noyhr extends TextView {
    public Noyhr(Context context) {
        super(context);
        init(context);

    }

    public Noyhr(Context context, AttributeSet attrs) {
        super(context, attrs);init(context);
    }

    public Noyhr(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public Noyhr(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    public void init(Context context) {
        Typeface tf = Typeface.createFromAsset(context.getAssets(), "fonts/Noyhr-Extralight.otf");
        setTypeface(tf);

    }
}
