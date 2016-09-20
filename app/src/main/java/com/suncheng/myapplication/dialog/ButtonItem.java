package com.suncheng.myapplication.dialog;

import com.suncheng.myapplication.R;

/**
 *
 * created by yulinye 2016/07/06
 */
public class ButtonItem{

	public String mText;
	public int resourceID;
	public OnClickListener mClickListener;

	public ButtonItem(String text, OnClickListener l){
		mText = text;
		mClickListener = l;
		resourceID = R.layout.layout_common_popup_dialog_button;
	}

    public void setText(String text){
        this.mText = text;
    };

	public interface OnClickListener{
		void onClick();
	}
}