package com.nrh.myinstitutetit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Apply window insets but avoid adding extra padding to the Lottie animation view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());

            // Only set padding to the ConstraintLayout, not the LottieAnimationView
            View mainView = findViewById(R.id.constraintLayout2);  // Assuming constraintLayout2 needs the insets
            mainView.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);

            // Make sure the LottieAnimationView remains full-screen
            View animationView = findViewById(R.id.animationView);
            animationView.setPadding(0, 0, 0, 0);  // Ensures no extra padding on the animation view

            return insets;
        });

        Button Begin = findViewById(R.id.materialButton);

        Begin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle the button click here
                Intent intent = new Intent(MainActivity.this, Login.class);

                // Start the new activity
                startActivity(intent);

            }});
    }
}
