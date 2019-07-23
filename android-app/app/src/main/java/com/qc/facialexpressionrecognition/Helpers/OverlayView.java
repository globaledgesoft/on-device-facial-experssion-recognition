package com.qc.facialexpressionrecognition.Helpers;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class OverlayView extends View {

    private static final String TAG = OverlayView.class.getSimpleName();
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private Paint mTextPaint;
    private Rect mRect;
    private static final int BACKGROUND = Color.TRANSPARENT;
    private int width;
    private int height;

    public OverlayView(Context context) {
        this(context, null);
    }

    public OverlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public OverlayView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCanvas = new Canvas();
        initPaint();
    }

    /**
     * Method to clear canvas
     */
    public void clear() {
        mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        mBitmap.eraseColor(BACKGROUND);
        mCanvas.setBitmap(mBitmap);
        postInvalidate();
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        clear();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(BACKGROUND);
        if (mBitmap != null)
            canvas.drawBitmap(mBitmap, 0, 0, null);
        if (null != mRect) {
            canvas.drawRect(mRect, mPaint);

        }
    }

    /**
     * Sets up paint attributes.
     */
    private void initPaint() {
        mPaint = new Paint();
        mTextPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(5);

        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(45);
        mTextPaint.setColor(Color.WHITE);
    }

    /**
     * Mrthod to draw face rect on canvas
     * @param rect
     * @param mWidth
     * @param mHeight
     */
    public void setRect(Rect rect, int mWidth, int mHeight) {
        this.width = mWidth;
        this.height = mHeight;
        this.mRect = rect;
        this.clear();
    }

    /**
     * Method to input facial expression text on canvas
     * @param text
     * @param x
     * @param y
     */
    public void setText(String text, int x, int y) {
        mCanvas.drawText(text, x + 20, y + 40, mTextPaint);
    }


}
