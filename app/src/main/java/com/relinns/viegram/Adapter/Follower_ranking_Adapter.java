package com.relinns.viegram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Fragment.Follower_ranking;
import com.relinns.viegram.Modal.FollowerDetail;
import com.relinns.viegram.Pojo.FollowerRanking;
import com.relinns.viegram.R;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by win 7 on 5/31/2017.
 */
public class Follower_ranking_Adapter extends RecyclerView.Adapter<Follower_ranking_Adapter.Viewholder> {
    private Context context;
    private List<FollowerRanking> data_list;
    private SharedPreferences preferences;

    public Follower_ranking_Adapter(Context activity, List<FollowerRanking> list) {
        this.context = activity;
        this.data_list = list;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public Viewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rank_view, parent, false);
        return new Viewholder(v);
    }

    @Override
    public void onBindViewHolder(Viewholder holder, final int position) {
        Glide.with(context).load(data_list.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.image_view);
        holder.text_rank.setText(data_list.get(position).getRank());
        holder.text_name.setText(data_list.get(position).getDisplayName());
        holder.text_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
        holder.image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(position);
            }
        });
    }

    private void open_profile(int position) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("another_user", data_list.get(position).getUserId());
        editor.commit();
        Intent i = new Intent(context, Another_user.class);
        context.startActivity(i);
    }

    @Override
    public int getItemCount() {
        return data_list.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView image_view;
        TextView text_name, text_rank;

        public Viewholder(View itemView) {
            super(itemView);
            image_view = (ImageView) itemView.findViewById(R.id.prfl_img);
            text_name = (TextView) itemView.findViewById(R.id.user_text);
            text_rank = (TextView) itemView.findViewById(R.id.rank_text);

        }
    }
}
