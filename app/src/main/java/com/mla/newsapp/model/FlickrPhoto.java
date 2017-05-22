package com.mla.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kumar.samdwar on 10/05/15.
 */
public class FlickrPhoto {

    @SerializedName("photos")
    public String mPhotos;

    @SerializedName("page")
    public String mPage;

    @SerializedName("pages")
    public String mPages;

    @SerializedName("perpage")
    public String mPerPage;

    @SerializedName("total")
    public String mTotalPhotos;

    @SerializedName("photo")
    public ArrayList<Photo> mPhoto;

    public ArrayList<Photo> getPhoto() {
        return mPhoto;
    }

    public void setPhoto(ArrayList<Photo> mPhoto) {
        this.mPhoto = mPhoto;
    }

    public String getPhotos() {
        return mPhotos;
    }

    public void setPhotos(String mPhotos) {
        this.mPhotos = mPhotos;
    }

    public String getPage() {
        return mPage;
    }

    public void setPage(String mPage) {
        this.mPage = mPage;
    }

    public String getPages() {
        return mPages;
    }

    public void setPages(String mPages) {
        this.mPages = mPages;
    }

    public String getPerPage() {
        return mPerPage;
    }

    public void setPerPage(String mPerPage) {
        this.mPerPage = mPerPage;
    }

    public String getTotalPhotos() {
        return mTotalPhotos;
    }

    public void setTotalPhotos(String mTotalPhotos) {
        this.mTotalPhotos = mTotalPhotos;
    }



}
