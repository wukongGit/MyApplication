package com.suncheng.myapplication;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.suncheng.myapplication.adapter.MasonryAdapter;
import com.suncheng.myapplication.framework.BaseActivity;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.JsoupController;
import com.suncheng.myapplication.net.OnDataCallBack;
import com.suncheng.myapplication.utils.NetworkUtils;
import com.suncheng.myapplication.view.SpacesItemDecoration;

import java.util.ArrayList;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private PullToRefreshRecyclerView mListView;
    private MasonryAdapter mAdapter;
    private JsoupController mJsoupController;
    private int currentPage = 1;

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
        mListView = (PullToRefreshRecyclerView) findViewById(R.id.news_list);
        mListView.getRefreshableView().setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mListView.getRefreshableView().addItemDecoration(decoration);
        mAdapter = new MasonryAdapter();
        mListView.setAdapter(mAdapter);
        mJsoupController = new JsoupController();
        mListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if(NetworkUtils.isNetworkStrictlyAvailable(MainActivity.this)) {
                    currentPage = 1;
                    mJsoupController.getArticleList(new ArticlePullDownListCallback(), 1);
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

    @Override
    public void onClick(View v) {

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
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    class ArticlePullDownListCallback implements OnDataCallBack<ArrayList<Article>> {

        @Override
        public void onSuccess(ArrayList<Article> result, int statusCode, String message) {
            mAdapter.setData(result);
            mAdapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
        }

        @Override
        public void onFailed(Exception e, int responseCode) {
            mListView.onRefreshComplete();
        }
    }

    class ArticlePullUpListCallback implements OnDataCallBack<ArrayList<Article>> {

        @Override
        public void onSuccess(ArrayList<Article> result, int statusCode, String message) {
            mAdapter.addData(result);
            mAdapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
        }

        @Override
        public void onFailed(Exception e, int responseCode) {
            mListView.onRefreshComplete();
        }
    }

}
