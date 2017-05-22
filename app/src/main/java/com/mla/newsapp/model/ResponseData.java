package com.mla.newsapp.model;

import android.widget.ListView;

import com.google.gson.annotations.SerializedName;

import java.io.CharArrayWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashokkumar.y on 13/05/15.
 */
public  class ResponseData {
    @SerializedName("results")
    public    List<RequireData> newsData= new ArrayList<RequireData>();
//    public static List<RequireData> allNewsData= new ArrayList<RequireData>();

  /*  public static List<RequireData> getAllNewsData() {
        return allNewsData;
    }

    public static void setAllNewsData(List<RequireData> allNewsData) {
        ResponseData.allNewsData = allNewsData;
    }*/

    public void setNewsData(List<RequireData> newsData) {
        this.newsData = newsData;
    }

    public List<RequireData> getNewsData() {
        return newsData;
    }

   /* @Override
    public String toString(){
        String data = "";
        for(Topics temp:topics){
            data = data+temp.getTitle() +"\n"+temp.getImageUrl()+"\n"+temp.getDescription();
        }
        return data;
    }*/
}
