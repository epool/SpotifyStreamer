package mx.eduardopool.spotifystreamer.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import java.util.ArrayList;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.beans.TrackBean;
import mx.eduardopool.spotifystreamer.fragments.MainActivityFragment;
import mx.eduardopool.spotifystreamer.fragments.TopTenTracksActivityFragment;
import mx.eduardopool.spotifystreamer.fragments.TrackPlayerActivityDialogFragment;


public class MainActivity extends BaseActivity implements MainActivityFragment.Callback, TopTenTracksActivityFragment.Callback {
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

    @Override
    public void onTrackClicked(ArtistBean artistBean, ArrayList<TrackBean> trackBeans, int trackBeanSelectedIndex) {
        if (mIsTablet) {
            TrackPlayerActivityDialogFragment trackPlayerActivityDialogFragment = TrackPlayerActivityDialogFragment.newInstance(artistBean, trackBeans, trackBeanSelectedIndex);
            // trackPlayerActivityDialogFragment.show(getSupportFragmentManager(), "TrackPlayerActivityDialogFragment");
            // DialogFragment.show() will take care of adding the fragment
            // in a transaction.  We also want to remove any currently showing
            // dialog, so make our own transaction and take care of that here.
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            trackPlayerActivityDialogFragment.show(ft, "dialog");
        }
    }
}
