package mx.eduardopool.spotifystreamer.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.commons.Constants;
import mx.eduardopool.spotifystreamer.fragments.MainActivityFragment;


public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(Constants.Tags.MAIN_ACTIVITY_FRAGMENT);
        if (fragment == null) {
            fragment = MainActivityFragment.newInstance();
            fragmentManager.beginTransaction()
                    .add(R.id.container, fragment, Constants.Tags.MAIN_ACTIVITY_FRAGMENT)
                    .commit();
        }

    }

}
