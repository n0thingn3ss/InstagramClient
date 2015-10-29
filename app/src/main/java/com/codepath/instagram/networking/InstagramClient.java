package com.codepath.instagram.networking;

import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class InstagramClient {
    public static void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        String url = "https://api.instagram.com/v1/media/popular?client_id=70a6a53f7f56413096cab50643edd709";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, responseHandler);
    }


}
