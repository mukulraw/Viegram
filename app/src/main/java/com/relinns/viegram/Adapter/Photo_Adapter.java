package com.relinns.viegram.Adapter;

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
import com.relinns.viegram.Modal.Photo_Model;
import com.relinns.viegram.Pojo.Post;
import com.relinns.viegram.R;
import java.util.List;

public class Photo_Adapter extends RecyclerView.Adapter<Photo_Adapter.View_Holder> {
    private Context context;
    private SharedPreferences preferences;
    private List<Post> posts;

    public Photo_Adapter(Context activity, List<Post> posts) {
        this.context = activity;
        this.posts = posts;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }


    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_photo, parent, false);

        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        holder.setIsRecyclable(false);
        holder.progress.setVisibility(View.VISIBLE);
        Glide.with(context).load(posts.get(position).getPhoto())
                .centerCrop()
                .override(100, 100)
             //   .thumbnail(0.01f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }
                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.progress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.img_view);
        if (posts.get(position).getType().equals("video")) {
            holder.play_icon.setVisibility(View.VISIBLE);
        } else {
            holder.play_icon.setVisibility(View.GONE);
        }
        holder.img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("post_id", posts.get(position).getPostId());
                editor.apply();
                Intent i = new Intent(context, Open_photo.class);
                context.startActivity(i);
            }
        });
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        ImageView img_view, play_icon;
        RelativeLayout card_view;
        ProgressBar progress;

        public View_Holder(View itemView) {
            super(itemView);
            img_view = (ImageView) itemView.findViewById(R.id.img_view);
            play_icon = (ImageView) itemView.findViewById(R.id.play_icon);
            card_view = (RelativeLayout) itemView.findViewById(R.id.card_view);
            progress = (ProgressBar) itemView.findViewById(R.id.progress);
            card_view.setLayoutParams(new LinearLayout.LayoutParams((getScreenWidth() / 4), (getScreenWidth() / 4)));
        }
    }

    private int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }
}

