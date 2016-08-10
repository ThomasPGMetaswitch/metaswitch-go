package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

/**
 * Created by tcpg on 10/08/2016.
 */
public class Metadex extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metadex_layout);

        addBackButton();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void addBackButton() {
        Button back_button = (Button) findViewById(R.id.back_to_menu);
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent back = new Intent(Metadex.this, HomeScreen.class);
                startActivity(back);
            }

        });
    }
}
