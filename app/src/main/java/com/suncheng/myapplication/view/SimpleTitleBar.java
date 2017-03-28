package com.suncheng.myapplication.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.suncheng.myapplication.R;
import com.suncheng.myapplication.utils.BlankUtil;
import com.suncheng.myapplication.utils.StatusBarUtils;

/**
 * 统一的标题栏
 * Created by yulinye on 2016/7/19.
 */
public class SimpleTitleBar extends RelativeLayout {
    private int mTextColor;
    private String mTitleText;
    private boolean mTransparent = false;
    private boolean mBottomShadowShow; //显示底部阴影，仅用于白色的背景才要，听设计的

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
        initAttr(attrs);
        mContext = context;
        init();
    }

    public void initAttr(AttributeSet attrs){
        if (getBackground() == null) {
            setBackgroundResource(R.color.titleBar);
        }
        TypedArray t = getContext().obtainStyledAttributes(attrs, R.styleable.SimpleTitleBar);
        mTextColor = t.getColor(R.styleable.SimpleTitleBar_title_text_color, getResources().getColor(R.color.white));
        mTitleText = t.getString(R.styleable.SimpleTitleBar_title_text);
        mTransparent = t.getBoolean(R.styleable.SimpleTitleBar_style_transparent, false);
        mBottomShadowShow = t.getBoolean(R.styleable.SimpleTitleBar_bottom_shadow_show, true);
    }

    private void init() {
        View view = View.inflate(mContext, R.layout.layout_simple_title_center, null);
        setCenterView(view);
        TextView tv = (TextView) findViewById(R.id.simple_title_center_text);
        tv.setTextColor(mTextColor);
        if (!BlankUtil.isBlank(mTitleText)) {
            tv.setText(mTitleText);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getVisibility() == VISIBLE) {
            StatusBarUtils.setWindowStatusBarColor((Activity)mContext, R.color.titleBar);
        }
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
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
            LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
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
        tv.setTextColor(mTextColor);
        tv.setText(title);
    }

    public void setTitleColor(int color){
        mTextColor = color;
    }

    /**
     * 显示back按钮，同时设置点击事件
     * @param listner
     */
    public void setBackListner(OnClickListener listner) {
        View view = View.inflate(mContext, R.layout.layout_title_back, null);
        view.setOnClickListener(listner);
        setLeftView(view);
    }

}
