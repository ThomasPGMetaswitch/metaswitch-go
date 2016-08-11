package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.util.Base64;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by tcpg on 10/08/2016.
 */
public class MetadexDetails extends Activity
{
    TextToSpeech t1;
    String toSpeak = "Harmabe";
    int index;
    ArrayList<MetadexEntry> caughtEntries;
    MetadexEntry mEntry;
    int width;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metadex_details_layout);

        Intent intent = getIntent();
        index = intent.getIntExtra("Index", 0);
        Details d = new Details();
        caughtEntries = d.getCaughtEntry();
        getScreenSize();
        addBackButton();

        mEntry = caughtEntries.get(index - 1);
        t1 = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.UK);
                    t1.setPitch(4);
                    t1.setSpeechRate(2);

                    t1.speak(mEntry.getmName(), TextToSpeech.QUEUE_FLUSH, null);
                }
            }
        });
        addDetails();









    }

    public void onPause(){
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(t1 !=null){
            t1.stop();
            t1.shutdown();
        }
    }

    public void addBackButton() {
        Button back_button = (Button) findViewById(R.id.back_to_metadex);
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent back = new Intent(MetadexDetails.this, Metadex.class);
                startActivity(back);
            }

        });
    }

    public void addDetails() {
        ImageView img = (ImageView) findViewById(R.id.photo);

        String base64 = mEntry.getmPhoto();
        System.out.println("ENTRY DETAILS" + mEntry.getmInitials());
        System.out.println("PHOTO DETAILS" + base64);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);

        Bitmap b = BitmapFactory.decodeByteArray(data, 0 ,data.length);
        float ratio = (float) b.getHeight()/ (float) b.getWidth();
        b = Bitmap.createScaledBitmap(b, 100, 100, false);


        BitmapDrawable bdrawable = new BitmapDrawable(this.getResources(),b);
        img.setBackground(bdrawable);
        img.setMaxWidth(width/2);
        img.setMinimumWidth(width/2);
        img.setMaxHeight((int) (width/2 * ratio));
        img.setMinimumHeight((int) (width/2 * ratio));

        TextView name = (TextView) findViewById(R.id.name);
        name.setText(mEntry.getmName());

        TextView job = (TextView) findViewById(R.id.team);
        job.setText((mEntry.getmTeam()));

        TextView location = (TextView) findViewById(R.id.location);
        location.setText(mEntry.getmLocation());
        System.out.println("SET TEXT = " + mEntry.getmInitials());
    }

    private void getScreenSize()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }

}
