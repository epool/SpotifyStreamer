package mx.eduardopool.spotifystreamer.util;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.widget.SearchView;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import mx.eduardopool.spotifystreamer.R;

/**
 * Utils for views.
 * Created by EduardoPool on 6/10/15.
 */
public class ViewUtil {

    /**
     * Disables the SearchView context menu(Select, cut, copy and paste).
     *
     * @param searchView SearchView for disable the context menu.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void disableSearchViewContextMenu(SearchView searchView) {
        EditText searchViewEditText = ((EditText) searchView.findViewById(R.id.search_src_text));
        disableEditTextContextMenu(searchViewEditText);
    }

    /**
     * Disables the EditText context menu(Select, cut, copy and paste).
     *
     * @param editText EditText for disable the context menu.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static void disableEditTextContextMenu(EditText editText) {
        editText.setCustomSelectionActionModeCallback(new ActionMode.Callback() {
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                return false;
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {

            }
        });
    }

}
