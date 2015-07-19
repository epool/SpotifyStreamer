package mx.eduardopool.spotifystreamer.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.activities.BaseActivity;
import mx.eduardopool.spotifystreamer.adapters.ArtistAdapter;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.util.ViewUtil;
import mx.eduardopool.spotifystreamer.ws.SpotifyWS;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment implements SearchView.OnQueryTextListener, BaseActivity.OnSearchVoiceResultListener {
    private final static String QUERY_PARAM = "query";
    private final static String ARTIST_BEANS_PARAM = "artistBeans";
    private final static String IS_EXPANDED_PARAM = "isSearchViewExpanded";

    @InjectView(R.id.progress_bar_container)
    protected FrameLayout progressBarFrameLayout;
    private boolean isSearchViewExpanded;
    private ArtistAdapter artistAdapter;
    private SearchView searchView;
    private String query;
    private ArrayList<ArtistBean> artistBeans;
    private int mPosition;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            query = savedInstanceState.getString(QUERY_PARAM);
            isSearchViewExpanded = savedInstanceState.getBoolean(IS_EXPANDED_PARAM);
            artistBeans = savedInstanceState.getParcelableArrayList(ARTIST_BEANS_PARAM);
        } else {
            artistBeans = new ArrayList<>();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        assert view != null;

        ListView listView = ButterKnife.findById(view, android.R.id.list);
        artistAdapter = new ArtistAdapter(getBaseActivity(), artistBeans);
        listView.setAdapter(artistAdapter);
        listView.setOnItemClickListener((parent, v, position, id) -> {
            ArtistBean artistBean = artistAdapter.getItem(position);
            ((Callback) getBaseActivity()).onArtistClicked(artistBean);
            mPosition = position;
        });

        return view;
    }

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_main;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(QUERY_PARAM, query);
        outState.putBoolean(IS_EXPANDED_PARAM, isSearchViewExpanded);
        outState.putParcelableArrayList(ARTIST_BEANS_PARAM, artistBeans);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_search_artirst, menu);

        MenuItem searchMenuItem = menu.findItem(R.id.action_search);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getBaseActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setQueryHint(getBaseActivity().getString(R.string.artist_name));
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getBaseActivity().getComponentName()));
        searchView.setOnQueryTextListener(this);
        // Get the search close button image view
        ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);
        // Set on click listener
        closeButton.setOnClickListener(v -> {
            //Clear query
            searchView.setQuery("", false);
            query = "";
        });
        ViewUtil.disableSearchViewContextMenu(searchView);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        searchView.post(() -> searchView.setQuery(query, false));
                        isSearchViewExpanded = true;
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
                        isSearchViewExpanded = false;
                        return true;
                    }
                });

        if (isSearchViewExpanded) {
            searchMenuItem.expandActionView();
            searchView.clearFocus();
        }

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        doSearch(query);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        if (!TextUtils.isEmpty(query)) {
            this.query = query;
        }
        return true;
    }

    @Override
    public void searchByVoiceQuery(String voiceQuery) {
        if (searchView != null) {
            searchView.setQuery(voiceQuery, true);
        }
    }

    private void doSearch(String query) {
        if (TextUtils.isEmpty(query)) {
            showToastMessage(R.string.empty_artist_name);
            return;
        }
        progressBarFrameLayout.setVisibility(View.VISIBLE);
        SpotifyWS.getSpotifyService().searchArtists(query, new retrofit.Callback<ArtistsPager>() {
            @Override
            public void success(final ArtistsPager artistsPager, Response response) {
                getBaseActivity().runOnUiThread(() -> {
                    artistBeans.clear();
                    if (artistsPager.artists.items.isEmpty()) {
                        showToastMessage(R.string.no_artist_found);
                    } else {
                        for (Artist artist : artistsPager.artists.items) {
                            ArtistBean artistBean = new ArtistBean(artist);
                            artistBeans.add(artistBean);
                        }
                    }
                    artistAdapter.notifyDataSetChanged();
                    progressBarFrameLayout.setVisibility(View.GONE);
                });
            }

            @Override
            public void failure(final RetrofitError error) {
                getBaseActivity().runOnUiThread(() -> {
                    showToastMessage(error.getMessage());
                    progressBarFrameLayout.setVisibility(View.GONE);
                });
            }
        });
    }

    public interface Callback {
        void onArtistClicked(ArtistBean artistBean);
    }

}
