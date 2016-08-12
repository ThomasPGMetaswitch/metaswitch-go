package com.example.metaswitchgo.metaswitchgo;

import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Comparator;

/**
 * Created by tcpg on 11/08/2016.
 */
public class Details {

    static TextToSpeech mTTS;

    static String myInitials = "TCPG";

    static ArrayList<String>  caughtInitials = new ArrayList<>();

    static ArrayList<String> everyInitial = new ArrayList<>();

    static ArrayList<MetadexEntry> caughtEntry = new ArrayList<>();

    static AsyncTask b;

    public Details()
    {

    }

    public void addTTS(TextToSpeech t1)
    {
        mTTS = t1;
    }

    public TextToSpeech getTTS()
    {
        return mTTS;
    }

    public String getMyInitials()
    {
        return myInitials;
    }

    public static ArrayList<String> getCaughtInitials() {

        Collections.sort(caughtInitials, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        return caughtInitials;
    }

    public void setMyInitials(String init)
    {
        myInitials = init;
    }

    public static void addToCaughtInitials(String initials)
    {
        caughtInitials.add(initials.toUpperCase());
    }

    public static ArrayList<MetadexEntry> getCaughtEntry()
    {
        Collections.sort(caughtEntry, new Comparator<MetadexEntry>() {
            @Override
            public int compare(MetadexEntry s1, MetadexEntry s2) {
                return s1.getmInitials().compareToIgnoreCase(s2.getmInitials());
            }
        });

        return caughtEntry;
    }

    public static ArrayList<String> getEveryInitial() {

        Collections.sort(everyInitial, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareToIgnoreCase(s2);
            }
        });

        return everyInitial;
    }

    public void getAllInitials()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                String path = "http://vac2/get_employee";
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

                allInitials = allInitials.replace("]","");
                allInitials = allInitials.replace("ufeffInitials","");
                allInitials = allInitials.replace("[","");
                allInitials = allInitials.replace(" ", "");
                allInitials = allInitials.replace("\"","");
                String [] split = allInitials.split(",");
                for (int ii = 1; ii < split.length; ii++)
                {
                    everyInitial.add(split[ii]);
                    if (caughtInitials.contains(split[ii]))
                    {
                        MetadexEntry newEntry = new MetadexEntry(split[ii]);

                        boolean add = true;
                        for(int kk = 0; kk < caughtEntry.size(); kk++)
                        {
                            if (caughtEntry.get(kk).getmInitials() == split[ii])
                            {
                                System.out.println("ALREADY CONTAINS");
                                add = false;
                                break;
                            }
                        }
                        if(add){
                            System.out.println("ADDING NEW PERSON" + split[ii]);
                            caughtEntry.add(newEntry);
                        }

                        String output = null;

                        try {
                            HttpPost post = new HttpPost(path);
                            if(newEntry.getmInitials().length() == 2)
                            {
                                newEntry.setmInitials(newEntry.getmInitials() + " ");
                            }
                            json.put("initials", newEntry.getmInitials());
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
                        for (int jj = 0; jj< typeOfOutput; jj++)
                        {

                            String[] variable = splitOutput[jj].split(": ");
                            System.out.println(variable[0]);
                            switch (variable[0])
                            {
                                case(" Team"):
                                    newEntry.setmTeam(variable[1]);
                                    break;
                                case(" Can be found"):
                                    newEntry.setmLocation(variable[1]);
                                    break;
                                case(" Name"):
                                    newEntry.setmName(variable[1]);
                                    break;
                                case("Directory photo"):
                                    System.out.println(variable[1]);
                                    newEntry.setmPhoto(variable[1]);
                            }
                        }
                    }
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


}
