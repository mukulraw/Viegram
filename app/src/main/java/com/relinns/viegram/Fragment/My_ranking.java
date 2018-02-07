package com.relinns.viegram.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Pojo.ResultPojo;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;

public class My_ranking extends Fragment {
    private SharedPreferences preferences;
    private TextView world_rank;
    private TextView rank_country;
    private TextView rank_follower;
    private TextView rank_following;
    private RelativeLayout progress_layout;
    private RelativeLayout working_layout;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_ranking, container, false);

        preferences = getActivity().getSharedPreferences("Viegram", Context.MODE_PRIVATE);
        world_rank = (TextView) v.findViewById(R.id.world_rank);
        rank_country = (TextView) v.findViewById(R.id.country_rank);
        rank_follower = (TextView) v.findViewById(R.id.follower_rank);
        rank_following = (TextView) v.findViewById(R.id.following_rank);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        progress_layout = (RelativeLayout) v.findViewById(R.id.progress_layout);
        working_layout = (RelativeLayout) v.findViewById(R.id.working_layout);

        if (Timeline.resultp!=null)
        {
            progress_layout.setVisibility(View.GONE);
            working_layout.setVisibility(View.VISIBLE);
            world_rank.setText(Timeline.resultp.getViegramRank());
            rank_country.setText(Timeline.resultp.getCountryRank());
            rank_follower.setText(Timeline.resultp.getFollowerRank());
            rank_following.setText(Timeline.resultp.getFollowingRank());
        }
        else {
            progress_layout.setVisibility(View.VISIBLE);
            working_layout.setVisibility(View.GONE);
            my_ranking();

        }
        return v;
    }

    private void my_ranking() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "user_ranking");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "user_ranking parameters :" + postParams.toString());
        Call<API_Response> call = service.rankingRelated(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "user_ranking Response : " + new Gson().toJson(response.body()));
                progress_layout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getResult().getMsg().equals("201")) {

                        working_layout.setVisibility(View.VISIBLE);
                        world_rank.setText(response.body().getResult().getViegramRank());
                        rank_country.setText(response.body().getResult().getCountryRank());
                        rank_follower.setText(response.body().getResult().getFollowerRank());
                        rank_following.setText(response.body().getResult().getFollowingRank());
                    }
                    else {
                        working_layout.setVisibility(View.GONE);
                        if(getActivity()!=null)
                        {
                            Alerter.create(getActivity())
                                    .setText(R.string.network_error)
                                    .setBackgroundColor(R.color.login_bg)
                                    .show();
                        }
                    }
                } else{
                    if(getActivity()!=null)
                    {
                        Alerter.create(getActivity())
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                    Log.e("API_Response", "user_ranking Response : " + new Gson().toJson(response.errorBody()));
            }}

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                working_layout.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                if(getActivity()!=null)
                {
                    Alerter.create(getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }
            }
        });
    }
}
