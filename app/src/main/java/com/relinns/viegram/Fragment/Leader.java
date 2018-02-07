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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.Adapter.Leader_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Leader extends Fragment {
    private ListView leader_list;
    private SharedPreferences preferences;
    private RelativeLayout progress_layout;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.leaders, container, false);
        leader_list = (ListView) v.findViewById(R.id.leader_list);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        progress_layout = (RelativeLayout) v.findViewById(R.id.progress_layout);
        preferences = getActivity().getSharedPreferences("Viegram", Context.MODE_PRIVATE);

        try {
            if (Timeline.rankingDetail.size() != 0) {
                progress_layout.setVisibility(View.GONE);
                leader_list.setVisibility(View.VISIBLE);
                Leader_Adapter adapter = new Leader_Adapter(getActivity(), Timeline.rankingDetail);
                leader_list.setAdapter(adapter);
            } else {
                progress_layout.setVisibility(View.VISIBLE);
                leader_list.setVisibility(View.GONE);
                get_ranking();
            }
        }catch (Exception e)
        {
            progress_layout.setVisibility(View.VISIBLE);
            leader_list.setVisibility(View.GONE);
            get_ranking();
        }
        return v;
    }

    private void get_ranking() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "overall_ranking");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "overall_ranking parameters :" + postParams.toString());
        Call<API_Response> call = service.rankingRelated(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "overall_ranking Response : " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getResult().getMsg().equals("201")) {
                        progress_layout.setVisibility(View.GONE);
                        leader_list.setVisibility(View.VISIBLE);
                        Leader_Adapter adapter = new Leader_Adapter(getActivity(), response.body().getResult().getRankingDetails());
                        leader_list.setAdapter(adapter);
                    } else {
                        progress_layout.setVisibility(View.VISIBLE);
                        leader_list.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        if(getActivity()!=null)
                        {
                            Alerter.create(getActivity())
                                    .setText(R.string.network_error)
                                    .setBackgroundColor(R.color.login_bg)
                                    .show();
                        }
                    }
                } else {
                    if(getActivity()!=null)
                    {
                        Alerter.create(getActivity())
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                    Log.e("API_Response", "overall_ranking Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                leader_list.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);

                if(getActivity()!=null)
                {
                    Alerter.create(getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }
                Log.d("API_Error", "overall_ranking Error : " + t.getMessage());
            }
        });
    }
}
