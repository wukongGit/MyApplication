package com.handmark.pulltorefresh.library;

import android.content.Context;
import android.graphics.Rect;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by suncheng on 2016/6/22.
 */
public class PullToRefreshRecyclerView extends PullToRefreshBase<RecyclerView> {

    public PullToRefreshRecyclerView(Context context) {
        super(context);
    }

    public PullToRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public Orientation getPullToRefreshScrollDirection() {
        return Orientation.VERTICAL;
    }

    @Override
    protected RecyclerView createRefreshableView(Context context, AttributeSet attrs) {
        RecyclerView recyclerView = new RecyclerView(context, attrs);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = 0;
                outRect.right = 0;
                outRect.bottom = 0;
            }
        });
        return recyclerView;
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        getRefreshableView().setAdapter(adapter);
    }

    @Override
    protected boolean isReadyForPullEnd() {
        RecyclerView.Adapter adapter = getRefreshableView().getAdapter();
        if(adapter == null) {
            return false;
        }
        int lastPosition = adapter.getItemCount();
        int lastVisiblePosition = getLastVisiblePosition();
        return lastVisiblePosition >= lastPosition - 1;
    }

    @Override
    protected boolean isReadyForPullStart() {
//        LinearLayoutManager layoutManager = (LinearLayoutManager) getRefreshableView().getLayoutManager();
//        return layoutManager.findFirstCompletelyVisibleItemPosition() == 0;
        return getFirstVisiblePosition() == 0;
    }

    /**
     * 获取第一条展示的位置
     *
     * @return
     */
    private int getFirstVisiblePosition() {
        int position;
        if (getRefreshableView().getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getRefreshableView().getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        } else if (getRefreshableView().getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getRefreshableView().getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        } else if (getRefreshableView().getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getRefreshableView().getLayoutManager();
            int[] lastPositions = layoutManager.findFirstCompletelyVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMinPositions(lastPositions);
        } else {
            position = 0;
        }
        return position;
    }

    /**
     * 获得当前展示最小的position
     *
     * @param positions
     * @return
     */
    private int getMinPositions(int[] positions) {
        int size = positions.length;
        int minPosition = Integer.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            minPosition = Math.min(minPosition, positions[i]);
        }
        return minPosition;
    }

    /**
     * 获取最后一条展示的位置
     *
     * @return
     */
    private int getLastVisiblePosition() {
        int position;
        if (getRefreshableView().getLayoutManager() instanceof LinearLayoutManager) {
            position = ((LinearLayoutManager) getRefreshableView().getLayoutManager()).findLastCompletelyVisibleItemPosition();
        } else if (getRefreshableView().getLayoutManager() instanceof GridLayoutManager) {
            position = ((GridLayoutManager) getRefreshableView().getLayoutManager()).findLastCompletelyVisibleItemPosition();
        } else if (getRefreshableView().getLayoutManager() instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager layoutManager = (StaggeredGridLayoutManager) getRefreshableView().getLayoutManager();
            int[] lastPositions = layoutManager.findLastCompletelyVisibleItemPositions(new int[layoutManager.getSpanCount()]);
            position = getMaxPosition(lastPositions);
        } else {
            position = getRefreshableView().getLayoutManager().getItemCount() - 1;
        }
        return position;
    }

    /**
     * 获得最大的位置
     *
     * @param positions
     * @return
     */
    private int getMaxPosition(int[] positions) {
        int size = positions.length;
        int maxPosition = Integer.MIN_VALUE;
        for (int i = 0; i < size; i++) {
            maxPosition = Math.max(maxPosition, positions[i]);
        }
        return maxPosition;
    }
}
