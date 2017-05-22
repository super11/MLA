package com.mla.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.mla.newsapp.ArticleNewsListener.AdapterListener;
import com.mla.newsapp.R;
import com.mla.newsapp.controller.ArticleNewsController;
import com.mla.newsapp.model.ResponseData;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;
import com.mla.newsapp.utils.ListAnimationUtils;

/**
 * Created by ashokkumar.y on 15/05/15.
 */
public class ArticleNewsAdapter extends BaseAdapter implements AdapterListener {

    Context mContext;
    ResponseData mResponse;
    // RequestQueue        mRequestQueue ;
    ImageLoader mImageLoader;
    NetworkRequestQueue mQueue;
   ArticleNewsController articleNewsController;
    private int lastPosition = -1;


    //constructor
    // public ArticleNewsAdapter(Context context,  ResponseData mResponse){
    public ArticleNewsAdapter(Context context) {
        this.mContext = context;
        articleNewsController=ArticleNewsController.getInstance();
        this.mResponse = articleNewsController.getResponseObject();
        mQueue = NetworkRequestQueue.getInstance();
        mQueue.initialize(context);
        // mRequestQueue=mQueue.getRequestQueue();
        mImageLoader = mQueue.getImageLoader();
        articleNewsController.registerDataSourceListener(ArticleNewsAdapter.this);

    }

   /* //constructor
    public ArticleNewsAdapter(Context context) {
        this.mContext = context;


    }*/


    @Override
    public int getCount() {
        return mResponse.getNewsData().size();
    }

    @Override
    public Object getItem(int position) {
        return mResponse.getNewsData().get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View rowItem = convertView;
        mViewHolder holder;
        if (rowItem == null) {
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(mContext.LAYOUT_INFLATER_SERVICE);
            rowItem = inflater.inflate(R.layout.article_item_list_view, viewGroup, false);
            holder = new mViewHolder(rowItem);
            rowItem.setTag(holder);
        } else {
            holder = (mViewHolder) rowItem.getTag();
        }
        //setting text to  Text View for title
        holder.articleTitle.setText(mResponse.getNewsData().get(position).getTitle());
        if (mResponse.getNewsData().get(position).getImageUrl() != "") {
            //setting image to  Image View for image
            holder.articleImageView.setImageUrl(mResponse.getNewsData().get(position).getImageUrl(), mImageLoader);
        } else {
            holder.articleImageView.setImageUrl("https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQSRh1LEkGycmDCVeK5A7W3VL3EVgXfnsoPUGPKSR50eCBqHu_-gQ", mImageLoader);
        }
        int mPosition =position;
        ListAnimationUtils listAnimationUtils = new ListAnimationUtils(mContext);
        Animation animation;
        if(mPosition>lastPosition){
            animation=listAnimationUtils.animateUpFromBottom();
        }else{
            animation=listAnimationUtils.animateDownFromTop();
        }

        lastPosition = position;
//        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        rowItem.startAnimation(animation);
        animation.setDuration(430);
        return rowItem;

    }

    @Override
    public void notifyListener() {
        this.notifyDataSetChanged();
    }
}
class mViewHolder{
    TextView articleTitle;
    NetworkImageView articleImageView;
    mViewHolder(View v){
        articleTitle= (TextView) v.findViewById(R.id.titleView);
        articleImageView= (NetworkImageView) v.findViewById(R.id.imageView);

    }

}
