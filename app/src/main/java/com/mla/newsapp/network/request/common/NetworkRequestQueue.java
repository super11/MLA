package com.mla.newsapp.network.request.common;

import android.app.Application;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.mla.newsapp.utils.LruBitmapCache;

import org.json.JSONObject;

/**
 * Created by manish.patwari on 5/10/15.
 */
public class NetworkRequestQueue extends Application {


    private VolleyJsonObjectResponseListener mVolleyJsonObjectResponseListener;
    private VolleyStringResponseListener mVolleyStringResponseListener;
    private RequestQueue mRequestQueue;
    private static NetworkRequestQueue instance;

    private NetworkRequestQueue() {
    }

    private ImageLoader mImageLoader;
    private Context mContext;

    public static NetworkRequestQueue getInstance() {
        if (instance == null) {
            synchronized (NetworkRequestQueue.class) {
                if (instance == null) {
                    instance = new NetworkRequestQueue();
                }
            }
        }
        return instance;
    }

    //A key concept is that the RequestQueue must be instantiated with the Application context,
    // not an Activity context.
    // This ensures that the RequestQueue will last for the lifetime of your app,
    // instead of being recreated every time the activity is recreated.
    public NetworkRequestQueue initialize(Context context) {

        // Instantiate the cache
        // Cache cache = new DiskBasedCache(mContext.getCacheDir(), 1024 * 1024); // 1MB cap

        // Set up the network to use HttpURLConnection as the HTTP client.
        // BasicNetwork network = new BasicNetwork(new HurlStack());

        // Instantiate the RequestQueue with the cache and network.
        // mRequestQueue = new RequestQueue(cache, network);
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(mContext); // 1MB cap
        // Start the queue
        mRequestQueue.start();

        mImageLoader = new ImageLoader(mRequestQueue, new LruBitmapCache(LruBitmapCache.getCacheSize(mContext.getApplicationContext())));

        return instance;
    }

    public void setVolleyJsonObjectResponseListener(VolleyJsonObjectResponseListener volleyJsonObjectResponseListener) {
        mVolleyJsonObjectResponseListener = volleyJsonObjectResponseListener;

    }

    public void setVolleyStringResponseListener(VolleyStringResponseListener volleyStringResponseListener) {
        mVolleyStringResponseListener = volleyStringResponseListener;
    }

    public RequestQueue getRequestQueue() {
        return mRequestQueue;
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }

    public void makeJsonObjectRequest(String url) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url + "&page=2", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        mVolleyJsonObjectResponseListener.onVolleyResponse(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mVolleyJsonObjectResponseListener.onVolleyErrorResponse(error);

            }
        });
        mRequestQueue.add(jsonObjectRequest);
    }

    public void makeStringRequest(String url) {

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mVolleyStringResponseListener.onVolleyResponse(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mVolleyStringResponseListener.onVolleyErrorResponse(error);
            }
        });
        mRequestQueue.add(stringRequest);
    }


    // Add the request to the RequestQueue.
    public <T> void addToRequestQueue(Request<T> req) {
        mRequestQueue.add(req);
    }


    public void cancelAllRequest(String tag) {
        mRequestQueue.cancelAll(tag);
    }

    public void destroy() {
        mRequestQueue.stop();
        mRequestQueue = null;
        instance = null;

        mImageLoader = null;
    }

    public interface VolleyJsonObjectResponseListener {
        void onVolleyResponse(JSONObject jsonObject);

        void onVolleyErrorResponse(VolleyError volleyError);
    }

    public interface VolleyStringResponseListener {
        void onVolleyResponse(String response);

        void onVolleyErrorResponse(VolleyError volleyError);
    }
}
