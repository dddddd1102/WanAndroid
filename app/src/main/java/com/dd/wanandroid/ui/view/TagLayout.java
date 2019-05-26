package com.dd.wanandroid.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dd.wanandroid.R;
import com.dd.wanandroid.entity.HotKey;

import java.util.List;


/**
 * WanAndroid
 *
 * @author daidong
 */
public class TagLayout extends ViewGroup {

    private static final String TAG = "TagLayout";

    // child view`s horizontal space
    private int horizontalSpace = 20;

    // child view`s vertical space
    private int verticalSpace = 10;

    // record current line width
    private int currentLineWidth = 0;

    private int childHeight;

    private OnTagIndexClickListener onTagIndexClickListener;

    public void setOnTagIndexClickListener(OnTagIndexClickListener listener) {
        onTagIndexClickListener = listener;
    }

    public TagLayout(Context context) {
        this(context, null);
    }

    public TagLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TagLayout);
        horizontalSpace = a.getDimensionPixelOffset(R.styleable.TagLayout_horizontal_space, 16);
        verticalSpace = a.getDimensionPixelOffset(R.styleable.TagLayout_vertical_space, 8);
        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        int childCount = getChildCount();
        int line = getChildLine(childCount);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        if (childCount == 0) {
            setMeasuredDimension(0, 0);
        } else if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            setMeasuredDimension(widthSpecSize, (childHeight + verticalSpace) * line + getPaddingTop() + getPaddingBottom());
        } else {
            setMeasuredDimension(widthSpecSize, heightSpecSize);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount < 0) {
            return;
        }
        int width = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        currentLineWidth = 0;
        int line = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            if (child.getVisibility() != GONE) {
                // child show
                int w = child.getMeasuredWidth() + horizontalSpace;
                int cl = getPaddingStart() + currentLineWidth;
                if (currentLineWidth + child.getMeasuredWidth() > width) {
                    line++;
                    currentLineWidth = w;
                    cl = getPaddingStart();
                } else {
                    currentLineWidth += w;
                }
                int ct = getPaddingTop() + line * (childHeight + verticalSpace);

                child.layout(cl, ct, cl + child.getMeasuredWidth(), ct + childHeight);

            } else {
                // TODO 20190526: child hide

            }
        }
    }

    private int getChildLine(int childCount) {
        if (childCount == 0) {
            return 0;
        }
        int width = getMeasuredWidth() - getPaddingStart() - getPaddingEnd();
        int line = 1;
        currentLineWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            int w = child.getMeasuredWidth() + horizontalSpace;
            // calculation child view height
            childHeight = Math.max(childHeight, child.getMeasuredHeight());
            // calculation current line width
            currentLineWidth += w;
            if (currentLineWidth - horizontalSpace > width) {
                line++;
                currentLineWidth = 0;
            }
        }
        return line;
    }

    public void addTags(List<HotKey> hotKeys) {
        removeAllViews();
        for (int i = 0; i < hotKeys.size(); i++) {
            final int index = i;
            HotKey hotKey = hotKeys.get(i);
            TagView tagView = (TagView) LayoutInflater.from(getContext()).inflate(R.layout.item_tag, this, false);
            tagView.setTagText(hotKey.getName());
            tagView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onTagIndexClickListener != null) {
                        onTagIndexClickListener.onClickIndex(index);
                    }
                }
            });
            addView(tagView);
        }
        requestLayout();
        invalidate();
    }

    public interface OnTagIndexClickListener {

        void onClickIndex(int index);
    }
}
