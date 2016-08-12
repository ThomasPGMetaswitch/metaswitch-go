package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;

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

/**
 * Created by tcpg on 12/08/2016.
 */
class PollForBattle extends AsyncTask<String, Void, String> {

    Details d;
    boolean battle;
    boolean done;
    static Context mContext;
    TextToSpeech t1;

    public PollForBattle(Context c){
        d = new Details();
        mContext = c;
    }

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
            if(isCancelled())
            {
                break;
            }
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

        Intent intent = new Intent();
        intent.setClass(mContext, BattleScreen.class);
        mContext.startActivity(intent);

    }

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}


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

}
