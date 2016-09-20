package com.suncheng.myapplication.dialog;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gx.tjsq.R;
import com.gx.tjsq.share.IExtraOperation;
import com.gx.tjsq.share.ShareModel;
import com.gx.tjsq.util.StringUtils;
import com.tj.framework.util.BlankUtil;

import java.util.List;

import cn.sharesdk.framework.PlatformActionListener;

/**
 * 整个项目统一的对话框调用, 这是基础控件，请不要添加业务代码
 * Created by yulinye on 2016/7/5.
 */
public class DialogManager {

    private Dialog mDialog;
    private Context mContext;
    private AlertDialog.Builder mBuilder;
    private boolean mCanceledOnClickBackKey = true;
    private boolean mCanceledOnClickOutside = true;

    private DialogManager(Context context) {
        mContext = context;
        mBuilder = new AlertDialog.Builder(context);
        mDialog = mBuilder.create();
    }

    public static DialogManager newInstance(Context context) {
        return new DialogManager(context);
    }

    @TargetApi(17)
    public boolean checkActivityValid() {
        if (mContext == null) {
            return false;
        }
        if (mDialog != null && mDialog.getWindow() == null) {
            return false;
        }
        if (((Activity) mContext).isFinishing()) {
            return false;
        }

        if (Build.VERSION.SDK_INT >= 17 && ((Activity) mContext).isDestroyed()) {
            return false;
        }
        return true;
    }

    /**
     * 关闭对话框
     */
    public void dismissDialog() {
        if (mContext != null && mDialog != null && mDialog.getWindow() != null) {
            if (mContext instanceof Activity) {
                Activity activity = (Activity) mContext;
                if (!activity.isFinishing())//如果dialog在延时比如handler。postDelay中调用,而activity.已经destory,会报异常java.lang.IllegalArgumentException: View not attached to window manager
                    mDialog.dismiss();
            } else
                mDialog.dismiss();
        }
    }

    public boolean isDialogShowing() {
        if (mDialog != null) {
            return mDialog.isShowing();
        }
        return false;
    }

    public void setCanceledOnClickBackKey(boolean cancelable) {
        mCanceledOnClickBackKey = cancelable;
    }

    public void setCanceledOnClickOutside(boolean cancelable) {
        mCanceledOnClickOutside = cancelable;
    }

