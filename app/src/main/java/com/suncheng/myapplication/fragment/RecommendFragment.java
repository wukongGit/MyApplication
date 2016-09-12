package com.suncheng.myapplication.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshRecyclerView;
import com.suncheng.myapplication.R;
import com.suncheng.myapplication.adapter.MasonryAdapter;
import com.suncheng.myapplication.framework.BaseFragment;
import com.suncheng.myapplication.model.Article;
import com.suncheng.myapplication.net.controller.BaseController;
import com.suncheng.myapplication.net.controller.JsoupController;
import com.suncheng.myapplication.utils.BlankUtil;
import com.suncheng.myapplication.utils.NetworkUtils;
import com.suncheng.myapplication.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class RecommendFragment extends BaseFragment {
    private PullToRefreshRecyclerView mListView;
    private MasonryAdapter mAdapter;
    private List<Article> mArticles;
    private JsoupController mJsoupController;
    private int currentPage = 1;

    public static RecommendFragment newInstance() {
        RecommendFragment fragment = new RecommendFragment();
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
                    mJsoupController.getArticleList(new ArticlePullDownListCallback(), currentPage);
                } else {
                    mListView.onRefreshComplete();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                if (NetworkUtils.isNetworkStrictlyAvailable(getActivity())) {
                    currentPage++;
                    mJsoupController.getArticleList(new ArticlePullUpListCallback(), currentPage);
                } else {
                    mListView.onRefreshComplete();
                }

            }
        });
        mJsoupController.getArticleList(new ArticlePullDownListCallback(), currentPage);
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
