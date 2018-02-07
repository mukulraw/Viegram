package com.relinns.viegram.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.relinns.viegram.Fragment.Likes_fragment;
import com.relinns.viegram.Fragment.comment_fragment;
import com.relinns.viegram.Fragment.liked_commentFragment;
import com.relinns.viegram.Fragment.repost_fragment;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Post_points extends AppCompatActivity implements View.OnClickListener {
    private TabLayout tabLayout;
    private ViewPager viewPager;
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
    private int[] tabIcons = {R.drawable.like_96, R.drawable.comment_96, R.drawable.repost_96, R.drawable.commentlike};
    private String post_id;
    private SharedPreferences preferences;
    private TextView badgeText;
    private Result result;
    private ProgressBar progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_points);

        post_id = getIntent().getStringExtra("post_id");
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        progress = (ProgressBar) findViewById(R.id.progress);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        menu_click = (ImageView) findViewById(R.id.point_menu_click);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        menu_open = (RelativeLayout) findViewById(R.id.point_menu_open);
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
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

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
        progress_layout.setVisibility(View.VISIBLE);
        menu_open.setVisibility(View.GONE);
        get_points();
    }

    private void get_points() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "post_details");
        postParam.put("postid", post_id);
        postParam.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "post_details parameters :" + postParam.toString());
        Call<API_Response> call = service.FriendsWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "post_details Response : " + new Gson().toJson(response.errorBody()));

                if (response.isSuccessful()) {
                    result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        progress_layout.setVisibility(View.GONE);
                        setupViewPager(viewPager);
                        tabLayout.setupWithViewPager(viewPager);
                        setupTabIcons();
                    }
                    else {
                        progress_layout.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        Alerter.create(Post_points.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                } else {
                    progress_layout.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                   Alerter.create(Post_points.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "post_details Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
               Alerter.create(Post_points.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "post_details Error : " + t.getMessage());
            }
        });
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter report_view_adapter = new ViewPagerAdapter(getSupportFragmentManager());
        report_view_adapter.addFragment(new Likes_fragment(), result.getLikeCounter());
        report_view_adapter.addFragment(new comment_fragment(), result.getCommentCounter());
        report_view_adapter.addFragment(new repost_fragment(), result.getRepostCounter());
        report_view_adapter.addFragment(new liked_commentFragment(), result.getMycommentCounter());
        viewPager.setAdapter(report_view_adapter);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
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
            Intent i = new Intent(Post_points.this, Stats.class);
            i.putExtra("stats_header", "My stats");
            i.putExtra("stats_id", preferences.getString("user_id", ""));
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
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
            Intent i = new Intent(Post_points.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Post_points.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Post_points.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Post_points.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Post_points.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Post_points.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Post_points.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Post_points.this, Timeline.class);
            startActivity(i);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void menu_status() {
        menu_open.setVisibility(View.GONE);
        menu_click.setVisibility(View.VISIBLE);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> mFragmentList = new ArrayList<>();
        private final ArrayList<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                new Likes_fragment().getInstance(result);
            }
            if (position == 1) {
                new comment_fragment().getInstance(result);
            }
            if (position == 2) {
                new repost_fragment().getInstance(result);
            }
            if (position == 3) {
                new liked_commentFragment().getInstance(result);
            }
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

    }

}
