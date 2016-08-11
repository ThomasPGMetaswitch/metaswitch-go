package com.example.metaswitchgo.metaswitchgo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

        // Get intent, action and MIME type
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        } else {

        }

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
                //Intent openCamera = new Intent(HomeScreen.this, MainActivity.class);
                //startActivity(openCamera);
                openApp(HomeScreen.this ,"com.luxand.facerecognition");
                finish();
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

    public static boolean openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent i = manager.getLaunchIntentForPackage(packageName);
        if (i == null) {
            return false;
            //throw new PackageManager.NameNotFoundException();
        }
        i.addCategory(Intent.CATEGORY_LAUNCHER);
        context.startActivity(i);
        return true;
    }
    void handleSendText(Intent data) {
        // The person was identified.
        // The Intent's data string gives the initials    }
        String initials = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        // Do something with the initals here
        System.out.println("You caught " + initials +"!");
    }

}
