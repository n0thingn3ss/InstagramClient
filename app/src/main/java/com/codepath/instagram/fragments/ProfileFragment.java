package com.codepath.instagram.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;


import cz.msebera.android.httpclient.Header;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private SimpleDraweeView mUserImage;
    private TextView mNoPhotos;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    public ProfileFragment() {}

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        mUserImage = (SimpleDraweeView) v.findViewById(R.id.sdvUserImage);
        mNoPhotos = (TextView) v.findViewById(R.id.tvNoPhotos);

        fetchProfile();

        return v;
    }

    public void fetchProfile() {
        MainApplication.getRestClient().getUserProfile(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d(TAG, "response code:" + statusCode + "\n" + response.toString());
                try {
                    InstagramUser user = InstagramUser.fromJson(response.getJSONObject("data"));
                    mUserImage.setImageURI(Uri.parse(user.profilePictureUrl));
                    mNoPhotos.setText(user.photoCount + " " + getString(R.string.profile_photos));

                    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.flPhotoGridContainer, PhotoGridFragment.newInstance(user.userId, ""));
                    ft.commit();
                } catch(JSONException je) {
                    je.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String res, Throwable throwable) {
                super.onFailure(statusCode, headers, res, throwable);
                Log.d(TAG, "response code:" + statusCode + "::" + res);
            }
        });
    }
}
