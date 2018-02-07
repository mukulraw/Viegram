package com.relinns.viegram.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by win 7 on 6/1/2017.
 */
public class Change_Password extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout badgeLayout;
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
    private TextView badgeText;
    private EditText current_change;
    private EditText new_change;
    private EditText confirm_change;
    private Button change_password;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;
    private static final String PASSWORD_PATTERN =
            "((?=.*[A-Z])(?=.*[@#$%_.]).{8,20})";
    private Pattern pattern;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        progressDialog = new ProgressDialog(this);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        menu_open_layout = (RelativeLayout) findViewById(R.id.change_menu_open);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        current_change = (EditText) findViewById(R.id.current_change);
        new_change = (EditText) findViewById(R.id.new_change);
        confirm_change = (EditText) findViewById(R.id.confirm_change);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);
        change_password = (Button) findViewById(R.id.change_password_bt);
        menu_click_view = (ImageView) findViewById(R.id.change_menu_click);

        menu_open_layout.setVisibility(View.GONE);
        menu_home.setOnClickListener(this);
        back.setOnClickListener(this);
        change_password.setOnClickListener(this);
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
        activity_layout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
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
            Intent i = new Intent(Change_Password.this, Timeline.class);
            startActivity(i);
            transition();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Change_Password.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Change_Password.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Change_Password.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Change_Password.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Change_Password.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Change_Password.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Change_Password.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Change_Password.this, Stats.class);
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
        if (v == change_password) {
            Matcher match = pattern.matcher(new_change.getText().toString());
            if (new_change.getText().toString().equals("") || confirm_change.getText().toString().equals("") || current_change.getText().toString().equals("")) {
                Alerter.create(Change_Password.this)
                        .setText("All Fields required.")
                        .setBackgroundColor(R.color.red)
                        .show();
            }
            if (!new_change.getText().toString().equals(confirm_change.getText().toString())) {
                Alerter.create(Change_Password.this)
                        .setText("Please enter same Password in New password and Confirm password field")
                        .setBackgroundColor(R.color.red)
                        .show();
            }
            else if (!match.matches()) {
                Alerter.create(Change_Password.this)
                        .setText(R.string.password_validation)
                        .setBackgroundColor(R.color.red)
                        .show();
            }
            else {
                progressDialog.show();
                change_password();
            }
        }
    }

    //transition animation
    private void transition()
    {
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    // opened menu visibility gone
    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    //change password api
    private void change_password() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "change_password");
        postParams.put("email", preferences.getString("email_id", ""));
        postParams.put("old_password", current_change.getText().toString());
        postParams.put("new_password", new_change.getText().toString());

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Log.d("API_Parameters", "change_password parameters :" + postParams.toString());

        Call<API_Response> call = service.accountWork(postParams);
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("API_Response", "change_password Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Change_Password.this);
                        alert.setMessage("Password updated Successfully.");
                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = new Intent(Change_Password.this, Settings.class);
                                startActivity(i);
                                transition();

                            }
                        }).show();
                    } else {
                        Alerter.create(Change_Password.this)
                                .setText("Current password does not match")
                                .setBackgroundColor(R.color.red)
                                .show();
//                        current_change.setError("Current password does not match");
                    }

                } else
                {
                     Alerter.create(Change_Password.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "change_password Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progressDialog.dismiss();
                Alerter.create(Change_Password.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "change_password Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Change_Password.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    //password validation
    public Change_Password() {
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }
}
