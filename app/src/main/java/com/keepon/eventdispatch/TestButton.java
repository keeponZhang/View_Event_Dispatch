package com.keepon.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

public class TestButton extends android.support.v7.widget.AppCompatButton {

    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("TAG", "TestButton dispatchTouchEvent-- action=" + event.getAction());
//        getParent().requestDisallowInterceptTouchEvent(true);
        boolean dispatchTouchEvent = super.dispatchTouchEvent(event);
//        Log.e("TAG", "TestButton dispatchTouchEvent-- return =" + dispatchTouchEvent);
        return dispatchTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", "TestButton onTouchEvent-- action=" + event.getAction());
        boolean onTouchEvent = super.onTouchEvent(event);
        return  onTouchEvent;
    }

}
