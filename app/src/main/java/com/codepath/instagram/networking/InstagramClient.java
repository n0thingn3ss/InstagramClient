package com.codepath.instagram.networking;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

public class InstagramClient {
    public static void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        String url = "https://api.instagram.com/v1/media/popular?client_id=70a6a53f7f56413096cab50643edd709";

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, responseHandler);
    }

    public static void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        String url = "https://api.instagram.com/v1/media/" + mediaId + "/comments?client_id=70a6a53f7f56413096cab50643edd709";
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, responseHandler);
    }
}
