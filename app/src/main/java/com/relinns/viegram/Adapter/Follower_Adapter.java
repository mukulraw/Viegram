package com.relinns.viegram.Adapter;

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
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Follower_following;
import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.FollowerList_Model;
import com.relinns.viegram.Pojo.FollowerList;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.sql.Time;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;

public class Follower_Adapter extends RecyclerView.Adapter<Follower_Adapter.View_holder> {
    private Follower_following context;
    private List<FollowerList> list_data;
    private SharedPreferences preferences;

    public Follower_Adapter(Follower_following activity, List<FollowerList> list) {
        context = activity;
        list_data = list;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public View_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower, parent, false);
        return new View_holder(v);
    }

    @Override
    public void onBindViewHolder(final View_holder holder, final int position) {

        Glide.with(context).load(list_data.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.follower_image);
        holder.follower_name.setText(list_data.get(position).getDisplayName());

        if (list_data.get(position).getFollowStatus().equals("1")) {
            holder.follow_button.setBackground(context.getResources().getDrawable(R.drawable.login_bg));
            holder.follow_text.setText("Unfollow");
            holder.follow_text.setTextColor(context.getResources().getColor(R.color.white));
        } else if (list_data.get(position).getFollowStatus().equals("0")) {
            holder.follow_button.setBackground(context.getResources().getDrawable(R.drawable.stats_bg));
            holder.follow_text.setText("Follow");
            holder.follow_text.setTextColor(context.getResources().getColor(R.color.login_bg));
        } else if (list_data.get(position).getFollowStatus().equals("2")) {
            holder.follow_button.setBackground(context.getResources().getDrawable(R.drawable.login_bg));
            holder.follow_text.setText("Requested");
            holder.follow_text.setTextColor(context.getResources().getColor(R.color.white));
        }

        if (list_data.get(position).getRestrictStatus().equals("1")) {
            holder.restrict_button.setBackground(context.getResources().getDrawable(R.drawable.restrict_bg));
            holder.restrict_text.setText("Restricted");
            holder.restrict_text.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.restrict_button.setBackground(context.getResources().getDrawable(R.drawable.stats_bg));
            holder.restrict_text.setText("Restrict");
            holder.restrict_text.setTextColor(context.getResources().getColor(R.color.retrict_bg));
        }

        holder.follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.follow_text.getText().toString().equals("Follow")) {
                    holder.follow_text.setVisibility(View.GONE);
                    holder.followProgress.setVisibility(View.VISIBLE);
                    holder.followProgress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.login_bg), PorterDuff.Mode.MULTIPLY);
                    follow_user(position, holder);
                } else if (holder.follow_text.getText().toString().equals("Requested")) {
                    holder.follow_text.setVisibility(View.GONE);
                    holder.followProgress.setVisibility(View.VISIBLE);
                    holder.followProgress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                    follow_user(position, holder);
                } else if (holder.follow_text.getText().toString().equals("Unfollow")) {

                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.unfollow_dialog);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView unfollow_popup_text = (TextView) dialog.findViewById(R.id.unfollow_popup_text);
                    unfollow_popup_text.setText(context.getResources().getString(R.string.unfollow_pop) + " " + list_data.get(position).getDisplayName() + " ?");
                    RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
                    final RelativeLayout unfollow_user = (RelativeLayout) dialog.findViewById(R.id.unfollow_user);
                    unfollow_user.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            holder.follow_text.setVisibility(View.GONE);
                            holder.followProgress.setVisibility(View.VISIBLE);
                            holder.followProgress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                            unfollow_user(position, holder);
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
        holder.restrict_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.restrict_text.getText().toString().equals("Restrict")) {
                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.restrict_popup);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView restrict_popup_text = (TextView) dialog.findViewById(R.id.restrict_popup_text);
                    restrict_popup_text.setText(context.getResources().getString(R.string.restrict_pop) + " " + list_data.get(position).getDisplayName() + " that you\'ve restricted them.");
                    RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
                    RelativeLayout allow_restrict = (RelativeLayout) dialog.findViewById(R.id.allow_restrict);
                    cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                    allow_restrict.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                            holder.restrict_text.setVisibility(View.GONE);
                            holder.restrictProgress.setVisibility(View.VISIBLE);
                            holder.restrictProgress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.retrict_bg), PorterDuff.Mode.MULTIPLY);
                            restrict_user(position, holder);
                        }
                    });
                    dialog.show();
                } else {
                    holder.restrict_text.setVisibility(View.GONE);
                    holder.restrictProgress.setVisibility(View.VISIBLE);
                    holder.restrictProgress.getIndeterminateDrawable().setColorFilter(context.getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                    restrict_user(position, holder);
                }
            }
        });

        holder.follower_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
        holder.follower_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
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

    private void unfollow_user(final int position, final View_holder holder) {
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
                holder.follow_text.setVisibility(View.VISIBLE);
                holder.followProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "unfollow_user Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        if (response.body().getResult().getReason().equals("user unfollow successfully")) {
                            holder.follow_button.setBackground(context.getResources().getDrawable(R.drawable.stats_bg));
                            holder.follow_text.setText("Follow");
                            holder.follow_text.setTextColor(context.getResources().getColor(R.color.login_bg));
                            try {
                                context.getData();
//                                context.get_following();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }

                            list_data.get(position).setFollowStatus("0");
                            if (Timeline.resultp != null)
                                Timeline.resultp.setFollowerList(list_data);
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
                holder.follow_text.setVisibility(View.VISIBLE);
                holder.followProgress.setVisibility(View.GONE);

                Alerter.create(context)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "unfollow_user Error : " + t.getMessage());
            }
        });
    }

    private void follow_user(final int position, final View_holder holder) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "follow_request");
        postParams.put("request_send_by", preferences.getString("user_id", ""));
        postParams.put("request_send_to", list_data.get(position).getUserId());

        Log.d("API_Parameters", " follow_request params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                holder.follow_text.setVisibility(View.VISIBLE);
                holder.followProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "follow_request Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        String reason = response.body().getResult().getReason();
                        if (reason.equals("Following successfully")) {
                            holder.follow_button.setBackground(context.getResources().getDrawable(R.drawable.login_bg));
                            holder.follow_text.setText("Unfollow");
                            holder.follow_text.setTextColor(context.getResources().getColor(R.color.white));
                            try {
                                context.getData();
//                                context.get_following();
                            } catch (TimeoutException e) {
                                e.printStackTrace();
                            }
                            list_data.get(position).setFollowStatus("1");
                        } else if (reason.equals("Following request send successfully")) {
                            holder.follow_button.setBackground(context.getResources().getDrawable(R.drawable.login_bg));
                            holder.follow_text.setText("Requested");
                            holder.follow_text.setTextColor(context.getResources().getColor(R.color.white));
                            list_data.get(position).setFollowStatus("2");
                        } else if (reason.equals("request deleted successfully")) {
                            holder.follow_button.setBackground(context.getResources().getDrawable(R.drawable.stats_bg));
                            holder.follow_text.setText("Follow");
                            holder.follow_text.setTextColor(context.getResources().getColor(R.color.login_bg));
                            list_data.get(position).setFollowStatus("0");
                        }
                        if (Timeline.resultp != null)
                            Timeline.resultp.setFollowerList(list_data);
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Request already send to this user.");
                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }
                } else {

                    Alerter.create(context)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "follow_request Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                holder.follow_text.setVisibility(View.VISIBLE);
                holder.followProgress.setVisibility(View.GONE);

                Alerter.create(context)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "follow_request Error : " + t.getMessage());
            }
        });
    }

    private void restrict_user(final int position, final View_holder holder) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "restrict_user");
        postParams.put("userid", preferences.getString("user_id", ""));
        postParams.put("restrict_user", list_data.get(position).getUserId());
        Log.d("API_Parameters", " restrict_user params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                holder.restrict_text.setVisibility(View.VISIBLE);
                holder.restrictProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "restrict_user Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        String reason = response.body().getResult().getReason();
                        if (reason.equals("user unrestricted successfully")) {
                            holder.restrict_button.setBackground(context.getResources().getDrawable(R.drawable.stats_bg));
                            holder.restrict_text.setText("Restrict");
                            holder.restrict_text.setTextColor(context.getResources().getColor(R.color.retrict_bg));
                            list_data.get(position).setRestrictStatus("0");
                        } else if (reason.equals("user restricted successfully")) {
                            holder.restrict_button.setBackground(context.getResources().getDrawable(R.drawable.restrict_bg));
                            holder.restrict_text.setText("Restricted");
                            holder.restrict_text.setTextColor(context.getResources().getColor(R.color.white));
                            list_data.get(position).setRestrictStatus("1");
                        }
                        if (Timeline.resultp != null)
                            Timeline.resultp.setFollowerList(list_data);
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(context);
                        alert.setMessage("Request already send to this user.");
                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).show();
                    }
                } else {
                    Alerter.create(context)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "restrict_user Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                holder.restrict_text.setVisibility(View.VISIBLE);
                holder.restrictProgress.setVisibility(View.GONE);
                Alerter.create(context)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "restrict_user Error : " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class View_holder extends RecyclerView.ViewHolder {
        ImageView follower_image;
        TextView follower_name, follow_text, restrict_text;
        RelativeLayout follow_button, restrict_button;
        ProgressBar followProgress, restrictProgress;

        public View_holder(View itemView) {
            super(itemView);
            follower_image = (ImageView) itemView.findViewById(R.id.follower_image);
            follower_name = (TextView) itemView.findViewById(R.id.follower_name);
            follow_text = (TextView) itemView.findViewById(R.id.follow_text);
            restrict_text = (TextView) itemView.findViewById(R.id.restrict_text);
            follow_button = (RelativeLayout) itemView.findViewById(R.id.follow_button);
            restrict_button = (RelativeLayout) itemView.findViewById(R.id.restrict);
            followProgress = (ProgressBar) itemView.findViewById(R.id.follower_progress);
            restrictProgress = (ProgressBar) itemView.findViewById(R.id.following_progress);

        }
    }
}
