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
import com.relinns.viegram.Adapter.NameAdapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Comment_like extends AppCompatActivity implements View.OnClickListener {

    private RelativeLayout badgeLayout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout menu_home;
    private RelativeLayout menu_open;
    private RelativeLayout menu_close;
    private RelativeLayout menu_profile;
    private RelativeLayout menu_stat;
    private RelativeLayout menu_follow;
    private RelativeLayout menu_notifications;
    private RelativeLayout menu_settings;
    private RelativeLayout menu_search;
    private RelativeLayout menu_ranking;
    private RelativeLayout menu_camera;
    private ImageView menu_click;
    private String comment_id;
    private SharedPreferences preferences;
    private NameAdapter adapter;
    private RecyclerView likecomment_list;
    private TextView badgeText;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_like);
        comment_id = getIntent().getStringExtra("comment_id");

        progress = (ProgressBar) findViewById(R.id.progress);
        likecomment_list = (RecyclerView) findViewById(R.id.likecomment_list);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        menu_click = (ImageView) findViewById(R.id.likecomment_menu_click);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        menu_open = (RelativeLayout) findViewById(R.id.likecomment_menu_open);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);

        menu_click.setOnClickListener(this);
        menu_home.setOnClickListener(this);
        back.setOnClickListener(this);
        menu_close.setOnClickListener(this);
        menu_follow.setOnClickListener(this);
        menu_ranking.setOnClickListener(this);
        menu_search.setOnClickListener(this);
        menu_notifications.setOnClickListener(this);
        menu_profile.setOnClickListener(this);
        menu_camera.setOnClickListener(this);
        menu_settings.setOnClickListener(this);
        menu_stat.setOnClickListener(this);
        activity_layout.setOnClickListener(this);
        menu_open.setVisibility(View.GONE);
        likecomment_list.setVisibility(View.GONE);
        progress_layout.setVisibility(View.VISIBLE);
        get_likes();
    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Stats.class);
            i.putExtra("stats_header", "My stats");
            i.putExtra("stats_id", preferences.getString("user_id", ""));
            startActivity(i);
            transition();
        }

        if (v == menu_click) {
            if (preferences.getInt("badge_value", 0) != 0) {
                badgeLayout.setVisibility(View.VISIBLE);
                badgeText.setText(preferences.getInt("badge_value", 0) + "");
            } else {
                badgeLayout.setVisibility(View.GONE);
            }
            menu_open.setVisibility(View.VISIBLE);
            menu_click.setVisibility(View.GONE);
        }
        if (v == menu_close) {
            menu_status();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Comment_like.this, Timeline.class);
            startActivity(i);
            transition();
        }
    }

    //transition animation
    private void transition() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    // opened menu visibility gone
    private void menu_status() {

        menu_open.setVisibility(View.GONE);
        menu_click.setVisibility(View.VISIBLE);
    }

    //get comment likes api
    private void get_likes() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "comment_like_users");
        postParam.put("commentid", comment_id);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Log.d("API_Parameters", "comment_like_users parameters :" + postParam.toString());

        Call<API_Response> call = service.FriendsWork(postParam);
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.e("API_Response", "comment_like_users Response : " + new Gson().toJson(response.body()));
                    progress_layout.setVisibility(View.GONE);
                    if (response.body().getResult().getMsg().equals("201")) {
                        likecomment_list.setVisibility(View.VISIBLE);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Comment_like.this);
                        likecomment_list.setLayoutManager(mLayoutManager);
                        adapter = new NameAdapter(Comment_like.this, response.body().getResult().getLikeComment());
                        likecomment_list.setAdapter(adapter);
                    } else if (!response.body().getResult().getMsg().equals("204")) {
                        Alerter.create(Comment_like.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }

                } else {
                    Alerter.create(Comment_like.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "comment_like_users Response : " + new Gson().toJson(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                likecomment_list.setVisibility(View.GONE);
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
             Alerter.create(Comment_like.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "comment_like_users Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Comment_like.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }
}
