package mx.eduardopool.spotifystreamer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.commons.Constants;
import mx.eduardopool.spotifystreamer.fragments.TopTenTracksActivityFragment;

public class TopTenTracksActivity extends BaseActivity {

    public static Intent getLaunchIntent(Context context, String artistId, String artistName) {
        Intent intent = new Intent(context, TopTenTracksActivity.class);
        intent.putExtra(Constants.Extras.ARTIST_ID, artistId);
        intent.putExtra(Constants.Extras.ARTIST_NAME, artistName);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top_ten_tracks);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(Constants.Tags.TOP_TEN_TRACKS_ACTIVITY_FRAGMENT);
        if (fragment == null) {
            Intent intent = getIntent();
            String artistId = intent.getStringExtra(Constants.Extras.ARTIST_ID);
            String artistName = intent.getStringExtra(Constants.Extras.ARTIST_NAME);
            fragment = TopTenTracksActivityFragment.newInstance(artistId, artistName);
            fragmentManager.beginTransaction()
                    .add(R.id.container, fragment, Constants.Tags.TOP_TEN_TRACKS_ACTIVITY_FRAGMENT)
                    .commit();
        }

    }

}
