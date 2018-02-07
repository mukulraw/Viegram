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
import com.relinns.viegram.Adapter.Another_Follower_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.FollowerList_Model;
import com.relinns.viegram.Modal.Result;
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
public class Another_follower_following extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private RelativeLayout badgeLayout;
    private RelativeLayout search_layout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout search_user;
    private RelativeLayout menu_home;
    private RelativeLayout another_follower_button;
    private RelativeLayout another_following_button;
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
    private TextView another_follower_text;
    private TextView another_following_text;
    private TextView another_userName;
    private TextView badgeText;
    private ImageView menu_click;
    private ImageView another_user_profile_image;
    private ImageView cover_Image;
    private String another_follow_code = "1";
    private String totalfollowers = "0";
    private String total_followings = "0";
    private SharedPreferences preferences;
    private List<FollowerList_Model> mfollowerList, mfollowingList, temp_data;
    private RecyclerView another_follower_list;
    private Another_Follower_Adapter adapter;
    private ProgressBar progress;
    private LinearLayout emptyLayout;
    private EditText search_another_follower;
    private Result result;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_follower_following);

        //initialize
        mfollowerList = new ArrayList<>();
        mfollowingList = new ArrayList<>();
        temp_data = new ArrayList<>();
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_follow_code", "1");
        editor.commit();

        //get intent data
        String profile_image = getIntent().getStringExtra("Profile_image");
        String display_name = getIntent().getStringExtra("Display_name");
        String cover_image = getIntent().getStringExtra("Cover_image");

        //id declaration
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        search_layout = (RelativeLayout) findViewById(R.id.search_layout);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        search_user = (RelativeLayout) findViewById(R.id.search_user);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        another_follower_button = (RelativeLayout) findViewById(R.id.another_follower_button);
        another_following_button = (RelativeLayout) findViewById(R.id.another_following_button);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        menu_open = (RelativeLayout) findViewById(R.id.menu_open);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);

        progress = (ProgressBar) findViewById(R.id.progress);
        search_another_follower = (EditText) findViewById(R.id.search_another_follower);
        emptyLayout = (LinearLayout) findViewById(R.id.empty_layout);
        another_follower_list = (RecyclerView) findViewById(R.id.follower_list);

        cover_Image = (ImageView) findViewById(R.id.cover_image);
        another_user_profile_image = (ImageView) findViewById(R.id.profile_image);
        menu_click = (ImageView) findViewById(R.id.menu_click);
        another_userName = (TextView) findViewById(R.id.name_user);
        badgeText = (TextView) findViewById(R.id.badge_text);
        another_follower_text = (TextView) findViewById(R.id.another_follower_text);
        another_following_text = (TextView) findViewById(R.id.another_following_txt);

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
        another_following_button.setOnClickListener(this);
        another_follower_button.setOnClickListener(this);
        search_user.setOnClickListener(this);
        search_another_follower.addTextChangedListener(this);


        //setData
        Glide.with(this).load(profile_image)
            //    .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .bitmapTransform(new CropCircleTransformation(this))
                .into(another_user_profile_image);
        another_userName.setText(display_name);
        Glide.with(this).load(cover_image)
              //  .thumbnail(0.01f)
                .into(cover_Image);

        menu_open.setVisibility(View.GONE);
        progress_layout.setVisibility(View.VISIBLE);
        another_follower_list.setVisibility(View.GONE);
//        get_count();
        try {
            get_followers(another_follow_code);
        } catch (TimeoutException e) {
            progress_layout.setVisibility(View.VISIBLE);
            another_follower_list.setVisibility(View.GONE);
            emptyLayout.setVisibility(View.GONE);
            progress.setVisibility(View.GONE);
            Alerter.create(Another_follower_following.this)
                    .setText(R.string.network_error)
                    .setBackgroundColor(R.color.login_bg)
                    .show();
            Log.d("API_", "Timeout Error : " + e.getMessage());
            e.printStackTrace();
        }
    }

    //get follower following count
