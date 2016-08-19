package com.suncheng.myapplication;

import android.os.Bundle;
import android.view.View;

import com.suncheng.myapplication.framework.BaseActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setTitle("演示");
        setContentView(R.layout.activity_main);
        setBackListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        initView();
    }

    private void initView() {

    }

    @Override
    public void onClick(View v) {

    }
}
