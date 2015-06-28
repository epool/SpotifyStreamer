package mx.eduardopool.spotifystreamer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.beans.TrackBean;
import mx.eduardopool.spotifystreamer.commons.Constants;
import mx.eduardopool.spotifystreamer.fragments.TrackPlayerActivityFragment;

public class TrackPlayerActivity extends BaseActivity {

    public static Intent getLaunchIntent(Context context, ArtistBean artistBean, TrackBean trackBean) {
        Intent intent = new Intent(context, TrackPlayerActivity.class);
        intent.putExtra(Constants.Extras.ARTIST_BEAN, artistBean);
        intent.putExtra(Constants.Extras.TRACK_BEAN, trackBean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            ArtistBean artistBean = intent.getParcelableExtra(Constants.Extras.ARTIST_BEAN);
            TrackBean trackBean = intent.getParcelableExtra(Constants.Extras.TRACK_BEAN);
            Fragment fragment = TrackPlayerActivityFragment.newInstance(artistBean, trackBean);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.track_player_container, fragment)
                    .commit();
        }
    }

}
