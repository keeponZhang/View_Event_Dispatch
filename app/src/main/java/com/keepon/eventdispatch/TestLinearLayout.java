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
        Log.w(TAG, "TestLinearLayout onInterceptTouchEvent-- action=" + Util.getActioString(event)+
                getSuperBeforeString());
        onInterceptTouchEvent = super.onInterceptTouchEvent(event);
        // if (event.getAction() == MotionEvent.ACTION_DOWN ) {
        //     onInterceptTouchEvent = false;
        // } else {
        //     onInterceptTouchEvent = true;
        // }
        return onInterceptTouchEvent;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean dispatchTouchEvent = false;
        Log.w(TAG, "TestLinearLayout dispatchTouchEvent-- action=" + Util.getActioString(event)+
                getSuperBeforeString());
        // if(event.getAction() ==MotionEvent.ACTION_MOVE){
        //     dispatchTouchEvent = true;
        // }else{
        //     dispatchTouchEvent = super.dispatchTouchEvent(event);
        // }
        dispatchTouchEvent = super.dispatchTouchEvent(event);

        Log.e(TAG, "TestLinearLayout dispatchTouchEvent-- action=" + Util.getActioString(event) +
                "  返回dispatchTouchEvent=" + dispatchTouchEvent);
        return dispatchTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = false;
        Log.w(TAG, "TestLinearLayout onTouchEvent-- action=" + Util.getActioString(event)+
                getSuperBeforeString());
        onTouchEvent = super.onTouchEvent(event);
        // onTouchEvent = true;
        Log.e(TAG, "TestLinearLayout onTouchEvent-- action=" + Util.getActioString(event) + "  " +
                "返回onTouchEvent=" + onTouchEvent);
        return onTouchEvent;
    }
    public String getSuperBeforeString(){
        return "----------调用super前";
    }

}
