package com.nrh.myinstitutetit;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText email, password;
    Button login_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        email = findViewById(R.id.email_id);
        password = findViewById(R.id.password);
        login_Button = findViewById(R.id.login_button);



        TextView RegisterRedirect = findViewById(R.id.register_redirect);

        RegisterRedirect.setOnClickListener(view -> {
            Intent intent = new Intent(Login.this, Signup.class);
            startActivity(intent);
        });



    }
    public Boolean validateEmail(){
        String val = email.getText().toString();
        if(val.isEmpty()){
            email.setError("Email cannot be empty");
            return false;
        }else{
            email.setError(null);
            return true;
        }
    }

    public Boolean validatePassword(){
        String val = password.getText().toString();
        if(val.isEmpty()){
            password.setError("Password cannot be empty");
            return false;
        }else{
            password.setError(null);
            return true;
        }
    }

    public void checkEmail(){
        String email_login = email.getText().toString().trim();
        String password_login = password.getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("email").equalTo(email_login);

        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    email.setError(null);
                    String passwordFromDB = snapshot.child(email_login).child("password").getValue(String.class);

                    if(!Objects.equals(passwordFromDB, password_login)){
                        email.setError(null);
                        Intent intent = new Intent(Login.this, HomePage.class);6
                    } else {
                        email.setError("Invalid Credentials");
                        email.requestFocus();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}