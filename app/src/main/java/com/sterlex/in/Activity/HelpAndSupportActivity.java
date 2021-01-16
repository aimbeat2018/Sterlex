package com.sterlex.in.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.sterlex.in.R;

public class HelpAndSupportActivity extends AppCompatActivity {

    ImageView img_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);
        init();
        onClick();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void init() {
        img_back = findViewById(R.id.img_back);
    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 9819122200"));
        startActivity(intent);
    }
}
