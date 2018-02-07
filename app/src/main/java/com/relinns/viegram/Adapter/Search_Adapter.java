package com.relinns.viegram.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.gson.Gson;
import com.relinns.viegram.Modal.CommentPost;
import com.relinns.viegram.R;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class Search_Adapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<CommentPost> data;
    private int designed_layout;
    private Filter filter = new CustomFilter();
    private List<CommentPost> new_list = new ArrayList<>();

    public Search_Adapter(Context search, int design_results_name, List<CommentPost> name_data) {
        context = search;
        designed_layout = design_results_name;
        data = name_data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CommentPost getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(designed_layout, null);
        }
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.searched_user);
        ImageView display_image = (ImageView) v.findViewById(R.id.display_image);
        TextView searched_name = (TextView) v.findViewById(R.id.name_text);
        searched_name.setText(data.get(i).getDisplayName());
        Glide.with(context).load(data.get(i).getProfileImage())
                .bitmapTransform(new CropCircleTransformation(context))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(display_image);
        return v;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private class CustomFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            new_list.clear();

            if (data != null && charSequence != null) { // Check if the Original List and Constraint aren't null.
                for (int i = 0; i < data.size(); i++) {
                    if (data.get(i).getDisplayName().toLowerCase().contains(charSequence)) { // Compare item in original list if it contains constraints.
                        Log.d("API_", "filter :" + new Gson().toJson(data.get(i)));
                        new_list.add(data.get(i)); // If TRUE add item in Suggestions.
                    }
                }
            }
            FilterResults results = new FilterResults(); // Create new Filter Results and return this to publishResults;
            results.values = new_list;
            results.count = new_list.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            if (filterResults.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }
    }
}
