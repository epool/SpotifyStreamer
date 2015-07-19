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
import mx.eduardopool.spotifystreamer.fragments.TrackPlayerActivityDialogFragment;

public class TrackPlayerActivity extends BaseActivity {

    public static Intent getLaunchIntent(Context context, ArtistBean artistBean, ArrayList<TrackBean> trackBeans, int trackBeanSelectedIndex) {
        Intent intent = new Intent(context, TrackPlayerActivity.class);
        intent.putExtra(Constants.Extras.ARTIST_BEAN, artistBean);
        intent.putExtra(Constants.Extras.TRACK_BEANS, trackBeans);
        intent.putExtra(Constants.Extras.TRACK_BEAN_SELECTED_INDEX, trackBeanSelectedIndex);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_player);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            ArtistBean artistBean = extras.getParcelable(Constants.Extras.ARTIST_BEAN);
            ArrayList<TrackBean> trackBeans = extras.getParcelableArrayList(Constants.Extras.TRACK_BEANS);
            int trackBeanSelectedIndex = extras.getInt(Constants.Extras.TRACK_BEAN_SELECTED_INDEX);
            Fragment fragment = TrackPlayerActivityDialogFragment.newInstance(artistBean, trackBeans, trackBeanSelectedIndex);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.track_player_container, fragment)
                    .commit();
        }
    }

}
