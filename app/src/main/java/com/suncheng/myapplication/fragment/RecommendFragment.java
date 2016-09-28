package com.suncheng.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.sunc.controller.BaseController;
import com.suncheng.commontools.utils.BlankUtil;
import com.suncheng.commontools.utils.NetworkUtils;
import com.suncheng.myapplication.R;
import com.suncheng.myapplication.adapter.MasonryAdapter;
import com.suncheng.myapplication.framework.BaseFragment;
import com.suncheng.myapplication.framework.Constants;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.controller.JsoupController;
import com.suncheng.myapplication.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends BaseFragment {
    public static final String URL = "url";
    private PullToRefreshRecyclerView mListView;
    private ProgressBar mProgressBar;
    private MasonryAdapter mAdapter;
    private List<Article> mArticles;
    private JsoupController mJsoupController;
    private int currentPage = 1;
    private String mUrl;

    public static RecommendFragment newInstance(String url) {
        RecommendFragment fragment = new RecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recommend, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = (PullToRefreshRecyclerView) view.findViewById(R.id.news_list);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if(bundle != null) {
            mUrl = bundle.getString(URL);
        } else {
            mUrl = Constants.POCO_URL_HOT;
        }
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
                if (NetworkUtils.isNetworkStrictlyAvailable(getActivity())) {
                    currentPage = 1;
                    mJsoupController.getArticleList(new ArticlePullDownListCallback(), mUrl, currentPage);
                } else {
                    mListView.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (NetworkUtils.isNetworkStrictlyAvailable(getActivity())) {
                    currentPage++;
                    mJsoupController.getArticleList(new ArticlePullUpListCallback(), mUrl, currentPage);
                } else {
                    mListView.onRefreshComplete();
                }

            }
        });
        mProgressBar.setVisibility(View.VISIBLE);
        mJsoupController.getArticleList(new ArticlePullDownListCallback(), mUrl, currentPage);
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
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onException(Exception ie) {
            mListView.onRefreshComplete();
            mProgressBar.setVisibility(View.GONE);
            if(BlankUtil.isBlank(mArticles)) {
                mJsoupController.getArticleListLocal(this);
            }
        }
    }

    class ArticlePullUpListCallback extends BaseController.CommonUpdateViewAsyncCallback<ArrayList<Article>> {

        @Override
        public void onPostExecute(ArrayList<Article> articles) {
            mArticles.addAll(articles);
            mAdapter.setData(mArticles);
            mAdapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
            mProgressBar.setVisibility(View.GONE);
        }

        @Override
        public void onException(Exception ie) {
            mListView.onRefreshComplete();
            mProgressBar.setVisibility(View.GONE);
        }
    }

}
