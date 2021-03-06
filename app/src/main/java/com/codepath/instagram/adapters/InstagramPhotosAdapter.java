package com.codepath.instagram.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.codepath.instagram.R;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class InstagramPhotosAdapter extends RecyclerView.Adapter<InstagramPhotosAdapter.PhotoViewHolder> {
    private static final String TAG = "InstagramPhotosAdapter";

    private List<InstagramPost> mPosts;

    public void add(List<InstagramPost> newPosts) {
        mPosts.clear();
        for (InstagramPost post : newPosts) {
            mPosts.add(post);
        }
    }

    public InstagramPhotosAdapter(List<InstagramPost> posts) {
        this.mPosts = (posts == null ? new ArrayList<InstagramPost>() : posts);
    }

    @Override
    public InstagramPhotosAdapter.PhotoViewHolder onCreateViewHolder(
            ViewGroup viewGroup,
            int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.layout_photo_item, viewGroup, false);
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(InstagramPhotosAdapter.PhotoViewHolder holder, int position) {
        final InstagramPost instagramPost = mPosts.get(position);

        // Reset image views
        holder.sdvPhoto.setImageURI(null);

        int width = instagramPost.image.imageWidth;
        int height = instagramPost.image.imageHeight;

        float aspectRatio = height > 0 ? (float) width / (float) height : 1;

        Uri imageUri = Uri.parse(instagramPost.image.imageUrl);
        holder.sdvPhoto.setImageURI(imageUri);
        holder.sdvPhoto.setAspectRatio(aspectRatio);
    }

    @Override
    public int getItemCount() {
        return (mPosts == null ? 0 : mPosts.size());
    }

    public static final class PhotoViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView sdvPhoto;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            sdvPhoto = (SimpleDraweeView) itemView.findViewById(R.id.sdvPhoto);
        }
    }


}