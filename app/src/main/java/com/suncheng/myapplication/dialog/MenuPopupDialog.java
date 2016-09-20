package com.suncheng.myapplication.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.gx.tjsq.R;
import com.tj.framework.util.BlankUtil;

import java.util.List;

/**
 * 底部弹出菜单对话框
 * created by yulinye 2016/07/06
 */
class MenuPopupDialog extends Dialog implements OnClickListener {

	private ViewGroup mRootView;
	protected ViewGroup mContentView;
	private TextView mMessageTv;
	private TextView mCancelBtn;

    public MenuPopupDialog(Context context, String title, List<ButtonItem> buttons, String cancelBtnText){
        this(context, title, buttons, new ButtonItem(cancelBtnText, null));
    }

	public MenuPopupDialog(Context context, String title, List<ButtonItem> buttons, final ButtonItem cancelBtnItem) {
		super(context, R.style.Dialog_Fullscreen);

        mRootView = (ViewGroup) View.inflate(getContext(), R.layout.layout_common_popup_dialog, null);
        mContentView = (ViewGroup) mRootView.findViewById(R.id.ll_more);
		mMessageTv = (TextView) mRootView.findViewById(R.id.tv_message);
		mCancelBtn = (TextView) mRootView.findViewById(R.id.btn_cancel);
		mCancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
                if(cancelBtnItem!=null && cancelBtnItem.mClickListener!=null){
                    cancelBtnItem.mClickListener.onClick();
                }
				dismiss();
			}
		});

		setContentView(mRootView);

		Window window = getWindow();
		WindowManager.LayoutParams params = window.getAttributes();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		window.setGravity( Gravity.BOTTOM );
		window.setAttributes(params);
		window.setWindowAnimations(R.style.PopupDialogAnimation);

        if(buttons!=null && buttons.size()>0){
            if(!BlankUtil.isBlank(title)){
                setMessage(title);
            }
            mContentView.setVisibility(View.VISIBLE);
            for(int i =0 ;i < buttons.size() ; i++){
                if(i == 0 ) {
                    if(!BlankUtil.isBlank(title))
                        addDivider();
                }
                else
                    addDivider();
                addItem(buttons.get(i));
            }
        }
		if(cancelBtnItem != null && cancelBtnItem.mText!=null && !cancelBtnItem.mText.isEmpty()){
			mCancelBtn.setVisibility(View.VISIBLE);
			mCancelBtn.setText(cancelBtnItem.mText);
		}
	}

	public MenuPopupDialog setMessage(String text) {
		mMessageTv.setVisibility(View.VISIBLE);
		mMessageTv.setText(text);
		return this;
	}

	public void addItem(final ButtonItem buttonItem){
        TextView item = (TextView) LayoutInflater.from(getContext()).inflate(buttonItem.resourceID, mContentView, false);
		item.setText(buttonItem.mText);
		item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				buttonItem.mClickListener.onClick();
				dismiss();
			}
		});
		mContentView.addView(item, mContentView.getChildCount());
	}

    public void addDivider(){
        View v2 = LayoutInflater.from(getContext()).inflate(R.layout.layout_common_popup_dialog_divider, mContentView, false);
        mContentView.addView(v2, mContentView.getChildCount());
    }

	@Override
	public void onClick(View v) {
	}

}
