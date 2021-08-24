package com.example.androidvideopicker.ui;

import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ViewModel;

import com.example.androidvideopicker.utils.VideoPlayerLocalPicker;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public
class VideoPlayerLocalViewModel extends ViewModel implements LifecycleObserver {

    public VideoPlayerLocalPicker videoPlayerLocalPicker ;

    public ObservableField<Boolean> playButtonLiveData ;

    public ObservableField<Boolean> isErrorMsgLiveData ;

    public VideoPlayerLocalViewModel() {
        playButtonLiveData = new ObservableField<>();
        isErrorMsgLiveData = new ObservableField<>();
        playButtonLiveData.set(false);
        isErrorMsgLiveData.set(false);
        videoPlayerLocalPicker = new VideoPlayerLocalPicker(playButtonLiveData , isErrorMsgLiveData);
        videoPlayerLocalPicker.setPlayBtnLiveData(playButtonLiveData);
    }


    public void init(Context context , PlayerView playerView){
        videoPlayerLocalPicker.setSimpleExoPlayer(new SimpleExoPlayer.Builder(context).build() , playerView);
        videoPlayerLocalPicker.setPlayBtnLiveData(playButtonLiveData);
    }

    public void playViewRatioChange(){
        videoPlayerLocalPicker.ratioChange();
    }

    public void replayOrPause() {
        videoPlayerLocalPicker.replayOrPause();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    public void onResume() {
        videoPlayerLocalPicker.reStartPlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    public void onPause() {
        videoPlayerLocalPicker.rePausePlayer();
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestory() {
        videoPlayerLocalPicker.releasePlayer();
    }


}
