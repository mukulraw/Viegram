package com.relinns.viegram.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
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

public class Update_Password extends AppCompatActivity implements View.OnClickListener {
private RelativeLayout back;
    private Button update;
    private EditText new_update;
    private EditText confirm_update;
    private ProgressDialog progressDialog;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update__password);
        back= (RelativeLayout) findViewById(R.id.back);
        new_update=(EditText)findViewById(R.id.new_update);
        confirm_update=(EditText)findViewById(R.id.confirm_update);
        update=(Button)findViewById(R.id.update);
        progressDialog = new ProgressDialog(this);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        back.setOnClickListener(this);
        update.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        Intent i= new Intent(Update_Password.this,Otp_Screen.class);
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if(v==back)
        {
            Intent i= new Intent(Update_Password.this,Otp_Screen.class);
            startActivity(i);
        }
        if(v==update)
        {
            if (!new_update.getText().toString().equals(confirm_update.getText().toString())) {
                Alerter.create(Update_Password.this)
                        .setText("Password does not match")
                        .setBackgroundColor(R.color.red)
                        .show();
            } else {
                progressDialog.show();
                update_password();
            }
        }
    }

    private void update_password() {
        Map<String, String> postParams = new HashMap<>();
        postParams.put("action", "reset_password");
        postParams.put("email", preferences.getString("email_id", ""));
        postParams.put("new_password", new_update.getText().toString());

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "reset_password parameters :" + postParams.toString());
        Call<API_Response> call = service.accountWork(postParams);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.e("API_Response", "reset_password Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Update_Password.this);
                        alert.setMessage("Password updated Successfully.");
                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = new Intent(Update_Password.this, Timeline.class);
                                startActivity(i);
                            }
                        }).show();
                    } else {
                        AlertDialog.Builder alert = new AlertDialog.Builder(Update_Password.this);
                        alert.setMessage("Something went wrong!");
                        alert.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = new Intent(Update_Password.this, Forgot_password.class);
                                startActivity(i);
                            }
                        }).show();
                    }
                } else {
                    Alerter.create(Update_Password.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "reset_password Response : " + new Gson().toJson(response.errorBody()));
                }
            }
            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progressDialog.dismiss();

                    Alerter.create(Update_Password.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
            }
        });
    }

}
