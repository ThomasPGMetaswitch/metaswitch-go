package com.example.metaswitchgo.metaswitchgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        addMetadexButton();
        addCameraButton();

    }

    public void addMetadexButton() {
        Button metadex_button = (Button) findViewById(R.id.metadex_button);
        metadex_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openMetadex = new Intent(HomeScreen.this, Metadex.class);
                startActivity(openMetadex);
            }

        });
    }

    public void addCameraButton() {
        Button camera_button = (Button) findViewById(R.id.camera_button);
        camera_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openCamera = new Intent(HomeScreen.this, Camera.class);
                startActivity(openCamera);
            }

        });
    }




}
