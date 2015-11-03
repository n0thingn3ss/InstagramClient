package com.codepath.instagram.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.codepath.instagram.R;
import com.codepath.instagram.adapters.HomeFragmentStatePagerAdapter;
import com.codepath.instagram.helpers.NonSwipeableViewPager;
import com.codepath.instagram.helpers.Utils;
import com.facebook.drawee.backends.pipeline.Fresco;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";
    private static final String POSITION = "POSITION";
    private ViewPager mNsvp;
    private TabLayout mTl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Fresco.initialize(this);

        setContentView(R.layout.activity_home);

        mNsvp = (NonSwipeableViewPager) findViewById(R.id.nsvpPager);
        FragmentManager fm = getSupportFragmentManager();

        HomeFragmentStatePagerAdapter adapter = new HomeFragmentStatePagerAdapter(fm, this);
        mNsvp.setAdapter(adapter);

        mTl = (TabLayout) findViewById(R.id.tlToolbar);
        mTl.setupWithViewPager(mNsvp);
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

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        super.onSaveInstanceState(currentState);
        currentState.putInt(POSITION, 0);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mNsvp.setCurrentItem(savedInstanceState.getInt(POSITION));
    }

    public boolean checkIsNetworkAvailable() {
        if (!Utils.isNetworkAvailable(this)) {
            Toast.makeText(this, "Unable to connect to network. Check later.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}
