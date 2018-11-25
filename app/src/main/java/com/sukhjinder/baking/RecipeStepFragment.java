package com.sukhjinder.baking;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sukhjinder.baking.Model.Step;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepFragment extends Fragment {

    private Step step;
    private SimpleExoPlayer exoPlayer;

    @BindView(R.id.simpleExoPlayerView)
    SimpleExoPlayerView exoPlayerView;

    @BindView(R.id.instructionsTV)
    TextView instructionsTV;

    public RecipeStepFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        if (args != null) {
            step = args.getParcelable("step");
        }
        setRetainInstance(true);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (step != null) {
            outState.putParcelable("STEP", step);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("STEP")) {
            step = savedInstanceState.getParcelable("STEP");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);
        ButterKnife.bind(this, view);

        instructionsTV.setText(step.getDescription());

        String videoURL = step.getVideoURL();
        if (!videoURL.isEmpty()) {
            initializePlayer(step.getVideoURL());
        } else {
            exoPlayerView.setVisibility(View.GONE);
        }

        return view;
    }


    private void initializePlayer(String videoURL) {
        if (exoPlayer == null) {
            exoPlayerView.requestFocus();

            BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
            DataSource.Factory dataSourceFactory;

            LoadControl loadControl = new DefaultLoadControl();
            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveVideoTrackSelection.Factory(bandwidthMeter);
            TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.setPlayWhenReady(true);

            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), "mediaPlayerSample"));

            MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoURL),
                    dataSourceFactory, extractorsFactory, null, null);

            exoPlayer.prepare(mediaSource);
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {

            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;
        }
    }
}
