package com.relinns.viegram.Activity;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import com.relinns.viegram.BuildConfig;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.RetrofitInstance;
import com.relinns.viegram.util.EditText_cursor;
import com.relinns.viegram.R;
import com.relinns.viegram.util.FileUtils;
import com.tapadoo.alerter.Alerter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


@SuppressWarnings("ALL")
public class Edit_Profile extends AppCompatActivity implements View.OnClickListener, View.OnTouchListener, View.OnFocusChangeListener {
    private TextView change_photo;
    private TextView cover_change;
    private TextView badgeText;
    private RelativeLayout badgeLayout;
    private RelativeLayout progress_layout;
    private RelativeLayout back;
    private RelativeLayout activity_layout;
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
    private RelativeLayout menu_home;
    private ImageView menu_click_view;
    private ImageView edit_cover_image;
    private LinearLayout working_layout;
    private Button save;
    private CircleImageView circleImageView;
    private SharedPreferences preferences;
    private EditText_cursor edit_name;
    private EditText_cursor edit_username;
    private EditText_cursor edit_link;
    private EditText_cursor edit_bio;
    private final int CAPTURE_IMAGE = 5;
    private final int PICK_IMAGE = 6;
    private final int STORAGE_PERMISSION_CODE = 23;
    private final int CAPTURE_PERMISSION_CODE = 5;
    private final int READ_PERMISSION_CODE = 10;
    private Uri filepath;
    private Uri fileUri;
    private boolean backpressed = true;
    private String profile_image_path = "";
    private String cover_image_path = "";
    private ProgressDialog progress_Dialog;
    private static final int MEDIA_TYPE_IMAGE = 1;
    private static final String IMAGE_DIRECTORY_NAME = "Viegram";
    private int value = 0;
    private ProgressBar displayProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit__profile);

        progress_Dialog = new ProgressDialog(this);
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);

        save = (Button) findViewById(R.id.save);
        displayProgress = (ProgressBar) findViewById(R.id.display_progress);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        working_layout = (LinearLayout) findViewById(R.id.working_layout);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        edit_bio = (EditText_cursor) findViewById(R.id.edit_bio);
        edit_link = (EditText_cursor) findViewById(R.id.edit_link);
        edit_name = (EditText_cursor) findViewById(R.id.edit_name);
        edit_username = (EditText_cursor) findViewById(R.id.edit_username);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        back = (RelativeLayout) findViewById(R.id.back);
        change_photo = (TextView) findViewById(R.id.change_photo);
        cover_change = (TextView) findViewById(R.id.cover_change);
        menu_open_layout = (RelativeLayout) findViewById(R.id.edit_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.edit_menu_click);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        circleImageView = (CircleImageView) findViewById(R.id.profile_image);
        edit_cover_image = (ImageView) findViewById(R.id.edit_cover_image);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);


        menu_open_layout.setVisibility(View.GONE);
        progress_layout.setVisibility(View.VISIBLE);
        working_layout.setVisibility(View.GONE);

        cover_change.setPaintFlags(change_photo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        cover_change.setText("Change Cover Photo");
        change_photo.setPaintFlags(change_photo.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        change_photo.setText("Change Photo");
        //set data
        edit_username.setText(preferences.getString("display_name", ""));
        edit_bio.setText(preferences.getString("bio_data", ""));
        edit_link.setText(preferences.getString("link", ""));
        edit_name.setText(preferences.getString("full_name", ""));

        Glide.with(this).load(preferences.getString("cover_image", ""))
                .into(edit_cover_image);
        Log.d("Glide", preferences.getString("profile_image", ""));
        Glide.with(this).load(preferences.getString("profile_image", ""))
            //    .thumbnail(0.1f)
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        Log.d("GLide", e.getMessage());
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.d("GLide", "Resource ready");
                        return false;
                    }
                })
                .into(circleImageView);

        progress_layout.setVisibility(View.GONE);
        working_layout.setVisibility(View.VISIBLE);
        save.setOnClickListener(this);
        back.setOnClickListener(this);
        menu_home.setOnClickListener(this);
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
        edit_username.setOnFocusChangeListener(this);
        change_photo.setOnClickListener(this);
        cover_change.setOnClickListener(this);
        edit_username.setOnTouchListener(this);
        edit_link.setOnTouchListener(this);
        edit_name.setOnTouchListener(this);
        edit_bio.setOnTouchListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == save) {
            progress_Dialog.show();
            try {
                update_profile();
            } catch (Exception e) {
                progress_Dialog.dismiss();
                Alerter.create(Edit_Profile.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                e.printStackTrace();
            }
        }
        if (v == menu_home) {
            Intent i = new Intent(Edit_Profile.this, Timeline.class);
            startActivity(i);
            transition();
        }
        if (v == back) {
            onBackPressed();
        }
        if (v == change_photo) {
            value = 0;
            choose_option();
        }
        if (v == cover_change) {
            value = 1;
            choose_option();
        }
        if (v == menu_camera) {
            menu_status();
            Intent i = new Intent(Edit_Profile.this, Upload_photo.class);
            startActivity(i);
            transition();
        }
        if (v == menu_follow) {
            menu_status();
            Intent i = new Intent(Edit_Profile.this, Follower_following.class);
            startActivity(i);
            transition();
        }
        if (v == menu_notifications) {
            menu_status();
            Intent i = new Intent(Edit_Profile.this, Notifications.class);
            startActivity(i);
            transition();
        }
        if (v == menu_profile) {
            menu_status();
            Intent i = new Intent(Edit_Profile.this, Profile.class);
            startActivity(i);
            transition();
        }
        if (v == menu_ranking) {
            menu_status();
            Intent i = new Intent(Edit_Profile.this, Ranking.class);
            startActivity(i);
            transition();
        }
        if (v == menu_search) {
            menu_status();
            Intent i = new Intent(Edit_Profile.this, Search.class);
            startActivity(i);
            transition();
        }
        if (v == menu_settings) {
            menu_status();
            Intent i = new Intent(Edit_Profile.this, Settings.class);
            startActivity(i);
            transition();
        }
        if (v == menu_stat) {
            menu_status();
            Intent i = new Intent(Edit_Profile.this, Stats.class);
            i.putExtra("stats_header", "My stats");
            i.putExtra("stats_id", preferences.getString("user_id", ""));
            startActivity(i);
            transition();
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

    //transition animation
    private void transition() {
        overridePendingTransition(R.anim.enter, R.anim.exit);
    }

    //verify username
    private void verify_username() {
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "verify_name");
        postParam.put("display_name", edit_username.getText().toString());

        Log.d("API_Parameters", "verify_name parameters :" + postParam.toString());
        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = service.getVerifyData(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                displayProgress.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Log.e("API_Response", "verify_name Response : " + new Gson().toJson(response.body()));

                    Result result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        edit_username.setError("Display name already exists.");
                    }
                } else
                    Log.e("API_Response", "verify_name Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                displayProgress.setVisibility(View.GONE);
                Log.d("API_Error", "verify_email Error : " + t.getMessage());
            }
        });
    }

    //custom dialog for choose photo
    private void choose_option() {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.design_choose_photo);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        RelativeLayout choose_gallery, capture_image, remove_image;
        choose_gallery = (RelativeLayout) dialog.findViewById(R.id.choose_gallery);
        capture_image = (RelativeLayout) dialog.findViewById(R.id.capture_image);
        remove_image = (RelativeLayout) dialog.findViewById(R.id.remove_image);
        TextView upload_photo_text = (TextView) dialog.findViewById(R.id.upload_image_text);
        if (value == 0) {
            upload_photo_text.setText("Set a Profile Photo");
        } else {
            upload_photo_text.setText("Set a Cover Photo");
        }
        choose_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                int permissionCheck = ContextCompat.checkSelfPermission(Edit_Profile.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

                if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Edit_Profile.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
                } else {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_IMAGE);
                    requestStoragePermission();
                }


            }
        });
        capture_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                if ((ContextCompat.checkSelfPermission(Edit_Profile.this, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Edit_Profile.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                    capture_ProfileImage();
                } else {
//                    ActivityCompat.requestPermissions(Edit_Profile.this, new String[]{android.Manifest.permission.CAMERA}, CAPTURE_PERMISSION_CODE);
                    ActivityCompat.requestPermissions(Edit_Profile.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, CAPTURE_PERMISSION_CODE);
//                    ActivityCompat.requestPermissions(Edit_Profile.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                }
            }
        });
        remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                if (value == 0) {
                    circleImageView.setImageDrawable(getResources().getDrawable(R.drawable.default_image2));
                    circleImageView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                    profile_image_path = getFileDataFromDrawable(Edit_Profile.this, circleImageView.getDrawable());

                } else {
                    edit_cover_image.setImageDrawable(getResources().getDrawable(R.drawable.default_cover_image));
                    cover_image_path = getFileDataFromDrawable(Edit_Profile.this, edit_cover_image.getDrawable());
                }

            }
        });
        dialog.show();
    }

    private String getFileDataFromDrawable(Context context, Drawable drawable) {
        Bitmap bitmap = null;
//        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
//
//                bitmap = ((BitmapDrawable) drawable).getBitmap();
//
//            }
//            else {
//             bitmap = ((GlideBitmapDrawable) drawable).getBitmap();
//
//        }
        bitmap = ((BitmapDrawable) drawable).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, byteArrayOutputStream);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("Cache", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath;
        if (value == 0)
            mypath = new File(directory, "defaultprofile.jpg");
        else
            mypath = new File(directory, "defaultcover.jpg");
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            fos.write(byteArrayOutputStream.toByteArray());
            // Use the compress method on the BitMap object to write image to the OutputStream
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("Path", mypath.getAbsolutePath());
        return mypath.getPath();
    }


    // opened menu visibility gone
    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    //intent code to capture image
    private void capture_ProfileImage() {
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        } else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
            // start the image capture Intent
            startActivityForResult(intent, CAPTURE_IMAGE);
        }
    }

    //get saved image uri
    private Uri getOutputMediaFileUri(int type) {
        if (android.os.Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(Edit_Profile.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    getOutputMediaFile(Edit_Profile.MEDIA_TYPE_IMAGE));
        } else {
            return Uri.fromFile(getOutputMediaFile(Edit_Profile.MEDIA_TYPE_IMAGE));
        }
//        return Uri.fromFile(getOutputMediaFile(type));
    }

    //check device support camera or not
    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    //save image in gallery
    private static File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory().getAbsolutePath() + "/Viegram/Media/ViegramProfileImages");

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else {
            return null;
        }
        return mediaFile;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, PICK_IMAGE);

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case CAPTURE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    capture_ProfileImage();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            case STORAGE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Edit_Profile.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            ActivityCompat.requestPermissions(Edit_Profile.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }


    //update profile
    private void update_profile() throws TimeoutException {
        RequestBody id =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), preferences.getString("user_id", ""));
        RequestBody action =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), "edit_profile");
        final RequestBody full_name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), edit_name.getText().toString());
        RequestBody bio_data =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), edit_bio.getText().toString());
        RequestBody link =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), edit_link.getText().toString());
        RequestBody display_name =
                RequestBody.create(
                        MediaType.parse("multipart/form-data"), edit_username.getText().toString());


        GetViegramData getResponse = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call;
        if (profile_image_path.equals("") && cover_image_path.equals("")) {
            call = getResponse.updateprofile(action, id, full_name, bio_data, link, display_name);
        } else if (profile_image_path.equals("") && !cover_image_path.equals("")) {
            File coverFile = new File(cover_image_path);
            RequestBody coverbody = RequestBody.create(MediaType.parse("*/*"), coverFile);
            MultipartBody.Part coverImage = MultipartBody.Part.createFormData("cover_image", coverFile.getName(), coverbody);
            call = getResponse.updateprofilesingle(coverImage, action, id, full_name, bio_data, link, display_name);
        } else if (cover_image_path.equals("") && !profile_image_path.equals("")) {
            Log.d("path", "profile" + profile_image_path);
            File profileFile = new File(profile_image_path);
            Log.d("path", "file " + profileFile.getPath());
            RequestBody profiebody = RequestBody.create(MediaType.parse("*/*"), profileFile);
            MultipartBody.Part profileImage = MultipartBody.Part.createFormData("filename", profileFile.getName(), profiebody);
            call = getResponse.updateprofilesingle(profileImage, action, id, full_name, bio_data, link, display_name);
        } else {
            File coverFile = new File(cover_image_path);
            RequestBody coverbody = RequestBody.create(MediaType.parse("*/*"), coverFile);
            MultipartBody.Part coverImage = MultipartBody.Part.createFormData("cover_image", coverFile.getName(), coverbody);
            File profileFile = new File(profile_image_path);
            RequestBody profiebody = RequestBody.create(MediaType.parse("*/*"), profileFile);
            MultipartBody.Part profileImage = MultipartBody.Part.createFormData("filename", profileFile.getName(), profiebody);
            call = getResponse.EditProfile(profileImage, coverImage, action, id, full_name, bio_data, link, display_name);
        }
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                if (response.isSuccessful()) {
                    Log.d("API_Response", "Edit profile response :" + new Gson().toJson(response.body().getResult()));
                    progress_Dialog.dismiss();
                    Result result = response.body().getResult();
                    if (result.getMsg().equals("201")) {
                        SharedPreferences.Editor edit = preferences.edit();
                        edit.putString("display_name", result.getDetails().getDisplayName());
                        edit.putString("bio_data", result.getDetails().getBioData());
                        edit.putString("link", result.getDetails().getLink());
                        edit.putString("cover_image", result.getDetails().getCoverImage());
                        edit.putString("profile_image", result.getDetails().getProfileImage());
                        edit.putString("full_name",result.getDetails().getFullName());
                        edit.commit();
//                        SharedPreferences.Editor edit = preferences.edit();
                        Timeline.resultp.setDisplayName(result.getDetails().getDisplayName());
                        Timeline.resultp.setBioData(result.getDetails().getBioData());
                        Timeline.resultp.setLink(result.getDetails().getLink());
                        Timeline.resultp.setCoverImage(result.getDetails().getCoverImage());
                        Timeline.resultp.setProfileImage(result.getDetails().getProfileImage());
                        Timeline.resultp.setFullName(result.getDetails().getFullName());
                        if (getIntent().getStringExtra("Activity").equals("profile"))
                        {
                        Intent i = new Intent(Edit_Profile.this, Profile.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.enter2, R.anim.exit2);
                        }
                        else
                        finish();
                    } else if (result.getMsg().equals("204")) {
                        if (result.getReason().equals("display name already exist")) {

                            Alerter.create(Edit_Profile.this)
                                    .setText("Display name Already exists")
                                    .setBackgroundColor(R.color.red)
                                    .show();
                            edit_username.requestFocus();
                        } else Alerter.create(Edit_Profile.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    } else {
                        Alerter.create(Edit_Profile.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.login_bg)
                                .show();
                    }
                } else {
                    Alerter.create(Edit_Profile.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.login_bg)
                            .show();
                    Log.d("API_Response", "Edit profile response :" + new Gson().toJson(response.errorBody()));

                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progress_Dialog.dismiss();
               Alerter.create(Edit_Profile.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.login_bg)
                        .show();
                Log.d("Upload_data", "API failure : " + t.getMessage());

            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                if (value == 0) {

                 //   profile_image_path = resultUri.getPath();
                    profile_image_path = FileUtils.getInstance().compressImage(resultUri,this);
                    circleImageView.setImageURI(resultUri);
                } else {
                 //   cover_image_path = resultUri.getPath();
                    cover_image_path = FileUtils.getInstance().compressImage(resultUri, this);
                    edit_cover_image.setImageURI(resultUri);
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();

                Log.d("Tag", "Crop error message : " + error);
            }
        }

        if (requestCode == CAPTURE_IMAGE && resultCode == RESULT_OK) {
            if (resultCode == RESULT_OK) {
                preview_ProfileImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null) {

            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
                String[] filePathColumn = {MediaStore.Images.Media.DATA};
                Cursor cursor = Edit_Profile.this.getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                cursor.moveToFirst();
                filepath = getImageUri(Edit_Profile.this, BitmapFactory.decodeFile(cursor.getString(cursor.getColumnIndex(filePathColumn[0]))));
                cursor.close();

            } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
                ParcelFileDescriptor parcelFileDescriptor;
                try {
                    parcelFileDescriptor = getContentResolver().openFileDescriptor(data.getData(), "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                    parcelFileDescriptor.close();
                    filepath = Uri.parse(bitmapToFile(image).getPath());
                    Log.d("Image", "K filepath : " + filepath);
                    if (value == 0) {
                        profile_image_path = filepath.getPath();
                        circleImageView.setImageURI(filepath);
                    } else {
                        cover_image_path = filepath.getPath();
                        edit_cover_image.setImageURI(filepath);
                    }
//                        setImagesize(filepath);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            } else {
                Bitmap image2 = null;
                try {
                    image2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                filepath = getImageUri(this, image2);
            }
            if (value == 0) {
                CropImage.activity(filepath)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(1, 1)
                        .start(this);
            } else {
                CropImage.activity(filepath)
                        .setCropShape(CropImageView.CropShape.RECTANGLE)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setAspectRatio(2, 1)
                        .start(this);
            }
        }
    }

    private File bitmapToFile(Bitmap bmp) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File myDir = cw.getDir("Cache", Context.MODE_PRIVATE);

//        File myDir = new File(root + "/saved_images");
        myDir.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
            Log.d("File", "return" + file.getAbsolutePath());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //preview captured  image
    private void preview_ProfileImage() {

        if (value == 0) {
            CropImage.activity(fileUri)
                    .setCropShape(CropImageView.CropShape.OVAL)
                    .setAspectRatio(1, 1)
                    .start(this);

        } else {
            CropImage.activity(fileUri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(2, 1)
                    .start(this);
        }
    }

    //create uri from bitmap
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File directory = new File(
                Environment
                        .getExternalStorageDirectory().getAbsolutePath() + "/Viegram/Media/ViegramProfileImages");
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String filename = "IMG_" + Calendar.getInstance()
                .getTimeInMillis() + ".jpg";
        File file = new File(directory.getPath(), filename);
        try {
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }

        String path = null;

        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, file.getPath(), filename);

        Log.d("Tag", "image path : " + path);
        Log.d("Tag", "return path : " + Uri.parse(path));
        return Uri.parse(path);
    }

    @Override
    public void onBackPressed() {
        if (backpressed) {
            super.onBackPressed();
            Edit_Profile.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
        } else {
            backpressed = true;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent motionEvent) {
        if (v == edit_bio) {
            backpressed = false;
        }
        if (v == edit_link) {
            backpressed = false;
        }
        if (v == edit_name) {
            backpressed = false;
        }
        if (v == edit_username) {
            backpressed = false;
        }
        return false;
    }

    @Override
    public void onFocusChange(View view, boolean b) {
        if (view == edit_username) {
            if (!b) {
                if (!edit_username.getText().toString().trim().equals(preferences.getString("display_name", ""))) {
                    displayProgress.setVisibility(View.VISIBLE);
                    verify_username();
                }
            }
        }
    }
}
