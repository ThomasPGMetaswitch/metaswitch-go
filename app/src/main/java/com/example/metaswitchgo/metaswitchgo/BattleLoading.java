package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

/**
 * Created by tcpg on 12/08/2016.
 */
public class BattleLoading extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_loading);

        Button camera_button = (Button) findViewById(R.id.button);
        camera_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openCamera = new Intent(BattleLoading.this, Battle.class);
                startActivity(openCamera);

            }

        });

        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        ImageView img = (ImageView)findViewById(R.id.loadingbattle);
        img.setBackgroundResource(R.drawable.animated_battle);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();
    }
}
