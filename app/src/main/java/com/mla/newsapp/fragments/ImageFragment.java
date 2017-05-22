package com.mla.newsapp.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mla.newsapp.GalleryActivity;
import com.mla.newsapp.R;
import com.mla.newsapp.adapters.ImageListAdapter;
import com.mla.newsapp.config.AppPreferences;
import com.mla.newsapp.config.Constants;
import com.mla.newsapp.listener.EndlessListScrollListener;
import com.mla.newsapp.model.FlickrResponse;
import com.mla.newsapp.model.JsonPhotoDataProvider;
import com.mla.newsapp.network.request.common.ImageResponse;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;
import com.mla.newsapp.utils.GSONBuilderHelper;
import com.mla.newsapp.utils.HttpsTrustManager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by amit.rs on 11/05/15.
 */
public class ImageFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ListView mListView;
    private Activity mActivity;

    private RequestQueue mRequestQueue;
    NetworkImageView mNetworkImageView;
    ImageLoader mImageLoader;
    //    ListView mImageListView;
    private GridView mGridView;
    private String mJsonResponse;
    FlickrResponse mFlickrRespons;
    private static String TAG = "FLICKR";
    private String FLICKR_API_URL;
    Context mContext;
    ProgressBar mProgressBar;
    private int totalNumberOfItems;
    private NetworkRequestQueue mNetworkRequestQueue;
    private int lastPosition = -1;
    private ImageListAdapter mImageListAdapter;
    GSONBuilderHelper mGsonBuilderHelper;
    JsonPhotoDataProvider mJsonPhotoDataProvider;
    ImageResponse mImageResponse;
    SwipeRefreshLayout mSwipeRefreshLayout;

    private OnFragmentInteractionListener mListener;

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BroadcastReceiver receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String searchKeyWord = intent.getStringExtra(Constants.IntentExtraKey.CATEGORY.toString());
                Log.i("Drawer_Click", searchKeyWord);

                try {
                    loadImageFeed(FLICKR_API_URL + "&text=" + URLEncoder.encode(searchKeyWord, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.IntentActionType.CATEGORY_CHANGE.toString());
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(receiver, filter);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        mActivity = getActivity();
//        mImageListView = (ListView) view.findViewById(R.id.list_fragment);
        mGridView = (GridView)  view.findViewById(R.id.grid_view_fragment);
        mContext = mActivity.getApplicationContext();
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressbar);

        mImageResponse = ImageResponse.getInstance();
        Log.d(TAG, "mProgressBar : " + mProgressBar);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.image_fragment_swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.d(TAG, "onRefresh Called");
                mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
            }
        });
        mGsonBuilderHelper = new GSONBuilderHelper();
        FLICKR_API_URL = AppPreferences.FLICKR_API_URL;
        loadImageFeed(FLICKR_API_URL + "&text=nature");

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                Toast.makeText(mContext, "You selected : " + (i + 1) + " item", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(mContext, GalleryActivity.class);
                intent.putExtra("ListItemPosition", i);
                intent.putExtra("TotalNumberOfItems", totalNumberOfItems);
                /*Log.d(TAG, "mJsonStringReponse before sending data : " + mJsonResponse);
                intent.putExtra("JsonResponseString", mJsonResponse);*/
                startActivity(intent);
                mActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.abc_fade_out);
            }
        });

        return view;
    }

    void loadImageFeed(final String apiUrl) {
        mNetworkRequestQueue = NetworkRequestQueue.getInstance();
        mNetworkRequestQueue.initialize(mContext);
        mRequestQueue = mNetworkRequestQueue.getRequestQueue();
        mImageLoader = mNetworkRequestQueue.getImageLoader();

        HttpsTrustManager.allowAllSSL();

        mNetworkRequestQueue.makeStringRequest(apiUrl);
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setRefreshing(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        mNetworkRequestQueue.setVolleyStringResponseListener(new NetworkRequestQueue.VolleyStringResponseListener() {
            @Override
            public void onVolleyResponse(String response) {
                FlickrResponse flickrResponse = mGsonBuilderHelper.getJsonFromString(response, FlickrResponse.class);
                mFlickrRespons = flickrResponse;
                mJsonResponse = response;
                mImageResponse.setFlickrResponse(flickrResponse);
                Log.d(TAG, flickrResponse + "");
                initListViewAdapter(mContext, apiUrl);
            }

            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Error Occurred :" + volleyError);
             /*   Cache.Entry entry = mRequestQueue.getCache().get(apiUrl);
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        System.out.println(" Reading from Cache Data");
                        Log.d(TAG, "Reading from Cache Data");
                        FlickrResponse flickrResponse = mGsonBuilderHelper.getJsonFromString(data, FlickrResponse.class);
                        mFlickrRespons = flickrResponse;
                        mJsonResponse = data;
                        mImageResponse.setFlickrResponse(flickrResponse);
                        initListViewAdapter(mContext, apiUrl);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Cache Data is NULL");
                }*/

            }
        });
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach()");

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        Log.d(TAG, "onDetach()");

        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public void initListViewAdapter(Context context, final String apiUrl) {
        Log.d(TAG, "Init ListView Adapter...");
        mImageListAdapter = new ImageListAdapter(context, mFlickrRespons);
        mImageResponse.registerUpdateDataSourceListener(mImageListAdapter);
        mGridView.setAdapter(mImageListAdapter);
        mGridView.setOnScrollListener(new EndlessListScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d(TAG, "Loading more data... page = " + page);
                loadNextPageData(page, apiUrl);
                this.isLoading = false;
            }
        });
        totalNumberOfItems = mGridView.getCount();

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mProgressBar.setVisibility(View.GONE);
            }
        });

    }

    private void loadNextPageData(int page, String apiUrl) {
        mSwipeRefreshLayout.setEnabled(true);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.orange, R.color.green, R.color.blue);
        /*mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);*/
        mSwipeRefreshLayout.setRefreshing(true);
        mNetworkRequestQueue.makeStringRequest(apiUrl + "&page=" + page);
        mNetworkRequestQueue.setVolleyStringResponseListener(new NetworkRequestQueue.VolleyStringResponseListener() {
            @Override
            public void onVolleyResponse(String response) {
                FlickrResponse flickrResponse = mGsonBuilderHelper.getJsonFromString(response, FlickrResponse.class);
                mFlickrRespons = flickrResponse;
                mJsonResponse = response;
                mImageResponse.updateFlickrResponse(flickrResponse);
//                Log.d(TAG, "For page  = "+ 2+" Response is :"+flickrResponse + "");
//                initListViewAdapter(mContext);
//                mImageListAdapter.notifyDataSetChanged();
//                mSwipeRefreshLayout.setRefreshing(false);

//                mSwipeRefreshLayout.setEnabled(false);

            }

            @Override
            public void onVolleyErrorResponse(VolleyError volleyError) {
                Log.d(TAG, "Error Occurred :" + volleyError);
                Cache.Entry entry = mRequestQueue.getCache().get(FLICKR_API_URL + "&text=car");
                if (entry != null) {
                    try {
                        String data = new String(entry.data, "UTF-8");
                        System.out.println(" Reading from Cache Data");
                        Log.d(TAG, "Reading from Cache Data");
                        FlickrResponse flickrResponse = mGsonBuilderHelper.getJsonFromString(data, FlickrResponse.class);
                        mFlickrRespons = flickrResponse;
                        mJsonResponse = data;
//                        mImageResponse.setFlickrResponse(flickrResponse);
//                        initListViewAdapter(mContext);
                        mImageResponse.updateFlickrResponse(flickrResponse);
                        mSwipeRefreshLayout.setEnabled(false);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d(TAG, "Cache Data is NULL");
                }

            }
        });

    }
}

