package com.example.androidvideopicker.data.entity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.library.baseAdapters.BR;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.signature.MediaStoreSignature;

import com.example.androidvideopicker.R;

public
class VideoRvItem extends BaseObservable{

    public VideoRvItem(VideoMedia videoMedia, boolean isSelected ,
                       OnVideoListItemClickListener onVideoItemClickListener) {
        this.videoMedia = videoMedia;
        this.isSelected = isSelected;
        this.onVideoItemClickListener = onVideoItemClickListener;
    }

    VideoMedia videoMedia;

    @Bindable
    public VideoMedia getVideoMedia() {
        return videoMedia;
    }


    public void setVideoMedia(VideoMedia videoMedia) {
        this.videoMedia = videoMedia;
        notifyPropertyChanged(BR.videoMedia);
    }

    boolean isSelected;

    @Bindable
    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        this.isSelected = selected;
        notifyPropertyChanged(BR.selected);
    }

    @BindingAdapter("glideLoadImage")
    public static void glideLoadImage(ImageView imageView, String path) {
        Glide.with(imageView.getContext())
                .load(  path)
//                .signature(new MediaStoreSignature(mimeType, dateModified, orientation))
                .error(R.drawable.ic_launcher_foreground)
                .centerCrop()
                .into(imageView);
    }



    public OnVideoListItemClickListener onVideoItemClickListener;

     public interface OnVideoListItemClickListener{
        void onItemClick(View view,VideoRvItem item , int position);
    }

    public OnVideoListItemClickListener getOnVideoItemClickListener() {
        return onVideoItemClickListener;
    }

    public void setOnVideoItemClickListener(OnVideoListItemClickListener onVideoItemClickListener) {
        this.onVideoItemClickListener = onVideoItemClickListener;
    }
}
