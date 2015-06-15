package mx.eduardopool.spotifystreamer.activities;

import android.app.SearchManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import java.util.List;

/**
 * Base activity for common activities operations.
 * Created by EduardoPool on 6/9/15.
 */
public abstract class BaseActivity extends AppCompatActivity {

    public void showToastMessage(int stringResourceId) {
        showToastMessage(getString(stringResourceId));
    }

    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent(intent);
    }

    /**
     * Handles the search action intent and calls the {@link BaseActivity.OnSearchVoiceResultListener}
     * of all the current fragments who implemented the interface
     *
     * @param intent Intent to validate if is of action search
     */
    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            for (Fragment fragment : fragments) {
                if (fragment instanceof OnSearchVoiceResultListener) {
                    (((OnSearchVoiceResultListener) fragment)).searchByVoiceQuery(query);
                }
            }
        }
    }

    public interface OnSearchVoiceResultListener {
        void searchByVoiceQuery(String voiceQuery);
    }

}
