package com.example.metaswitchgo.metaswitchgo;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
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
import java.util.Locale;

public class HomeScreen extends AppCompatActivity {

    Details d;
    boolean battle;
    boolean done;
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        addMetadexButton();
        addCameraButton();
        addBattleButton();

        Button camera_button = (Button) findViewById(R.id.signin);
        camera_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent openCamera = new Intent(HomeScreen.this, Signin.class);
                startActivity(openCamera);

            }

        });

        d = new Details();


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


        d.getAllInitials();

        new LongOperation().execute();


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
        d.addToCaughtInitials(initials);

    }


    public void getBattleResponse()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.print("POLLING");
                while (!battle){
                    try {
                        synchronized (this){
                            wait(2000);
                        }

                    } catch (InterruptedException e){

                    }
                    System.out.print("POLLING");
                    String path = "http://vac2/battle_request?my_initials=" + d.getMyInitials();
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
                            InputStream in = response.getEntity().getContent();
                            allInitials = convertStreamToString(in);
                            Log.i("received request?", allInitials);

                        }
                        battle = (allInitials.contains("true"));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                done = true;
                String path = "http://vac2/target_accept_battle";

                String allInitials = null;

                HttpClient client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
                // Limit
                HttpResponse response;
                JSONObject json = new JSONObject();
                try {
                    HttpPost post = new HttpPost(path);
                    json.put("initials", "INITIALS");
                    Log.i("jason Object", json.toString());
                    post.setHeader("json", json.toString());
                    StringEntity se = new StringEntity(json.toString());
                    se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                            "application/json"));
                    post.setEntity(se);
                    response = client.execute(post);
                    System.out.println("ACCEPTACCEPTACCEPT");
        /* Checking response */
                    if (response != null) {
                        InputStream in = response.getEntity().getContent();
                        allInitials = convertStreamToString(in);
                        Log.i("Read from Server", allInitials);


                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        thread.start();
    }

    private static String convertStreamToString(InputStream is) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if(status != TextToSpeech.ERROR) {
                        t1.setLanguage(Locale.UK);
                        t1.setPitch(4);
                        t1.setSpeechRate(2);
                        System.out.println("INITED");
                    }
                }
            });
            d.addTTS(t1);
            while (!battle){
                try {
                    synchronized (this){
                        wait(2000);
                    }

                } catch (InterruptedException e){

                }
                System.out.print("POLLING");
                String path = "http://vac2/battle_request?my_initials=" + d.getMyInitials();
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
                        InputStream in = response.getEntity().getContent();
                        allInitials = convertStreamToString(in);
                        Log.i("received request?", allInitials);

                    }
                    battle = (allInitials.contains("true"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(battle)
                {
                    path = "http://vac2/target_accept_battle";

                    allInitials = null;

                    client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
                    // Limit
                    json = new JSONObject();
                    try {
                        HttpPost post = new HttpPost(path);
                        json.put("initials", "INITIALS");
                        Log.i("jason Object", json.toString());
                        post.setHeader("json", json.toString());
                        StringEntity se = new StringEntity(json.toString());
                        se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,
                                "application/json"));
                        post.setEntity(se);
                        response = client.execute(post);
                        System.out.println("ACCEPTACCEPTACCEPT");
        /* Checking response */
                        if (response != null) {
                            InputStream in = response.getEntity().getContent();
                            allInitials = convertStreamToString(in);
                            Log.i("Read from Server", allInitials);


                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {

            Intent openMetadex = new Intent(HomeScreen.this, BattleScreen.class);
            startActivity(openMetadex);

        }

        @Override
        protected void onPreExecute() {}

        @Override
        protected void onProgressUpdate(Void... values) {}
    }


}
