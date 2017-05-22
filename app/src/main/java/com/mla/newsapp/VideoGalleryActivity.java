package com.mla.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.toolbox.ImageLoader;
import com.mla.newsapp.adapters.VideoMediaPlayerPagerAdapter;
import com.mla.newsapp.controller.VideoNewsController;
import com.mla.newsapp.model.VideosItem;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;
import com.mla.newsapp.utils.DemoUtil;

import java.util.ArrayList;


public class VideoGalleryActivity extends ActionBarActivity {

    private ImageLoader mImageLoader;
    private NetworkRequestQueue mNetworkRequestQueue;
    private int listItemPosition;
    private int totalNumberOfItems;

    private Context mContext;
    private Toolbar toolbar;
    private ArrayList<VideosItem> video_list;
    private ViewPager viewPager;
    private VideoMediaPlayerPagerAdapter customPagerAdapter;
    int currentPosition;


    // Activity lifecycle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }

        Intent intent = getIntent();

        mContext = this;

        if (intent != null) {

            if (null != intent.getExtras()) {
                listItemPosition = intent.getIntExtra("ListItemPosition", 0);
                totalNumberOfItems = intent.getIntExtra("TotalNumberOfItems", 0);

            }

            mNetworkRequestQueue = NetworkRequestQueue.getInstance();
            mNetworkRequestQueue.initialize(mContext);
            mImageLoader = mNetworkRequestQueue.getImageLoader();
            video_list = VideoNewsController.getInstance().getVideosItems();



        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initViewPager();
    }

    public void initViewPager() {

      viewPager =  (ViewPager) findViewById(R.id.video_pager);
         customPagerAdapter = new VideoMediaPlayerPagerAdapter(VideoGalleryActivity.this, totalNumberOfItems, video_list);
        viewPager.setAdapter(customPagerAdapter);
        viewPager.setCurrentItem(listItemPosition);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)     {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.menu_item_share) {
            currentPosition = customPagerAdapter.getCurrentPosition();
            video_list = VideoNewsController.getInstance().getVideosItems();
            String url = video_list.get(currentPosition).getThumbnailUrl();
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain"); // might be text, sound, whatever
            share.putExtra(Intent.EXTRA_TEXT, url);

            Log.d("URL", url);
            startActivity(share);


        }
        if(id == R.id.exoplayer_test){

            Intent playerIntent = new Intent(this, PlayerActivity.class)
                    .setData(Uri.parse("http://html5demos.com/assets/dizzy.mp4"))
                    .putExtra(PlayerActivity.CONTENT_ID_EXTRA, "")
                    .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, DemoUtil.TYPE_MP4);
            startActivity(playerIntent);
            return true;


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
       // customPagerAdapter.destroyItem();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
