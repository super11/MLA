package com.mla.newsapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.toolbox.ImageLoader;
import com.mla.newsapp.adapters.GalleryViewPagerAdapter;
import com.mla.newsapp.config.AppPreferences;
import com.mla.newsapp.model.JsonPhotoDataProvider;
import com.mla.newsapp.network.request.common.ImageResponse;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class GalleryActivity extends ActionBarActivity {

    ImageLoader mImageLoader;
    private static String TAG = "FLICKR";
    private static String FLICKR_API_URL;
    private NetworkRequestQueue mNetworkRequestQueue;
    private ImageResponse mImageResponse;

    private int listItemPostion;
    private Context mContext;
    private Toolbar toolbar;
    GalleryViewPagerAdapter mCustomPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.app_name);
            setSupportActionBar(toolbar);
        }
        FLICKR_API_URL = AppPreferences.FLICKR_API_URL;
        Intent intent = getIntent();
        mContext = this;
        FLICKR_API_URL = FLICKR_API_URL + "&text=car";
        mImageResponse = ImageResponse.getInstance();
        if (intent != null) {
            Log.d(TAG, intent.toString());
            if (null != intent.getExtras()) {

                String searchString = intent.getStringExtra(SearchManager.QUERY);
                listItemPostion = intent.getIntExtra("ListItemPosition", 0);
                if (searchString != null)
                    FLICKR_API_URL = FLICKR_API_URL + "&text=" + URLEncoder.encode(searchString);
            }
        }
        mNetworkRequestQueue = NetworkRequestQueue.getInstance();
        mNetworkRequestQueue.initialize(mContext);
        mImageLoader = mNetworkRequestQueue.getImageLoader();
        initViewPager();
    }

    public void initViewPager() {
        Log.d(TAG, "Init ViewPager...");
        ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        mCustomPagerAdapter = new GalleryViewPagerAdapter(GalleryActivity.this);
        viewPager.setAdapter(mCustomPagerAdapter);
        viewPager.setCurrentItem(listItemPostion);
        viewPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            private static final float MIN_SCALE = 0.75f;

            @Override
            public void transformPage(View view, float position) {
                int pageWidth = view.getWidth();

                if (position < -1) { // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    view.setAlpha(0);

                } else if (position <= 0) { // [-1,0]
                    // Use the default slide transition when moving to the left page
                    view.setAlpha(1);
                    view.setTranslationX(0);
                    view.setScaleX(1);
                    view.setScaleY(1);

                } else if (position <= 1) { // (0,1]
                    // Fade the page out.
                    view.setAlpha(1 - position);

                    // Counteract the default slide transition
                    view.setTranslationX(pageWidth * -position);

                    // Scale the page down (between MIN_SCALE and 1)
                    float scaleFactor = MIN_SCALE
                            + (1 - MIN_SCALE) * (1 - Math.abs(position));
                    view.setScaleX(scaleFactor);
                    view.setScaleY(scaleFactor);

                } else { // (1,+Infinity]
                    // This page is way off-screen to the right.
                    view.setAlpha(0);
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onPause() {
//        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.abc_fade_out);
        super.onPause();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        if (id == R.id.menu_item_share) {
            Intent share = new Intent(Intent.ACTION_SEND);

            String imageUrl;
            JsonPhotoDataProvider jsonPhotoDataProvider = new JsonPhotoDataProvider(mImageResponse.getFlickrResponse(), (mCustomPagerAdapter.getCurrentPosition() - 1));
            imageUrl = jsonPhotoDataProvider.getImageUrl();

            Log.e(TAG, "mCustomPagerAdapter.getCurrentPosition() : " + (mCustomPagerAdapter.getCurrentPosition() - 1));
            Log.e(TAG, "Sharing image uri with : " + imageUrl);
            new LoadImage().execute(imageUrl);
        }
        return super.onOptionsItemSelected(item);
    }

    class LoadImage extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... params) {
            URL myurl = null;
            try {
                myurl = new URL(params[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(myurl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                    bitmap, "Image Description", null);
            Uri shareUri = Uri.parse(path);
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/jpeg"); // might be text, sound, whatever
            share.putExtra(Intent.EXTRA_STREAM, shareUri);
            startActivity(share);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
