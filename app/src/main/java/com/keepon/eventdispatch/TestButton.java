package com.keepon.eventdispatch;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import static com.keepon.eventdispatch.ListenerActivity.TAG;

public class TestButton extends android.support.v7.widget.AppCompatButton {
    public TestButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        boolean dispatchTouchEvent = false;
        Log.d(TAG, "TestButton dispatchTouchEvent:" +Util.getActioString(event));
//        父容器不拦截的话,不会发出cancel事件,所以如果父容器不拦截，这里不是收到Actino_Move
//         if(event.getAction()==MotionEvent.ACTION_DOWN){
//             return true;
//         }else{
//             return true;
//         }
//        Log.e("TAG", "TestButton dispatchTouchEvent-- action=" + event.getAction());
////        getParent().requestDisallowInterceptTouchEvent(true);
        dispatchTouchEvent = super.dispatchTouchEvent(event);
////        Log.e("TAG", "TestButton dispatchTouchEvent-- return =" + dispatchTouchEvent);
       return dispatchTouchEvent;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean onTouchEvent = false;
        Log.d(TAG, "TestButton onTouchEvent-- action=" +Util.getActioString(event));
         onTouchEvent = super.onTouchEvent(event);
        return  onTouchEvent;
    }

}
