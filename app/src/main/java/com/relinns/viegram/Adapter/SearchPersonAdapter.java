package com.relinns.viegram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Search;
import com.relinns.viegram.Modal.CommentPost;
import com.relinns.viegram.R;
import java.util.List;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class SearchPersonAdapter extends RecyclerView.Adapter<SearchPersonAdapter.View_Holder> {
    private Search context;
    private List<CommentPost> list;
    private SharedPreferences preferences;

    public SearchPersonAdapter(Search search, List<CommentPost> name_data) {
        this.context = search;
        this.list = name_data;
        preferences = context.getSharedPreferences("Viegram", Context.MODE_PRIVATE);
    }

    @Override
    public SearchPersonAdapter.View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.design_results_name, parent, false);
        return new View_Holder(v);
    }

    @Override
    public void onBindViewHolder(SearchPersonAdapter.View_Holder holder, final int position) {
        Glide.with(context).load(list.get(position).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(holder.displayImage);
        holder.userName.setText(list.get(position).getDisplayName());
        holder.searchedUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(context.activity_layout.getWindowToken(), 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("another_user", list.get(position).getUserid());
                editor.commit();
                Intent intent = new Intent(context, Another_user.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class View_Holder extends RecyclerView.ViewHolder {
        ImageView displayImage;
        TextView userName;
        RelativeLayout searchedUser;

        public View_Holder(View itemView) {
            super(itemView);
            displayImage = (ImageView) itemView.findViewById(R.id.display_image);
            userName = (TextView) itemView.findViewById(R.id.name_text);
            searchedUser = (RelativeLayout) itemView.findViewById(R.id.searched_user);
        }
    }
}
