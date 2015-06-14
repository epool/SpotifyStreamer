package mx.eduardopool.spotifystreamer.ws;

import com.facebook.stetho.okhttp.StethoInterceptor;
import com.squareup.okhttp.OkHttpClient;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Spotify Web Service class.
 * Created by EduardoPool on 6/13/15.
 */
public class SpotifyWS {
    private static SpotifyService spotifyService;

    public static void init() {
        OkHttpClient client = new OkHttpClient();
        client.networkInterceptors().add(new StethoInterceptor());

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(SpotifyApi.SPOTIFY_WEB_API_ENDPOINT)
                .setClient(new OkClient(client))
                .build();
        spotifyService = restAdapter.create(SpotifyService.class);
    }

    public static SpotifyService getSpotifyService() {
        return spotifyService;
    }
}
