package com.suncheng.myapplication.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.sunc.controller.BaseController;
import com.suncheng.myapplication.R;
import com.suncheng.myapplication.adapter.ImageAdapter;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.controller.JsoupController;
import com.suncheng.myapplication.view.SimpleTitleBar;
import com.suncheng.myapplication.view.SpacesItemDecoration;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    private PullToRefreshRecyclerView mListView;
    private SimpleTitleBar mTitleBar;
    private ProgressBar mProgressBar;
    private ImageAdapter mAdapter;
    private JsoupController mJsoupController;
    private Article mArticle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_recommend);
        mArticle = (Article) getIntent().getSerializableExtra("article");
        initView();
    }

    private void initView() {
        mTitleBar = (SimpleTitleBar)findViewById(R.id.titleBar);
        mTitleBar.setVisibility(View.VISIBLE);
        if(mArticle != null) {
            mTitleBar.setTitle(mArticle.getTitle());
        }
        mTitleBar.setBackListner(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
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
            mProgressBar.setVisibility(View.GONE);
            mAdapter.setData(articles);
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void onException(Exception ie) {
            mProgressBar.setVisibility(View.GONE);
        }
    }


}
