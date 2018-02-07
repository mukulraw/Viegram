package com.relinns.viegram.Fragment;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.relinns.viegram.Adapter.Photo_Adapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Detail;
import com.relinns.viegram.Pojo.Post;
import com.relinns.viegram.Pojo.UserData;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static android.content.Context.MODE_PRIVATE;

@SuppressLint("ValidFragment")
public class My_photos extends Fragment {
    private Photo_Adapter photo_adapter;
    private RecyclerView my_photos;
    private GridLayoutManager layoutManager;
    private SharedPreferences preferences;
    private RelativeLayout no_photos;
    private RelativeLayout private_account_layout;
    private static Detail detail=null;
    private static String profile="";
    private int index = 1;
    private boolean loading = true;
    private LinearLayout load_more;
    private List<Post> list = new ArrayList<>();
    private boolean mCheck, mCheck1;

    @SuppressLint("ValidFragment")
    public My_photos(boolean mCheck1, String s) {
        this.mCheck1 = mCheck1;
        profile = s;
    }

    public static void getInstance(Detail details, String s) {
        detail = details;
        profile = s;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.my_photos, container, false);
        no_photos = v.findViewById(R.id.no_photos);
        my_photos = v.findViewById(R.id.my_photos);
        load_more = v.findViewById(R.id.load_moreData);
        private_account_layout = v.findViewById(R.id.private_account_layout);
        private_account_layout.setVisibility(View.GONE);
        no_photos.setVisibility(View.GONE);

        preferences = getActivity().getSharedPreferences("Viegram", MODE_PRIVATE);
        if (mCheck1){
            Gson gson = new Gson();
            String data = preferences.getString("detail","");
            if (!(data.isEmpty()))
                detail =  gson.fromJson(data, Detail.class);
        }
        updateView();

        my_photos.addOnScrollListener(
                new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        if (dy > 0) {

                            int visibleItemCount = layoutManager.getChildCount();
                            int totalItemCount = layoutManager.getItemCount();
                            int pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();
                            if (Integer.parseInt(detail.getTotalPosts()) > totalItemCount) {
                                if (loading) {

                                    if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                        load_more.setVisibility(View.VISIBLE);
                                        loading = false;
                                        Log.v("P_new data", "Last Item Wow !");
                                        //Do pagination.. i.e. fetch new data
                                        index++;
                                        if (profile.equals("0")) {
                                            load_morephotos(index);
                                        } else {
                                            load_moreData(index);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                    }
                }
        );
        return v;
    }

    private void setData() {
        if (detail!=null){
            if ((detail.getFollowerStatus().equals("1") || detail.getPrivacyStatus().equals("0")) && !detail.getTotalPosts().equals("0")) {
                my_photos.setVisibility(View.VISIBLE);
                no_photos.setVisibility(View.GONE);
                layoutManager = new GridLayoutManager(getActivity(), 4);
                my_photos.setHasFixedSize(true);

                my_photos.setLayoutManager(layoutManager);
                photo_adapter = new Photo_Adapter(getActivity(), list);
                my_photos.setAdapter(photo_adapter);
            } else if ((detail.getFollowerStatus().equals("1") || detail.getPrivacyStatus().equals("0")) && detail.getTotalPosts().equals("0")) {
                private_account_layout.setVisibility(View.GONE);
                my_photos.setVisibility(View.GONE);
                no_photos.setVisibility(View.VISIBLE);
            } else if (!detail.getFollowerStatus().equals("1") && detail.getPrivacyStatus().equals("1")) {
                private_account_layout.setVisibility(View.VISIBLE);
                my_photos.setVisibility(View.GONE);
                no_photos.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (profile.equals("0")) {
                setData();
            }
        }

    }


    private void load_morephotos(int index) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "fetch_user_profile");
        postParams.put("userid", preferences.getString("user_id", ""));
        postParams.put("userid2", preferences.getString("another_user", ""));
        postParams.put("page", "" + index);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "fetch_user_profile parameters :" + postParams.toString());
        Call<API_Response> call = service.FriendsWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                load_more.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "fetch_user_profile Response : " + new Gson().toJson(response.body()));
                    list.addAll(response.body().getResult().getProfile_details().getPosts());
                    photo_adapter.notifyDataSetChanged();

                    loading = true;
                } else {
                    Alerter.create(getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "fetch_user_profile Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                load_more.setVisibility(View.GONE);
                Alerter.create(getActivity())
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
            }
        });
    }

    private void load_moreData(int index) {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "fetch_profile");
        postParams.put("user_id", preferences.getString("user_id", ""));
        postParams.put("page", "" + index);

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "fetch_profile parameters :" + postParams.toString());
        Call<UserData> call = service.FetchProfile(postParams);
        call.enqueue(new Callback<UserData>() {
            @Override
            public void onResponse(Call<UserData> call, retrofit2.Response<UserData> response) {
                load_more.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "fetch_profile Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        list.addAll(response.body().getResult().getProfile_details().getPosts());
                        photo_adapter.notifyDataSetChanged();

                        loading = true;
                    }
                } else {
                    if (getActivity() != null) {
                        Alerter.create(getActivity())
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                    Log.e("API_Response", "fetch_profile Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<UserData> call, Throwable t) {
                load_more.setVisibility(View.GONE);

                if (getActivity() != null) {
                    Alerter.create(getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }
            }
        });
    }

    public void updateViewCheck(Detail details, String s) {
        if (!(mCheck)){
            detail = details;
            profile = s;
            updateView();
        }
    }

    private void updateView() {
        if (detail!=null){
            mCheck = true;
            list = detail.getPosts();
            if (profile.equals("0")) {
                setData();
            } else {
                if (detail.getPosts().size() != 0) {
                    my_photos.setVisibility(View.VISIBLE);
                    no_photos.setVisibility(View.GONE);
                    private_account_layout.setVisibility(View.GONE);

                    layoutManager = new GridLayoutManager(getActivity(), 4);
                    my_photos.setHasFixedSize(true);
                    my_photos.setLayoutManager(layoutManager);
                    photo_adapter = new Photo_Adapter(getActivity(), list);
                    my_photos.setAdapter(photo_adapter);
                } else {
                    private_account_layout.setVisibility(View.GONE);
                    my_photos.setVisibility(View.GONE);
                    no_photos.setVisibility(View.VISIBLE);
                }
            }
        }else {
            mCheck = false;
            detail = new Detail();
        }
    }
}

