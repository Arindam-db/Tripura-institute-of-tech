package com.nrh.myinstitutetit;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        // Check if message contains a notification payload
        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            long timestamp = System.currentTimeMillis();

            // Save the notification locally
            saveNotification(new NotificationModel(title, body, timestamp));
        }
    }

    private void saveNotification(NotificationModel notification) {
        SharedPreferences prefs = getSharedPreferences("notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get the current list of notifications
        Gson gson = new Gson();
        String json = prefs.getString("notifications_list", "[]");
        Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
        ArrayList<NotificationModel> notificationsList = gson.fromJson(json, type);

        // Add the new notification
        notificationsList.add(notification);

        // Save it back
        editor.putString("notifications_list", gson.toJson(notificationsList));
        editor.apply();

        // Log the list to verify it is saved correctly
        Log.d("FCM", "Notification saved: " + gson.toJson(notificationsList));
    }
}
