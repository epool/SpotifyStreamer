package mx.eduardopool.spotifystreamer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import mx.eduardopool.spotifystreamer.adapters.TrackAdapter;
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
    private final static String ARTIST_ID_PARAM = "artistId";
    private final static String ARTIST_NAME_PARAM = "artistName";
    private final static String TRACK_BEANS_PARAM = "trackBeans";

    private TrackAdapter trackAdapter;
    private String artistId;
    private String artistName;
    private ArrayList<TrackBean> trackBeans;

    public TopTenTracksActivityFragment() {
    }

    public static TopTenTracksActivityFragment newInstance(String artistId, String artistName) {
        TopTenTracksActivityFragment topTenTracksActivityFragment = new TopTenTracksActivityFragment();
        Bundle args = new Bundle();
        args.putString(Constants.Extras.ARTIST_ID, artistId);
        args.putString(Constants.Extras.ARTIST_NAME, artistName);
        topTenTracksActivityFragment.setArguments(args);
        return topTenTracksActivityFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            artistId = savedInstanceState.getString(ARTIST_ID_PARAM);
            artistName = savedInstanceState.getString(ARTIST_NAME_PARAM);
            trackBeans = savedInstanceState.getParcelableArrayList(TRACK_BEANS_PARAM);
        } else {
            artistId = getArguments().getString(Constants.Extras.ARTIST_ID);
            artistName = getArguments().getString(Constants.Extras.ARTIST_NAME);
            trackBeans = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ten_tracks, container, false);

        ListView listView = ButterKnife.findById(view, android.R.id.list);
        trackAdapter = new TrackAdapter(getActivity(), trackBeans);
        listView.setAdapter(trackAdapter);

        if (savedInstanceState == null) {
            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put(SpotifyService.COUNTRY, Constants.Countries.MX);
            SpotifyWS.getSpotifyService().getArtistTopTrack(artistId, queryMap, new Callback<Tracks>() {
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
                        }
                    });
                }

                @Override
                public void failure(final RetrofitError error) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showToastMessage(error.getMessage());
                        }
                    });
                }
            });
        }

        setActionBarSubTitle(artistName);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(ARTIST_ID_PARAM, artistId);
        outState.putString(ARTIST_NAME_PARAM, artistName);
        outState.putParcelableArrayList(TRACK_BEANS_PARAM, trackBeans);
        super.onSaveInstanceState(outState);
    }
}
