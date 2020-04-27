package com.keepon.eventdispatch;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
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

        // content.setOnTouchListener(this);
//        mLayout.setOnTouchListener(this);
//        mTextView.setOnTouchListener(this);
//        mMyView.setOnTouchListener(this);
//        mButton.setOnTouchListener(this);

//        mButton.getParent().requestDisallowInterceptTouchEvent(false);
//         getWindow().getDecorView().setOnTouchListener(this);
        // getWindow().setCallback(getCallback());
//        mButton.setOnTouchListener(this);

//        mLayout.setOnClickListener(this);
       mButton.setOnClickListener(this);
        mButton.setTag("TestButtom");
    }

    private Window.Callback getCallback() {
        return new Window.Callback() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent event) {
                return false;
            }

            @Override
            public boolean dispatchKeyShortcutEvent(KeyEvent event) {
                return false;
            }

            @Override
            public boolean dispatchTouchEvent(MotionEvent event) {
                Log.e(TAG, "getWindow().setCallback dispatchTouchEvent:");
                return false;
            }

            @Override
            public boolean dispatchTrackballEvent(MotionEvent event) {
                return false;
            }

            @Override
            public boolean dispatchGenericMotionEvent(MotionEvent event) {
                return false;
            }

            @Override
            public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
                return false;
            }

            @Nullable
            @Override
            public View onCreatePanelView(int featureId) {
                return null;
            }

            @Override
            public boolean onCreatePanelMenu(int featureId, Menu menu) {
                return false;
            }

            @Override
            public boolean onPreparePanel(int featureId, View view, Menu menu) {
                return false;
            }

            @Override
            public boolean onMenuOpened(int featureId, Menu menu) {
                return false;
            }

            @Override
            public boolean onMenuItemSelected(int featureId, MenuItem item) {
                return false;
            }

            @Override
            public void onWindowAttributesChanged(WindowManager.LayoutParams attrs) {

            }

            @Override
            public void onContentChanged() {

            }

            @Override
            public void onWindowFocusChanged(boolean hasFocus) {

            }

            @Override
            public void onAttachedToWindow() {

            }

            @Override
            public void onDetachedFromWindow() {

            }

            @Override
            public void onPanelClosed(int featureId, Menu menu) {

            }

            @Override
            public boolean onSearchRequested() {
                return false;
            }

            @Nullable
            @Override
            public ActionMode onWindowStartingActionMode(ActionMode.Callback callback) {
                return null;
            }

            @Override
            public void onActionModeStarted(ActionMode mode) {

            }

            @Override
            public void onActionModeFinished(ActionMode mode) {

            }
        };
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(v.getTag()!=null){
            Log.e(TAG, v.getTag()+" OnTouchListener--onTouch-- action="+Util.getActioString(event));
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if(v.getTag()!=null){
            Log.e(TAG, v.getTag()+" OnClickListener--onClick-- action=");
        }
        Toast.makeText(this,"click", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(TAG, " Activity dispatchTouchEvent "+Util.getActioString(event));
        if(event.getAction() ==MotionEvent.ACTION_UP){
            Log.w("TAG", "ListenerActivity dispatchTouchEvent:");
        }
        boolean b = super.dispatchTouchEvent(event);
        return b;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG, " Activity onTouchEvent "+Util.getActioString(event));
        boolean b = super.onTouchEvent(event);
        return b;
    }
}
