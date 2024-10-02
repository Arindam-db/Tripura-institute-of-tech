package com.nrh.myinstitutetit;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Feeds extends Fragment {

    FloatingActionButton fab;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout and return the view
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        // Initialize the FloatingActionButton
        fab = view.findViewById(R.id.fab_add);

        // Set a click listener for the FAB
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), UploadActivity.class);
                startActivity(intent);
                // Handle FAB click event
                // You can open a new activity or dialog for sharing the feed here
            }
        });

        return view; // Return the inflated view
    }
}
