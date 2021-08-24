package com.example.androidvideopicker.utils;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.analytics.AnalyticsListener;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.LoadEventInfo;
import com.google.android.exoplayer2.source.MediaLoadData;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.UnrecognizedInputFormatException;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DataSpec;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.io.IOException;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FIT;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_ZOOM;

public
class VideoPlayerLocalPicker extends VideoPlayer {

    private SimpleExoPlayer simpleExoPlayer;

    private PlayerView playerView;

    public ObservableField<Boolean> playBtnLiveData;

    public ObservableField<Boolean> isErrorMsgLiveData;

    public VideoPlayerLocalPicker(ObservableField<Boolean> playBtnLiveData, ObservableField<Boolean> isErrorMsgLiveData) {
        this.playBtnLiveData = playBtnLiveData;
        this.isErrorMsgLiveData = isErrorMsgLiveData;
    }

    @Override
    public void setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer, PlayerView playerView) {
        this.simpleExoPlayer = simpleExoPlayer;
        this.playerView = playerView;

        playerView.setPlayer(simpleExoPlayer);
        simpleExoPlayer.setRepeatMode(simpleExoPlayer.REPEAT_MODE_ONE);

        simpleExoPlayer.addAnalyticsListener(new AnalyticsListener() {
            //            @Override
//            public void onVideoDecoderInitialized(EventTime eventTime, String decoderName, long initializationDurationMs) {
//                Log.d("addAnalyticsListener" , "onVideoDecoderInitialized - " + decoderName);
//            }
            @Override
            public void onPlayerError(EventTime eventTime, ExoPlaybackException error) {
                isErrorMsgLiveData.set(true);
                playBtnLiveData.set(false);
            }

        });
    }


    @Override
    public void play(String uri) {
        simpleExoPlayer.stop(true);
        simpleExoPlayer.clearMediaItems();
        simpleExoPlayer.setMediaItem(MediaItem.fromUri(Uri.parse(uri)));
        simpleExoPlayer.prepare();
        simpleExoPlayer.play();

        isErrorMsgLiveData.set(false);
        playBtnLiveData.set(false);


//        동영상의 비율을 줌되도록해준다
        if (playerView.getResizeMode() == AspectRatioFrameLayout.RESIZE_MODE_FIT) {
            playerView.setResizeMode(RESIZE_MODE_ZOOM);
        }
    }


    public void replayOrPause() {
        if (simpleExoPlayer.isPlaying()) {
            simpleExoPlayer.pause();
            playBtnLiveData.set(true);
        } else {
            simpleExoPlayer.play();
            playBtnLiveData.set(false);
        }
    }

    public void reStartPlayer() {
        simpleExoPlayer.play();
        playBtnLiveData.set(false);
    }

    public void rePausePlayer() {
        if (simpleExoPlayer.isPlaying()) {
            simpleExoPlayer.pause();
            playBtnLiveData.set(true);
        }
    }

    public void ratioChange() {
        if (playerView.getResizeMode() == RESIZE_MODE_ZOOM) {
            playerView.setResizeMode(RESIZE_MODE_FIT);
        } else {
            playerView.setResizeMode(RESIZE_MODE_ZOOM);
        }
    }

    public void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    public ObservableField<Boolean> getPlayBtnLiveData() {
        return playBtnLiveData;
    }

    public void setPlayBtnLiveData(ObservableField<Boolean> playBtnLiveData) {
        this.playBtnLiveData = playBtnLiveData;
    }
}
