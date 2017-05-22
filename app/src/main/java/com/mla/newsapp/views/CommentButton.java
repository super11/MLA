package com.mla.newsapp.views;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.mla.newsapp.R;

/**
 * Created by manish.patwari on 5/21/15.
 */
public class CommentButton extends LinearLayout {
    private Context mContext;
    private Boolean isDialogBoxCreated = false;
    private  Dialog dialog;
    private Comments mComments;
    Button mCommentBtn;
    String mCommentBtnText = " Comments";
    public CommentButton(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public CommentButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public CommentButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    public CommentButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        init();
    }

    public void init(){

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT);
         mCommentBtn = new Button(mContext);

        mCommentBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.comment,0,0,0);

        mCommentBtn.setLayoutParams(params);
        mCommentBtn.setPadding(25,30,25,15);
        mCommentBtn.setText(mCommentBtnText + " (0)");
        addView(mCommentBtn);
        mCommentBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDialogBoxCreated) {
                    createCommentDialog();
                }
                dialog.show();
            }
        });

    }

    public void createCommentDialog(){
        dialog = new Dialog(mContext, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen);
        dialog.setContentView(R.layout.comment_dialog);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        dialog.show();

        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (keyEvent.getKeyCode() ==  KeyEvent.KEYCODE_BACK) {
                    updateCommentCount();
                }
                return false;
            }
        });
        ImageButton closeDialogButton = (ImageButton) dialog.findViewById(R.id.close_button);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateCommentCount();
                dialog.dismiss();
            }
        });

        isDialogBoxCreated = true;
    }

    private void updateCommentCount(){
        if(mComments == null)
        {
            mComments =(Comments) dialog.findViewById(R.id.comments_layout);
        }
        mCommentBtn.setText(mCommentBtnText +" ("+mComments.getNumberOfComments()+")");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
           updateCommentCount();
          //  return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
