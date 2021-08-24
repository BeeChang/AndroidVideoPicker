package com.example.androidvideopicker.data.repository;

import android.app.Application;

import com.example.androidvideopicker.data.entity.VideoAlbumName;
import com.example.androidvideopicker.data.entity.VideoMedia;
import com.example.androidvideopicker.data.local.LocalMediaVideoSource;
import com.example.androidvideopicker.utils.DeviceVersionCheck;

import java.util.ArrayList;

public
class VideoRepository {

    private  LocalMediaVideoSource localMediaVideoSource;

    private  Application application;

    private  RepositoryReturnDataListener returnListener;

    private  DeviceVersionCheck deviceVersionCheck;


    public VideoRepository(LocalMediaVideoSource localMediaVideoSource, Application application,
                           RepositoryReturnDataListener repositoryReturnDataListener, DeviceVersionCheck deviceVersionCheck) {
        this.localMediaVideoSource = localMediaVideoSource;
        this.application = application;
        this.returnListener = repositoryReturnDataListener;
        this.deviceVersionCheck = deviceVersionCheck;
    }


    public void getLocalMediaVideoInfomation() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<VideoMedia> returnVideoList;

                if (deviceVersionCheck.isVersionUpper29()) {
                    returnVideoList = localMediaVideoSource.getVideoInfoUpper29(application);
                } else {
                    returnVideoList = localMediaVideoSource.getVideoInfoUnder28(application);
                }

                if (returnVideoList != null) {
                    if (returnVideoList.size() > 0) {
                        returnListener.successReturn("videoInformation", returnVideoList);
                    } else {
                        returnListener.failReturn("videoInformation", null);
                    }
                } else {
                    returnListener.failReturn("videoInformation", null);
                }

            }
        }).start();

    }

    public void getLocalMediaVideoName() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<VideoAlbumName> returnVideoList;

                if (deviceVersionCheck.isVersionUpper29()) {
                    returnVideoList = localMediaVideoSource.getVideoDirectoryUpper29(application);
                } else {
                    returnVideoList = localMediaVideoSource.getVideoDirectoryUnder28(application);
                }

                if (returnVideoList != null) {
                    if (returnVideoList.size() > 0) {
                        returnListener.successReturn("videoAlbumName", returnVideoList);
                    } else {
                        returnListener.failReturn("videoAlbumName", null);
                    }
                } else {
                    returnListener.failReturn("videoAlbumName", null);
                }

            }
        }).start();

    }

}
