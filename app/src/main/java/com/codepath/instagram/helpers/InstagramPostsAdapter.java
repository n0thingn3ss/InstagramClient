package com.codepath.instagram.helpers;


import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Date;
import java.util.List;

public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.InstagramPostViewHolder>{
    List<InstagramPost> mPosts;

    public InstagramPostsAdapter(List<InstagramPost> posts) {
        this.mPosts = posts;
    }

    @Override
    public InstagramPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View instagramPostView = inflater.inflate(R.layout.post_item, parent, false);
        InstagramPostViewHolder viewHolder = new InstagramPostViewHolder(instagramPostView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramPostViewHolder holder, int position) {
        InstagramPost post = mPosts.get(position);

        holder.mTvUserName.setText(post.user.userName);
        holder.mSdvUserImage.setImageURI(Uri.parse(post.user.profilePictureUrl));
        holder.mTvPostDate.setText(DateUtils.getRelativeTimeSpanString(post.createdTime * 1000));
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class InstagramPostViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvUserName;
        public TextView mTvPostDate;
        public SimpleDraweeView mSdvUserImage;


        public InstagramPostViewHolder(View instagramPostView) {
            super(instagramPostView);
            mTvUserName = (TextView) instagramPostView.findViewById(R.id.tvUserName);
            mSdvUserImage = (SimpleDraweeView) instagramPostView.findViewById(R.id.sdvUserImage);
            mTvPostDate = (TextView) instagramPostView.findViewById(R.id.tvPostDate);
        }
    }
}
