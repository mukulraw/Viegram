package com.relinns.viegram.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.relinns.viegram.Adapter.Comment_Adapter;
import com.relinns.viegram.Adapter.Search_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.CommentPost;
import com.relinns.viegram.Modal.Result;
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

public class Comments extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView comment_list;
    private Comment_Adapter adapter;
    private RelativeLayout send;
    private RelativeLayout badgeLayout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout no_comment_found;
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
    private MultiAutoCompleteTextView write_comment;
    private ProgressBar progress;
    private ProgressDialog progress_Dialog;
    private SharedPreferences preferences;
    private TextView badgeText;
    private List<CommentPost> name_data;
    private String comment = "";
    private String mentionID = "";
    private List<String> id_list;
    private List<String> nameList;
    private Result result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);

        name_data = new ArrayList<>();
        id_list = new ArrayList<>();
        nameList = new ArrayList<>();
        progress_Dialog = new ProgressDialog(this);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        //notification badge code
        if (getIntent().getExtras()!=null)
        {
            if (getIntent().getExtras().containsKey("decrement"))
            {
                int badge_value=preferences.getInt("badge_value",0);
                badge_value--;
                SharedPreferences.Editor editor=preferences.edit();
                editor.putInt("badge_value",badge_value);
                editor.commit();
                ShortcutBadger.applyCount(getApplicationContext(), badge_value);

            }
        }
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        progress = (ProgressBar) findViewById(R.id.progress);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        comment_list = (RecyclerView) findViewById(R.id.comment_list);
        send = (RelativeLayout) findViewById(R.id.send);
        back = (RelativeLayout) findViewById(R.id.back);
        write_comment = (MultiAutoCompleteTextView) findViewById(R.id.write_comment);
        no_comment_found = (RelativeLayout) findViewById(R.id.no_comment_found);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        menu_open_layout = (RelativeLayout) findViewById(R.id.comment_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.comment_menu_click);
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

        back.setOnClickListener(this);
        menu_follow.setOnClickListener(this);
        menu_home.setOnClickListener(this);
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
        send.setOnClickListener(this);
        activity_layout.setOnClickListener(this);

        no_comment_found.setVisibility(View.GONE);
        menu_open_layout.setVisibility(View.GONE);
        progress.setVisibility(View.VISIBLE);
        progress_layout.setVisibility(View.VISIBLE);
        comment_list.setVisibility(View.GONE);
        get_comments();

        //multiautocomplete view set tokenizer for mention people
        write_comment.setTokenizer(new MultiAutoCompleteTextView.Tokenizer() {
            @Override
            public CharSequence terminateToken(CharSequence text) {
                int i = text.length();

                while (i > 0 && text.charAt(i - 1) == ' ') {
                    i--;
                }

                if (i > 0 && text.charAt(i - 1) == ' ') {
                    return text;
                } else {
                    if (text instanceof Spanned) {
                        SpannableString sp = new SpannableString(text + " ");
                        TextUtils.copySpansFrom((Spanned) text, 0, text.length(), Object.class, sp, 0);
                        return sp;
                    } else {
                        return text + " ";
                    }
                }
            }

            @Override
            public int findTokenStart(CharSequence text, int cursor) {
                int i = cursor;
                while (i > 0 && text.charAt(i - 1) != '@') {
                    i--;
                }

                //Check if token really started with @, else we don't have a valid token
                if (i < 1 || text.charAt(i - 1) != '@') {
                    comment = comment + "@";
                    return cursor;
                } else {
                    comment = write_comment.getText().toString();
                }
                return i;
            }

            @Override
            public int findTokenEnd(CharSequence text, int cursor) {
                int i = cursor;
                int len = text.length();

                while (i < len) {
                    if (text.charAt(i) == ' ') {
                        return i;
                    } else {
                        i++;
                    }
                }

                return len;
            }
        });
        write_comment.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                comment = write_comment.getText().toString();
                comment = comment.substring(0, comment.lastIndexOf("@c"));
                Log.d("Tag", "Comment data : " + comment);
                write_comment.setText(comment + "@" + name_data.get(i).getDisplayName());
                write_comment.setSelection(write_comment.getText().length());
                id_list.add(name_data.get(i).getUserid());
                nameList.add(name_data.get(i).getDisplayName());
            }
        });
    }

    //get post comments
    public void get_comments() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "fetch_comments");
        postParam.put("userid", preferences.getString("user_id", ""));
        postParam.put("postid", preferences.getString("post_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "fetch_comments parameters :" + postParam.toString());
        Call<API_Response> call = service.getPostActionResponse(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "fetch_comments Response : " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    get_following();
                    if (response.body().getResult().getMsg().equals("201")) {
                        progress_layout.setVisibility(View.GONE);
                        no_comment_found.setVisibility(View.GONE);
                        comment_list.setVisibility(View.VISIBLE);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Comments.this);
                        comment_list.setLayoutManager(mLayoutManager);
                        adapter = new Comment_Adapter(Comments.this, response.body().getResult().getComments());
                        comment_list.setAdapter(adapter);
                    } else if (response.body().getResult().getMsg().equals("204")){
                        progress_layout.setVisibility(View.GONE);
                        no_comment_found.setVisibility(View.VISIBLE);
                        comment_list.setVisibility(View.GONE);
                    }
                    else
                    {
                        Alerter.create(Comments.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                } else
                {
                    Alerter.create(Comments.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "fetch_comments Response : " + new Gson().toJson(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                no_comment_found.setVisibility(View.GONE);
                comment_list.setVisibility(View.GONE);
                progress.setVisibility(View.GONE);
                Alerter.create(Comments.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "fetch_comments Error : " + t.getMessage());
            }
        });
    }

    //get friend list
    private void get_following() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "following_list");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Log.d("API_Parameters", "following_list parameters :" + postParams.toString());

        Call<API_Response> call = service.FriendsWork(postParams);
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {

                if (response.isSuccessful()) {
                    Log.e("API_Response", "following_list Response : " + new Gson().toJson(response.body()));

                    result = response.body().getResult();

                    if (result.getMsg().equals("201")) {

                        name_data = new ArrayList<CommentPost>();
                        for (int i = 0; i < result.getFollowingList().size(); i++) {

                            name_data.add(new CommentPost(result.getFollowingList().get(i).getUserId(), result.getFollowingList().get(i).getDisplayName(), result.getFollowingList().get(i).getProfileImage()));
                        }
                        Search_Adapter search_adapter = new Search_Adapter(Comments.this, R.layout.design_results_name, name_data);
                        write_comment.setAdapter(search_adapter);
                    }

                } else
                    Log.e("API_Response", "following_list Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                Log.d("API_Error", "following_list Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Comments.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
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
            comment_value();
            Intent i = new Intent(Comments.this, Timeline.class);
            startActivity(i);
            transition();
        }
        if (v == menu_camera) {
            menu_status();
            comment_value();
            Intent i = new Intent(Comments.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            comment_value();
            Intent i = new Intent(Comments.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            comment_value();
            Intent i = new Intent(Comments.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            comment_value();
            Intent i = new Intent(Comments.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            comment_value();
            Intent i = new Intent(Comments.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            comment_value();
            Intent i = new Intent(Comments.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            comment_value();
            Intent i = new Intent(Comments.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == menu_stat) {
            menu_status();
            comment_value();
            Intent i = new Intent(Comments.this, Stats.class);
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
        if (v == send) {
            if (!write_comment.getText().toString().trim().equals("")) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity_layout.getWindowToken(), 0);
                progress_Dialog.show();
                if (id_list.isEmpty()) {
                    mentionID = "";
                } else {
                    for (int j = 0; j < id_list.size(); j++) {
                        mentionID = mentionID + "," + id_list.get(j);
                    }
                    mentionID = mentionID.replaceFirst(",", "");
                }
                post_comment();
            }
        }
    }

    //transition animation
    private void transition()
    {
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    //post points after post comment
    private void comment_value() {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("Comment_value", "0");
        editor.commit();
    }

    // opened menu visibility gone
    private void menu_status() {
        menu_click_view.setVisibility(View.VISIBLE);
        menu_open_layout.setVisibility(View.GONE);
    }

    //post comment
    private void post_comment() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "comment_post");
        postParam.put("comment_userid", preferences.getString("user_id", ""));
        postParam.put("postid", preferences.getString("post_id", ""));
        postParam.put("post_userid", preferences.getString("another_user", ""));
        postParam.put("comment", write_comment.getText().toString());
        postParam.put("mention_userid", mentionID);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Log.d("API_Parameters", "comment_post parameters :" + postParam.toString());

        Call<API_Response> call = service.getPostActionResponse(postParam);
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "comment_post Response : " + new Gson().toJson(response.body()));
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {

                    if (response.body().getResult().getMsg().equals("201")) {
                        id_list.clear();
                        mentionID = "";
                        write_comment.setText("");
                        get_comments();
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("Comment_points", response.body().getResult().getTotal_post_points());
                        editor.putString("Comment_post", preferences.getString("post_id", ""));
                        editor.putString("Comment_value", "1");
                        editor.commit();
                    } else {
                        Alerter.create(Comments.this)
                                .setText("Something went wrong. Please try again later.")
                                .setBackgroundColor(R.color.red)
                                .show();
                    }

                } else
                {
                    Alerter.create(Comments.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "comment_post Response : " + new Gson().toJson(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_Dialog.dismiss();
               Alerter.create(Comments.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "comment_post Error : " + t.getMessage());
            }
        });
    }

}
