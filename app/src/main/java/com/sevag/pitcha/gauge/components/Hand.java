package com.sevag.pitcha.gauge.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;

/**
 * Created by sevag on 3/8/15.
 */
public class Hand {

    private View parentView;

    public boolean handInitialized = false;
    public float handPosition = Scale.CENTER_VALUE;
    public float handTarget = Scale.CENTER_VALUE;
    public String handText = "";
    public float handVelocity = 0.0f;
    public float handAcceleration = 0.0f;
    public long lastHandMoveTime = -1L;

    private Paint handPaint;
    private Path handPath;
    private Paint handScrewPaint;
    private Paint handTextPaint;

    private Canvas canvas;

    public Hand(View view) {
        this.parentView = view;

        handPaint = new Paint();
        handPaint.setAntiAlias(true);
        handPaint.setColor(Color.MAGENTA);
        handPaint.setStyle(Paint.Style.FILL);

        /*
        handTextPaint = new Paint();
        handTextPaint.setAntiAlias(true);
        handTextPaint.setColor(Color.GREEN);
        handTextPaint.setStyle(Paint.Style.FILL);
        */

        handPath = new Path();
        handPath.moveTo(0.5f, 0.5f + 0.2f);
        handPath.lineTo(0.5f - 0.010f, 0.5f + 0.2f - 0.007f);
        handPath.lineTo(0.5f - 0.002f, 0.5f - 0.32f);
        handPath.lineTo(0.5f + 0.002f, 0.5f - 0.32f);
        handPath.lineTo(0.5f + 0.010f, 0.5f + 0.2f - 0.007f);
        handPath.lineTo(0.5f, 0.5f + 0.2f);
        handPath.addCircle(0.5f, 0.5f, 0.025f, Path.Direction.CW);

        handScrewPaint = new Paint();
        handScrewPaint.setAntiAlias(true);
        handScrewPaint.setColor(Color.CYAN);
        handScrewPaint.setStyle(Paint.Style.FILL);
    }

    public void setCanvas(Canvas canvasParam) {
        this.canvas = canvasParam;
    }

    public void drawHand() {
        float handAngle = degreeToAngle(handPosition);
        canvas.rotate(handAngle, 0.5f, 0.5f);
        canvas.drawPath(handPath, handPaint);
        //canvas.drawText(handText, 0, 0, handTextPaint);

        canvas.drawCircle(0.5f, 0.5f, 0.01f, handScrewPaint);
    }

    private float degreeToAngle(float degree) {
        return (degree - Scale.CENTER_VALUE) / 2.0f * Scale.VALUES_PER_NOTCH;
    }

    public boolean handNeedsToMove() {
        return Math.abs(handPosition - handTarget) > 0.01f;
    }

    public void moveHand() {
        if (!handNeedsToMove()) {
            return;
        }
        if (lastHandMoveTime != -1L) {
            long currentTime = System.currentTimeMillis();
            float delta = (currentTime - lastHandMoveTime) / 1000.0f;

            float direction = Math.signum(handVelocity);
            if (Math.abs(handVelocity) < 90.0f) {
                handAcceleration = 5.0f * (handTarget - handPosition);
            } else {
                handAcceleration = 0.0f;
            }
            handPosition += handVelocity * delta;
            handVelocity += handAcceleration * delta;
            if ((handTarget - handPosition) * direction < 0.01f * direction) {
                handPosition = handTarget;
                handVelocity = 0.0f;
                handAcceleration = 0.0f;
                lastHandMoveTime = -1L;
            } else {
                lastHandMoveTime = System.currentTimeMillis();
            }
            parentView.invalidate();
        } else {
            lastHandMoveTime = System.currentTimeMillis();
            moveHand();
        }
    }
}
