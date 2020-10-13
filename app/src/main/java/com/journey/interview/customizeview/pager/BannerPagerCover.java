package com.journey.interview.customizeview.pager;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.journey.interview.R;

public class BannerPagerCover extends RelativeLayout {

    private Context mContext;
    private ImageView ivCover;
    private TextView tvName;

    public BannerPagerCover(Context context) {
        this(context, null);
    }

    public BannerPagerCover(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerPagerCover(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView() {
        View v = LayoutInflater.from(mContext).inflate(R.layout.imusic_view_banner_pager, this, true);
        ivCover = v.findViewById(R.id.iv_cover);
        tvName = v.findViewById(R.id.tv_name);
    }

    public void setText(String text) {
        tvName.setText(text);
    }

    public void setBannerCover(String url) {
        Glide.with(mContext).load(url).transition(new DrawableTransitionOptions().crossFade()).into(ivCover);
    }
    public void setBannerCover(int resource) {
        ivCover.setImageResource(resource);
    }
}
