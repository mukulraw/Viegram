package com.relinns.viegram.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.relinns.viegram.Adapter.Follow_people_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.CommentPost;
import com.relinns.viegram.Pojo.SettingDetails;
import com.relinns.viegram.Pojo.UserData;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;

public class Settings extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout logout;
    private LinearLayout change_password;
    private LinearLayout viegram_works;
    private LinearLayout terms_service;
    private LinearLayout icon_guide;
    private LinearLayout edit_profile;
    private LinearLayout invite_friends;
    private RelativeLayout badgeLayout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout support;
    private RelativeLayout follow_people_button;
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
    private Switch private_account;
    private CheckBox like_box;
    private CheckBox comment_box;
    private CheckBox tag_box;
    private CheckBox follow_request_box;
    private CheckBox repost_box;
    private String privacy = "0";
    private String likes = "0";
    private String comments = "0";
    private String tags = "0";
    private String reposts = "0";
    private String follow_requests = "0";
    private String reference_link;
    private ProgressDialog progress_Dialog;
    private SharedPreferences preferences;
    private TextView badgeText;
    public static List<CommentPost> followPeople;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        followPeople = new ArrayList<>();
        progress_Dialog = new ProgressDialog(this);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        support = (RelativeLayout) findViewById(R.id.support);
        invite_friends = (LinearLayout) findViewById(R.id.invite_friends);
        private_account = (Switch) findViewById(R.id.private_account);
        like_box = (CheckBox) findViewById(R.id.like_box);
        comment_box = (CheckBox) findViewById(R.id.comment_box);
        tag_box = (CheckBox) findViewById(R.id.tag_box);
        follow_request_box = (CheckBox) findViewById(R.id.follow_request_box);
        repost_box = (CheckBox) findViewById(R.id.repost_box);
        edit_profile = (LinearLayout) findViewById(R.id.edit_profile);
        terms_service = (LinearLayout) findViewById(R.id.terms_service);
        icon_guide = (LinearLayout) findViewById(R.id.icon_guide);
        logout = (LinearLayout) findViewById(R.id.logout);
        back = (RelativeLayout) findViewById(R.id.back);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        change_password = (LinearLayout) findViewById(R.id.change_password);
        viegram_works = (LinearLayout) findViewById(R.id.viegram_works);
        follow_people_button = (RelativeLayout) findViewById(R.id.follow_people_button);
        menu_open_layout = (RelativeLayout) findViewById(R.id.settings_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.settings_menu_click);
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
        support.setOnClickListener(this);
        logout.setOnClickListener(this);
        activity_layout.setOnClickListener(this);
        edit_profile.setOnClickListener(this);
        menu_home.setOnClickListener(this);
        back.setOnClickListener(this);
        icon_guide.setOnClickListener(this);
        terms_service.setOnClickListener(this);
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
        follow_people_button.setOnClickListener(this);
        change_password.setOnClickListener(this);
        viegram_works.setOnClickListener(this);
        invite_friends.setOnClickListener(this);
        comment_box.setOnCheckedChangeListener(this);
        follow_request_box.setOnCheckedChangeListener(this);
        like_box.setOnCheckedChangeListener(this);
        repost_box.setOnCheckedChangeListener(this);
        tag_box.setOnCheckedChangeListener(this);
        private_account.setOnCheckedChangeListener(this);

        if (Timeline.resultp != null) {
            reference_link = Timeline.resultp.getReferenceLink();
            setSettings(Timeline.resultp.getSettingDetails());
        } else {
            progress_Dialog.show();
            progress_Dialog.setCancelable(false);
            get_settings();
        }
        getFollowData();
    }

    public void getFollowData() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "all_user_list");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "all_user_list parameters :" + postParams.toString());
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "all_user_list Response : " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    if (response.body().getResult().getMsg().equals("201")) {
                        followPeople = response.body().getResult().getUserDetails();

                    } else {

                    }
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {

            }
        });
    }

    private void get_settings() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "get_settings");
        postParam.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "get_settings parameters :" + postParam.toString());
        Call<UserData> call = service.Settings(postParam);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.e("API_Response", "get_settings Response : " + new Gson().toJson(response.body()));

                    if (response.body().getResult().getMsg().equals("201")) {
                        reference_link = response.body().getResult().getReferenceLink();
                        if (Timeline.resultp != null)
                            Timeline.resultp.setSettingDetails(response.body().getResult().getSettingDetails());
                        setSettings(response.body().getResult().getSettingDetails());

                    } else Alerter.create(Settings.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                } else {
                    Alerter.create(Settings.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "get_settings Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                progress_Dialog.dismiss();
                Alerter.create(Settings.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "logout Error : " + t.getMessage());
            }
        });
    }

    private void setSettings(SettingDetails settingDetails) {

        if (settingDetails.getPrivacy().equals("1")) {
            privacy = "1";
            private_account.setChecked(true);
        } else {
            privacy = "0";
            private_account.setChecked(false);
        }
        if (settingDetails.getLikes().equals("1")) {
            likes = "1";
            like_box.setChecked(true);
        } else {
            likes = "0";
            like_box.setChecked(false);
        }
        if (settingDetails.getComments().equals("1")) {
            comments = "1";
            comment_box.setChecked(true);
        } else {
            comments = "0";
            comment_box.setChecked(false);
        }
        if (settingDetails.getTags().equals("1")) {
            tags = "1";
            tag_box.setChecked(true);
        } else {
            tags = "0";
            tag_box.setChecked(false);
        }
        if (settingDetails.getRepost().equals("1")) {
            reposts = "1";
            repost_box.setChecked(true);
        } else {
            reposts = "0";
            repost_box.setChecked(false);
        }
        if (settingDetails.getFollowRequest().equals("1")) {
            follow_requests = "1";
            follow_request_box.setChecked(true);
        } else {
            follow_requests = "0";
            follow_request_box.setChecked(false);
        }
    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == support) {
            sendEmail();
        }
        if (v == invite_friends) {
            String message = getResources().getString(R.string.invite_text) + " " + reference_link;
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("text/plain");
            share.putExtra(Intent.EXTRA_TEXT, message);
            startActivity(Intent.createChooser(share, "Invite friends from:"));
        }
        if (v == logout) {
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setMessage("Are you sure you want to logout?");
            alert.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    progress_Dialog.show();
                    logout();
                }
            }).setPositiveButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        }
        if (v == back) {
            //add_settings();
            onBackPressed();


        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Settings.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == icon_guide) {
            Intent i = new Intent(Settings.this, Options.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == terms_service) {
            Intent i = new Intent(Settings.this, Terms_service.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == edit_profile) {
            Intent i = new Intent(Settings.this, Edit_Profile.class);
            i.putExtra("Activity", "settings");
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == change_password) {
            Intent i = new Intent(Settings.this, Change_Password.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }

        if (v == follow_people_button) {
            Intent i = new Intent(Settings.this, Follow_people.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == viegram_works) {
            Intent i = new Intent(Settings.this, Viegram_works.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Settings.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Settings.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Settings.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Settings.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Settings.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Settings.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            get_settings();
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Settings.this, Stats.class);
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
    }

    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    private void add_settings() {

        progress_Dialog.show();
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "add_settings");
        postParam.put("userid", preferences.getString("user_id", ""));
        postParam.put("privacy", privacy);
        postParam.put("likes", likes);
        postParam.put("comments", comments);
        postParam.put("tags", tags);
        postParam.put("repost", reposts);
        postParam.put("follow_request", follow_requests);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "add_settings parameters :" + postParam.toString());
        Call<API_Response> call = service.rankingRelated(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {
                    Timeline.resultp.getSettingDetails().setComments(response.body().getResult().getSettingDetails().getComments());
                    Timeline.resultp.getSettingDetails().setFollowRequest(response.body().getResult().getSettingDetails().getFollowRequest());
                    Timeline.resultp.getSettingDetails().setLikes(response.body().getResult().getSettingDetails().getLikes());
                    Timeline.resultp.getSettingDetails().setPrivacy(response.body().getResult().getSettingDetails().getPrivacy());
                    Timeline.resultp.getSettingDetails().setRepost(response.body().getResult().getSettingDetails().getRepost());
                    Timeline.resultp.getSettingDetails().setTags(response.body().getResult().getSettingDetails().getTags());

                    Log.e("API_Response", "add_settings Response : " + new Gson().toJson(response.body()));

                } else {
                    Toast.makeText(Settings.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
                    Log.e("API_Response", "add_settings Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_Dialog.dismiss();
                Log.d("API_Error", "search_user Error : " + t.getMessage());
                Toast.makeText(Settings.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void logout() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "logout");
        postParam.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "logout parameters :" + postParam.toString());
        Call<API_Response> call = service.accountWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.e("API_Response", "logout Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.commit();
                        ShortcutBadger.removeCount(Settings.this);
                        Intent i = new Intent(Settings.this, Login_Screen.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                } else
                    Log.e("API_Response", "logout Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_Dialog.dismiss();
                Log.d("API_Error", "logout Error : " + t.getMessage());
                Toast.makeText(Settings.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        //add_settings();
        Intent i = new Intent(Settings.this, Timeline.class);
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

        if (compoundButton.isPressed()) {
            if (compoundButton == comment_box) {
                if (b) {
                    comments = "1";


                } else {
                    comments = "0";

                }
            }
            if (compoundButton == follow_request_box) {
                if (b) {
                    follow_requests = "1";
                } else
                    follow_requests = "0";
            }
            if (compoundButton == like_box) {
                if (b) {
                    likes = "1";
                } else
                    likes = "0";
            }
            if (compoundButton == repost_box) {
                if (b) {
                    reposts = "1";
                } else
                    reposts = "0";
            }
            if (compoundButton == tag_box) {
                if (b) {
                    tags = "1";
                } else
                    tags = "0";
            }
            if (compoundButton == private_account) {
                if (b) {
                    privacy = "1";
                } else
                    privacy = "0";
            }
            add_settings();
        }

    }

    private void sendEmail() {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setData(Uri.parse("mailto:"));
        i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"teamviegram@gmail.com"});
        i.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
        i.putExtra(android.content.Intent.EXTRA_TEXT, "");
        i.setType("message/rfc822");
        final PackageManager pm = Settings.this.getPackageManager();
        final List<ResolveInfo> matches = pm.queryIntentActivities(i, 0);
        ResolveInfo best = null;
        for (final ResolveInfo info : matches)
            if (info.activityInfo.packageName.endsWith(".gm") || info.activityInfo.name.toLowerCase().contains("gmail"))
                best = info;
        if (best != null)
            i.setClassName(best.activityInfo.packageName, best.activityInfo.name);
        startActivity(i);
    }
}
