package com.example.yee;

import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class BaseActivity extends AppCompatActivity {
    //改变状态栏颜色
    public void setThisStatusBarColor() {
        getWindow().getDecorView().addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                initStatusBar();
                getWindow().getDecorView().removeOnLayoutChangeListener(this);
            }
        });

    }

    private void initStatusBar() {
        //利用反射机制修改状态栏背景
        int identifier = getResources().getIdentifier("statusBarBackground", "id", "android");
        View statusBarView = getWindow().findViewById(identifier);
        statusBarView.setBackgroundResource(R.drawable.status_bar_gradient_bg);
    }

}
