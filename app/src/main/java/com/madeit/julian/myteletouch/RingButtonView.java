package com.madeit.julian.myteletouch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class RingButtonView extends View {
    private OnIsDownChangeListener isDownListener;
    private boolean isDown;
    private Paint circlePaintFill;
    private Paint circlePaint;
    private Paint circlePaintInner;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int contentWidth;
    private int contentHeight;
    private int innerColor;

    public RingButtonView(Context context) {
        super(context);
        init(null, 0);
    }

    public RingButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RingButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {

        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.RingButtonView);

        innerColor = a.getColor(R.styleable.RingButtonView_innerColor, Color.WHITE);

        a.recycle();

        circlePaintFill = new Paint();
        circlePaintFill.setColor(Color.WHITE);
        circlePaintFill.setStyle(Paint.Style.FILL);
        circlePaintFill.setAlpha(75);

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(3);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);

        circlePaintInner = new Paint();
        circlePaintInner.setColor(innerColor);
        circlePaintInner.setStyle(Paint.Style.STROKE);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        paddingLeft = getPaddingLeft();
        paddingTop = getPaddingTop();
        paddingRight = getPaddingRight();
        paddingBottom = getPaddingBottom();

        contentWidth = getWidth() - paddingLeft - paddingRight;
        contentHeight = getHeight() - paddingTop - paddingBottom;

        float radiusOut = Math.min(contentWidth, contentHeight) / 2;
        float radiusIn = radiusOut / 2.0f;
        float strokeIn = radiusIn / 1.5f;

        Point center = new Point(getWidth() / 2, getHeight() / 2);

        if(isDown) {
            canvas.drawCircle(center.x, center.y, radiusOut, circlePaintFill);
        }

        canvas.drawCircle(center.x, center.y, radiusOut, circlePaint);

        circlePaintInner.setStrokeWidth(strokeIn);
        canvas.drawCircle(center.x, center.y, radiusIn, circlePaintInner);
    }

    public void setIsDownChangeListener(OnIsDownChangeListener listener) {
        this.isDownListener = listener;
    }

    public boolean isDown() {
        return this.isDown;
    }

    public void setIsDown(boolean isDown) {
        this.isDown = isDown;
        this.invalidate();
        if(isDownListener != null)
            isDownListener.onIsDownChanged(this, isDown);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                setIsDown(true);
                return true;

            case MotionEvent.ACTION_UP:
                setIsDown(false);
                return true;
        }
        return super.onTouchEvent(event);
    }
}
