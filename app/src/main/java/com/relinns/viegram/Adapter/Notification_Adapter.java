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
import com.relinns.viegram.Activity.Comments;
import com.relinns.viegram.Fragment.NotificationFragment;
import com.relinns.viegram.Modal.Notification;
import com.relinns.viegram.Activity.Open_photo;
import com.relinns.viegram.R;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Notification_Adapter extends RecyclerView.Adapter<Notification_Adapter.ViewHolder> {
    private SharedPreferences preferences;
    private List<Notification> listData;
    private NotificationFragment fragment;

    public Notification_Adapter(List<Notification> notification, NotificationFragment notificationFragment) {
        this.fragment = notificationFragment;
        this.listData = notification;
        preferences = fragment.getActivity().getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.request_layout.setVisibility(View.GONE);
        holder.notification_layout.setVisibility(View.VISIBLE);
        Glide.with(fragment.getActivity()).load(listData.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(fragment.getActivity()))
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
              //  .skipMemoryCache(true)
             //   .thumbnail(0.5f)
                .into(holder.user_image);
        holder.notify_time.setText(listData.get(position).getTimeAgo());
        holder.notify_text.setText(listData.get(position).getPurpose());
        holder.notify_name.setText(listData.get(position).getDisplayName());
        if (listData.get(position).getPurpose().contains("follow")) {
            holder.post_image.setVisibility(View.GONE);
        } else {
            holder.post_image.setVisibility(View.VISIBLE);
            Glide.with(fragment.getActivity()).load(listData.get(position).getPhoto())
              //      .thumbnail(0.1f)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                 //   .skipMemoryCache(true)
                    .into(holder.post_image);
        }

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("post_id", listData.get(position).getPostId());
                editor.commit();
                if (listData.get(position).getStatus().equals("2") || listData.get(position).getStatus().equals("8"))
                {
                    Intent i = new Intent(fragment.getActivity(), Comments.class);
                    fragment.getActivity().startActivity(i);
                    fragment.getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);}

                else {
                Intent i = new Intent(fragment.getActivity(), Open_photo.class);
                fragment.getActivity().startActivity(i);
                fragment.getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);}
            }
        });

        holder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });

        holder.notify_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               open_profile(position);
            }
        });
    }

    private void open_profile(int position) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_user", listData.get(position).getUserId());
        editor.commit();
        Intent i = new Intent(fragment.getActivity(), Another_user.class);
        fragment.getActivity().startActivity(i);
        fragment.getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    }


    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView user_image, post_image;
        TextView notify_time, notify_name, notify_text;
        RelativeLayout notification_layout, request_layout;

        public ViewHolder(View itemView) {
            super(itemView);
            user_image = (ImageView) itemView.findViewById(R.id.notify_user);
            post_image = (ImageView) itemView.findViewById(R.id.notify_pic);
            notify_name = (TextView) itemView.findViewById(R.id.notify_name);
            notify_text = (TextView) itemView.findViewById(R.id.notify_text);
            notify_time = (TextView) itemView.findViewById(R.id.notify_time);
            notification_layout = (RelativeLayout) itemView.findViewById(R.id.notification_layout);
            request_layout = (RelativeLayout) itemView.findViewById(R.id.request_layout);

        }
    }
}
