package com.codepath.instagram.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
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
import com.codepath.instagram.models.InstagramPosts;
import com.codepath.instagram.persistence.InstagramClientDatabase;
import com.codepath.instagram.services.PostsIntentService;
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
                PostsFragment.this.fetchPosts();
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

    @Override
    public void onResume() {
        super.onResume();
        // Register for the particular broadcast based on ACTION string
        IntentFilter filter = new IntentFilter(PostsIntentService.ACTION);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(postsReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister the listener when the application is paused
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(postsReceiver);
    }

    private void fetchPosts() {
        if (Utils.isNetworkAvailable(getActivity())) {
            Intent i = new Intent(getActivity(), PostsIntentService.class);
            getActivity().startService(i);
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

    private BroadcastReceiver postsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getIntExtra(PostsIntentService.KEY_RESULT_CODE, Activity.RESULT_CANCELED) == Activity.RESULT_OK) {
                InstagramPosts postsObj = (InstagramPosts) intent.getSerializableExtra(
                        PostsIntentService.KEY_RESULTS);
                mIgPostsAdapter.add(postsObj.mPosts);
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                // handle failure
            }
        }
    };
}
