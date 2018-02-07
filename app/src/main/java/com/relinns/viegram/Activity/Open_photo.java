package com.relinns.viegram.Activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.relinns.viegram.Modal.API_Response;
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

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import me.leolin.shortcutbadger.ShortcutBadger;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Open_photo extends AppCompatActivity implements View.OnClickListener {
    private ImageView profile_image, post_image, like_view, menu_click_view;
    private TextView badgeText, user_name, post_points, caption, userName, location_text, time_text, errorText;
    private RelativeLayout badgeLayout, progress_layout, back, activity_layout, like, comment, repost, options;
    private SharedPreferences preferences;
    private RelativeLayout postLayout;
    private RelativeLayout showTag;
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
    private JZVideoPlayerStandard player;
    private ProgressBar progress;
    private ProgressBar loadProgress;
    private List<TimelinePost> photo_data = new ArrayList<>();
    private ScrollView scrollView;
    private float vwidthScreen;
    private float vNewHeight;
    private int value = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_photo);

        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        //notification badge code
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey("decrement")) {
                int badge_value = preferences.getInt("badge_value", 0);
                badge_value--;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putInt("badge_value", badge_value);
                editor.commit();
                ShortcutBadger.applyCount(getApplicationContext(), badge_value);

            }
        }
        errorText = (TextView) findViewById(R.id.image_error_text);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        showTag = (RelativeLayout) findViewById(R.id.show_tag);
        loadProgress = (ProgressBar) findViewById(R.id.load_progress);
        player =  findViewById(R.id.player);
        postLayout = (RelativeLayout) findViewById(R.id.post_layout);
        profile_image = (ImageView) findViewById(R.id.user_image);
        post_image = (ImageView) findViewById(R.id.post_image);
        like_view = (ImageView) findViewById(R.id.photo_like_view);
        like = (RelativeLayout) findViewById(R.id.like);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        time_text = (TextView) findViewById(R.id.time_text);
        comment = (RelativeLayout) findViewById(R.id.comment);
        repost = (RelativeLayout) findViewById(R.id.repost);
        user_name = (TextView) findViewById(R.id.username_text);
        location_text = (TextView) findViewById(R.id.location_text);
        post_points = (TextView) findViewById(R.id.post_points);
        caption = (TextView) findViewById(R.id.caption);
        userName = (TextView) findViewById(R.id.username_post);
        options = (RelativeLayout) findViewById(R.id.post_options);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        menu_open_layout = (RelativeLayout) findViewById(R.id.open_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.open_menu_click);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);

        menu_open_layout.setVisibility(View.GONE);
        progress_layout.setVisibility(View.VISIBLE);
        scrollView.setVisibility(View.GONE);
        get_post();

        post_image.setOnClickListener(this);
        activity_layout.setOnClickListener(this);
        repost.setOnClickListener(this);
        comment.setOnClickListener(this);
        like.setOnClickListener(this);
        menu_home.setOnClickListener(this);
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
        options.setOnClickListener(this);
        profile_image.setOnClickListener(this);
        user_name.setOnClickListener(this);
        back.setOnClickListener(this);
        post_points.setOnClickListener(this);
    }

    //get post details
    private void get_post() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "open_picture");
        postParams.put("postid", preferences.getString("post_id", ""));
        postParams.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "open_picture parameters :" + postParams.toString());
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.d("API_Response", "open_picture Response : " + new Gson().toJson(response.body()));
                    final Result result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        photo_data = result.getTimelinePosts();
                        DisplayMetrics displayMetrics = new DisplayMetrics();
                        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                        progress_layout.setVisibility(View.GONE);
                        scrollView.setVisibility(View.VISIBLE);
                        vwidthScreen = displayMetrics.widthPixels;
                        if (photo_data.get(0).getImageWidth().equals("") || photo_data.get(0).getImageHeight().equals("")) {
                            LinearLayout.LayoutParams linearparams = new LinearLayout.LayoutParams((int) vwidthScreen, (int) vwidthScreen);
                            postLayout.setLayoutParams(linearparams);
                            errorText.setVisibility(View.VISIBLE);
                        } else {
                            errorText.setVisibility(View.GONE);
                            float aspectRatio = Float.parseFloat(photo_data.get(0).getImageWidth()) / Float.parseFloat(photo_data.get(0).getImageHeight());
                            vNewHeight = vwidthScreen / aspectRatio;
                            LinearLayout.LayoutParams linearparams = new LinearLayout.LayoutParams((int) vwidthScreen, (int) vNewHeight);
                            postLayout.setLayoutParams(linearparams);
                            Log.d("ImageParams", "image width : " + photo_data.get(0).getImageWidth() + "  image height : " + photo_data.get(0).getImageHeight());
                            Log.d("ImageParams", "aspect : " + aspectRatio);
                            Log.d("ImageParams", "new width : " + vwidthScreen + "  new height : " + vNewHeight);
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) vwidthScreen, (int) vNewHeight);
                            post_image.setLayoutParams(params);
                            showTag.setLayoutParams(params);
                            player.setLayoutParams(params);


                            if (!photo_data.get(0).getFileType().equals("image")) {

                                player.setVisibility(View.VISIBLE);
                                player.setUp(photo_data.get(0).getVideo(), JZVideoPlayer.SCREEN_WINDOW_LIST, "");
                                try {
                                    Glide.with(Open_photo.this).load(photo_data.get(0).getPhoto())
                                            .crossFade()
                                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                        //    .skipMemoryCache(true)
                                            .override((int) vwidthScreen, (int) vNewHeight)
                                        //    .thumbnail(0.1f)
                                            .into(player.thumbImageView);
                                }catch (Exception e){
                                }
                                post_image.setVisibility(View.GONE);
                                showTag.setVisibility(View.GONE);
                                loadProgress.setVisibility(View.GONE);
                            } else {
                                player.setVisibility(View.GONE);
                                showTag.setVisibility(View.VISIBLE);
                                post_image.setVisibility(View.VISIBLE);
                                loadProgress.setVisibility(View.VISIBLE);

                                try {
                                    Glide.with(Open_photo.this).load(photo_data.get(0).getPhoto())
                                            .crossFade()
                                            .diskCacheStrategy(DiskCacheStrategy.RESULT)
                                         //   .skipMemoryCache(true)
                                            .override((int) vwidthScreen, (int) vNewHeight)
                                        //    .thumbnail(0.1f)
                                            .listener(new RequestListener<String, GlideDrawable>() {
                                                @Override
                                                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                                    return false;
                                                }

                                                @Override
                                                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                                    loadProgress.setVisibility(View.GONE);
                                                    return false;
                                                }
                                            })
                                            .into(post_image);
                                }catch (Exception e){
                                }
                                for (int i = 0; i < photo_data.get(0).getTagPeople().size(); i++) {
                                    if (!photo_data.get(0).getTagPeople().get(i).getId().equals("") && !photo_data.get(0).getTagPeople().get(i).getId().equals("null") && !photo_data.get(0).getTagPeople().get(i).getId().equals("[]")) {
                                        float temp_width = Float.parseFloat(photo_data.get(0).getTagPeople().get(i).getXCordinates());
                                        float temp_height = Float.parseFloat(photo_data.get(0).getTagPeople().get(i).getYCordinates());
                                        float new_width = (temp_width * vwidthScreen) / 100;
                                        float tag_height = (temp_height * vNewHeight) / 100;
                                        add_name((int) new_width, (int) tag_height, photo_data.get(0).getTagPeople().get(i).getDisplayName());
                                    }
                                }
                            }
                        }
                        userName.setText(photo_data.get(0).getDisplayName());
                        caption.setText(photo_data.get(0).getCaption());
                        location_text.setText(photo_data.get(0).getLocation());
                        time_text.setText(photo_data.get(0).getTimeAgo());
                        post_points.setText(photo_data.get(0).getTotal_points());
                        if (!photo_data.get(0).getUser_id().equals(preferences.getString("user_id", ""))) {
                            options.setVisibility(View.GONE);
                            RelativeLayout.LayoutParams parameters = (RelativeLayout.LayoutParams) time_text.getLayoutParams();
                            parameters.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                            time_text.setLayoutParams(parameters); //causes layout update                            }
                        } else {
                            options.setVisibility(View.VISIBLE);
                            time_text.setVisibility(View.VISIBLE);
                        }
                        if (!photo_data.get(0).getFirst_display_name().equals("")) {
                            String post_name = photo_data.get(0).getDisplayName() + " shared " + photo_data.get(0).getFirst_display_name() + "\'s post.";
                            SpannableString spanString = new SpannableString(post_name);
                            ClickableSpan clickableSpan = new ClickableSpan() {
                                @Override
                                public void onClick(View textView) {


                                    if (photo_data.get(0).getUser_id().equals(preferences.getString("user_id", ""))) {
                                        Intent i = new Intent(Open_photo.this, Profile.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                    } else {

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("another_user", photo_data.get(0).getUser_id());
                                        editor.commit();
                                        Intent i = new Intent(Open_photo.this, Another_user.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                    }
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setStyle(Paint.Style.FILL_AND_STROKE);
                                    ds.setUnderlineText(false);
                                }
                            };
                            ClickableSpan clickableSpan1 = new ClickableSpan() {
                                @Override
                                public void onClick(View textView) {


                                    if (photo_data.get(0).getFirst_user_id().equals(preferences.getString("user_id", ""))) {
                                        Intent i = new Intent(Open_photo.this, Profile.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                    } else {

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("another_user", photo_data.get(0).getFirst_user_id());
                                        editor.commit();
                                        Intent i = new Intent(Open_photo.this, Another_user.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                    }
                                }

                                @Override
                                public void updateDrawState(TextPaint ds) {
                                    super.updateDrawState(ds);
                                    ds.setStyle(Paint.Style.FILL_AND_STROKE);

                                    ds.setUnderlineText(false);
                                }
                            };
                            int fromIndex, startSpan = 0, endSpan = 0;

                            fromIndex = 0;
                            while (fromIndex < (post_name.length() - 1)) {
                                startSpan = post_name.indexOf("shared", fromIndex);
                                if (startSpan == -1)
                                    break;
                                endSpan = startSpan + "shared".length();
                                spanString.setSpan(new ForegroundColorSpan(Color.DKGRAY), startSpan,
                                        endSpan, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                                fromIndex = endSpan;
                            }
                            spanString.setSpan(clickableSpan, 0, photo_data.get(0).getDisplayName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            spanString.setSpan(clickableSpan1, endSpan, photo_data.get(0).getFirst_display_name().length() + 1 + endSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                            user_name.setText(spanString);
                            user_name.setMovementMethod(LinkMovementMethod.getInstance());
                        } else {
                            SpannableString spanString = new SpannableString(photo_data.get(0).getDisplayName());


                            user_name.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    if (photo_data.get(0).getUser_id().equals(preferences.getString("user_id", ""))) {
                                        Intent i = new Intent(Open_photo.this, Profile.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                    } else {

                                        SharedPreferences.Editor editor = preferences.edit();
                                        editor.putString("another_user", photo_data.get(0).getUser_id());
                                        editor.commit();
                                        Intent i = new Intent(Open_photo.this, Another_user.class);
                                        startActivity(i);
                                        overridePendingTransition(R.anim.enter, R.anim.exit);
                                    }

                                }
                            });
                            spanString.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0,
                                    spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            user_name.setText(spanString);
                        }

                        Glide.with(Open_photo.this).load(photo_data.get(0).getProfileImage())
                                .bitmapTransform(new CropCircleTransformation(Open_photo.this))
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progress_layout.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(profile_image);

                        if (photo_data.get(0).getPostLike().equals("1")) {
                            like_view.setImageDrawable(getResources().getDrawable(R.drawable.liked_96));
                        } else {
                            like_view.setImageDrawable(getResources().getDrawable(R.drawable.like_96));
                        }
                    } else {
                        progress_layout.setVisibility(View.VISIBLE);
                        progress.setVisibility(View.GONE);
                        Alerter.create(Open_photo.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                } else {
                    Log.e("API_Response", "open_picture Response : " + new Gson().toJson(response.errorBody()));
                    progress_layout.setVisibility(View.VISIBLE);
                    progress.setVisibility(View.GONE);
                    Alerter.create(Open_photo.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.VISIBLE);
                progress.setVisibility(View.GONE);
               Alerter.create(Open_photo.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "open_picture Error : " + t.getMessage());
            }
        });
    }

    //show tag person name
    private void add_name(int new_width, int tag_height, String displayName) {
        final View layout = LayoutInflater.from(Open_photo.this).inflate(R.layout.tag_layout, showTag, false);
        final TextView tag_name = (TextView) layout.findViewById(R.id.tag_friend_name);
        tag_name.setText(displayName);

        tag_name.measure(0, 0);
        final int[] width = {tag_name.getMeasuredWidth() + 20};
        final int[] height = {tag_name.getMeasuredHeight() + 10};
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width[0], height[0]);
        params.setMargins(new_width, tag_height, 0, 0);
        showTag.addView(layout, params);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < photo_data.get(0).getTagPeople().size(); i++) {
                    if (photo_data.get(0).getTagPeople().get(i).getDisplayName().equals(tag_name.getText().toString())) {
                        if (photo_data.get(0).getTagPeople().get(i).getId().equals(preferences.getString("user_id", ""))) {
                            Intent intent = new Intent(Open_photo.this, Profile.class);
                            startActivity(intent);
                        } else {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("another_user", photo_data.get(0).getTagPeople().get(i).getId());
                            editor.commit();
                            Intent intent = new Intent(Open_photo.this, Another_user.class);
                            startActivity(intent);
                        }
                    }
                }
            }
        });
    }

    //delete post
    private void delete_post() {
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "delete_post");
        postParam.put("userid", preferences.getString("user_id", ""));
        postParam.put("postid", preferences.getString("post_id", ""));

        Call<API_Response> call = service.FriendsWork(postParam);
        Log.d("API_Parameters", "delete_post parameters : " + postParam.toString());
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.e("API_Response", "delete_post Response : " + new Gson().toJson(response.body()));
                    Result result = response.body().getResult();
                    Log.d("Tag", result.getReason());
                    String msg = result.getMsg();
                    if (msg.equals("201")) {
                        if (!result.getTotal_posts().equals(Timeline.resultp.getPhotosDetails().getTotalPosts())) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("Update", result.getTotal_posts());
                            editor.commit();
//    Timeline.resultp.getProfile_details().setTotalPosts(result.getTotal_posts());

                        }
                        Intent i = new Intent(Open_photo.this, Profile.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    }
                } else
                    Log.e("API_Response", "delete_post Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                Log.d("Tag", " delete_post failure : " + t.getMessage());
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == post_points) {
            Intent intent = new Intent(this, Post_points.class);
            intent.putExtra("post_id", preferences.getString("post_id", ""));
            startActivity(intent);
            transition();
        }
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == profile_image) {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("another_user", photo_data.get(0).getUser_id());
            editor.commit();
            if (preferences.getString("user_id", "").equals(photo_data.get(0).getUser_id())) {
                Intent i = new Intent(Open_photo.this, Profile.class);
                startActivity(i);
                transition();
            } else {
                Intent i = new Intent(Open_photo.this, Another_user.class);
                startActivity(i);
                transition();
            }
        }
        if (v == post_image) {
            value++;
            if (value % 2 != 0) {
                showTag.setVisibility(View.GONE);
            } else showTag.setVisibility(View.VISIBLE);
        }
        if (v == options) {
            options.measure(0, 0);
            final Dialog dialog = new Dialog(this);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.design_options_data);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            Window window = dialog.getWindow();

