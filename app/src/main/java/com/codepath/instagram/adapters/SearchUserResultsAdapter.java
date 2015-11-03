package com.codepath.instagram.adapters;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.activities.PhotoGridActivity;
import com.codepath.instagram.models.InstagramSearchTag;
import com.codepath.instagram.models.InstagramUser;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

public class SearchUserResultsAdapter extends RecyclerView.Adapter<SearchUserResultsAdapter.SearchUsersViewHolder> {
    private List<InstagramUser> mUsers;

    public SearchUserResultsAdapter(List<InstagramUser> users) {
        this.mUsers = users;
    }

    @Override
    public SearchUsersViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_search_user_item, parent, false);
        return new SearchUsersViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SearchUsersViewHolder holder, int position) {
        final InstagramUser user = mUsers.get(position);
        holder.tvUserName.setText(user.userName);
        holder.tvFullName.setText(user.fullName);
        holder.sdvUserImage.setImageURI(Uri.parse(user.profilePictureUrl));
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    public void replaceAll(List<InstagramUser> users) {
        this.mUsers.clear();
        this.mUsers.addAll(users);
        this.notifyDataSetChanged();
    }

    public class SearchUsersViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView tvUserName;
        TextView tvFullName;
        SimpleDraweeView sdvUserImage;

        public SearchUsersViewHolder(View itemView) {
            super(itemView);
            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvFullName = (TextView) itemView.findViewById(R.id.tvFullName);
            sdvUserImage = (SimpleDraweeView)itemView.findViewById(R.id.sdvUserImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent i = new Intent(v.getContext(), PhotoGridActivity.class);
            i.putExtra(PhotoGridActivity.EXTRA_USER_ID, mUsers.get(getPosition()).userId);
            v.getContext().startActivity(i);
        }
    }
}