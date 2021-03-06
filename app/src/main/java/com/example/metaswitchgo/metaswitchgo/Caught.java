package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by tcpg on 12/08/2016.
 */
public class Caught extends Activity{

    String initial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.caught_layout);
        Intent intent = getIntent();
        initial = intent.getStringExtra("Initials");

        TextView t = (TextView) findViewById(R.id.youcaught);
        t.setText("You caught " + initial);

        Button camera_button = (Button) findViewById(R.id.button);
        camera_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openCamera = new Intent(Caught.this, Metadex.class);
                startActivity(openCamera);

            }

        });

        // Load the ImageView that will host the animation and
        // set its background to our AnimationDrawable XML resource.
        ImageView img = (ImageView)findViewById(R.id.caughtimageview);
        img.setBackgroundResource(R.drawable.animated);

        // Get the background, which has been compiled to an AnimationDrawable object.
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();

        // Start the animation (looped playback by default).
        frameAnimation.start();
    }


}
