package com.nrh.myinstitutetit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class Profile extends Fragment {

    TextView name, email, branch;
    Button logoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        name = view.findViewById(R.id.profile_name);
        email = view.findViewById(R.id.profile_email);
        branch = view.findViewById(R.id.profile_branch);
        logoutButton = view.findViewById(R.id.logout_button);

        showUserData();

        // Set up the logout button functionality
        logoutButton.setOnClickListener(v -> logOut());

        return view;
    }

    // Method to show user data in the profile
    public void showUserData() {
        if (getActivity() != null) {
            SharedPreferences prefs = getActivity().getSharedPreferences(Login.getPrefsName(), getActivity().MODE_PRIVATE);

            // Retrieve the user data from SharedPreferences
            String nameUser = prefs.getString("name", "Default Name");
            String emailUser = prefs.getString("email", "Default Email");
            String branchUser = prefs.getString("branch", "Default Branch");

            name.setText(nameUser);
            email.setText(emailUser);
            branch.setText(branchUser);
        }
    }

    // Method to handle logout
    private void logOut() {
        // Clear SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences(Login.getPrefsName(), getActivity().MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Login.getKeyIsLoggedIn(), false); // Mark as logged out
        editor.apply();

        // Sign out from Firebase (optional if you're using FirebaseAuth)
        FirebaseAuth.getInstance().signOut();

        // Redirect to login screen
        Intent intent = new Intent(getActivity(), Login.class);
        startActivity(intent);
        getActivity().finish(); // Close the current activity
    }
}
