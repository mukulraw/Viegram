package com.relinns.viegram.Activity;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.lzyzsd.circleprogress.CircleProgress;
import com.google.gson.Gson;
import com.relinns.viegram.BuildConfig;
import com.relinns.viegram.Modal.API_Response;
import com.relinns.viegram.Modal.Result;
import com.relinns.viegram.R;
import com.relinns.viegram.network.GetViegramData;
import com.relinns.viegram.network.ProgressRequestBody;
import com.relinns.viegram.network.RetrofitInstance;
import com.relinns.viegram.uploadservice.events.NotificationActions;
import com.relinns.viegram.util.FileUtils;
import com.relinns.viegram.util.RealPathUtils;
import com.relinns.viegram.video.MediaController;
import com.tapadoo.alerter.Alerter;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationAction;
import net.gotev.uploadservice.UploadNotificationConfig;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

//import com.iceteck.silicompressorr.SiliCompressor;

@SuppressWarnings("ALL")
public class Upload_photo extends AppCompatActivity implements View.OnClickListener, ProgressRequestBody.Listener {
    private VideoView upload_video_view;
    private LinearLayout tag_people, working_layout, add_location;
    private RelativeLayout  uploadprogressLayout,badgeLayout,progress_layout, restrict_layout, back, activity_layout, upload_edit_photo, menu_home, menu_open_layout, menu_close, menu_profile, menu_stat, menu_follow, menu_notifications, menu_settings, menu_search, menu_ranking, imageHolder, menu_camera;
    private ImageView play_video, menu_click_view, upload_image_view, inital_image;
    private Button upload_photo;
    private final int PICK_VIDEOIMAGE = 5;
    private final int STORAGE_PERMISSION_CODE = 23;
    private final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2;
    private final int TAG_CODE = 1;
    private final int LOCATION_CODE = 3;
    private final int CAPTURE_PERMISSION_CODE = 5;
    private final int READ_PERMISSION_CODE = 10;
    private String type;
    private String upload_image_path = "";
    private String check_value = "0";
    private String filetype = "";
    private String location_result = "";
    private String result_id = "";
    private String x_value_result = "";
    private String y_value_result = "";
    private TextView badgeText;
    private TextView location;
    private TextView tag_text;
    private TextView add_caption_textbt;
    private TextView upload_header;
    private Uri fileUri;
    private SharedPreferences preferences;
    private CheckBox check;
    private EditText caption_text;
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    private static final int MEDIA_TYPE_IMAGE = 1;
    private File thumbnail;
    private File tempFile;
    private ProgressDialog progressDialog;
    private CircleProgress circleProgress;
    private Uri mSelectedMediaUri;
    private static final String PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final int REQUEST_STORAGE_PERMISSION = 123;
    private static final int MAX_RETRIES = 3;
    private static final boolean FIXED_LENGTH_STREAMING_MODE = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);

        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        imageHolder = (RelativeLayout) findViewById(R.id.upload_image_holder);
        progressDialog = new ProgressDialog(this);
        check = (CheckBox) findViewById(R.id.check);
        upload_video_view = (VideoView) findViewById(R.id.upload_video_view);
        play_video = (ImageView) findViewById(R.id.play_video);
        activity_layout = (RelativeLayout) findViewById(R.id.activity_layout);
        upload_edit_photo = (RelativeLayout) findViewById(R.id.upload_edit_photo);
        upload_photo = (Button) findViewById(R.id.upload_photo);
        back = (RelativeLayout) findViewById(R.id.back);
        uploadprogressLayout = (RelativeLayout) findViewById(R.id.upload_progresslayout);
        circleProgress = (CircleProgress) findViewById(R.id.circle_progress);
        restrict_layout = (RelativeLayout) findViewById(R.id.restrict_layout);
        working_layout = (LinearLayout) findViewById(R.id.working_layout);
        progress_layout = (RelativeLayout) findViewById(R.id.progress_layout);
        upload_header = (TextView) findViewById(R.id.upload_header);
        add_caption_textbt = (TextView) findViewById(R.id.add_caption_textbt);
        caption_text = (EditText) findViewById(R.id.caption_text);
        menu_home = (RelativeLayout) findViewById(R.id.menu_home);
        add_location = (LinearLayout) findViewById(R.id.add_location);
        tag_people = (LinearLayout) findViewById(R.id.tag_people);
        menu_open_layout = (RelativeLayout) findViewById(R.id.upload_menu_open);
        menu_click_view = (ImageView) findViewById(R.id.upload_menu_click);
        menu_profile = (RelativeLayout) findViewById(R.id.menu_profile);
        menu_stat = (RelativeLayout) findViewById(R.id.menu_stat);
        menu_follow = (RelativeLayout) findViewById(R.id.menu_follow_following);
        menu_notifications = (RelativeLayout) findViewById(R.id.menu_notification);
        menu_settings = (RelativeLayout) findViewById(R.id.menu_settings);
        menu_search = (RelativeLayout) findViewById(R.id.menu_search);
        menu_ranking = (RelativeLayout) findViewById(R.id.menu_ranking);
        menu_camera = (RelativeLayout) findViewById(R.id.menu_camera);
        menu_close = (RelativeLayout) findViewById(R.id.menu_close);
        upload_image_view = (ImageView) findViewById(R.id.upload_image_view);
        inital_image = (ImageView) findViewById(R.id.inital_image);
        badgeLayout = (RelativeLayout) findViewById(R.id.badge_layout);
        badgeText = (TextView) findViewById(R.id.badge_text);
        location = (TextView) findViewById(R.id.location);
        tag_text = (TextView) findViewById(R.id.tag_text);
        uploadprogressLayout.setVisibility(View.GONE);
        menu_open_layout.setVisibility(View.GONE);
        tag_people.setVisibility(View.GONE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("result_name", "");
        editor.putString("result_id", "");
        editor.putString("x_value_result", "");
        editor.putString("y_value_result", "");
        editor.putString("result_size", "");
        editor.commit();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            type = "repost";
            repostWork(getIntent().getStringExtra("photo"), getIntent().getStringExtra("caption"));
        } else {
            type = "post";
            progress_layout.setVisibility(View.GONE);
            working_layout.setVisibility(View.VISIBLE);
            inital_image.setVisibility(View.VISIBLE);
            upload_image_view.setVisibility(View.GONE);
            add_location.setEnabled(false);
        }
        play_video.setOnClickListener(this);
        upload_image_view.setOnClickListener(this);
        activity_layout.setOnClickListener(this);
        upload_photo.setOnClickListener(this);
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
        add_location.setOnClickListener(this);
        tag_people.setOnClickListener(this);
        upload_edit_photo.setOnClickListener(this);
        imageHolder.setOnClickListener(this);
        add_caption_textbt.setOnClickListener(this);
        check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    check_value = "1";
                } else {
                    check_value = "0";
                }
            }
        });
    }

    //repost post set data
    private void repostWork(String photo, String caption) {
        upload_edit_photo.setVisibility(View.GONE);
        inital_image.setVisibility(View.GONE);
        upload_image_view.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams((int) getIntent().getFloatExtra("width", 0.0f), (int) getIntent().getFloatExtra("height", 0.0f));
        upload_image_view.setLayoutParams(params);
        add_location.setVisibility(View.GONE);
        restrict_layout.setVisibility(View.GONE);
        upload_image_view.setEnabled(false);
        upload_header.setText("Repost Photo");
        caption_text.setText(caption);
        upload_photo.setText("Repost");
        Glide.with(Upload_photo.this).load(photo)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progress_layout.setVisibility(View.GONE);
                        working_layout.setVisibility(View.VISIBLE);
                        if (getIntent().getStringExtra("postType").equals("video")) {
                            play_video.setVisibility(View.VISIBLE);
                            upload_image_path = getIntent().getStringExtra("video");
                        }
                        return false;
                    }
                })
                .into(upload_image_view);
    }

    @Override
    public void onBackPressed() {
        deleteTempFile();
        Intent i = new Intent(Upload_photo.this, Timeline.class);
        startActivity(i);
        Upload_photo.this.overridePendingTransition(R.anim.exit2, R.anim.enter2);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case READ_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent =  new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("*/*");
                    intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});

                    //     Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    //  intent.setType("video/*, image/*");
                    //     intent.setType("*/*");
                    startActivityForResult(intent, PICK_VIDEOIMAGE);

                }
                return;
            }
            case CAPTURE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    captureImage();
                } else {
                }
                return;
            }
            case STORAGE_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                }
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {                                    //cropped image code
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                filetype = "image";
                onImageSet(result.getUri());
                setImagesize(result.getUri());
                upload_image_path = FileUtils.getInstance().compressImage(result.getUri(),this);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d("Screen_Upload_Photo", "Crop Error : " + error);
            }
        }
        if (requestCode == TAG_CODE) {                                                                              //get tagged people data
            if (resultCode == Activity.RESULT_OK) {
                result_id = data.getStringExtra("result_id");
                x_value_result = data.getStringExtra("x_value_result");
                y_value_result = data.getStringExtra("y_value_result");
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("result_name", data.getStringExtra("result_name"));
                editor.putString("result_id", result_id);
                editor.putString("x_value_result", x_value_result);
                editor.putString("y_value_result", y_value_result);
                editor.putString("result_size", data.getStringExtra("result_size"));
                editor.commit();
                if (data.getStringExtra("result_size").equals("0")) {
                    tag_text.setText("Tag People");
                } else
                    tag_text.setText(data.getStringExtra("result_size") + " people tagged ");
            }
            if (resultCode == Activity.RESULT_CANCELED) {
            }
        }
        if (requestCode == LOCATION_CODE) {                                                                 //get location
            if (resultCode == Activity.RESULT_OK) {
                location_result = data.getStringExtra("location");
                if (!location_result.equals(""))
                    location.setText(location_result);
            }

        }
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {                                         //captured image
            if (resultCode == RESULT_OK) {
                play_video.setVisibility(View.GONE);
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(), "User cancelled image capture", Toast.LENGTH_SHORT).show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(), "Sorry! Failed to capture image", Toast.LENGTH_SHORT).show();
            }
        }
        if (requestCode == PICK_VIDEOIMAGE && resultCode == RESULT_OK) {                                        //pick video or image code
            mSelectedMediaUri = data.getData();
            //   File video_final_path = new File(getRealPathFromURI(mSelectedMediaUri));
            File video_final_path = new File(RealPathUtils.getRealPath(mSelectedMediaUri, this));
            //    File video_final_path = new File(RealPathUtils.getRealPath(getApplicationContext(),selectedMediaUri));
            Log.d("Screen_Upload_Photo", "pick video or image uri : " + video_final_path.toString());

            add_location.setEnabled(true);
            location.setTextColor(getResources().getColor(R.color.upload_text));
            if (mSelectedMediaUri.toString().contains("image")) {                                                        //if image is selected
                imageIsSelected(mSelectedMediaUri);
            } else if (mSelectedMediaUri.toString().contains("video")) {   //if video is selected
                result_id = "";
                x_value_result = "";
                y_value_result = "";

                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("result_name", "");
                editor.putString("result_id", "");
                editor.putString("x_value_result", "");
                editor.putString("y_value_result", "");
                editor.putString("result_size", "");
                editor.commit();
                //    saveTempFile(mSelectedMediaUri);

             /*   Bitmap b = */

                DisplayMetrics displayMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                float width_screen = displayMetrics.widthPixels;

                Bitmap out = null;
                try {
                    Bitmap bm = MediaStore.Video.Thumbnails.getThumbnail(
                            getContentResolver(),
                            ContentUris.parseId(mSelectedMediaUri),
                            MediaStore.Video.Thumbnails.MINI_KIND,
                            null);
                    out = Bitmap.createScaledBitmap(bm, (int) width_screen, (int) width_screen, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                upload_image_path = video_final_path.toString();
                Log.d("Screen_Upload_Photo", "thumbnailout : " + upload_image_path);

                videoIsSelected(out);

            }
            //when uri returns file path
            else {
                if (mSelectedMediaUri.toString().endsWith(".jpg") || mSelectedMediaUri.toString().endsWith(".jpeg") ||
                        mSelectedMediaUri.toString().endsWith(".png")) {
                    CropImage.activity(mSelectedMediaUri)
                            .setCropShape(CropImageView.CropShape.RECTANGLE)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(this);
                } else {
                    result_id = "";
                    x_value_result = "";
                    y_value_result = "";
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("result_name", "");
                    editor.putString("result_id", "");
                    editor.putString("x_value_result", "");
                    editor.putString("y_value_result", "");
                    editor.putString("result_size", "");
                    editor.commit();
                    File tempVideo = new File(mSelectedMediaUri.getPath());
                    Log.d("Screen_Upload_Photo", "tempFile of video  : " + tempVideo.getAbsolutePath());
                    Bitmap thumb = ThumbnailUtils.createVideoThumbnail(tempVideo.getAbsolutePath(), MediaStore.Video.Thumbnails.MINI_KIND);
                    Log.d("Screen_Upload_Photo", "thumbnail created by video : " + thumb);
                    upload_image_path = video_final_path.toString();
                    videoIsSelected(thumb);
                }
            }
        }
    }

    //when video is selected from gallery
    private void videoIsSelected(Bitmap out) {
        tag_people.setVisibility(View.GONE);
        filetype = "video";
        play_video.setVisibility(View.VISIBLE);
        inital_image.setVisibility(View.VISIBLE);
        if (out != null) {
            thumbnail = bitmapToFile(out);
            Log.d("Screen_Upload_Photo", "selected tumbnail is : " + thumbnail);

            inital_image.setVisibility(View.GONE);
            upload_image_view.setVisibility(View.VISIBLE);
            upload_image_view.setImageBitmap(out);

        }




        /*else {
            upload_image_view.setVisibility(View.GONE);
            inital_image.setVisibility(View.GONE);
            upload_video_view.setVisibility(View.VISIBLE);
            upload_video_view.setVideoPath(upload_image_path);
            upload_video_view.requestFocus();
            upload_video_view.start();
        }*/
    }

    //when image is selected from gallery
    private void imageIsSelected(Uri selectedMediaUri) {
        Uri filepath = null;
        // for marshmallow
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = Upload_photo.this.getContentResolver().query(selectedMediaUri, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            filepath = getImageUri(Upload_photo.this, BitmapFactory.decodeFile(cursor.getString(columnIndex)));
            Log.d("Screen_Upload_Photo", "M filepath : " + filepath);
            cursor.close();

        }
        //for kitkat
        else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT) {
            ParcelFileDescriptor parcelFileDescriptor;
            try {
                parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedMediaUri, "r");
                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                parcelFileDescriptor.close();
                filepath = Uri.parse(bitmapToFile(image).getPath());
                Log.d("Screen_Upload_Photo", "K filepath : " + filepath);
                onImageSet(filepath);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

        } else {
            Bitmap image2 = null;
            try {
                image2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedMediaUri);

            } catch (IOException e) {
                e.printStackTrace();
            }
            filepath = getImageUri(this, image2);
            Log.d("Screen_Upload_Photo", "Other filepath : " + filepath.toString());
        }
        CropImage.activity(filepath)
                .setCropShape(CropImageView.CropShape.RECTANGLE)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(this);

    }

    //when image is set on imageview
    private void onImageSet(Uri filepath) {

        filetype = "image";
        play_video.setVisibility(View.GONE);
        upload_image_path = filepath.getPath();
        upload_image_view.setImageURI(filepath);
        upload_image_view.setVisibility(View.VISIBLE);
        inital_image.setVisibility(View.GONE);
        upload_video_view.setVisibility(View.GONE);
        tag_people.setVisibility(View.VISIBLE);
        tag_people.setEnabled(true);
        add_location.setEnabled(true);
        tag_text.setTextColor(getResources().getColor(R.color.upload_text));
        location.setTextColor(getResources().getColor(R.color.upload_text));

    }

    //delete temporary video file
    private void deleteTempFile() {
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

    //save temporary video file
    private void saveTempFile(Uri selectedMediaUri) {

        if (selectedMediaUri != null) {
            Cursor cursor = getContentResolver().query(selectedMediaUri, null, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    String displayName = cursor.getString(
                            cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    int sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE);
                    String size = null;
                    if (!cursor.isNull(sizeIndex)) {
                        size = cursor.getString(sizeIndex);
                    } else {
                        size = "Unknown";
                    }
                    tempFile = FileUtils.saveTempFile(displayName, this, selectedMediaUri, getApplicationContext());

                    upload_image_path = tempFile.getPath();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

        }
    }

    //save image in gallery from bitmap
    private File bitmapToFile(Bitmap bmp) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File myDir = cw.getDir("Cache", Context.MODE_PRIVATE);
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
            Log.d("Screen_Upload_Photo", "return File path from bitmap" + file.getAbsolutePath());
            return file;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            Log.d("Screen_Upload_Photo", "getpath from uri : " + cursor.getString(column_index));
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    //set size of selected image
    private void setImagesize(Uri resultUri) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(resultUri.getPath()).getAbsolutePath(), options);
        int imageHeight = options.outHeight;
        int imageWidth = options.outWidth;

        Log.d("EXTRA", " image width : " + imageWidth + " image  height : " + imageHeight);
        float aspect = imageWidth / imageHeight;
        Log.d("EXTRA", "aspect : " + aspect);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        float width_screen = displayMetrics.widthPixels;
        float new_height = width_screen / aspect;

        Log.d("EXTRA", "new width : " + width_screen + "  height : " + new_height);
        RelativeLayout.LayoutParams layoutParams;
        if (aspect == 0.0) {
            layoutParams = new RelativeLayout.LayoutParams((int) width_screen, (int) new_height);
        } else {
            layoutParams = new RelativeLayout.LayoutParams((int) width_screen, (int) width_screen);
        }
        upload_image_view.setLayoutParams(layoutParams);
    }

    //save selected image from gallery to new folder and get uri
    private Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File directory = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Viegram/Media/ViegramImages");

        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create " + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imagename = "IMG_" + timeStamp + ".jpg";
        File file = new File(directory.getPath(), imagename);

        try {
            file.createNewFile();
            FileOutputStream fo = new FileOutputStream(file);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String path = null;
        path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, file.getPath(), imagename);
        Log.d("Screen_Upload_Photo", "get Image path from bitmap : " + path);

        return Uri.parse(path);
    }

    @Override
    public void onClick(View v) {
        if (v == play_video) {
            play_video.setVisibility(View.GONE);
            upload_image_view.setVisibility(View.GONE);
            upload_video_view.setVisibility(View.VISIBLE);
            upload_video_view.setVideoPath(upload_image_path);
            upload_video_view.requestFocus();
            upload_video_view.start();
        }
        if (v == add_caption_textbt) {
            caption_text.requestFocus();
        }
        if (v == activity_layout) {
            if (menu_open_layout.getVisibility() == View.VISIBLE) {
                menu_status();
            }
        }
        if (v == upload_edit_photo || v == upload_image_view || v == imageHolder) {
            final Dialog dialog = new Dialog(this);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.design_choose_photo);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            RelativeLayout choose_gallery, capture_image, remove_image;
            TextView upload_photo_text = (TextView) dialog.findViewById(R.id.upload_image_text);
            choose_gallery = (RelativeLayout) dialog.findViewById(R.id.choose_gallery);
            capture_image = (RelativeLayout) dialog.findViewById(R.id.capture_image);
            remove_image = (RelativeLayout) dialog.findViewById(R.id.remove_image);
            upload_photo_text.setText("Upload Photo or Video :");
            remove_image.setVisibility(View.GONE);
            choose_gallery.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    int permissionCheck = ContextCompat.checkSelfPermission(Upload_photo.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                    if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(Upload_photo.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_PERMISSION_CODE);
                    } else {

                        Intent intent =  new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("*/*");
                        intent.putExtra(Intent.EXTRA_MIME_TYPES, new String[] {"image/*", "video/*"});

                   //     Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                      //  intent.setType("video/*, image/*");
                   //     intent.setType("*/*");
                        startActivityForResult(intent, PICK_VIDEOIMAGE);
                        requestStoragePermission();
                        return;
                    }
                }
            });
            capture_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    if ((ContextCompat.checkSelfPermission(Upload_photo.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(Upload_photo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
                        captureImage();
                    } else {
                        ActivityCompat.requestPermissions(Upload_photo.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, CAPTURE_PERMISSION_CODE);
                    }
                }
            });

            dialog.show();
        }
        if (v == tag_people) {
            upload_image_view.measure(0, 0);
            Intent i = new Intent(Upload_photo.this, Tag_photo.class);
            i.putExtra("image_path", upload_image_path);
            i.putExtra("image_width", upload_image_view.getMeasuredWidth() + "");
            i.putExtra("image_height", upload_image_view.getMeasuredHeight() + "");
            startActivityForResult(i, TAG_CODE);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == upload_photo) {
            if (type.equals("repost")) {
                progressDialog.show();
                repost_photo();
            } else {
                if (!upload_image_path.equals("")) {
                    if (filetype.equals("video")) {
                        Log.i("image_path", upload_image_path);

                        new VideoCompressor().execute();
                    } else {
                        callUploadMethod();
                    }
                } else {
                    Alerter.create(Upload_photo.this)
                            .setText("Please select an image to upload..")
                            .setBackgroundColor(R.color.red)
                            .show();
                }
            }
        }

        if (v == back) {
            onBackPressed();
        }
        if (v == menu_home) {
            menu_status();
            deleteTempFile();
            Intent i = new Intent(Upload_photo.this, Timeline.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == add_location) {
            Intent i = new Intent(Upload_photo.this, Search_location.class);
            i.putExtra("location", location_result);
            startActivityForResult(i, LOCATION_CODE);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_camera) {
            menu_status();
            deleteTempFile();
        }
        if (v == menu_follow) {
            menu_status();
            deleteTempFile();
            Intent i = new Intent(Upload_photo.this, Follower_following.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_notifications) {
            menu_status();
            deleteTempFile();
            Intent i = new Intent(Upload_photo.this, Notifications.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_profile) {
            menu_status();
            deleteTempFile();
            Intent i = new Intent(Upload_photo.this, Profile.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_ranking) {
            menu_status();
            deleteTempFile();
            Intent i = new Intent(Upload_photo.this, Ranking.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_search) {
            menu_status();
            deleteTempFile();
            Intent i = new Intent(Upload_photo.this, Search.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_settings) {
            menu_status();
            deleteTempFile();
            Intent i = new Intent(Upload_photo.this, Settings.class);
            startActivity(i);
            overridePendingTransition(R.anim.enter, R.anim.exit);
        }
        if (v == menu_stat) {
            menu_status();
            deleteTempFile();
            Intent i = new Intent(Upload_photo.this, Stats.class);
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

    //hit repost api
    private void repost_photo() {

        GetViegramData service = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("action", "repost_post");
        postParam.put("postid", getIntent().getStringExtra("post_id"));
        postParam.put("post_userid", getIntent().getStringExtra("postuser_id"));
        postParam.put("repost_userid", preferences.getString("user_id", ""));
        postParam.put("repost_text", caption_text.getText().toString());
        Log.d("API_Response", "repost_post Parameters : " + postParam.toString());
        Call<API_Response> call = service.getPostActionResponse(postParam);
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    Log.d("API_Response", "repost_post Response : " + new Gson().toJson(response.body()));
                    Result result = response.body().getResult();

                    String msg = result.getMsg();
                    if (msg.equals("201")) {
                        Intent i = new Intent(Upload_photo.this, Timeline.class);
                        startActivity(i);
                    }
                } else
                    Log.e("API_Response", "repost_post Response : " + new Gson().toJson(response.errorBody()));
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                progressDialog.dismiss();
                Log.d("Tag", "repost_post failure : " + t.getMessage());
            }
        });
    }

    //upload image api
    private void upload_photo() {

        GetViegramData getResponse = RetrofitInstance.getRetrofitInstance().create(GetViegramData.class);
        Call<API_Response> call = null;
        RequestBody id =
                RequestBody.create(MediaType.parse("multipart/form-data"), preferences.getString("user_id", ""));
        RequestBody action =
                RequestBody.create(MediaType.parse("multipart/form-data"), "upload_photo");
        RequestBody caption =
                RequestBody.create(MediaType.parse("multipart/form-data"), caption_text.getText().toString());
        RequestBody file_type =
                RequestBody.create(MediaType.parse("multipart/form-data"), filetype);
        RequestBody restricted_status =
                RequestBody.create(MediaType.parse("multipart/form-data"), check_value);
        RequestBody tag_people =
                RequestBody.create(MediaType.parse("multipart/form-data"), result_id);
        RequestBody location =
                RequestBody.create(MediaType.parse("multipart/form-data"), location_result);
        RequestBody x_cordinates =
                RequestBody.create(MediaType.parse("multipart/form-data"), x_value_result);
        RequestBody y_cordinates =
                RequestBody.create(MediaType.parse("multipart/form-data"), y_value_result);
        File file1 = new File(upload_image_path);

        Log.d("Screen_Upload_Photo", "upload file : " + file1.getPath() + "-----type-----" + filetype);

        if (filetype.equals("image")) {
            Log.d("Screen_Upload_Photo", "image to be upload string :" + upload_image_path);
            File file = new File(upload_image_path);
            Log.d("Screen_Upload_Photo", "image to be upload file : " + file.getPath());
            RequestBody imagefile = RequestBody.create(MediaType.parse("image/* video/*"), file);
            ProgressRequestBody fileBody = new ProgressRequestBody(imagefile, this);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("filename", file.getName(), fileBody);
            call = getResponse.UploadImage(fileToUpload, action, id, caption, file_type, restricted_status, tag_people, location, x_cordinates, y_cordinates);
        } else {
            File file = new File(upload_image_path);
            Log.d("Screen_Upload_Photo", "video to be upload string : " + upload_image_path);
            Log.d("Screen_Upload_Photo", "video to be upload file : " + file.getPath());
            Log.d("Screen_Upload_Photo", "thumbnial : " + thumbnail);

            RequestBody videoThumbnail = RequestBody.create(MediaType.parse("*/*"), thumbnail);
            MultipartBody.Part thumbnailBody = MultipartBody.Part.createFormData("thumbnail", thumbnail.getName(), videoThumbnail);
            Log.d("Screen_Upload_Photo", "video thumbnail to be upload file : " + thumbnail);
            RequestBody imagefile = RequestBody.create(MediaType.parse("image/* video/*"), file);
            ProgressRequestBody fileBody = new ProgressRequestBody(imagefile, this);
            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("filename", file.getName(), fileBody);
            call = getResponse.UploadVideo(fileToUpload, thumbnailBody, action, id, caption, file_type, restricted_status, tag_people, location, x_cordinates, y_cordinates);
        }
        call.enqueue(new Callback<API_Response>() {
            @Override
            public void onResponse(Call<API_Response> call, retrofit2.Response<API_Response> response) {
                uploadprogressLayout.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    Result result = response.body().getResult();
                    Log.d("API_Response", "upload_photo" + new Gson().toJson(response.body().getResult()));
                    //Toast.makeText(getApplicationContext(), "User can1"+new Gson().toJson(response.body().getResult()), Toast.LENGTH_SHORT).show();

                    if (result.getMsg().equals("201")) {


                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(Upload_photo.this).setCancelable(false);
                        if (filetype.equals("video"))
                            alertDialog.setMessage(getResources().getString(R.string.upload_video_done));
                        else
                            alertDialog.setMessage(getResources().getString(R.string.upload_image_done));
                        alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                deleteTempFile();
                                dialogInterface.dismiss();
                                Intent intent = new Intent(Upload_photo.this, Timeline.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.enter, R.anim.exit);
                            }
                        });
                        alertDialog.show();
                    } else {
                        //  Toast.makeText(getApplicationContext(), "User can1", Toast.LENGTH_SHORT).show();

                        Alerter.create(Upload_photo.this)
                                .setText(R.string.network_error)
                                .setBackgroundColor(R.color.red)
                                .show();
                        setScreenClickable();
                    }
                } else {
                    // Toast.makeText(getApplicationContext(), "U2", Toast.LENGTH_SHORT).show();

                    setScreenClickable();
                    Alerter.create(Upload_photo.this)
                            .setText(R.string.network_error)
                            .setBackgroundColor(R.color.red)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<API_Response> call, Throwable t) {
                uploadprogressLayout.setVisibility(View.GONE);
                setScreenClickable();

                //  Toast.makeText(getApplicationContext(), "User 31", Toast.LENGTH_SHORT).show();

                Alerter.create(Upload_photo.this)
                        .setText(R.string.network_error)
                        .setBackgroundColor(R.color.red)
                        .show();
            }
        });
    }

    void uploadPhotoByService() {
        final String uploadId = UUID.randomUUID().toString();

        try {
            File file = new File(upload_image_path);
            final MultipartUploadRequest request =
                    new MultipartUploadRequest(this, uploadId, (RetrofitInstance.BASE_URL + "upload_object.php"))
                            .setMethod("POST")
                            .setUtf8Charset()
                            .setNotificationConfig(getNotificationConfig(uploadId, R.string.multipart_upload))
                            .setMaxRetries(MAX_RETRIES)
                            //.setCustomUserAgent(getUserAgent())
                            .setUsesFixedLengthStreamingMode(FIXED_LENGTH_STREAMING_MODE);
            request.addParameter("action", "upload_photo")
                    .addParameter("userid", (preferences.getString("user_id", "")))
                    .addParameter("caption", (caption_text.getText().toString()))
                    .addParameter("file_type", filetype)
                    .addParameter("restricted_status", check_value)
                    .addParameter("tag_people", result_id)
                    .addParameter("location", location_result)
                    .addParameter("x_cordinates", x_value_result)
                    .addParameter("y_cordinates", y_value_result)
                    .addFileToUpload(file.getAbsolutePath(), "filename");

            if (!(filetype.equals("image")))
                request.addFileToUpload(thumbnail.getAbsolutePath(), "thumbnail");

            request.startUpload();
            finish();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    protected UploadNotificationConfig getNotificationConfig(final String uploadId, @StringRes int title) {
        UploadNotificationConfig config = new UploadNotificationConfig();

        PendingIntent clickIntent = PendingIntent.getActivity(
                this, 1, new Intent(this, Timeline.class), PendingIntent.FLAG_UPDATE_CURRENT);

        config.setTitleForAllStatuses(getString(title))
                .setRingToneEnabled(true)
                .setClickIntentForAllStatuses(clickIntent)
                .setClearOnActionForAllStatuses(true);

        config.getProgress().message = getString(R.string.uploading);
        config.getProgress().iconResourceID = R.drawable.ic_upload;
        config.getProgress().iconColorResourceID = Color.BLUE;
        config.getProgress().actions.add(new UploadNotificationAction(
                R.drawable.ic_cancelled,
                getString(R.string.cancel_upload),
                NotificationActions.getCancelUploadAction(this, 1, uploadId)));

        config.getCompleted().message = getString(R.string.upload_success);
        config.getCompleted().iconResourceID = R.drawable.ic_upload_success;
        config.getCompleted().iconColorResourceID = Color.GREEN;

        config.getError().message = getString(R.string.upload_error);
        config.getError().iconResourceID = R.drawable.ic_upload_error;
        config.getError().iconColorResourceID = Color.RED;

        config.getCancelled().message = getString(R.string.upload_cancelled);
        config.getCancelled().iconResourceID = R.drawable.ic_cancelled;
        config.getCancelled().iconColorResourceID = Color.YELLOW;

        return config;
    }


    //open menu visibilty gone
    private void menu_status() {
        menu_open_layout.setVisibility(View.GONE);
        menu_click_view.setVisibility(View.VISIBLE);
    }

    //permission READ_EXTERNAL_STORAGE is granted or not
    private boolean isReadStorageAllowed() {
        int result = ContextCompat.checkSelfPermission(Upload_photo.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    //request to grant READ_EXTERNAL_STORAGE permission
    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Upload_photo.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        }
        ActivityCompat.requestPermissions(Upload_photo.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

    //intent to capture image
    private void captureImage() {
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
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
    }

    //check device support camera or not
    private boolean isDeviceSupportCamera() {
        // this device has a camera
// no camera on this device
        return getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    //preview captured image
    private void previewCapturedImage() {
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            CropImage.activity(fileUri)
                    .setCropShape(CropImageView.CropShape.RECTANGLE)
                    .setAspectRatio(1, 1)
                    .start(this);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    // save captured image to gallery and get uri
    private Uri getOutputMediaFileUri(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStorageDirectory().getAbsolutePath() + "/Viegram/Media/ViegramImages");
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
        String imagename = "IMG_" + timeStamp + ".jpg";
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath(), imagename);
        } else {
            return null;
        }
        if (Build.VERSION.SDK_INT >= 24) {
            return FileProvider.getUriForFile(Upload_photo.this,
                    BuildConfig.APPLICATION_ID + ".provider",
                    mediaFile);
        } else {
            return Uri.fromFile(mediaFile);
        }
    }

    //image or video uploading show progress
    @Override
    public void onProgress(final int progress) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator anim = ObjectAnimator.ofInt(circleProgress, "progress", progress);
                anim.setInterpolator(new DecelerateInterpolator());
                anim.setDuration(100);
                anim.start();
                if (progress == 100) {
                    Toast.makeText(Upload_photo.this, "Please wait... your post is uploading", Toast.LENGTH_SHORT).show();
                }
            }
//            progressC.setProgress(progress);
//        progressC.
        });

    }

    // video compression code
    private class VideoCompressor extends AsyncTask<Void, Void, Boolean> {
        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            pDialog = new ProgressDialog(Upload_photo.this);

            Log.d("Screen_Upload_Photo", "start video compression");
            pDialog.setMessage("Video is compressing");
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            /*File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + getPackageName() + "/media/videos");
            if (f.mkdirs() || f.isDirectory())
            Log.d("Screen_Upload_Photo", "upload_image_path : " + upload_image_path);
            try {
               // upload_image_path = SiliCompressor.with(Upload_photo.this).compressVideo(upload_image_path, f.getPath());
            } catch (Exception e) {
                Log.d("Screen_Upload_Photo", "exception : " + upload_image_path);

                e.printStackTrace();
            }*/
            try {
                return MediaController.getInstance().convertVideo(upload_image_path);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            // return  true;
        }

        @Override
        protected void onPostExecute(Boolean compressed) {
            super.onPostExecute(compressed);
            pDialog.dismiss();
            if (compressed) {
                upload_image_path = MediaController.getInstance().getNewPath();
            }

            if (upload_image_path != null) {
                File tempVideo = new File(upload_image_path);
                Bitmap thumb = ThumbnailUtils.createVideoThumbnail(tempVideo.getAbsolutePath(), ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
                Log.d("Screen_Upload_Photo", "error in thumbail of video  : " + thumb + "sdffffffffffffj" + thumbnail + "ghjgjh" + upload_image_path);

                if (thumbnail == null) {
                    thumbnail = bitmapToFile(thumb);
                }
                //      Log.d("Screen_Upload_Photo", "path of compressed video :" + upload_image_path);
                //   Log.d("Screen_Upload_Photo", "thumbnail  of compressed video :" + thumbnail.getAbsolutePath());
                callUploadMethod();
            }
        }
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    //call to method upload photo
    private void callUploadMethod() {
        uploadprogressLayout.setVisibility(View.VISIBLE);
        working_layout.setAlpha(0.3f);
        upload_image_view.setEnabled(false);
        tag_people.setEnabled(false);
        upload_edit_photo.setEnabled(false);
        restrict_layout.setEnabled(false);
        caption_text.setEnabled(false);
        add_location.setEnabled(false);

        upload_photo();
        //  uploadPhotoByService();

    }

    //setAll part clickable
    private void setScreenClickable() {
        working_layout.setAlpha(0.0f);
        upload_image_view.setEnabled(true);
        tag_people.setEnabled(true);
        upload_edit_photo.setEnabled(true);
        restrict_layout.setEnabled(true);
        caption_text.setEnabled(true);
        add_location.setEnabled(true);
    }


}
