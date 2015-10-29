package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.InstagramPostsAdapter;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.networking.InstagramClient;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;


public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static RecyclerView mRvPosts;
    private static InstagramPostsAdapter mIgPostsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Unable to connect to network. Check later.", Toast.LENGTH_LONG).show();
            return;
        }

        Fresco.initialize(this);

        setContentView(R.layout.activity_home);

        mRvPosts = (RecyclerView) findViewById(R.id.rvPosts);
        mIgPostsAdapter = new InstagramPostsAdapter(null, this);
        mRvPosts.setAdapter(mIgPostsAdapter);
        mRvPosts.setLayoutManager(new LinearLayoutManager(this));

        InstagramClient.getPopularFeed(getPopularPostResponseHander());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private List<InstagramPost> fetchPosts() {
        List<InstagramPost> posts = new ArrayList<>();

        try {
            posts = Utils.decodePostsFromJsonResponse(Utils.loadJsonFromAsset(this, "popular.json"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }

    private JsonHttpResponseHandler getPopularPostResponseHander() {
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
            }
        };
    }

}
