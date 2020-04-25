package com.keepon.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

public class TestLinearLayout extends LinearLayout {
    public TestLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //test3
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        Log.w("TAG", "TestLinearLayout onInterceptTouchEvent-- action=" + ev.getAction());
        if(ev.getAction()==MotionEvent.ACTION_DOWN){
            return  false;
        }
        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.w("TAG", "TestLinearLayout dispatchTouchEvent-- action=" + event.getAction());
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w("TAG", "TestLinearLayout onTouchEvent-- action=" + event.getAction());

        return true;
    }

}
