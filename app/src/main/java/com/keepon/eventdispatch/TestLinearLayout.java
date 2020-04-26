package com.keepon.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import static com.keepon.eventdispatch.ListenerActivity.TAG;

public class TestLinearLayout extends LinearLayout {
    public TestLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //test3
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        Log.w(TAG, "TestLinearLayout onInterceptTouchEvent-- action=" + Util.getActioString(event));
        // if(event.getAction()==MotionEvent.ACTION_DOWN){
        //     return  false;
        // }
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.w(TAG, "TestLinearLayout dispatchTouchEvent-- action=" + Util.getActioString(event));
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w(TAG, "TestLinearLayout onTouchEvent-- action=" +Util.getActioString(event));
        return super.onTouchEvent(event);
    }



}
