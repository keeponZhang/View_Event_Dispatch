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



public class ListenerActivity extends Activity implements View.OnTouchListener, View.OnClickListener {
    //修改
    private LinearLayout mLayout;
    private TestButton   mButton;
    private MyView       mMyView;
    private TextView     mTextView;

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

    private static final String TAG = "ListenerActivity";
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getTag()!=null){
            Log.e("TAG", v.getTag()+" OnTouchListener--onTouch-- action="+event.getAction()+" ");
        }else{
            Log.e("TAG", this+" OnTouchListener--onTouch-- action="+event.getAction()+" ");
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        Log.e("TAG", v.getTag()+"  OnClickListener--onClick--");
        Toast.makeText(this,"click", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.e("TAG", " Activity dispatchTouchEvent "+ev.getAction());
        boolean b = super.dispatchTouchEvent(ev);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e("TAG", " Activity onTouchEvent "+event.getAction());
        boolean b = super.onTouchEvent(event);
        return b;
    }
}
