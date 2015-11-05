package com.codepath.instagram.adapters;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.CommentsActivity;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramComment;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.common.executors.CallerThreadExecutor;
import com.facebook.common.references.CloseableReference;
import com.facebook.datasource.DataSource;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.Priority;
import com.facebook.imagepipeline.core.ImagePipeline;
import com.facebook.imagepipeline.datasource.BaseBitmapDataSubscriber;
import com.facebook.imagepipeline.image.CloseableImage;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

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
            mPosts.addAll(posts);
            notifyDataSetChanged();
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
            // the view holder like ibMore you are adding it only once
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

            List<InstagramComment> lastTwoComments;
            if (post.commentsCount <= 2) {
                lastTwoComments = post.comments;
            } else {
                lastTwoComments = post.comments.subList(post.comments.size() - 3, post.comments.size() - 1);
            }
            for (InstagramComment comment : lastTwoComments) {
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

    public class InstagramPostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView mTvUserName;
        public TextView mTvPostDate;
        public SimpleDraweeView mSdvUserImage;
        public SimpleDraweeView mSdvPostImage;
        public TextView mTvLikes;
        public TextView mTvUserNameDesc;
        public TextView mTvViewAllComments;
        public LinearLayout mLlComments;
        public ImageButton ibMore;

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
            ibMore = (ImageButton) instagramPostView.findViewById(R.id.ibMore);
            ibMore.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            showMorePopup(v);
        }

        // Display anchored popup menu based on view selected
        private void showMorePopup(View v) {
            PopupMenu popup = new PopupMenu(v.getContext(), v);
            // Inflate the menu from xml
            popup.getMenuInflater().inflate(R.menu.menu_more, popup.getMenu());
            // Setup menu item selection
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                private Context mCtx;
                private String mImgUrl;

                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.menu_share:

                            ImagePipeline imagePipeline = Fresco.getImagePipeline();

                            ImageRequest imageRequest = ImageRequestBuilder
                                    .newBuilderWithSource(Uri.parse(mImgUrl))
                                    .setRequestPriority(Priority.HIGH)
                                    .setLowestPermittedRequestLevel(ImageRequest.RequestLevel.FULL_FETCH)
                                    .build();

                            DataSource<CloseableReference<CloseableImage>> dataSource =
                                    imagePipeline.fetchDecodedImage(imageRequest, mCtx);

                            try {
                                dataSource.subscribe(new BaseBitmapDataSubscriber() {
                                    @Override
                                    public void onNewResultImpl(@Nullable Bitmap bitmap) {
                                        if (bitmap == null) {
                                            Log.d("T", "Bitmap data source returned success, but bitmap null.");
                                            return;
                                        }
                                        // The bitmap provided to this method is only guaranteed to be around
                                        // for the lifespan of this method. The image pipeline frees the
                                        // bitmap's memory after this method has completed.
                                        //
                                        // This is fine when passing the bitmap to a system process as
                                        // Android automatically creates a copy.
                                        //
                                        // If you need to keep the bitmap around, look into using a
                                        // BaseDataSubscriber instead of a BaseBitmapDataSubscriber.

                                        String path = MediaStore.Images.Media.insertImage(mCtx.getContentResolver(),
                                                bitmap, "Image Description", null);
                                        Uri bmpUri = Uri.parse(path);
                                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                                        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
                                        shareIntent.setType("image/*");
                                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                                        mCtx.startActivity(Intent.createChooser(shareIntent, "Share Image"));                                    }

                                    @Override
                                    public void onFailureImpl(DataSource dataSource) {
                                        // No cleanup required here
                                    }
                                }, CallerThreadExecutor.getInstance());
                            } finally {
                                if (dataSource != null) {
                                    dataSource.close();
                                }
                            }
                        return true;
                        default:
                            return false;
                    }
                }
                public PopupMenu.OnMenuItemClickListener init(Context ctx, String imgUrl) {
                    mCtx = ctx;
                    mImgUrl = imgUrl;
                    return this;
                }
            }.init(v.getContext(), mPosts.get(getLayoutPosition()).image.imageUrl));
            // Handle dismissal with: popup.setOnDismissListener(...);
            // Show the menu
            popup.show();
        }
    }
}
