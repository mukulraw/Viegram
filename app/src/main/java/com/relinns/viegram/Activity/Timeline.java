package com.relinns.viegram.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.relinns.viegram.Adapter.Timeline_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.LeaderData;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.Modal.TagPerson;
import com.relinns.viegram.Modal.TimelinePost;
import com.relinns.viegram.Pojo.ResultPojo;
import com.relinns.viegram.Pojo.UserData;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.relinns.viegram.uploadservice.events.WeakInstanceClass;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import retrofit2.Call;
import retrofit2.Callback;

public class Timeline extends AppCompatActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout swipe_refresh;
    private TextView point1;
    private TextView point2;
    private TextView point3;
    private TextView point4;
    private TextView point5;
    private TextView point6;
    private TextView point7;
    private TextView point8;
    private TextView point9;
    private RecyclerView timeline;
    private Timeline_Adapter adapter;
    private RelativeLayout badgeLayout, progress_layout, activity_layout;
    private RelativeLayout no_posts;
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
    private ProgressBar progress;
    private List<TimelinePost> list;
    private LinearLayoutManager layoutManager;
    private Boolean loading = true;
    private int index = 1;
    private int total_posts = 0;
    private int offlineposts = 0;
    private int value = 0;
    private LinearLayout load_moreData;
    private TextView badgeText;
    public static ResultPojo resultp;
    public static List<LeaderData> rankingDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        list = new ArrayList<>();
        rankingDetail = new ArrayList<>();
        timeline = findViewById(R.id.timeline);
        point1 = findViewById(R.id.point1);
        point2 = findViewById(R.id.point2);
        point3 = findViewById(R.id.point3);
        point4 = findViewById(R.id.point4);
        point5 = findViewById(R.id.point5);
        point6 = findViewById(R.id.point6);
        point7 = findViewById(R.id.point7);
        point8 = findViewById(R.id.point8);
        point9 = findViewById(R.id.point9);
        no_posts = findViewById(R.id.no_post);
        load_moreData = findViewById(R.id.load_moreData);
        progress = findViewById(R.id.progress);
        progress_layout = findViewById(R.id.progress_layout);
        activity_layout = findViewById(R.id.activity_layout);
        swipe_refresh = findViewById(R.id.swipe_refresh);
        menu_home = findViewById(R.id.menu_home);
        menu_open_layout = findViewById(R.id.timeline_menu_open);
        menu_click_view = findViewById(R.id.timeline_menu_click);
        menu_profile = findViewById(R.id.menu_profile);
        menu_stat = findViewById(R.id.menu_stat);
        menu_follow = findViewById(R.id.menu_follow_following);
        menu_notifications = findViewById(R.id.menu_notification);
        menu_settings = findViewById(R.id.menu_settings);
        menu_search = findViewById(R.id.menu_search);
        menu_ranking = findViewById(R.id.menu_ranking);
        menu_camera = findViewById(R.id.menu_camera);
        menu_close = findViewById(R.id.menu_close);
        badgeLayout = findViewById(R.id.badge_layout);
        badgeText = findViewById(R.id.badge_text);
        updatePoints(preferences.getString("total_points", "000000000"));

        WeakInstanceClass.getInstance().updateReference(this);

        makepostconnection();


        getLeaderData();

        if (preferences.getBoolean("timelinecache", false)) {
            progress_layout.setVisibility(View.GONE);
            swipe_refresh.setVisibility(View.VISIBLE);
            getFromCache();
        } else {
            progress_layout.setVisibility(View.VISIBLE);
            swipe_refresh.setVisibility(View.GONE);
        }

        no_posts.setVisibility(View.GONE);
        menu_open_layout.setVisibility(View.GONE);
        load_moreData.setVisibility(View.GONE);
        try {
            get_timeline();
        } catch (TimeoutException e) {
            load_moreData.setVisibility(View.GONE);
            loading = true;

            load_moreData.setVisibility(View.GONE);
            swipe_refresh.setRefreshing(false);

            Alerter.create(Timeline.this)
                    .setText(R.string.network_error)
                    .setBackgroundColor(R.color.red)
                    .show();
            Log.d("API_", "Timeout error : " + e.getMessage());
            e.printStackTrace();
        }
        upload_token();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("signup")) {
                final Dialog dialog = new Dialog(this);
                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.pop_up_congratulations);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                RelativeLayout cancel = dialog.findViewById(R.id.cancel1);
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("timelinecache", false);
                editor.apply();
            }
        }
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
        menu_home.setOnClickListener(this);
        swipe_refresh.setOnRefreshListener(this);
        timeline.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                    Log.d("kajal", "total post: " + total_posts);
                    Log.d("kajal", "total item count : " + totalItemCount);
                    Log.d("kajal", "visible item count : " + visibleItemCount);
                    Log.d("kajal", "past visible count : " + pastVisiblesItems);
