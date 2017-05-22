package com.mla.newsapp.network.request.common;



import android.content.Context;
import android.util.Log;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.mla.newsapp.controller.ArticleNewsController;
import com.mla.newsapp.fragments.ArticleFragment;

import org.json.JSONObject;


/**
 * Created by ashokkumar.y on 15/05/15.
 */
public class ArticleNetworkRequest{


   // String             mUrl                   ="http://www.faroo.com/api?q=iphone&l=en&src=news&f=json&key=@aBBIz-CKJTNQshA8yhSbB3xJ1Q_";
    public     String             responseStringData;
    NetworkRequestQueue           queue;
    RequestQueue                  requestQueue;
    Context                       mContext;
    ListView                      mListView;
    //ArticleFragment articleFragment;
    ArticleNewsController articleNewsController;
   // ResponseData   mResponseObject;
    public  void  getArticleNews(Context context,int start,int length,ListView listView, final ArticleFragment articleFragment, final ArticleNewsController articleNewsController, final boolean appendDataOnScroll,String search_key){
           //String  mUrl="http://www.faroo.com/api?q=iphone&l=en&src=news&f=json&key=@aBBIz-CKJTNQshA8yhSbB3xJ1Q_";
        String  mUrl="http://www.faroo.com/api?l=en&src=news&f=json&key=@aBBIz-CKJTNQshA8yhSbB3xJ1Q_";
          // this.articleFragment=articleFragment;
           mContext=context;
           mListView=listView;
           queue = NetworkRequestQueue.getInstance();
           queue.initialize(mContext);
           requestQueue= queue.getRequestQueue();
           mUrl=mUrl+"&start="+start+"&length="+length+"&q="+search_key;
        Log.d("requestUrl " , mUrl);


           final JsonObjectRequest jsonObjRequest = new JsonObjectRequest(Request.Method.GET,mUrl,null,new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                          System.out.println("Inside json request"+response);
                       // ArticleNewsController articleNewsController=new ArticleNewsController();
                        if(appendDataOnScroll==false) {
                            articleNewsController.processResponse(response);
                            articleFragment.setResponseInAdapter(true);
                        }
                        else{
                            articleNewsController.updateResponse(response);

                            articleFragment.setResponseInAdapter(false);
                           // articleFragment.getArticleNewsAdapter().notifyDataSetChanged();
                           // articleNewsAdapter.notifyDataSetChanged();
                        }
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                       Toast.makeText(mContext, error.toString(), Toast.LENGTH_LONG).show();
                        System.out.println("Inside json request"+error);

                    }
                });
        requestQueue.add(jsonObjRequest);
    }


}
