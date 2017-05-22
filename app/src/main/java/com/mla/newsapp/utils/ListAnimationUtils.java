package com.mla.newsapp.utils;

import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.mla.newsapp.R;

/**
 * Created by kumar.samdwar on 18/05/15.
 */
public class ListAnimationUtils {
    private Context mContext;

    public ListAnimationUtils(Context context) {
        mContext = context;
    }

    public Animation animateDownFromTop() {
        Animation animation = AnimationUtils.loadAnimation(mContext,  R.anim.down_from_top);
        return animation;
    }
    public Animation animateUpFromBottom() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.up_from_bottom);
        return animation;
    }
    public Animation animateFadeIn() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_in);
        return animation;
    }

    public Animation animatePushLeftIn() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_left_in);
        return animation;
    }
    public Animation animatePushLeftOut() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.push_left_out);
        return animation;
    }
    public Animation animateSlideInRight() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);
        return animation;
    }

    public Animation animateSlideOutRight() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_out_right);
        return animation;
    }

    public Animation animateWaveScale() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.wave_scale);
        return animation;
    }

    public Animation animateFadeOut() {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out);
        return animation;
    }
}
