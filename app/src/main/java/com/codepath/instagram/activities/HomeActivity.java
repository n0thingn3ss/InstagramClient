package com.codepath.instagram.activities;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PostsFragment;
import com.codepath.instagram.helpers.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsNetworkAvailable()) {
            return;
        }

        Fresco.initialize(this);

        setContentView(R.layout.activity_home);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        PostsFragment postsFragment = new PostsFragment();
        ft.replace(R.id.flPostsContainer, postsFragment);
        ft.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!checkIsNetworkAvailable()) {
            return;
        }
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

    public boolean checkIsNetworkAvailable() {
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Unable to connect to network. Check later.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
