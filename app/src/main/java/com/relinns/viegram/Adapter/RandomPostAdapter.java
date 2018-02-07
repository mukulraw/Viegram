package com.relinns.viegram.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.relinns.viegram.Activity.Open_photo;
import com.relinns.viegram.Activity.Search;
import com.relinns.viegram.Modal.TimelinePost;
import com.relinns.viegram.R;

import java.util.List;

public class RandomPostAdapter extends RecyclerView.Adapter<RandomPostAdapter.ViewHolder> {
    private Search context;
    private List<TimelinePost> posts;
    private SharedPreferences preferences;

    public RandomPostAdapter(Search search, List<TimelinePost> timelinePosts) {
        this.context = search;
        this.posts = timelinePosts;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_photo, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.progress.setVisibility(View.VISIBLE);

        String mImageUrl;

        try {
            if (posts.get(position).getThumbnail() == null || posts.get(position).getThumbnail().isEmpty() || posts.get(position).getThumbnail().equalsIgnoreCase("null"))
                mImageUrl = posts.get(position).getPhoto();
            else
                mImageUrl = posts.get(position).getThumbnail();
        } catch (Exception e) {
            e.printStackTrace();
            mImageUrl = posts.get(position).getPhoto();
        }

        Glide.with(context).load(mImageUrl)
                //  .thumbnail(0.01f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.img_view);
        if (posts.get(position).getRandomtype().equals("video")) {
            holder.playIcon.setVisibility(View.VISIBLE);
        } else {
            holder.playIcon.setVisibility(View.GONE);
        }
        holder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("post_id", posts.get(position).getPostId());
                editor.commit();
                Intent i = new Intent(context, Open_photo.class);
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_view, playIcon;
        RelativeLayout card_view;
        ProgressBar progress;

        public ViewHolder(View itemView) {
            super(itemView);
            img_view = itemView.findViewById(R.id.img_view);
            progress = itemView.findViewById(R.id.progress);
            playIcon = itemView.findViewById(R.id.play_icon);
            card_view = itemView.findViewById(R.id.card_view);
            card_view.setLayoutParams(new LinearLayout.LayoutParams((getScreenWidth() / 4), (getScreenWidth() / 4)));
        }
    }

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;

    }
}
