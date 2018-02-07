package com.relinns.viegram.Activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.relinns.viegram.Adapter.Search_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.CommentPost;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.Modal.TagPerson;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Tag_photo extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener {
    private RelativeLayout badgeLayout;
    private RelativeLayout search_friend_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout search_friend;
    private RelativeLayout tag_photo_layout;
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
    private ImageView tag_image;
    private TextView badgeText;
    private Button done;
    private AutoCompleteTextView search_tag_friend;
    private int key;
    private int data;
    private SharedPreferences preferences;
    private List<CommentPost> name_data;
    private List<CommentPost> temp_data;
    private Search_Adapter search_adapter;
    private View who_this;

    private List<TagPerson> tag_list;
    private float tempWidth;
    private float tempHeight;
    private float image_width;
    private float image_height;
    private float endWidth;
    private float endHeight;
    private float x_value;
    private float y_value;
    private Result result;
    private DisplayMetrics displayMetrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_photo);
        name_data = new ArrayList<>();
        tag_list = new ArrayList<>();
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        search_friend_layout = (RelativeLayout) findViewById(R.id.search_friend_layout);
        search_friend_layout.setVisibility(View.GONE);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        tag_image = (ImageView) findViewById(R.id.tag_image);
        done = (Button) findViewById(R.id.done);
        back = (RelativeLayout) findViewById(R.id.back);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        menu_open_layout = (RelativeLayout) findViewById(R.id.tag_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.tag_menu_click);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        tag_photo_layout = (RelativeLayout) findViewById(R.id.tag_photo_layout);
        search_tag_friend = (AutoCompleteTextView) findViewById(R.id.search_tag_friend);
        search_friend = (RelativeLayout) findViewById(R.id.search_friend);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;
        tempWidth = Float.parseFloat(getIntent().getStringExtra("image_width"));
        tempHeight = Float.parseFloat(getIntent().getStringExtra("image_height"));
        tag_image.setImageBitmap(BitmapFactory.decodeFile(getIntent().getStringExtra("image_path")));

        displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        image_width = displayMetrics.widthPixels;
        float aspectRatio = tempWidth / tempHeight;
        image_height = image_width / aspectRatio;
        tag_image.setLayoutParams(new RelativeLayout.LayoutParams((int) image_width, (int) (image_height)));
        tag_image.measure(0, 0);
        endWidth = image_width - ((image_width * 30) / 100);
        endHeight = image_height - ((image_height * 10) / 100);

        search_tag_friend.setThreshold(0);
        menu_open_layout.setVisibility(View.GONE);
        tag_image.setOnTouchListener(this);
        menu_home.setOnClickListener(this);
        back.setOnClickListener(this);
        done.setOnClickListener(this);
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
        get_friend_list();
        menu_settings.setOnClickListener(this);
        menu_stat.setOnClickListener(this);
        search_friend.setOnClickListener(this);
        search_tag_friend.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                search_adapter = new Search_Adapter(Tag_photo.this, R.layout.design_results_name, name_data);
                search_tag_friend.setAdapter(search_adapter);
                search_tag_friend.showDropDown();
                return false;
            }
        });
        search_tag_friend.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                temp_data = new ArrayList<>();
                for (int data = 0; data < name_data.size(); data++) {
                    if (name_data.get(data).getDisplayName().contains(charSequence + "")) {
                        temp_data.add(name_data.get(data));
                    }
                    search_adapter = new Search_Adapter(Tag_photo.this, R.layout.design_results_name, temp_data);
                    search_tag_friend.setAdapter(search_adapter);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        search_tag_friend.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity_layout.getWindowToken(), 0);
                search_tag_friend.setText(name_data.get(i).getDisplayName());
                tag_photo_layout.removeView(who_this);
                float new_width = (x_value * 100) / image_width;
                float new_height = (y_value * 100) / image_height;
                tag_list.add(new TagPerson(name_data.get(i).getUserid(), search_tag_friend.getText().toString(), new_width + "", new_height + ""));
                Log.d("Tag","click id :"+name_data.get(i).getUserid());
                addname(x_value, y_value, search_tag_friend.getText().toString());
                key = 0;
            }
        });

        if (!preferences.getString("result_size", "").equals("0") && !preferences.getString("result_size", "").equals("")) {
            String temp_name[] = preferences.getString("result_name", "").split(",");
            String temp_x[] = preferences.getString("x_value_result", "").split(",");
            String temp_y[] = preferences.getString("y_value_result", "").split(",");
            String temp_id[] = preferences.getString("result_id", "").split(",");
            for (int i = 0; i < temp_name.length; i++) {

                tag_list.add(new TagPerson(temp_id[i], temp_name[i], temp_x[i], temp_y[i]));
                float width_prcntage = Float.parseFloat(temp_x[i]);
                float height_prcntage = Float.parseFloat(temp_y[i]);
                float new_width = (width_prcntage * image_width) / 100;
                float new_height = (height_prcntage * image_height) / 100;
                addname(new_width, new_height, tag_list.get(i).getDisplayName());
            }
        }
    }

    private void get_friend_list() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "following_list");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "following_list parameters :" + postParams.toString());
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.e("API_Response", "following_list Response : " + new Gson().toJson(response.body()));
                    result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        name_data = new ArrayList<CommentPost>();
                        for (int i = 0; i < result.getFollowingList().size(); i++) {
                            Log.d("Tag","follower id :"+result.getFollowingList().get(i).getUserId());
                            name_data.add(new CommentPost(result.getFollowingList().get(i).getUserId(), result.getFollowingList().get(i).getDisplayName(), result.getFollowingList().get(i).getProfileImage()));
                            Log.d("Tag","name id : "+name_data.get(i).getId());
                            Log.d("Tag","name user id :"+name_data.get(i).getUserid());
                        }
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
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == search_friend) {
            if (search_tag_friend.getText().toString().equals("")) {
                search_tag_friend.requestFocus();
                search_tag_friend.setAdapter(search_adapter);
                search_tag_friend.showDropDown();
            } else {
                temp_data = new ArrayList<>();
                for (int data = 0; data < name_data.size(); data++) {
                    if (name_data.get(data).getDisplayName().contains(search_tag_friend.getText().toString())) {
                        Log.d("Tag", "contains data" + name_data.get(data).getDisplayName());
                        temp_data.add(name_data.get(data));
                    }
                }
                search_adapter = new Search_Adapter(Tag_photo.this, R.layout.design_results_name, temp_data);
                search_tag_friend.setAdapter(search_adapter);
            }
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == done) {
            String result_id = "", result_name = "", x_value_result = "", y_value_result = "";
            for (int i = 0; i < tag_list.size(); i++) {
                result_id = result_id + "," + tag_list.get(i).getId();
                result_name = result_name + "," + tag_list.get(i).getDisplayName();
                x_value_result = x_value_result + "," + tag_list.get(i).getXCordinates();
                y_value_result = y_value_result + "," + tag_list.get(i).getYCordinates();
            }
            result_id = result_id.replaceFirst(",", "");
            result_name = result_name.replaceFirst(",", "");
            x_value_result = x_value_result.replaceFirst(",", "");
            y_value_result = y_value_result.replaceFirst(",", "");
            Intent returnIntent = new Intent();
            returnIntent.putExtra("result_id", result_id);
            returnIntent.putExtra("result_name", result_name);
            returnIntent.putExtra("x_value_result", x_value_result);
            returnIntent.putExtra("y_value_result", y_value_result);
            returnIntent.putExtra("result_size", tag_list.size() + "");
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            Tag_photo.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Tag_photo.this, Stats.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Tag_photo.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            search_friend_layout.setVisibility(View.VISIBLE);
            if (key == 0) {
                addlayout(event);
                key++;
            } else {
                tag_photo_layout.removeView(who_this);
                addlayout(event);
            }
        }
        return true;
    }

    private void addlayout(MotionEvent event) {
        View layout = LayoutInflater.from(Tag_photo.this).inflate(R.layout.tag_layout, tag_photo_layout, false);
        TextView name = (TextView) layout.findViewById(R.id.tag_friend_name);
        RelativeLayout cancel_name = (RelativeLayout) layout.findViewById(R.id.cancel_name);
        if (event.getX() > endWidth) {
            x_value = endWidth;
        } else {
            x_value = event.getX();
        }
        if (event.getY() > endHeight) {
            y_value = endHeight;
        } else {
            y_value = event.getY();
        }
        name.measure(0, 0);
        float p = name.getMeasuredWidth() + 15;
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) p, 40);
        params.setMargins((int) x_value, (int) y_value, 0, 0);
        cancel_name.setVisibility(View.GONE);
        tag_photo_layout.addView(layout, params);
        who_this = layout;
    }

    private void addname(final float x_coordinate, final float y_coordinate, String name_text) {

        final View layout = LayoutInflater.from(Tag_photo.this).inflate(R.layout.tag_layout, tag_photo_layout, false);
        final TextView name = (TextView) layout.findViewById(R.id.tag_friend_name);
        final RelativeLayout cancel_name = (RelativeLayout) layout.findViewById(R.id.cancel_name);
        name.setText(name_text);
        name.measure(0, 0);
        final int[] width = {name.getMeasuredWidth() + 30};
        final int[] height = {name.getMeasuredHeight() + 20};
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width[0], height[0]);
        params.setMargins((int) x_coordinate, (int) y_coordinate, 0, 0);
        cancel_name.setVisibility(View.GONE);
        tag_photo_layout.addView(layout, params);
        search_tag_friend.setText("");

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data++;
                RelativeLayout.LayoutParams params;
                tag_photo_layout.removeView(layout);
                if (data % 2 == 0) {
                    cancel_name.setVisibility(View.GONE);
                    name.measure(0, 0);
                    width[0] = name.getMeasuredWidth() + 30;
                    height[0] = name.getMeasuredHeight() + 20;
                    params = new RelativeLayout.LayoutParams(width[0], height[0]);
                } else {
                    cancel_name.setVisibility(View.VISIBLE);
                    cancel_name.measure(0, 0);
                    width[0] = width[0] + cancel_name.getMeasuredWidth();
                    height[0] = height[0] + cancel_name.getMeasuredHeight();
                    params = new RelativeLayout.LayoutParams(width[0], height[0]);
                }
                params.setMargins((int) x_coordinate, (int) y_coordinate, 0, 0);
                tag_photo_layout.addView(layout, params);
            }
        });
        cancel_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < tag_list.size(); i++) {
                    if (tag_list.get(i).getDisplayName().equals(name.getText().toString())) {
                        tag_list.remove(i);
                        tag_photo_layout.removeView(layout);
                    }
                }
            }
        });

    }
}
