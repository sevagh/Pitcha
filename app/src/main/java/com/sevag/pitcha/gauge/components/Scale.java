package com.sevag.pitcha.gauge.components;

import android.graphics.Canvas;
import android.graphics.Color;
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

    public static final int TOTAL_NOTCHES = 20;
    public static final float VALUES_PER_NOTCH = 360.0f / TOTAL_NOTCHES;
    public static final int CENTER_VALUE = 100;
    public static final int MIN_VALUE = 90;
    public static final int MAX_VALUE = 110;

    public Scale() {
        scalePaint = new Paint();
        scalePaint.setStyle(Paint.Style.FILL);
        scalePaint.setColor(Color.BLACK);
        scalePaint.setStrokeWidth(0.005f);
        scalePaint.setAntiAlias(true);

        scalePaint.setTextSize(0.075f);
        scalePaint.setTypeface(Typeface.DEFAULT);
        scalePaint.setTextScaleX(0.8f);
        scalePaint.setTextAlign(Paint.Align.CENTER);

        scaleRect = new RectF();
    }

    public RectF getScaleRect() {
        return this.scaleRect;
    }

    public float getScalePosition() {
        return this.scalePosition;
    }

    public void drawScale(Canvas canvas) {
        canvas.drawOval(scaleRect, scalePaint);

        for (int i = 0; i < TOTAL_NOTCHES; ++i) {
            float y1 = scaleRect.top;
            float y2 = y1 - 0.020f;

            canvas.drawLine(0.5f, y1, 0.5f, y2, scalePaint);

            int value = notchToDegree(i);

            if (value >= MIN_VALUE && value <= MAX_VALUE) {
                if ((value == 95) || (value == 100) || (value == 105)) {
                    String valueString = Integer.toString(value);
                    canvas.drawText(valueString, 0.5f, y2 - 0.015f, scalePaint);
                }
            }
            canvas.rotate(VALUES_PER_NOTCH, 0.5f, 0.5f);
        }
    }

    private int notchToDegree(int notch) {
        int rawDegree = ((notch < TOTAL_NOTCHES / 2) ? notch : (notch - TOTAL_NOTCHES)) * 1;
        int shiftedDegree = rawDegree + CENTER_VALUE;
        return shiftedDegree;
    }
}
