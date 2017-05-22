package com.mla.newsapp.views;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.mla.newsapp.R;
import com.mla.newsapp.adapters.CommentAdapter;
import com.mla.newsapp.controller.CommentsCtrl;

/**
 * Created by manish.patwari on 5/18/15.
 */
public class Comments extends LinearLayout {

    private Context mContext;
    private Button mPostBtn;
    private EditText mCommentText;
    private CommentsCtrl mCommentsCtrl;
    private CommentAdapter mCommentAdapter;
    private ListView mCommentList;

    public Comments(Context context) {
        super(context);
        mContext = context;
        initialize();
    }

    public Comments(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initialize();
    }

    public Comments(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initialize();
    }

    public Comments(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        initialize();
    }


    private void postComment() {
        String commentText = mCommentText.getText().toString();
        if (commentText.trim().isEmpty()) {
            Toast.makeText(mContext,"Please enter comment",Toast.LENGTH_SHORT);
        } else {
            mCommentsCtrl.postComment(commentText, "Manish Patwari", "http://lh6.googleusercontent.com/-K5iaLXoeMmw/AAAAAAAAAAI/AAAAAAAAABE/iQIZJkprsPk/s48-c-k-no/photo.jpg");
            mCommentText.setText("");
            //Keyboard Close
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(mContext.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mCommentText.getWindowToken(), 0);
        }
    }

    public int getNumberOfComments(){
        return mCommentsCtrl.getCommentItemsLength();
    }


    private void initialize()
    {

        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View mCommentLayout = inflater.inflate(R.layout.comment_layout,this);

            mCommentText = (EditText) mCommentLayout.findViewById(R.id.comment_text);
        mPostBtn = (Button) mCommentLayout.findViewById(R.id.comment_post_btn);
        mCommentList = (ListView) mCommentLayout.findViewById(R.id.comment_list);

        mCommentsCtrl = new CommentsCtrl(new CommentsCtrl.Listeners() {
            @Override
            public void dataUpdated() {
                mCommentAdapter.updateData();
            }
        });

        mCommentAdapter = new CommentAdapter(mContext,inflater,mCommentsCtrl);

        mCommentList.setAdapter(mCommentAdapter);

        mPostBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                postComment();
            }
        });


        mCommentText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.toString().substring(start).contains("\n")) {
                    postComment();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        /*mCommentList.setOnTouchListener(new OnTouchListener() {
            // Setting on Touch Listener for handling the touch inside ScrollView
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });*/
    }


}
