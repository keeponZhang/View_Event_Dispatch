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
        boolean onInterceptTouchEvent = false;
        Log.w(TAG, "TestLinearLayout onInterceptTouchEvent-- action=" + Util.getActioString(event));
        // onInterceptTouchEvent = super.onInterceptTouchEvent(event);
        onInterceptTouchEvent = true;
        return onInterceptTouchEvent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean dispatchTouchEvent = false;
        Log.w(TAG, "TestLinearLayout dispatchTouchEvent-- action=" + Util.getActioString(event));
        dispatchTouchEvent = super.dispatchTouchEvent(event);
        Log.w(TAG, "TestLinearLayout dispatchTouchEvent-- action=" + Util.getActioString(event)+
                "  返回dispatchTouchEvent="+dispatchTouchEvent);
        return dispatchTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = false;
        Log.w(TAG, "TestLinearLayout onTouchEvent-- action=" +Util.getActioString(event));
        onTouchEvent = super.onTouchEvent(event);
        Log.w(TAG, "TestLinearLayout onTouchEvent-- action=" +Util.getActioString(event)+"  " +
                "返回onTouchEvent="+onTouchEvent);
        return onTouchEvent;
    }



}
