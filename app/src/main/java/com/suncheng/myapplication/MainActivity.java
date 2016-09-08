package com.suncheng.myapplication;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.suncheng.myapplication.adapter.MasonryAdapter;
import com.suncheng.myapplication.framework.BaseActivity;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.controller.BaseController;
import com.suncheng.myapplication.net.controller.JsoupController;
import com.suncheng.myapplication.utils.BlankUtil;
import com.suncheng.myapplication.utils.NetworkUtils;
import com.suncheng.myapplication.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private PullToRefreshRecyclerView mListView;
    private MasonryAdapter mAdapter;
    private List<Article> mArticles;
    private JsoupController mJsoupController;
    private int currentPage = 1;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setTitle("美拍");
        setContentView(R.layout.activity_main);
        setBackListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applicationFinish();
            }
        });
        initView();
    }

    private void initView() {
        mListView = (PullToRefreshRecyclerView) findViewById(R.id.news_list);
        mListView.getRefreshableView().setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mListView.getRefreshableView().addItemDecoration(decoration);
        mAdapter = new MasonryAdapter();
        mListView.setAdapter(mAdapter);
        mArticles = new ArrayList<>();
        mJsoupController = new JsoupController();
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if(NetworkUtils.isNetworkStrictlyAvailable(MainActivity.this)) {
                    currentPage = 1;
                    mJsoupController.getArticleList(new ArticlePullDownListCallback(), currentPage);
                }else {
                    mListView.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if(NetworkUtils.isNetworkStrictlyAvailable(MainActivity.this)){
                    currentPage++;
                    mJsoupController.getArticleList(new ArticlePullUpListCallback(), currentPage);
                }else {
                    mListView.onRefreshComplete();
                }

            }
        });
        mJsoupController.getArticleList(new ArticlePullDownListCallback(), currentPage);
    }

    private void checkPermissions() {
        String [] permissions = new String[]{
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this, permissions, new PermissionsResultAction() {
            @Override
            public void onGranted() {
            }

            @Override
            public void onDenied(String permission) {
                showPermissionDenyDialog();
            }
        });
    }

    private void showPermissionDenyDialog() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("请打开相应的权限，否则将会导致应用程序不可用！")
                .setPositiveButton("去设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            Intent i = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");

                            String pkg = "com.android.settings";
                            String cls = "com.android.settings.applications.InstalledAppDetails";

                            i.setComponent(new ComponentName(pkg, cls));
                            i.setData(Uri.parse("package:" + getPackageName()));
                            startActivity(i);
                        } catch (Exception e) {

                        } finally {
                            finish();
                            android.os.Process.killProcess(android.os.Process.myPid());
                        }
                    }
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            if((System.currentTimeMillis() - exitTime) > 2000){
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                applicationFinish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ArticlePullDownListCallback extends BaseController.CommonUpdateViewAsyncCallback<ArrayList<Article>> {

        @Override
        public void onPostExecute(ArrayList<Article> articles) {
            if(BlankUtil.isBlank(articles)) {
                if(BlankUtil.isBlank(mArticles)) {
                    mJsoupController.getArticleListLocal(this);
                }
                return;
            }
            if(BlankUtil.isBlank(mArticles)) {
                mJsoupController.setArticleListLocal(articles);
            }
            mArticles.clear();
            mArticles.addAll(articles);
            mAdapter.setData(articles);
            mAdapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
        }

        @Override
        public void onException(Exception ie) {
            mListView.onRefreshComplete();
            if(BlankUtil.isBlank(mArticles)) {
                mJsoupController.getArticleListLocal(this);
            }
            return;
        }
    }

    class ArticlePullUpListCallback extends BaseController.CommonUpdateViewAsyncCallback<ArrayList<Article>> {

        @Override
        public void onPostExecute(ArrayList<Article> articles) {
            mArticles.addAll(articles);
            mAdapter.setData(mArticles);
            mAdapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
        }

        @Override
        public void onException(Exception ie) {
            mListView.onRefreshComplete();
        }
    }

}
