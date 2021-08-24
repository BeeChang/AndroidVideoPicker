package com.example.androidvideopicker.data.entity;

import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public
class VideoAlbumNameRvItem extends BaseObservable {

    public VideoAlbumNameRvItem(VideoAlbumName videoAlbumName, AlbumNameOnClickListener albumNameListener) {
        this.videoAlbumName = videoAlbumName;
        this.albumNameListener = albumNameListener;
    }

    VideoAlbumName videoAlbumName;
    @Bindable
    public VideoAlbumName getVideoAlbumName() {
        return videoAlbumName;
    }

    public void setVideoAlbumName(VideoAlbumName videoAlbumName) {
        this.videoAlbumName = videoAlbumName;
    }


    public AlbumNameOnClickListener albumNameListener;

    public interface AlbumNameOnClickListener {
        void onAlbumItemClick(View view, VideoAlbumName item , BottomSheetDialogFragment bottomSheetDialogFragment);
    }

    public AlbumNameOnClickListener getAlbumNameListener() {
        return albumNameListener;
    }

    public void setAlbumNameListener(AlbumNameOnClickListener albumNameListener) {
        this.albumNameListener = albumNameListener;
    }

}
