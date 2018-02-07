package com.relinns.viegram.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.relinns.viegram.Adapter.Follower_Adapter;
import com.relinns.viegram.Adapter.Following_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.Pojo.FollowerList;
import com.relinns.viegram.Pojo.FollowingList;
import com.relinns.viegram.Pojo.UserData;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Follower_following extends AppCompatActivity implements View.OnClickListener {

    private TextView badgeText;
    private TextView follower_text;
    private TextView following_text;
    private TextView full_name;
    private TextView no_follower_text;
    private RelativeLayout badgeLayout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout search_follower_view;
    private RelativeLayout followers;
    private RelativeLayout following;
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
    private ImageView profile_image;
    private ImageView cover_image;
    private List<FollowerList> temp_data;
    private List<FollowerList> followerList;
    private List<FollowingList> followingList, tempFollowing;
    private LinearLayout no_follower;
    private SharedPreferences preferences;
    private ProgressBar progress;
    private RecyclerView follower_list, followingRV;
    private Follower_Adapter follower_adapter;
    private Following_Adapter following_adapter;
    private String follow_code;
    private EditText search_follower;
    private Result result;
    private String totalFollower = "", totalFollowing = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follower_following);

        followerList = new ArrayList<>();
        followingList = new ArrayList<>();
        temp_data = new ArrayList<>();
        tempFollowing = new ArrayList<>();
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        follow_code = "0";
        follower_list = (RecyclerView) findViewById(R.id.follower_list);
        followingRV = (RecyclerView) findViewById(R.id.following_list);

        no_follower = (LinearLayout) findViewById(R.id.no_follower);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        followers = (RelativeLayout) findViewById(R.id.followers);
        following = (RelativeLayout) findViewById(R.id.followed);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        menu_open_layout = (RelativeLayout) findViewById(R.id.follower_following_menu_open);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);

        follower_text = (TextView) findViewById(R.id.follower_text);
        following_text = (TextView) findViewById(R.id.following_text);
        full_name = (TextView) findViewById(R.id.full_name);
        no_follower_text = (TextView) findViewById(R.id.no_folwr_text);

        profile_image = (ImageView) findViewById(R.id.profile_image);
        cover_image = (ImageView) findViewById(R.id.cover_image);
        menu_click_view = (ImageView) findViewById(R.id.follower_following_menu_click);
        search_follower_view = (RelativeLayout) findViewById(R.id.search_follower_view);

        search_follower = (EditText) findViewById(R.id.search_follower);
        back = (RelativeLayout) findViewById(R.id.back);


