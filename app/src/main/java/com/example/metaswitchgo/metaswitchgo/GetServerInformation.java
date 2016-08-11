package com.example.metaswitchgo.metaswitchgo;

import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by tcpg on 10/08/2016.
 */
public class GetServerInformation {

    MetadexEntry mEntry;
    boolean done = false;

    public GetServerInformation(final MetadexEntry entry)
    {
        final String initials = null;
        mEntry = entry;
    }

    public Thread thread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            System.out.println("LOOKING UP ENTRY DETAILS");
            String path = "http://vac2/get_employee";
            String output = null;

            HttpClient client = new DefaultHttpClient();
            HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
            // Limit
            HttpResponse response;
            JSONObject json = new JSONObject();
            try {
                HttpPost post = new HttpPost(path);
                json.put("initials", "ES ");
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
                    output = convertStreamToString(in);
                    Log.i("Read from Server", output);

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            output = output.replace("{","");
            output = output.replace("}","");
            output = output.replace("\"","");
            String[] splitOutput = output.split(",");
            int typeOfOutput = splitOutput.length;
            for (int ii = 0; ii< typeOfOutput; ii++)
            {

                String[] variable = splitOutput[ii].split(": ");
                System.out.println(variable[0]);
                switch (variable[0])
                {
                    case("Team"):
                        mEntry.setmTeam(variable[1]);
                        break;
                    case("Can be found"):
                        mEntry.setmLocation(variable[1]);
                        break;
                    case("Name"):
                        mEntry.setmName(variable[1]);
                        break;
                    case("Directory photo"):
                        mEntry.setmPhoto(variable[1]);
                }
            }
            System.out.println("RESPONSE = " + output);
            System.out.println("SPLIT = " + mEntry.getmPhoto());

            //{"Team": "Support R&D - L3", "Can be found": "1st Floor Ross House", "Name": "Thomas Prideaux-Ghee"}
            done = true;


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
