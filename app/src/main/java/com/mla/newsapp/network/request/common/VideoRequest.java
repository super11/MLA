package com.mla.newsapp.network.request.common;

import android.app.ProgressDialog;
import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mla.newsapp.adapters.VideoListAdapter;
import com.mla.newsapp.config.AppPreferences;
import com.mla.newsapp.controller.VideoNewsController;
import com.mla.newsapp.model.VideosItem;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by amit.rs on 15/05/15.
 */
public class VideoRequest {

    private ArrayList<VideosItem> video_list = new ArrayList<>();
    private ProgressDialog pDialog;

    public void fetchVideos(Context context, final VideoListAdapter videoAdapter, String drawerList){



        String youtube_url = AppPreferences.YOUTUBE_URL + drawerList;

        pDialog = new ProgressDialog(context);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        //clear the video_list to update with new content
        VideoNewsController.getInstance().clearVideoList();

        JsonObjectRequest videoReq = new JsonObjectRequest(youtube_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        video_list.addAll(VideoNewsController.getInstance().processVideoItem(response)) ;

                        // notifying list adapter about data changes,so that it renders the list view with updated data
                        videoAdapter.notifyDataSetChanged();
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Cache.Entry entry=null;
                //Cache.Entry entry = NetworkRequestQueue.getInstance().getCache().get(AppPreferences.YOUTUBE_URL);
                if (entry != null) {
                    try {
                        JSONObject data=null ;
                        video_list.addAll(VideoNewsController.getInstance().processVideoItem(data)) ;

                        VideoNewsController.getInstance().setVideosItems(video_list);

                        // notifying list adapter about data changes,so that it renders the list view with updated data
                        videoAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });

        // Adding request to request queue
        NetworkRequestQueue.getInstance().addToRequestQueue(videoReq);
    }


    public void fetchMoreVideos(Context context, final VideoListAdapter videoAdapter, String drawerList){

        /*ArrayList<VideosItem> tempData = video_list;
        video_list.addAll(tempData);
        videoAdapter.notifyDataSetChanged();*/

        String youtube_url = AppPreferences.YOUTUBE_URL + drawerList + "&pageToken=" + VideoNewsController.getInstance().getNextPageToken();

        pDialog = new ProgressDialog(context);
        // Showing progress dialog before making http request
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest videoReq = new JsonObjectRequest(youtube_url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        video_list.addAll(VideoNewsController.getInstance().processVideoItem(response)) ;

                        // notifying list adapter about data changes,so that it renders the list view with updated data
                        videoAdapter.notifyDataSetChanged();
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Cache.Entry entry=null;
                //Cache.Entry entry = NetworkRequestQueue.getInstance().getCache().get(AppPreferences.YOUTUBE_URL);
                if (entry != null) {
                    try {
                        JSONObject data=null ;
                        video_list.addAll(VideoNewsController.getInstance().processVideoItem(data)) ;

                        VideoNewsController.getInstance().setVideosItems(video_list);

                        // notifying list adapter about data changes,so that it renders the list view with updated data
                        videoAdapter.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                }
            }
        });

        // Adding request to request queue
        NetworkRequestQueue.getInstance().addToRequestQueue(videoReq);
    }

    }


