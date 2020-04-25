package com.keepon.eventdispatch;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {
    //修改
    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint.setTextSize(40);
        paint.setColor(Color.WHITE);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("TAG", "MyView dispatchTouchEvent-- action=" + event.getAction());
       boolean dispatchTouchEvent = super.dispatchTouchEvent(event);
       Log.e("TAG", "MyView dispatchTouchEvent-- return =" + dispatchTouchEvent);
//        return dispatchTouchEvent;
        if(event.getAction()== MotionEvent.ACTION_DOWN){
//           getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }else{
            return false;
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "MyView onTouchEvent-- action=" + event.getAction());
        return super.onTouchEvent(event);
    }

    Paint paint = new Paint();
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText("MyView",getMeasuredWidth()/2-50,50,paint);
    }
}
