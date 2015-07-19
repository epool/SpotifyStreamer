package mx.eduardopool.spotifystreamer.fragments;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.InjectView;
import butterknife.OnClick;
import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.beans.TrackBean;
import mx.eduardopool.spotifystreamer.commons.Constants;
import mx.eduardopool.spotifystreamer.service.MediaPlayerService;
import mx.eduardopool.spotifystreamer.util.Util;

/**
 * A placeholder fragment containing a simple view.
 */
public class TrackPlayerActivityDialogFragment extends BaseDialogFragment implements MediaPlayerService.MediaPlayerListener, SeekBar.OnSeekBarChangeListener {
    @InjectView(R.id.artist_name_text_view)
    protected TextView artistNameTextView;
    @InjectView(R.id.album_name_text_view)
    protected TextView albumNameTextView;
    @InjectView(R.id.album_artwork_image_view)
    protected ImageView albumArtworkImageView;
    @InjectView(R.id.track_name_text_view)
    protected TextView trackNameTextView;
    @InjectView(R.id.seek_bar)
    protected SeekBar trackProgressSeekBar;
    @InjectView(R.id.current_time_text_view)
    protected TextView currentTimeTextView;
    @InjectView(R.id.left_time_text_view)
    protected TextView leftTimeTextView;
    @InjectView(R.id.previous_image_button)
    protected ImageButton previousImageButton;
    @InjectView(R.id.play_pause_image_button)
    protected ImageButton playPauseImageButton;
    @InjectView(R.id.next_image_button)
    protected ImageButton nextImageButton;
    boolean mBound = false;
    private ArtistBean artistBean;
    private ArrayList<TrackBean> trackBeans;
    private TrackBean trackBean;
    private int trackBeanSelectedIndex;
    private MediaPlayerService mediaPlayerService;
    private Handler mHandler = new Handler();
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            // We've bound to MediaPlayerService, cast the IBinder and get MediaPlayerService instance
            MediaPlayerService.MediaPlayerBinder binder = (MediaPlayerService.MediaPlayerBinder) service;
            mediaPlayerService = binder.getService();
            mediaPlayerService.setMediaPlayerListener(TrackPlayerActivityDialogFragment.this);
            mediaPlayerService.hideMediaPlayerNotification();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName className) {
            mBound = false;
        }
    };

    public TrackPlayerActivityDialogFragment() {
    }

    public static TrackPlayerActivityDialogFragment newInstance(ArtistBean artistBean, ArrayList<TrackBean> trackBeans, int trackBeanSelectedIndex) {
        TrackPlayerActivityDialogFragment trackPlayerActivityDialogFragment = new TrackPlayerActivityDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.Extras.ARTIST_BEAN, artistBean);
        args.putInt(Constants.Extras.TRACK_BEAN_SELECTED_INDEX, trackBeanSelectedIndex);
        args.putParcelableArrayList(Constants.Extras.TRACK_BEANS, trackBeans);
        trackPlayerActivityDialogFragment.setArguments(args);
        return trackPlayerActivityDialogFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistBean = getArguments().getParcelable(Constants.Extras.ARTIST_BEAN);
            trackBeans = getArguments().getParcelableArrayList(Constants.Extras.TRACK_BEANS);
            trackBeanSelectedIndex = getArguments().getInt(Constants.Extras.TRACK_BEAN_SELECTED_INDEX);
            trackBean = trackBeans.get(trackBeanSelectedIndex);

            Intent intent = new Intent(getBaseActivity(), MediaPlayerService.class).setAction(MediaPlayerService.ACTION_PLAY).putExtra(Constants.Extras.ARTIST_BEAN, artistBean).putExtra(Constants.Extras.TRACK_BEANS, trackBeans).putExtra(Constants.Extras.TRACK_INDEX, trackBeanSelectedIndex);
            getActivity().startService(intent);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        trackProgressSeekBar.setOnSeekBarChangeListener(this);
        updateTrackView(trackBean);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Bind to LocalService
        Intent intent = new Intent(getBaseActivity(), MediaPlayerService.class);
        getBaseActivity().bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            mediaPlayerService.setMediaPlayerListener(null);
            mediaPlayerService.showMediaPlayerNotification();
            getBaseActivity().unbindService(mConnection);
            mBound = false;
        }
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_track_player;
    }

    @OnClick({R.id.previous_image_button, R.id.play_pause_image_button, R.id.next_image_button})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.previous_image_button: {
                if (mBound) {
                    mediaPlayerService.previous();
                }
            }
            break;
            case R.id.play_pause_image_button: {
                togglePlayPause();
            }
            break;
            case R.id.next_image_button: {
                if (mBound) {
                    mediaPlayerService.next();
                }
            }
            break;
        }
    }

    private void updateTrackView(TrackBean trackBean) {
        artistNameTextView.setText(artistBean.getName());
        albumNameTextView.setText(trackBean.getAlbumName());
        Picasso.with(getBaseActivity()).load(trackBean.getLargeImageUrl()).placeholder(R.mipmap.ic_launcher).into(albumArtworkImageView);
        trackNameTextView.setText(trackBean.getName());
    }

    private void togglePlayPause() {
        if (mBound) {
            if (!mediaPlayerService.isPlaying()) {
                mediaPlayerService.play();
            } else {
                mediaPlayerService.pause();
            }
        }
    }

    @Override
    public void onStartTrack(TrackBean trackBean, int duration) {
        updateTrackView(trackBean);
        trackProgressSeekBar.setMax(duration);
    }

    @Override
    public void onMediaPlayerStateChange(MediaPlayerService.MediaPlayerState mediaPlayerState) {
        switch (mediaPlayerState) {
            case IDLE:
            case PAUSED:
            case STOPPED:
            case PLAYBACK_COMPLETED:
                Picasso.with(getBaseActivity()).load(android.R.drawable.ic_media_play).into(playPauseImageButton);
                break;
            case PREPARING:
            case PLAYING:
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (mBound) {
                            int currentPosition = mediaPlayerService.getCurrentTrackPosition();
                            int leftTime = mediaPlayerService.getMediaDuration() - currentPosition;
                            trackProgressSeekBar.setProgress(currentPosition);
                            currentTimeTextView.setText(Util.millisecondsToMinutesAndSecondsString(currentPosition));
                            leftTimeTextView.setText(Util.millisecondsToMinutesAndSecondsString(leftTime));
                            mHandler.postDelayed(this, 1000);
                        }
                    }
                });
                Picasso.with(getBaseActivity()).load(android.R.drawable.ic_media_pause).into(playPauseImageButton);
                break;
        }
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mBound && fromUser) {
            mediaPlayerService.seekMediaPlayerTo(progress);
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
