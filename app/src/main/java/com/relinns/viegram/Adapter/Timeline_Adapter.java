package com.relinns.viegram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Comments;
import com.relinns.viegram.Activity.Post_points;
import com.relinns.viegram.Activity.Upload_photo;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.Modal.TagPerson;
import com.relinns.viegram.Modal.TimelinePost;
import com.relinns.viegram.Activity.Profile;
import com.relinns.viegram.R;
import com.relinns.viegram.Activity.Timeline;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.relinns.viegram.util.FileUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;
import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;

public class Timeline_Adapter extends RecyclerView.Adapter<Timeline_Adapter.ViewHolder> {
    private ViewGroup group;
    private Timeline context;
    private SharedPreferences preferences;
    private List<TimelinePost> list;
    private List<TagPerson> tag_list = new ArrayList<>();
    private int touch = 0;
    private int screenValue;
    private float width_screen;
    private float new_height;

    public Timeline_Adapter(Timeline timeline, List<TimelinePost> timelinePosts, int value) {
        this.context = timeline;
        this.list = timelinePosts;
        screenValue = value;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        group = parent;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.timeline, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.setIsRecyclable(false);
        tag_list = list.get(position).getTagPeople();

        //update comment points
        if (preferences.getString("Comment_value", "").equals("1")) {
            if (list.get(position).getRepostId().equals(preferences.getString("Comment_post", ""))) {
                list.get(position).setPostPoints(preferences.getString("Comment_points", ""));
            }
            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("Comment_value", "0");
            edit.apply();
        }

        //set data
        holder.userName.setText(list.get(position).getDisplayName());
        holder.post_points.setText(list.get(position).getPostPoints());
        try {
            if (list.get(position).getLocation()==null || list.get(position).getLocation().equals("null") || list.get(position).getLocation().equals("")) {
                holder.location_text.setVisibility(View.GONE);
            } else {
                holder.location_text.setVisibility(View.VISIBLE);
                holder.location_text.setText(list.get(position).getLocation());
            }
        }catch (Exception e){
            e.printStackTrace();
            holder.location_text.setVisibility(View.GONE);
        }
        try {
            if (list.get(position).getCaption().equals("null") || list.get(position).getCaption().equals("")||list.get(position).getCaption()==null) {
                holder.caption.setVisibility(View.GONE);
            } else {
                holder.caption.setVisibility(View.VISIBLE);
                holder.caption.setText(list.get(position).getCaption());

            }
        }catch (Exception e) {
            holder.caption.setVisibility(View.GONE);

        }

        Glide.with(context).load(list.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .into(holder.profile_image);


//set post image
        if (list.get(position).getImageWidth().equals("") || list.get(position).getImageHeight().equals("")) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            final float width = displayMetrics.widthPixels;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams((int) width, (int) width);
            holder.post_layout.setLayoutParams(params);
            holder.errorText.setVisibility(View.VISIBLE);
        } else {
            holder.errorText.setVisibility(View.GONE);



            DisplayMetrics displayMetrics = new DisplayMetrics();
            context.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            width_screen = displayMetrics.widthPixels;
            float aspect = Float.parseFloat(list.get(position).getImageWidth()) / Float.parseFloat(list.get(position).getImageHeight());
            new_height = width_screen / aspect;

            RelativeLayout.LayoutParams layoutParams;
            layoutParams = new RelativeLayout.LayoutParams((int) width_screen, (int) new_height);
            holder.post_image.setLayoutParams(layoutParams);
            holder.player.setLayoutParams(layoutParams);
            holder.player.setVisibility(View.GONE);

            if (list.get(position).getFileType().equals("video")) {

                holder.showTagLayout.setVisibility(View.GONE);
                holder.player.setVisibility(View.VISIBLE);

                Glide.with(context).load(list.get(position).getPhoto())
                        .crossFade()
                      //  .thumbnail(0.001f)
                        .diskCacheStrategy(DiskCacheStrategy.RESULT)
         //               .skipMemoryCache(true)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                            holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.player.thumbImageView);

                holder.player.setUp(list.get(position).getVideo(), JZVideoPlayer.SCREEN_WINDOW_LIST, "");
            } else {

                holder.post_image.setVisibility(View.VISIBLE);
                holder.progressBar.setVisibility(View.VISIBLE);

                Glide.with(context).load(list.get(position).getPhoto())
                        .crossFade()
                //        .thumbnail(0.001f)
                      .diskCacheStrategy(DiskCacheStrategy.RESULT)
                   //   .skipMemoryCache(true)
                        .listener(new RequestListener<String, GlideDrawable>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
                        .into(holder.post_image);

                //  holder.post_video.setVisibility(View.GONE);
                holder.player.setVisibility(View.GONE);

                holder.post_image.setVisibility(View.VISIBLE);
                holder.showTagLayout.setVisibility(View.GONE);
                holder.showTagLayout.setLayoutParams(layoutParams);
                for (int i = 0; i < tag_list.size(); i++) {
                    if (!tag_list.get(i).getId().equals("null")) {
                        float temp_width = Float.parseFloat(tag_list.get(i).getXCordinates());
                        float temp_height = Float.parseFloat(tag_list.get(i).getYCordinates());
                        float new_width = (temp_width * width_screen) / 100;
                        float tag_height = (temp_height * new_height) / 100;
                        add_name((int) new_width, (int) tag_height, tag_list.get(i).getDisplayName(), holder, position);
                    }
                }
            }

        }
        if (list.get(position).getPostLike().equals("1")) {
            holder.like_view.setImageResource(R.drawable.liked_96);
        } else {
            holder.like_view.setImageResource(R.drawable.like_96);
        }
        holder.post_time.setText(list.get(position).getTimeAgo());

        // set name
        if (list.get(position).getPostType().equals("repost post")) {

            String post_name = list.get(position).getDisplayName() + " shared " + list.get(position).getFirst_display_name() + "\'s post.";
            SpannableString spanString = new SpannableString(post_name);
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(View textView) {
                    open_firstProfile(position);
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
                    if (list.get(position).getSecondUserid().equals(preferences.getString("user_id", ""))) {
                        Intent i = new Intent(context, Profile.class);
                        context.startActivity(i);
                    } else {
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("another_user", list.get(position).getSecondUserid());
                        editor.apply();
                        Intent i = new Intent(context, Another_user.class);
                        context.startActivity(i);
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
            spanString.setSpan(clickableSpan, 0, list.get(position).getDisplayName().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spanString.setSpan(clickableSpan1, endSpan, list.get(position).getFirst_display_name().length() + 1 + endSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.user_name.setText(spanString);
            holder.user_name.setMovementMethod(LinkMovementMethod.getInstance());
        } else {
            SpannableString spanString = new SpannableString(list.get(position).getDisplayName());
            holder.user_name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    open_firstProfile(position);
                }
            });
            spanString.setSpan(new ForegroundColorSpan(Color.DKGRAY), 0,
                    spanString.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.user_name.setText(spanString);
        }

        //click listener
        holder.profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                open_firstProfile(position);
            }
        });

        holder.mPostPointView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Post_points.class);
                intent.putExtra("post_id", list.get(position).getRepostId());
                context.startActivity(intent);
            }
        });

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

             /*   if (holder.like_view.getDrawable().getConstantState().equals(ContextCompat.getDrawable(context, R.drawable.like_96).getConstantState())) {
                    holder.like_view.setImageResource(R.drawable.liked_96);
                } else
                    holder.like_view.setImageResource(R.drawable.like_96);*/
                like_post();
            }

            private void like_post() {
                GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put("action", "like_post");
                postParam.put("postid", list.get(position).getRepostId());
                postParam.put("post_userid", list.get(position).getUserid());
                postParam.put("liked_userid", preferences.getString("user_id", ""));

                Log.d("API_Response", "like_post Parameters : " + postParam.toString());
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
                                    holder.like_view.setImageResource(R.drawable.liked_96);
                                    holder.post_points.setText(result.getTotal_post_points());
                                    list.get(position).setPostPoints(result.getTotal_post_points());
                                    list.get(position).setPostLike("1");
                                } else {
                                    holder.like_view.setImageResource(R.drawable.like_96);
                                    holder.post_points.setText(result.getTotal_post_points());
                                    list.get(position).setPostPoints(result.getTotal_post_points());
                                    list.get(position).setPostLike("0");
                                }
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
        });
        holder.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("post_id", list.get(position).getRepostId());
                editor.putString("another_user", list.get(position).getUserid());
                editor.putString("Comment_value", "0");
                editor.apply();
                Intent i = new Intent(context, Comments.class);
                context.startActivity(i);
            }
        });

        //repost post
        holder.repost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).getPostType().equals("repost post")) {
                    Intent i = new Intent(context, Upload_photo.class);
                    i.putExtra("post_id", list.get(position).getPostId());
                    i.putExtra("postuser_id", list.get(position).getSecondUserid());
                    i.putExtra("postType", list.get(position).getFileType());
                    i.putExtra("width", width_screen);
                    i.putExtra("height", new_height);
                    i.putExtra("video", list.get(position).getVideo());
                    i.putExtra("photo", list.get(position).getPhoto());
                    if (list.get(position).getCaption().equals("null"))
                        i.putExtra("caption", "");
                    else
                        i.putExtra("caption", list.get(position).getCaption());
                    context.startActivity(i);
                } else {
                    Intent i = new Intent(context, Upload_photo.class);
                    i.putExtra("post_id", list.get(position).getRepostId());
                    i.putExtra("postuser_id", list.get(position).getUserid());
                    i.putExtra("postType", list.get(position).getFileType());
                    i.putExtra("width", width_screen);
                    i.putExtra("height", new_height);
                    i.putExtra("video", list.get(position).getVideo());
                    i.putExtra("photo", list.get(position).getPhoto());
                    if (list.get(position).getCaption().equals("null"))
                        i.putExtra("caption", "");
                    else
                        i.putExtra("caption", list.get(position).getCaption());
                    context.startActivity(i);
                }
