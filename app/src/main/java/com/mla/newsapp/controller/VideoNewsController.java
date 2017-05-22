package com.mla.newsapp.controller;

import com.mla.newsapp.model.VideosItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by amit.rs on 16/05/15.
 */
public class VideoNewsController {

    private static VideoNewsController instance;

    private ArrayList<VideosItem> video_list = new ArrayList<>();
    private static String nextPageToken;

    public static VideoNewsController getInstance(){
        if(instance == null){
            synchronized (VideoNewsController.class){
                if(instance == null){
                    instance = new VideoNewsController();
                }
            }
        }
        return instance;
    }

    public ArrayList<VideosItem> processVideoItem(JSONObject response) {
        JSONArray urlItems=null;


        try{
            setNextPageToken(response.getString("nextPageToken"));
            urlItems=(JSONArray) response.get("items");
        }catch (JSONException e){
            e.printStackTrace();
        }
        // Parsing json
        for (int i = 0; i < urlItems.length(); i++) {
            try {

                JSONObject obj = urlItems.getJSONObject(i);
                VideosItem videoData = new VideosItem();
                videoData.setTitle(obj.getJSONObject("snippet").getString("title"));
                videoData.setThumbnailUrl(obj.getJSONObject("snippet").getJSONObject("thumbnails").getJSONObject("default").getString("url"));
                videoData.setDescription(obj.getJSONObject("snippet").getString("description"));
                videoData.setDate(obj.getJSONObject("snippet").getString("publishedAt"));
                videoData.setVideoID(obj.getJSONObject("id").getString("videoId"));

                video_list.add(videoData);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        setVideosItems(video_list);

        return video_list;
    }


    public void setVideosItems(ArrayList<VideosItem> videosItems) {

        video_list=videosItems;

    }

    public ArrayList<VideosItem> getVideosItems(){
        return video_list;
    }

    public void clearVideoList(){
        video_list.clear();
    }



    public String getNextPageToken() {
        return nextPageToken;
    }

    public void setNextPageToken(String token){
        nextPageToken=token;

    }
}
