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
import com.relinns.viegram.Fragment.RequestFragment;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Notification;
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

@SuppressWarnings("ALL")
public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private RequestFragment fragment;
    private SharedPreferences preferences;
    private ProgressDialog progress_Dialog;
    private String respond;
    private List<Notification> list_data;

    public RequestAdapter(List<Notification> notificationData, RequestFragment requestFragment) {
        this.fragment = requestFragment;
        this.list_data = notificationData;
        preferences = fragment.getActivity().getSharedPreferences("Viegram", Context.MODE_PRIVATE);
        progress_Dialog = new ProgressDialog(fragment.getActivity());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.request_layout.setVisibility(View.VISIBLE);
        holder.notification_layout.setVisibility(View.GONE);
        Glide.with(fragment.getActivity()).load(list_data.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(fragment.getActivity()))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.request_image);
        holder.request_username.setText(list_data.get(position).getDisplayName());
        holder.request_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
        holder.request_username.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
        holder.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progress_Dialog.show();
                respond = "1";
                respond_request(position, respond);
            }


        });
        holder.reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                respond = "0";
                progress_Dialog.show();
                respond_request(position, respond);
            }
        });
    }

    private void open_profile(int position) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_user", list_data.get(position).getUserId());
        editor.commit();
        Intent i = new Intent(fragment.getActivity(), Another_user.class);
        fragment.getActivity().startActivity(i);
        fragment.getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void respond_request(int position, String respond) {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "respond_request");
        postParam.put("following_userid", list_data.get(position).getUserId());
        postParam.put("response", respond);
        postParam.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "respond_request parameters :" + postParam.toString());
        Call<API_Response> call = service.FriendsWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "respond_request Response : " + new Gson().toJson(response.body()));
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {
                    fragment.getRequests();

                } else {
                    Alerter.create(fragment.getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "respond_request Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_Dialog.dismiss();
               Alerter.create(fragment.getActivity())
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "respond_request Error : " + t.getMessage());
            }
        });
    }


    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout notification_layout, accept, reject, request_layout;
        ImageView request_image;
        TextView request_username;

        public ViewHolder(View itemView) {
            super(itemView);
            request_username = (TextView) itemView.findViewById(R.id.request_user_name);
            notification_layout = (RelativeLayout) itemView.findViewById(R.id.notification_layout);
            accept = (RelativeLayout) itemView.findViewById(R.id.accept);
            reject = (RelativeLayout) itemView.findViewById(R.id.reject);
            request_layout = (RelativeLayout) itemView.findViewById(R.id.request_layout);
            request_image = (ImageView) itemView.findViewById(R.id.request_user_image);
        }
    }
}
