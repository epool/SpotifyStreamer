package mx.eduardopool.spotifystreamer.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import java.io.IOException;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.beans.TrackBean;
import mx.eduardopool.spotifystreamer.commons.Constants;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrackPlayerActivityFragment extends BaseDialogFragment {
    private ArtistBean artistBean;
    private TrackBean trackBean;

    public TrackPlayerActivityFragment() {
    }

    public static TrackPlayerActivityFragment newInstance(ArtistBean artistBean, TrackBean trackBean) {
        TrackPlayerActivityFragment trackPlayerActivityFragment = new TrackPlayerActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.Extras.ARTIST_BEAN, artistBean);
        args.putParcelable(Constants.Extras.TRACK_BEAN, trackBean);
        trackPlayerActivityFragment.setArguments(args);
        return trackPlayerActivityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistBean = getArguments().getParcelable(Constants.Extras.ARTIST_BEAN);
            trackBean = getArguments().getParcelable(Constants.Extras.TRACK_BEAN);

            MediaPlayer mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mediaPlayer.setDataSource(trackBean.getPreviewUrl());
            } catch (IOException e) {
                showToastMessage(e.getMessage());
            }
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepareAsync();
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_track_player;
    }
}
