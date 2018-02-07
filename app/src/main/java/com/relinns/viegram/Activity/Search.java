package com.relinns.viegram.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.relinns.viegram.Adapter.RandomPostAdapter;
import com.relinns.viegram.Adapter.SearchPersonAdapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.CommentPost;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.Modal.TimelinePost;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Search extends AppCompatActivity implements View.OnClickListener {
    private RandomPostAdapter photo_adapter;
    private RecyclerView search_photos;
    private GridLayoutManager layoutManager;
    private RelativeLayout badgeLayout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout search_view;
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
    public RelativeLayout activity_layout;
    private LinearLayout no_seach_result;
    private LinearLayout load_moreData;
    private SharedPreferences preferences;
    private AutoCompleteTextView search_text;
    private List<CommentPost> name_data;
    private ProgressBar progress;
    private int index = 1;
    private boolean loading = true;
    private List<TimelinePost> list;
    private TextView badgeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        list = new ArrayList<>();
        name_data = new ArrayList<>();
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        no_seach_result = findViewById(R.id.no_seach_result);
        load_moreData = findViewById(R.id.load_moreData);
        progress = findViewById(R.id.progress);
        progress_layout = findViewById(R.id.progress_layout);
        activity_layout = findViewById(R.id.activity_layout);
        menu_home = findViewById(R.id.menu_home);
        back = findViewById(R.id.back);
        search_view = findViewById(R.id.search_view);
        search_photos = findViewById(R.id.search_list);
        menu_open_layout = findViewById(R.id.search_menu_open);
        menu_click_view = findViewById(R.id.search_menu_click);
        menu_profile = findViewById(R.id.menu_profile);
        menu_stat = findViewById(R.id.menu_stat);
        menu_follow = findViewById(R.id.menu_follow_following);
        menu_notifications = findViewById(R.id.menu_notification);
        menu_settings = findViewById(R.id.menu_settings);
        menu_search = findViewById(R.id.menu_search);
        menu_ranking = findViewById(R.id.menu_ranking);
        menu_camera = findViewById(R.id.menu_camera);
        menu_close = findViewById(R.id.menu_close);
        search_text = findViewById(R.id.search_text);
        badgeLayout = findViewById(R.id.badge_layout);
        badgeText = findViewById(R.id.badge_text);

        search_view.setOnClickListener(this);
        search_text.setThreshold(0);

        search_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.toString().equals("")) {
                    if (!list.isEmpty()) {
                        layoutManager = new GridLayoutManager(Search.this, 4);
                        search_photos.setHasFixedSize(true);
                        search_photos.setLayoutManager(layoutManager);
                        photo_adapter = new RandomPostAdapter(Search.this, list);
                        search_photos.setAdapter(photo_adapter);
                        search_text.clearFocus();
                    } else {
                        search_photos.setVisibility(View.GONE);
                        no_seach_result.setVisibility(View.VISIBLE);
                    }
                } else
                    search_people(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (search_text.getText().toString().equals("")) {
                    layoutManager = new GridLayoutManager(Search.this, 4);
                    search_photos.setHasFixedSize(true);
                    search_photos.setLayoutManager(layoutManager);
                    photo_adapter = new RandomPostAdapter(Search.this, list);
                    search_photos.setAdapter(photo_adapter);
                    search_text.clearFocus();
                }
            }
        });

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

        menu_open_layout.setVisibility(View.GONE);
        no_seach_result.setVisibility(View.GONE);
        progress_layout.setVisibility(View.VISIBLE);
        search_photos.setVisibility(View.GONE);
        get_random_posts();
    }

    private void get_random_posts() {
        Map<String, String> postparams = new HashMap<>();
        postparams.put("action", "random_posts");
        postparams.put("userid", preferences.getString("user_id", ""));
        postparams.put("page", "" + index);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "random_posts parameters :" + postparams.toString());
        Call<API_Response> call = service.FriendsWork(postparams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.d("API_Response", "random_posts Response : " + new Gson().toJson(response.body()));
                    final Result result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        if (!result.getTotalRecords().equals("0")) {
                            search_photos.setVisibility(View.VISIBLE);
                            progress_layout.setVisibility(View.GONE);
                            no_seach_result.setVisibility(View.GONE);
                            layoutManager = new GridLayoutManager(Search.this, 4);
                            search_photos.setHasFixedSize(true);
                            list = result.getTimelinePosts();
                            search_photos.setLayoutManager(layoutManager);
                            photo_adapter = new RandomPostAdapter(Search.this, list);
                            search_photos.setAdapter(photo_adapter);
                            search_photos.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                @Override
                                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                    super.onScrollStateChanged(recyclerView, newState);
                                }

                                @Override
                                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                    super.onScrolled(recyclerView, dx, dy);
                                    if (dy > 0) {
                                        int visibleItemCount = layoutManager.getChildCount();
                                        int totalItemCount = layoutManager.getItemCount();
                                        int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                                        if (Integer.parseInt(result.getTotalRecords()) > totalItemCount) {
                                            if (loading) {
                                                if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                                    load_moreData.setVisibility(View.VISIBLE);
                                                    loading = false;
                                                    Log.v("P_new data", "Last Item Wow !");
                                                    //Do pagination.. i.e. fetch new data
                                                    index++;
                                                    load_more(index);
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        } else {
                            no_seach_result.setVisibility(View.VISIBLE);
                            search_photos.setVisibility(View.GONE);
                        }
                    } else if (result.getMsg().equals("204")) {
                        no_seach_result.setVisibility(View.VISIBLE);
                        search_photos.setVisibility(View.GONE);
                    } else Alerter.create(Search.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                } else {
                    Alerter.create(Search.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "random_posts Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
             Alerter.create(Search.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "random_posts Error : " + t.getMessage());
            }
        });
    }

    private void search_people(String s) {
        Map<String, String> postparams = new HashMap<>();
        postparams.put("action", "search_user");
        postparams.put("userid", preferences.getString("user_id", ""));
        postparams.put("search", s);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "search_user parameters :" + postparams.toString());
        Call<API_Response> call = service.FriendsWork(postparams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.e("API_Response", "search_user Response : " + new Gson().toJson(response.body().getResult()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        name_data = new ArrayList<CommentPost>();
                        no_seach_result.setVisibility(View.GONE);
                        search_photos.setVisibility(View.VISIBLE);
                        progress_layout.setVisibility(View.GONE);
                        name_data = response.body().getResult().getUserDetails();

                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(Search.this);
                        search_photos.setLayoutManager(mLayoutManager);
                        SearchPersonAdapter adapter = new SearchPersonAdapter(Search.this, name_data);
                        search_photos.setAdapter(adapter);
                    } else if (response.body().getResult().getMsg().equals("204")) {
                        no_seach_result.setVisibility(View.VISIBLE);
                        search_photos.setVisibility(View.GONE);
                        progress_layout.setVisibility(View.GONE);

                    } else Alerter.create(Search.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                } else {
                    Alerter.create(Search.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "search_user Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                no_seach_result.setVisibility(View.GONE);
                search_photos.setVisibility(View.GONE);
                progress_layout.setVisibility(View.GONE);
                    Alerter.create(Search.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();

                Log.d("API_Error", "search_user Error : " + t.getMessage());
            }
        });
    }

    private void load_more(int index) {
        Map<String, String> postparams = new HashMap<>();
        postparams.put("action", "random_posts");
        postparams.put("userid", preferences.getString("user_id", ""));
        postparams.put("page", "" + index);
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "random_posts parameters :" + postparams.toString());
        Call<API_Response> call = service.FriendsWork(postparams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.d("API_Response", "random_posts Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        list.addAll(response.body().getResult().getTimelinePosts());
                        photo_adapter.notifyDataSetChanged();
                        load_moreData.setVisibility(View.GONE);
                        loading = true;
                    }
                } else
                    Log.e("API_Response", "random_posts Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {

                 Alerter.create(Search.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Search.this, Timeline.class);
        startActivity(i);
        Search.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == search_view) {
            if (search_text.getText().toString().equals(""))
                search_text.requestFocus();
            else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity_layout.getWindowToken(), 0);
                search_people(search_text.getText().toString());
            }
        }

        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Search.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Search.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Search.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Search.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Search.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Search.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            get_random_posts();
//            Intent i = new Intent(Search.this, Search.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Search.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Search.this, Stats.class);
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
}
