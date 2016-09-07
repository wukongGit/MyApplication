package com.suncheng.myapplication;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.suncheng.myapplication.adapter.ImageAdapter;
import com.suncheng.myapplication.adapter.MasonryAdapter;
import com.suncheng.myapplication.framework.BaseActivity;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.BaseController;
import com.suncheng.myapplication.net.JsoupController;
import com.suncheng.myapplication.utils.NetworkUtils;
import com.suncheng.myapplication.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends BaseActivity {
    private PullToRefreshRecyclerView mListView;
    private ImageAdapter mAdapter;
    private JsoupController mJsoupController;
    private Article mArticle;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        mArticle = (Article) getIntent().getSerializableExtra("article");
        setTitle(mArticle.getTitle());
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
        mListView.getRefreshableView().setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mListView.getRefreshableView().addItemDecoration(decoration);
        mAdapter = new ImageAdapter();
        mListView.setAdapter(mAdapter);
        mJsoupController = new JsoupController();
        mJsoupController.getArticleDetail(new ArticleDetailCallback(), mArticle.getUrl());
    }

    class ArticleDetailCallback extends BaseController.CommonUpdateViewAsyncCallback<ArrayList<String>> {

        @Override
        public void onPostExecute(ArrayList<String> articles) {
            mAdapter.setData(articles);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onException(Exception ie) {

        }
    }


}
