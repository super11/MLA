package com.mla.newsapp.listener;

import android.util.Log;
import android.widget.AbsListView;

/**
 * Created by kumar.samdwar on 15/05/15.
 */
public abstract class EndlessListScrollListener implements AbsListView.OnScrollListener {
    private static final String TAG = "FLICKR";
    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private int visibleThreshold = 0;
    // The current offset index of data you have loaded
    private int currentPage = 0;
    // The total number of items in the dataset after the last load
    private int previousTotalItemCount = 0;
    // True if we are still waiting for the last set of data to load.
    public boolean isLoading = true;
    // Sets the starting page index
    private int startingPageIndex = 0;

    public EndlessListScrollListener() {
    }

    public EndlessListScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public EndlessListScrollListener(int visibleThreshold, int startPage) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startPage;
        this.currentPage = startPage;
    }

    // This happens many times a second during a scroll, so be wary of the code you place here.
    // We are given a few useful parameters to help us work out if we need to load some more data,
    // but first we check if we are waiting for the previous load to finish.
    @Override
    public void onScroll(AbsListView view,int firstVisibleItem,int visibleItemCount,int totalItemCount)
    {
        // If the total item count is zero and the previous isn't, assume the
        // list is invalidated and should be reset back to initial state

    Log.d(TAG, "firstVisibleItem: "+firstVisibleItem+", visibleItemCount: "+visibleItemCount+"totalItemCount: "+totalItemCount);
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) { this.isLoading = true; }
        }
        // If it’s still loading, we check to see if the dataset count has
        // changed, if so we conclude it has finished loading and update the current page
        // number and total item count.
        if (isLoading && (totalItemCount > previousTotalItemCount)) {
            isLoading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If it isn’t currently loading, we check to see if we have breached
        // the visibleThreshold and need to reload more data.
        // If we do need to reload some more data, we execute onLoadMore to fetch the data.
        if (!isLoading && (totalItemCount - visibleItemCount)<=(firstVisibleItem + visibleThreshold)) {
            onLoadMore(currentPage + 1, totalItemCount);
            isLoading = true;
        }
        /*if(firstVisibleItem+visibleItemCount>=(totalItemCount)){
            onLoadMore(currentPage + 1, totalItemCount);
        }*/
    }

    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // Don't take any action on changed
    }
}