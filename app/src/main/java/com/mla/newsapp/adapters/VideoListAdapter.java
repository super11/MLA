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
import com.mla.newsapp.R;
import com.mla.newsapp.model.VideosItem;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;
import com.mla.newsapp.utils.ListAnimationUtils;

import java.util.ArrayList;


/**
 * Created by amit.rs on 13/05/15.
 */
public class VideoListAdapter extends BaseAdapter {

    private ArrayList<VideosItem> videoList;
    private LayoutInflater layoutInflater;
    private ImageLoader mImageLoader;
    private int lastPosition = -1;
    private Context mContext;


    public VideoListAdapter(Context aContext, ArrayList<VideosItem> videoList) {
        mContext=aContext;
        this.videoList = videoList;
        layoutInflater = LayoutInflater.from(aContext);
        mImageLoader =  NetworkRequestQueue.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return videoList.size();
    }

    @Override
    public Object getItem(int position) {
        return videoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.custom_video_row, null);
            holder = new ViewHolder();
            holder.imageView =(NetworkImageView) convertView.findViewById(R.id.video_thumbnail);
            holder.titleView = (TextView) convertView.findViewById(R.id.video_title);
            holder.descriptionView = (TextView) convertView.findViewById(R.id.video_description);
            holder.dateView = (TextView) convertView.findViewById(R.id.video_date);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageView.setImageUrl(videoList.get(position).getThumbnailUrl(),mImageLoader);
        holder.titleView.setText(videoList.get(position).getTitle());
        holder.descriptionView.setText("By, " + videoList.get(position).getDescription());
        holder.dateView.setText(videoList.get(position).getDate());

        mImageLoader.get(videoList.get(position).getThumbnailUrl(), ImageLoader.getImageListener(holder.imageView, R.mipmap.ic_launcher, R.mipmap.ic_launcher));

        //            Animation logic
        int tempPosition = position;
        ListAnimationUtils listAnimationUtils = new ListAnimationUtils(mContext);
        Animation animation;
        if(tempPosition>lastPosition){
            animation=listAnimationUtils.animateUpFromBottom();
        }else{
            animation=listAnimationUtils.animateDownFromTop();
        }

        lastPosition = tempPosition;
        convertView.startAnimation(animation);
        animation.setDuration(430);
        return convertView;
    }

    static class ViewHolder {
        TextView titleView;
        TextView descriptionView;
        TextView dateView;
        NetworkImageView imageView;
    }
}