//                    if (total_posts > totalItemCount) {
                    if (loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                            load_moreData.setVisibility(View.VISIBLE);
                            loading = false;
                            Log.v("P_new data", "Last Item Wow !");
                            index++;
                            load_more(index);
                        }
                    }
//                    }
//                    else if ((pastVisiblesItems+visibleItemCount) >= total_posts) {
//                        Alerter.create(Timeline.this)
//                                .setText("No more posts to display")
//                                .setBackgroundColor(R.color.red)
//                                .show();                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }
        });
//
//        swipe_refresh.post(new Runnable() {
//                               @Override
//                               public void run() {
//                                   swipe_refresh.setRefreshing(true);
//                                   makepostconnection();
//                               }
//                           }
//        );


    }

    public void makepostconnection() {

        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "all_data");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "fetch_profile parameters :" + postParams.toString());
        Call<UserData> call = service.getAllData(postParams);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {

                if (response.isSuccessful()) {
                    Log.d("API_Response", "all_data Response : " + new Gson().toJson(response.body()));
                    resultp = response.body().getResult();
                    if (resultp.getMsg().equals("201")) {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("total_points", response.body().getResult().getScorePoints());
                        editor.commit();
                        updatePoints(response.body().getResult().getScorePoints());
//                        Toast.makeText(c, "This is my Toast message1!" + resultp.getDisplayName(),
//                                Toast.LENGTH_LONG).show();
                    }
                } else
                    Log.d("API_Response", "all_data Response : " + new Gson().toJson(response.errorBody()));

            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                // progress_layout.setVisibility(View.VISIBLE);
                swipe_refresh.setRefreshing(false);
                Log.d("API_Error", "All data : " + t.getMessage());
//                Toast.makeText(c, "This is my Toast message!3",
//                        Toast.LENGTH_LONG).show();
                // progress.setVisibility(View.GONE);
            }
        });

    }

    private void getFromCache() {

        String secondname = "", profileimage = "", postimage = "", location = "", firstname = "", timeago = "", firstuserid = "", seconduserid = "", caption = "", tagpeopleid = "", tagpeoplename = "",
                tagx = "", tagy = "", imagewidth = "", imageheight = "", posttype = "", filetype = "", postpoints = "", postlike = "", postid = "", repostid = "", video = "";
        profileimage = preferences.getString("timelineprofile", "");
        firstname = preferences.getString("firstname", "");
        secondname = preferences.getString("secondname", "");
        firstuserid = preferences.getString("firstuserid", "");
        seconduserid = preferences.getString("seconduserid", "");
        location = preferences.getString("locationtimeline", "");
        timeago = preferences.getString("timeago", "");
        postimage = preferences.getString("timelinepost", "");
        postid = preferences.getString("timelinepostid", "");
        posttype = preferences.getString("posttype", "");
        filetype = preferences.getString("postfile", "");
        postlike = preferences.getString("postlike", "");
        video = preferences.getString("post_video", "");
        repostid = preferences.getString("repostid", "");
        postpoints = preferences.getString("postpoint", "");
        caption = preferences.getString("caption", "");
        tagpeoplename = preferences.getString("tagpeoplename", "");
        tagpeopleid = preferences.getString("tagpeopleid", "");
        tagx = preferences.getString("tagx", "");
        tagy = preferences.getString("tagy", "");
        imageheight = preferences.getString("postheight", "");
        imagewidth = preferences.getString("postwidth", "");
        if (!list.isEmpty()) {
            list.clear();
        }
        String[] videoArray = video.split(",");
        String[] image = profileimage.split(",");
        String[] name = firstname.split(",");
        String[] secondName = secondname.split(",");
        String[] firstid = firstuserid.split(",");
        String[] secondid = seconduserid.split(",");
        String[] locationArray = location.split("/");
        String[] time_Ago = timeago.split("/");
        String[] photo = postimage.split(",");
        String[] photoid = postid.split(",");
        String[] type = posttype.split(",");
        String[] file = filetype.split(",");
        String[] like = postlike.split(",");
        String[] points = postpoints.split(",");
        String[] repost_id = repostid.split(",");
        String[] captiontext = caption.split("\\^");
        String[] temptagname = tagpeoplename.split("/,");
        String[] temptagid = tagpeopleid.split("/,");
        String[] temptag_x = tagx.split("/,");
        String[] temptag_y = tagy.split("/,");
        String[] height = imageheight.split(",");
        String[] width = imagewidth.split(",");

        for (int m = 0; m < captiontext.length; m++) {
            List<TagPerson> tagdata = new ArrayList<>();

            if (temptagid[m].contains(",")) {
                String[] tagname = temptagname[m].split(",");
                String[] tagid = temptagid[m].split(",");
                String[] tag_x = temptag_x[m].split(",");
                String[] tag_y = temptag_y[m].split(",");
                for (int z = 0; z < tagid.length; z++) {
                    tagdata.add(new TagPerson(tagid[z], tagname[z], tag_x[z], tag_y[z]));
                }
            } else {
                tagdata.add(new TagPerson(temptagid[m], temptagname[m], temptag_x[m], temptag_y[m]));
            }
            list.add(m, new TimelinePost(getStringValue(videoArray, m), getStringValue(type, m), getStringValue(repost_id, m), getStringValue(photoid, m), getStringValue(points, m), getStringValue(like, m), getStringValue(secondid, m), getStringValue(firstid, m), getStringValue(secondName, m), getStringValue(image, m), getStringValue(photo, m), getStringValue(file, m), getStringValue(captiontext, m), getStringValue(name, m), tagdata, getStringValue(locationArray, m), getStringValue(width, m), getStringValue(height, m), getStringValue(time_Ago, m)));
        }
        offlineposts = list.size();
        timeline.measure(0, 0);
        layoutManager = new LinearLayoutManager(Timeline.this);
        timeline.setLayoutManager(layoutManager);
        adapter = new Timeline_Adapter(Timeline.this, list, value);
        timeline.setAdapter(adapter);

        timeline.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                JZVideoPlayer jzvd = view.findViewById(R.id.player);
                if (jzvd != null && jzvd.dataSourceObjects != null)
                    if (JZUtils.dataSourceObjectsContainsUri(jzvd.dataSourceObjects, JZMediaManager.getCurrentDataSource()))
                        JZVideoPlayer.releaseAllVideos();
            }
        });

    }

    private String getStringValue(String[] mArray, int mPosition) {
        try {
            if (mArray[mPosition] != null)
                return mArray[mPosition];
            else
                return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    private void upload_token() {

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "update_token");
        postParam.put("userid", preferences.getString("user_id", ""));
        postParam.put("device_token", FirebaseInstanceId.getInstance().getToken());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.
                pointsWork(postParam);

        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (!response.isSuccessful()) {
                    upload_token();
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
//                Log.d("API_Error ", "update_token error : " + t.getMessage());
                upload_token();
            }
        });

    }

    private void load_more(int index) {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "fetch_timeline");
        postParam.put("userid", preferences.getString("user_id", ""));
        postParam.put("page", index + "");
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.getTimeline(postParam);
        Log.wtf("URL Called", call.request().url() + "");
        Log.d("API_Parameters", "Timeline Parameters : " + postParam.toString());
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                load_moreData.setVisibility(View.GONE);
                loading = true;
                if (response.isSuccessful()) {

                    Log.d("API_Response", "Timeline Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getTimelinePosts() != null) {
                        list.addAll(response.body().getResult().getTimelinePosts());
                        adapter.notifyDataSetChanged();

                    } else Alerter.create(Timeline.this)
                            .setText("No more posts to display")
                            .setBackgroundColor(R.color.red)
                            .show();
                } else {
                    Alerter.create(Timeline.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "Timeline Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                load_moreData.setVisibility(View.GONE);
                Alerter.create(Timeline.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error ", "Timeline error : " + t.getMessage());
            }
        });
    }

    private void get_timeline() throws TimeoutException {

        Map<String, String> postParam = new HashMap<>();
        postParam.put("action", "fetch_timeline");
        postParam.put("userid", preferences.getString("user_id", ""));
        postParam.put("page", index + "");
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Call<API_Response> call = service.getTimeline(postParam);

        Log.d("API_Parameters", "Timeline Parameters : " + postParam.toString());
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_layout.setVisibility(View.GONE);
                load_moreData.setVisibility(View.GONE);
                offlineposts = 0;
                if (response.isSuccessful()) {
                    updatePoints(response.body().getResult().getTotal_score());
                    Log.d("API_Response", "Timeline Response : " + new Gson().toJson(response.body()));
                    generatetimelineData(response.body().getResult());
                } else {
                    swipe_refresh.setRefreshing(false);
                    Alerter.create(Timeline.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "Timeline Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                load_moreData.setVisibility(View.GONE);
                progress_layout.setVisibility(View.GONE);
                loading = true;
                Log.d("API_Error ", "Timeline error : " + t.getMessage());
                load_moreData.setVisibility(View.GONE);
                swipe_refresh.setRefreshing(false);
                Alerter.create(Timeline.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
            }
        });
    }

    private void generatetimelineData(Result result) {
        String msg = result.getMsg();
        progress_layout.setVisibility(View.GONE);
        if (msg.equals("201")) {
            if (!list.isEmpty()) {
                list.clear();
            }
            Log.e("timeline_rsponse", "add data: +" + result.getTotalRecords());

            try {
                total_posts = Integer.parseInt(result.getTotalRecords());
            } catch (Exception e) {

            }

            no_posts.setVisibility(View.GONE);
            swipe_refresh.setVisibility(View.VISIBLE);
            swipe_refresh.setRefreshing(false);
            list = result.getTimelinePosts();
            load_moreData.setVisibility(View.GONE);
            loading = true;
            timeline.measure(0, 0);
            layoutManager = new LinearLayoutManager(Timeline.this);
            timeline.setLayoutManager(layoutManager);
            adapter = new Timeline_Adapter(Timeline.this, list, value);
            timeline.setAdapter(adapter);
            timeline.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(View view) {
                }

                @Override
                public void onChildViewDetachedFromWindow(View view) {
                    JZVideoPlayer jzvd = view.findViewById(R.id.player);
                    if (jzvd != null && jzvd.dataSourceObjects != null)
                        if (JZUtils.dataSourceObjectsContainsUri(jzvd.dataSourceObjects, JZMediaManager.getCurrentDataSource()))
                            JZVideoPlayer.releaseAllVideos();
                }
            });

            addtocache();
        } else {
            String reason = result.getReason();
            TextView no_timeline_text = findViewById(R.id.no_timeline_text);
            if (reason.equals("No post upload by your following")) {
                no_posts.setVisibility(View.VISIBLE);
                timeline.setVisibility(View.GONE);
                no_timeline_text.setText(getResources().getString(R.string.no_following_posts));
            } else {
                no_posts.setVisibility(View.VISIBLE);
                timeline.setVisibility(View.GONE);
                no_timeline_text.setText(getResources().getString(R.string.no_posts));
            }
        }

