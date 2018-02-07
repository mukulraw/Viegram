package com.relinns.viegram.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.relinns.viegram.util.EditText_cursor;
import com.relinns.viegram.R;
import com.tapadoo.alerter.Alerter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;

@SuppressWarnings("ALL")
public class Signup extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener, View.OnFocusChangeListener, View.OnTouchListener {

    private Button signup_;
    private TextView terms_o_s;
    private AutoCompleteTextView country_signUp;
    private ArrayList<String> country_name;
    private ArrayList<String> country_id;
    private ArrayAdapter country_adapter;
    private EditText_cursor email_signUp;
    private EditText_cursor password_signUp;
    private EditText_cursor name_signUp;
    private EditText_cursor userName_signUp;
    private ProgressDialog progress_Dialog;
    private String countryId;
    private String deviceId;
    private ProgressBar display_progress;
    private ProgressBar email_progress;
    private SharedPreferences preferences;
    private boolean backpressed = true;
    private RelativeLayout back;
    private String PASSWORD_PATTERN = "((?=.*[A-Z])(?=.*[@#$%_.]).{6,20})";
//            "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%_.]).{6,20})";
    private Pattern pattern;
   private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+[.]+[a-z.]";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        pattern = Pattern.compile(PASSWORD_PATTERN);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        country_name = new ArrayList<>();
        country_id = new ArrayList<>();
        progress_Dialog = new ProgressDialog(this);
        deviceId = android.provider.Settings.Secure.getString(this.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
        countrylist();
        signup_ = (Button) findViewById(R.id.signup_);
        terms_o_s = (TextView) findViewById(R.id.terms_o_s);
        country_signUp = (AutoCompleteTextView) findViewById(R.id.country_signUp);
        email_signUp = (EditText_cursor) findViewById(R.id.email_signup);
        password_signUp = (EditText_cursor) findViewById(R.id.password_signUp);
        name_signUp = (EditText_cursor) findViewById(R.id.sign_upName);
        userName_signUp = (EditText_cursor) findViewById(R.id.userName_signUp);
        display_progress = (ProgressBar) findViewById(R.id.display_progress);
        email_progress = (ProgressBar) findViewById(R.id.email_progress);
        back = (RelativeLayout) findViewById(R.id.back);

        terms_o_s.setPaintFlags(terms_o_s.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        terms_o_s.setText("Terms of Service");
        country_adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, country_name);
        country_signUp.setAdapter(country_adapter);
        country_signUp.setThreshold(1);

        country_signUp.setOnItemClickListener(this);
        signup_.setOnClickListener(this);
        terms_o_s.setOnClickListener(this);
        name_signUp.setOnTouchListener(this);
        userName_signUp.setOnTouchListener(this);
        email_signUp.setOnTouchListener(this);
        password_signUp.setOnTouchListener(this);
        back.setOnClickListener(this);
        userName_signUp.setOnFocusChangeListener(this);
        email_signUp.setOnFocusChangeListener(this);
    }

