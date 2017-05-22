package com.mla.newsapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.mla.newsapp.R;
import com.mla.newsapp.controller.CommentsCtrl;
import com.mla.newsapp.network.request.common.NetworkRequestQueue;
import com.mla.newsapp.network.responseModels.CommentItem;

/**
 * Created by manish.patwari on 5/18/15.
 */
public class CommentAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater mInflater;
    CommentsCtrl mCommentCtrl;
    ImageLoader mImageLoader;

    public CommentAdapter(Context context, LayoutInflater inflater,CommentsCtrl commentsCtrl) {
        mContext = context;
        mInflater = inflater;
        mCommentCtrl = commentsCtrl;
        mImageLoader = NetworkRequestQueue.getInstance().getImageLoader();
    }

    @Override
    public int getCount() {
        return mCommentCtrl.getCommentItemsLength();
    }

    @Override
    public Object getItem(int position) {
        return mCommentCtrl.getCommentAtPosition(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        // check if the view already exists
        // if so, no need to inflate and findViewById again!
        if (convertView == null) {

            // Inflate the custom row layout from your XML.
            convertView = mInflater.inflate(R.layout.comment_items, null);

            // create a new "Holder" with subviews
            holder = new ViewHolder();
            holder.thumbnailImageView = (ImageView) convertView.findViewById(R.id.user_img);
            holder.timeTextView = (TextView) convertView.findViewById(R.id.comment_time);
            holder.authorTextView = (TextView) convertView.findViewById(R.id.user_name);
            holder.commentText = (TextView) convertView.findViewById(R.id.comment_item_text);

            // hang onto this holder for future recyclage
            convertView.setTag(holder);
        } else {

            // skip all the expensive inflation/findViewById
            // and just get the holder you already made
            holder = (ViewHolder) convertView.getTag();
        }


        // Get the current book's data in JSON form
        CommentItem commentItem = (CommentItem) getItem(position);

        // Grab the title and author from the JSON
        String commentBy = "";
        String commentTime = "";
        String commentText = "";

        commentBy = commentItem.getCommentBy();
        commentTime = commentItem.getCommentTime();
        commentText = commentItem.getCommentText();

        // Send these Strings to the TextViews for display
        holder.timeTextView.setText(commentTime);
        holder.authorTextView.setText(commentBy);
        holder.commentText.setText(commentText);


        // See if there is a cover ID in the Object
        if (commentItem.getImgURL() != null) {


            String imageURL = commentItem.getImgURL();


            mImageLoader.get(imageURL, ImageLoader.getImageListener(holder.thumbnailImageView,
                    R.mipmap.ic_launcher, R.mipmap.ic_launcher));

            // Use Picasso to load the image
            // Temporarily have a placeholder in case it's slow to load
            // Picasso.with(mContext).load(imageURL).placeholder(R.mipmap.img_books_loading).into(holder.thumbnailImageView);
        } else {
            // If there is no cover ID in the object, use a placeholder
            holder.thumbnailImageView.setImageResource(R.mipmap.ic_launcher);
        }

        return convertView;
    }

    public void updateData() {
        // update the adapter's dataset
        notifyDataSetChanged();
    }
    // this is used so you only ever have to do
    // inflation and finding by ID once ever per View
    private static class ViewHolder {
        public ImageView thumbnailImageView;
        public TextView timeTextView;
        public TextView authorTextView;
        public TextView commentText;
    }
}
