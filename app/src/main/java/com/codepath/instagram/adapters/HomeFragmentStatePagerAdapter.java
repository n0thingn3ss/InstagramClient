package com.codepath.instagram.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.codepath.instagram.R;
import com.codepath.instagram.fragments.PostsFragment;
import com.codepath.instagram.fragments.SearchFragment;
import com.codepath.instagram.helpers.SmartFragmentStatePagerAdapter;

public class HomeFragmentStatePagerAdapter extends SmartFragmentStatePagerAdapter {
    private Context mCtx;
    private static final int NUM_ITEMS = 5;
    private int[] mImageId = {
            R.drawable.ic_home,
            R.drawable.ic_search,
            R.drawable.ic_capture,
            R.drawable.ic_notifs,
            R.drawable.ic_profile
    };

    public HomeFragmentStatePagerAdapter(FragmentManager fragmentManager, Context ctx) {
        super(fragmentManager);
        this.mCtx = ctx;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch(position) {
            case 0:
                fragment = PostsFragment.newInstance();
                break;
            case 1:
                fragment = SearchFragment.newInstance();
                break;
            default:
                fragment = PostsFragment.newInstance();
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(mCtx, mImageId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}