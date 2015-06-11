package mx.eduardopool.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;

import mx.eduardopool.spotifystreamer.commons.Constants;

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_ten_tracks, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
