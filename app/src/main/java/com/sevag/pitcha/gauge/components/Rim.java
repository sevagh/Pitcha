package com.sevag.pitcha.gauge.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by sevag on 3/8/15.
 */
public class Rim {
    private RectF rimRect;
    private Paint rimCirclePaint;
    private float rimSize = 0.02f;

    public Rim() {
        rimRect = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

        rimCirclePaint = new Paint();
        rimCirclePaint.setAntiAlias(true);
        rimCirclePaint.setStyle(Paint.Style.FILL);
        rimCirclePaint.setColor(Color.CYAN);
        rimCirclePaint.setStrokeWidth(0.005f);
    }

    public RectF getRimRect() {
        return this.rimRect;
    }

    public Paint getRimCirclePaint() {
        return this.rimCirclePaint;
    }

    public float getRimSize() {
        return this.rimSize;
    }

    public void drawRim(Canvas canvas) {
        canvas.drawOval(rimRect, rimCirclePaint);
    }
}
