package com.mla.newsapp.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.mla.newsapp.R;
import com.mla.newsapp.model.VideosItem;

import java.util.ArrayList;

/**
 * Created by amit.rs on 18/05/15.
 */
public class VideoMediaPlayerPagerAdapter extends PagerAdapter{

    private Context mContext;
    private int numberOfPages;
    private ArrayList<VideosItem> video_list;
    private View[] pages;
    private int totalRenderedPages = 3;
    private LayoutInflater inflater;
    private int lastPageCreatedIndex;
    int currentPosition;


    public VideoMediaPlayerPagerAdapter(Context context , int numberOfPages, ArrayList<VideosItem> video_list) {
        mContext = context;
        this.numberOfPages = numberOfPages;
        this.video_list = video_list;
        this.pages = new View[totalRenderedPages];
         inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return numberOfPages;
    }

    @Override
    public boolean isViewFromObject(View view, Object o) {
        return view == o;
    }


    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        setCurrentPosition(position);
        View view = pages[position % totalRenderedPages];
        Holder mHolder = null;


        if(view == null)
        {

            view = inflater.inflate(R.layout.activity_video_player, null);

            pages[position % totalRenderedPages] =  view;

            mHolder = new Holder();

            mHolder.webview = (WebView)view.findViewById(R.id.web_view);

            mHolder.progressBar = (ProgressBar) view.findViewById(R.id.progressBar);



            mHolder.webview.getSettings().setJavaScriptEnabled(true);
            mHolder.webview.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);

            mHolder.webview.setWebViewClient(new WebViewClient() {

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    Log.d("debug","I am into");
                    //((View)view.getTag()).setVisibility(View.GONE);


                }
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    Log.d("debug","I am into");

                   // ((View)view.getTag()).setVisibility(View.GONE);
                }
            });



            view.setTag(mHolder);

        }
        else
        {
            mHolder = (Holder)view.getTag();

            container.removeView(view);

            int indexToStopVideo;

            // Swipe Forward
            if(position > lastPageCreatedIndex)
            {
                indexToStopVideo= (position-2);

            }
            else // Swipe Backward
            {
                indexToStopVideo= (position+2);

            }

            View previousPage = pages[indexToStopVideo % totalRenderedPages];
            Holder previousPageHolder =(Holder)previousPage.getTag();

            if(previousPage != null)
            {
                previousPageHolder.webview.loadUrl(previousPageHolder.webview.getUrl());
            }


        }


        mHolder.webview.loadUrl("https://www.youtube.com/watch?v="+ video_list.get(position).getVideoID());

        (container).addView(view);

        lastPageCreatedIndex = position;

        return view;

    }

    public int getCurrentPosition() {
        return currentPosition ;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

       // ((WebView)((View)object).getTag()).destroy();
      //  (container).removeView((View)object);
//        (container).removeAllViews();
//        webView.onPause();
//
//
//        webView.loadUrl("about:blank");
//        webView.destroy();
//        webView = null;
    }



    class Holder{
        WebView webview;

        ProgressBar progressBar;
    }

}
