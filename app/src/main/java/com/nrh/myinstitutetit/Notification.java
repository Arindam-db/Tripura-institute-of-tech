package com.nrh.myinstitutetit;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Notification extends Fragment {

    private RecyclerView recyclerView;
    private NotificationsAdapter adapter;
    private ArrayList<NotificationModel> notificationsList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false); // Inflate the layout


        recyclerView = view.findViewById(R.id.recycler_view_notifications); // Find recyclerView within the inflated view
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));



        // Load notifications from SharedPreferences
        notificationsList = loadNotifications();
        adapter = new NotificationsAdapter(notificationsList);
        recyclerView.setAdapter(adapter);

        // Add this line to ensure RecyclerView refreshes the data
        adapter.notifyDataSetChanged();

        return view;
    }



    private void addSampleNotification() {
        NotificationModel sampleNotification = new NotificationModel(
                "Sample Title",
                "This is a sample notification body.",
                System.currentTimeMillis()
        );

        // Save the sample notification to SharedPreferences
        SharedPreferences prefs = requireContext().getSharedPreferences("notifications", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        // Get the current list of notifications
        Gson gson = new Gson();
        String json = prefs.getString("notifications_list", "[]");
        Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
        ArrayList<NotificationModel> notificationsList = gson.fromJson(json, type);

        // Add the sample notification
        notificationsList.add(sampleNotification);

        // Save the updated list back to SharedPreferences
        editor.putString("notifications_list", gson.toJson(notificationsList));
        editor.apply();
    }





    private ArrayList<NotificationModel> loadNotifications() {
        SharedPreferences prefs = requireContext().getSharedPreferences("notifications", MODE_PRIVATE); // Same name as in MyFirebaseMessagingService
        Gson gson = new Gson();
        String json = prefs.getString("notifications_list", "[]"); // Check if notifications exist
        Type type = new TypeToken<ArrayList<NotificationModel>>() {}.getType();
        ArrayList<NotificationModel> notificationsList = gson.fromJson(json, type);

        // Log the loaded notifications to verify
        Log.d("FCM", "Loaded notifications: " + gson.toJson(notificationsList));

        return notificationsList;
    }
}