//        timeline.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (dy > 0) {
//                    int visibleItemCount = layoutManager.getChildCount();
//                    int totalItemCount = layoutManager.getItemCount();
//                    int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
//                    Log.d("kajal","total post: "+total_posts);
//                    Log.d("kajal","total item count : "+totalItemCount);
//                    Log.d("kajal","visible item count : "+visibleItemCount);
//                    Log.d("kajal","past visible count : "+pastVisiblesItems);
////                    if (total_posts > totalItemCount) {
//                        if (loading) {
//                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
//                                load_moreData.setVisibility(View.VISIBLE);
//                                loading = false;
//                                Log.v("P_new data", "Last Item Wow !");
//                                index++;
//                                load_more(index);
//                            }
//                        }
////                    }
////                    else if ((pastVisiblesItems+visibleItemCount) >= total_posts) {
////                        Alerter.create(Timeline.this)
////                                .setText("No more posts to display")
////                                .setBackgroundColor(R.color.red)
////                                .show();                    }
//                }
//            }
//
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//        });
    }

    private void addtocache() {
        String secondname = "", profileimage = "", postimage = "", location = "", firstname = "", timeago = "", firstuserid = "", seconduserid = "", caption = "", tagpeopleid = "", tagpeoplename = "",
                tagx = "", tagy = "", imagewidth = "", imageheight = "", posttype = "", filetype = "", postpoints = "", postlike = "", postid = "", repostid = "", video = "";

        for (int k = 0; k < list.size(); k++) {

            tagpeoplename = tagpeoplename + "/";
            tagpeopleid = tagpeopleid + "/";
            tagx = tagx + "/";

            tagy = tagy + "/";
            profileimage = profileimage + "," + list.get(k).getProfileImage();
            firstname = firstname + "," + list.get(k).getFirst_display_name();
            secondname = secondname + "," + list.get(k).getDisplayName();
            firstuserid = firstuserid + "," + list.get(k).getSecondUserid();
            seconduserid = seconduserid + "," + list.get(k).getUserid();
            postimage = postimage + "," + list.get(k).getPhoto();
            video = video + "," + list.get(k).getVideo();

            try {
                if (list.get(k).getCaption().equalsIgnoreCase("") || list.get(k).getCaption() == null || list.get(k).getCaption().isEmpty()) {
                    caption = caption + "^" + "null";
                } else {
                    caption = caption + "^" + list.get(k).getCaption();
                }
            } catch (Exception e) {
                caption = caption + "^" + "null";

            }
            timeago = timeago + "/" + list.get(k).getTimeAgo();
            repostid = repostid + "," + list.get(k).getRepostId();
            filetype = filetype + "," + list.get(k).getFileType();
            posttype = posttype + "," + list.get(k).getPostType();
            postid = postid + "," + list.get(k).getPostId();
            postpoints = postpoints + "," + list.get(k).getPostPoints();
            location = location + "/" + list.get(k).getLocation();
            postlike = postlike + "," + list.get(k).getPostLike();

            imageheight = imageheight + "," + list.get(k).getImageHeight();
            imagewidth = imagewidth + "," + list.get(k).getImageWidth();

            for (int j = 0; j < list.get(k).getTagPeople().size(); j++) {
//                Log.e("Tag","add data: +"+list.get(k).getTagPeople().get(j).getId());
//                if (list.get(k).getTagPeople().get(j).getId().equals(""))
//                {
//                    tagpeoplename = tagpeoplename + "," + "null";
//                    tagpeopleid = tagpeopleid + "," + "null";
//                    tagx = tagx + "," + "null";
//                    tagy = tagy + "," + "null";
//                }
//                else
//                {
                tagpeoplename = tagpeoplename + "," + list.get(k).getTagPeople().get(j).getDisplayName();
                tagpeopleid = tagpeopleid + "," + list.get(k).getTagPeople().get(j).getId();
                tagx = tagx + "," + list.get(k).getTagPeople().get(j).getXCordinates();
                tagy = tagy + "," + list.get(k).getTagPeople().get(j).getYCordinates();
//                }

            }

        }
        tagpeoplename = tagpeoplename.replaceFirst("/", "");
        tagpeopleid = tagpeopleid.replaceFirst("/", "");
        tagx = tagx.replaceFirst("/", "");
        tagy = tagy.replaceFirst("/", "");
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString("timelineprofile", profileimage.replaceFirst(",", ""));
        edit.putString("firstname", firstname.replaceFirst(",", ""));
        edit.putString("secondname", secondname.replaceFirst(",", ""));
        edit.putString("firstuserid", firstuserid.replaceFirst(",", ""));
        edit.putString("seconduserid", seconduserid.replaceFirst(",", ""));
        edit.putString("locationtimeline", location.replaceFirst("/", ""));
        edit.putString("timeago", timeago.replaceFirst("/", ""));
        edit.putString("timelinepost", postimage.replaceFirst(",", ""));
        edit.putString("timelinepostid", postid.replaceFirst(",", ""));
        edit.putString("repostid", repostid.replaceFirst(",", ""));
        edit.putString("posttype", posttype.replaceFirst(",", ""));
        edit.putString("postfile", filetype.replaceFirst(",", ""));
        edit.putString("postlike", postlike.replaceFirst(",", ""));
        edit.putString("postpoint", postpoints.replaceFirst(",", ""));
        edit.putString("caption", caption.replaceFirst("\\^", ""));
        edit.putString("tagpeoplename", tagpeoplename.replaceFirst(",", ""));
        edit.putString("tagpeopleid", tagpeopleid.replaceFirst(",", ""));
        edit.putString("tagx", tagx.replaceFirst(",", ""));
        edit.putString("tagy", tagy.replaceFirst(",", ""));
        edit.putString("post_video", video.replaceFirst(",", ""));
        edit.putString("postheight", imageheight.replaceFirst(",", ""));
        edit.putString("postwidth", imagewidth.replaceFirst(",", ""));
        edit.putBoolean("timelinecache", true);
        edit.apply();


    }

    private void getLeaderData() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "overall_ranking");
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "overall_ranking parameters :" + postParams.toString());
        Call<API_Response> call = service.rankingRelated(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "overall_ranking Response : " + new Gson().toJson(response.body()));
                if (response.isSuccessful()) {
                    rankingDetail = response.body().getResult().getRankingDetails();
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {

            }
        });
    }