//set profile and cover image dara
        Glide.with(this).load(preferences.getString("profile_image", ""))
            //    .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(profile_image);
        Glide.with(this).load(preferences.getString("cover_image", ""))
             //   .thumbnail(0.01f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .into(cover_image);
        full_name.setText(preferences.getString("display_name", ""));

        followingRV.setVisibility(View.GONE);
        search_follower_view.setOnClickListener(this);
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
        followers.setOnClickListener(this);
        following.setOnClickListener(this);
        back.setOnClickListener(this);
        activity_layout.setOnClickListener(this);

        search_follower.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                search_friend(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
//        get_count();
        menu_open_layout.setVisibility(View.GONE);
        no_follower.setVisibility(View.GONE);
        if (Timeline.resultp != null) {
            progress_layout.setVisibility(View.GONE);
            followerList = Timeline.resultp.getFollowerList();
            totalFollowing = Timeline.resultp.getTotalFollowings();
            totalFollower = Timeline.resultp.getTotalFollowers();
            followingList = Timeline.resultp.getFollowingList();

            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follower_following.this);
            follower_list.setLayoutManager(mLayoutManager);
            follower_adapter = new Follower_Adapter(Follower_following.this, followerList);
            follower_list.setAdapter(follower_adapter);

            RecyclerView.LayoutManager mManager = new LinearLayoutManager(Follower_following.this);
            followingRV.setLayoutManager(mManager);
            following_adapter = new Following_Adapter(Follower_following.this, followingList);
            followingRV.setAdapter(following_adapter);
            setData();
//            setFollowerData();

        } else {
            progress_layout.setVisibility(View.VISIBLE);
            follower_list.setVisibility(View.GONE);
            try {
//                get_follower();
                getData();
            } catch (TimeoutException e) {
                follower_list.setVisibility(View.GONE);
                no_follower.setVisibility(View.GONE);
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                Alerter.create(Follower_following.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                e.printStackTrace();
            }
        }

    }

    private void setData() {
        follower_text.setText("Followers (" + totalFollower + ")");
        following_text.setText("Following (" + totalFollowing + ")");
        if (follow_code.equals("0")) {
            followingRV.setVisibility(View.GONE);
            if (followerList.size() != 0) {
                follower_list.setVisibility(View.VISIBLE);
                no_follower.setVisibility(View.GONE);
            } else {

                follower_list.setVisibility(View.GONE);
                no_follower.setVisibility(View.VISIBLE);
                no_follower_text.setText(getResources().getString(R.string.no_follower));
            }
        } else {

            follower_list.setVisibility(View.GONE);
            if (followingList.size() != 0) {
                followingRV.setVisibility(View.VISIBLE);
                no_follower.setVisibility(View.GONE);
            } else {

                followingRV.setVisibility(View.GONE);
                no_follower.setVisibility(View.VISIBLE);
                no_follower_text.setText(getResources().getString(R.string.no_following));
            }
        }
    }

    private void setFollowerData() {
        followingRV.setVisibility(View.GONE);
        follower_list.setVisibility(View.VISIBLE);
        Log.d("Follower/ing", "Follwer :" + followerList.size());
        follower_text.setText("Followers (" + totalFollower + ")");
        following_text.setText("Following (" + totalFollowing + ")");
        Log.d("Follower", "list data : " + new Gson().toJson(followerList));
        if (followerList.size() != 0) {
            no_follower.setVisibility(View.GONE);
            follower_list.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follower_following.this);
            follower_list.setLayoutManager(mLayoutManager);
            follower_adapter = new Follower_Adapter(Follower_following.this, followerList);
            follower_list.setAdapter(follower_adapter);
        } else {
            no_follower.setVisibility(View.VISIBLE);
            follower_list.setVisibility(View.GONE);
            no_follower_text.setText(getResources().getString(R.string.no_follower));

        }
    }

    //search friend
    private void search_friend(String name) {
        if (!tempFollowing.isEmpty()) {
            tempFollowing.clear();
        }
        if (!temp_data.isEmpty())
            temp_data.clear();
        if (follow_code.equals("0")) {

            if (!followerList.isEmpty()) {
                for (int data = 0; data < followerList.size(); data++) {
                    if (followerList.get(data).getDisplayName().toLowerCase().contains(name)) {
                        temp_data.add(followerList.get(data));
                    }
                }
                follower_list.setVisibility(View.VISIBLE);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follower_following.this);
                follower_list.setLayoutManager(mLayoutManager);
                follower_adapter = new Follower_Adapter(Follower_following.this, temp_data);
                follower_list.setAdapter(follower_adapter);
            }
        } else if (follow_code.equals("1")) {

            if (!followingList.isEmpty()) {
                for (int data = 0; data < followingList.size(); data++) {
                    if (followingList.get(data).getDisplayName().toLowerCase().contains(name)) {
                        tempFollowing.add(followingList.get(data));
                    }
                }
                followingRV.setVisibility(View.VISIBLE);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follower_following.this);
                followingRV.setLayoutManager(mLayoutManager);
                following_adapter = new Following_Adapter(Follower_following.this, tempFollowing);
                followingRV.setAdapter(following_adapter);
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == search_follower_view) {
            if (search_follower.getText().toString().equals("")) {
                search_follower.requestFocus();
            } else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity_layout.getWindowToken(), 0);
                search_friend(search_follower.getText().toString());
            }
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Follower_following.this, Timeline.class);
            startActivity(i);
            transition();
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == followers) {
            callFollowersMethod();
        }
        if (v == following) {
            follow_code = "1";
            following.setBackground(getResources().getDrawable(R.drawable.login_bg));
            following_text.setTextColor(getResources().getColor(R.color.white));
            followers.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            follower_text.setTextColor(getResources().getColor(R.color.login_bg));
            setData();
//            setFollowingData(followingList);
//
//
//            try {
//                get_following();
//            } catch (TimeoutException e) {
//                follower_list.setVisibility(View.GONE);
//                no_follower.setVisibility(View.GONE);
//                progress_layout.setVisibility(View.VISIBLE);
//                progress.setVisibility(View.GONE);
//                Alerter.create(Follower_following.this)
//                        .setText(R.string.network_error)
//                        .setBackgroundColor(R.color.login_bg)
//                        .show();
//                e.printStackTrace();
//            }



        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Follower_following.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            callFollowersMethod();
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Follower_following.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Follower_following.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Follower_following.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Follower_following.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Follower_following.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Follower_following.this, Stats.class);
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

    public void setFollowingData(List<FollowingList> list) {

        follower_list.setVisibility(View.GONE);
        follower_text.setText("Followers (" + totalFollower + ")");
        following_text.setText("Following (" + totalFollowing + ")");

        if (list.size() != 0) {
            followingRV.setVisibility(View.VISIBLE);
            no_follower.setVisibility(View.GONE);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follower_following.this);
            followingRV.setLayoutManager(mLayoutManager);
            following_adapter = new Following_Adapter(Follower_following.this, list);
            followingRV.setAdapter(following_adapter);
        } else {
            no_follower.setVisibility(View.VISIBLE);
            followingRV.setVisibility(View.GONE);
            no_follower_text.setText(getResources().getString(R.string.no_following));


        }
    }

    private void callFollowersMethod() {
        follow_code = "0";
        followers.setBackground(getResources().getDrawable(R.drawable.login_bg));
        follower_text.setTextColor(getResources().getColor(R.color.white));
        following.setBackground(getResources().getDrawable(R.drawable.stats_bg));
        following_text.setTextColor(getResources().getColor(R.color.login_bg));
        setData();
//        get_count();
//        setFollowerData();
//        try {
//
//            get_follower();
//        } catch (TimeoutException e) {
//            follower_list.setVisibility(View.GONE);
//            no_follower.setVisibility(View.GONE);
//            progress_layout.setVisibility(View.VISIBLE);
//            progress.setVisibility(View.GONE);
//            Alerter.create(Follower_following.this)
//                    .setText(R.string.network_error)
//                    .setBackgroundColor(R.color.login_bg)
//                    .show();
//            e.printStackTrace();
//        }

    }

    //transition animation
    private void transition() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    // opened menu visibility gone
    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    //get follower following
    public void getData() throws TimeoutException {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "merge_list");
        postParams.put("userid", preferences.getString("user_id", ""));

        Log.d("API_Parameters", "merge_list parameters :" + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<UserData> call = service.getFollower(postParams);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                progress_layout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.e("API_Response", "merge_list Response : " + new Gson().toJson(response.body()));
                    followerList = new ArrayList<FollowerList>();
                    followingList = new ArrayList<FollowingList>();
                    if (response.body().getResult().getMsg().equals("201")) {
                        followerList = response.body().getResult().getFollowerList();
                        followingList = response.body().getResult().getFollowingList();
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follower_following.this);
                        follower_list.setLayoutManager(mLayoutManager);
                        follower_adapter = new Follower_Adapter(Follower_following.this, followerList);
                        follower_list.setAdapter(follower_adapter);

                        RecyclerView.LayoutManager mManager = new LinearLayoutManager(Follower_following.this);
                        followingRV.setLayoutManager(mManager);
                        following_adapter = new Following_Adapter(Follower_following.this, followingList);
                        followingRV.setAdapter(following_adapter);

                    } else if (response.body().getResult().getMsg().equals("204")) {

                    } else Alerter.create(Follower_following.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();

                    totalFollower = response.body().getResult().getTotalFollowers();
                    totalFollowing = response.body().getResult().getTotalFollowings();
                    if (Timeline.resultp != null) {
                        Timeline.resultp.setFollowerList(followerList);
                        Timeline.resultp.setFollowingList(followingList);
                        Timeline.resultp.setTotalFollowers(totalFollower);
                        Timeline.resultp.setTotalFollowings(totalFollowing);
                    }
                    setData();

                } else {
                    Alerter.create(Follower_following.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "merge_list Response : " + new Gson().toJson(response.errorBody()));
                }
            }


            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                follower_list.setVisibility(View.GONE);
                no_follower.setVisibility(View.GONE);
                progress_layout.setVisibility(View.GONE);

             Alerter.create(Follower_following.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "merge_list Error : " + t.getMessage());
            }
        });
    }


