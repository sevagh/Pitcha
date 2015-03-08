package com.sevag.pitcha.gauge.components;

import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;

/**
 * Created by sevag on 3/8/15.
 */
public class Title {
    private Paint titlePaint;
    private Path titlePath;

    public Title() {
        titlePaint = new Paint();
        titlePaint.setColor(0xaf946109);
        titlePaint.setAntiAlias(true);
        titlePaint.setTypeface(Typeface.DEFAULT_BOLD);
        titlePaint.setTextAlign(Paint.Align.CENTER);
        titlePaint.setTextSize(0.05f);
        titlePaint.setTextScaleX(0.8f);

        titlePath = new Path();
        titlePath.addArc(new RectF(0.24f, 0.24f, 0.76f, 0.76f), -180.0f, -180.0f);
    }

    public Paint getTitlePaint() {
        return this.titlePaint;
    }

    public Path getTitlePath() {
        return this.titlePath;
    }
}
