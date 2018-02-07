package com.relinns.viegram.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Follow_people;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.CommentPost;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by win 7 on 6/5/2017.
 */
@SuppressWarnings("ALL")
public class Follow_people_Adapter extends RecyclerView.Adapter<Follow_people_Adapter.Viewholder> {
    private Follow_people context;
    private List<CommentPost> list_data;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;

    public Follow_people_Adapter(Follow_people activity, List<CommentPost> list) {
        context = activity;
        list_data = list;
        progressDialog = new ProgressDialog(context);
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.follower, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(final Viewholder holder, final int position) {
        holder.following_name.setText(list_data.get(position).getDisplayName());
        holder.following_button.setVisibility(View.GONE);
        Glide.with(context).load(list_data.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.following_image);
        holder.follow_button.setBackground(context.getResources().getDrawable(R.drawable.stats_bg));
        holder.follow_text.setText("Follow");
        holder.follow_text.setTextColor(context.getResources().getColor(R.color.login_bg));
        holder.following_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
        holder.following_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
        holder.follow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                follow_user(position, holder);
            }
        });
    }

    private void open_profile(int position) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_user", list_data.get(position).getUserid());
        editor.commit();
        Intent i = new Intent(context, Another_user.class);
        context.startActivity(i);
        context.overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void follow_user(int position, final Viewholder holder) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "follow_request");
        postParams.put("request_send_by", preferences.getString("user_id", ""));
        postParams.put("request_send_to", list_data.get(position).getUserid());

        Log.d("API_Parameters", " follow_request params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("API_Response", "follow_request Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        context.get_people();
                    } else Alerter.create(context)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
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
                progressDialog.dismiss();
                    Alerter.create(context)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                Log.d("API_Error", "follow_request Error : " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView following_image;
        TextView following_name, following_text, follow_text;
        RelativeLayout following_button, follow_button;

        public Viewholder(View itemView) {
            super(itemView);
            following_image = (ImageView) itemView.findViewById(R.id.follower_image);
            following_name = (TextView) itemView.findViewById(R.id.follower_name);
            following_text = (TextView) itemView.findViewById(R.id.follow_text);
            follow_text = (TextView) itemView.findViewById(R.id.restrict_text);
            following_button = (RelativeLayout) itemView.findViewById(R.id.follow_button);
            follow_button = (RelativeLayout) itemView.findViewById(R.id.restrict);

        }
    }
}
