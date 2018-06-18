package com.madeit.julian.myteletouch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * TODO: document your custom view class.
 */
public class RectButtonView extends View {
    private OnIsDownChangeListener isDownListener;
    private boolean isDown;
    private boolean drawLines;
    private Paint rectPaintFill;
    private Paint linePaint;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int contentWidth;
    private int contentHeight;

    public RectButtonView(Context context) {
        super(context);
        init(null, 0);
    }

    public RectButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RectButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        rectPaintFill = new Paint();
        rectPaintFill.setColor(Color.WHITE);
        rectPaintFill.setStyle(Paint.Style.FILL);
        rectPaintFill.setAlpha(75);

        linePaint = new Paint();
        linePaint.setStrokeWidth(3);
        linePaint.setColor(Color.WHITE);
        linePaint.setStyle(Paint.Style.STROKE);

        TypedArray a = getContext().obtainStyledAttributes(
                attrs,
                R.styleable.RectButtonView);

        drawLines = a.getBoolean(R.styleable.RectButtonView_drawLines, false);

        a.recycle();
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

        if(isDown) {
            canvas.drawRect(0, 0, contentWidth, contentHeight, rectPaintFill);
        }

        if(drawLines) {
            canvas.drawLine(0, 10, 0, contentHeight-20, linePaint);
            canvas.drawLine(contentWidth, 10, contentWidth, contentHeight-20, linePaint);
        }
    }

    public void setIsDownChangeListener(OnIsDownChangeListener listener) {
        this.isDownListener = listener;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setIsDown(boolean isDown) {
        this.isDown = isDown;
        this.invalidate();
        if(isDownListener != null)
            isDownListener.onIsDownChanged(this, isDown);
    }

    public boolean getDrawLines() {
        return drawLines;
    }

    public void setDrawLines(boolean drawLines) {
        this.drawLines = drawLines;
        this.invalidate();
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
