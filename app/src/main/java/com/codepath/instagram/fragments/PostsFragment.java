package com.codepath.instagram.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class PostsFragment extends Fragment {
    private static final String TAG = "PostsFragment";
    private static RecyclerView mRvPosts;
    private static InstagramPostsAdapter mIgPostsAdapter;

    public PostsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts, container, false);

        mRvPosts = (RecyclerView) v.findViewById(R.id.rvPosts);
        mIgPostsAdapter = new InstagramPostsAdapter(null, v.getContext());
        mRvPosts.setAdapter(mIgPostsAdapter);
        mRvPosts.setLayoutManager(new LinearLayoutManager(v.getContext()));

        MainApplication.getRestClient().getUserFeed(getUserFeedResponseHander());

        return v;
    }

    private JsonHttpResponseHandler getUserFeedResponseHander() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                Log.d(TAG, "response code:" + statusCode + "\n" + response.toString());
                mIgPostsAdapter.add(Utils.decodePostsFromJsonResponse(response));
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "response code:" + statusCode + "::" + res);
            }
        };
    }
}
