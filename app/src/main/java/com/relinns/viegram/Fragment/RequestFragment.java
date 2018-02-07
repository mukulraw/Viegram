package com.relinns.viegram.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.relinns.viegram.Adapter.RequestAdapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Notification;
import com.relinns.viegram.Modal.Result;
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

public class RequestFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private Result result;
    private RecyclerView requestList;
    private RelativeLayout noNotifications;
    private RelativeLayout progress_layout;
    private LinearLayout loadMore;
    private LinearLayoutManager mLayoutManager;
    private boolean loading = true;
    private int index = 1;
    private SharedPreferences preferences;
    private RequestAdapter requestAdapter;
    private List<Notification> notificationData = new ArrayList<>();
    private SwipeRefreshLayout refreshLayout;
    private TextView noRequestTExt;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_notification, container, false);
        preferences = getActivity().getSharedPreferences("Viegram", Context.MODE_PRIVATE);
        requestList = (RecyclerView) v.findViewById(R.id.notification_list);
        noNotifications = (RelativeLayout) v.findViewById(R.id.no_notification);
        loadMore = (LinearLayout) v.findViewById(R.id.load_moreData);
        progress_layout = (RelativeLayout) v.findViewById(R.id.progress_layout);
        refreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.notification_swipeRefresh);
        refreshLayout.setOnRefreshListener(this);
        noRequestTExt = (TextView) v.findViewById(R.id.no_notification_text);
        progress_layout.setVisibility(View.VISIBLE);
        getRequests();

        return v;
    }

    public void getRequests() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "fetch_notification");
        postParam.put("page", "" + index);
        postParam.put("type", "2");
        postParam.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "fetch_notification parameters :" + postParam.toString());
        Call<API_Response> call = service.getTimeline(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                refreshLayout.setRefreshing(false);
                progress_layout.setVisibility(View.GONE);

                if (response.isSuccessful()) {
                    Log.d("API_Response", "fetch_notification Response : " + new Gson().toJson(response.body()));
                    result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        noNotifications.setVisibility(View.GONE);
                        requestList.setVisibility(View.VISIBLE);
                        notificationData = result.getNotification();

                        mLayoutManager = new LinearLayoutManager(getActivity());
                        requestList.setLayoutManager(mLayoutManager);
                        requestAdapter = new RequestAdapter(notificationData, RequestFragment.this);
                        requestList.setAdapter(requestAdapter);

                        requestList.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                            }

                            @Override
                            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                if (dy > 0) {

                                    int visibleItemCount = mLayoutManager.getChildCount();
                                    int totalItemCount = mLayoutManager.getItemCount();
                                    int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                                    if (Integer.parseInt(result.getTotalRecords()) > totalItemCount) {
                                        if (loading) {
                                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount) {
                                                loading = false;
                                                Log.v("P_new data", "Last Item Wow !");
                                                //Do pagination.. i.e. fetch new data
                                                index++;
                                                load_more(index);
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                    else if (result.getMsg().equals("204")) {
                        noNotifications.setVisibility(View.VISIBLE);
                        requestList.setVisibility(View.GONE);
                        noRequestTExt.setText(getResources().getString(R.string.no_request));
                    }
                    else {
                        noNotifications.setVisibility(View.GONE);
                        requestList.setVisibility(View.GONE);
                        if(getActivity()!=null)
                        {
                            Alerter.create(getActivity())
                                    .setText(R.string.network_error)
                                    .setBackgroundColor(R.color.login_bg)
                                    .show();
                        }
                    }

                }
                else {
                    noNotifications.setVisibility(View.GONE);
                    requestList.setVisibility(View.GONE);
                    if(getActivity()!=null)
                    {
                        Alerter.create(getActivity())
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                    Log.e("API_Response", "fetch_notification Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                refreshLayout.setRefreshing(false);

                progress_layout.setVisibility(View.GONE);
                noNotifications.setVisibility(View.GONE);
                requestList.setVisibility(View.GONE);
                if(getActivity()!=null)
                {
                    Alerter.create(getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }
            }
        });
    }

    private void load_more(int index) {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "fetch_notification");
        postParam.put("page", "" + index);
        postParam.put("type", "1");
        postParam.put("userid", preferences.getString("user_id", ""));

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "fetch_notification parameters :" + postParam.toString());
        Call<API_Response> call = service.getTimeline(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                loadMore.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.d("API_Response", "fetch_notification Response : " + new Gson().toJson(response.body()));
                    notificationData.addAll(response.body().getResult().getNotification());
                    requestAdapter.notifyDataSetChanged();

                    loading = true;
                } else{
                    if(getActivity()!=null)
                    {
                        Alerter.create(getActivity())
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                    Log.e("API_Response", "fetch_notification Response : " + new Gson().toJson(response.errorBody()));
            }}

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                loadMore.setVisibility(View.GONE);
                if(getActivity()!=null)
                {
                    Alerter.create(getActivity())
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        index = 1;
        getRequests();
    }
}
