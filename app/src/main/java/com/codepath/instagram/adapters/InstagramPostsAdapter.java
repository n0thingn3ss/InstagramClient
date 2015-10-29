package com.codepath.instagram.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.InstagramPostViewHolder> {
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
            Utils.renderStyledUserNameWithExtraText(post.user.userName, post.caption, holder.mTvUserNameDesc, mCtx);
            holder.mTvUserNameDesc.setVisibility(View.VISIBLE);
        } else {
            holder.mTvUserNameDesc.setVisibility(View.GONE);
        }

        holder.mLlComments.removeAllViews();
        holder.mTvViewAllComments.setVisibility(View.GONE);

        if (post.commentsCount > 0) {
            holder.mTvViewAllComments.setText(String.format(mCtx.getString(R.string.view_all_comments), post.commentsCount));
            holder.mTvViewAllComments.setVisibility(View.VISIBLE);

            // TODO Probably this is wrong you are adding multiple event listeners but when you add to
            // the view holder like book search example you are adding it only once
            holder.mTvViewAllComments.setOnClickListener(new View.OnClickListener() {
                private String mMediaId;
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mCtx, CommentsActivity.class);
                    i.putExtra("mediaId", mMediaId);
                    mCtx.startActivity(i);
                }

                private View.OnClickListener init(String mediaId) {
                    mMediaId = mediaId;
                    return this;
                }
            }.init(post.mediaId));

            for (InstagramComment comment : post.comments.subList(post.comments.size() - 3, post.comments.size() - 1)) {
                TextView tvComment = (TextView) LayoutInflater.from(mCtx).inflate(R.layout.layout_comment_item_text, null, false);
                Utils.renderStyledUserNameWithExtraText(comment.user.userName, comment.text, tvComment, mCtx);
                holder.mLlComments.addView(tvComment);
            }
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
        public TextView mTvViewAllComments;
        public LinearLayout mLlComments;

        public InstagramPostViewHolder(View instagramPostView) {
            super(instagramPostView);

            mTvUserName = (TextView) instagramPostView.findViewById(R.id.tvUserName);
            mSdvUserImage = (SimpleDraweeView) instagramPostView.findViewById(R.id.sdvUserImage);
            mTvPostDate = (TextView) instagramPostView.findViewById(R.id.tvPostDate);
            mSdvPostImage = (SimpleDraweeView) instagramPostView.findViewById(R.id.sdvPostImage);
            mTvLikes = (TextView) instagramPostView.findViewById(R.id.tvLikes);
            mTvUserNameDesc = (TextView) instagramPostView.findViewById(R.id.tvUserNameDesc);
            mTvViewAllComments = (TextView) instagramPostView.findViewById(R.id.tvViewAllComments);
            mLlComments = (LinearLayout) instagramPostView.findViewById(R.id.llComments);
        }
    }
}
