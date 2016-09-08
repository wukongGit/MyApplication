package com.suncheng.myapplication;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.suncheng.myapplication.adapter.MasonryAdapter;
import com.suncheng.myapplication.framework.BaseActivity;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.BaseController;
import com.suncheng.myapplication.net.JsoupController;
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
