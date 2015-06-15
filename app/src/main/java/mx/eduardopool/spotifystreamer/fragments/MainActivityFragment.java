package mx.eduardopool.spotifystreamer.fragments;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

import butterknife.ButterKnife;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import mx.eduardopool.spotifystreamer.R;
import mx.eduardopool.spotifystreamer.activities.BaseActivity;
import mx.eduardopool.spotifystreamer.activities.TopTenTracksActivity;
import mx.eduardopool.spotifystreamer.adapters.ArtistAdapter;
import mx.eduardopool.spotifystreamer.beans.ArtistBean;
import mx.eduardopool.spotifystreamer.util.ViewUtil;
import mx.eduardopool.spotifystreamer.ws.SpotifyWS;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends BaseFragment implements SearchView.OnQueryTextListener, BaseActivity.OnSearchVoiceResultListener {
    private final static String QUERY_PARAM = "query";
    private final static String ARTIST_BEANS_PARAM = "artistBeans";
    private final static String IS_EXPANDED_PARAM = "isSearchViewExpanded";

    boolean isSearchViewExpanded;
    private ArtistAdapter artistAdapter;
    private SearchView searchView;
    private String query;
    private ArrayList<ArtistBean> artistBeans;

    public MainActivityFragment() {
    }

    public static MainActivityFragment newInstance() {
        MainActivityFragment mainActivityFragment = new MainActivityFragment();
        Bundle args = new Bundle();
        mainActivityFragment.setArguments(args);
        return mainActivityFragment;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        ListView listView = ButterKnife.findById(view, android.R.id.list);
        artistAdapter = new ArtistAdapter(getBaseActivity(), artistBeans);
        listView.setAdapter(artistAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ArtistBean artistBean = artistAdapter.getItem(position);
                startActivity(TopTenTracksActivity.getLaunchIntent(getBaseActivity(), artistBean.getId(), artistBean.getName()));
            }
        });

        return view;
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
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Clear query
                searchView.setQuery("", false);
                query = "";
            }
        });
        ViewUtil.disableSearchViewContextMenu(searchView);
        MenuItemCompat.setOnActionExpandListener(searchMenuItem,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        searchView.post(new Runnable() {
                            @Override
                            public void run() {
                                searchView.setQuery(query, false);
                            }
                        });
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
        SpotifyWS.getSpotifyService().searchArtists(query, new Callback<ArtistsPager>() {
            @Override
            public void success(final ArtistsPager artistsPager, Response response) {
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                });
            }

            @Override
            public void failure(final RetrofitError error) {
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showToastMessage(error.getMessage());
                    }
                });
            }
        });
    }

}
