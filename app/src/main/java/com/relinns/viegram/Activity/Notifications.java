package com.relinns.viegram.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.relinns.viegram.Fragment.NotificationFragment;
import com.relinns.viegram.Fragment.RequestFragment;
import com.relinns.viegram.R;
import java.util.ArrayList;
import java.util.List;
import me.leolin.shortcutbadger.ShortcutBadger;

@SuppressWarnings("ALL")
public class Notifications extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {
    private RelativeLayout request_button;
    private RelativeLayout notification_button;
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
    private SharedPreferences preferences;
    private TextView notification_text;
    private TextView request_text;
    private ViewPager notificationPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ShortcutBadger.removeCount(this);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putInt("badge_value",0);
        editor.commit();

        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        request_button = (RelativeLayout) findViewById(R.id.request_button);
        notification_button = (RelativeLayout) findViewById(R.id.notification_button);
        notificationPager = (ViewPager) findViewById(R.id.notification_pager);
        menu_open_layout = (RelativeLayout) findViewById(R.id.notification_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.notification_menu_click);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        notification_text = (TextView) findViewById(R.id.notification_text);
        request_text = (TextView) findViewById(R.id.request_text);

        menu_open_layout.setVisibility(View.GONE);

        request_button.setOnClickListener(this);
        notification_button.setOnClickListener(this);
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
        setUpViewPager(notificationPager);
        notificationPager.addOnPageChangeListener(this);
    }

    private void setUpViewPager(ViewPager notificationPager) {
        NotificationPagerAdapter adapter = new NotificationPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new RequestFragment());
        adapter.addFragment(new NotificationFragment());
        notificationPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Notifications.this, Timeline.class);
        startActivity(i);
        Notifications.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onClick(View v) {
        if (v == request_button) {
            notificationPager.setCurrentItem(0);
        }
        if (v == notification_button) {
            notificationPager.setCurrentItem(1);
        }
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
            Intent i = new Intent(Notifications.this, Timeline.class);
            startActivity(i);
            transition();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Notifications.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Notifications.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            notificationPager.setCurrentItem(0);
//            Intent i = new Intent(Notifications.this, Notifications.class);
//            startActivity(i);
//            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Notifications.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Notifications.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Notifications.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Notifications.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Notifications.this, Stats.class);
            i.putExtra("stats_header", "My stats");
            i.putExtra("stats_id", preferences.getString("user_id", ""));
            startActivity(i);
            transition();
        }
        if (v == menu_click_view) {
            menu_open_layout.setVisibility(View.VISIBLE);
            menu_click_view.setVisibility(View.GONE);
        }
        if (v == menu_close) {
            menu_status();
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

    //viewpager listener
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            request_button.setBackground(getResources().getDrawable(R.drawable.login_bg));
            request_text.setTextColor(getResources().getColor(R.color.white));
            notification_text.setTextColor(getResources().getColor(R.color.login_bg));
            notification_button.setBackground(getResources().getDrawable(R.drawable.stats_bg));
        } else {
            notification_button.setBackground(getResources().getDrawable(R.drawable.login_bg));
            notification_text.setTextColor(getResources().getColor(R.color.white));
            request_text.setTextColor(getResources().getColor(R.color.login_bg));
            request_button.setBackground(getResources().getDrawable(R.drawable.stats_bg));
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

//viewpager adapter
    private class NotificationPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentlist = new ArrayList<>();

        public NotificationPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            return fragmentlist.get(position);
        }

        public void addFragment(Fragment notificationFragment) {
            fragmentlist.add(notificationFragment);
        }

        @Override
        public int getCount() {
            return fragmentlist.size();
        }
    }
}
