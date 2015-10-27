package com.codepath.instagram.helpers;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;

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

        holder.mTvPostUser.setText(post.user.userName);
    }

    @Override
    public int getItemCount() {
        return mPosts.size();
    }

    public static class InstagramPostViewHolder extends RecyclerView.ViewHolder {
        public TextView mTvPostUser;

        public InstagramPostViewHolder(View instagramPostView) {
            super(instagramPostView);
            mTvPostUser = (TextView) instagramPostView.findViewById(R.id.tvPostUser);
        }
    }
}
