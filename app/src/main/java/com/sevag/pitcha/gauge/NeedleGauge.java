package com.sevag.pitcha.gauge;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;

import com.sevag.pitcha.gauge.components.Hand;
import com.sevag.pitcha.gauge.components.Rim;
import com.sevag.pitcha.gauge.components.Scale;
import com.sevag.pitcha.gauge.components.Title;

public final class NeedleGauge extends View {
	// drawing tools
    private Rim rimComponent = new Rim();
    private Hand handComponent = new Hand(this);
    private Scale scaleComponent = new Scale();
    private Title titleComponent = new Title();

	private RectF rimRect = rimComponent.getRimRect();
	private Paint rimPaint = rimComponent.getRimPaint();
	private Paint rimCirclePaint = rimComponent.getRimCirclePaint();
    private float rimSize = rimComponent.getRimSize();

    private RectF scaleRect = scaleComponent.getScaleRect();
    private Paint scalePaint = scaleComponent.getScalePaint();
    private float scalePosition = scaleComponent.getScalePosition();

	private Paint titlePaint = titleComponent.getTitlePaint();
	private Path titlePath = titleComponent.getTitlePath();

	private Paint handPaint = handComponent.getHandPaint();
	private Path handPath = handComponent.getHandPath();
	private Paint handScrewPaint = handComponent.getHandScrewPaint();

    private RectF faceRect;

	private Paint backgroundPaint;
	// end drawing tools

	private Bitmap background; // holds the cached static part

	// hand dynamics -- all are angular expressed in F degrees


    private String titleString = "pitcha";

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

	@Override
	protected void onRestoreInstanceState(Parcelable state) {
		Bundle bundle = (Bundle) state;
		Parcelable superState = bundle.getParcelable("superState");
		super.onRestoreInstanceState(superState);

		handComponent.handInitialized = bundle.getBoolean("handInitialized");
        handComponent.handPosition = bundle.getFloat("handPosition");
        handComponent.handTarget = bundle.getFloat("handTarget");
        handComponent.handVelocity = bundle.getFloat("handVelocity");
        handComponent.handAcceleration = bundle.getFloat("handAcceleration");
        handComponent.lastHandMoveTime = bundle.getLong("lastHandMoveTime");
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Parcelable superState = super.onSaveInstanceState();

		Bundle state = new Bundle();
		state.putParcelable("superState", superState);
		state.putBoolean("handInitialized", handComponent.handInitialized);
		state.putFloat("handPosition", handComponent.handPosition);
		state.putFloat("handTarget", handComponent.handTarget);
		state.putFloat("handVelocity", handComponent.handVelocity);
		state.putFloat("handAcceleration", handComponent.handAcceleration);
		state.putLong("lastHandMoveTime", handComponent.lastHandMoveTime);
		return state;
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

	private void drawRim(Canvas canvas) {
		canvas.drawOval(rimRect, rimPaint);
		canvas.drawOval(rimRect, rimCirclePaint);
	}

    private void setTitleString(String titleArg) {
        this.titleString = titleArg;
    }

    private void drawTitle(Canvas canvas) {
        canvas.drawTextOnPath(this.titleString, titlePath, 0.0f,0.0f, titlePaint);
    }

    private void drawFace(Canvas canvas) {
		canvas.drawOval(faceRect, rimCirclePaint);
	}

	private void drawBackground(Canvas canvas) {
		if (background == null) {
		} else {
			canvas.drawBitmap(background, 0, 0, backgroundPaint);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		drawBackground(canvas);

		float scale = (float) getWidth();
		canvas.save(Canvas.MATRIX_SAVE_FLAG);
		canvas.scale(scale, scale);

		handComponent.drawHand(canvas);
		canvas.restore();

		if (handComponent.handNeedsToMove()) {
			handComponent.moveHand();
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		regenerateBackground();
	}

	private void regenerateBackground() {
		// free the old bitmap
		if (background != null) {
			background.recycle();
		}

		background = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
		Canvas backgroundCanvas = new Canvas(background);
		float scale = (float) getWidth();
		backgroundCanvas.scale(scale, scale);

		drawRim(backgroundCanvas);
		drawFace(backgroundCanvas);
		scaleComponent.drawScale(backgroundCanvas);
        drawTitle(backgroundCanvas);
	}

	public void setHandTarget(String note, double pitch) {
		if (pitch < Scale.MIN_VALUE) {
            pitch = Scale.MIN_VALUE;
		} else if (pitch > Scale.MAX_VALUE) {
            pitch = Scale.MAX_VALUE;
		}
        handComponent.handTarget = (float) pitch;
		handComponent.handInitialized = true;
        setTitleString(note);
		invalidate();
	}
}
