package com.mla.newsapp.model;

/**
 * Created by amit.rs on 13/05/15.
 */
public class VideosItem {

    private String title;
    private String description;
    private String date;
    private String thumbnailUrl;
    private  String videoID;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public void setVideoID(String videoID){this.videoID = videoID;}

    public String getVideoID(){return videoID;}



}
