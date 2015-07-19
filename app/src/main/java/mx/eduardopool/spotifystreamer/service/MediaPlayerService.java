package mx.eduardopool.spotifystreamer.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.wifi.WifiManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;
import java.util.ArrayList;

import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.beans.TrackBean;
import mx.eduardopool.spotifystreamer.commons.Constants;

public class MediaPlayerService extends Service implements MediaPlayer.OnErrorListener {
    public static final String ACTION_STOP = "mx.eduardopool.action.STOP";
    public static final String ACTION_PLAY = "mx.eduardopool.action.PLAY";
    public static final String ACTION_PAUSE = "mx.eduardopool.action.PAUSE";
    public static final String ACTION_NEXT = "mx.eduardopool.action.NEXT";
    public static final String ACTION_PREVIOUS = "mx.eduardopool.action.PREVIOUS";
    private static final int NOTIFICATION_ID = 1;
    // Binder given to clients
    private final IBinder mBinder = new MediaPlayerBinder();
    private MediaPlayer mMediaPlayer = null;
    private WifiManager.WifiLock wifiLock;
    private ArtistBean artistBean;
    private ArrayList<TrackBean> trackBeans;
    private int currentTrackIndex = -1;
    private MediaPlayerState currentMediaPlayerState = MediaPlayerState.IDLE;
    private MediaPlayerListener mediaPlayerListener;

