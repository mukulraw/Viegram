package com.relinns.viegram.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.relinns.viegram.Fragment.My_info;
import com.relinns.viegram.Fragment.My_photos;
import com.relinns.viegram.Modal.API_Response;
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
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Another_user extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private RelativeLayout badgeLayout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout menu_home;
    private RelativeLayout user_info;
    private RelativeLayout user_photos;
    private RelativeLayout follow_user;
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
    private RelativeLayout another_follower_data;
    private RelativeLayout another_stats;
    private TextView userinfo_text;
    private TextView userphotos_text;
    private TextView following;
    private TextView another_username;
    private TextView badgeText;
    private ImageView userinfo_view;
    private ImageView userphoto_view;
    private ImageView menu_click_view;
    private ImageView another_profile_image;
    private ImageView another_cover_imaage;
    private ProgressBar progress;
    private ProgressBar followProgress;
    private LinearLayout working_layout;
    private SharedPreferences preferences;
    private String display_name;
    private Result result;
    private ViewPager user_pager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_another_user);

        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        //notification badge code
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("decrement")) {
                int badge_value = preferences.getInt("badge_value", 0);
                badge_value--;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("badge_value", badge_value);
                editor.commit();
                ShortcutBadger.applyCount(getApplicationContext(), badge_value);

            }
        }
        followProgress = (ProgressBar) findViewById(R.id.follow_progress);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        user_pager = (ViewPager) findViewById(R.id.user_pager);
        following = (TextView) findViewById(R.id.following);
        working_layout = (LinearLayout) findViewById(R.id.working_layout);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        another_profile_image = (ImageView) findViewById(R.id.another_profile_image);
        another_follower_data = (RelativeLayout) findViewById(R.id.another_follower);
        another_stats = (RelativeLayout) findViewById(R.id.another_stat);
        another_username = (TextView) findViewById(R.id.another_username);
        back = (RelativeLayout) findViewById(R.id.back);
        another_cover_imaage = (ImageView) findViewById(R.id.another_cover_image);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        user_info = (RelativeLayout) findViewById(R.id.user_info);
        follow_user = (RelativeLayout) findViewById(R.id.follow_user);
        user_photos = (RelativeLayout) findViewById(R.id.user_photos);
        userinfo_text = (TextView) findViewById(R.id.userinfo_text);
        userphotos_text = (TextView) findViewById(R.id.userphotos_text);
        userinfo_view = (ImageView) findViewById(R.id.userinfo_view);
        userphoto_view = (ImageView) findViewById(R.id.userphoto_view);
        menu_open_layout = (RelativeLayout) findViewById(R.id.another_menu_open_layout);
        menu_click_view = (ImageView) findViewById(R.id.another_menu_click);
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

        activity_layout.setOnClickListener(this);
        back.setOnClickListener(this);
        menu_home.setOnClickListener(this);
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
        user_info.setOnClickListener(this);
        user_photos.setOnClickListener(this);
        following.setOnClickListener(this);
        another_stats.setOnClickListener(this);
        another_follower_data.setOnClickListener(this);
        follow_user.setOnClickListener(this);
        user_pager.addOnPageChangeListener(this);

        menu_open_layout.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        progress_layout.setVisibility(View.VISIBLE);
        working_layout.setVisibility(View.GONE);
        try {
            fetch_profile();
        } catch (TimeoutException e) {
            working_layout.setVisibility(View.GONE);
            progress_layout.setVisibility(View.VISIBLE);
            progress.setVisibility(View.GONE);
            Alerter.create(Another_user.this)
                    .setText(R.string.network_error)
                    .setBackgroundColor(R.color.login_bg)
                    .show();
            Log.d("API_", "Timeout Error : " + e.getMessage());
            e.printStackTrace();
        }


    }

    //visiting another user profile

    //fetch another user data
    private void fetch_profile() throws TimeoutException {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "fetch_user_profile");
        postParams.put("userid", preferences.getString("user_id", ""));
        postParams.put("userid2", preferences.getString("another_user", ""));
        postParams.put("page", "1");

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "fetch_user_profile parameters :" + postParams.toString());
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                working_layout.setVisibility(View.VISIBLE);
                progress_layout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "fetch_user_profile Response : " + new Gson().toJson(response.body()));

                    result = response.body().getResult();
                    if (result.getMsg().equals("201")) {

                        another_username.setText(result.getProfile_details().getDisplayName());
                        Glide.with(Another_user.this).load(result.getProfile_details().getProfileImage())
                             //   .thumbnail(0.1f)

                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .bitmapTransform(new CropCircleTransformation(Another_user.this))
                                .into(another_profile_image);

                        userphotos_text.setText("photos (" + result.getProfile_details().getTotalPosts() + ")");
                        Glide.with(Another_user.this).load(result.getProfile_details().getCoverImage())
                            //    .thumbnail(0.01f)
                                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                .into(another_cover_imaage);

                        if (result.getProfile_details().getFollowerStatus().equals("1")) {
                            following.setText("Following");
                            following.setTextColor(getResources().getColor(R.color.white));
                            follow_user.setBackground(getResources().getDrawable(R.drawable.login_bg));
                        } else if (result.getProfile_details().getFollowerStatus().equals("0")) {
                            following.setText("Follow");
                            following.setTextColor(getResources().getColor(R.color.login_bg));
                            follow_user.setBackground(getResources().getDrawable(R.drawable.stats_bg));
                        } else if (result.getProfile_details().getFollowerStatus().equals("2")) {
                            following.setText("Requested");
                            following.setTextColor(getResources().getColor(R.color.white));
                            follow_user.setBackground(getResources().getDrawable(R.drawable.login_bg));
                        }

                        if (!result.getProfile_details().getFollowerStatus().equals("1") && result.getProfile_details().getPrivacyStatus().equals("1")) {
                            another_follower_data.setVisibility(View.GONE);
                            another_stats.setVisibility(View.GONE);
                        } else {
                            another_follower_data.setVisibility(View.VISIBLE);
                            another_stats.setVisibility(View.VISIBLE);
                        }

                        display_name = result.getProfile_details().getDisplayName();
                        user_info.setBackground(getResources().getDrawable(R.drawable.login_bg));
                        userinfo_text.setTextColor(getResources().getColor(R.color.white));
                        userinfo_view.setImageResource(R.drawable.info_96);
                        user_photos.setBackground(getResources().getDrawable(R.drawable.stats_bg));
                        userphotos_text.setTextColor(getResources().getColor(R.color.login_bg));
                        userphoto_view.setImageResource(R.drawable.photos_purple_96);
                        setupViewPager(user_pager);
                    }
                } else {
                    working_layout.setVisibility(View.GONE);
                    progress_layout.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    Alerter.create(Another_user.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "fetch_user_profile Response : " + new Gson().toJson(response.errorBody()));
                }

            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                working_layout.setVisibility(View.GONE);
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
                Alerter.create(Another_user.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "fetch_user_profile Error : " + t.getMessage());
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
        if (v == user_info) {
            user_pager.setCurrentItem(0, true);
        }
        if (v == user_photos) {
            user_pager.setCurrentItem(1, true);
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Another_user.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Another_user.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Another_user.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Another_user.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Another_user.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Another_user.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Another_user.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Another_user.this, Stats.class);
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
            menu_open_layout.setVisibility(View.GONE);
            menu_click_view.setVisibility(View.VISIBLE);
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Another_user.this, Timeline.class);
            startActivity(i);
            transition();
        }
        if (v == back) {
            onBackPressed();
            Another_user.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
        }
        if (v == another_follower_data) {
            Intent i = new Intent(Another_user.this, Another_follower_following.class);
            i.putExtra("Profile_image", result.getProfile_details().getProfileImage());
            i.putExtra("Cover_image", result.getProfile_details().getCoverImage());
            i.putExtra("Display_name", result.getProfile_details().getDisplayName());
            startActivity(i);
        }
        if (v == another_stats) {
            Intent i = new Intent(Another_user.this, Stats.class);

            if (display_name.trim().contains(" ")) {
                i.putExtra("stats_header", display_name.substring(display_name.indexOf(" ")) + "'s Stats");
            } else {
                i.putExtra("stats_header", display_name + "'s Stats");
            }
            i.putExtra("stats_id", preferences.getString("another_user", ""));
            i.putExtra("layout", "1");
            startActivity(i);
            transition();
        }
        if (v == follow_user || v == following) {
            if (following.getText().equals("Follow")) {
                following.setVisibility(View.GONE);
                followProgress.setVisibility(View.VISIBLE);
                followProgress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.login_bg), PorterDuff.Mode.MULTIPLY);
                try {
                    follow_user();
                } catch (TimeoutException e) {
                    followProgress.setVisibility(View.GONE);
                    following.setVisibility(View.VISIBLE);
                    Alerter.create(Another_user.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    e.printStackTrace();
                }
            } else if (following.getText().equals("Requested")) {
                following.setVisibility(View.GONE);
                followProgress.setVisibility(View.VISIBLE);
                followProgress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                try {
                    follow_user();
                } catch (TimeoutException e) {
                    followProgress.setVisibility(View.GONE);
                    following.setVisibility(View.VISIBLE);
                    Alerter.create(Another_user.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    e.printStackTrace();
                }
            } else if (following.getText().equals("Following")) {
                following.setVisibility(View.GONE);
                followProgress.setVisibility(View.VISIBLE);
                followProgress.getIndeterminateDrawable().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.MULTIPLY);
                try {
                    Unfollow_user();
                } catch (TimeoutException e) {
                    followProgress.setVisibility(View.GONE);
                    following.setVisibility(View.VISIBLE);
                    Alerter.create(Another_user.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.d("API_", "Timeout Error : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
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


    @Override
    protected void onStop() {
        super.onStop();
        Log.d("Parkash", "hanji");
    }

    //unfollow user api
    private void Unfollow_user() throws TimeoutException {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "unfollow_user");
        postParams.put("userid", preferences.getString("user_id", ""));
        postParams.put("following_userid", preferences.getString("another_user", ""));

        Log.d("API_Parameters", " unfollow_user params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {

                if (response.isSuccessful()) {
                    Log.d("API_Response", "unfollow_user Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        followProgress.setVisibility(View.GONE);
                        following.setVisibility(View.VISIBLE);
                        if (response.body().getResult().getReason().equals("user unfollow successfully")) {
                            following.setText("Follow");
                            following.setTextColor(getResources().getColor(R.color.login_bg));
                            follow_user.setBackground(getResources().getDrawable(R.drawable.stats_bg));
                            result.getProfile_details().setFollowerStatus("0");
                            if (result.getProfile_details().getPrivacyStatus().equals("1"))
                            {
                                another_follower_data.setVisibility(View.GONE);
                                another_stats.setVisibility(View.GONE);
                            }
                            user_pager.setCurrentItem(0);
//                            if (user_pager.getCurrentItem()==1)
//                            {
//                                new My_photos().getInstance(result.getProfile_details(),"0");
//                            }
//                            try {
//
//                                fetch_profile();
//                            } catch (TimeoutException e) {
//                                Log.d("API_", "Timeout Error : " + e.getMessage());
//                                e.printStackTrace();
//                            }

                        } else {

                            Alerter.create(Another_user.this)
                                    .setText("Something went wrong. Please try again after sometime!!")
                                    .setBackgroundColor(R.color.red)
                                    .show();
                        }
                    } else {
                        Alerter.create(Another_user.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                        Log.e("API_Response", "unfollow_user Response : " + new Gson().toJson(response.errorBody()));
                    }

                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                followProgress.setVisibility(View.GONE);
                following.setVisibility(View.VISIBLE);
                Alerter.create(Another_user.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "unfollow_user Error : " + t.getMessage());
            }
        });
    }

    //follow user api
    private void follow_user() throws TimeoutException {

        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "follow_request");
        postParams.put("request_send_by", preferences.getString("user_id", ""));
        postParams.put("request_send_to", preferences.getString("another_user", ""));

        Log.d("API_Parameters", " follow_request params : " + postParams.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                followProgress.setVisibility(View.GONE);
                following.setVisibility(View.VISIBLE);
                if (response.isSuccessful()) {
                    String reason = response.body().getResult().getReason();

                    Log.d("API_Response", "follow_request Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        if (reason.equals("request deleted successfully"))
                        {
                            following.setText("Follow");
                            following.setTextColor(getResources().getColor(R.color.login_bg));
                            follow_user.setBackground(getResources().getDrawable(R.drawable.stats_bg));
                            result.getProfile_details().setFollowerStatus("0");
                            if (user_pager.getCurrentItem()==1)
                            {
                             new My_photos(false,"0").getInstance(result.getProfile_details(),"0");
                            }
                        }
                        else if (reason.equals("Following request send successfully"))
                        {
                            following.setText("Requested");
                            following.setTextColor(getResources().getColor(R.color.white));
                            follow_user.setBackground(getResources().getDrawable(R.drawable.login_bg));
                            result.getProfile_details().setFollowerStatus("2");
                            if (user_pager.getCurrentItem()==1)
                            {
                                new My_photos(false,"0").getInstance(result.getProfile_details(),"0");
                            }
                        }
                        else if (reason.equals("Following successfully"))
                        {
                            following.setText("Following");
                            following.setTextColor(getResources().getColor(R.color.white));
                            follow_user.setBackground(getResources().getDrawable(R.drawable.login_bg));
                            result.getProfile_details().setFollowerStatus("1");
                            if (user_pager.getCurrentItem()==1)
                            {
                                new My_photos(false,"0").getInstance(result.getProfile_details(),"0");
                            }
                        }
//                        try {
//                            fetch_profile();
//                        } catch (TimeoutException e) {
//                            e.printStackTrace();
//                            Log.d("API_", "Timeout Error : " + e.getMessage());
//                        }

                    } else {
                        if (reason.equals("request already send to this user")) {

                            AlertDialog.Builder alert = new AlertDialog.Builder(Another_user.this);
                            alert.setMessage("Request already send to this user.");
                            alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                        }
                    }
                } else {
                    Alerter.create(Another_user.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "follow_request Response : " + new Gson().toJson(response.errorBody()));
                }

            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                followProgress.setVisibility(View.GONE);
                following.setVisibility(View.VISIBLE);
                Alerter.create(Another_user.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "follow_request Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Another_user.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);

    }

    private void setupViewPager(ViewPager user_pager) {
        Another_user.ViewPagerAdapter report_view_adapter = new ViewPagerAdapter(getSupportFragmentManager());
        report_view_adapter.addFragment(new My_info(false));
        report_view_adapter.addFragment(new My_photos(false,"0"));
        user_pager.setAdapter(report_view_adapter);
    }

    //viewpager page change listener method
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            user_info.setBackground(getResources().getDrawable(R.drawable.login_bg));
            userinfo_text.setTextColor(getResources().getColor(R.color.white));
            userinfo_view.setImageResource(R.drawable.info_96);
            user_photos.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            userphotos_text.setTextColor(getResources().getColor(R.color.login_bg));
            userphoto_view.setImageResource(R.drawable.photos_purple_96);
        }
        if (position == 1) {
            user_photos.setBackground(getResources().getDrawable(R.drawable.login_bg));
            userphotos_text.setTextColor(getResources().getColor(R.color.white));
            userphoto_view.setImageResource(R.drawable.photos_96);
            user_info.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            userinfo_text.setTextColor(getResources().getColor(R.color.login_bg));
            userinfo_view.setImageResource(R.drawable.info_purple_96);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    //view pager adapter
    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
//            new Likes_fragment().getInstance(ArrayList);
                new My_info(false).getInstance(result.getProfile_details());
            }

            if (position == 1) {
                new My_photos(false,"0").getInstance(result.getProfile_details(), "0");
            }


            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

    }
}

