package com.sevag.pitcha.gauge.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * Created by sevag on 3/8/15.
 */
public class Scale {

    private Paint scalePaint;
    private RectF scaleRect;
    private float scalePosition = 0.10f;

    public static final int TOTAL_NOTCHES = 200;
    public static final float VALUES_PER_NOTCH = 360.0f / TOTAL_NOTCHES;
    public static final int CENTER_VALUE = 100;
    public static final int MIN_VALUE = -100;
    public static final int MAX_VALUE = 100;

    public Scale() {
        scalePaint = new Paint();
        scalePaint.setStyle(Paint.Style.STROKE);
        scalePaint.setColor(0x9f004d0f);
        scalePaint.setStrokeWidth(0.005f);
        scalePaint.setAntiAlias(true);

        scalePaint.setTextSize(0.045f);
        scalePaint.setTypeface(Typeface.SANS_SERIF);
        scalePaint.setTextScaleX(0.8f);
        scalePaint.setTextAlign(Paint.Align.CENTER);

        scaleRect = new RectF();
    }

    public RectF getScaleRect() {
        return this.scaleRect;
    }

    public Paint getScalePaint() {
        return this.scalePaint;
    }

    public float getScalePosition() {
        return this.scalePosition;
    }

    public void drawScale(Canvas canvas) {
        canvas.drawOval(scaleRect, scalePaint);

        canvas.save(Canvas.MATRIX_SAVE_FLAG);
        for (int i = 0; i < Scale.TOTAL_NOTCHES; ++i) {
            float y1 = scaleRect.top;
            float y2 = y1 - 0.020f;

            canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

            if (i % 5 == 0) {
                int value = notchToDegree(i);

                if (value >= Scale.MIN_VALUE && value <= Scale.MAX_VALUE) {
                    String valueString = Integer.toString(value);
                    canvas.drawText(valueString, 0.5f, y2 - 0.015f, scalePaint);
                }
            }

            canvas.rotate(Scale.VALUES_PER_NOTCH, 0.5f, 0.5f);
        }
        canvas.restore();
    }

    private int notchToDegree(int notch) {
        int rawDegree = ((notch < TOTAL_NOTCHES / 2) ? notch : (notch - TOTAL_NOTCHES)) * 2;
        int shiftedDegree = rawDegree + CENTER_VALUE;
        return shiftedDegree;
    }
}
