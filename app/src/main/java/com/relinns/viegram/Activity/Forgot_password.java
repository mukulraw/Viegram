package com.relinns.viegram.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import com.google.gson.Gson;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.tapadoo.alerter.Alerter;
import java.util.HashMap;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;

public class Forgot_password extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout back;
    private EditText forgot_email;
    private Button submit;
    private ProgressDialog progress_Dialog;
    private SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        preferences=getSharedPreferences("Viegram",MODE_PRIVATE);
        forgot_email = (EditText) findViewById(R.id.forgot_email);
        back = (RelativeLayout) findViewById(R.id.back);
        submit = (Button) findViewById(R.id.submit_bt);
        progress_Dialog = new ProgressDialog(this);
        back.setOnClickListener(this);
        submit.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
        Forgot_password.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            onBackPressed();
        }
        if (v == submit) {
            progress_Dialog.show();
            forgot_email();
        }
    }

    private void forgot_email() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "forget_password");
        postParam.put("email", forgot_email.getText().toString());

        Log.d("API_Parameters", "account_viewed parameters :" + postParam.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.accountWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.e("API_Response", "account_viewed Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        SharedPreferences.Editor edit= preferences.edit();
                        edit.putString("email_id",forgot_email.getText().toString());
                        edit.commit();
                        Intent i = new Intent(Forgot_password.this, Otp_Screen.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter,R.anim.exit);
                    } else if (response.body().getResult().getMsg().equals("204"))
                        Alerter.create(Forgot_password.this)
                                .setText("Enter valid email address")
                                .setBackgroundColor(R.color.red)
                                .show();
                    else Alerter.create(Forgot_password.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                } else {
                    Alerter.create(Forgot_password.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "account_viewed Response : " + new Gson().toJson(response.errorBody()));
                }
            }
            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_Dialog.dismiss();
               Alerter.create(Forgot_password.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("API_Error", "account_viewed Error : " + t.getMessage());
            }
        });
    }

}
