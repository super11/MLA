package com.mla.newsapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ashokkumar.y on 13/05/15.
 */
public class RequireData {
    @SerializedName("title")
    public String title;
    @SerializedName("iurl")
    public String imageUrl;
    @SerializedName("kwic")
    public String shortDescription;
    @SerializedName("url")
    public String contentUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getShortDescription() {
        return shortDescription;
    }
    public String getContentUrl() {
        return contentUrl;
    }

}
