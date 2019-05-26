package com.dd.wanandroid.ui.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.TextView;

import com.dd.wanandroid.R;

/**
 * WanAndroid
 *
 * @author daidong
 */
public class TagView extends TextView {

    private float borderWidth;

    private float borderRadius;

    private float textSize;

    private int horizontalPadding;

    private int verticalPadding;

    private int borderColor;

    private int backgroundColor;

    private int textColor;

    private int selectedBackgroundColor;

    private Paint paint;

    private float textWidth;

    private float textHeight;

    private int dy;

    private RectF rectF;

    private String content;

    public TagView(Context context) {
        this(context, null);
    }

    public TagView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagView);
        borderWidth = a.getDimension(R.styleable.TagView_border_width, R.dimen.tag_border_width);
        borderRadius = a.getDimension(R.styleable.TagView_border_radius, R.dimen.tag_border_radius);
        horizontalPadding = a.getDimensionPixelOffset(R.styleable.TagView_horizontal_padding, R.dimen.tag_horizontal_padding);
        verticalPadding = a.getDimensionPixelOffset(R.styleable.TagView_vertical_padding, R.dimen.tag_vertical_padding);
        textSize = a.getDimensionPixelOffset(R.styleable.TagView_tag_textSize, R.dimen.primary_text_size);
        borderColor = a.getColor(R.styleable.TagView_border_color, getResources().getColor(R.color.colorAccent));
        backgroundColor = a.getColor(R.styleable.TagView_tag_backgroundColor, getResources().getColor(R.color.colorAccent));
        selectedBackgroundColor = a.getColor(R.styleable.TagView_tag_selectedBackgroundColor, getResources().getColor(R.color.colorAccent));
        textColor = a.getColor(R.styleable.TagView_tag_textColor, getResources().getColor(R.color.white));
        content = a.getString(R.styleable.TagView_tag_text);
        a.recycle();


        rectF = new RectF();

        initPaint();

        initContentSize();
    }

    private void initPaint() {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(textSize);
        paint.setColor(borderColor);
    }

    private void initContentSize() {
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        textHeight = fontMetrics.descent - fontMetrics.ascent;
        textWidth = paint.measureText(content);
        dy = (int) ((fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.bottom);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = (int) (horizontalPadding * 2 + textWidth);
        int height = (int) (verticalPadding * 2 + textHeight);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        rectF.set(borderWidth, borderWidth, w - borderWidth, h - borderWidth);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        // draw background
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(backgroundColor);
        canvas.drawRoundRect(rectF, borderRadius, borderRadius, paint);
        // draw border
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(borderColor);
        paint.setStrokeWidth(borderWidth);
        canvas.drawRoundRect(rectF, borderRadius, borderRadius, paint);
        // draw text
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(textColor);
        canvas.drawText(content, getWidth() / 2f - textWidth / 2, getHeight() / 2f + dy, paint);

    }

    public void setTagText(@StringRes int resId) {
        setTagText(getResources().getString(resId));
    }

    public void setTagText(String text) {
        content = text;
        initContentSize();
        invalidate();
    }
}
