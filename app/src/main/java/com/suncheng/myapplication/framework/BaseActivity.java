package com.suncheng.myapplication.framework;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import com.suncheng.myapplication.R;
import com.suncheng.myapplication.view.SimpleTitleBar;

/**
 * Created by suncheng on 2016/8/18.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private SimpleTitleBar mTitleBar;
    private FrameLayout mContentLayoutRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        window.setContentView(R.layout.activity_base);
        mTitleBar = (SimpleTitleBar) window.findViewById(R.id.titleBar);
        mContentLayoutRoot = (FrameLayout) window.findViewById(R.id.layout_activity_base_content_root);
        onActivityCreate(savedInstanceState);
    }

    protected abstract void onActivityCreate(Bundle savedInstanceState);

    @Override
    public void setTitle(CharSequence title) {
        mTitleBar.setTitle((String) title);
    }

    @Override
    public void setTitle(int titleId) {
        mTitleBar.setTitle((String) getResources().getText(titleId));
    }

    public void setBackListner(View.OnClickListener listner) {
        mTitleBar.setBackListner(listner);
    }

    public void setLeftView(View leftView) {
        mTitleBar.setLeftView(leftView);
    }

    public void setLeftLayout(@LayoutRes int leftLayout) {
        mTitleBar.setLeftLayout(leftLayout);
    }

    public void setRightView(View rightView) {
        mTitleBar.setRightView(rightView);
    }

    public void setRightLayout(@LayoutRes int rightLayout) {
        mTitleBar.setRightLayout(rightLayout);
    }

    public void setCenterView(View centerView) {
        mTitleBar.setCenterView(centerView);
    }

    public void setCenterLayout(@LayoutRes int centerLayout) {
        mTitleBar.setCenterLayout(centerLayout);
    }

    @SuppressWarnings("unchecked")
    public <T extends View> T findView(int id) {
        return (T) mContentLayoutRoot.findViewById(id);
    }

    @Override
    public View findViewById(int id) {
        return mContentLayoutRoot.findViewById(id);
    }

    private void clearContentView() {
        mContentLayoutRoot.removeAllViews();
    }

    @Override
    public void setContentView(int layoutResID) {
        clearContentView();
        getLayoutInflater().inflate(layoutResID, mContentLayoutRoot, true);
    }

    @Override
    public void setContentView(View view) {
        clearContentView();
        mContentLayoutRoot.addView(view);
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        clearContentView();
        mContentLayoutRoot.addView(view, params);
    }
}
