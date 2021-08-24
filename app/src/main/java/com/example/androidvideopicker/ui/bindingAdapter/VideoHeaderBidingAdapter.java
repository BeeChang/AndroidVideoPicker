package com.example.androidvideopicker.ui.bindingAdapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

public
class VideoHeaderBidingAdapter {

    @BindingAdapter("header_onBackImageClickListener")
    public static void setOnBackImageClickListener(ImageView imageView, View.OnClickListener onClickListener) {
        imageView.setOnClickListener(onClickListener);
    }

    @BindingAdapter("header_onAlbumClickListener")
    public static void setOnAlbumClickListener(ImageView imageView, View.OnClickListener onClickListener) {
        imageView.setOnClickListener(onClickListener);
    }

    @BindingAdapter("header_onAlbumClickListener")
    public static void setOnAlbumClickListener(TextView textView, View.OnClickListener onClickListener) {
        textView.setOnClickListener(onClickListener);
    }
}