//    private void get_points() {
//
//        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("action", "score_points");
//        postParam.put("userid", preferences.getString("user_id", ""));
//
////        Log.wtf("index", index + "");
//        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
//
//        Call<API_Response> call = service.rankingRelated(postParam);
//
//
////        Log.d("API_Parameters", "score_points Parameters : " + postParam.toString());
//        call.enqueue(new Callback<API_Response>() {
//            @Override
//            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
//                if (response.isSuccessful()) {
////                    Log.d("API_Response", "score_points Response : " + new Gson().toJson(response.body()));
//
//
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putString("total_points", response.body().getResult().getScore_points());
//                    editor.commit();
//                    updatePoints(response.body().getResult().getScore_points());
//
////                    Log.d("API_Response", "score_points Response : " + new Gson().toJson(response.body()));
//
//                }
////                else
////                    Log.d("API_Response", "score_points Error  Response : " + new Gson().toJson(response.errorBody()));
//
//            }
//
//            @Override
//            public void onFailure(Call<API_Response> call, Throwable t) {
////                Log.d("API_Error ", "score_points error : " + t.getMessage());
//                upload_token();
//            }
//        });
//
//    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == menu_home) {
            menu_status();
          /*  Intent i = new Intent(Timeline.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);*/
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Timeline.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Timeline.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Timeline.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Timeline.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Timeline.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Timeline.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Timeline.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Timeline.this, Stats.class);
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

    private void updatePoints(String points) {
        point1.setText(points.substring(8));
        point2.setText(points.substring(7, 8));
        point3.setText(points.substring(6, 7));
        point4.setText(points.substring(5, 6));
        point5.setText(points.substring(4, 5));
        point6.setText(points.substring(3, 4));
        point7.setText(points.substring(2, 3));
        point8.setText(points.substring(1, 2));
        point9.setText(points.substring(0, 1));
    }

    @Override
    protected void onResume() {
        super.onResume();


        if (preferences.getString("Comment_value", "").equals("1")) {
            Log.d("Tag", "comment value 1");
            for (int i = 0; i < list.size(); i++) {
                Log.d("Tag", "list size " + list.size());

                if (list.get(i).getRepostId().equals(preferences.getString("Comment_post", ""))) {
                    Log.d("Tag", "post find ");

                    list.get(i).setPostPoints(preferences.getString("Comment_points", ""));
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("Comment_value", "0");
                    edit.commit();
                    adapter.notifyDataSetChanged();
                }
            }
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (preferences.getBoolean("timelinecache", false)) {
            value = 0;
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onRefresh() {
        index = 1;
        try {
            get_timeline();
        } catch (TimeoutException e) {
            load_moreData.setVisibility(View.GONE);
            loading = true;

            load_moreData.setVisibility(View.GONE);
            swipe_refresh.setRefreshing(false);
            Alerter.create(Timeline.this)
                    .setText(R.string.network_error)
                    .setBackgroundColor(R.color.red)
                    .show();
            e.printStackTrace();
            Log.d("API_", "Timeout error : " + e.getMessage());
        }
        makepostconnection();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        finishAffinity();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
        if (preferences.getBoolean("timelinecache", false)) {
            value = 1;
            adapter.notifyDataSetChanged();
        }
    }
}
