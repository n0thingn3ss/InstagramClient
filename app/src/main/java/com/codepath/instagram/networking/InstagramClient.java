package com.codepath.instagram.networking;

import android.content.Context;

import com.codepath.instagram.helpers.Constants;
import com.codepath.oauth.OAuthAsyncHttpClient;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.scribe.builder.api.Api;

import java.text.MessageFormat;

public class InstagramClient extends OAuthBaseClient {
    public static final Class<? extends Api> REST_API_CLASS = InstagramApi.class;
    public static final String REST_URL = "https://api.instagram.com/v1";
    public static final String REST_CONSUMER_KEY = "70a6a53f7f56413096cab50643edd709";
    public static final String REST_CONSUMER_SECRET = "88a522068bbd4ed5b523101b98ea2d30";

    private static final String USER_FEED_URL = "users/self/feed";
    private static final String USER_RECENT_PHOTOS = "users/{0}/media/recent";
    private static final String POPULAR_FEED_URL = "media/popular";
    private static final String COMMENT_URL = "media/{0}/comments";
    private static final String SEARCH_USERS_URL = "users/search";
    private static final String SEARCH_TAGS_URL = "tags/search";
    private static final String TAG_RECENT_PHOTOS = "tags/{0}/media/recent";

    private SyncHttpClient mSyncHttpClient = new SyncHttpClient();


    public InstagramClient(Context context) {
        super(context, REST_API_CLASS, REST_URL,
                REST_CONSUMER_KEY, REST_CONSUMER_SECRET, Constants.REDIRECT_URI, Constants.SCOPE);
    }

    public void getUserFeed(JsonHttpResponseHandler responseHandler) {
        RequestParams params = getDefaultRequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        client.get(getApiUrl(USER_FEED_URL), params, responseHandler);
    }

    public void getPopularFeed(JsonHttpResponseHandler responseHandler) {
        client.get(getApiUrl(POPULAR_FEED_URL), getDefaultRequestParams(), responseHandler);
    }

    public void getUserRecentPhotos(String userId, JsonHttpResponseHandler responseHandler) {
        String url = getApiUrl(MessageFormat.format(USER_RECENT_PHOTOS, userId));
        RequestParams params = getDefaultRequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        client.get(url, params, responseHandler);
    }

    public void getRecentPhotosByTag(String tag, JsonHttpResponseHandler responseHandler) {
        String url = getApiUrl(MessageFormat.format(TAG_RECENT_PHOTOS, tag));
        RequestParams params = getDefaultRequestParams();
        client.get(url, params, responseHandler);
    }

    public void getComments(String mediaId, JsonHttpResponseHandler responseHandler) {
        String url = getApiUrl(MessageFormat.format(COMMENT_URL, mediaId));
        RequestParams params = getDefaultRequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        client.get(url, params, responseHandler);
    }

    public void searchUsers(String srch, JsonHttpResponseHandler responseHandler) {
        RequestParams params = getDefaultRequestParams();
        params.put("q", srch);
        client.get(getApiUrl(SEARCH_USERS_URL), params, responseHandler);
    }

    public void searchTags(String srch, JsonHttpResponseHandler responseHandler) {
        RequestParams params = getDefaultRequestParams();
        params.put("q", srch);
        client.get(getApiUrl(SEARCH_TAGS_URL), params, responseHandler);
    }

    public void getUserFeedSynchronously(AsyncHttpResponseHandler responseHandler) {
        RequestParams params = getDefaultRequestParams();
        params.put("access_token", client.getAccessToken().getToken());
        mSyncHttpClient.get(getApiUrl(USER_FEED_URL), params, responseHandler);
    }

    private static RequestParams getDefaultRequestParams() {
        RequestParams params = new RequestParams();
        params.put("client_id", REST_CONSUMER_KEY);
        return params;
    }
}
