package com.suncheng.myapplication.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.suncheng.myapplication.R;
import com.suncheng.myapplication.dialog.ButtonItem;
import com.suncheng.myapplication.dialog.DialogManager;
import com.suncheng.myapplication.framework.BaseActivity;
import com.suncheng.myapplication.photo.CropHandler;
import com.suncheng.myapplication.photo.CropHelper;
import com.suncheng.myapplication.photo.CropParams;
import com.suncheng.myapplication.utils.BitmapUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements View.OnClickListener, CropHandler {
    CropParams mCropParams;
    ImageView mImageView;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mCropParams = new CropParams(this);
        initView();
    }

    private void initView() {
        setTitle("修图");
        mImageView = (ImageView) findViewById(R.id.image);
        findViewById(R.id.open).setOnClickListener(this);
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.open:
                showPhotoMenu();
                break;
        }
    }

    private void showPhotoMenu() {
        ButtonItem takePhoto = new ButtonItem("拍照", new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                mCropParams.enable = false;
                mCropParams.compress = true;
                Intent intent = CropHelper.buildCameraIntent(mCropParams);
                startActivityForResult(intent, CropHelper.REQUEST_CAMERA);
            }
        });

        ButtonItem selectPhote = new ButtonItem("相册", new ButtonItem.OnClickListener() {
            @Override
            public void onClick() {
                mCropParams.enable = false;
                mCropParams.compress = true;
                Intent intent = CropHelper.buildGalleryIntent(mCropParams);
                startActivityForResult(intent, CropHelper.REQUEST_PICK);
            }
        });

        List<ButtonItem> list = new ArrayList<>();
        list.add(takePhoto);
        list.add(selectPhote);

        DialogManager.newInstance(this).showMenuPopupDialog(list, "取消");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CropHelper.handleResult(this, requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        CropHelper.clearCacheDir();
        super.onDestroy();
    }

    @Override
    public void onPhotoCropped(Uri uri) {
        if (!mCropParams.compress)
            mImageView.setImageBitmap(BitmapUtil.decodeUriAsBitmap(this, uri));
    }

    @Override
    public void onCompressed(Uri uri) {
        mImageView.setImageBitmap(BitmapUtil.decodeUriAsBitmap(this, uri));
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onFailed(String message) {

    }

    @Override
    public void handleIntent(Intent intent, int requestCode) {
        startActivityForResult(intent, requestCode);
    }

    @Override
    public CropParams getCropParams() {
        return mCropParams;
    }
}
