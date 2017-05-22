package com.mla.newsapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by kumar.samdwar on 10/05/15.
 */
public class FlickrResponse {

    private FlickrResponse mFlickrResponse;

    @SerializedName("photos")
    public FlickrPhoto mFlickrPhoto;

    @SerializedName("stat")
    public String mStat;

    public FlickrPhoto getFlickrPhoto() {
        return mFlickrPhoto;
    }

    public String getStat() {
        return mStat;
    }

    @Override
    public String toString() {

        String photo="[";
        if(mFlickrPhoto!=null) {
            ArrayList<Photo> photoArrayList = mFlickrPhoto.getPhoto();
            for (Photo temp : photoArrayList) {
//            System.out.println(temp);
                photo = photo + "{ id : " + temp.getID() + ", owner: " + temp.getOwner() + ", secret: " + temp.getSecret() +
                        ", server: " + temp.getServer() + ", farm: " + temp.getFarm() +
                        ", title: " + temp.getTitle() +
                        ", ispublic: " + temp.getIsPublic() + ", isfriend: " + temp.getIsFriend() + ", isfamily: " + temp.getIsFamily() + " },";
//                System.out.println(photo);
            }
            photo = photo + "]";

            return "page :" + mFlickrPhoto.getPage() +
                    " pages :" + mFlickrPhoto.getPages() +
                    " perpage :" + mFlickrPhoto.getPerPage() +
                    " total: " + mFlickrPhoto.getTotalPhotos() +
                    " photo: " + photo +
                    " stat:  " + mStat;
        }
        return null;
    }
}
