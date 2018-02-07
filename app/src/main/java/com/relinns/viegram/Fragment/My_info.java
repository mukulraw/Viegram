package com.relinns.viegram.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.relinns.viegram.Modal.Detail;
import com.relinns.viegram.R;

import static android.content.Context.MODE_PRIVATE;

@SuppressWarnings("ALL")
public class My_info extends Fragment {
    private TextView display_name;
    private TextView score_point;
    private TextView point_status;
    private TextView link;
    private TextView bio_data;
    private int status;
    private boolean mCheck,mCheck1;
    private static Detail info_detail=null;
    private SharedPreferences preferences;

    public My_info(boolean mCheck1) {
        this.mCheck1= mCheck1;
    }

    public static void getInstance(Detail detail) {
        info_detail = detail;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.info, container, false);

        display_name = (TextView) v.findViewById(R.id.full_name);
        score_point = (TextView) v.findViewById(R.id.score_point);
        point_status = (TextView) v.findViewById(R.id.user_status);
        link = (TextView) v.findViewById(R.id.user_link);
        bio_data = (TextView) v.findViewById(R.id.user_bio);

        preferences = getActivity().getSharedPreferences("Viegram", MODE_PRIVATE);
        if (mCheck1){
            Gson gson = new Gson();
            String data = preferences.getString("detail","");
            if (!(data.isEmpty()))
                info_detail =  gson.fromJson(data, Detail.class);
        }
        updateView();

        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(link.getText().toString().isEmpty())){
                    String url_open = link.getText().toString();
                    if (!url_open.startsWith("http://") && !url_open.startsWith("https://"))
                        url_open = "http://" + url_open;

                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url_open));
                    startActivity(browserIntent);
                }
            }
        });
        return v;
    }
    public void updateViewCheck(Detail detail){
        if (!mCheck){
            info_detail = detail;
            updateView();
        }
    }

    private void updateView() {
        if (info_detail!=null){
            mCheck = true;

            status = Integer.parseInt(info_detail.getScorepoint());
            display_name.setText(info_detail.getFullName());
            score_point.setText(info_detail.getScorepoint());
            link.setText(info_detail.getLink());
            bio_data.setText(info_detail.getBioData());


            if (0 <= status && status <= 24999) {
                point_status.setText("Neophyte");
                point_status.setTextColor(getResources().getColor(R.color.stats_color));
                score_point.setTextColor(getResources().getColor(R.color.stats_color));


            } else if (25000 <= status && status <= 99999) {
                point_status.setText("Bonafide");

                point_status.setTextColor(getResources().getColor(R.color.login_bg));
                score_point.setTextColor(getResources().getColor(R.color.login_bg));
            } else if (100000 <= status && status <= 999999) {
                point_status.setText("Socialite");

                point_status.setTextColor(getResources().getColor(R.color.socialite));
                score_point.setTextColor(getResources().getColor(R.color.socialite));
            } else if (1000000 <= status) {
                point_status.setText("Icon");

                point_status.setTextColor(getResources().getColor(R.color.icon));
                score_point.setTextColor(getResources().getColor(R.color.icon));

            }
        }else{
            mCheck = false;
            info_detail = new Detail();
        }
    }
}
