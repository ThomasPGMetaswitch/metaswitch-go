package com.example.metaswitchgo.metaswitchgo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HomeScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        addMetadexButton();
        addCameraButton();
        addBattleButton();

        Details d = new Details();
        d.getAllInitials();
        //
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
                Intent openCamera = new Intent(HomeScreen.this, CatchActivity.class);
                startActivity(openCamera);
            }

        });
    }

    public void addBattleButton() {
        Button battle_button = (Button) findViewById(R.id.battle_button);
        battle_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openCamera = new Intent(HomeScreen.this, Battle.class);
                startActivity(openCamera);
            }

        });
    }

}
