package com.relinns.viegram.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.relinns.viegram.Activity.Stats_Details;
import com.relinns.viegram.Modal.Point_Model;
import com.relinns.viegram.R;

import java.util.List;

public class StatDetailAdapter extends RecyclerView.Adapter<StatDetailAdapter.View_holder> {
    private Context context;
    private List<Point_Model> data_point;

    public StatDetailAdapter(Stats_Details stats_details, List<Point_Model> points) {
        this.context = stats_details;
        this.data_point = points;
    }

    @Override
    public View_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.earning_hint, parent, false);
        return new View_holder(v);
    }

    @Override
    public void onBindViewHolder(View_holder holder, int position) {
        holder.name.setText(data_point.get(position).getEarningHints());
        holder.points.setText(data_point.get(position).getPoints());
    }

    @Override
    public int getItemCount() {
        return data_point.size();
    }

    public class View_holder extends RecyclerView.ViewHolder {
        TextView name, points, hint;

        public View_holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            points = (TextView) itemView.findViewById(R.id.points);
            hint = (TextView) itemView.findViewById(R.id.name_hint);
        }
    }
}
