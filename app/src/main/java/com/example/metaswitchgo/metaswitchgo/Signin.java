package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by tcpg on 11/08/2016.
 */
public class Signin extends Activity {

    final Details d = new Details();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signin);
        final Context c = this;



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
                writeToFile(d.getMyInitials(),c);
                thread2.start();
            }
        });

        TextView t4 = (TextView) findViewById(R.id.textView4);
        t4.setText(d.getMyInitials());
    }

    private void writeToFile(String data,Context context) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    Thread thread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.print("POLLING");
            String path = "http://vac2/update_last_seen?initials=" + d.getMyInitials();
            System.out.println(path);
            String allInitials = null;

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
            // Limit
            HttpResponse response;
            JSONObject json = new JSONObject();
            try {
                HttpPost post = new HttpPost(path);
                json.put("initials", "whatever");
                Log.i("jason Object", json.toString());
                post.setHeader("json", json.toString());
                StringEntity se = new StringEntity(json.toString());
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                        "application/json"));
                post.setEntity(se);
                response = client.execute(post);
        /* Checking response */
                if (response != null) {

                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    });

}
