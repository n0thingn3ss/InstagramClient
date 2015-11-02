package com.codepath.instagram.networking;

import android.content.Context;

import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthAsyncHttpClient;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;

import java.text.MessageFormat;

public class InstagramClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = InstagramApi.class;
    public static final String REST_URL = "https://api.instagram.com/v1";
    public static final String REST_CONSUMER_KEY = "70a6a53f7f56413096cab50643edd709";
    public static final String REST_CONSUMER_SECRET = "88a522068bbd4ed5b523101b98ea2d30";

    private static final String FEED_URL = "media/popular";
    private static final String COMMENT_URL = "media/{0}/comments";

    public InstagramClient(Context context) {
        super(context, REST_API_CLASS, REST_URL,
                REST_CONSUMER_KEY, REST_CONSUMER_SECRET, Constants.REDIRECT_URI, Constants.SCOPE);
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        client.get(getApiUrl(FEED_URL), getDefaultRequestParams(), responseHandler);
    }

    public void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        String url = getApiUrl(MessageFormat.format(COMMENT_URL, mediaId));
        RequestParams params = getDefaultRequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        client.get(getApiUrl(COMMENT_URL), params, responseHandler);
    }

    private static RequestParams getDefaultRequestParams() {
        RequestParams params = new RequestParams();
        params.put("client_id", REST_CONSUMER_KEY);
        return params;
    }
}
