package mx.eduardopool.spotifystreamer.fragments;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.adapters.TrackAdapter;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.beans.TrackBean;
import mx.eduardopool.spotifystreamer.commons.Constants;
import mx.eduardopool.spotifystreamer.ws.SpotifyWS;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenTracksActivityFragment extends BaseFragment {
    private final static String TRACK_BEANS_PARAM = "trackBeans";

    @InjectView(R.id.progress_bar_container)
    FrameLayout progressBarFrameLayout;
    private TrackAdapter trackAdapter;
    private ArtistBean artistBean;
    private ArrayList<TrackBean> trackBeans;

    public TopTenTracksActivityFragment() {
    }

    public static TopTenTracksActivityFragment newInstance(ArtistBean artistBean) {
        TopTenTracksActivityFragment topTenTracksActivityFragment = new TopTenTracksActivityFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.Extras.ARTIST_BEAN, artistBean);
        topTenTracksActivityFragment.setArguments(args);
        return topTenTracksActivityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            artistBean = savedInstanceState.getParcelable(Constants.Extras.ARTIST_BEAN);
            trackBeans = savedInstanceState.getParcelableArrayList(TRACK_BEANS_PARAM);
        } else {
            artistBean = getArguments().getParcelable(Constants.Extras.ARTIST_BEAN);
            trackBeans = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        ListView listView = ButterKnife.findById(view, android.R.id.list);
        trackAdapter = new TrackAdapter(getActivity(), trackBeans);
        listView.setAdapter(trackAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TrackBean trackBean = trackAdapter.getItem(position);

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
        });

        if (savedInstanceState == null) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put(SpotifyService.COUNTRY, Constants.Countries.MX);
            progressBarFrameLayout.setVisibility(View.VISIBLE);
            SpotifyWS.getSpotifyService().getArtistTopTrack(artistBean.getId(), queryMap, new Callback<Tracks>() {
                @Override
                public void success(final Tracks tracks, Response response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            trackBeans.clear();
                            if (tracks.tracks.isEmpty()) {
                                showToastMessage(R.string.artist_without_top_tracks);
                            } else {
                                for (Track track : tracks.tracks) {
                                    TrackBean trackBean = new TrackBean(track);
                                    trackBeans.add(trackBean);
                                }
                            }
                            trackAdapter.notifyDataSetChanged();
                            progressBarFrameLayout.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void failure(final RetrofitError error) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToastMessage(error.getMessage());
                            progressBarFrameLayout.setVisibility(View.GONE);
                        }
                    });
                }
            });
        }

        setActionBarSubTitle(artistBean.getName());

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_top_ten_tracks;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(Constants.Extras.ARTIST_BEAN, artistBean);
        outState.putParcelableArrayList(TRACK_BEANS_PARAM, trackBeans);
        super.onSaveInstanceState(outState);
    }
}
