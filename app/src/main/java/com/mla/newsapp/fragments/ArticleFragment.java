package com.mla.newsapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mla.newsapp.ArticleDetailPagerActivity;
import com.mla.newsapp.ArticleNewsListener.EndlessScrollListener;
import com.mla.newsapp.R;
import com.mla.newsapp.adapters.ArticleNewsAdapter;
import com.mla.newsapp.config.Constants;
import com.mla.newsapp.controller.ArticleNewsController;
import com.mla.newsapp.model.ResponseData;
import com.mla.newsapp.network.request.common.ArticleNetworkRequest;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


/**
 * Created by amit.rs on 11/05/15.
 */
public class ArticleFragment extends Fragment {

    ListView mListView;
    Activity mActivity;
    Context mContext;
    int start;
    int length;
    ArticleNetworkRequest getNewsListData = new ArticleNetworkRequest();
    ArticleNewsController articleNewsController;
   // ResponseData mObject;
    boolean appendDataOnScroll = false;
    ArticleNewsAdapter articleNewsAdapter;
    public final String TAG = "ARTICLE_NEWS";
    String searchKey;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        mActivity = getActivity();//new
        mContext = mActivity.getApplicationContext();//new
        articleNewsController = ArticleNewsController.getInstance();
        start = 1;
        length = 10;

        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                searchKey = intent.getStringExtra(Constants.IntentExtraKey.CATEGORY.toString());
                Log.i("Drawer_Click", searchKey);
                appendDataOnScroll=false;
                //try {
                if (searchKey == "Top News") {
                    searchKey = "topnews";

                }
                  getNewsListData.getArticleNews(mContext, start, length, mListView,ArticleFragment.this,articleNewsController,appendDataOnScroll,searchKey);
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.IntentActionType.CATEGORY_CHANGE.toString());
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(receiver, filter);
        if (searchKey == null) {
            searchKey = "phone";
            appendDataOnScroll=false;
            getNewsListData.getArticleNews(mContext, start, length, mListView, this, articleNewsController, appendDataOnScroll, searchKey);
        }


        mListView.setOnScrollListener(new EndlessScrollListener() {


            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "Inside on load more");
                articleNewsController = ArticleNewsController.getInstance();
                start = this.getPreviousTotalItemCount() + 1;
                length = start + 9;
                appendDataOnScroll = true;
                getNewsListData.getArticleNews(mContext, start, length, mListView, ArticleFragment.this, articleNewsController, appendDataOnScroll, searchKey);

            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //responseStringData=responseObject.toString();//new
                Intent intent;
                intent = new Intent(mContext, ArticleDetailPagerActivity.class);
                // intent.putExtra("data", responseStringData);
                intent.putExtra("position", i);
                startActivity(intent);
            }
        });


   /*  mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public int currentFirstVisibleItem;
            public int currentVisibleItemCount;
            public int currentScrollState;
            public int currentTotalItemCount;
            public int lastItem;

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                this.currentFirstVisibleItem = firstVisibleItem;
                this.currentVisibleItemCount = visibleItemCount;
                this.currentTotalItemCount =  totalItemCount;
                lastItem = firstVisibleItem + visibleItemCount;
                System.out.println("scrolled");
            }
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {
                System.out.println("onScrollStateChanged");
                this.currentScrollState = scrollState;
               // if ((mArticleNewsAdapter.getCount() - (currentFirstVisibleItem + currentVisibleItemCount)) <= 1) {
                    this.isScrollCompleted();
                //}

            }

            private void isScrollCompleted() {
                System.out.println("isScrollCompleted");
                if (this.currentVisibleItemCount > 0 && lastItem == this.currentTotalItemCount) {
                    //&& this.currentScrollState == 1
                   *//*** In this way I detect if there's been a scroll which has completed ***//*
                   *//*** do the work for load more date! ***//*
                    start = this.currentTotalItemCount+1;
                    length = this.currentTotalItemCount + 10;
                    isLoading=false;
                   // System.out.println("url "+mUrl);
                    if(!isLoading){
                        isLoading = true;
                        appendDataOnScroll=true;
                        getNewsListData.getArticleNews(mContext, start, length, mListView,mUrl,ArticleFragment.this,articleNewsController,appendDataOnScroll);
                    }


                }
            }
        });*/

        return view;
    }

    public void setResponseInAdapter(boolean firstTimeResponse) {
        // ArticleNewsController articleNewsController = new ArticleNewsController();
        //mObject=articleNewsController.getResponseObject();
        // articleNewsController=ArticleNewsController.getInstance();
        //articleNewsAdapter = new ArticleNewsAdapter(mContext);
        // articleNewsAdapter.notifyDataSetChanged();
        if (firstTimeResponse == true) {
            articleNewsAdapter = new ArticleNewsAdapter(mContext);
            mListView.setAdapter(articleNewsAdapter);
        }
    }

    public ArticleNewsAdapter getArticleNewsAdapter() {
        return articleNewsAdapter;
    }
}




