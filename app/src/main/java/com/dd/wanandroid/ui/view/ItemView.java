package com.dd.wanandroid.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.constraint.ConstraintLayout;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.dd.wanandroid.R;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class ItemView extends ConstraintLayout {

    private boolean hasSwitch = false;

    private ImageView ivIcon;

    private TextView tvTitle;

    private Switch switchMode;

    public ItemView(Context context) {
        this(context, null);
    }

    public ItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.view_item, this);
        ivIcon = findViewById(R.id.iv_icon);
        tvTitle = findViewById(R.id.tv_title);
        switchMode = findViewById(R.id.switch_mode);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ItemView);
        Drawable d = a.getDrawable(R.styleable.ItemView_android_src);
        if (d != null) {
            ivIcon.setImageDrawable(d);
        }
        CharSequence c = a.getText(R.styleable.ItemView_android_text);
        if (!TextUtils.isEmpty(c)) {
            tvTitle.setText(c);
        }
        hasSwitch = a.getBoolean(R.styleable.ItemView_has_switch, hasSwitch);
        switchMode.setVisibility(hasSwitch ? VISIBLE : GONE);
        a.recycle();


    }
}
