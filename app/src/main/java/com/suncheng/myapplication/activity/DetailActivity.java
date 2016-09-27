package com.suncheng.myapplication.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.suncheng.myapplication.R;
import com.suncheng.myapplication.adapter.ImageAdapter;
import com.suncheng.myapplication.framework.BaseActivity;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.controller.BaseController;
import com.suncheng.myapplication.net.controller.JsoupController;
import com.suncheng.myapplication.view.SpacesItemDecoration;

import java.util.ArrayList;

public class DetailActivity extends BaseActivity {
    private PullToRefreshRecyclerView mListView;
    private ProgressBar mProgressBar;
    private ImageAdapter mAdapter;
    private JsoupController mJsoupController;
    private Article mArticle;

    @Override
    protected void onActivityCreate(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_recommend);
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
        mProgressBar = (ProgressBar) findViewById(R.id.progressbar);
        mListView.getRefreshableView().setLayoutManager(new LinearLayoutManager(this));
        SpacesItemDecoration decoration=new SpacesItemDecoration(16);
        mListView.getRefreshableView().addItemDecoration(decoration);
        mAdapter = new ImageAdapter();
        mListView.setAdapter(mAdapter);
        mJsoupController = new JsoupController();
        mProgressBar.setVisibility(View.VISIBLE);
        mJsoupController.getArticleDetail(new ArticleDetailCallback(), mArticle.getUrl());
    }

    class ArticleDetailCallback extends BaseController.CommonUpdateViewAsyncCallback<ArrayList<String>> {

        @Override
        public void onPostExecute(ArrayList<String> articles) {
            mAdapter.setData(articles);
            mAdapter.notifyDataSetChanged();
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onException(Exception ie) {
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
