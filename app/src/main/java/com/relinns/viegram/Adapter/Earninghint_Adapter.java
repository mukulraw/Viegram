package com.relinns.viegram.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.relinns.viegram.Modal.Point_Model;
import com.relinns.viegram.Pojo.BreakdownPoint;
import com.relinns.viegram.Pojo.Hint;
import com.relinns.viegram.R;

import java.util.List;

/**
 * Created by win 7 on 5/30/2017.
 */
public class Earninghint_Adapter extends RecyclerView.Adapter<Earninghint_Adapter.View_holder> {
    private Context context;
    private List<Hint> data_point;

    public Earninghint_Adapter(Context activity, List<Hint> hint_data) {
        this.context = activity;
        this.data_point = hint_data;
    }

    @Override
    public View_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.earning_hint, parent, false);
        return new View_holder(v);
    }

    @Override
    public void onBindViewHolder(View_holder holder, int position) {
        if (data_point.get(position).getEarningHints().contains("*")) {
            String first = data_point.get(position).getEarningHints().substring(0, data_point.get(position).getEarningHints().indexOf("*"));
            String second = data_point.get(position).getEarningHints().substring(data_point.get(position).getEarningHints().indexOf("*") + 1);
            holder.name.setText(first);
            holder.hint.setText(second);
        } else {
            holder.name.setText(data_point.get(position).getEarningHints());
        }
        holder.points.setText(data_point.get(position).getAmount());
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
