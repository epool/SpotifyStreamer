package mx.eduardopool.spotifystreamer.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.beans.TrackBean;
import mx.eduardopool.spotifystreamer.commons.Constants;
import mx.eduardopool.spotifystreamer.fragments.TopTenTracksActivityFragment;

public class TopTenTracksActivity extends BaseActivity implements TopTenTracksActivityFragment.Callback {

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
                    .add(R.id.top_ten_tracks_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onTrackClicked(ArtistBean artistBean, ArrayList<TrackBean> trackBeans, int trackBeanSelectedIndex) {
        startActivity(TrackPlayerActivity.getLaunchIntent(this, artistBean, trackBeans, trackBeanSelectedIndex));
    }
}
