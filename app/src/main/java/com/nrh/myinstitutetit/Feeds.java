package com.nrh.myinstitutetit;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;public class Feeds extends Fragment {

    FloatingActionButton fab;
    private MyAdapter myAdapter;
    private List<DataClass> dataList;
    private DatabaseReference databaseReference;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feeds, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dataList = new ArrayList<>(); // Initialize dataList here
        myAdapter = new MyAdapter(getContext(), dataList); // Initialize myAdapter here
        recyclerView.setAdapter(myAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("Images");

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(this::fetchImagesAndCaptions);

        fetchImagesAndCaptions(); // Call only once

        fab = view.findViewById(R.id.upload_now);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), UploadActivity.class);
            startActivity(intent);
        });

        return view;
    }

    private void fetchImagesAndCaptions() {
        swipeRefreshLayout.setRefreshing(true); // Show refresh indicator

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                dataList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DataClass data = snapshot.getValue(DataClass.class);
                    if (data != null && data.getImageUrl() != null && !data.getImageUrl().isEmpty()) {
                        dataList.add(data);
                    }
                }

                Collections.reverse(dataList); // Reverse the list

                myAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
}