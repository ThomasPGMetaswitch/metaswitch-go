package com.example.metaswitchgo.metaswitchgo;
        import android.content.Intent;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.View;
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
        import java.io.UnsupportedEncodingException;
        import java.net.URLDecoder;

public class BattleScreen extends AppCompatActivity {

    Button selectWater;
    Button selectFire;
    Button selectLeaf;
    String mAttack = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.battle_view_layout);
        sendFireMove();
        sendLeafMove();
        sendWaterMove();

    }

    public void sendWaterMove() {
        Button waterButton = (Button) findViewById(R.id.waterbutton);

        waterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAttack = "water";
                thread2.start();
                Intent openMetadex = new Intent(BattleScreen.this, Waiting.class);
                startActivity(openMetadex);
            }
        });
    }

    public void sendFireMove() {
        Button fireButton = (Button) findViewById(R.id.firebutton);
        fireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAttack = "fire";
                thread2.start();
                Intent openMetadex = new Intent(BattleScreen.this, Waiting.class);
                startActivity(openMetadex);
            }
        });
    }

    public void sendLeafMove() {
        Button leafButton = (Button) findViewById(R.id.leafbutton);
        leafButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAttack = "leaf";
                thread2.start();
                Intent openMetadex = new Intent(BattleScreen.this, Waiting.class);
                startActivity(openMetadex);
            }
        });
    }

    Thread thread2 = new Thread(new Runnable() {
        @Override
        public void run() {
            System.out.println("Thread started");
            Details d = new Details();

            String path = "http://vac2/send_attack?player_initials=" + d.getMyInitials() + "&attack=" + mAttack;

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
