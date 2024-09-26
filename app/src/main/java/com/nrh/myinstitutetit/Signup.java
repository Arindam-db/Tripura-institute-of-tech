package com.nrh.myinstitutetit;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signup extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference reference;
    FirebaseAuth mAuth;
    EditText name, email, password;
    Spinner branch;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("users");

        name = findViewById(R.id.name_edit_text);
        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        branch = findViewById(R.id.branch_spinner);
        register = findViewById(R.id.register_button);

        // Check if the user is already logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            // If the user is already logged in, bypass this activity
            Intent intent = new Intent(Signup.this, HomePage.class);
            startActivity(intent);
            finish(); // Close Signup activity
        }

        register.setOnClickListener(view -> {
            if (!isInternetAvailable()) {
                Toast.makeText(Signup.this, "No internet connection. Please check your network.", Toast.LENGTH_SHORT).show();
                return;
            }

            String namestored = name.getText().toString();
            String emailstored = email.getText().toString();
            String passwordstored = password.getText().toString();
            String branchstored = branch.getSelectedItem().toString();

            if (namestored.isEmpty() || emailstored.isEmpty() || passwordstored.isEmpty()) {
                Toast.makeText(Signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Check if the user already exists
            reference.orderByChild("email").equalTo(emailstored).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // Email already registered
                        Toast.makeText(Signup.this, "An account with this email already exists.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signup.this, Login.class);
                        startActivity(intent);
                        finish();
                        register.setEnabled(true);  // Enable the button again
                    } else {
                        // Register the user
                        HelperClass helperClass = new HelperClass(namestored, emailstored, passwordstored, branchstored);
                        reference.child(namestored).setValue(helperClass);

                        Toast.makeText(Signup.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Signup.this, HomePage.class);
                        startActivity(intent);
                        finish(); // Close the Signup activity
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(Signup.this, "Failed to create account. Please try again.", Toast.LENGTH_SHORT).show();
                    register.setEnabled(true);  // Enable the button again
                }
            });
        });

        TextView loginRedirect = findViewById(R.id.login_redirect);

        loginRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(Signup.this, Login.class);
            startActivity(intent);
        });
    }

    // Helper method to check internet connectivity
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }
}
