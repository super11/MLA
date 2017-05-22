package com.mla.newsapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.mla.newsapp.ArticleNewsListener.AdapterListener;
import com.mla.newsapp.R;
import com.mla.newsapp.controller.ArticleNewsController;
import com.mla.newsapp.model.ResponseData;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;

/**
 * Created by ashokkumar.y on 15/05/15.
 */
public class ArticleNewsDetailAdapter  extends PagerAdapter implements AdapterListener{

    private static final String TAG = "ARTICLE";
    Context mContext;
    ResponseData mResponse;
   // ImageLoader mImageLoader;
   // NetworkRequestQueue mQueue;
    LayoutInflater mLayoutInflater;
    WebView mWebView;
    ArticleNewsController articleNewsController;



    int currentPosition;

   //constructor
   public ArticleNewsDetailAdapter(Context context,int position){
       this.mContext=context;
       articleNewsController= ArticleNewsController.getInstance();
       this.mResponse=articleNewsController.getInstance().getResponseObject();
      // mQueue = NetworkRequestQueue.getInstance();
      // mQueue.initialize(context);
       //mImageLoader=mQueue.getImageLoader();
       mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      // mResponse.registerDataSourceListener
       articleNewsController.registerDataSourceListener(ArticleNewsDetailAdapter.this);
   }
    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Override
    public int getCount() {
        return mResponse.getNewsData().size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.article_item_view_pager, container, false);
        ProgressBar progressBar = (ProgressBar) itemView.findViewById(R.id.articleProgressBar);

        mWebView=(WebView)itemView.findViewById(R.id.main_webview);
        setCurrentPosition(position);

        mWebView.setTag(progressBar);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl(mResponse.getNewsData().get(position).getContentUrl());
        //progressBar.setProgress(100);
        mWebView.setWebChromeClient(new WebChromeClient());
       // mWebView.setWebViewClient(new WebViewClient());
     mWebView.setWebViewClient(new WebViewClient(){


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                Log.d(TAG,"onPageStarted");
               // progressBar.setProgress(0);
                ((View)view.getTag()).setVisibility(View.VISIBLE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i(TAG, "onPageFinished");
                ((View)view.getTag()).setVisibility(View.GONE);

            }
        });

      /* mWebView.setWebChromeClient(new WebChromeClient(){

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(View.VISIBLE);
                progressBar.setProgress(newProgress);
                if(newProgress==100){
                    progressBar.setVisibility(ProgressBar.GONE);
                }
            }
        } );*/
        container.addView(itemView);
        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    @Override
    public void notifyListener() {
        this.notifyDataSetChanged();
    }
}
