package com.codepath.instagram.helpers;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.codepath.instagram.models.InstagramSearchTag;
import com.codepath.instagram.models.InstagramUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

public class Utils {
    private static final String TAG = "Utils";
    private static final NumberFormat numberFormat;

    static {
        numberFormat = NumberFormat.getInstance();
        numberFormat.setGroupingUsed(true);
    }

    public static String formatNumberForDisplay(int number) {
        return numberFormat.format(number);
    }

    public static JSONObject loadJsonFromAsset(Context context, String fileName) throws IOException, JSONException {
        InputStream inputStream = context.getResources().getAssets().open(fileName);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));

        String line;
        StringBuilder builder = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            builder.append(line);
        }

        JSONObject result = new JSONObject(builder.toString());

        inputStream.close();
        bufferedReader.close();

        return result;
    }

    public static List<InstagramPost> decodePostsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramPost> posts = InstagramPost.fromJson(getDataJsonArray(jsonObject));
        return posts == null ? new ArrayList<InstagramPost>() : posts;
    }

    public static List<InstagramComment> decodeCommentsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramComment> comments = InstagramComment.fromJson(getDataJsonArray(jsonObject));
        return comments == null ? new ArrayList<InstagramComment>() : comments;
    }

    public static List<InstagramUser> decodeUsersFromJsonResponse(JSONObject jsonObject) {
        List<InstagramUser> users = InstagramUser.fromJson(getDataJsonArray(jsonObject));
        return users == null ? new ArrayList<InstagramUser>() : users;
    }

    public static List<InstagramSearchTag> decodeSearchTagsFromJsonResponse(JSONObject jsonObject) {
        List<InstagramSearchTag> searchTags = InstagramSearchTag.fromJson(getDataJsonArray(jsonObject));
        return searchTags == null ? new ArrayList<InstagramSearchTag>() : searchTags;
    }

    public static void renderStyledUserNameWithExtraText(String userName, String extraText, TextView tv, Context ctx) {
        SpannableStringBuilder ssb = new SpannableStringBuilder(userName + " " + extraText);
        ssb.setSpan(
                new ForegroundColorSpan(
                        ctx.getResources().getColor(R.color.blue_text)
                ),
                0,
                userName.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        ssb.setSpan(
                new TypefaceSpan(
                        "sans-serif-medium"
                ),
                0,
                userName.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        tv.setText(ssb);
        return;

    }

    private static JSONArray getDataJsonArray(JSONObject jsonObject) {
        JSONArray jsonArray = null;
        if (jsonObject != null) {
            jsonArray = jsonObject.optJSONArray("data");
        }
        return jsonArray;
    }
}
