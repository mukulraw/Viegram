package com.relinns.viegram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Comment_like;
import com.relinns.viegram.Activity.Profile;
import com.relinns.viegram.Modal.CommentLiked;
import com.relinns.viegram.R;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Likecomment_Adapter extends RecyclerView.Adapter<Likecomment_Adapter.ViewHolder> {
    private Context context;
    private List<CommentLiked> list;
    private SharedPreferences preferences;

    public Likecomment_Adapter(Context activity, List<CommentLiked> list_data) {
        this.list = list_data;
        this.context = activity;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.user_image);
        holder.points.setVisibility(View.GONE);
        holder.like_text.setVisibility(View.VISIBLE);
        if (!list.get(position).getCommentLikes().equals("0")) {
            holder.like_comment.setVisibility(View.VISIBLE);
            holder.like_text.setText(list.get(position).getCommentLikes());
        } else
            holder.like_comment.setVisibility(View.GONE);

        holder.name.setText(list.get(position).getDisplayName());
        holder.comment.setText(list.get(position).getComment());
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
        holder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
        holder.like_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Comment_like.class);
                intent.putExtra("comment_id", list.get(position).getCommentId());
                context.startActivity(intent);
            }
        });
    }

    private void open_profile(int position) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_user", list.get(position).getId());
        editor.commit();
        if (list.get(position).getId().equals(preferences.getString("user_id", ""))) {
            Intent i = new Intent(context, Profile.class);
            context.startActivity(i);
        } else {
            Intent i = new Intent(context, Another_user.class);
            context.startActivity(i);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image, like, points;
        TextView name, comment, time, point_text, like_text;
        RelativeLayout like_comment, posted_comment_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            like_comment = (RelativeLayout) itemView.findViewById(R.id.like_comment);
            posted_comment_layout = (RelativeLayout) itemView.findViewById(R.id.posted_comment_layout);
            user_image = (ImageView) itemView.findViewById(R.id.comment_user);
            like = (ImageView) itemView.findViewById(R.id.like_comnt_view);
            points = (ImageView) itemView.findViewById(R.id.point_view);
            name = (TextView) itemView.findViewById(R.id.comment_name);
            comment = (TextView) itemView.findViewById(R.id.comment_text);
            time = (TextView) itemView.findViewById(R.id.comment_time);
            point_text = (TextView) itemView.findViewById(R.id.liked_points);
            like_text = (TextView) itemView.findViewById(R.id.like_text);
        }
    }
}
