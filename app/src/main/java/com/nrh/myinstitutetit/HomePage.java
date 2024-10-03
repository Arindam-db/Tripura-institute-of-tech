package com.nrh.myinstitutetit;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.messaging.FirebaseMessaging;

public class HomePage extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FrameLayout frameLayout;

    // ActivityResultLauncher for permission handling
    private ActivityResultLauncher<String> resultLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    // Permission Granted
                    // Get Device token from Firebase
                    getDeviceToken();
                } else {
                    // Permission Denied
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        EdgeToEdge.enable(this);

        // Handle edge-to-edge content
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Request permissions and check login state
        requestPermission();
        checkLoginState();

        // Setup bottom navigation and fragments
        bottomNavigationView = findViewById(R.id.bottom_NavView);
        frameLayout = findViewById(R.id.frame_layout);

        bottomNavigationView.setBackgroundColor(getResources().getColor(R.color.nav_bar_color));

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            int itemId = item.getItemId();

            if (itemId == R.id.nav_feeds) {
                loadFragment(new Feeds(), false);
            } else if (itemId == R.id.nav_materials) {
                loadFragment(new Materials(), false);
            } else if (itemId == R.id.nav_notifications) {
                loadFragment(new Notification(), false);
            } else { // nav profile
                loadFragment(new Profile(), false);
            }

            return true;
        });

        // Load initial fragment (Feeds)
        loadFragment(new Feeds(), true);
    }

    // Method to request POST_NOTIFICATIONS permission
    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                // Permission already granted
                getDeviceToken();
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // Explain to the user why you need the permission (if needed)
            } else {
                // Request for permission
                resultLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            // For older versions, get the device token directly
            getDeviceToken();
        }
    }

    // Method to get Firebase device token
    private void getDeviceToken() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (!task.isSuccessful()) {
                    Log.e("FireBaseLogs", "Fetching token failed", task.getException());
                    return;
                }

                // Get device token
                String token = task.getResult();
                Log.v("FireBaseLogs", "Device Token: " + token);

                // Subscribe the user to the "all_users" topic
                FirebaseMessaging.getInstance().subscribeToTopic("all_users")
                        .addOnCompleteListener(subscribeTask -> {
                            if (subscribeTask.isSuccessful()) {
                                Log.d("FCM", "Subscribed to topic: all_users");
                                //Toast.makeText(HomePage.this, "Subscribed to topic", Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("FCM", "Subscription failed");
                                Toast.makeText(HomePage.this, "Subscription failed", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }

    // Method to check the user's login state
    private void checkLoginState() {
        SharedPreferences prefs = getSharedPreferences(Login.getPrefsName(), MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(Login.getKeyIsLoggedIn(), false);

        if (!isLoggedIn) {
            // User is not logged in, redirect to Login activity
            Intent intent = new Intent(HomePage.this, Login.class);
            startActivity(intent);
            finish(); // Close HomePage activity
        }
    }

    // Method to load fragments into the FrameLayout
    private void loadFragment(Fragment fragment, boolean isAppInitialised) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (isAppInitialised) {
            fragmentTransaction.add(R.id.frame_layout, fragment);
        } else {
            fragmentTransaction.replace(R.id.frame_layout, fragment);
        }
        fragmentTransaction.commit();
    }
}
