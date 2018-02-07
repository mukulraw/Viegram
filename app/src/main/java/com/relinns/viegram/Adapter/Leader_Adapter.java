package com.relinns.viegram.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Profile;
import com.relinns.viegram.Modal.LeaderData;
import com.relinns.viegram.R;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Leader_Adapter extends BaseAdapter {
    private Context context;
    private List<LeaderData> data_list;
    private SharedPreferences preferences;

    public Leader_Adapter(Context activity, List<LeaderData> list) {
        this.context = activity;
        this.data_list = list;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public int getCount() {
        return data_list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.design_leader, viewGroup, false);

        ImageView leader_image = (ImageView) view.findViewById(R.id.leader_image);
        TextView leader_name = (TextView) view.findViewById(R.id.leader_name);
        TextView text_type = (TextView) view.findViewById(R.id.leader_text);
        RelativeLayout line = (RelativeLayout) view.findViewById(R.id.leader_line);
        RelativeLayout line2 = (RelativeLayout) view.findViewById(R.id.leader_above_line);

        text_type.setVisibility(View.VISIBLE);
        if (data_list.get(i).getDataMsg().equals("204")) {
            text_type.setText(data_list.get(i).getMsg());
            leader_image.setVisibility(View.GONE);
            leader_name.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(data_list.get(i).getProfileImage())
                    .bitmapTransform(new CropCircleTransformation(context))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .into(leader_image);
            text_type.setText(data_list.get(i).getMsg());
            leader_name.setText(data_list.get(i).getDisplayName());
        }
        if (i == data_list.size() - 1 ) {
            line.setVisibility(View.GONE);
        }
        if (i == 0) {
            line2.setVisibility(View.GONE);
        }
        leader_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(i);
            }
        });
        leader_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                open_profile(i);
            }
        });
        return view;
    }
    private void open_profile(int i) {
        if (data_list.get(i).getUserId().equals(preferences.getString("user_id", ""))) {
            Intent intent = new Intent(context, Profile.class);
            context.startActivity(intent);
        } else {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("another_user", data_list.get(i).getUserId());
            editor.commit();
            Intent intent = new Intent(context, Another_user.class);
            context.startActivity(intent);
        }
    }
}
