package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * Created by tcpg on 11/08/2016.
 */
public class Battle extends Activity{

    boolean done = false;
    String[] onlineInitials;
    String mBattler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_layout_splash);
        thread.start();
        while(!done){

        }
        addOnlineProviders();

    }

    public Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            String path = "http://vac2/see_online";
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
        /* Checking response */
                if (response != null) {
                    InputStream in = response.getEntity().getContent();
                    allInitials = convertStreamToString(in);
                    Log.i("Read from Server", allInitials);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            allInitials = allInitials.replace(" ", "");
            allInitials = allInitials.replace("\\",  "");
            allInitials = allInitials.replace("n","");
            allInitials = allInitials.replace("[","");
            allInitials = allInitials.replace("]","");
            allInitials = allInitials.replace("\"","");
            System.out.println(allInitials);
            onlineInitials = allInitials.split(",");



            done = true;
            //"[\n    \"HAR\",\n    \"RPO\"\n]"
        }
    });

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

    public void addOnlineProviders()
    {
        int number = onlineInitials.length;
        LinearLayout ll = (LinearLayout) findViewById(R.id.battle_splash);

        for (int ii = 0; ii < number; ii++)
        {
            final int initial = ii;
            Button b = new Button(this);
            b.setText(onlineInitials[initial]);
            b.setOnClickListener(new View.OnClickListener() {


                @Override
                public void onClick(View arg0) {
                    mBattler = onlineInitials[initial];
                    thread2.start();
                    Intent openMetadex = new Intent(Battle.this, WaitingForBattle.class);
                    startActivity(openMetadex);
                }

            });
            ll.addView(b);
        }

    }

    Thread thread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("Thread started");
            Details d = new Details();

            String path = "http://vac2/battle?primary_initials=";
            String toEncode1 = d.getMyInitials();
            String toEncode2 = mBattler.trim();
            try {
                toEncode1 = URLDecoder.decode(toEncode1,"UTF-8");
                toEncode2 = URLDecoder.decode(toEncode2,"UTF-8");
            } catch (UnsupportedEncodingException e){
                e.printStackTrace();

            }
            path = path + toEncode1 + "&target_initials=" + toEncode2;

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
}
