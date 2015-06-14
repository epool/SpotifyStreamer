package mx.eduardopool.spotifystreamer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import mx.eduardopool.spotifystreamer.adapters.TrackAdapter;
import mx.eduardopool.spotifystreamer.commons.Constants;
import mx.eduardopool.spotifystreamer.ws.SpotifyWS;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class TopTenTracksActivityFragment extends BaseFragment {
    private TrackAdapter trackAdapter;
    private String artistId;
    private String artistName;

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

        artistId = getArguments().getString(Constants.Extras.ARTIST_ID);
        artistName = getArguments().getString(Constants.Extras.ARTIST_NAME);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top_ten_tracks, container, false);

        ListView listView = (ListView) view.findViewById(android.R.id.list);
        if (trackAdapter == null) {
            trackAdapter = new TrackAdapter(getActivity(), new ArrayList<Track>());

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put(SpotifyService.COUNTRY, Constants.Countries.MX);
            SpotifyWS.getSpotifyService().getArtistTopTrack(artistId, queryMap, new Callback<Tracks>() {
                @Override
                public void success(final Tracks tracks, Response response) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            trackAdapter.clear();
                            if (tracks.tracks.isEmpty()) {
                                showToastMessage(R.string.artist_without_top_tracks);
                            } else {
                                trackAdapter.addAll(tracks.tracks);
                            }
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
        listView.setAdapter(trackAdapter);

        setActionBarSubTitle(artistName);

        return view;
    }

}
