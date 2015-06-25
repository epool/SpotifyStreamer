package mx.eduardopool.spotifystreamer.activities;

import android.os.Bundle;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.fragments.MainActivityFragment;
import mx.eduardopool.spotifystreamer.fragments.TopTenTracksActivityFragment;


public class MainActivity extends BaseActivity implements MainActivityFragment.Callback {
    private boolean mIsTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mIsTablet = findViewById(R.id.top_ten_tracks_container) != null;
    }

    @Override
    public void onArtistClicked(ArtistBean artistBean) {
        if (mIsTablet) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.top_ten_tracks_container, TopTenTracksActivityFragment.newInstance(artistBean))
                    .commit();
        } else {
            startActivity(TopTenTracksActivity.getLaunchIntent(this, artistBean));
        }
    }
}
