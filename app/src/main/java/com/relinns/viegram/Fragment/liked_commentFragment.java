package com.relinns.viegram.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.relinns.viegram.Adapter.Likecomment_Adapter;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.R;

@SuppressWarnings("ALL")
public class liked_commentFragment extends Fragment {

    private RecyclerView list;
    private Likecomment_Adapter adapter;
    private RelativeLayout no_data_found;
    private ImageView no_data_view;
    private TextView no_data_text;
    private static Result commentLikedResult;

    public static void getInstance(Result result) {
        commentLikedResult = result;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_point_details, container, false);
        list = (RecyclerView) v.findViewById(R.id.detail_list);
        no_data_found = (RelativeLayout) v.findViewById(R.id.no_data_found);
        no_data_view = (ImageView) v.findViewById(R.id.no_data_view);
        no_data_text = (TextView) v.findViewById(R.id.no_data_text);

        if (!commentLikedResult.getMycommentCounter().equals("0")) {
            no_data_found.setVisibility(View.GONE);
            list.setVisibility(View.VISIBLE);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            list.setLayoutManager(mLayoutManager);
            adapter = new Likecomment_Adapter(getActivity(), commentLikedResult.getMyComments());
            list.setAdapter(adapter);
        }
        else {
            no_data_found.setVisibility(View.VISIBLE);
            list.setVisibility(View.GONE);
            no_data_view.setImageDrawable(getResources().getDrawable(R.drawable.comment_240));
            no_data_text.setText(getResources().getString(R.string.no_comment_like));
        }
        return v;
    }
}