//                final Dialog dialog = new Dialog(context);
//                dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//                dialog.setContentView(R.layout.repost_dialog);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                RelativeLayout repost_button = (RelativeLayout) dialog.findViewById(R.id.repost_button);
//                RelativeLayout cancel = (RelativeLayout) dialog.findViewById(R.id.cancel);
//                cancel.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//                    }
//                });
//                repost_button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        dialog.dismiss();
//
//                    }
//                });
//                dialog.show();
            }
        });

        holder.post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                touch++;
                if (touch % 2 == 0) {
                    holder.showTagLayout.setVisibility(View.GONE);
                } else {
                    holder.showTagLayout.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profile_image, post_image, like_view;
        TextView user_name, post_points, caption, userName, post_time, location_text, errorText;
        RelativeLayout like, comment, repost, post_layout, showTagLayout;
        View mPostPointView;

        private cn.jzvd.JZVideoPlayerStandard player;
        ProgressBar progressBar;

        public ViewHolder(View itemView) {
            super(itemView);
            profile_image = itemView.findViewById(R.id.user_image);
            post_image = itemView.findViewById(R.id.post_image);
            like_view = itemView.findViewById(R.id.like_view);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            repost = itemView.findViewById(R.id.repost);
            user_name = itemView.findViewById(R.id.username_text);
            post_points = itemView.findViewById(R.id.post_points);
            caption = itemView.findViewById(R.id.caption);
            userName = itemView.findViewById(R.id.username_post);
            post_time = itemView.findViewById(R.id.post_time);
            location_text = itemView.findViewById(R.id.location_text);
            player = itemView.findViewById(R.id.player);
            errorText = itemView.findViewById(R.id.image_error_text);

            mPostPointView = itemView.findViewById(R.id.post_point_view);


            //  post_video = (VideoView) itemView.findViewById(R.id.post_video);
            post_layout = itemView.findViewById(R.id.post_layout);
            showTagLayout = itemView.findViewById(R.id.show_tag);
            progressBar = itemView.findViewById(R.id.progress);
        }
    }

    //open first user profile
    private void open_firstProfile(int position) {
        if (list.get(position).getUserid().equals(preferences.getString("user_id", ""))) {
            Intent i = new Intent(context, Profile.class);
            context.startActivity(i);
            context.overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("another_user", list.get(position).getUserid());
            editor.commit();
            Intent i = new Intent(context, Another_user.class);
            context.startActivity(i);
            context.overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    //show tagged person
    private void add_name(int new_width, int new_height, String name, ViewHolder holder, final int position) {
        final View layout = LayoutInflater.from(context).inflate(R.layout.tag_layout, group, false);
        final TextView tag_name = layout.findViewById(R.id.tag_friend_name);
        tag_name.setText(name);

        tag_name.measure(0, 0);
        final int[] width = {tag_name.getMeasuredWidth() + 20};
        final int[] height = {tag_name.getMeasuredHeight() + 10};
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width[0], height[0]);
        params.setMargins(new_width, new_height, 0, 0);
        holder.showTagLayout.addView(layout, params);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < list.get(position).getTagPeople().size(); i++) {
                    if (list.get(position).getTagPeople().get(i).getDisplayName().equals(tag_name.getText().toString())) {
                        if (tag_list.get(i).getId().equals(preferences.getString("user_id", ""))) {
                            Intent intent = new Intent(context, Profile.class);
                            context.startActivity(intent);
                        } else {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("another_user", list.get(position).getTagPeople().get(i).getId());
                            editor.commit();
                            Intent intent = new Intent(context, Another_user.class);
                            context.startActivity(intent);
                        }
                    }
                }
            }
        });
    }
}
