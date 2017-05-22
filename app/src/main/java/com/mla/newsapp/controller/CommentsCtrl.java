package com.mla.newsapp.controller;

import com.mla.newsapp.network.responseModels.CommentItem;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by manish.patwari on 5/18/15.
 */
public class CommentsCtrl {

    private ArrayList<CommentItem> mCommentItems;
    private Listeners mListener;


    public CommentsCtrl(Listeners listener){
        mCommentItems = new ArrayList<CommentItem>();
        mListener = listener;
    }

    public CommentItem getCommentAtPosition(int position){
        int totalElements = getCommentItemsLength() -1;
        int reversePosition = totalElements - position;
        return mCommentItems.get(reversePosition);
    }

    public int getCommentItemsLength()
    {
        return mCommentItems.size();
    }

    public void postComment(String commentText,String commentBy,String imgURL)
    {
        CommentItem item = new CommentItem();
        item.setCommentText(commentText);
        item.setCommentBy(commentBy);
        item.setImgURL(imgURL);
        item.setCommentTime(new Date().toString());
        mCommentItems.add(item);
        mListener.dataUpdated();
    }

    public interface Listeners{
        public void dataUpdated();
    }

}
