package com.relinns.viegram.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
import com.google.gson.Gson;
import com.relinns.viegram.Adapter.PlaceAutoCompleteAdapter;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Search_location extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks
{
    private RelativeLayout badgeLayout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
    private RelativeLayout search_location_button;
    private RelativeLayout menu_home;
    private RelativeLayout menu_open_layout;
    private RelativeLayout menu_close;
    private RelativeLayout menu_profile;
    private RelativeLayout menu_stat;
    private RelativeLayout menu_follow;
    private RelativeLayout menu_notifications;
    private RelativeLayout menu_settings;
    private RelativeLayout menu_search;
    private RelativeLayout menu_ranking;
    private RelativeLayout menu_camera;
    private ImageView menu_click_view;
    private AutoCompleteTextView search_location_text;
    private ProgressBar progress;
    private Button done;
    private ListView result_list;
    private TextView badgeText;
    private SharedPreferences preferences;
    private List<String> list_data;
    private ArrayAdapter adapter;
    private PlaceAutoCompleteAdapter completeAdapter;
    private static final int GOOGLE_API_CLIENT_ID = 0;
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);
        list_data = new ArrayList<>();
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        mGoogleApiClient = new GoogleApiClient.Builder(Search_location.this)
                .addApi(Places.GEO_DATA_API)
                .enableAutoManage(this, GOOGLE_API_CLIENT_ID, this)
                .addConnectionCallbacks(this)
                .build();
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        progress = (ProgressBar) findViewById(R.id.progress);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        done = (Button) findViewById(R.id.done);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        result_list = (ListView) findViewById(R.id.search_location_result);
        search_location_button = (RelativeLayout) findViewById(R.id.search_location_button);
        search_location_text = (AutoCompleteTextView) findViewById(R.id.search_location_text);
        back = (RelativeLayout) findViewById(R.id.back);
        menu_open_layout = (RelativeLayout) findViewById(R.id.location_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.location_menu_click);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);

        if (ContextCompat.checkSelfPermission(Search_location.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(Search_location.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Search_location.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION}, Integer.parseInt("123"));

        }
        if (getIntent().getExtras()!=null)
        {
           if( getIntent().getExtras().containsKey("location"))
               search_location_text.setText(getIntent().getStringExtra("location"));
            search_location_text.setSelection(search_location_text.getText().toString().length());
        }
        menu_open_layout.setVisibility(View.GONE);
        menu_home.setOnClickListener(this);
        back.setOnClickListener(this);
        done.setOnClickListener(this);
        menu_follow.setOnClickListener(this);
        menu_ranking.setOnClickListener(this);
        menu_open_layout.setOnClickListener(this);
        menu_search.setOnClickListener(this);
        menu_notifications.setOnClickListener(this);
        menu_profile.setOnClickListener(this);
        menu_camera.setOnClickListener(this);
        menu_click_view.setOnClickListener(this);
        menu_close.setOnClickListener(this);
        menu_settings.setOnClickListener(this);
        menu_stat.setOnClickListener(this);
        activity_layout.setOnClickListener(this);
        search_location_button.setOnClickListener(this);
        try {
            myLoc();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                search_location_text.setText(list_data.get(i));
            }
        });

        completeAdapter = new PlaceAutoCompleteAdapter(this, R.layout.list_item);
        search_location_text.setAdapter(completeAdapter);
    }


    private void myLoc() throws IOException {
        LocationManager ls = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location lc = ls.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lc == null) {
            Log.d("Tag", "check location through network provider");
            lc = ls.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }
        if (lc == null) {
            Log.d("Tag", "check  location");
            Toast.makeText(Search_location.this, "No Network Location Available", Toast.LENGTH_SHORT).show();
        } else {
            Log.d("Tag", "call search location");
            progress_layout.setVisibility(View.VISIBLE);
            result_list.setVisibility(View.GONE);
            get_results(lc.getLatitude(), lc.getLongitude());
        }
        Log.d("Tag", "call search");
    }

    private void get_results(double latitude, double longitude) {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "search_location");
        postParam.put("latitude", "" + latitude);
        postParam.put("longitude", "" + longitude);
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "search_location parameters :" + postParam.toString());
        Call<API_Response> call = service.FriendsWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_layout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.e("API_Response", "search_location Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {

                        result_list.setVisibility(View.VISIBLE);

                        for (int j=0;j<response.body().getResult().getPlacesDetail().size();j++)
                        list_data.add(response.body().getResult().getPlacesDetail().get(j).getPlaceName());

                        adapter = new ArrayAdapter(Search_location.this, android.R.layout.simple_list_item_1, list_data);
                        result_list.setAdapter(adapter);
                    }
                    else Alerter.create(Search_location.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                } else{
                    Alerter.create(Search_location.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "search_location Response : " + new Gson().toJson(response.errorBody()));
            }}

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_layout.setVisibility(View.GONE);
               Alerter.create(Search_location.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "search_user Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Search_location.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == search_location_button) {
            if (search_location_text.getText().toString().equals(""))
                search_location_text.requestFocus();
            else {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(activity_layout.getWindowToken(), 0);
            }
        }

        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            Intent i = new Intent(Search_location.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == done) {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("location", search_location_text.getText().toString());
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
            Search_location.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Search_location.this, Upload_photo.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Search_location.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Search_location.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Search_location.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Search_location.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Search_location.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Search_location.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Search_location.this, Stats.class);
            i.putExtra("stats_header", "My stats");
            i.putExtra("stats_id", preferences.getString("user_id", ""));
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_click_view) {
            if (preferences.getInt("badge_value", 0) != 0) {
                badgeLayout.setVisibility(View.VISIBLE);
                badgeText.setText(preferences.getInt("badge_value", 0) + "");
            } else {
                badgeLayout.setVisibility(View.GONE);
            }
            menu_open_layout.setVisibility(View.VISIBLE);
            menu_click_view.setVisibility(View.GONE);
        }
        if (v == menu_close) {
            menu_status();
        }
    }

    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 123: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        myLoc();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                }
            }

        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        search_location_text.setText(list_data.get(i));
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("Tag", "Google Places API connection failed with error code: "
                + connectionResult.getErrorCode());
        Toast.makeText(this,
                "Google Places API connection failed with error code:" +
                        connectionResult.getErrorCode(),
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(Bundle bundle) {
//        mPlaceArrayAdapter.setGoogleApiClient(mGoogleApiClient);
//        Log.i("Tag", "Google Places API connected.");
    }

    @Override
    public void onConnectionSuspended(int i) {
//        mPlaceArrayAdapter.setGoogleApiClient(null);
//        Log.e("Tag", "Google Places API connection suspended.");
    }

}
