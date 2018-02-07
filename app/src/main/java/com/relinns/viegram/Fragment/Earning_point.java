package com.relinns.viegram.Fragment;

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
import android.widget.ScrollView;
import android.widget.TextView;
import com.google.gson.Gson;
import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.Adapter.Earninghint_Adapter;
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

public class Earning_point extends Fragment {
    private RecyclerView point_list;
    private Earninghint_Adapter earninghint_adapter;
    private TextView note;
    private ScrollView working_layout;
    private RelativeLayout progress_layout;
    private ProgressBar progress;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.earning_point, container, false);

        point_list = (RecyclerView) v.findViewById(R.id.point_list);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        progress_layout = (RelativeLayout) v.findViewById(R.id.progress_layout);
        working_layout = (ScrollView) v.findViewById(R.id.working_layout);
        note = (TextView) v.findViewById(R.id.note);

        if (Timeline.resultp!=null)
        {
            progress_layout.setVisibility(View.GONE);
            working_layout.setVisibility(View.VISIBLE);
            note.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            point_list.setLayoutManager(mLayoutManager);
            earninghint_adapter = new Earninghint_Adapter(getActivity(), Timeline.resultp.getHints());
            point_list.setAdapter(earninghint_adapter);
        }
        else {
            note.setVisibility(View.GONE);
            progress_layout.setVisibility(View.VISIBLE);
            working_layout.setVisibility(View.GONE);
            earning_hint();
        }

        return v;
    }

    private void earning_hint() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "earning_hints");
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "earning_hints parameters :" + postParams.toString());
        Call<UserData> call = service.Earnings(postParams);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                Log.e("API_Response", "earning_hints Response : " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    if (response.body().getResult().getMsg().equals("201")) {
                        progress_layout.setVisibility(View.GONE);
                        working_layout.setVisibility(View.VISIBLE);
                        note.setVisibility(View.VISIBLE);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                        point_list.setLayoutManager(mLayoutManager);
                        earninghint_adapter = new Earninghint_Adapter(getActivity(), response.body().getResult().getHints());
                        point_list.setAdapter(earninghint_adapter);
                    }

                    else {

                        if(getActivity()!=null)
                        {
                            Alerter.create(getActivity())
                                    .setText(R.string.network_error)
                                    .setBackgroundColor(R.color.login_bg)
                                    .show();
                        }
                    }

                } else
                {
                    progress_layout.setVisibility(View.VISIBLE);
                    working_layout.setVisibility(View.GONE);
                    note.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    if(getActivity()!=null)
                    {
                        Alerter.create(getActivity())
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                    Log.e("API_Response", "earning_hints Response : " + new Gson().toJson(response.errorBody()));
                }
            }
            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                working_layout.setVisibility(View.GONE);
                note.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);

                if(getActivity()!=null)
                {
                    Alerter.create(getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }
                Log.d("API_Error", "earning_hints Error : " + t.getMessage());
            }
        });
    }
}