//                Window dialogWindow = dialog1.getWindow();
            WindowManager.LayoutParams wmlp = window.getAttributes();
            window.setGravity(Gravity.RIGHT | Gravity.TOP);
//                wmlp.gravity = Gravity.CENTER;
            wmlp.y = options.getMeasuredHeight() + 150;   //x position
            wmlp.x = 20;   //y position
            TextView delete = (TextView) dialog.findViewById(R.id.delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(Open_photo.this);
                    alert.setMessage("Are you sure you want to delete this post.");
                    alert.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            delete_post();
                        }
                    }).show();
                }

            });
            dialog.show();
        }
        if (v == like) {
           /* if (like_view.getDrawable().getConstantState().equals(ContextCompat.getDrawable(Open_photo.this, R.drawable.like_96).getConstantState())){
                like_view.setImageResource(R.drawable.liked_96);
            }
            else
                like_view.setImageResource(R.drawable.like_96);*/
            like_post();
        }
        if (v == comment) {
            open_comments();
        }
        if (v == repost) {

            if (!photo_data.get(0).getFirst_post_id().equals("")) {
                Intent i = new Intent(Open_photo.this, Upload_photo.class);
                i.putExtra("post_id", photo_data.get(0).getFirst_post_id());
                i.putExtra("postuser_id", photo_data.get(0).getFirst_user_id());
                i.putExtra("width", vwidthScreen);
                i.putExtra("height", vNewHeight);
                i.putExtra("photo", photo_data.get(0).getPhoto());
                i.putExtra("postType", photo_data.get(0).getFileType());
                i.putExtra("video", photo_data.get(0).getVideo());
                i.putExtra("caption", photo_data.get(0).getCaption());
                startActivity(i);
            } else {
                Intent i = new Intent(Open_photo.this, Upload_photo.class);
                i.putExtra("post_id", photo_data.get(0).getPostId());
                i.putExtra("postuser_id", photo_data.get(0).getUser_id());
                i.putExtra("width", vwidthScreen);
                i.putExtra("height", vNewHeight);
                i.putExtra("photo", photo_data.get(0).getPhoto());
                i.putExtra("postType", photo_data.get(0).getFileType());
                i.putExtra("video", photo_data.get(0).getVideo());
                i.putExtra("caption", photo_data.get(0).getCaption());
                startActivity(i);
            }
        }


        if (v == back) {
            onBackPressed();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Open_photo.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Open_photo.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Open_photo.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Open_photo.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Open_photo.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Open_photo.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Open_photo.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Open_photo.this, Stats.class);
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
        if (v == menu_home) {
            Intent i = new Intent(Open_photo.this, Timeline.class);
            startActivity(i);
            transition();
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

    private void open_comments() {

        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("post_id", preferences.getString("post_id", ""));
        editor.putString("another_user", photo_data.get(0).getUser_id());
        editor.commit();
        Intent i = new Intent(Open_photo.this, Comments.class);
        startActivity(i);
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    private void like_post() {
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "like_post");
        postParam.put("postid", preferences.getString("post_id", ""));
        postParam.put("post_userid", photo_data.get(0).getUser_id());
        postParam.put("liked_userid", preferences.getString("user_id", ""));

        Log.d("API_Parameters", "like_post parameters : " + postParam.toString());
        Call<API_Response> call = service.getPostActionResponse(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.d("API_Response", "like_post Response : " + new Gson().toJson(response.body()));
                    Result result = response.body().getResult();
                    Log.d("Tag", result.getReason());
                    String msg = result.getMsg();
                    if (msg.equals("201")) {
                        String reason = result.getReason();
                        if (reason.equals("user like post successfully")) {
                            like_view.setImageResource(R.drawable.liked_96);
                        } else {
                            like_view.setImageResource(R.drawable.like_96);
                        }
                        post_points.setText(result.getTotal_post_points());
                    }
                } else
                    Log.e("API_Response", "like_post Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                Log.d("Tag", "Like post failure : " + t.getMessage());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (preferences.getString("Comment_value", "").equals("1")) {
            post_points.setText(preferences.getString("Comment_points", ""));
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("Comment_value", "0");
            edit.commit();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

}
