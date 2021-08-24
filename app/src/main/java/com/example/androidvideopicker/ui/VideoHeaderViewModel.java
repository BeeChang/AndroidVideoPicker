package com.example.androidvideopicker.ui;


import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public
class VideoHeaderViewModel extends BaseObservable {

    public View.OnClickListener onBackImageClickListener;
    @Bindable
    public View.OnClickListener getOnBackImageClickListener() {
        return onBackImageClickListener;
    }

    public void setOnBackImageViewClickListener(View.OnClickListener onClickListener) {
        this.onBackImageClickListener = onClickListener;
        notifyPropertyChanged(BR.onBackImageClickListener);
    }

    public String title;
    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }


    public View.OnClickListener onAlbumClickListener;

    @Bindable
    public View.OnClickListener getOnAlbumClickListener() {
        return onAlbumClickListener;
    }

    public void setOnAlbumClickListener(View.OnClickListener onAlbumClickListener) {
        this.onAlbumClickListener = onAlbumClickListener;
        notifyPropertyChanged(BR.onAlbumClickListener);
    }



}
