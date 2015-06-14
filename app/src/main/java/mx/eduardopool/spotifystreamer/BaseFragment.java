package mx.eduardopool.spotifystreamer;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;

/**
 * Base fragment for common fragments operations.
 * Created by EduardoPool on 6/9/15.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);
    }

    protected BaseActivity getBaseActivity() {
        return ((BaseActivity) getActivity());
    }

    protected ActionBar getSupportActionBar() {
        return getBaseActivity().getSupportActionBar();
    }

    public void showToastMessage(int stringResourceId) {
        getBaseActivity().showToastMessage(stringResourceId);
    }

    public void showToastMessage(String message) {
        getBaseActivity().showToastMessage(message);
    }

    protected void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(title);
        }
    }

    protected void setActionBarSubTitle(String subTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setSubtitle(subTitle);
        }
    }

}
