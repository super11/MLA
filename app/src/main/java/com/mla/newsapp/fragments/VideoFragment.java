package com.mla.newsapp.fragments;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mla.newsapp.PlayerActivity;
import com.mla.newsapp.R;
import com.mla.newsapp.VideoGalleryActivity;
import com.mla.newsapp.adapters.VideoListAdapter;
import com.mla.newsapp.config.Constants;
import com.mla.newsapp.controller.VideoNewsController;
import com.mla.newsapp.listener.EndlessListScrollListener;
import com.mla.newsapp.model.VideosItem;
import com.mla.newsapp.network.request.common.VideoRequest;
import com.mla.newsapp.utils.DemoUtil;

import java.util.ArrayList;


/**
 * Created by amit.rs on 11/05/15.
 */
public class VideoFragment extends Fragment {



     private VideoListAdapter videoAdapter;
     private String default_drawer_list = "India";

     private VideoRequest videoRequest;

     private ArrayList<VideosItem> video_list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        video_list =VideoNewsController.getInstance().getVideosItems();

        final ListView videoList = (ListView)rootView.findViewById(R.id.video_list);
        videoAdapter = new VideoListAdapter(getActivity().getApplicationContext(),video_list);
        videoList.setAdapter(videoAdapter);
        videoList.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Toast.makeText(getActivity().getApplicationContext(), "You selected : " + (position + 1) + " item", Toast.LENGTH_SHORT).show();

                Intent mpdIntent = new Intent(getActivity().getApplicationContext(), VideoGalleryActivity.class)
                        .setData(Uri.parse(""))
                        .putExtra(PlayerActivity.CONTENT_ID_EXTRA, "")
                        .putExtra("ListItemPosition", position)
                        .putExtra("TotalNumberOfItems", videoAdapter.getCount())
                        .putExtra(PlayerActivity.CONTENT_TYPE_EXTRA, DemoUtil.TYPE_MP4);

                startActivity(mpdIntent);


            }
        });
        videoList.setOnScrollListener(new EndlessListScrollListener() {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                Log.d("Lazy", "Loading more data... page = " + totalItemsCount);
                videoRequest.fetchMoreVideos(getActivity(),videoAdapter, default_drawer_list);

            }
        });

        // Register to receive messages.
        // We are registering an observer (mMessageReceiver) to receive Intents
        // with actions name
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).registerReceiver(mMessageReceiver,
                new IntentFilter(Constants.IntentActionType.CATEGORY_CHANGE.toString()));


// TODO : remove as controller
        videoRequest =new VideoRequest();
        videoRequest.fetchVideos(getActivity(),videoAdapter, default_drawer_list);

        return rootView;
    }


    // Our handler for received Intents. This will be called whenever an Intent
// with an action named "custom-event-name" is broad casted.
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            // Get extra data included in the Intent
            String drawerList = intent.getStringExtra(Constants.IntentExtraKey.CATEGORY.toString());
            Log.d("receiver", "Got message: " + drawerList);
            videoRequest.fetchVideos(getActivity(),videoAdapter, drawerList);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();

        //unregister your receiver
        LocalBroadcastManager.getInstance(getActivity().getApplicationContext()).unregisterReceiver(mMessageReceiver);
    }
}