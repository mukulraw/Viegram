package com.relinns.viegram.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.relinns.viegram.Fragment.Earning_point;
import com.relinns.viegram.Fragment.My_stats;
import com.relinns.viegram.Fragment.Status_Score;
import com.relinns.viegram.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Stats extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private RelativeLayout badgeLayout;
    private RelativeLayout back;
    private RelativeLayout my_stats;
    private RelativeLayout status_scores;
    private RelativeLayout earning_points;
    private TextView stats_text;
    private TextView badgeText;
    private TextView score_text;
    private TextView earning_text;
    private TextView stats_header;
    private FrameLayout layout;
    private LinearLayout butn_layout;
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
    private String value;
    private String header_text;
    private String stats_id;
    private ViewPager mStatsPager;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        stats_header = (TextView) findViewById(R.id.stats_header);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        mStatsPager = (ViewPager) findViewById(R.id.stats_pager);
        my_stats = (RelativeLayout) findViewById(R.id.my_stats);
        status_scores = (RelativeLayout) findViewById(R.id.status_score);
        earning_points = (RelativeLayout) findViewById(R.id.earning_points);
        butn_layout = (LinearLayout) findViewById(R.id.layout);
        Intent intent = getIntent();
// Get the extras (if there are any)
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.containsKey("layout")) {
                value = getIntent().getStringExtra("layout");
                butn_layout.setVisibility(View.GONE);
            }
            header_text = getIntent().getStringExtra("stats_header");
            stats_id = getIntent().getStringExtra("stats_id");
            Log.d("Stats", "id :" + stats_id);
            Log.d("Stats", "header :" + header_text);

            stats_header.setText(header_text);
        }
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        layout = (FrameLayout) findViewById(R.id.show_stats);
        stats_text = (TextView) findViewById(R.id.stats_text);
        score_text = (TextView) findViewById(R.id.scores_text);
        earning_text = (TextView) findViewById(R.id.earning_text);
        menu_open_layout = (RelativeLayout) findViewById(R.id.stat_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.stat_menu_click);
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
        my_stats.setOnClickListener(this);
        status_scores.setOnClickListener(this);
        earning_points.setOnClickListener(this);
        mStatsPager.setOnPageChangeListener(this);
        setUpViewPager(mStatsPager);
//        Fragment stats = new My_stats(stats_id, header_text);
//        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction3.replace(R.id.show_stats, stats, null);
//        fragmentTransaction3.commit();
    }

    private void setUpViewPager(ViewPager mStatsPager) {
        StatsPagerAdapter pagerAdapter = new StatsPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new My_stats());
        pagerAdapter.addFragment(new Status_Score());
        pagerAdapter.addFragment(new Earning_point());
        mStatsPager.setAdapter(pagerAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == my_stats) {
            mStatsPager.setCurrentItem(0, true);
//            callMyStats();

        }
        if (v == status_scores) {
            mStatsPager.setCurrentItem(1, true);
//
//            Fragment stats = new Status_Score();
//            FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction3.replace(R.id.show_stats, stats, null);
//            fragmentTransaction3.commit();
        }
        if (v == earning_points) {
            mStatsPager.setCurrentItem(2, true);
//            Fragment stats = new Earning_point();
//            FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction3.replace(R.id.show_stats, stats, null);
//            fragmentTransaction3.commit();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Stats.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Stats.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Stats.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Stats.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Stats.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Stats.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Stats.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            mStatsPager.setCurrentItem(0, true);

        }
        if (v == menu_click_view) {
            try {
                SharedPreferences preferences;
                preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
                Log.d("badge_value", preferences.getInt("badge_value", 0) + "");
                if (preferences.getInt("badge_value", 0) != 0) {
                    badgeLayout.setVisibility(View.VISIBLE);
                    badgeText.setText(preferences.getInt("badge_value", 0) + "");
                } else {
                    badgeLayout.setVisibility(View.GONE);
                }
                menu_open_layout.setVisibility(View.VISIBLE);
                menu_click_view.setVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (v == menu_close) {
            menu_status();
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Stats.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    private void callMyStats() {
        my_stats.setBackground(getResources().getDrawable(R.drawable.login_bg));
        stats_text.setTextColor(getResources().getColor(R.color.white));
        score_text.setTextColor(getResources().getColor(R.color.login_bg));
        earning_text.setTextColor(getResources().getColor(R.color.login_bg));
        status_scores.setBackground(getResources().getDrawable(R.drawable.stats_bg));
        earning_points.setBackground(getResources().getDrawable(R.drawable.stats_bg));
//        Fragment stats = new My_stats(stats_id, header_text);
        Fragment stats = new My_stats();
        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
        fragmentTransaction3.replace(R.id.show_stats, stats, null);
        fragmentTransaction3.commit();
    }

    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        if (value == null) {
            super.onBackPressed();
            Stats.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
        } else if (!value.equals("")) {
            super.onBackPressed();
            Stats.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
        } else {
            Intent i = new Intent(Stats.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            my_stats.setBackground(getResources().getDrawable(R.drawable.login_bg));
            stats_text.setTextColor(getResources().getColor(R.color.white));
            score_text.setTextColor(getResources().getColor(R.color.login_bg));
            earning_text.setTextColor(getResources().getColor(R.color.login_bg));
            status_scores.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            earning_points.setBackground(getResources().getDrawable(R.drawable.stats_bg));
        }
        if (position == 1) {
            status_scores.setBackground(getResources().getDrawable(R.drawable.login_bg));
            score_text.setTextColor(getResources().getColor(R.color.white));
            stats_text.setTextColor(getResources().getColor(R.color.login_bg));
            earning_text.setTextColor(getResources().getColor(R.color.login_bg));
            my_stats.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            earning_points.setBackground(getResources().getDrawable(R.drawable.stats_bg));
        }
        if (position == 2) {
            earning_points.setBackground(getResources().getDrawable(R.drawable.login_bg));
            earning_text.setTextColor(getResources().getColor(R.color.white));
            score_text.setTextColor(getResources().getColor(R.color.login_bg));
            stats_text.setTextColor(getResources().getColor(R.color.login_bg));
            status_scores.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            my_stats.setBackground(getResources().getDrawable(R.drawable.stats_bg));
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private class StatsPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();

        public StatsPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                new My_stats().getInstance(stats_id, header_text);
            }
            return mFragmentList.get(position);
        }

        public void addFragment(Fragment fragment) {
            mFragmentList.add(fragment);
        }

        @Override
        public int getCount() {
            if (getIntent().getExtras() != null) {
                if (getIntent().getExtras().containsKey("layout"))
                    return 1;
                else
                    return mFragmentList.size();
            } else
                return mFragmentList.size();
        }

    }


}


