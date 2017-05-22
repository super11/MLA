package com.mla.newsapp.model;

import org.json.JSONObject;

/**
 * Created by kumar.samdwar on 11/05/15.
 */
public class JsonPhotoDataProvider {
    Photo mPhoto;
    int mPosition;

    public JSONObject getJsonObject() {
        return mJsonObject;
    }

    public void setJsonObject(JSONObject mJsonObject) {
        this.mJsonObject = mJsonObject;
    }

    private JSONObject mJsonObject;


    public JsonPhotoDataProvider(FlickrResponse flickrResponse, int position) {
        mPosition = position;
        mPhoto = flickrResponse.mFlickrPhoto.getPhoto().get(position);
    }

    public String getImageUrl() {
        //  https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg
        //  String imageUrl = "https://farm{farm-id}.staticflickr.com/{server-id}/{id}_{secret}.jpg";

        String imageUrl = "https://farm" + mPhoto.getFarm() + ".staticflickr.com/" + mPhoto.getServer() + "/" +
                mPhoto.getID() + "_" + mPhoto.getSecret() + ".jpg";
        return imageUrl;
    }

    public String getTitle() {
        String title = mPhoto.getTitle();
        return title;
    }


}
