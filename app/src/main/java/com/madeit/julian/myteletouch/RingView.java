package com.madeit.julian.myteletouch;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.Calendar;
import java.util.Date;

/**
 * TODO: document your custom view class.
 */
public class RingView extends View {
    private OnOffsetChangeListener offsetListener;
    private OnIsDownChangeListener isDownListener;
    private Point offset = new Point(0, 0);
    private boolean isDown;
    private boolean isInDown;
    private boolean isMouse;
    private Point visualOffset = new Point(0, 0);
    private Paint circlePaintFill;
    private Paint circlePaint;
    private Paint circlePaintInner;
    private int paddingLeft;
    private int paddingTop;
    private int paddingRight;
    private int paddingBottom;
    private int contentWidth;
    private int contentHeight;

    PointF startLocation;
    PointF previousLocation;
    long timeStart;
    long timeLast;

    public void setIsMouse(boolean isMouse) {
        this.isMouse = isMouse;
    }

    public interface OnOffsetChangeListener {
        void onOffsetChanged(View v, Point offset);
    }


    public RingView(Context context) {
        super(context);
        init(null, 0);
    }

    public RingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public RingView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        circlePaintFill = new Paint();
        circlePaintFill.setColor(Color.WHITE);
        circlePaintFill.setStyle(Paint.Style.FILL);
        circlePaintFill.setAlpha(75);

        circlePaint = new Paint();
        circlePaint.setStrokeWidth(3);
        circlePaint.setColor(Color.WHITE);
        circlePaint.setStyle(Paint.Style.STROKE);

        circlePaintInner = new Paint();
        circlePaintInner.setColor(Color.WHITE);
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

        float radiusOut = (Math.min(contentWidth, contentHeight) * 0.75f) / 2;
        float radiusIn = radiusOut / 2.0f;
        float strokeIn = radiusIn / 1.5f;

        Point center = new Point(getWidth() / 2, getHeight() / 2);

        canvas.drawCircle(center.x, center.y, radiusOut, circlePaintFill);

        canvas.drawCircle(center.x, center.y, radiusOut, circlePaint);

        circlePaintInner.setStrokeWidth(strokeIn);
        canvas.drawCircle(center.x + visualOffset.x, center.y + visualOffset.y,
                radiusIn, circlePaintInner);
    }

    public void setOnOffsetChangeListener(OnOffsetChangeListener listener) {
        this.offsetListener = listener;
    }

    public void setIsDownChangeListener(OnIsDownChangeListener listener) {
        this.isDownListener = listener;
    }

    public boolean isDown() {
        return isDown;
    }

    public void setIsDown(boolean isDown) {
        this.isDown = isDown;
        if(isDownListener != null)
            isDownListener.onIsDownChanged(this, isDown);
    }

    public Point getVisualOffset() {
        return visualOffset;
    }

    public void setVisualOffset(Point visualOffset) {
        this.visualOffset = visualOffset;
        this.invalidate();
    }

    public Point getOffset() {
        return offset;
    }

    public void setOffset(Point offset) {
        this.offset = offset;
        if(offsetListener != null)
            offsetListener.onOffsetChanged(this, offset);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                // touch down code
                onTouchDown(new PointF(event.getX(), event.getY()));
                return true;
                //break;

            case MotionEvent.ACTION_MOVE:
                // touch move code
                onTouchMove(new PointF(event.getX(), event.getY()));
                return true;
                //break;

            case MotionEvent.ACTION_UP:
                // touch up code
                try {
                    onTouchUp(new PointF(event.getX(), event.getY()));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return true;
                //break;
        }
        return super.onTouchEvent(event);
    }

    private void onTouchDown(PointF position){
        isInDown = true;
        timeStart = Calendar.getInstance().getTimeInMillis();
        timeLast = timeStart;
        if(isMouse){
            startLocation = position;
            previousLocation = startLocation;
        } else {
            startLocation = new PointF(getWidth()/2, getHeight()/2);
            previousLocation = startLocation;
            handleMoved(position, true);
        }
    }

    private void onTouchMove(PointF position){
        if(isInDown)
        {
            handleMoved(position, false);
        }
    }

    private void onTouchUp(PointF position) throws InterruptedException {
        if(isInDown)
        {
            isInDown = false;

            long timeSinceStartExecutionMs = Math.abs(Calendar.getInstance().getTimeInMillis() - timeStart);
            boolean isMouseTap = isMouse && (Math.abs(offset.x) < 5 && Math.abs(offset.y) < 5 &&
                    timeSinceStartExecutionMs < 500);

            Thread.sleep(100);

            setOffset(new Point());
            setVisualOffset(new Point());

            if (isMouseTap) {
                Thread.sleep(100);
                setIsDown(true);
                Thread.sleep(100);
                setIsDown(false);
                Thread.sleep(100);
            }
        }
    }

    private void handleMoved(PointF location, boolean forceMove) {
        PointF newOffset = new PointF();

        if(isMouse) {
            float factor = 2;
            //var timeSinceStartExecutionMs = abs(NSDate().timeIntervalSince1970 - timeStart) * 1000
            //if(timeSinceStartExecutionMs > 400) {
            //    factor = 2
            //}

            newOffset.x = Math.min(Math.max((location.x - previousLocation.x) * factor, -127), 127);
            newOffset.y = Math.min(Math.max((location.y - previousLocation.y) * factor, -127), 127);
        } else {
            newOffset.x = Math.min(Math.max(location.x - startLocation.x, -127), 127);
            newOffset.y = Math.min(Math.max(location.y - startLocation.y, -127), 127);
        }

        long timeSinceLastExecutionMs = Math.abs(Calendar.getInstance().getTimeInMillis() - timeLast);
        boolean needToUpdate = forceMove || ((timeSinceLastExecutionMs > 20) &&
                (Math.abs(offset.x - newOffset.x) >= 1.0 || Math.abs(offset.y - newOffset.y) >= 1.0));

        if(needToUpdate) {
            PointF newVisualOffset = new PointF();
            newVisualOffset.x = Math.min(Math.max(newOffset.x, -10), 10);
            newVisualOffset.y = Math.min(Math.max(newOffset.y, -10), 10);

            setVisualOffset(new Point((int) newVisualOffset.x, (int) newVisualOffset.y));
            setOffset(new Point((int) newOffset.x, (int) newOffset.y));

            previousLocation = location;
            timeLast = Calendar.getInstance().getTimeInMillis();
        }
    }
}
