package com.keepon.eventdispatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static com.keepon.eventdispatch.ListenerActivity.TAG;

public class MyView extends View {
    //修改
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean dispatchTouchEvent = false;
        Log.e(TAG, "MyView dispatchTouchEvent-- action=" + Util.getActioString(event));
        dispatchTouchEvent = super.dispatchTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            //让父控件不要拦截
            getParent().requestDisallowInterceptTouchEvent(true);
            dispatchTouchEvent = true;
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            getParent().requestDisallowInterceptTouchEvent(false);
            dispatchTouchEvent = false;
        } else {
            dispatchTouchEvent = true;
        }
        return dispatchTouchEvent;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = false;
        Log.e(TAG, "MyView onTouchEvent-- action=" + Util.getActioString(event));
        onTouchEvent = super.onTouchEvent(event);
        return onTouchEvent;
    }

    Paint paint = new Paint();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawText("MyView", getMeasuredWidth() / 2 - 50, 50, paint);
    }
}
