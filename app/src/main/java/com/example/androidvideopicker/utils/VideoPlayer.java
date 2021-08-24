package com.example.androidvideopicker.utils;

import androidx.databinding.ObservableField;

import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.ui.PlayerView;

public abstract
class VideoPlayer {
    public abstract void setSimpleExoPlayer(SimpleExoPlayer simpleExoPlayer, PlayerView playerView) ;

    public abstract void play(String uri);
}
