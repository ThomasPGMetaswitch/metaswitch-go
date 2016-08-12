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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

public class HomeScreen extends AppCompatActivity {

    Details d;
    boolean battle;
    boolean done;
    TextToSpeech t1;
    PollForBattle p;

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
        } else if (Intent.ACTION_MAIN.equals(action) && type != null) {
            if ("text/plain".equals(type)) {
                handleSendText(intent); // Handle text being sent
            }
        }


        d.getAllInitials();

        if (readFromFile(this) != null)
        {
            d.setMyInitials(readFromFile(this));
            if (!d.getCaughtInitials().contains(readFromFile(this)))
            {
                d.addToCaughtInitials(readFromFile(this));
            }

        }

        //new LongOperation().execute();

         p = new PollForBattle(this);
        p.execute();


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
                Intent openCamera = new Intent(HomeScreen.this, BattleLoading.class);
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

    @Override
    protected void onResume() {
        super.onResume();
        p.cancel(true);
        p = new PollForBattle(this);
        p.execute();
    }

    void handleSendText(Intent data) {
        // The person was identified.
        // The Intent's data string gives the initials    }
        String initials = getIntent().getStringExtra(Intent.EXTRA_TEXT);
        // Do something with the initals here
        System.out.println("You caught " + initials +"!");
        if(!d.getCaughtInitials().contains(initials))
        {
            d.addToCaughtInitials(initials);
        }

        Intent openCamera = new Intent(HomeScreen.this, Caught.class);
        openCamera.putExtra("Initials",initials);
        startActivity(openCamera);

    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("ONPAUSE");
        p.cancel(true);

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

    private String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    private class LongOperation extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

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
                    InputStream in = response.getEntity().getContent();
                    allInitials = convertStreamToString(in);
                    Log.i("set online?", allInitials);

                }
                battle = (allInitials.contains("true"));
            } catch (Exception e) {
                e.printStackTrace();
            }
            while (!battle){
                try {
                    synchronized (this){
                        Thread.sleep(3000);
                    }

                } catch (InterruptedException e){

                }
                System.out.print("POLLING");
                path = "http://vac2/battle_request?my_initials=" + d.getMyInitials();
                allInitials = null;

                client = new DefaultHttpClient();
                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
                // Limit
                json = new JSONObject();
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
