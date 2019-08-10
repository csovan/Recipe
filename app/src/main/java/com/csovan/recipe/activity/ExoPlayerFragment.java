package com.csovan.recipe.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.csovan.recipe.R;
import com.csovan.recipe.model.Step;
import com.csovan.recipe.utils.GlideApp;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.csovan.recipe.utils.Constant.EXOPLAYER_STATE;
import static com.csovan.recipe.utils.Constant.STEP_SINGLE;
import static com.csovan.recipe.utils.Constant.STEP_VIDEO_POSITION;


public class ExoPlayerFragment extends Fragment {


    @BindView(R.id.tv_step_title)
    TextView stepTitle;

    @BindView(R.id.exoplayer_view)
    PlayerView exoPlayerView;

    @BindView(R.id.tv_step_description)
    TextView stepDescription;

    @BindView(R.id.iv_video_placeholder)
    ImageView videoPlaceHolder;

    private SimpleExoPlayer simpleExoPlayer;
    private Step step;
    private boolean exoPlayerState = true;
    private long exoPlayerPosition;


    private TrackSelector trackSelector;
    private DataSource.Factory dataSourceFactory;
    private MediaSource videoSource;

    public ExoPlayerFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exoplayer, container, false);

        Intent intent = Objects.requireNonNull(getActivity()).getIntent();

        if (intent != null){
            if (savedInstanceState != null && savedInstanceState.containsKey(STEP_SINGLE)){
                step = savedInstanceState.getParcelable(STEP_SINGLE);
            }else{
                if (intent.hasExtra(STEP_SINGLE)){
                    step = intent.getParcelableExtra(STEP_SINGLE);
                }else {
                    if (getArguments() != null) {
                        step = getArguments().getParcelable(STEP_SINGLE);
                    }
                }
            }
        }

        ButterKnife.bind(this, rootView);

        stepDescription.setText(step.getDescription());
        stepTitle.setText(step.getShortDescription());

        if (!step.getVideoURL().isEmpty()){
            videoPlaceHolder.setVisibility(View.GONE);
            exoPlayerView.setVisibility(View.VISIBLE);
        }else if (!step.getThumbnailURL().isEmpty()){
            exoPlayerView.setVisibility(View.INVISIBLE);
            GlideApp.with(getActivity())
                    .load(step.getThumbnailURL())
                    .placeholder(R.drawable.ic_videocam_off)
                    .error(R.drawable.ic_videocam_off)
                    .into(videoPlaceHolder);
            videoPlaceHolder.setVisibility(View.VISIBLE);
        }else {
            exoPlayerView.setVisibility(View.INVISIBLE);
            videoPlaceHolder.setImageResource(R.drawable.ic_videocam_off);
            videoPlaceHolder.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    // Initialize exo-player
    public void initializeExoPlayer(Uri videoUri){

        if(simpleExoPlayer == null){

            // Create a default TrackSelector
            DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);
            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            // Create the player
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);

            // Bind the player to the view.
            exoPlayerView.setPlayer(simpleExoPlayer);

            // Produces DataSource instances through which media data is loaded.
            dataSourceFactory = new DefaultDataSourceFactory(Objects.requireNonNull(getActivity()),
                    Util.getUserAgent(getActivity(), getString(R.string.app_name)), bandwidthMeter);

            // This is the MediaSource representing the media to be played.
            videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);

            if (exoPlayerPosition != C.TIME_UNSET) {
                simpleExoPlayer.seekTo(exoPlayerPosition);
            }
            // Prepare the player with the source.
            simpleExoPlayer.prepare(videoSource);
            simpleExoPlayer.setPlayWhenReady(exoPlayerState);
        }
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
            dataSourceFactory = null;
            videoSource = null;
            trackSelector = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializeExoPlayer(Uri.parse(step.getVideoURL()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            initializeExoPlayer(Uri.parse(step.getVideoURL()));
        }
        if(simpleExoPlayer != null){
            simpleExoPlayer.setPlayWhenReady(exoPlayerState);
            simpleExoPlayer.seekTo(exoPlayerPosition);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if(simpleExoPlayer != null){
            exoPlayerPosition = simpleExoPlayer.getCurrentPosition();
            exoPlayerState = simpleExoPlayer.getPlayWhenReady();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(simpleExoPlayer != null){
            if (Util.SDK_INT > 23) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(STEP_SINGLE, step);
        outState.putLong(STEP_VIDEO_POSITION, exoPlayerPosition);
        outState.putBoolean(EXOPLAYER_STATE, exoPlayerState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState){
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState != null){
            exoPlayerPosition = savedInstanceState.getLong(STEP_VIDEO_POSITION);
            exoPlayerState = savedInstanceState.getBoolean(EXOPLAYER_STATE);
            if (savedInstanceState.containsKey(STEP_SINGLE)){
                step = savedInstanceState.getParcelable(STEP_SINGLE);
            }
        }
    }

}
