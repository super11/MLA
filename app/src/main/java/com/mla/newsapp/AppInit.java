package com.mla.newsapp;

import android.content.Context;

import com.mla.newsapp.config.AppPreferences;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;

/**
 * Created by manish.patwari on 5/10/15.
 */
public class AppInit {
    private AppPreferences mAppPreferences;
    private NetworkRequestQueue mNetworkRequestQueue;
    private static AppInit instance;

    private AppInit(){};

    public static AppInit getInstance(){
        if(instance == null){
            synchronized (AppInit.class){
                if(instance == null){
                    instance = new AppInit();
                }
            }
        }
        return instance;
    }

    public AppInit initialize(Context context) {

        // App Preferences
        mAppPreferences = AppPreferences.getInstance().initialize(context);

        //Start Volley
        mNetworkRequestQueue = NetworkRequestQueue.getInstance().initialize(context);

        return instance;
    }

    public void destroy()
    {
        mAppPreferences.destroy();
        mNetworkRequestQueue.destroy();
    }


}