    private String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = Signup.this.getAssets().open("country.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void countrylist() {
        try {
            JSONArray jsonArray = new JSONArray(loadJSONFromAsset());
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                country_name.add(object.getString("name"));
                country_id.add(object.getString("id"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        if (v == back) {
            finish();
            Signup.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
        }
        if (v == signup_) {
            Matcher match = pattern.matcher(password_signUp.getText().toString());
            if (name_signUp.getText().toString().equals("") && userName_signUp.getText().toString().equals("") && country_signUp.getText().toString().equals("")
                    && email_signUp.getText().toString().equals("") && password_signUp.getText().toString().equals("")) {
                Alerter.create(Signup.this)
                        .setText("All fields are required")
                        .setBackgroundColor(R.color.red)
                        .show();
            } else if (name_signUp.getText().toString().equals("")) {
                Alerter.create(Signup.this)
                        .setText("Field required")
                        .setBackgroundColor(R.color.red)
                        .show();
            } else if (userName_signUp.getText().toString().equals("")) {
                Alerter.create(Signup.this)
                        .setText("Field required")
                        .setBackgroundColor(R.color.red)
                        .show();
            } else if (country_signUp.getText().toString().equals("")) {
                Alerter.create(Signup.this)
                        .setText("Field required")
                        .setBackgroundColor(R.color.red)
                        .show();
            } else if (email_signUp.getText().toString().equals("")) {
                Alerter.create(Signup.this)
                        .setText("Field required")
                        .setBackgroundColor(R.color.red)
                        .show();
            }
            else if (email_signUp.getText().toString().matches(emailPattern)) {
                Alerter.create(Signup.this)
                        .setText("Please enter valid email address")
                        .setBackgroundColor(R.color.red)
                        .show();
            }
          else if (password_signUp.getText().toString().equals("")) {
                Alerter.create(Signup.this)
                        .setText("Field required")
                        .setBackgroundColor(R.color.red)
                        .show();
            }
//            else if (password_signUp)
            else if (!match.matches()) {
                Alerter.create(Signup.this)
                        .setText(R.string.password_validation)
                        .setBackgroundColor(R.color.red)
                        .show();
            }
            else {
                progress_Dialog.show();
                sign_up();
            }
        }
        if (v == terms_o_s) {
            Intent i = new Intent(Signup.this, Terms_service.class);
            i.putExtra("terms","1");
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
    }

    private void sign_up() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "signup");
        postParam.put("email", email_signUp.getText().toString());
        postParam.put("password", password_signUp.getText().toString());
        postParam.put("full_name", name_signUp.getText().toString());
        postParam.put("display_name", userName_signUp.getText().toString());
        postParam.put("country", countryId);
        postParam.put("device_id", deviceId);
        postParam.put("device_type", "android");
        postParam.put("device_token", FirebaseInstanceId.getInstance().getToken());

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Log.d("API_Parameters", "signup parameters :" + postParam.toString());
        Call<API_Response> call = service.accountWork(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progress_Dialog.dismiss();
                if (response.isSuccessful()) {
                    Log.e("API_Response", "signup Response : " + new Gson().toJson(response.body()));
                    Result result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("user_id", result.getDetail().getUserId());
                        edit.putString("email_id", email_signUp.getText().toString());
                        edit.commit();
                        Intent i = new Intent(Signup.this, Timeline.class);
                        i.putExtra("signup", "done");
                        startActivity(i);
                        overridePendingTransition(R.anim.enter, R.anim.exit);
                    } else {
                        String reason = result.getReason();
                        if (reason.equals("email already exist")) {
                            email_signUp.setError(reason);
                        }
                        if (reason.equals("display name already exist")) ;
                        {
                            userName_signUp.setError(reason);
                        }
                        if (reason.equals("Please try again")) {
                            Alerter.create(Signup.this).setBackgroundColor(R.color.red).setText(R.string.network_error).show();
                        }
                    }
                } else {
                    Alerter.create(Signup.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                    Log.e("API_Response", "signup Response : " + new Gson().toJson(response.errorBody()));
                }
            }
            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_Dialog.dismiss();
                Alerter.create(Signup.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
                Log.d("API_Error", "signup Error : " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backpressed) {
            Intent i = new Intent(Signup.this, Login_Screen.class);
            startActivity(i);
        } else
            backpressed = true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String name = country_signUp.getText().toString();
        int i = country_name.indexOf(name);
        countryId = country_id.get(i);

    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (v == userName_signUp) {
            if (!hasFocus) {
                display_progress.setVisibility(View.VISIBLE);
                verify_username();
            }
        }
        if (v == email_signUp) {
            if (!hasFocus) {
                email_progress.setVisibility(View.VISIBLE);
                verify_email();
            }
        }
    }

    private void verify_email() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "verify_email");
        postParam.put("email", email_signUp.getText().toString());

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Log.d("API_Parameters", "verify_email parameters :" + postParam.toString());

        Call<API_Response> call = service.getVerifyData(postParam);
        Log.wtf("URL Called", call.request().url() + "");

        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.e("API_Response", "verify_email Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        email_progress.setVisibility(View.GONE);
                        email_signUp.setError("Email id already exists.");
                    } else {
                        email_progress.setVisibility(View.INVISIBLE);
                    }
                } else
                    Log.e("API_Response", "verify_email Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                email_progress.setVisibility(View.INVISIBLE);
                Log.d("API_Error", "verify_email Error : " + t.getMessage());
            }
        });
    }

    private void verify_username() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "verify_name");
        postParam.put("display_name", userName_signUp.getText().toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);

        Log.d("API_Parameters", "verify_name parameters :" + postParam.toString());
        Call<API_Response> call = service.getVerifyData(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.e("API_Response", "verify_name Response : " + new Gson().toJson(response.body()));
                    if (response.body().getResult().getMsg().equals("201")) {
                        display_progress.setVisibility(View.GONE);
                        userName_signUp.setError("Display name already exists.");
                    } else {
                        display_progress.setVisibility(View.INVISIBLE);}
                } else
                    Log.e("API_Response", "verify_name Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                display_progress.setVisibility(View.INVISIBLE);
                Log.d("API_Error", "verify_email Error : " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v == name_signUp) {
            backpressed = false;
        }
        if (v == userName_signUp) {
            backpressed = false;
        }
        if (v == email_signUp) {
            backpressed = false;
        }
        if (v == password_signUp) {
            backpressed = false;
        }
        return false;
    }
}
