package mx.eduardopool.spotifystreamer.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import mx.eduardopool.spotifystreamer.activities.BaseActivity;

/**
 * Base fragment for common fragments operations.
 * Created by EduardoPool on 6/9/15.
 */
public abstract class BaseDialogFragment extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutResource(), container, false);

        ButterKnife.inject(this, view);

        return view;
    }

    protected abstract int getLayoutResource();

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
