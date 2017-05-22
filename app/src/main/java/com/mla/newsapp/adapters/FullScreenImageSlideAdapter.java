package com.mla.newsapp.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mla.newsapp.R;
import com.mla.newsapp.model.FlickrResponse;
import com.mla.newsapp.model.JsonPhotoDataProvider;
import com.mla.newsapp.network.request.common.ImageResponse;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;
import com.mla.newsapp.utils.HttpsTrustManager;

/**
 * Created by kumar.samdwar on 20/05/15.
 */
public class FullScreenImageSlideAdapter extends PagerAdapter implements BaseNewsAdapterUpdaterListener {
    private static final String TAG = "FLICKR";
    private Context mContext;
    private NetworkRequestQueue mNetworkRequestQueue;
    private ImageResponse mImageResponse;
    private NetworkImageView mNetworkImageView;
    private ImageLoader mImageLoader;
    private String mImageURL;
    private static int mPosition;

    public FullScreenImageSlideAdapter(Context context) {
        mContext = context;
        mImageResponse = ImageResponse.getInstance();
        mNetworkRequestQueue = NetworkRequestQueue.getInstance();
        mImageLoader = mNetworkRequestQueue.getImageLoader();
        mImageResponse.registerUpdateDataSourceListener(FullScreenImageSlideAdapter.this);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return mImageResponse.getFlickrResponse().getFlickrPhoto().getPhoto().size();
    }

    /**
     * Create the page for the given position.  The adapter is responsible
     * for adding the view to the container given here, although it only
     * must ensure this is done by the time it returns from
     * {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View in which the page will be shown.
     * @param position  The page position to be instantiated.
     * @return Returns an Object representing the new page.  This does not
     * need to be a View, but can be some other container of the page.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View rowView = (RelativeLayout) layoutInflater.inflate(R.layout.full_screen_image_slide, container, false);

        mNetworkImageView = (NetworkImageView) rowView.findViewById(R.id.full_screen_gallery_image_view);


        final FlickrResponse flickrResponse = mImageResponse.getFlickrResponse();
        Log.d(TAG, "flickrResponse = :" + flickrResponse);
        if (flickrResponse != null && flickrResponse.mFlickrPhoto != null) {
            JsonPhotoDataProvider jsonPhotoDataProvider = new JsonPhotoDataProvider(flickrResponse, position);
            mImageURL = jsonPhotoDataProvider.getImageUrl();
            Log.d(TAG, mImageURL);
            mNetworkImageView.setImageUrl(mImageURL, mImageLoader);
            HttpsTrustManager.allowAllSSL();
            mImageLoader.get(mImageURL, ImageLoader.getImageListener(mNetworkImageView, R.drawable.loading_txt, R.drawable.error_ic));
        } else {
            mNetworkImageView.setErrorImageResId(R.drawable.error_ic);
        }

       /* View view = ((Activity)mContext).findViewById(R.id.tool_bar);
        view.setVisibility(View.GONE);
        Log.i(TAG, "Tool view = " + view);

        ViewGroup parent = (ViewGroup) view.getParent();
        parent.removeView(view);*/

        container.addView(rowView);
        return rowView;
    }

    /**
     * Remove a page for the given position.  The adapter is responsible
     * for removing the view from its container, although it only must ensure
     * this is done by the time it returns from {@link #finishUpdate(ViewGroup)}.
     *
     * @param container The containing View from which the page will be removed.
     * @param position  The page position to be removed.
     * @param object    The same object that was returned by
     *                  {@link #instantiateItem(View, int)}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
//        super.destroyItem(container, position, object);
    }

    /**
     * Determines whether a page View is associated with a specific key object
     * as returned by {@link #instantiateItem(ViewGroup, int)}. This method is
     * required for a PagerAdapter to function properly.
     *
     * @param view   Page View to check for association with <code>object</code>
     * @param object Object to check for association with <code>view</code>
     * @return true if <code>view</code> is associated with the key object <code>object</code>
     */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * Called when the host view is attempting to determine if an item's position
     * has changed. Returns {@link #POSITION_UNCHANGED} if the position of the given
     * item has not changed or {@link #POSITION_NONE} if the item is no longer present
     * in the adapter.
     * <p/>
     * <p>The default implementation assumes that items will never
     * change position and always returns {@link #POSITION_UNCHANGED}.
     *
     * @param object Object representing an item, previously returned by a call to
     *               {@link #instantiateItem(View, int)}.
     * @return object's new position index from [0, {@link #getCount()}),
     * {@link #POSITION_UNCHANGED} if the object's position has not changed,
     * or {@link #POSITION_NONE} if the item is no longer present.
     */
    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }

    /**
     * This method should be called by the application if the data backing this adapter has changed
     * and associated views should update.
     */
    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public void updateListener() {
        this.notifyDataSetChanged();
    }
}
