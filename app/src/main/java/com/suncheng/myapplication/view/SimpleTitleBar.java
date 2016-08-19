package com.suncheng.myapplication.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suncheng.myapplication.R;

public class SimpleTitleBar extends RelativeLayout {

    private View mLeftView;
    private View mRightView;
    private View mCenterView;

    private Context mContext;

    public SimpleTitleBar(Context context) {
        this(context, null);
    }

    public SimpleTitleBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public SimpleTitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (getBackground() == null) {
            setBackgroundResource(R.color.colorPrimary);
        }
        mContext = context;
        init();

    }

    private void init() {
        View view = View.inflate(mContext, R.layout.layout_simple_title_center, null);
        setCenterView(view);
    }

    /**
     * 左部的View
     * @param leftView
     */
    public void setLeftView(View leftView) {
        if (this.mLeftView != null)
            removeView(this.mLeftView);
        this.mLeftView = leftView;
        if (mLeftView != null) {
            RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(CENTER_VERTICAL);
            params.addRule(ALIGN_PARENT_LEFT);
            addView(this.mLeftView, params);
        }
    }

    public void setLeftLayout(@LayoutRes int leftLayout) {
        if (leftLayout > 0) {
            setLeftView(View.inflate(mContext, leftLayout, null));
        }
    }

    /**
     * 右边的view
     * @param rightView
     */
    public void setRightView(View rightView) {
        if (this.mRightView != null)
            removeView(this.mRightView);
        this.mRightView = rightView;
        if (mRightView != null) {
            RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
            params.addRule(CENTER_VERTICAL);
            params.addRule(ALIGN_PARENT_RIGHT);
            addView(this.mRightView, params);
        }
    }

    public void setRightLayout(@LayoutRes int rightLayout) {
        if (rightLayout > 0) {
            setRightView(View.inflate(mContext, rightLayout, null));
        }
    }

    /**
     * 中间的view
     * @param centerView
     */
    public void setCenterView(View centerView) {
        if (centerView == null)
            return;
        if (this.mCenterView != null)
            removeView(this.mCenterView);
        this.mCenterView = centerView;
        RelativeLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params.addRule(CENTER_IN_PARENT);
        addView(this.mCenterView, params);
    }

    public void setCenterLayout(@LayoutRes int centerLayout) {
        if (centerLayout > 0) {
            setCenterView(View.inflate(mContext, centerLayout, null));
        }
    }

    public void setTitle(String title){
        TextView tv = (TextView) mCenterView.findViewById(R.id.simple_title_center_text);
        tv.setText(title);
    }

    /**
     * 显示back按钮，同时设置点击事件
     * @param listner
     */
    public void setBackListner(View.OnClickListener listner) {
        View view = View.inflate(mContext, R.layout.layout_title_back, null);
        view.setOnClickListener(listner);
        setLeftView(view);
    }

}
