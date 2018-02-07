package com.relinns.viegram.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.relinns.viegram.R;

public class Viegram_works extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout back;
    private RelativeLayout badgeLayout;
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
    private WebView webView;
    private String url="http://www.viegram.com/viegram_pages/viegram_work.html";
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viegram_works);

        preferences=getSharedPreferences("Viegram",MODE_PRIVATE);
        activity_layout=(RelativeLayout)findViewById(R.id.activity_layout);
        webView=(WebView)findViewById(R.id.web);
        progress=(ProgressBar)findViewById(R.id.progress);
        back= (RelativeLayout) findViewById(R.id.back);
        menu_home=(RelativeLayout)findViewById(R.id.menu_home);
        menu_open_layout = (RelativeLayout) findViewById(R.id.viegram_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.viegram_menu_click);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        badgeLayout=(RelativeLayout)findViewById(R.id.badge_layout);
        badgeText=(TextView)findViewById(R.id.badge_text);

        menu_open_layout.setVisibility(View.GONE);

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
        webView.loadUrl(url);
        activity_layout.setOnClickListener(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient()
        {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progress.setVisibility(View.VISIBLE);
                progress.setProgress(newProgress);
                if(newProgress==100)
                {
                    progress.setVisibility(View.GONE);
                }
            }
        } );
    }


    @Override
    public void onBackPressed() {
       super.onBackPressed();
        Viegram_works.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onClick(View v) {
        if (v==activity_layout)
        {
            if (menu_open_layout.getVisibility()==View.VISIBLE)
            {
                menu_status();
            }
        }
        if(v==back)
        {
            onBackPressed();
        }
        if(v==menu_home)
        {
            menu_status();
            Intent i= new Intent(Viegram_works.this,Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Viegram_works.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Viegram_works.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Viegram_works.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Viegram_works.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Viegram_works.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Viegram_works.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Viegram_works.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Viegram_works.this, Stats.class);
            i.putExtra("stats_header","My stats");
            i.putExtra("stats_id",preferences.getString("user_id",""));
            startActivity(i);
            overridePendingTransition(R.anim.enter,R.anim.exit);

        }
        if (v == menu_click_view) {

            if (preferences.getInt("badge_value",0)!=0)
            {
                badgeLayout.setVisibility(View.VISIBLE);
                badgeText.setText(preferences.getInt("badge_value",0)+"");
            }
            else
            {
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
