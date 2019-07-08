package com.avraam.smartlist.models;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;

public class RectOverlay extends GraphicOverlay.Graphic{

    private static final int RECT_COLOR = Color.YELLOW;
    private static final float STROKE_WIDTH = 8.0f;
    private final Paint rectPaint;
    private final Rect bounds;

    public RectOverlay(GraphicOverlay overlay , Rect bounds) {
        super(overlay);
        this.bounds = bounds;
        this.rectPaint = new Paint();
        this.rectPaint.setColor(RECT_COLOR);
        this.rectPaint.setStyle(Paint.Style.STROKE);
        this.rectPaint.setStrokeWidth(STROKE_WIDTH);
        postInvalidate();
    }

    @Override
    public void draw(Canvas canvas) {

        if(bounds == null)
        {
            throw new IllegalStateException("Attempting to draw a null bounds");

        }
        RectF rectF = new RectF(bounds);
        canvas.drawRect(rectF, rectPaint);

    }
}
