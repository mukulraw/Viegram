package com.relinns.viegram.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.relinns.viegram.util.EditText_cursor;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

public class Login_Screen extends Activity implements View.OnClickListener, View.OnTouchListener {

    private TextView sign_up;
    private TextView forgot_password;
    private Button login;
    private View activityRootView;
    private EditText_cursor login_email;
    private EditText_cursor login_password;
    private ProgressDialog progress_Dialog;
    private SharedPreferences preferences;
    private boolean backpressed = true;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login__screen);
        progress_Dialog = new ProgressDialog(this);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        token = FirebaseInstanceId.getInstance().getToken();

        login_email = (EditText_cursor) findViewById(R.id.login_email);
        login_password = (EditText_cursor) findViewById(R.id.login_password);
        sign_up = (TextView) findViewById(R.id.sign_up);
        activityRootView = findViewById(R.id.activity_root);
        forgot_password = (TextView) findViewById(R.id.forgot_password);
        login = (Button) findViewById(R.id.login);
        login_email.setOnTouchListener(this);

//        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputManager.hideSoftInputFromWindow(login_email.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        login.setOnClickListener(this);
        sign_up.setOnClickListener(this);
        forgot_password.setOnClickListener(this);
        login_password.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == forgot_password) {
            Intent intent = new Intent(Login_Screen.this, Forgot_password.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == sign_up) {
            Intent i = new Intent(Login_Screen.this, Signup.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }

        if (v == login) {
            if (login_email.getText().toString().equals("") && login_password.getText().toString().equals("")) {
                Alerter.create(Login_Screen.this)
                        .setText("All fields are required")
                        .setBackgroundColor(R.color.red)
                        .show();
            } else if (login_email.getText().toString().equals("")) {
                Alerter.create(Login_Screen.this)
                        .setText("Email field required")
                        .setBackgroundColor(R.color.red)
                        .show();
            } else if (login_password.getText().toString().equals("")) {
                Alerter.create(Login_Screen.this)
                        .setText("Password field required")
                        .setBackgroundColor(R.color.red)
                        .show();
            } else {
                progress_Dialog.show();
                sign_in();
            }
        }
    }

    private void sign_in() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "login");
        postParam.put("device_type", "android");
        postParam.put("email", login_email.getText().toString());
        postParam.put("password", login_password.getText().toString());
        postParam.put("device_token", token);
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "login parameters :" + postParam.toString());

        Call<API_Response> call = service.accountWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("API_Response", "login Response : " + new Gson().toJson(response.body()));
                    Result result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("user_id", result.getDetail().getUserId());
                        edit.putString("display_name", result.getDetail().getDisplayName());
                        edit.putString("bio_data", result.getDetail().getBioData());
                        edit.putString("link", result.getDetail().getLink());
                        edit.putString("cover_image", result.getDetail().getCoverImage());
                        edit.putString("profile_image", result.getDetail().getProfileImage());
                        edit.putString("full_name", result.getDetail().getFullName());
                        edit.putString("sign_done", "0");
                        edit.putString("country_iso", result.getDetail().getCountryIso());
                        edit.putString("", result.getDetail().getCountryIso());

                        edit.putString("email_id", result.getDetail().getEmail());
                        edit.commit();
                        Intent i = new Intent(Login_Screen.this, Timeline.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    } else if (result.getMsg().equals("204")) {
                        if (result.getReason().contains("exist")) {
                            Alerter.create(Login_Screen.this)
                                    .setText("Email does not exist.")
                                    .setBackgroundColor(R.color.red)
                                    .show();
                        } else {
                            Alerter.create(Login_Screen.this)
                                    .setText("password does not match email")
                                    .setBackgroundColor(R.color.red)
                                    .show();
                        }
                    } else {
                        Alerter.create(Login_Screen.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                } else {
                    Log.e("API_Response", "login Response : " + new Gson().toJson(response.errorBody()));
                    Alerter.create(Login_Screen.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                }

            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                Log.d("API_Error", "login Error : " + t.getMessage());
                progress_Dialog.dismiss();
               Alerter.create(Login_Screen.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (backpressed) {
            finishAffinity();
        } else {
            backpressed = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == login_email) {
            backpressed = false;
        }
        if (v == login_password) {
            backpressed = false;
        }
        return false;
    }
}
