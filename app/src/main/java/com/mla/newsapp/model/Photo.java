package com.mla.newsapp.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by kumar.samdwar on 10/05/15.
 */
public class Photo {

    @SerializedName("id")
    public String mID;

    @SerializedName("owner")
    public String mOwner;

    @SerializedName("secret")
    public String mSecret;

    @SerializedName("server")
    public String mServer;

    @SerializedName("farm")
    public String mFarm;

    @SerializedName("title")
    public String mTitle;

    @SerializedName("ispublic")
    public String mIsPublic;

    @SerializedName("isfriend")
    public String mIsFriend;

    @SerializedName("isfamily")
    public String mIsFamily;

    public String getID() {
        return mID;
    }

    public void setID(String mID) {
        this.mID = mID;
    }

    public String getOwner() {
        return mOwner;
    }

    public void setOwner(String mOwner) {
        this.mOwner = mOwner;
    }

    public String getSecret() {
        return mSecret;
    }

    public void setSecret(String mSecret) {
        this.mSecret = mSecret;
    }

    public String getServer() {
        return mServer;
    }

    public void setServer(String mServer) {
        this.mServer = mServer;
    }

    public String getFarm() {
        return mFarm;
    }

    public void setFarm(String mFarm) {
        this.mFarm = mFarm;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getIsPublic() {
        return mIsPublic;
    }

    public void setIsPublic(String mIsPublic) {
        this.mIsPublic = mIsPublic;
    }

    public String getIsFriend() {
        return mIsFriend;
    }

    public void setIsFriend(String mIsFriend) {
        this.mIsFriend = mIsFriend;
    }

    public String getIsFamily() {
        return mIsFamily;
    }

    public void setIsFamily(String mIsFamily) {
        this.mIsFamily = mIsFamily;
    }
    @Override
    public String toString() {
            return "id :" + mID +
                    " owner :" + mOwner+
                    " secret: " + mSecret +
                    " server: " + mServer +
                    " farm:  " + mFarm+
                    " title:  " + mTitle+
                    " ispublic:  " + mIsPublic+
                    " isfriend:  " + mIsFriend+
                    " isfamily:  " + mIsFamily;
    }


}