    /**
     * 确认对话框
     */
    public void showOkDialog(@NonNull String content, @NonNull String okBtnText, @Nullable final OnDialogOkListener listener) {
        if (!checkActivityValid()) {
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_ok);

        TextView tip = (TextView) window.findViewById(R.id.title);
        tip.setText(content);

        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText(okBtnText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (listener != null) {
                    listener.onOk();
                }
            }
        });

    }

    /**
     * 确认取消对话框
     */
    public void showOkCancelDialog(@NonNull String title, String okBtnText, final OnDialogOkListener listener) {
        showOkCancelDialog(title, null, okBtnText, null, listener);
    }
    public void showOkCancelDialog(@NonNull String title, @Nullable String content, String okBtnText, String cancelBtnText,final OnDialogOkListener listener) {
        if (!checkActivityValid()) {
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_ok_cancel);

        TextView tip = (TextView) window.findViewById(R.id.title);
        tip.setText(title);

        if (!BlankUtil.isBlank(content)) {
            TextView contentTv = (TextView) window.findViewById(R.id.content);
            contentTv.setVisibility(View.VISIBLE);
            contentTv.setText(content);
        }

        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        ok.setText(okBtnText);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (listener != null) {
                    listener.onOk();
                }
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        if(!StringUtils.isEmpty(cancelBtnText)) {
            cancel.setText(cancelBtnText);
        }
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
    }

    /**
     * 带有输入框的对话框
     * @param title 标题
     * @param text  原有内容
     * @param maxNum 最大限度的字符个数
     * @param okLabel 确认按钮的文字
     * @param okLabelColor 传0则为默认
     * @param cancelable 对话框是否cancelable
     * @param listener
     */
    public void showEditableOkCancelDialog(@Nullable String title, @Nullable String text, final int maxNum, String okLabel, int okLabelColor, boolean cancelable, final OnEditOkListener listener) {

        if (!checkActivityValid()) {
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = mBuilder.create();

        mDialog.setCancelable(cancelable);
        mDialog.setCanceledOnTouchOutside(cancelable);
        mDialog.show();
        Window window = mDialog.getWindow();
        window.setContentView(R.layout.dialog_with_edittext);

        TextView tip = (TextView) window.findViewById(R.id.title);
        tip.setText(title);

        final TextView txNum = (TextView) window.findViewById(R.id.textNum);
        if (!BlankUtil.isBlank(text)) {
            txNum.setText(text.length() + "");
        }
        final TextView txMaxNum = (TextView) window.findViewById(R.id.textMaxNum);
        txMaxNum.setText(maxNum + "");

        final EditText editText = (EditText) window.findViewById(R.id.editText);
        if (!BlankUtil.isBlank(text)) {
            editText.setText(text);
        }
        editText.requestFocus();

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Editable editable = editText.getText();
                int len = editable.length();

                if (len > maxNum) {
                    int selEndIndex = Selection.getSelectionEnd(editable);
                    String str = editable.toString();
                    //截取新字符串
                    String newStr = str.substring(0, maxNum);
                    editText.setText(newStr);
                    editable = editText.getText();

                    //新字符串的长度
                    int newLen = editable.length();
                    //旧光标位置超过字符串长度
                    if (selEndIndex > newLen) {
                        selEndIndex = editable.length();
                    }
                    //设置新光标所在的位置
                    Selection.setSelection(editable, selEndIndex);
                }
                txNum.setText(editable.length() + "");
            }
        });

        //只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        //加上下面这一行弹出对话框时软键盘随之弹出
//        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        TextView ok = (TextView) window.findViewById(R.id.btn_ok);
        if (okLabelColor != 0)
            ok.setTextColor(okLabelColor);
        ok.setText(okLabel);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (listener != null) {
                    listener.onEditOk(editText.getText().toString());
                }
            }
        });

        TextView cancel = (TextView) window.findViewById(R.id.btn_cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (listener != null) {
                    listener.onCancel();
                }
            }
        });
    }

    /**
     * 从窗口底部向上弹出的菜单列表
     * @param btnItems
     * @param cancelBtn
     */
    public void showMenuPopupDialog(List<ButtonItem> btnItems, ButtonItem cancelBtn) {

        if (!checkActivityValid()) {
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = new MenuPopupDialog(mContext, null, btnItems, cancelBtn);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
        mDialog.show();
    }

    public void showMenuPopupDialog(List<ButtonItem> btnItems, String cancelText) {

        if (!checkActivityValid()) {
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }
        mDialog = new MenuPopupDialog(mContext, null, btnItems, cancelText);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
        mDialog.show();
    }

    /**
     * 分享入口
     * @param shareModel
     * @param platformActionListener
     */
    public void showShareDialog(ShareModel shareModel, PlatformActionListener platformActionListener) {
        showShareDialog(shareModel, platformActionListener, null);
    }
    public void showShareDialog(ShareModel shareModel, PlatformActionListener platformActionListener, IExtraOperation operation) {
        if (!checkActivityValid()) {
            return;
        }

        if (mDialog.isShowing()) {
            mDialog.hide();
        }

        mDialog = new SharePopupDialog(mContext, shareModel, platformActionListener, operation);
        mDialog.setCancelable(mCanceledOnClickBackKey);
        mDialog.setCanceledOnTouchOutside(mCanceledOnClickOutside);
        mDialog.show();
    }


    public interface OnEditOkListener {
        void onEditOk(String text);
        void onCancel();
    }

    public interface OnDialogOkListener {
        void onOk();
        void onCancel();
    }

}
