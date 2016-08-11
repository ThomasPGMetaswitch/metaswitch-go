package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.os.Bundle;
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
 * Created by tcpg on 11/08/2016.
 */
public class Battle extends Activity{

    boolean done = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        thread.start();
        while(!done){

        }

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
}