//    //get followers
//    private void get_follower() throws TimeoutException {
//        Map<String, String> postParams = new HashMap<>();
//        postParams.put("action", "follower_list");
//        postParams.put("userid", preferences.getString("user_id", ""));
//
//        Log.d("API_Parameters", "follower_list parameters :" + postParams.toString());
//        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
//        Call<UserData> call = service.getFollower(postParams);
//        call.enqueue(new Callback<UserData>() {
//            @Override
//            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
//                progress_layout.setVisibility(View.GONE);
//                if (response.isSuccessful()) {
//                    Log.e("API_Response", "follower_list Response : " + new Gson().toJson(response.body()));
//                    followerList = new ArrayList<FollowerList>();
//                    if (response.body().getResult().getMsg().equals("201")) {
//
//                        followerList = response.body().getResult().getFollowerList();
//                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Follower_following.this);
//                        follower_list.setLayoutManager(mLayoutManager);
//                        follower_adapter = new Follower_Adapter(Follower_following.this, followerList);
//                        follower_list.setAdapter(follower_adapter);
//                    } else if (response.body().getResult().getMsg().equals("204")) {
//
//
//                    } else Alerter.create(Follower_following.this)
//                            .setText(R.string.network_error)
//                            .setBackgroundColor(R.color.login_bg)
//                            .show();
//                    if (Timeline.resultp != null)
//                        Timeline.resultp.setFollowerList(followerList);
//                    totalFollower = response.body().getResult().getTotalFollowers();
//                    totalFollowing = response.body().getResult().getTotalFollowings();
////                    setFollowerData();
//                    setData();
//                } else {
//                    Alerter.create(Follower_following.this)
//                            .setText(R.string.network_error)
//                            .setBackgroundColor(R.color.login_bg)
//                            .show();
//                    Log.e("API_Response", "follower_list Response : " + new Gson().toJson(response.errorBody()));
//                }
//
//
//            }
//
//
//            @Override
//            public void onFailure(Call<UserData> call, Throwable t) {
//                follower_list.setVisibility(View.GONE);
//                no_follower.setVisibility(View.GONE);
//                progress_layout.setVisibility(View.VISIBLE);
//                progress.setVisibility(View.GONE);
//
//                    Alerter.create(Follower_following.this)
//                            .setText(R.string.no_internet_msg)
//                            .setBackgroundColor(R.color.login_bg)
//                            .show();
//                else Alerter.create(Follower_following.this)
//                        .setText(R.string.network_error)
//                        .setBackgroundColor(R.color.login_bg)
//                        .show();
//                Log.d("API_Error", "following_list Error : " + t.getMessage());
//            }
//        });
//    }
//
//    //get following
//    public void get_following() throws TimeoutException {
//        Map<String, String> postParams = new HashMap<>();
//        postParams.put("action", "following_list");
//        postParams.put("userid", preferences.getString("user_id", ""));
//
//        Log.d("API_Parameters", "following_list parameters :" + postParams.toString());
//        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
//        Call<UserData> call = service.getFollower(postParams);
//        call.enqueue(new Callback<UserData>() {
//            @Override
//            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
//                if (response.isSuccessful()) {
//                    Log.e("API_Response", "following_list Response : " + new Gson().toJson(response.body()));
//                    progress_layout.setVisibility(View.GONE);
//                    followingList = new ArrayList<FollowingList>();
//                    if (response.body().getResult().getMsg().equals("201")) {
//
////                        if (!followingList.isEmpty())
////                            followingList.clear();
//                        followingList = response.body().getResult().getFollowingList();
//                        RecyclerView.LayoutManager mManager = new LinearLayoutManager(Follower_following.this);
//                        followingRV.setLayoutManager(mManager);
//                        following_adapter = new Following_Adapter(Follower_following.this, followingList);
//                        followingRV.setAdapter(following_adapter);
//                    } else if (response.body().getResult().getMsg().equals("204")) {
//
////                        if (!followerList.isEmpty())
////                            followerList.clear();
//                    } else Alerter.create(Follower_following.this)
//                            .setText(R.string.network_error)
//                            .setBackgroundColor(R.color.login_bg)
//                            .show();
//
//                    Log.d("follower/ing", "Following api :" + followingList.size());
//                    totalFollower = response.body().getResult().getTotalFollowers();
//                    totalFollowing = response.body().getResult().getTotalFollowings();
//                    if (Timeline.resultp != null)
//                        Timeline.resultp.setFollowingList(followingList);
//                    setData();
////                    setFollowingData(followingList);
//                } else {
//                    Alerter.create(Follower_following.this)
//                            .setText(R.string.network_error)
//                            .setBackgroundColor(R.color.login_bg)
//                            .show();
//
//                    Log.e("API_Response", "following_list Response : " + new Gson().toJson(response.errorBody()));
//                }
//            }
//
//
//            @Override
//            public void onFailure(Call<UserData> call, Throwable t) {
//                follower_list.setVisibility(View.GONE);
//                no_follower.setVisibility(View.GONE);
//                progress_layout.setVisibility(View.VISIBLE);
//                progress.setVisibility(View.GONE);
//              Alerter.create(Follower_following.this)
//                        .setText(R.string.network_error)
//                        .setBackgroundColor(R.color.login_bg)
//                        .show();
//                Log.d("API_Error", "following_list Error : " + t.getMessage());
//            }
//        });
//    }

    //get total count of follower and following
    public void get_count() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "follower_following_total");
        postParams.put("userid", preferences.getString("user_id", ""));
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Log.d("API_Parameters", "follower_following_total parameters :" + postParams.toString());
        Call<API_Response> call = service.pointsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.e("API_Response", "follower_following_total Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        totalFollowing = response.body().getResult().getTotalFollowings();
                        totalFollower = response.body().getResult().getTotalFollowers();
                        following_text.setText("Following (" + response.body().getResult().getTotalFollowings() + ")");
                        follower_text.setText("Followers (" + response.body().getResult().getTotalFollowers() + ")");
//                        Timeline.resultp.setTotalFollowings(totalFollowing);
//                        Timeline.resultp.setTotalFollowers(totalFollower);
                    }
                } else
                    Log.e("API_Response", "follower_following_total Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                Log.d("API_Error", "follower_following_total Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Follower_following.this, Timeline.class);
        startActivity(i);
        Follower_following.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }
}

