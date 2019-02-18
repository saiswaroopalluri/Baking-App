package com.example.android.bakingapp.fragments;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.PlayerActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.models.RecipeStep;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeDetailFragment extends Fragment {

    private static final String VIDEO_URL_KEY = "video_url";

    private static final String PLAYER_POSITION = "player_position";
    private static final String PLAYER_STATE = "player_state";
    private static final String RECIPE_STEP_POSITION = "STEP_POSITION";

    private List<RecipeStep> recipeStepList;
    private SimpleExoPlayer mExoPlayer;
    private OnDetailFragmentInteractionListener mListener;
    private RecipeStep recipeStep;
    private boolean mTwoPane;
    private long playerPosition;
    private boolean getPlayerWhenReady;

    @BindView(R.id.exo_player_detail)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.exo_player_detail_card_view)
    CardView exoPlayerDetailCardView;

    @BindView(R.id.exo_player_image_view)
    ImageView exoPlayerImageView;

    @BindView(R.id.tv_description)
    TextView textViewDescription;

    @BindView(R.id.tv_no_video)
    TextView textViewNoVideo;

    @BindView(R.id.btn_next)
    Button btnNext;

    @BindView(R.id.btn_previous)
    Button btnPrevious;

    public RecipeDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        // Tablet layout condition
        if (mTwoPane) {
            if (savedInstanceState == null) {
                resetPlayerData();
            } else {
                playerPosition = savedInstanceState.getLong(PLAYER_POSITION, 0);
                getPlayerWhenReady = savedInstanceState.getBoolean(PLAYER_STATE, true);
            }
        }

        if (savedInstanceState == null) {
            mListener.onDetailFragmentInteraction();
        } else {
            int position = savedInstanceState.getInt(RECIPE_STEP_POSITION,0);
            loadToPosition(position);
        }

        return rootView;
    }

    public void resetPlayerData() {
        playerPosition = 0;
        getPlayerWhenReady = true; // By default will play
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(RECIPE_STEP_POSITION, recipeStep.getmStepId());
        if (mTwoPane) {
            playerPosition = mExoPlayer.getCurrentPosition();
            getPlayerWhenReady = mExoPlayer.getPlayWhenReady();
            outState.putLong(PLAYER_POSITION, playerPosition);
            outState.putBoolean(PLAYER_STATE, getPlayerWhenReady);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDetailFragmentInteractionListener) {
            mListener = (OnDetailFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void showNavigationButtons() {
        if (mTwoPane) {
            btnPrevious.setVisibility(View.INVISIBLE);
            btnNext.setVisibility(View.INVISIBLE);
        } else {
            btnPrevious.setVisibility(View.VISIBLE);
            btnNext.setVisibility(View.VISIBLE);
        }
    }

    private void showHidePlayerView() {
        if (mTwoPane) {
            exoPlayerImageView.setVisibility(View.INVISIBLE);
            exoPlayerView.setVisibility(View.VISIBLE);
        } else {
            if (recipeStep.getmThumbnailURL() != null) {
                Picasso.get().
                        load(Uri.parse(recipeStep.getmThumbnailURL()))
                        .placeholder(android.R.drawable.ic_media_play)
                        .error(android.R.drawable.ic_media_play)
                        .into(exoPlayerImageView);
            }
            exoPlayerImageView.setVisibility(View.VISIBLE);
            exoPlayerView.setVisibility(View.INVISIBLE);
        }
    }

    public void setRecipeStepList(List<RecipeStep> recipeStepList, boolean mTwoPane) {
        this.recipeStepList = recipeStepList;
        this.mTwoPane = mTwoPane;
        if (this.mTwoPane) {
            resetPlayerData();
        }
    }

    public void refreshToPosition(int position) {
        playerPosition = 0;
        getPlayerWhenReady = true;
        loadToPosition(position);
    }

    private void loadToPosition(int position) {
        recipeStep = recipeStepList.get(position);
        showHidePlayerView();
        showNavigationButtons();
        refreshNavigationButtons(position);
        initializePlayer(Uri.parse(recipeStep.getmVideoURL()));
        textViewDescription.setText(recipeStep.getmDescription());
    }



    public void refreshNavigationButtons(int currentPosition) {
        if (mTwoPane) {
            btnNext.setVisibility(View.INVISIBLE);
            btnPrevious.setVisibility(View.INVISIBLE);
            return;
        }

        int count = recipeStepList.size();
        if (currentPosition == 0) {
            btnPrevious.setEnabled(false);
        }

        if (currentPosition == count-1) {
            btnNext.setEnabled(false);
        }

        if (currentPosition > 0 && currentPosition < count-2) {
            btnPrevious.setEnabled(true);
            btnNext.setEnabled(true);
        }
    }

    @OnClick(R.id.exo_player_image_view)
    public void showPlayerActivity(View view) {
        Intent intent = new Intent(getContext(), PlayerActivity.class);
        intent.putExtra(VIDEO_URL_KEY, recipeStep.getmVideoURL());
        startActivity(intent);
    }

    @OnClick(R.id.btn_previous)
    public void btnPreviousClicked(View view) {
        int position = recipeStep.getmStepId();
        if (position >= 0) {
            position = position-1;
            refreshToPosition(position);
        }
    }

    @OnClick(R.id.btn_next)
    public void btnNextClicked(View view) {
        int position = recipeStep.getmStepId();
        if (position >= 0) {
            position = position+1;
            refreshToPosition(position);
        }
    }

    private void initializePlayer(Uri mediaUri) {

        if (mediaUri == null || mediaUri.toString().equals("")) {
            textViewNoVideo.setVisibility(View.VISIBLE);
            textViewNoVideo.bringToFront();
            textViewNoVideo.setTextColor(Color.rgb(0,0,0));
            exoPlayerImageView.setVisibility(View.INVISIBLE);
            exoPlayerView.setVisibility(View.INVISIBLE);
            return;
        } else {
            textViewNoVideo.setVisibility(View.INVISIBLE);
            if (mTwoPane) {
                exoPlayerImageView.setVisibility(View.INVISIBLE);
                exoPlayerView.setVisibility(View.VISIBLE);
            } else {
                exoPlayerImageView.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.INVISIBLE);
            }
        }

        if (!mTwoPane) {
            return;
        }


        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(mExoPlayer);
        } else {
            mExoPlayer.stop();
        }
            // Prepare the MediaSource.
        String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
        MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
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


    public interface OnDetailFragmentInteractionListener {
        void onDetailFragmentInteraction();
    }

}
