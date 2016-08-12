package com.example.metaswitchgo.metaswitchgo;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by jz2 on 11/08/2016.
 */
public class Waiting extends Activity {
/* We want to check the outcome of the battle every 2 seconds. When the outcome is decided,
 * we go to the result page. */

    boolean result = false;
    boolean done = false;
    String mInitials;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting);

        Details d = new Details();
        mInitials = d.getMyInitials();
        Button back_button = (Button) findViewById(R.id.to_menu);
        RelativeLayout r = (RelativeLayout) findViewById(R.id.waitingbackground);
        r.setBackgroundResource(R.drawable.metawaiting);
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent back = new Intent(Waiting.this, HomeScreen.class);
                startActivity(back);
            }

        });

        getBattleResponse();
        while(!done)
        {

        }
        TextView t = (TextView) findViewById(R.id.textView3);
        switch (status){
            case(0):
                t.setText("");
                r.setBackgroundResource(R.drawable.metaloser1);
                break;
            case(1):
                t.setText("");
                r.setBackgroundResource(R.drawable.metadraw);
                break;
            case(2):
                t.setText("");
                r.setBackgroundResource(R.drawable.metawinner);
                break;
        }


        /* If there's no winner, we carry on waiting, otherwise we save the winners initials (which
        * should be returned by get_results), and go to the results screen.. */
    }

    public void getBattleResponse()
    {
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while(!result)
                {
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e)
                    {

                    }

                    System.out.print("POLLING");
                    System.out.print("POLLING");
                    String path = "http://vac2/get_results";
                    String allInitials = null;

                    HttpClient client = new DefaultHttpClient();
                    HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); // Timeout
                    // Limit
                    HttpResponse response;
                    JSONObject json = new JSONObject();
                    try {
                        HttpPost post = new HttpPost(path);
                        json.put("initials", "TCPG");
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
                            Log.i("is result true?", allInitials.toUpperCase());

                        }

                        if(allInitials.toUpperCase().contains("DRAW"))
                        {
                            status = 1;
                            System.out.println("Status = 1"+ result);
                            result = true;
                        } else if (allInitials.toUpperCase().contains(mInitials))
                        {
                            status = 2;
                            result = true;
                            System.out.println("Status = 2"+ result);

                        } else if (allInitials.toUpperCase().contains("NONE")){
                            System.out.println("NONE" + result);

                        }
                        else{
                            if(!allInitials.toUpperCase().contains("NULL"))
                            {
                                status = 0;
                                result = true;
                                System.out.println("Status = 0"+ result);
                            }
                            System.out.println("null oww :(");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                done = true;
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
