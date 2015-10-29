package com.codepath.instagram.adapters;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.InstagramPostViewHolder>{
    List<InstagramPost> mPosts;
    Context mCtx;

    public InstagramPostsAdapter(List<InstagramPost> posts, Context ctx) {
        if (posts == null) {
            this.mPosts = new ArrayList<InstagramPost>();
        } else {
            this.mPosts = posts;
        }
        mCtx = ctx;
    }

    public void add(List<InstagramPost> posts) {
        if (posts != null) {
            this.mPosts.addAll(posts);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public InstagramPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View instagramPostView = inflater.inflate(R.layout.layout_post_item, parent, false);
        InstagramPostViewHolder viewHolder = new InstagramPostViewHolder(instagramPostView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramPostViewHolder holder, int position) {
        InstagramPost post = mPosts.get(position);

        holder.mTvUserName.setText(post.user.userName);
        holder.mTvPostDate.setText(DateUtils.getRelativeTimeSpanString(post.createdTime * 1000));
        holder.mSdvUserImage.setImageURI(Uri.parse(post.user.profilePictureUrl));
        holder.mSdvPostImage.setImageURI(Uri.parse(post.image.imageUrl));

        if (post.likesCount > 0) {
            holder.mTvLikes.setText(post.likesCount + " likes");
        }

        if (post.caption != null && post.caption != "") {
            String userName = post.user.userName;

            SpannableStringBuilder ssb = new SpannableStringBuilder(userName + " " + post.caption);
            ssb.setSpan(
                    new ForegroundColorSpan(
                            mCtx.getResources().getColor(R.color.blue_text)
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
            holder.mTvUserNameDesc.setText(ssb);
            holder.mTvUserNameDesc.setVisibility(View.VISIBLE);
        } else {
            holder.mTvUserNameDesc.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class InstagramPostViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvUserName;
        public TextView mTvPostDate;
        public SimpleDraweeView mSdvUserImage;
        public SimpleDraweeView mSdvPostImage;
        public TextView mTvLikes;
        public TextView mTvUserNameDesc;

        public InstagramPostViewHolder(View instagramPostView) {
            super(instagramPostView);

            mTvUserName = (TextView) instagramPostView.findViewById(R.id.tvUserName);
            mSdvUserImage = (SimpleDraweeView) instagramPostView.findViewById(R.id.sdvUserImage);
            mTvPostDate = (TextView) instagramPostView.findViewById(R.id.tvPostDate);
            mSdvPostImage = (SimpleDraweeView) instagramPostView.findViewById(R.id.sdvPostImage);
            mTvLikes = (TextView) instagramPostView.findViewById(R.id.tvLikes);
            mTvUserNameDesc = (TextView) instagramPostView.findViewById(R.id.tvUserNameDesc);
        }
    }
}
