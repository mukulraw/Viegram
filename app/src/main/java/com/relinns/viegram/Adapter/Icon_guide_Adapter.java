package com.relinns.viegram.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.relinns.viegram.Pojo.IconDetail;
import com.relinns.viegram.R;
import java.util.List;

public class Icon_guide_Adapter extends RecyclerView.Adapter<Icon_guide_Adapter.View_holder> {
    private Context context;
    private List<IconDetail> icon_data;
    public Icon_guide_Adapter(Context options, List<IconDetail> list) {
        this.context=options;
        this.icon_data=list;
    }

    @Override
    public View_holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.guide_list,parent,false);
        return new View_holder(v);
    }

    @Override
    public void onBindViewHolder(View_holder holder, final int position) {
        Glide.with(context).load(icon_data.get(position).getIcons())
                .into(holder.icon_view);
        holder.guide_text.setText(icon_data.get(position).getDetail());
    }

    @Override
    public int getItemCount() {
        return icon_data.size();
    }

    public class View_holder extends RecyclerView.ViewHolder {
        ImageView icon_view;
        TextView guide_text;
        RelativeLayout guide_layout;
        public View_holder(View itemView) {
            super(itemView);
            icon_view=(ImageView)itemView.findViewById(R.id.guide_icon);
            guide_text=(TextView)itemView.findViewById(R.id.guide_text);
            guide_layout=(RelativeLayout)itemView.findViewById(R.id.guide_layout);
        }
    }
}
