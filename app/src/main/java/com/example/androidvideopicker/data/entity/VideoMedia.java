package com.example.androidvideopicker.data.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public
class VideoMedia extends BaseObservable {

    private String contentUri;
    private String id;
    //    아래 data는 api28이하만 들어간다
    private String data;
    private String mimeType;
    private String width;
    private String height;
    private String size;
    private String displayName;
    private String dateAdded;
    private String title;
    //    아래의 3개는 api29이상일 경우만 들어간다
    private String duration;
    private String bucketDisplayName;
    //    29미만일 경우 전체경로 , 29이상일경우 버켓아이디가 들어간다
    private String bucketId;

    /**
     * 버전에 따라서 해당값이 없는 경우 "NULL" 로 추가
     *
     * @param contentUri
     * @param id                MediaStore.Files.FileColumns._ID
     * @param data              MediaStore.MediaColumns.DATA
     * @param mimeType          MediaStore.MediaColumns.MIME_TYPE
     * @param width             MediaStore.MediaColumns.WIDTH
     * @param height            MediaStore.MediaColumns.HEIGHT
     * @param size              MediaStore.MediaColumns.SIZE
     * @param displayName       MediaStore.MediaColumns.DISPLAY_NAME
     * @param dateAdded         MediaStore.MediaColumns.DATE_ADDED
     * @param title             MediaStore.MediaColumns.TITLE
     * @param duration          MediaStore.MediaColumns.DURATION
     * @param bucketDisplayName MediaStore.MediaColumns.BUCKET_DISPLAY_NAME
     * @param bucketId          MediaStore.MediaColumns.BUCKET_ID
     */
    public VideoMedia(String contentUri, String id, String data,
                      String mimeType, String width, String height,
                      String size, String displayName, String dateAdded,
                      String title, String duration, String bucketDisplayName, String bucketId) {
        this.contentUri = contentUri;
        this.id = id;
        this.data = data;
        this.mimeType = mimeType;
        this.width = width;
        this.height = height;
        this.size = size;
        this.displayName = displayName;
        this.dateAdded = dateAdded;
        this.title = title;
        this.duration = duration;
        this.bucketDisplayName = bucketDisplayName;
        this.bucketId = bucketId;
    }

    public String getContentUri() {
        return contentUri;
    }

    public void setContentUri(String contentUri) {
        this.contentUri = contentUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Bindable
    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getBucketDisplayName() {
        return bucketDisplayName;
    }

    public void setBucketDisplayName(String bucketDisplayName) {
        this.bucketDisplayName = bucketDisplayName;
    }

    public String getBucketId() {
        return bucketId;
    }

    public void setBucketId(String bucketId) {
        this.bucketId = bucketId;
    }

}
