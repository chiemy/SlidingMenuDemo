package com.chiemy.demo.slidingmenu;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

/**
 * Created by chiemy on 2017/2/8.
 */
public class SlidingMenu extends HorizontalScrollView {
    private int contentLayoutId;
    private int menuLayoutId;
    private View contentView;
    private View menuView;
    private int contentPadding;
    private int menuWidth;

    private GestureDetector gestureDetector;
    private int minimueVelocity;

    public SlidingMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SlidingMenu);
        contentLayoutId = ta.getResourceId(R.styleable.SlidingMenu_content_layout, 0);
        menuLayoutId = ta.getResourceId(R.styleable.SlidingMenu_menu_layout, 0);
        contentPadding = ta.getDimensionPixelOffset(R.styleable.SlidingMenu_content_padding, 100);
        ta.recycle();

        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(OVER_SCROLL_NEVER);

        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if (Math.abs(velocityX) >= 2000) {
                    if (velocityX < 0) {
                        smoothScrollTo(menuWidth, 0);
                    } else {
                        smoothScrollTo(0, 0);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LinearLayout wrapper = new LinearLayout(getContext());
        wrapper.setOrientation(LinearLayout.HORIZONTAL);

        LayoutInflater inflater = LayoutInflater.from(getContext());
        menuView = inflater.inflate(menuLayoutId, wrapper, false);
        contentView = inflater.inflate(contentLayoutId, wrapper, false);
        wrapper.addView(menuView, new LinearLayout.LayoutParams(-2, -1));
        wrapper.addView(contentView, new LinearLayout.LayoutParams(-2, -1));

        addView(wrapper, new MarginLayoutParams(MarginLayoutParams.MATCH_PARENT, MarginLayoutParams.MATCH_PARENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        menuWidth = menuView.getLayoutParams().width = getMeasuredWidth() - contentPadding;
        contentView.getLayoutParams().width = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        scrollTo(menuWidth, 0);

    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!gestureDetector.onTouchEvent(ev)) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_UP:
                    int scrollX = getScrollX();
                    if (scrollX >= menuWidth / 2) {
                        smoothScrollTo(menuWidth, 0);
                    } else {
                        smoothScrollTo(0, 0);
                    }
                    return true;
            }
            return super.onTouchEvent(ev);
        }
        return true;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        menuView.setTranslationX(getScrollX());
    }
}
