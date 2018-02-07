package com.relinns.viegram.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.relinns.viegram.Adapter.Follow_people_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;


public class Follow_people extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView follow_people_list;
    private RelativeLayout badgeLayout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout menu_home;
    private RelativeLayout menu_open_layout;
    private RelativeLayout menu_close;
    private RelativeLayout menu_profile;
    private RelativeLayout menu_stat;
    private RelativeLayout menu_follow;
    private RelativeLayout menu_notifications;
    private RelativeLayout menu_settings;
    private RelativeLayout menu_search;
    private RelativeLayout menu_ranking;
    private RelativeLayout menu_camera;
    private ImageView menu_click_view;
    private TextView badgeText;
    private ProgressBar progress;
    private SharedPreferences preferences;
private LinearLayout noDataLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_people);

        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
noDataLayout=(LinearLayout)findViewById(R.id.no_follower);
        follow_people_list = (RecyclerView) findViewById(R.id.follow_people_list);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        progress = (ProgressBar) findViewById(R.id.progress);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        menu_open_layout = (RelativeLayout) findViewById(R.id.follow_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.follow_menu_click);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);

        menu_home.setOnClickListener(this);
        back.setOnClickListener(this);
        menu_follow.setOnClickListener(this);
        menu_ranking.setOnClickListener(this);
        menu_open_layout.setOnClickListener(this);
        menu_search.setOnClickListener(this);
        menu_notifications.setOnClickListener(this);
        menu_profile.setOnClickListener(this);
        menu_camera.setOnClickListener(this);
        menu_click_view.setOnClickListener(this);
        menu_close.setOnClickListener(this);
        menu_settings.setOnClickListener(this);
        menu_stat.setOnClickListener(this);
        activity_layout.setOnClickListener(this);

        menu_open_layout.setVisibility(View.GONE);

        if (Settings.followPeople.size()!=0)
        {follow_people_list.setVisibility(View.VISIBLE);
            progress_layout.setVisibility(View.GONE);
            noDataLayout.setVisibility(View.GONE);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follow_people.this);
            follow_people_list.setLayoutManager(mLayoutManager);
            Follow_people_Adapter adapter = new Follow_people_Adapter(Follow_people.this, Settings.followPeople);
            follow_people_list.setAdapter(adapter);

        }
        else {
            follow_people_list.setVisibility(View.GONE);
            progress_layout.setVisibility(View.VISIBLE);
            noDataLayout.setVisibility(View.GONE);
            get_people();
        }

    }

    //get viegram users
    public void get_people() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "all_user_list");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "all_user_list parameters :" + postParams.toString());
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "all_user_list Response : " + new Gson().toJson(response.body()));
                progress_layout.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    if (response.body().getResult().getMsg().equals("201")) {
                        follow_people_list.setVisibility(View.VISIBLE);
                        noDataLayout.setVisibility(View.GONE);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follow_people.this);
                        follow_people_list.setLayoutManager(mLayoutManager);
                        Follow_people_Adapter adapter = new Follow_people_Adapter(Follow_people.this, response.body().getResult().getUserDetails());
                        follow_people_list.setAdapter(adapter);
                    } else {
                        noDataLayout.setVisibility(View.VISIBLE);
                        follow_people_list.setVisibility(View.GONE);
                        Alerter.create(Follow_people.this)
                                .setText("No People Found")
                                .setBackgroundColor(R.color.red)
                                .show();
                    }
                } else
                {
                    Alerter.create(Follow_people.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "all_user_list Response : " + new Gson().toJson(response.errorBody()));
                }

            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                follow_people_list.setVisibility(View.GONE);
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
               Alerter.create(Follow_people.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "all_user_list Error : " + t.getMessage());
            }
        });
    }


    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Timeline.class);
            startActivity(i);
            transition();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Follow_people.this, Stats.class);
            i.putExtra("stats_header", "My stats");
            i.putExtra("stats_id", preferences.getString("user_id", ""));
            startActivity(i);
            transition();
        }
        if (v == menu_click_view) {
            if (preferences.getInt("badge_value", 0) != 0) {
                badgeLayout.setVisibility(View.VISIBLE);
                badgeText.setText(preferences.getInt("badge_value", 0) + "");
            } else {
                badgeLayout.setVisibility(View.GONE);
            }
            menu_open_layout.setVisibility(View.VISIBLE);
            menu_click_view.setVisibility(View.GONE);
        }
        if (v == menu_close) {
            menu_status();
        }
    }

    //transition animation
    private void transition()
    {
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    // opened menu visibility gone
    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Follow_people.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }
}
