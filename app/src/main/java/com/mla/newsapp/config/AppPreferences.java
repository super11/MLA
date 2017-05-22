package com.mla.newsapp.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.mla.newsapp.network.request.common.NetworkRequestQueue;

/**
 * Created by manish.patwari on 5/8/15.
 */
public class AppPreferences {

    private static final String PACKAGE_NAME = "com.mla.newsapp";

    // Google Console APIs developer key
    public static final String YOUTUBE_URL = "https://www.googleapis.com/youtube/v3/search?part=snippet&maxResults=25&type=video&key=AIzaSyAy_mjrGdkTjmBumgnKdqsvWGi-fE8vVkQ&q=";

    private static AppPreferences instance;
    private SharedPreferences sharedPreferences;
    public static String FLICKR_API_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.search&api_key=5d96cc677f6446044c8bd880b943549b&per_page=15&format=json&nojsoncallback=1&safe_search=true";

    private AppPreferences(){};

    public static AppPreferences getInstance(){
        if(instance == null){
            synchronized (AppPreferences.class){
                if(instance == null){
                    instance = new AppPreferences();
                }
            }
        }
        return instance;
    }

    public AppPreferences initialize(Context context) {
        sharedPreferences = context.getSharedPreferences(PACKAGE_NAME, Context.MODE_PRIVATE);

        //Start Volley
        NetworkRequestQueue.getInstance().initialize(context);

        return instance;
    }

    private static String getKey(String initialKey){
        return PACKAGE_NAME + "." + initialKey;
    }

    private final String  EMAIL_ID                      =   getKey("emailId");
    private final String  USER_NAME                     =   getKey("userName");


    public Boolean isInitialized(){
        if(instance != null && sharedPreferences!= null)
        {
            return  true;
        }
        return false;
    }
    public void saveEmailId(String emailId) {
        if(!isInitialized() || emailId == null || emailId.trim() == "") return;
        this.sharedPreferences.edit().putString(EMAIL_ID, emailId).commit();
    }


    public String getEmailId() {
        if(!isInitialized()) return null;
        return this.sharedPreferences.getString(EMAIL_ID, null);
    }

    public void saveUserName(String userName) {
        if(!isInitialized() || userName == null || userName.trim() == "") return;
        this.sharedPreferences.edit().putString(USER_NAME, userName).commit();
    }

    public String getUserName() {
        if(!isInitialized()) {
            return null;
        }
        return this.sharedPreferences.getString(USER_NAME, "Guest");
    }

    public void destroy()
    {
        instance = null;
        sharedPreferences = null;
    }


}