//    private void get_count() {
//        Map<String, String> postParams = new HashMap<>();
//        postParams.put("action", "follower_following_total");
//        postParams.put("userid", preferences.getString("another_user", ""));
//
//        Log.d("API_Parameters", " follower_following_total params : " + postParams.toString());
//        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
//        Call<API_Response> call = service.pointsWork(postParams);
//        call.enqueue(new Callback<API_Response>() {
//            @Override
//            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
//                if (response.isSuccessful()) {
//                    Log.d("API_Response", "follower_following_total Response : " + new Gson().toJson(response.body()));
//                    if (response.body().getResult().getMsg().equals("201")) {
//                        totalfollowers = response.body().getResult().getTotalFollowers();
//                        total_followings = response.body().getResult().getTotalFollowings();
//                        another_following_text.setText("Following (" + total_followings + ")");
//                        another_follower_text.setText("Followers (" + totalfollowers + ")");
//                    }
//                } else
//                    Log.e("API_Response", "fetch_user_profile Response : " + new Gson().toJson(response.errorBody()));
//            }
//
//            @Override
//            public void onFailure(Call<API_Response> call, Throwable t) {
//                Log.d("API_Error", "fetch_user_profile Error : " + t.getMessage());
//            }
//        });
//    }


    @Override
    protected void onPause() {
        super.onPause();
        userId = preferences.getString("another_user", "");
        Log.d("Another", "user_id : " + userId);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_user", userId);
        editor.commit();
        Log.d("Another", "user_id return : " + userId);
    }

    //get follower following data
    private void get_followers(final String code) throws TimeoutException {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "user_follower_list");
        postParams.put("userid", preferences.getString("user_id", ""));
        postParams.put("userid2", preferences.getString("another_user", ""));
