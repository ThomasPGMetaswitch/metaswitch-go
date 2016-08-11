package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by tcpg on 11/08/2016.
 */
public class Signin extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);

        final Details d = new Details();

        Button back_button = (Button) findViewById(R.id.menu);
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent back = new Intent(Signin.this, HomeScreen.class);
                startActivity(back);
            }

        });




        Button mButton = (Button)findViewById(R.id.button1);

        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                EditText name = (EditText) findViewById(R.id.editText);
                name.getText().toString();
                System.out.println("NAME = " + name.getText().toString());
                d.setMyInitials(name.getText().toString());
            }
        });
    }
}
