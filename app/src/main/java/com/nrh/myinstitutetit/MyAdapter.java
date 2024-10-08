package com.nrh.myinstitutetit;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private List<DataClass> dataList;
    private Context context;
    private DatabaseReference databaseReference;

    public MyAdapter(Context context, List<DataClass> dataList) {
        this.context = context;
        this.dataList = dataList;
        databaseReference = FirebaseDatabase.getInstance().getReference("Images");
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass data = dataList.get(position);

        // Load image using Glide
        Glide.with(context).load(data.getImageUrl()).into(holder.recyclerImage);
        holder.recyclerCaption.setText(data.getCaption());

        // Handle delete button click with confirmation
        holder.deleteButton.setOnClickListener(v -> {
            // Show confirmation dialog before deleting
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Delete")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete the item if confirmed
                        deleteItemFromFirebase(data.getKey(), position);
                    })
                    .setNegativeButton("No", (dialog, which) -> {
                        // Dismiss the dialog if canceled
                        dialog.dismiss();
                    })
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    // Method to delete the item from Firebase
    private void deleteItemFromFirebase(String key, int position) {
        databaseReference.child(key).removeValue().addOnSuccessListener(aVoid -> {
            // Remove item from local list
            dataList.remove(position);

            // Notify adapter that item has been removed
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, dataList.size()); // Update the positions of remaining items
        }).addOnFailureListener(e -> {
            // Handle errors
            Toast.makeText(context, "Error deleting item: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView recyclerImage;
        TextView recyclerCaption;
        ImageButton deleteButton; // Add ImageButton for delete

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recyclerImage = itemView.findViewById(R.id.recycler_image);
            recyclerCaption = itemView.findViewById(R.id.recyclerCaption);
            deleteButton = itemView.findViewById(R.id.deleteButton); // Reference to delete button
        }
    }
}