    public MediaPlayerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        initMediaPlayerIfNeeded();
        wifiLock = ((WifiManager) getSystemService(Context.WIFI_SERVICE))
                .createWifiLock(WifiManager.WIFI_MODE_FULL, "mylock");
    }

    public void initMediaPlayerIfNeeded() {
        if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mMediaPlayer.setOnPreparedListener(mediaPlayer -> currentMediaPlayerState.onPlay(MediaPlayerService.this));
            mMediaPlayer.setOnCompletionListener(mediaPlayer -> currentMediaPlayerState.onPlaybackCompleted(MediaPlayerService.this));
        } else {
            mMediaPlayer.reset();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            if (intent.hasExtra(Constants.Extras.ARTIST_BEAN)) {
                artistBean = intent.getParcelableExtra(Constants.Extras.ARTIST_BEAN);
            }
            if (intent.hasExtra(Constants.Extras.TRACK_BEANS)) {
                trackBeans = intent.getParcelableArrayListExtra(Constants.Extras.TRACK_BEANS);
            }
            if (intent.hasExtra(Constants.Extras.TRACK_INDEX) || currentTrackIndex == -1) {
                currentTrackIndex = intent.getIntExtra(Constants.Extras.TRACK_INDEX, 0);
            }

            String action = intent.getAction();
            if (action != null) {
                switch (action) {
                    case ACTION_STOP:
                        currentMediaPlayerState.onStop(this);
                        break;
                    case ACTION_PLAY:
                        currentMediaPlayerState.onPlay(this);
                        break;
                    case ACTION_PAUSE:
                        currentMediaPlayerState.onPause(this);
                        break;
                    case ACTION_NEXT:
                        currentMediaPlayerState.onNext(this);
                        break;
                    case ACTION_PREVIOUS:
                        currentMediaPlayerState.onPrevious(this);
                        break;
                }
            }
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public void previous() {
        currentMediaPlayerState.onPrevious(MediaPlayerService.this);
        if (mediaPlayerListener != null) {
            mediaPlayerListener.onStartTrack(trackBeans.get(currentTrackIndex), mMediaPlayer.getDuration());
        }
    }

    public void play() {
        currentMediaPlayerState.onPlay(MediaPlayerService.this);
    }

    public void pause() {
        currentMediaPlayerState.onPause(MediaPlayerService.this);
    }

    public void next() {
        currentMediaPlayerState.onNext(MediaPlayerService.this);
        if (mediaPlayerListener != null) {
            mediaPlayerListener.onStartTrack(trackBeans.get(currentTrackIndex), mMediaPlayer.getDuration());
        }
    }

    public boolean isPlaying() {
        return currentMediaPlayerState.equals(MediaPlayerState.PLAYING) || currentMediaPlayerState.equals(MediaPlayerState.PREPARING);
    }

    public int getMediaDuration() {
        return mMediaPlayer.getDuration();
    }

    public int getCurrentTrackPosition() {
        return mMediaPlayer.getCurrentPosition();
    }

    public void seekMediaPlayerTo(int position) {
        mMediaPlayer.seekTo(position);
    }

    public void showMediaPlayerNotification() {
        TrackBean trackBean = trackBeans.get(currentTrackIndex);
        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle(artistBean.getName())
                .setContentInfo(trackBean.getAlbumName())
                .setContentText(trackBean.getName())
                .build();

        startForeground(NOTIFICATION_ID, notification);
    }

    public void hideMediaPlayerNotification() {
        stopForeground(true);
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    public void setMediaPlayerListener(MediaPlayerListener mediaPlayerListener) {
        this.mediaPlayerListener = mediaPlayerListener;
    }

    private void setCurrentMediaPlayerState(MediaPlayerState currentMediaPlayerState) {
        this.currentMediaPlayerState = currentMediaPlayerState;
        if (mediaPlayerListener != null) {
            mediaPlayerListener.onMediaPlayerStateChange(currentMediaPlayerState);
        }
    }

    public enum MediaPlayerState implements MediaPlayerEventListener {
        IDLE {
            @Override
            public void onPlay(MediaPlayerService mediaPlayerService) {
                try {
                    if (mediaPlayerService.trackBeans != null && !mediaPlayerService.trackBeans.isEmpty()) {
                        mediaPlayerService.mMediaPlayer.reset();
                        if (mediaPlayerService.currentTrackIndex < 0) {
                            mediaPlayerService.currentTrackIndex = mediaPlayerService.trackBeans.size() - 1;
                        } else if (mediaPlayerService.currentTrackIndex >= mediaPlayerService.trackBeans.size()) {
                            mediaPlayerService.currentTrackIndex = 0;
                        }
                        mediaPlayerService.mMediaPlayer.setDataSource(mediaPlayerService.trackBeans.get(mediaPlayerService.currentTrackIndex).getPreviewUrl());
                        mediaPlayerService.mMediaPlayer.prepareAsync(); // prepare async to not block main thread
                        mediaPlayerService.setCurrentMediaPlayerState(PREPARING);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        },
        PREPARING {
            @Override
            public void onPlay(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.mMediaPlayer.start();
                mediaPlayerService.setCurrentMediaPlayerState(PLAYING);
                if (mediaPlayerService.mediaPlayerListener != null) {
                    mediaPlayerService.mediaPlayerListener.onStartTrack(mediaPlayerService.trackBeans.get(mediaPlayerService.currentTrackIndex), mediaPlayerService.mMediaPlayer.getDuration());
                }
            }
        },
        STOPPED {
            @Override
            public void onPlay(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.mMediaPlayer.prepareAsync();
            }
        },
        PLAYING {
            @Override
            public void onPlay(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.setCurrentMediaPlayerState(IDLE);
                mediaPlayerService.currentMediaPlayerState.onPlay(mediaPlayerService);
            }

            @Override
            public void onPause(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.mMediaPlayer.pause();
                mediaPlayerService.setCurrentMediaPlayerState(PAUSED);
            }

            @Override
            public void onStop(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.mMediaPlayer.stop();
                mediaPlayerService.setCurrentMediaPlayerState(STOPPED);
            }

            @Override
            public void onPrevious(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.currentTrackIndex--;
                mediaPlayerService.setCurrentMediaPlayerState(IDLE);
                mediaPlayerService.currentMediaPlayerState.onPlay(mediaPlayerService);
            }

            @Override
            public void onNext(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.currentTrackIndex++;
                mediaPlayerService.setCurrentMediaPlayerState(IDLE);
                mediaPlayerService.currentMediaPlayerState.onPlay(mediaPlayerService);
            }

            @Override
            public void onPlaybackCompleted(MediaPlayerService mediaPlayerService) {
                if (mediaPlayerService.currentTrackIndex < mediaPlayerService.trackBeans.size() - 1) {
                    onNext(mediaPlayerService);
                } else {
                    mediaPlayerService.setCurrentMediaPlayerState(PLAYBACK_COMPLETED);
                }
            }
        },
        PAUSED {
            @Override
            public void onPlay(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.mMediaPlayer.start();
                mediaPlayerService.setCurrentMediaPlayerState(PLAYING);
            }

            @Override
            public void onStop(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.mMediaPlayer.stop();
                mediaPlayerService.setCurrentMediaPlayerState(STOPPED);
            }

            @Override
            public void onPrevious(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.currentTrackIndex--;
                mediaPlayerService.setCurrentMediaPlayerState(IDLE);
                mediaPlayerService.currentMediaPlayerState.onPlay(mediaPlayerService);
            }

            @Override
            public void onNext(MediaPlayerService mediaPlayerService) {
                mediaPlayerService.currentTrackIndex++;
                mediaPlayerService.setCurrentMediaPlayerState(IDLE);
                mediaPlayerService.currentMediaPlayerState.onPlay(mediaPlayerService);
            }
        },
        PLAYBACK_COMPLETED;

        @Override
        public void onPlay(MediaPlayerService mediaPlayerService) {
        }

        @Override
        public void onPause(MediaPlayerService mediaPlayerService) {
        }

        @Override
        public void onStop(MediaPlayerService mediaPlayerService) {
        }

        @Override
        public void onPrevious(MediaPlayerService mediaPlayerService) {
        }

        @Override
        public void onNext(MediaPlayerService mediaPlayerService) {
        }

        @Override
        public void onPlaybackCompleted(MediaPlayerService mediaPlayerService) {
        }
    }

    public interface MediaPlayerListener {
        void onStartTrack(TrackBean trackBean, int duration);

        void onMediaPlayerStateChange(MediaPlayerState mediaPlayerState);
    }

    private interface MediaPlayerEventListener {
        void onPlay(MediaPlayerService mediaPlayerService);

        void onPause(MediaPlayerService mediaPlayerService);

        void onStop(MediaPlayerService mediaPlayerService);

        void onPrevious(MediaPlayerService mediaPlayerService);

        void onNext(MediaPlayerService mediaPlayerService);

        void onPlaybackCompleted(MediaPlayerService mediaPlayerService);
    }

    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class MediaPlayerBinder extends Binder {

        public MediaPlayerService getService() {
            // Return this instance of MediaPlayerService so clients can call public methods
            return MediaPlayerService.this;
        }

    }

}