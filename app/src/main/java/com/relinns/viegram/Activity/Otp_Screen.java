package com.relinns.viegram.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Otp_Screen extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout back;
    private Button submit_otp;
    private EditText first;
    private EditText second;
    private EditText third;
    private EditText fourth;
    private ProgressDialog progress_Dialog;
    private SharedPreferences preferences;
    private String otp_code = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp__screen);
        back = (RelativeLayout) findViewById(R.id.back);
        submit_otp = (Button) findViewById(R.id.submit_otp);
        first = (EditText) findViewById(R.id.first);
        second = (EditText) findViewById(R.id.second);
        third = (EditText) findViewById(R.id.third);
        fourth = (EditText) findViewById(R.id.fourth);

        progress_Dialog = new ProgressDialog(this);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);



        first.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1) {
                    second.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        second.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1) {
                    third.requestFocus();
                } else if (s.toString().length() == 0) {
                    first.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        third.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 1) {
                    fourth.requestFocus();
                } else if (s.toString().length() == 0) {
                    second.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        fourth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() == 0) {
                    third.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        back.setOnClickListener(this);
        submit_otp.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Otp_Screen.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onClick(View v) {
        if (v == submit_otp) {
            if (!first.getText().toString().equals("") && !second.getText().toString().equals("") && !third.getText().toString().equals("") && !fourth.getText().toString().equals("")) {
                otp_code = first.getText().toString() + second.getText().toString() + third.getText().toString() + fourth.getText().toString();
                otp_code = otp_code.replace(" ", "");
                progress_Dialog.show();
                submit_otp();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setMessage("Invalid OTP code.");
                alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        first.setText("");
                        second.setText("");
                        third.setText("");
                        fourth.setText("");
                        first.requestFocus();
                    }
                }).show();
            }
        }
        if (v == back) {
            onBackPressed();
        }
    }

    private void submit_otp() {
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "verify_otp");
        postParam.put("email", preferences.getString("email_id", ""));
        postParam.put("otp_code", otp_code);

        Log.d("API_Parameters", "verify_otp parameters : " + postParam.toString());
        Call<API_Response> call = service.accountWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("API_Response", "verify_otp Response : " + new Gson().toJson(response.body()));
                    String msg = response.body().getResult().getMsg();
                    if (msg.equals("201")) {
                        Intent i = new Intent(Otp_Screen.this, Update_Password.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    } else if (msg.equals("204")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Otp_Screen.this);
                        alert.setMessage("Invalid OTP code.");
                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                first.setText("");
                                second.setText("");
                                third.setText("");
                                fourth.setText("");
                                first.requestFocus();
                            }
                        }).show();
                    } else {
                        Alerter.create(Otp_Screen.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                } else {
                    Alerter.create(Otp_Screen.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.e("API_Response", "verify_otp Response : " + new Gson().toJson(response.errorBody()));
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_Dialog.dismiss();
              Alerter.create(Otp_Screen.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("Tag", "verify_otp failure : " + t.getMessage());
            }
        });
    }

}
