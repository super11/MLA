package com.mla.newsapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mla.newsapp.R;
import com.mla.newsapp.model.FlickrResponse;
import com.mla.newsapp.model.JsonPhotoDataProvider;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;
import com.mla.newsapp.utils.HttpsTrustManager;
import com.mla.newsapp.utils.ListAnimationUtils;

/**
 * Created by kumar.samdwar on 17/05/15.
 */
public class ImageListAdapter extends BaseAdapter implements BaseNewsAdapterUpdaterListener {


    private static final String TAG = "FLICKR";
    private Context mContext;
    private FlickrResponse mFlickrResponse;
    private int totalNumberOfItems;
    private ImageLoader mImageLoader;
    private int lastPosition = -1;
    private NetworkRequestQueue mNetworkRequestQueue;


    public ImageListAdapter(Context context, FlickrResponse response) {
        mContext = context;
        mFlickrResponse = response;
        mNetworkRequestQueue = NetworkRequestQueue.getInstance();
        mImageLoader = mNetworkRequestQueue.getImageLoader();

        if (response != null && response.mFlickrPhoto != null) {
            totalNumberOfItems = response.mFlickrPhoto.getPhoto().size();
        } else {
            totalNumberOfItems = 0;
        }
    }

    @Override
    public int getCount() {
        return mFlickrResponse.mFlickrPhoto.getPhoto().size();
    }

    @Override
    public Object getItem(int i) {
        return mFlickrResponse.mFlickrPhoto.getPhoto().get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        View rowView = view;
        ImageListViewHolder imageListViewHolder = null;

        if (rowView == null) {
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.image_list_item, viewGroup, false);
            imageListViewHolder =  new ImageListViewHolder();
//            TextView textView = (TextView) rowView.findViewById(R.id.title_view);
            NetworkImageView networkImageView = (NetworkImageView) rowView.findViewById(R.id.image_1);
//            imageListViewHolder.imageTitleView = textView;
            imageListViewHolder.networkImageView  = networkImageView;
            rowView.setTag(imageListViewHolder);
        }
        else{
            imageListViewHolder = (ImageListViewHolder) rowView.getTag();
        }
        /*if (i % 2 == 0) {
//                rowView.setBackgroundColor(Color.rgb(200, 231, 230));
            rowView.setBackgroundColor(Color.rgb(220, 220, 220));
        } else {
//                rowView.setBackgroundColor(Color.rgb(210, 220, 250));
            rowView.setBackgroundColor(Color.rgb(230, 230, 230));
        }*/
//            rowView.setBackgroundColor(Color.BLUE);

//        rowView.setBackgroundColor(Color.rgb(230, 230, 230));
        rowView.setBackgroundColor(Color.rgb(220, 220, 220));


        if (mFlickrResponse != null && mFlickrResponse.mFlickrPhoto != null) {
            JsonPhotoDataProvider jsonPhotoDataProvider = new JsonPhotoDataProvider(mFlickrResponse, i);

            String imageUrl = jsonPhotoDataProvider.getImageUrl();

            Log.d(TAG, imageUrl);
            imageListViewHolder.networkImageView.setImageUrl(imageUrl, mImageLoader);
//            imageListViewHolder.imageTitleView.setText(String.valueOf(jsonPhotoDataProvider.getTitle()));
            HttpsTrustManager.allowAllSSL();
            mImageLoader.get(imageUrl, ImageLoader.getImageListener(imageListViewHolder.networkImageView, R.drawable.hourglass_icon, R.drawable.error_ic));
        } else {
//            imageListViewHolder.imageTitleView.setText("Some Error Occurred !");
            imageListViewHolder.networkImageView.setErrorImageResId(R.drawable.error_ic);
        }

        view = rowView;
//            Animation animation = ListAnimationUtils.loadAnimation(mContext, R.anim.push_up_in);
        int position = i;
        ListAnimationUtils listAnimationUtils = new ListAnimationUtils(mContext);
        Animation animation;
        if(position>lastPosition){
            animation=listAnimationUtils.animateFadeIn();
        }else{
            animation=listAnimationUtils.animateFadeOut();
        }

        lastPosition = position;
//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        view.startAnimation(animation);
        animation.setDuration(430);
//        lastPosition = position;
//        animation = null;

        /*ListAnimationUtils listAnimationUtils = new ListAnimationUtils(mContext);
        Animation animation = listAnimationUtils.animatePushLeftIn();
        view.startAnimation(animation);
        animation.setDuration(430);
*/

        return view;
    }

    @Override
    public void updateListener() {
        this.notifyDataSetChanged();
    }

    public class ImageListViewHolder {
        private TextView imageTitleView;
        private NetworkImageView networkImageView;
    }
}

