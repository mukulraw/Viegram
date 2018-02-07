package com.relinns.viegram.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Follower_following;
import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.FollowerList_Model;
import com.relinns.viegram.Pojo.FollowingList;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Following_Adapter extends RecyclerView.Adapter<Following_Adapter.View_Holder> {
    private Follower_following context;
    private List<FollowingList> list_data;
    private SharedPreferences preferences;

    public Following_Adapter(Follower_following activity, List<FollowingList> list) {
        context = activity;
        list_data = list;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower, parent, false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(final View_Holder holder, final int position) {
        Glide.with(context).load(list_data.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.following_image);
        holder.following_name.setText(list_data.get(position).getDisplayName());
        if (list_data.get(position).getFollowStatus().equals("1")) {
            holder.following_button.setVisibility(View.VISIBLE);
            holder.following_button.setBackground(context.getResources().getDrawable(R.drawable.login_bg));
            holder.following_text.setText("Follows You");
            holder.following_text.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.following_button.setVisibility(View.GONE);
        }
        holder.unfollow_text.setText("Unfollow");
        holder.unfollow_text.setTextColor(context.getResources().getColor(R.color.white));
        holder.unfollow_button.setBackground(context.getResources().getDrawable(R.drawable.login_bg));

        holder.unfollow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Adapter", holder.getAdapterPosition() + " holder");
                Log.d("Adapter", "position" + position);
                final Dialog dialog = new Dialog(context);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.unfollow_dialog);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                TextView unfollow_popup_text = (TextView) dialog.findViewById(R.id.unfollow_popup_text);
                unfollow_popup_text.setText(context.getResources().getString(R.string.unfollow_pop) + " " + list_data.get(holder.getAdapterPosition()).getDisplayName() + " ?");

                RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
                final RelativeLayout unfollow_user = (RelativeLayout) dialog.findViewById(R.id.unfollow_user);
                unfollow_user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        holder.unfollowProgress.setVisibility(View.VISIBLE);
                        holder.unfollow_text.setVisibility(View.GONE);
                        holder.unfollowProgress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                        unfollow_user(holder, holder.getAdapterPosition());
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                dialog.show();
            }


//            private void removeitem(int adapterPosition) {
//                list_data.remove(adapterPosition);
//                Log.d("Tag", "total following:" + Followers.total_following);
//                int following = Integer.parseInt(Followers.total_following) - 1;
//
//                Log.d("Tag", "after remove user :" + following);
//                Follower_following.following_text.setText("Following (" + following + ")");
//                Followers.total_following = following + "";
//                if (following == 0) {
//                    followers.get_following();
//                }
//                notifyItemRemoved(adapterPosition);
//
//            }


        });
        holder.following_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(holder.getAdapterPosition());
            }
        });
        holder.following_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(holder.getAdapterPosition());
            }
        });
    }

    private void open_profile(int position) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_user", list_data.get(position).getUserId());
        editor.commit();
        Intent i = new Intent(context, Another_user.class);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void unfollow_user(final View_Holder holder, final int position) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "unfollow_user");
        postParams.put("userid", preferences.getString("user_id", ""));
        postParams.put("following_userid", list_data.get(position).getUserId());

        Log.d("API_Parameters", " unfollow_user params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                holder.unfollowProgress.setVisibility(View.GONE);
                holder.unfollow_text.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "unfollow_user Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        if (response.body().getResult().getReason().equals("user unfollow successfully")) {
                            try {
//                                context.get_following();
                                context.getData();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }
                            if (Timeline.resultp != null)
                                Timeline.resultp.getFollowingList().remove(position);

                        } else {
                            Alerter.create(context)
                                    .setText("Something went wrong. Please try again after sometime!!")
                                    .setBackgroundColor(R.color.red)
                                    .show();
                        }
                    } else {

                        Alerter.create(context)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.red)
                                .show();
                        Log.e("API_Response", "unfollow_user Response : " + new Gson().toJson(response.errorBody()));
                    }
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                holder.unfollowProgress.setVisibility(View.GONE);
                holder.unfollow_text.setVisibility(View.VISIBLE);

                Alerter.create(context)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "unfollow_user Error : " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        ImageView following_image;
        TextView following_name, following_text, unfollow_text;
        RelativeLayout following_button, unfollow_button;
        ProgressBar unfollowProgress;

        public View_Holder(View itemView) {
            super(itemView);
            following_image = (ImageView) itemView.findViewById(R.id.follower_image);
            following_name = (TextView) itemView.findViewById(R.id.follower_name);
            following_text = (TextView) itemView.findViewById(R.id.follow_text);
            unfollow_text = (TextView) itemView.findViewById(R.id.restrict_text);
            following_button = (RelativeLayout) itemView.findViewById(R.id.follow_button);
            unfollow_button = (RelativeLayout) itemView.findViewById(R.id.restrict);
            unfollowProgress = (ProgressBar) itemView.findViewById(R.id.following_progress);

        }
    }


}
