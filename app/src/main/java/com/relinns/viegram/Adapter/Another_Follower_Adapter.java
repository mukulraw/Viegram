package com.relinns.viegram.Adapter;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import com.relinns.viegram.Activity.Another_follower_following;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.FollowerList_Model;
import com.relinns.viegram.Activity.Profile;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Another_Follower_Adapter extends RecyclerView.Adapter<Another_Follower_Adapter.View_Holder> {
    private SharedPreferences preferences;
    private List<FollowerList_Model> listData = new ArrayList<>();
    private Another_follower_following activity;

    public Another_Follower_Adapter(Another_follower_following followers, List<FollowerList_Model> list) {
        this.listData = list;
        this.activity = followers;
        preferences = activity.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower, parent, false);
        return new View_Holder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final View_Holder holder, int position) {
        Glide.with(activity).load(listData.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(activity))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.following_image);
        holder.following_name.setText(listData.get(position).getDisplayName());

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
        Log.e("Follower", "follower id : " + listData.get(position).getUserId());
        Log.e("Follower", "user id : " + preferences.getString("user_id", ""));
        if (listData.get(position).getUserId()!=null) {
            if (listData.get(position).getUserId().equals(preferences.getString("user_id", ""))) {
                holder.unfollow_button.setVisibility(View.GONE);
                holder.following_button.setVisibility(View.GONE);

            } else {
                holder.unfollow_button.setVisibility(View.VISIBLE);

                if (listData.get(position).getFollowStatus().equals("1")) {
                    holder.unfollow_button.setBackground(activity.getResources().getDrawable(R.drawable.login_bg));
                    holder.unfollow_text.setText("Unfollow");
                    holder.unfollow_text.setTextColor(activity.getResources().getColor(R.color.white));
                } else if (listData.get(position).getFollowStatus().equals("0")) {
                    holder.unfollow_button.setBackground(activity.getResources().getDrawable(R.drawable.stats_bg));
                    holder.unfollow_text.setText("Follow");
                    holder.unfollow_text.setTextColor(activity.getResources().getColor(R.color.login_bg));
                } else if (listData.get(position).getFollowStatus().equals("2")) {
                    holder.unfollow_button.setBackground(activity.getResources().getDrawable(R.drawable.login_bg));
                    holder.unfollow_text.setText("Requested");
                    holder.unfollow_text.setTextColor(activity.getResources().getColor(R.color.white));
                }
                if (listData.get(position).getUserFollowStatus().equals("1")) {
                    holder.following_button.setVisibility(View.VISIBLE);
                    holder.following_button.setBackground(activity.getResources().getDrawable(R.drawable.login_bg));
                    holder.following_text.setText("Follows You");
                    holder.following_text.setTextColor(activity.getResources().getColor(R.color.white));
                } else {
                    holder.following_button.setVisibility(View.GONE);
                }
            }
        } else {
            holder.unfollow_button.setVisibility(View.GONE);
            holder.following_button.setVisibility(View.GONE);
        }

        holder.unfollow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.unfollow_text.getText().toString().equals("Follow")) {
                    holder.unfollow_text.setVisibility(View.GONE);
                    holder.following_progress.setVisibility(View.VISIBLE);
                    holder.following_progress.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.login_bg), PorterDuff.Mode.MULTIPLY);
                    follow_user(holder.getAdapterPosition(), holder);
                } else if (holder.unfollow_text.getText().toString().equals("Requested")) {
                    holder.unfollow_text.setVisibility(View.GONE);
                    holder.following_progress.setVisibility(View.VISIBLE);
                    holder.following_progress.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                    follow_user(holder.getAdapterPosition(), holder);
                } else {
                    final Dialog dialog = new Dialog(activity);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.unfollow_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView unfollow_popup_text = (TextView) dialog.findViewById(R.id.unfollow_popup_text);
                    unfollow_popup_text.setText(activity.getResources().getString(R.string.unfollow_pop) + " " + listData.get(holder.getAdapterPosition()).getDisplayName() + " ?");
                    RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
                    final RelativeLayout unfollow_user = (RelativeLayout) dialog.findViewById(R.id.unfollow_user);
                    unfollow_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            holder.unfollow_text.setVisibility(View.GONE);
                            holder.following_progress.setVisibility(View.VISIBLE);
                            holder.following_progress.getIndeterminateDrawable().setColorFilter(activity.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                            unfollow_user(holder.getAdapterPosition(), holder);
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
            }
        });
    }

    private void open_profile(int position) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_user", listData.get(position).getUserId());
        editor.commit();
        if (listData.get(position).getUserId().equals(preferences.getString("user_id", ""))) {
            Intent i = new Intent(activity, Profile.class);
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            Intent i = new Intent(activity, Another_user.class);
            activity.startActivity(i);
            activity.overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    private void follow_user(final int position, final View_Holder holder) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "follow_request");
        postParams.put("request_send_by", preferences.getString("user_id", ""));
        postParams.put("request_send_to", listData.get(position).getUserId());

        Log.d("API_Parameters", " follow_request params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                holder.unfollow_text.setVisibility(View.VISIBLE);
                holder.following_progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "follow_request Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        String reason = response.body().getResult().getReason();
                        if (reason.equals("Following successfully")) {
                            holder.unfollow_button.setBackground(activity.getResources().getDrawable(R.drawable.login_bg));
                            holder.unfollow_text.setText("Unfollow");
                            holder.unfollow_text.setTextColor(activity.getResources().getColor(R.color.white));
                            listData.get(position).setFollowStatus("1");

                        } else if (reason.equals("Following request send successfully")) {
                            holder.unfollow_button.setBackground(activity.getResources().getDrawable(R.drawable.login_bg));
                            holder.unfollow_text.setText("Requested");
                            holder.unfollow_text.setTextColor(activity.getResources().getColor(R.color.white));
                            listData.get(position).setFollowStatus("2");
                        } else {
                            holder.unfollow_button.setBackground(activity.getResources().getDrawable(R.drawable.stats_bg));
                            holder.unfollow_text.setText("Follow");
                            holder.unfollow_text.setTextColor(activity.getResources().getColor(R.color.login_bg));
                            listData.get(position).setFollowStatus("0");
                        }
                    } else {
                        String reason = response.body().getResult().getReason();
                        if (reason.equals("request already send to this user")) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                            alert.setMessage("Request already send to this user.");
                            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }
                    }
                } else {
                    Alerter.create(activity)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "follow_request Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                holder.unfollow_text.setVisibility(View.VISIBLE);
                holder.following_progress.setVisibility(View.GONE);

                    Alerter.create(activity)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                Log.d("API_Error", "follow_request Error : " + t.getMessage());
            }
        });
    }

    private void unfollow_user(final int position, final View_Holder holder) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "unfollow_user");
        postParams.put("userid", preferences.getString("user_id", ""));
        postParams.put("following_userid", listData.get(position).getUserId());
        Log.d("API_Parameters", " unfollow_user params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                holder.unfollow_text.setVisibility(View.VISIBLE);
                holder.following_progress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "unfollow_user Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        holder.unfollow_button.setBackground(activity.getResources().getDrawable(R.drawable.stats_bg));
                        holder.unfollow_text.setText("Follow");
                        holder.unfollow_text.setTextColor(activity.getResources().getColor(R.color.login_bg));
                        listData.get(position).setFollowStatus("0");
                    } else {
                        Alerter.create(activity)
                                .setText("Something went wrong. Please try again after sometime!!")
                                .setBackgroundColor(R.color.red)
                                .show();
                    }
                } else {
                    Alerter.create(activity)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();

                    Log.e("API_Response", "unfollow_user Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                holder.unfollow_text.setVisibility(View.VISIBLE);
                holder.following_progress.setVisibility(View.GONE);

                    Alerter.create(activity)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                Log.d("API_Error", "unfollow_user Error : " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        ImageView following_image;
        TextView following_name, following_text, unfollow_text;
        RelativeLayout following_button, unfollow_button;
        ProgressBar following_progress;

        public View_Holder(View itemView) {
            super(itemView);
            following_image = (ImageView) itemView.findViewById(R.id.follower_image);
            following_name = (TextView) itemView.findViewById(R.id.follower_name);
            following_text = (TextView) itemView.findViewById(R.id.follow_text);
            unfollow_text = (TextView) itemView.findViewById(R.id.restrict_text);
            following_button = (RelativeLayout) itemView.findViewById(R.id.follow_button);
            unfollow_button = (RelativeLayout) itemView.findViewById(R.id.restrict);
            following_progress = (ProgressBar) itemView.findViewById(R.id.following_progress);
        }
    }
}
