package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchFragmentStatePagerAdapter;

public class SearchFragment extends Fragment {
    private SearchFragmentStatePagerAdapter mSfsPagerAdapter;
    private TabLayout mTlSearchCategories;
    private ViewPager mVpSearch;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    public SearchFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mSfsPagerAdapter = new SearchFragmentStatePagerAdapter(
                getChildFragmentManager(),
                getActivity()
        );
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        mVpSearch = (ViewPager) view.findViewById(R.id.vpSearch);
        mTlSearchCategories = (TabLayout) view.findViewById(R.id.tlSearch);

        mVpSearch.setAdapter(mSfsPagerAdapter);
        mTlSearchCategories.setupWithViewPager(mVpSearch);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.menu_search, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                searchView.setQuery("", false);
                searchView.setIconified(true);
                searchItem.collapseActionView();
                if (mSfsPagerAdapter != null) {
                    SearchUsersResultFragment usersResultFragment =
                            (SearchUsersResultFragment) mSfsPagerAdapter.getRegisteredFragment(0);
                    if (usersResultFragment != null) {
                        usersResultFragment.fetchSearchResults(query);
                    }

                    SearchTagsResultFragment tagsResultFragment =
                            (SearchTagsResultFragment) mSfsPagerAdapter.getRegisteredFragment(1);
                    if (tagsResultFragment != null) {
                        tagsResultFragment.fetchSearchResults(query);
                    }
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }
}