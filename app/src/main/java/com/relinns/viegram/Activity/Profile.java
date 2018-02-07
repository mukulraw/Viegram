package com.relinns.viegram.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
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
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.relinns.viegram.Fragment.My_info;
import com.relinns.viegram.Fragment.My_photos;
import com.relinns.viegram.Modal.Detail;
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
public class Profile extends AppCompatActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private RelativeLayout badgeLayout, progress_layout, back, info, photos, menu_home, menu_open_layout, menu_close, menu_profile, menu_stat, menu_follow, menu_notifications, menu_settings, menu_search, menu_ranking, menu_camera, edit;
    private ImageView menu_click_view, profile_image, profile_cover_image, info_view, photo_view;
    private TextView info_text, photos_text, my_name, badgeText;
    private ProgressBar progress, coverProgress, imageProgress;
    private ViewPager profilepager;
    private LinearLayout working_layout;
    private SharedPreferences preferences;
    private CoordinatorLayout activity_layout;
    private int index = 1;
    private boolean mUpdateCheck;
    Detail detail;
    ViewPagerAdapter report_view_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        inIt();
        fetchData();
    }

    private void fetchData() {
        if (Timeline.resultp != null) {
            working_layout.setVisibility(View.VISIBLE);
            progress_layout.setVisibility(View.GONE);
            if (!preferences.getString("Update", "").equals(Timeline.resultp.getPhotosDetails().getTotalPosts())) {
                mUpdateCheck = true;
                try {
                    fetch_profile();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            } else
                setProfileData(Timeline.resultp.getDisplayName(), Timeline.resultp.getProfileImage(), Timeline.resultp.getCoverImage(), Timeline.resultp.getPhotosDetails().getTotalPosts());
        } else {
            working_layout.setVisibility(View.GONE);
            progress_layout.setVisibility(View.VISIBLE);
            mUpdateCheck = true;
            try {
                fetch_profile();
            } catch (TimeoutException e) {
                progress_layout.setVisibility(View.GONE);
                working_layout.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                Alerter.create(Profile.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_", "Timeout Error : " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private void inIt() {

        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        coverProgress = (ProgressBar) findViewById(R.id.cover_progress);
        imageProgress = (ProgressBar) findViewById(R.id.profile_progress);
        working_layout = (LinearLayout) findViewById(R.id.working_layout);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        profilepager = (ViewPager) findViewById(R.id.profile_pager);
        activity_layout = (CoordinatorLayout) findViewById(R.id.activity_layout);
        profile_cover_image = (ImageView) findViewById(R.id.profile_cover_image);
        my_name = (TextView) findViewById(R.id.my_name);
        profile_image = (ImageView) findViewById(R.id.profile_image);
        edit = (RelativeLayout) findViewById(R.id.edit);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        info = (RelativeLayout) findViewById(R.id.info);
        photos = (RelativeLayout) findViewById(R.id.photos);
        info_text = (TextView) findViewById(R.id.info_text);
        photos_text = (TextView) findViewById(R.id.photos_text);
        info_view = (ImageView) findViewById(R.id.info_view);
        photo_view = (ImageView) findViewById(R.id.photo_view);
        menu_open_layout = (RelativeLayout) findViewById(R.id.profile_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.profile_menu_click);
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

        setupViewPager(profilepager);
        profilepager.setOffscreenPageLimit(2);

        info.setBackground(getResources().getDrawable(R.drawable.login_bg));
        info_text.setTextColor(getResources().getColor(R.color.white));
        info_view.setImageResource(R.drawable.info_96);
        photos.setBackground(getResources().getDrawable(R.drawable.stats_bg));
        photos_text.setTextColor(getResources().getColor(R.color.login_bg));
        photo_view.setImageResource(R.drawable.photos_purple_96);

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
        back.setOnClickListener(this);
        menu_home.setOnClickListener(this);
        edit.setOnClickListener(this);
        info.setOnClickListener(this);
        photos.setOnClickListener(this);
        profilepager.addOnPageChangeListener(this);

    }

    private void setupViewPager(ViewPager profilepager) {
        report_view_adapter = new ViewPagerAdapter(getSupportFragmentManager());
        report_view_adapter.addFragment(new My_info(true));
        report_view_adapter.addFragment(new My_photos(true,"1"));
        profilepager.setAdapter(report_view_adapter);
    }

    private void fetch_profile() throws TimeoutException {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "fetch_profile");
        postParams.put("user_id", preferences.getString("user_id", ""));
        postParams.put("page", "" + index);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "fetch_profile parameters :" + postParams.toString());
        Call<UserData> call = service.FetchProfile(postParams);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                progress_layout.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Log.d("API_Response", "fetch_profile Response : " + new Gson().toJson(response.body()));

                    if (response.body().getResult().getMsg().equals("201")) {
                        detail = response.body().getResult().getProfile_details();
                        setProfileData(detail.getDisplayName(), detail.getProfileImage(), detail.getCoverImage(), detail.getTotalPosts());
                    } else {
                        progress_layout.setVisibility(View.VISIBLE);
                        working_layout.setVisibility(View.GONE);
                        progress.setVisibility(View.GONE);
                        Alerter.create(Profile.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.red)
                                .show();
                    }
                } else {
                    progress_layout.setVisibility(View.VISIBLE);
                    working_layout.setVisibility(View.GONE);
                    progress.setVisibility(View.GONE);
                    Log.e("API_Response", "fetch_profile Response : " + new Gson().toJson(response.errorBody()));
                    Alerter.create(Profile.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                working_layout.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                Alerter.create(Profile.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "fetch_profile Error : " + t.getMessage());
            }
        });
    }

    private void setProfileData(String displayName, String profileImage, String coverImage, String totalPosts) {

        working_layout.setVisibility(View.VISIBLE);
        my_name.setText(displayName);

        photos_text.setText("photos (" + totalPosts + ")");

        Glide.with(Profile.this).load(profileImage)
                .bitmapTransform(new CropCircleTransformation(Profile.this))
            //    .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        imageProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(profile_image);

        Glide.with(Profile.this).load(coverImage)
             //   .thumbnail(0.01f)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        coverProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(profile_cover_image);


        if (mUpdateCheck) {
            ((My_info) report_view_adapter.mFragmentList.get(0)).updateViewCheck(getData());

            Gson gson = new Gson();
            String json = gson.toJson(getData());

            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("display_name", detail.getDisplayName());
            edit.putString("bio_data", detail.getBioData());
            edit.putString("link", detail.getLink());
            edit.putString("cover_image", detail.getCoverImage());
            edit.putString("profile_image", detail.getProfileImage());
            edit.putString("full_name", detail.getFullName());
            edit.putString("Update", detail.getTotalPosts());
            edit.putString("detail", json);
            edit.commit();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == info) {
            profilepager.setCurrentItem(0, true);
        }

        if (v == photos) {
            profilepager.setCurrentItem(1, true);
        }
        if (v == menu_camera) {
            Intent i = new Intent(Profile.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Profile.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Profile.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
//            try {
//                fetch_profile();
//            } catch (TimeoutException e) {
//                progress_layout.setVisibility(View.VISIBLE);
//                working_layout.setVisibility(View.GONE);
//                progress.setVisibility(View.GONE);
//                Alerter.create(Profile.this)
//                        .setText(R.string.network_error)
//                        .setBackgroundColor(R.color.red)
//                        .show();
//                Log.d("API_", "Timeout Error : " + e.getMessage());
//                e.printStackTrace();
//            }
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Profile.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Profile.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Profile.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Profile.this, Stats.class);
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
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Profile.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == edit) {
            Intent i = new Intent(Profile.this, Edit_Profile.class);
            i.putExtra("Activity", "profile");
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Profile.this, Timeline.class);
        startActivity(i);
        Profile.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int position) {
        if (position == 0) {
            if (mUpdateCheck)
                ((My_info) report_view_adapter.mFragmentList.get(0)).updateViewCheck(getData());
            info.setBackground(getResources().getDrawable(R.drawable.login_bg));
            info_text.setTextColor(getResources().getColor(R.color.white));
            info_view.setImageResource(R.drawable.info_96);
            photos.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            photos_text.setTextColor(getResources().getColor(R.color.login_bg));
            photo_view.setImageResource(R.drawable.photos_purple_96);
        }
        if (position == 1) {
            if (mUpdateCheck)
                ((My_photos) report_view_adapter.mFragmentList.get(1)).updateViewCheck(getData(), "1");
            photos.setBackground(getResources().getDrawable(R.drawable.login_bg));
            photos_text.setTextColor(getResources().getColor(R.color.white));
            photo_view.setImageResource(R.drawable.photos_96);
            info.setBackground(getResources().getDrawable(R.drawable.stats_bg));
            info_text.setTextColor(getResources().getColor(R.color.login_bg));
            info_view.setImageResource(R.drawable.info_purple_96);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    public class ViewPagerAdapter extends FragmentPagerAdapter {
        public final List<Fragment> mFragmentList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);
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

    Detail getData() {
        if (detail == null) {
            detail = new Detail();
            detail.setBioData(Timeline.resultp.getBioData());
            detail.setCoverImage(Timeline.resultp.getCoverImage());
            detail.setDisplayName(Timeline.resultp.getDisplayName());
            detail.setEmail(Timeline.resultp.getEmail());
            detail.setFullName(Timeline.resultp.getFullName());
            detail.setLink(Timeline.resultp.getLink());
            detail.setTotalPosts(Timeline.resultp.getPhotosDetails().getTotalPosts());
            detail.setPosts(Timeline.resultp.getPhotosDetails().getPosts());
            detail.setProfileImage(Timeline.resultp.getProfileImage());
            detail.setScorepoint(Timeline.resultp.getTotalOverallPoints());
        }
        return detail;
    }
}
