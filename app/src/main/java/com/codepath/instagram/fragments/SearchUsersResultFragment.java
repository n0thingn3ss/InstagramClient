package com.codepath.instagram.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.SearchUserResultsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.DividerItemDecoration;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramUser;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


public class SearchUsersResultFragment extends Fragment {
    private static final String TAG = "SearchUsersResultFragment";

    private ArrayList<InstagramUser> users;
    private SearchUserResultsAdapter mSurAdapter;

    public static SearchUsersResultFragment newInstance() {
        return new SearchUsersResultFragment();
    }

    public SearchUsersResultFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_users, container, false);

        users = new ArrayList<>();

        RecyclerView rvSearchResults = (RecyclerView) view.findViewById(R.id.rvSearchResults);
        RecyclerView.ItemDecoration itemDecoration =  new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL_LIST);
        rvSearchResults.addItemDecoration(itemDecoration);
        mSurAdapter = new SearchUserResultsAdapter(users);

        // Set Adapter
        rvSearchResults.setAdapter(mSurAdapter);

        // Set layout
        rvSearchResults.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    public void fetchSearchResults(String searchTerm) {
        MainApplication.getRestClient().searchUsers(searchTerm, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "response code:" + statusCode + "\n" + response.toString());
                mSurAdapter.replaceAll(Utils.decodeUsersFromJsonResponse(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable throwable) {
                super.onFailure(statusCode, headers, res, throwable);
                Log.d(TAG, "response code:" + statusCode + "::" + res);
            }
        });
    }
}