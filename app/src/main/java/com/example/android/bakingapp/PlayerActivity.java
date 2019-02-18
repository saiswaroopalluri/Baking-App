package com.example.android.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity {

    private static final String VIDEO_URL_KEY = "video_url";
    private static final String PLAYER_POSITION = "player_position";
    private static final String PLAYER_STATE = "player_state";

    private SimpleExoPlayer mExoPlayer;
    private long playerPosition;
    private boolean getPlayerWhenReady;

    @BindView(R.id.exo_player_activity)
    SimpleExoPlayerView exoPlayerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            playerPosition = 0;
            getPlayerWhenReady = true; // By default will play
        } else {
            playerPosition = savedInstanceState.getLong(PLAYER_POSITION, 0);
            getPlayerWhenReady = savedInstanceState.getBoolean(PLAYER_STATE, true);
        }

        Intent intentCalled = getIntent();
        if (intentCalled != null && intentCalled.hasExtra(VIDEO_URL_KEY)) {
            String url = intentCalled.getStringExtra(VIDEO_URL_KEY);
            initializePlayer(Uri.parse(url));
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        playerPosition = mExoPlayer.getCurrentPosition();
        getPlayerWhenReady = mExoPlayer.getPlayWhenReady();
        outState.putLong(PLAYER_POSITION, playerPosition);
        outState.putBoolean(PLAYER_STATE, getPlayerWhenReady);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);
            exoPlayerView.setPlayer(mExoPlayer);
        } else {
            mExoPlayer.stop();
        }
        // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(this, getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                this, userAgent), new DefaultExtractorsFactory(), null, null);
        mExoPlayer.prepare(mediaSource);
        mExoPlayer.seekTo(playerPosition);
        mExoPlayer.setPlayWhenReady(getPlayerWhenReady);
    }

    private void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }
}
