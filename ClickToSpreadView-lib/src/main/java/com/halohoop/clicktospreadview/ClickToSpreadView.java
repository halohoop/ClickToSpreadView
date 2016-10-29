/*
 * Copyright (C) 2016, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * ClickToSpreadView.java
 *
 * Touch To Spread like a ripple
 *
 * Author huanghaiqi, Created at 2016-10-22
 *
 * Ver 1.0, 2016-10-22, huanghaiqi, Create file.
 */

package com.halohoop.clicktospreadview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

public class ClickToSpreadView extends FrameLayout {
    private final Context mContext;
    private WindowManager mWm;
    private WindowManager.LayoutParams mLayoutParams;
    private final static int DEFAULT_RIPPLE_COLOR = Color.GRAY;
    private int mRippleColor = DEFAULT_RIPPLE_COLOR;
    private final static long DEFAULT_ANIM_DURATION = 1000;
    private long mAnimDuration = DEFAULT_ANIM_DURATION;
    private final static float DEFAULT_END_RADIUS = 720;
    private float mRippleEndRadius = DEFAULT_END_RADIUS;
    private final static float DEFAULT_START_STROKE_WIDTH = 100;
    private float mStartStrokeWidth = DEFAULT_START_STROKE_WIDTH;
    private Activity mActivity;

    public ClickToSpreadView(Context context) {
        this(context, null);
    }

    public ClickToSpreadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClickToSpreadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mActivity = (Activity) context;
        this.mContext = context;
        initAttr(context, attrs);
        init();
    }

    private void initAttr(Context context, AttributeSet attrs) {
        TypedArray attributes = context.obtainStyledAttributes(attrs,
                com.halohoop.clicktospreadview.R.styleable.ClickToSpreadView);
        mStartStrokeWidth = attributes.getFloat(
                com.halohoop.clicktospreadview.R.styleable.ClickToSpreadView_start_stroke_width,
                DEFAULT_START_STROKE_WIDTH);
        mRippleEndRadius = attributes.getFloat(
                com.halohoop.clicktospreadview.R.styleable.ClickToSpreadView_end_radius,
                DEFAULT_END_RADIUS);
        mRippleColor = attributes.getColor(
                com.halohoop.clicktospreadview.R.styleable.ClickToSpreadView_stroke_color,
                DEFAULT_RIPPLE_COLOR);
        mAnimDuration = (long) attributes.getFloat(
                R.styleable.ClickToSpreadView_animation_duration,
                DEFAULT_ANIM_DURATION);
    }

    private void init() {
        mWm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Point screenSize = getScreenSize(mContext);
        mLayoutParams = new WindowManager.LayoutParams();
        mLayoutParams.alpha = 1.0f;
        mLayoutParams.format = PixelFormat.RGBA_8888;
        mLayoutParams.width = screenSize.x;
        mLayoutParams.height = screenSize.y;
        mLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        mLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        mLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mLayoutParams.x = 0;
        mLayoutParams.y = 0;
    }

    public static Point getScreenSize(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point out = new Point();
        if (Build.VERSION.SDK_INT >= 13) {
            display.getSize(out);
        } else {
            int width = display.getWidth();
            int height = display.getHeight();
            out.set(width, height);
        }
        return out;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!Settings.canDrawOverlays(mContext)) {
                    Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                    mContext.startActivity(intent);
                } else {
                    float downRawX = event.getRawX();
                    float downRawY = event.getRawY();
                    RippleView rippleView = new RippleView(mContext, downRawX, downRawY);
                    rippleView.startRipple();
//                    rippleView.startRipple(720);
                }
                break;
        }
        return super.dispatchTouchEvent(event);
    }

    private class RippleView extends View {
        private Paint mPaint;
        private float mDownRawY;
        private float mDownRawX;
        private float mRippleRadius = 0;

        public RippleView(Context context, float downRawX, float downRawY) {
            this(context, null, 0);
            this.mDownRawX = downRawX;
            this.mDownRawY = downRawY;
        }

        private RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
            mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mPaint.setStyle(Paint.Style.STROKE);
            mPaint.setStrokeWidth(mStartStrokeWidth);
            mPaint.setColor(mRippleColor);
            mPaint.setAlpha(255);
        }

        public void startRipple() {
            startRipple(mRippleEndRadius);
        }

        public void startRipple(float endRadius) {
            mRippleEndRadius = endRadius;
            final float distance = Math.abs(mRippleEndRadius - 0);
            final float tmpStartRadius = 0;
            final ValueAnimator rippleAnim = ValueAnimator.ofFloat(0, mRippleEndRadius);
            rippleAnim.setDuration(mAnimDuration);
            rippleAnim.setInterpolator(new LinearInterpolator());
            rippleAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mRippleRadius = (float) animation.getAnimatedValue();
                    float currenDistan = Math.abs(mRippleRadius - tmpStartRadius);
                    float currentPercent = currenDistan / distance;
                    mPaint.setAlpha((int) (255 * (1 - currentPercent)));
                    mPaint.setStrokeWidth(mStartStrokeWidth * (1 - currentPercent));
                    invalidate();
                }
            });
            rippleAnim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mWm.addView(RippleView.this, mLayoutParams);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    if (RippleView.this.isAttachedToWindow()) {
                        mWm.removeView(RippleView.this);
                    }
                }
            });
            rippleAnim.start();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            //draw circle
            canvas.drawCircle(mDownRawX, mDownRawY, mRippleRadius, mPaint);
        }
    }
}
