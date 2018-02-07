package com.relinns.viegram.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.relinns.viegram.Activity.Another_user;
import com.relinns.viegram.Activity.Comments;
import com.relinns.viegram.Activity.Notifications;
import com.relinns.viegram.Activity.Open_photo;
import com.relinns.viegram.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Random;

import me.leolin.shortcutbadger.ShortcutBadger;

/**
 * Created by win 7 on 6/14/2017.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private SharedPreferences preferences;
    private static final String TAG = "Firebase";
    private JSONObject data;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        preferences = getSharedPreferences("Viegram", MODE_PRIVATE);
        Log.d("Notification_service", "data :" + remoteMessage.getData());

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            int badge_value = preferences.getInt("badge_value", 0);
            badge_value++;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("badge_value", badge_value);
            editor.commit();
            ShortcutBadger.applyCount(getApplicationContext(), badge_value);

            Log.d("Notification_008", remoteMessage.getData() + "");
            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                Log.d("json", json + "");

                try {
                    data = json.getJSONObject("data");

                    Log.d("jsonobject", data.toString());

                    String title = data.getString("title");
                    String message = data.getString("message");
                    String type = data.getString("status");
                    Log.d("Notification_007", title + "," + message);
                    sendNotification1(title, message, type);

                } catch (JSONException e) {
                    Log.e("Json Exception:", "" + e.getMessage());
                } catch (Exception e) {
                    Log.e("Exception:", "" + e.getMessage());
                }

            } catch (Exception e) {
                Log.e("Exception:", "" + e.getMessage());
            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
    }


    private void sendNotification1(String title, String message, String type) {

        Log.d("Notification_message", message);

        if (type.equals("2")) {
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Comments.class);
            in.putExtra("decrement", true);

            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString("post_id", data.getString("postid"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());

        }

        if (type.equals("8")) {

            Log.d("parkash001", "run");

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Comments.class);
            //    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            in.putExtra("decrement", true);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString("post_id", data.getString("postid"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_ONE_SHOT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());

        }

        if (type.equals("1")) {

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Open_photo.class);
            in.putExtra("decrement", true);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString("post_id", data.getString("postid"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());

        }
        if (type.equals("9")) {

            Log.d("parkash002", "run");

            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Open_photo.class);
            in.putExtra("decrement", true);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString("post_id", data.getString("postid"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());

        }

        if (type.equals("4")) {
            Log.d("parkash003", "run");
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Open_photo.class);
            in.putExtra("decrement", true);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString("post_id", data.getString("postid"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());

        }
        if (type.equals("6")) {
            Log.d("parkash004", "run");
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Another_user.class);
            in.putExtra("decrement", true);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString("another_user", data.getString("userid"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());

        }

        if (type.equals("3")) {
            Log.d("parkash004", "run");
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Another_user.class);
            in.putExtra("decrement", true);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString("post_id", data.getString("postid"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());

        }

        if (type.equals("5")) {
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Notifications.class);
            in.putExtra("decrement", true);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());

        }

        if (type.equals("7")) {

            Log.d("parkash004", "run");
            Random random = new Random();
            int m = random.nextInt(9999 - 1000) + 1000;
            Intent in = new Intent(this, Another_user.class);
            in.putExtra("decrement", true);
            SharedPreferences.Editor editor = preferences.edit();
            try {
                editor.putString("post_id", data.getString("postid"));
                editor.commit();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PendingIntent pendingIntent = PendingIntent.getActivity(this, m, in, PendingIntent.FLAG_UPDATE_CURRENT);
            NotificationCompat.Builder notifiBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.app_icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(m, notifiBuilder.build());
        }

    }

//    private void handleNotification(String message) {
//        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//            // app is in foreground, broadcast the push message
//            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//            pushNotification.putExtra("message", message);
//            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//            // play notification sound
//            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
////            notificationUtils.playNotificationSound();
//        } else {
//            // If the app is in background, firebase itself handles the notification
//        }
//    }
//
//    private void handleDataMessage(JSONObject json) {
//        Log.e(TAG, "push json: " + json.toString());
//
//        try {
//            JSONObject data = json.getJSONObject("data");
//
//            String title = data.getString("title");
//            String message = data.getString("message");
//            boolean isBackground = data.getBoolean("is_background");
//            String imageUrl = data.getString("image");
//            String timestamp = data.getString("timestamp");
//            JSONObject payload = data.getJSONObject("payload");
//
//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);
//
//
//            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
//                // app is in foreground, broadcast the push message
//                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
//                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);
//
//                // play notification sound
//                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
////                notificationUtils.playNotificationSound();
//            } else {
//                // app is in background, show the notification in notification tray
//                Intent resultIntent = new Intent(getApplicationContext(), Notifications.class);
//                resultIntent.putExtra("message", message);
//
//                // check for image attachment
//                if (TextUtils.isEmpty(imageUrl)) {
//                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//                } else {
//                    // image is present, show notification with image
//                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//                }
//            }
//        } catch (JSONException e) {
//            Log.e(TAG, "Json Exception: " + e.getMessage());
//        } catch (Exception e) {
//            Log.e(TAG, "Exception: " + e.getMessage());
//        }
//    }
//
//    /**
//     * Showing notification with text only
//     */
//    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
//    }
//
//    /**
//     * Showing notification with text and image
//     */
//    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
//        notificationUtils = new NotificationUtils(context);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
//    }
//
//    @Override
//    protected Intent zzF(Intent intent) {
//        return null;
//    }
}