package com.keepon.eventdispatch;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


// http://minjie.tech/2016/04/26/Binder%E7%9A%84%E7%9B%B8%E5%85%B3%E7%9F%A5%E8%AF%86%E7%82%B9/
// https://www.jianshu.com/p/e99b5e8bd67b

//https://blog.csdn.net/guolin_blog/article/details/9097463
//https://blog.csdn.net/guolin_blog/article/details/9153747
public class ListenerActivity extends Activity implements View.OnTouchListener, View.OnClickListener {
    //修改
    private LinearLayout mLayout;
    private TestButton   mButton;
    private MyView       mMyView;
    private TextView     mTextView;
    public static final String TAG = "事件拦截";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.listener);

        mLayout = (LinearLayout) this.findViewById(R.id.mylayout);
        mMyView = (MyView) this.findViewById(R.id.myview);
        mButton = (TestButton) this.findViewById(R.id.my_btn);
        mTextView = (TextView) this.findViewById(R.id.tv);
        View decorView = getWindow().getDecorView();
        FrameLayout content = (FrameLayout) this.findViewById(android.R.id.content );


        content.setTag("AndroidContent");
        decorView.setTag("DevorView");

        content.setOnTouchListener(this);
//        mLayout.setOnTouchListener(this);
//        mTextView.setOnTouchListener(this);
//        mMyView.setOnTouchListener(this);
//        mButton.setOnTouchListener(this);

//        mButton.getParent().requestDisallowInterceptTouchEvent(false);
        getWindow().getDecorView().setOnTouchListener(this);
//        mButton.setOnTouchListener(this);

//        mLayout.setOnClickListener(this);
       mButton.setOnClickListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getTag()!=null){
            Log.e(TAG, v.getTag()+" OnTouchListener--onTouch-- action="+Util.getActioString(event));
        }else{
            Log.e(TAG, this+" OnTouchListener--onTouch-- action="+Util.getActioString(event));
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Log.e(TAG, v.getTag()+"  OnClickListener--onClick--");
        Toast.makeText(this,"click", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.e("TAG", " Activity dispatchTouchEvent "+Util.getActioString(event));
        boolean b = super.dispatchTouchEvent(event);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", " Activity onTouchEvent "+Util.getActioString(event));
        boolean b = super.onTouchEvent(event);
        return b;
    }
}
