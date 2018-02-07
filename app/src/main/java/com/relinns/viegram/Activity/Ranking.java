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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.relinns.viegram.Fragment.Follower_ranking;
import com.relinns.viegram.Fragment.Leader;
import com.relinns.viegram.Fragment.My_info;
import com.relinns.viegram.Fragment.My_photos;
import com.relinns.viegram.Fragment.My_ranking;
import com.relinns.viegram.R;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class Ranking extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private RelativeLayout badgeLayout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout menu_home;
    private RelativeLayout leader;
    private RelativeLayout my_ranking;
    private RelativeLayout follower_ranking;
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
    private TextView leader_text;
    private TextView ranking_text;
    private TextView flwr_rnk_text;
    private TextView badgeText;
    private SharedPreferences preferences;
    private ViewPager mRankingPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        leader = (RelativeLayout) findViewById(R.id.leader);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        my_ranking = (RelativeLayout) findViewById(R.id.my_ranking);
        follower_ranking = (RelativeLayout) findViewById(R.id.follower_ranking);
        leader_text = (TextView) findViewById(R.id.leader_text);
        ranking_text = (TextView) findViewById(R.id.ranking_text);
        flwr_rnk_text = (TextView) findViewById(R.id.fllwr_text);
        menu_open_layout = (RelativeLayout) findViewById(R.id.ranking_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.ranking_menu_click);
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
        mRankingPager = (ViewPager) findViewById(R.id.ranking_pager);
        menu_open_layout.setVisibility(View.GONE);

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
        leader.setOnClickListener(this);
        activity_layout.setOnClickListener(this);
        mRankingPager.addOnPageChangeListener(this);
        my_ranking.setOnClickListener(this);
        follower_ranking.setOnClickListener(this);
        setUpViewPager(mRankingPager);
//        Fragment fragment_leader = new Leader();
//        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction3.replace(R.id.ranking_data, fragment_leader, null);
//        fragmentTransaction3.commit();
    }

    private void setUpViewPager(ViewPager mRankingPager) {
        Log.d("Fragment","setUp");
        Ranking.ViewPagerAdapter rankingViewAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        rankingViewAdapter.addFragment(new Leader());
        rankingViewAdapter.addFragment(new My_ranking());
        rankingViewAdapter.addFragment(new Follower_ranking());
        mRankingPager.setAdapter(rankingViewAdapter);
    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == leader) {
            callLeaderMethod();

        }
        if (v == my_ranking) {
            mRankingPager.setCurrentItem(1,true);
//            Fragment fragment_leader = new My_ranking();
//            FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction3.replace(R.id.ranking_data, fragment_leader, null);
//            fragmentTransaction3.commit();
        }
        if (v == follower_ranking) {
            mRankingPager.setCurrentItem(2,true);
//            Fragment fragment_leader = new Follower_ranking();
//            FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//            fragmentTransaction3.replace(R.id.ranking_data, fragment_leader, null);
//            fragmentTransaction3.commit();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Ranking.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Ranking.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Ranking.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Ranking.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            callLeaderMethod();
//            Intent i = new Intent(Ranking.this, Ranking.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Ranking.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Ranking.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Ranking.this, Stats.class);
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
        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Ranking.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    private void callLeaderMethod() {
        mRankingPager.setCurrentItem(0,true);

//
//        Fragment fragment_leader = new Leader();
//        FragmentTransaction fragmentTransaction3 = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction3.replace(R.id.ranking_data, fragment_leader, null);
//        fragmentTransaction3.commit();
    }

    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Ranking.this, Timeline.class);
        startActivity(i);
        Ranking.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        if (position==0)
        {
            leader.setBackground(getResources().getDrawable(R.drawable.login_bg));
            my_ranking.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            follower_ranking.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            leader_text.setTextColor(getResources().getColor(R.color.white));
            ranking_text.setTextColor(getResources().getColor(R.color.login_bg));
            flwr_rnk_text.setTextColor(getResources().getColor(R.color.login_bg));
        }
        if (position==1)
        {
            my_ranking.setBackground(getResources().getDrawable(R.drawable.login_bg));
            leader.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            follower_ranking.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            ranking_text.setTextColor(getResources().getColor(R.color.white));
            leader_text.setTextColor(getResources().getColor(R.color.login_bg));
            flwr_rnk_text.setTextColor(getResources().getColor(R.color.login_bg));
        }
        if (position==2)
        {
            follower_ranking.setBackground(getResources().getDrawable(R.drawable.login_bg));
            my_ranking.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            leader.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            flwr_rnk_text.setTextColor(getResources().getColor(R.color.white));
            ranking_text.setTextColor(getResources().getColor(R.color.login_bg));
            leader_text.setTextColor(getResources().getColor(R.color.login_bg));
        }

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager );

        }

        @Override
        public Fragment getItem(int position) {
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
