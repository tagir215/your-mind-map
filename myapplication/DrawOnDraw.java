package com.example.myapplication;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

public class DrawOnDraw extends View {
    public DrawOnDraw(Context context) {
        super(context);
    }
    public DrawOnDraw(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }
    public DrawOnDraw(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    Paint paintBox;
    Paint paintBorders;
    boolean called;
    boolean clear;
    int x,y,h,w;
    void constructMe()
    {
        paintBox = new Paint();
        paintBox.setColor(Color.BLUE);
        paintBox.setStrokeWidth(5f);
        paintBox.setAlpha(50);
        paintBorders = new Paint();
        paintBorders.setColor(Color.BLUE);
        paintBorders.setStrokeWidth(1f);
        paintBorders.setStyle(Paint.Style.STROKE);
    }
    void drawBox(int xx,int yy,int ww,int hh)
    {
        called = true;
        x = xx;
        y = yy;
        w = ww;
        h = hh;
        invalidate();
    }
    void clearCanvas()
    {
        clear = true;
        invalidate();
    }
    @Override
    protected void onDraw(Canvas canvas) {
        if(clear) {
            canvas.drawLine(0, 0, 0, 0, paintBorders);
            clear = false;
            called = false;
        }
        if(!called)
            return;

        canvas.drawRect(x,y,x+w,y+h,paintBox);
        canvas.drawRect(x,y,x+w,y+h,paintBorders);
        called = false;
        super.onDraw(canvas);
    }
}
