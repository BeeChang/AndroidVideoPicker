package com.example.androidvideopicker.data.entity;

import androidx.databinding.BaseObservable;

public
class VideoAlbumName extends BaseObservable {

    String eniquePathOrId;
    String albumName;

    public VideoAlbumName(String eniquePathOrId, String albumName) {
        this.eniquePathOrId = eniquePathOrId;
        this.albumName = albumName;
    }


    public String getEniquePathOrId() {
        return eniquePathOrId;
    }

    public void setEniquePathOrId(String eniquePathOrId) {
        this.eniquePathOrId = eniquePathOrId;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

}
