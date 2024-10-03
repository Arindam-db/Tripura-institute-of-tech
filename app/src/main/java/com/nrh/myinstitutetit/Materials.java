package com.nrh.myinstitutetit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

public class Materials extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_materials, container, false);

        ImageView telegramButton = view.findViewById(R.id.telegram_button);
        telegramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTelegramLink();
            }
        });

        return view;
    }

    private void openTelegramLink() {
        String url = "https://t.me/titquestionbank"; // Replace with your Telegram group link
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}