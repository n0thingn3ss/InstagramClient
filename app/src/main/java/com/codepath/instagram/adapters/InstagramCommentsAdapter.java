package com.codepath.instagram.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class InstagramCommentsAdapter extends RecyclerView.Adapter<InstagramCommentsAdapter.InstagramCommentViewHolder> {
    private List<InstagramComment> mComments;

    public InstagramCommentsAdapter(List<InstagramComment> comments) {
        if (comments != null) {
            mComments = comments;
        } else {
            mComments = new ArrayList<InstagramComment>();
        }
    }

    public void add(List<InstagramComment> posts) {
        if (posts != null) {
            this.mComments.addAll(posts);
            this.notifyDataSetChanged();
        }
    }

    @Override
    public InstagramCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View instagramCommentView = inflater.inflate(R.layout.layout_comment_item, parent, false);
        InstagramCommentViewHolder viewHolder = new InstagramCommentViewHolder(instagramCommentView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(InstagramCommentViewHolder holder, int position) {
        InstagramComment comment = mComments.get(position);

        holder.mSdvUserImage.setImageURI(Uri.parse(comment.user.profilePictureUrl));
        Utils.renderStyledUserNameWithExtraText(comment.user.userName, comment.text, holder.mTvComment, holder.mCtx);
        holder.mTvCommentDate.setText(DateUtils.getRelativeTimeSpanString(comment.createdTime * 1000));
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    public static class InstagramCommentViewHolder extends RecyclerView.ViewHolder {
        public SimpleDraweeView mSdvUserImage;
        public TextView mTvComment;
        public TextView mTvCommentDate;
        public Context mCtx;

        public InstagramCommentViewHolder(View commentView) {
            super(commentView);

            mCtx = commentView.getContext();
            mSdvUserImage = (SimpleDraweeView) commentView.findViewById(R.id.sdvUserImage);
            mTvComment = (TextView) commentView.findViewById(R.id.tvComment);
            mTvCommentDate = (TextView) commentView.findViewById(R.id.tvCommentDate);
        }
    }
}
