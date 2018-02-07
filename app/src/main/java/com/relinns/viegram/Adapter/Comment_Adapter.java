package com.relinns.viegram.Adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Comment_like;
import com.relinns.viegram.Activity.Comments;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Activity.Profile;
import com.relinns.viegram.Modal.Comment_Model;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Comment_Adapter extends RecyclerView.Adapter<Comment_Adapter.View_holder> {
    private Comments context;
    private List<Comment_Model> data;
    private SharedPreferences preferences;
    private ProgressDialog progressDialog;

    public Comment_Adapter(Comments comments, List<Comment_Model> list) {
        this.context = comments;
        data = list;
        progressDialog = new ProgressDialog(context);
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public View_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment, parent, false);
        return new View_holder(v);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final View_holder holder, int position) {
        Glide.with(context).load(data.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.user_image);

        SpannableString spanString = new SpannableString(data.get(position).getComments());
        for (int k = 0; k < data.get(position).getMentionPeople().size(); k++) {
            final int finalK = k;
            if (data.get(position).getComments().contains("@" + data.get(position).getMentionPeople().get(k).getDisplayName())) {
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                        if (data.get(holder.getAdapterPosition()).getMentionPeople().get(finalK).getId().equals(preferences.getString("user_id", ""))) {
                            Intent i = new Intent(context, Profile.class);
                            context.startActivity(i);
                            context.overridePendingTransition(R.anim.enter, R.anim.exit);
                        } else {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putString("another_user", data.get(holder.getAdapterPosition()).getMentionPeople().get(finalK).getId());
                            editor.commit();
                            Intent i = new Intent(context, Another_user.class);
                            context.startActivity(i);
                            context.overridePendingTransition(R.anim.enter, R.anim.exit);
                        }
                    }

                    @Override
                    public void updateDrawState(TextPaint ds) {
                        super.updateDrawState(ds);
                        ds.setStyle(Paint.Style.FILL_AND_STROKE);
                        ds.setUnderlineText(false);
                    }
                };
                int fromIndex = 0, startSpan = 0, endSpan = 0;
                startSpan = data.get(position).getComments().indexOf(data.get(position).getMentionPeople().get(k).getDisplayName(), fromIndex);
                if (startSpan == -1) {
                    startSpan = 0;
                    break;
                }
                endSpan = startSpan + data.get(position).getMentionPeople().get(k).getDisplayName().length();
                spanString.setSpan(clickableSpan, startSpan, endSpan, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        holder.comment.setText(spanString);
        holder.comment.setMovementMethod(LinkMovementMethod.getInstance());
        if (data.get(position).getLike().equals("1")) {
            holder.like.setImageResource(R.drawable.liked_96);
        } else {
            holder.like.setImageResource(R.drawable.like_96);
        }
        holder.name.setText(data.get(position).getDisplayName());
        holder.time.setText(data.get(position).getTimeAgo());

        if (data.get(position).getTotalLikes().equals("0 points")) {
            holder.points.setVisibility(View.GONE);
            holder.point_text.setVisibility(View.GONE);
        } else {
            holder.point_text.setText(data.get(position).getTotalLikes());
        }
        holder.point_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, Comment_like.class);
                intent.putExtra("comment_id", data.get(holder.getAdapterPosition()).getCommentId());
                context.startActivity(intent);
            }
        });
        holder.name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(holder.getAdapterPosition());

            }
        });
        holder.user_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(holder.getAdapterPosition());
            }
        });
        holder.like_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                like_comment();
            }

            private void like_comment() {
                Map<String, String> postParam = new HashMap<String, String>();
                postParam.put("action", "like_comment");
                postParam.put("comment_likeby", preferences.getString("user_id", ""));
                postParam.put("postid", data.get(holder.getAdapterPosition()).getPostid());
                postParam.put("comment_userid", data.get(holder.getAdapterPosition()).getCommentUserid());
                postParam.put("commentid", data.get(holder.getAdapterPosition()).getCommentId());
                GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
                Log.d("API_Parameters", "like_comment parameters :" + postParam.toString());
                Call<API_Response> call = service.getPostActionResponse(postParam);
                call.enqueue(new Callback<API_Response>() {
                    @Override
                    public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                        Log.e("API_Response", "like_comment Response : " + new Gson().toJson(response.body()));
                        if (response.isSuccessful()) {
                            if (response.body().getResult().getMsg().equals("201")) {
                                String total_comment_points = response.body().getResult().getTotal_comment_points();
                                if (response.body().getResult().getReason().equals("user dislike comment successfully")) {
                                    holder.like.setImageResource(R.drawable.like_96);
                                    data.get(holder.getAdapterPosition()).setLike("0");
                                    if (total_comment_points.equals("0 points") || total_comment_points.equals(" points")) {
                                        holder.point_text.setVisibility(View.GONE);
                                        holder.points.setVisibility(View.GONE);
                                    } else {
                                        holder.point_text.setVisibility(View.VISIBLE);
                                        holder.points.setVisibility(View.VISIBLE);
                                        holder.point_text.setText(total_comment_points);
                                    }
                                } else {
                                    holder.points.setVisibility(View.VISIBLE);
                                    holder.point_text.setVisibility(View.VISIBLE);
                                    data.get(holder.getAdapterPosition()).setLike("1");
                                    holder.point_text.setText(total_comment_points);
                                    holder.like.setImageResource(R.drawable.liked_96);
                                }
                            }
                        } else
                            Log.e("API_Response", "like_comment Response : " + new Gson().toJson(response.errorBody()));
                    }

                    @Override
                    public void onFailure(Call<API_Response> call, Throwable t) {
                        Log.d("API_Error", "like_comment Error : " + t.getMessage());
                    }
                });
            }
        });


        holder.posted_comment_layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (data.get(holder.getAdapterPosition()).getCommentUserid().equals(preferences.getString("user_id", "")) || data.get(holder.getAdapterPosition()).getPostUserid().equals(preferences.getString("user_id", ""))) {
                    final Dialog dialog = new Dialog(context);
                    dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.design_options_data);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    TextView delete = (TextView) dialog.findViewById(R.id.delete);
                    delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            progressDialog.show();
                            delete_comment(holder.getAdapterPosition());
                        }

                    });
                    dialog.show();
                }
                return false;
            }
        });
    }

    private void open_profile(int position) {
        if (data.get(position).getCommentUserid().equals(preferences.getString("user_id", ""))) {
            Intent i = new Intent(context, Profile.class);
            context.startActivity(i);
            context.overridePendingTransition(R.anim.enter, R.anim.exit);
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("another_user", data.get(position).getCommentUserid());
            editor.commit();
            Intent i = new Intent(context, Another_user.class);
            context.startActivity(i);
            context.overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    private void delete_comment(int position) {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "delete_comment");
        postParam.put("post_userid", data.get(position).getPostUserid());
        postParam.put("comment_userid", data.get(position).getCommentUserid());
        postParam.put("comment_id", data.get(position).getCommentId());
        postParam.put("postid", data.get(position).getPostid());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "delete_comment parameters :" + postParam.toString());
        Call<API_Response> call = service.FriendsWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                Log.e("API_Response", "delete_comment Response : " + new Gson().toJson(response.body()));
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    context.get_comments();
                } else {
                    Alerter.create(context)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "delete_comment Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progressDialog.dismiss();
                Alerter.create(context)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "delete_comment Error : " + t.getMessage());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class View_holder extends RecyclerView.ViewHolder {
        ImageView user_image, like, points;
        TextView name, comment, time, point_text;
        RelativeLayout like_comment, posted_comment_layout;

        public View_holder(View itemView) {
            super(itemView);
            like_comment = (RelativeLayout) itemView.findViewById(R.id.like_comment);
            posted_comment_layout = (RelativeLayout) itemView.findViewById(R.id.posted_comment_layout);
            user_image = (ImageView) itemView.findViewById(R.id.comment_user);
            like = (ImageView) itemView.findViewById(R.id.like_comnt_view);
            points = (ImageView) itemView.findViewById(R.id.point_view);
            name = (TextView) itemView.findViewById(R.id.comment_name);
            comment = (TextView) itemView.findViewById(R.id.comment_text);
            time = (TextView) itemView.findViewById(R.id.comment_time);
            point_text = (TextView) itemView.findViewById(R.id.liked_points);
        }
    }
}
