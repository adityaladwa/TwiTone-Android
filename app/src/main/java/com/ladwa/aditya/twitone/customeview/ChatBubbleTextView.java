package com.ladwa.aditya.twitone.customeview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.ladwa.aditya.twitone.R;

/**
 * Created by Aditya on 21-Oct-16.
 */

public class ChatBubbleTextView extends TextView {
    private Drawable mBubbleBackground;
    private int mBubbleTextColor;

    public ChatBubbleTextView(Context context) {
        super(context);
    }

    public ChatBubbleTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomeProperty(context, attrs);

    }

    public ChatBubbleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomeProperty(context, attrs);

    }

    public ChatBubbleTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        applyCustomeProperty(context, attrs);
    }


    private void applyCustomeProperty(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.ChatBubble, 0, 0);

        try {
            mBubbleBackground = a.getDrawable(R.styleable.ChatBubble_bubble_background);
            mBubbleTextColor = a.getColor(R.styleable.ChatBubble_bubble_text_color,
                    ContextCompat.getColor(context, R.color.md_black_1000));
        } finally {
            a.recycle();
        }

        setBackground(mBubbleBackground);
        setTextColor(mBubbleTextColor);
    }

}
