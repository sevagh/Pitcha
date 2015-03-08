package com.sevag.pitcha.gauge.components;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;

/**
 * Created by sevag on 3/8/15.
 */
public class Rim {
    private RectF rimRect;
    private Paint rimPaint;
    private Paint rimCirclePaint;
    private float rimSize = 0.02f;

    public Rim() {
        rimRect = new RectF(0.1f, 0.1f, 0.9f, 0.9f);

        // the linear gradient is a bit skewed for realism
        rimPaint = new Paint();
        rimPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        rimPaint.setShader(new LinearGradient(0.40f, 0.0f, 0.60f, 1.0f,
                Color.rgb(0xf0, 0xf5, 0xf0),
                Color.rgb(0xf0, 0xf5, 0xf0),
                Shader.TileMode.CLAMP));

        rimCirclePaint = new Paint();
        rimCirclePaint.setAntiAlias(true);
        rimCirclePaint.setStyle(Paint.Style.FILL);
        rimCirclePaint.setColor(Color.argb(0x4f, 0x33, 0x36, 0x33));
        rimCirclePaint.setStrokeWidth(0.005f);
    }

    public RectF getRimRect() {
        return this.rimRect;
    }

    public Paint getRimPaint() {
        return this.rimPaint;
    }

    public Paint getRimCirclePaint() {
        return this.rimCirclePaint;
    }

    public float getRimSize() {
        return this.rimSize;
    }
}
