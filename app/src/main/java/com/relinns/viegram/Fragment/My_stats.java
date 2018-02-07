package com.relinns.viegram.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Detail;
import com.relinns.viegram.Pojo.ResultPojo;
import com.relinns.viegram.R;
import com.relinns.viegram.Activity.Stats_Details;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by win 7 on 5/30/2017.
 */
@SuppressLint("ValidFragment")
public class My_stats extends Fragment implements View.OnClickListener {
    private RelativeLayout progress_layout;
    private RelativeLayout today;
    private RelativeLayout week;
    private RelativeLayout month;
    private RelativeLayout year;
    private RelativeLayout overall;
    private TextView today_stats;
    private TextView week_stats;
    private TextView month_stats;
    private TextView year_stats;
    private TextView overall_stats;
    private SharedPreferences preferences;
    private ProgressBar progress;
    private LinearLayout working_layout;
    private static String stats_id;
    private static String header_text;

    public My_stats(){

    }

    public static void getInstance(String statsId, String headerText) {
        stats_id = statsId;
        header_text = headerText;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_stats, container, false);

        today = (RelativeLayout) v.findViewById(R.id.today);
        week = (RelativeLayout) v.findViewById(R.id.week);
        month = (RelativeLayout) v.findViewById(R.id.month);
        year = (RelativeLayout) v.findViewById(R.id.year);
        overall = (RelativeLayout) v.findViewById(R.id.overall);
        today_stats = (TextView) v.findViewById(R.id.today_stats);
        week_stats = (TextView) v.findViewById(R.id.week_stats);
        month_stats = (TextView) v.findViewById(R.id.month_stats);
        year_stats = (TextView) v.findViewById(R.id.year_stats);
        overall_stats = (TextView) v.findViewById(R.id.overall_stats);
        working_layout = (LinearLayout) v.findViewById(R.id.working_layout);
        progress = (ProgressBar) v.findViewById(R.id.progress);
        progress_layout = (RelativeLayout) v.findViewById(R.id.progress_layout);
        preferences = getActivity().getSharedPreferences("Viegram", Context.MODE_PRIVATE);
     if (stats_id.equals(preferences.getString("user_id",""))) {
         if (Timeline.resultp != null) {
             working_layout.setVisibility(View.VISIBLE);
             progress_layout.setVisibility(View.GONE);

             today_stats.setText(Timeline.resultp.getTotalTodayPoints());
             week_stats.setText(Timeline.resultp.getTotalWeekPoints());
             month_stats.setText(Timeline.resultp.getTotalMonthPoints());
             year_stats.setText(Timeline.resultp.getTotalYearPoints());
             overall_stats.setText(Timeline.resultp.getTotalOverallPoints());
         } else {
             working_layout.setVisibility(View.GONE);
             progress_layout.setVisibility(View.VISIBLE);
             mystats();
         }
     }
        else {
         working_layout.setVisibility(View.GONE);
         progress_layout.setVisibility(View.VISIBLE);
         mystats();
     }
        today.setOnClickListener(this);
        week.setOnClickListener(this);
        month.setOnClickListener(this);
        year.setOnClickListener(this);
        overall.setOnClickListener(this);
        return v;
    }

    private void mystats() {
       Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "get_stats");
        postParams.put("userid", stats_id);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "get_stats parameters :" + postParams.toString());
        Call<API_Response> call = service.rankingRelated(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "get_stats Response : " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (response.body().getResult().getMsg().equals("201")) {
                        working_layout.setVisibility(View.VISIBLE);
                        progress_layout.setVisibility(View.GONE);
                        today_stats.setText(response.body().getResult().getTotalTodayPoints());
                        week_stats.setText(response.body().getResult().getTotalWeekPoints());
                        month_stats.setText(response.body().getResult().getTotalMonthPoints());
                        year_stats.setText(response.body().getResult().getTotalYearPoints());
                        overall_stats.setText(response.body().getResult().getTotalOverallPoints());
                    } else {
                        working_layout.setVisibility(View.GONE);
                        progress_layout.setVisibility(View.VISIBLE);
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
                    Log.e("API_Response", "get_stats Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                working_layout.setVisibility(View.GONE);
                progress_layout.setVisibility(View.VISIBLE);
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

    @Override
    public void onClick(View v) {

        if (v == today) {
//            type = "today";
            Intent i = new Intent(getActivity(), Stats_Details.class);
            i.putExtra("header_text", header_text);
            i.putExtra("stats_id", stats_id);
            i.putExtra("type", "today");
            startActivity(i);
        }
        if (v == week) {
//            type = "weekly";
            Intent i = new Intent(getActivity(), Stats_Details.class);
            i.putExtra("stats_id", stats_id);
            i.putExtra("header_text", header_text);
            i.putExtra("type", "weekly");
            startActivity(i);
        }
        if (v == month) {
//            type = "monthly";
            Intent i = new Intent(getActivity(), Stats_Details.class);
            i.putExtra("stats_id", stats_id);
            i.putExtra("header_text", header_text);
            i.putExtra("type", "monthly");
            startActivity(i);
        }
        if (v == year) {
//            type = "yearly";
            Intent i = new Intent(getActivity(), Stats_Details.class);
            i.putExtra("stats_id", stats_id);
            i.putExtra("header_text", header_text);
            i.putExtra("type", "yearly");
            startActivity(i);
        }
        if (v == overall) {
//            type = "overall";
            Intent i = new Intent(getActivity(), Stats_Details.class);
            i.putExtra("stats_id", stats_id);
            i.putExtra("header_text", header_text);
            i.putExtra("type", "overall");
            startActivity(i);
        }

    }
}
