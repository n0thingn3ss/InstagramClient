package com.codepath.instagram.fragments;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;

public class PostsFragment extends Fragment {
    private static final String TAG = "PostsFragment";
    private RecyclerView mRvPosts;
    private InstagramPostsAdapter mIgPostsAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private InstagramClientDatabase mDb;

    public static PostsFragment newInstance() {
        return new PostsFragment();
    }

    public PostsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDb = InstagramClientDatabase.getInstance(getActivity().getApplicationContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_posts, container, false);

        // Pull to refresh
        mSwipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                fetchPosts();
            }
        });
        // Configure the refreshing colors
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        mRvPosts = (RecyclerView) v.findViewById(R.id.rvPosts);
        mIgPostsAdapter = new InstagramPostsAdapter(null, v.getContext());
        mRvPosts.setAdapter(mIgPostsAdapter);
        mRvPosts.setLayoutManager(new LinearLayoutManager(v.getContext()));

        fetchPosts();

        return v;
    }

    private void fetchPosts() {
        if (Utils.isNetworkAvailable(getActivity())) {
            MainApplication.getRestClient().getUserFeed(getUserFeedResponseHander());
        } else {
            Toast.makeText(getActivity(), "Unable to connect to network. From Cache. Check later.", Toast.LENGTH_SHORT).show();
            mIgPostsAdapter.add(mDb.getAllInstagramPosts());
        }
    }

    private JsonHttpResponseHandler getUserFeedResponseHander() {
        return new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // Root JSON in response is an dictionary i.e { "data : [ ... ] }
                // Handle resulting parsed JSON response here
                Log.d(TAG, "response code:" + statusCode + "\n" + response.toString());
                List<InstagramPost> posts = Utils.decodePostsFromJsonResponse(response);
                mIgPostsAdapter.add(posts);

                mSwipeRefreshLayout.setRefreshing(false);
                mDb.emptyAllTables();
                mDb.addInstagramPosts(posts);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable t) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(TAG, "response code:" + statusCode + "::" + res);
            }
        };
    }
}
