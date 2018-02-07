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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.relinns.viegram.Adapter.StatDetailAdapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.relinns.viegram.util.BottomOffsetDecoration;
import com.relinns.viegram.R;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;

public class Stats_Details extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView detail_stat;
    private StatDetailAdapter adapter;
    private TextView badgeText;
    private TextView stat_type;
    private TextView stat_title;
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
    private ProgressBar progress;
    private SharedPreferences preferences;
    private String stats_id;
    private String type;
    private String header_text;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats__details);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        progress = (ProgressBar) findViewById(R.id.progress);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        detail_stat = (RecyclerView) findViewById(R.id.stats_detail);
        stat_type = (TextView) findViewById(R.id.stat_type);
        stat_title = (TextView) findViewById(R.id.stat_title);
        menu_open_layout = (RelativeLayout) findViewById(R.id.detail_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.detail_menu_click);
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

        menu_open_layout.setVisibility(View.GONE);
        activity_layout.setOnClickListener(this);
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
        menu_home.setOnClickListener(this);
        back.setOnClickListener(this);

        stats_id = getIntent().getStringExtra("stats_id");
        header_text = getIntent().getStringExtra("header_text");
        stat_title.setText(header_text);
        type = getIntent().getStringExtra("type");
        if (type.equals("today")) {
            stat_type.setText("Points earned today");
        } else if (type.equals("weekly")) {
            stat_type.setText("Points earned this week");
        } else if (type.equals("monthly")) {
            stat_type.setText("Points earned this month");
        } else if (type.equals("yearly")) {
            stat_type.setText("Points earned this year");
        } else if (type.equals("overall")) {
            stat_type.setText("Overall points earned");
        }

        progress_layout.setVisibility(View.VISIBLE);
        detail_stat.setVisibility(View.GONE);
        stat_detail();

    }

    private void stat_detail() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "breakdown_stats");
        postParams.put("userid", stats_id);
        postParams.put("breakdown", type);
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "breakdown_stats parameters :" + postParams.toString());
        Call<API_Response> call = service.rankingRelated(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_layout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.e("API_Response", "breakdown_stats Response : " + new Gson().toJson(response.body()));
                    Result result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        detail_stat.setVisibility(View.VISIBLE);
                        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                        detail_stat.setLayoutManager(layoutManager);
                        adapter = new StatDetailAdapter(Stats_Details.this, result.getPoints());
                        detail_stat.setAdapter(adapter);
                        float offsetPx = getResources().getDimension(R.dimen.below_margin);
                        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
                        detail_stat.addItemDecoration(bottomOffsetDecoration);
                    } else Alerter.create(Stats_Details.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                } else {
                    Alerter.create(Stats_Details.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "breakdown_stats Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                Alerter.create(Stats_Details.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "breakdown_stats Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Stats_Details.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Stats_Details.this, Stats.class);
            i.putExtra("stats_header", "My stats");
            i.putExtra("stats_id", preferences.getString("user_id", ""));
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
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

    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }
}