//        postParams.put("follow", code);

        Log.d("API_Parameters", " another_user_follower_list params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Call<API_Response> call = service.FriendsWork(postParams);
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.d("API_Response", "another_user_follower_list Response : " + new Gson().toJson(response.body()));
                    progress_layout.setVisibility(View.GONE);
                    result = response.body().getResult();
                    if (result.getMsg().equals("201")) {

                        another_follower_list.setVisibility(View.VISIBLE);
                        emptyLayout.setVisibility(View.GONE);
                        mfollowerList = result.getFollowerList();
                        mfollowingList = result.getFollowingList();

                    } else if (result.getMsg().equals("204")) {

                    } else Alerter.create(Another_follower_following.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    total_followings = result.getTotalFollowings();
                    totalfollowers = result.getTotalFollowers();
                    setData();
                } else {
                    Log.e("API_Response", "fetch_user_profile Response : " + new Gson().toJson(response.errorBody()));
                    Alerter.create(Another_follower_following.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                another_follower_list.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                Alerter.create(Another_follower_following.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "fetch_user_profile Error : " + t.getMessage());
            }
        });
    }

    private void setData() {
        another_follower_text.setText("Followers (" + totalfollowers + ")");
        another_following_text.setText("Following (" + total_followings + ")");
        if (another_follow_code.equals("1")) {
            if (mfollowerList.size() != 0) {
                another_follower_list.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Another_follower_following.this);
                another_follower_list.setLayoutManager(mLayoutManager);
                adapter = new Another_Follower_Adapter(Another_follower_following.this, mfollowerList);
                another_follower_list.setAdapter(adapter);

            } else {
                another_follower_list.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
        } else {
            if (mfollowingList.size() != 0) {
                another_follower_list.setVisibility(View.VISIBLE);
                emptyLayout.setVisibility(View.GONE);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Another_follower_following.this);
                another_follower_list.setLayoutManager(mLayoutManager);
                adapter = new Another_Follower_Adapter(Another_follower_following.this, mfollowingList);
                another_follower_list.setAdapter(adapter);

            } else {
                another_follower_list.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.VISIBLE);
            }
        }
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
            Intent i = new Intent(Another_follower_following.this, Stats.class);
            i.putExtra("stats_header", "My stats");
            i.putExtra("stats_id", preferences.getString("user_id", ""));
            startActivity(i);
            transition();
        }

        if (v == search_user) {
            if (search_another_follower.getText().toString().equals("")) {
                search_another_follower.requestFocus();
            } else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity_layout.getWindowToken(), 0);
                searchFriend(search_another_follower.getText().toString());

            }
        }
        if (v == another_follower_button) {
            another_follow_code = "1";
            setData();
//            try {
//                get_followers(another_follow_code);
//            } catch (TimeoutException e) {
//                progress_layout.setVisibility(View.VISIBLE);
//                another_follower_list.setVisibility(View.GONE);
//                emptyLayout.setVisibility(View.GONE);
//                progress.setVisibility(View.GONE);
//                Alerter.create(Another_follower_following.this)
//                        .setText(R.string.network_error)
//                        .setBackgroundColor(R.color.login_bg)
//                        .show();
//                Log.d("API_", "Timeout Error : " + e.getMessage());
//                e.printStackTrace();
//            }
            another_follower_button.setBackground(getResources().getDrawable(R.drawable.login_bg));
            another_follower_text.setTextColor(getResources().getColor(R.color.white));
            another_following_button.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            another_following_text.setTextColor(getResources().getColor(R.color.login_bg));
//            progress_layout.setVisibility(View.VISIBLE);
//            another_follower_list.setVisibility(View.GONE);
//            progress.setVisibility(View.VISIBLE);
        }
        if (v == another_following_button) {
            another_follow_code = "0";
            setData();
//            try {
//                get_followers(another_follow_code);
//            } catch (TimeoutException e) {
//                progress_layout.setVisibility(View.VISIBLE);
//                another_follower_list.setVisibility(View.GONE);
//                emptyLayout.setVisibility(View.GONE);
//                progress.setVisibility(View.GONE);
//                Alerter.create(Another_follower_following.this)
//                        .setText(R.string.network_error)
//                        .setBackgroundColor(R.color.login_bg)
//                        .show();
//                Log.d("API_", "Timeout Error : " + e.getMessage());
//                e.printStackTrace();
//            }
            another_following_button.setBackground(getResources().getDrawable(R.drawable.login_bg));
            another_following_text.setTextColor(getResources().getColor(R.color.white));
            another_follower_button.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            another_follower_text.setTextColor(getResources().getColor(R.color.login_bg));
//            progress_layout.setVisibility(View.VISIBLE);
//            another_follower_list.setVisibility(View.GONE);
//            progress.setVisibility(View.VISIBLE);
//            get_count();
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
            Intent i = new Intent(Another_follower_following.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Another_follower_following.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Another_follower_following.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Another_follower_following.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Another_follower_following.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Another_follower_following.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Another_follower_following.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Another_follower_following.this, Timeline.class);
            startActivity(i);
            transition();
        }
    }

    private void searchFriend(String name) {
        temp_data = new ArrayList<>();
        if (another_follow_code.equals("1")) {
            if (!mfollowerList.isEmpty()) {
                for (int data = 0; data < mfollowerList.size(); data++) {
                    if (mfollowerList.get(data).getDisplayName().toLowerCase().contains(search_another_follower.getText().toString())) {
                        temp_data.add(mfollowerList.get(data));
                    }
                }
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Another_follower_following.this);
                another_follower_list.setLayoutManager(mLayoutManager);
                adapter = new Another_Follower_Adapter(Another_follower_following.this, temp_data);
                another_follower_list.setAdapter(adapter);
            }
        } else {
            if (!mfollowingList.isEmpty()) {
                for (int data = 0; data < mfollowingList.size(); data++) {
                    if (mfollowingList.get(data).getDisplayName().toLowerCase().contains(search_another_follower.getText().toString())) {
                        temp_data.add(mfollowingList.get(data));
                    }
                }
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Another_follower_following.this);
                another_follower_list.setLayoutManager(mLayoutManager);
                adapter = new Another_Follower_Adapter(Another_follower_following.this, temp_data);
                another_follower_list.setAdapter(adapter);
            }
        }

    }

    // opened menu visibility gone
    private void menu_status() {
        menu_open.setVisibility(View.GONE);
        menu_click.setVisibility(View.VISIBLE);
    }

    //transition animation
    private void transition() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Another_follower_following.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    //edittext text watcher methods
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        if (charSequence.toString().equals("")) {
            try {
                get_followers(another_follow_code);
            } catch (TimeoutException e) {
                progress_layout.setVisibility(View.VISIBLE);
                another_follower_list.setVisibility(View.GONE);
                emptyLayout.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                Alerter.create(Another_follower_following.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                e.printStackTrace();
            }

        } else {
            Log.d("Tag", "data :" + charSequence);
            searchFriend(charSequence.toString());
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {
    }
}
