package com.relinns.viegram.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.Adapter.Follower_ranking_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Pojo.ResultPojo;
import com.relinns.viegram.Pojo.UserData;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;

public class Follower_ranking extends Fragment {
    private RecyclerView follower_rank_list;
    private SharedPreferences preferences;
    private RelativeLayout progress_layout;
    private RelativeLayout follower_ranking_layout;
    private RelativeLayout no_follower_ranking;
    private TextView no_follower_ranking_text;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.follower_ranking, container, false);
        follower_rank_list = (RecyclerView) v.findViewById(R.id.flwr_rnk_list);
        follower_ranking_layout = (RelativeLayout) v.findViewById(R.id.follower_ranking_layout);
        no_follower_ranking = (RelativeLayout) v.findViewById(R.id.no_follower_ranking);
        no_follower_ranking_text = (TextView) v.findViewById(R.id.no_follower_ranking_text);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        progress_layout = (RelativeLayout) v.findViewById(R.id.progress_layout);
        no_follower_ranking.setVisibility(View.GONE);
        preferences = getActivity().getSharedPreferences("Viegram", Context.MODE_PRIVATE);
        progress_layout.setVisibility(View.GONE);
        follower_ranking_layout.setVisibility(View.VISIBLE);
        if (Timeline.resultp!=null)
        {
            if (Timeline.resultp.getFollowerRanking().size()!=0) {
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                follower_rank_list.setLayoutManager(mLayoutManager);
                Log.i("viegram", "posts" + Timeline.resultp.getFollowerRanking());
                Follower_ranking_Adapter adapter = new Follower_ranking_Adapter(getActivity(), Timeline.resultp.getFollowerRanking());
                follower_rank_list.setAdapter(adapter);
            }
            else {
                no_follower_ranking.setVisibility(View.VISIBLE);
                follower_ranking_layout.setVisibility(View.GONE);
                getRanking();
            }
        }

        return v;
    }

    private void getRanking() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "follower_ranking");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "follower_ranking parameters :" + postParams.toString());
        Call<UserData> call = service.Settings(postParams);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                Log.e("API_Response", "follower_ranking Response : " + new Gson().toJson(response.body()));
                progress_layout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    if (response.body().getResult().getMsg().equals("201")) {
                        no_follower_ranking.setVisibility(View.GONE);
                        follower_ranking_layout.setVisibility(View.VISIBLE);

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        follower_rank_list.setLayoutManager(mLayoutManager);
                        Log.i("viegram", "posts" + Timeline.resultp.getFollowerRanking());
                        Follower_ranking_Adapter adapter = new Follower_ranking_Adapter(getActivity(), response.body().getResult().getFollowerRanking());
                        follower_rank_list.setAdapter(adapter);
                    }
                    else {
                        no_follower_ranking.setVisibility(View.VISIBLE);
                        follower_ranking_layout.setVisibility(View.GONE);

                    }
                } else
                {
                    no_follower_ranking.setVisibility(View.GONE);
                    follower_ranking_layout.setVisibility(View.GONE);
                    if(getActivity()!=null)
                    {
                        Alerter.create(getActivity())
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                    Log.e("API_Response", "follower_ranking Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                no_follower_ranking.setVisibility(View.GONE);
                follower_ranking_layout.setVisibility(View.GONE);
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);

                if(getActivity()!=null)
                {
                    Alerter.create(getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }
                Log.d("API_Error", "follower_ranking Error : " + t.getMessage());
            }
        });
    }
}
