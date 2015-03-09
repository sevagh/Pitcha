package com.sevag.pitcha.gauge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.sevag.pitcha.gauge.components.Hand;
import com.sevag.pitcha.gauge.components.Rim;
import com.sevag.pitcha.gauge.components.Scale;

public final class NeedleGauge extends View {
    private Rim rimComponent = new Rim();
    private Hand handComponent = new Hand();
    private Scale scaleComponent = new Scale();

    private RectF rimRect = rimComponent.getRimRect();
    private Paint rimCirclePaint = rimComponent.getRimCirclePaint();
    private float rimSize = rimComponent.getRimSize();

    private RectF scaleRect = scaleComponent.getScaleRect();
    private float scalePosition = scaleComponent.getScalePosition();

    private RectF faceRect;

    private Paint backgroundPaint;
    private Bitmap background;

    public NeedleGauge(Context context) {
        super(context);
        init();
    }

    public NeedleGauge(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NeedleGauge(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void init() {
        initDrawingTools();
    }

    private void initDrawingTools() {
        faceRect = new RectF();
        faceRect.set(rimRect.left + rimSize, rimRect.top + rimSize,
                rimRect.right - rimSize, rimRect.bottom - rimSize);

        scaleRect.set(faceRect.left + scalePosition, faceRect.top + scalePosition,
                faceRect.right - scalePosition, faceRect.bottom - scalePosition);

        backgroundPaint = new Paint();
        backgroundPaint.setFilterBitmap(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int chosenWidth = chooseDimension(widthMode, widthSize);
        int chosenHeight = chooseDimension(heightMode, heightSize);

        int chosenDimension = Math.min(chosenWidth, chosenHeight);

        setMeasuredDimension(chosenDimension, chosenDimension);
    }

    private int chooseDimension(int mode, int size) {
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.EXACTLY) {
            return size;
        } else { // (mode == MeasureSpec.UNSPECIFIED)
            return getPreferredSize();
        }
    }

    // in case there is no size specified
    private int getPreferredSize() {
        return 300;
    }

    private void drawFace(Canvas canvas) {
        canvas.drawOval(faceRect, rimCirclePaint);
    }

    private void drawBackground(Canvas canvas) {
        if (background != null) {
            canvas.drawBitmap(background, 0, 0, backgroundPaint);
        }
    }

    private void regenerateBackground() {
        background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas backgroundCanvas = new Canvas(background);

        float scale = (float) getWidth();
        backgroundCanvas.scale(scale, scale);
        rimComponent.drawRim(backgroundCanvas);
        drawFace(backgroundCanvas);
        scaleComponent.drawScale(backgroundCanvas);
        handComponent.drawHand(backgroundCanvas);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        drawBackground(canvas);
        regenerateBackground();
    }

    public void setHandTarget(String note, double value) {
        if (value < Scale.MIN_VALUE) {
            value = Scale.MIN_VALUE;
        } else if (value > Scale.MAX_VALUE) {
            value = Scale.MAX_VALUE;
        }

        handComponent.handTarget = (float) value;
        scaleComponent.NOTE_STRING = note;

        if (handComponent.handNeedsToMove()) {
            handComponent.moveHand();
        }
        invalidate();
    }
}
