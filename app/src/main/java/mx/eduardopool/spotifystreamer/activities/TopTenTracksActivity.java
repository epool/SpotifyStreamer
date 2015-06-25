package mx.eduardopool.spotifystreamer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.commons.Constants;
import mx.eduardopool.spotifystreamer.fragments.TopTenTracksActivityFragment;

public class TopTenTracksActivity extends BaseActivity {

    public static Intent getLaunchIntent(Context context, ArtistBean artistBean) {
        Intent intent = new Intent(context, TopTenTracksActivity.class);
        intent.putExtra(Constants.Extras.ARTIST_BEAN, artistBean);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten_tracks);

        if (savedInstanceState == null) {
            Intent intent = getIntent();
            ArtistBean artistBean = intent.getParcelableExtra(Constants.Extras.ARTIST_BEAN);
            Fragment fragment = TopTenTracksActivityFragment.newInstance(artistBean);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.top_ten_tracks_container, fragment, Constants.Tags.TOP_TEN_TRACKS_ACTIVITY_FRAGMENT)
                    .commit();
        }
    }

}
