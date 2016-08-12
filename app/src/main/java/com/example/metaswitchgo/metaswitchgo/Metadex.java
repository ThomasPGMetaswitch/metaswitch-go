package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Base64;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import java.util.ArrayList;

/**
 * Created by tcpg on 10/08/2016.
 */
public class Metadex extends Activity{

    private int mNumberOfEmployees;
    private int itemsPerRow = 4;
    BitmapDrawable bdrawable;
    int width;
    float ratio;
    ArrayList<MetadexEntry> caughtEntries;
    PollForBattle p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.metadex_layout);

        Details d = new Details();
        caughtEntries = d.getCaughtEntry();
        for (int ii = 0; ii<caughtEntries.size(); ii++)
        {
            System.out.println(caughtEntries.get(ii).mInitials);
        }

        mNumberOfEmployees = caughtEntries.size();
        System.out.println("NUMBER" + mNumberOfEmployees);


        getScreenSize();

        addBackButton();
        addImages();
        p = new PollForBattle(this);
        p.execute();
    }

    private void getScreenSize()
    {
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        width = size.x;
    }


    public void addBackButton() {
        Button back_button = (Button) findViewById(R.id.back_to_menu);
        back_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent back = new Intent(Metadex.this, HomeScreen.class);
                startActivity(back);
            }

        });
    }

    private void addNewButton(LinearLayout row, final int index)
    {
        ImageView details_button = new ImageView(this);
        bdrawable = new BitmapDrawable(this.getResources(),decode(index));
        details_button.setBackground(bdrawable);
        details_button.setMaxWidth(width/itemsPerRow);
        details_button.setMinimumWidth(width/itemsPerRow);
        details_button.setMaxHeight((int) (width/itemsPerRow * ratio));
        details_button.setMinimumHeight((int) (width/itemsPerRow * ratio));

        details_button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent details = new Intent(Metadex.this, MetadexDetails.class);
                details.putExtra("Index", index);
                startActivity(details);
            }

        });
        row.addView(details_button);
    }

    @Override
    protected void onResume() {
        super.onResume();
        p.cancel(true);
        p = new PollForBattle(this);
        p.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("ONPAUSE");
        p.cancel(true);

    }

    private void addBlankButton(LinearLayout row)
    {
        ImageView details_button = new ImageView(this);

        Bitmap b = BitmapFactory.decodeResource(this.getResources(), R.drawable.white);
        b = Bitmap.createScaledBitmap(b, 50, 50, false);
        bdrawable = new BitmapDrawable(this.getResources(),Bitmap.createScaledBitmap(b, 50, 50, false));

        details_button.setBackground(bdrawable);
        details_button.setMaxWidth(width/itemsPerRow);
        details_button.setMinimumWidth(width/itemsPerRow);
        details_button.setMaxHeight((int) (width/itemsPerRow * ratio));
        details_button.setMinimumHeight((int) (width/itemsPerRow * ratio));

        row.addView(details_button);
    }

    private void addNewRow(LinearLayout sv, int row)
    {
        LinearLayout newRow = new LinearLayout(this);
        newRow.setWeightSum(itemsPerRow);
        newRow.setOrientation(LinearLayout.HORIZONTAL);
        for (int item = 1; item <= itemsPerRow; item ++)
        {
            int index = (row - 1) * itemsPerRow + item;
            addNewButton(newRow, index);
        }
        sv.addView(newRow);
    }


    public void addImages()
    {
        int numberOfRows = (int) Math.ceil( (double) mNumberOfEmployees / (double) itemsPerRow);
        System.out.println("NUMBER" + mNumberOfEmployees);
        System.out.println(numberOfRows);

        LinearLayout ll = (LinearLayout) findViewById(R.id.ll);


        for (int row = 1; row < numberOfRows; row ++)
        {
            addNewRow(ll, row);

        }
        LinearLayout newRow = new LinearLayout(this);
        newRow.setOrientation(LinearLayout.HORIZONTAL);
        int numberOfItemsInLastRow = mNumberOfEmployees % itemsPerRow;
        if (numberOfItemsInLastRow == 0)
        {
            numberOfItemsInLastRow = itemsPerRow;
        }
        for (int itemInLastRow = 1; itemInLastRow <= numberOfItemsInLastRow; itemInLastRow ++)
        {
            int index = itemInLastRow + (numberOfRows - 1) * itemsPerRow;
            System.out.println("INDEX" + index);

            addNewButton(newRow, index);
        }
        if (numberOfItemsInLastRow < itemsPerRow)
        {
            for (int remaining = 1; remaining <= (itemsPerRow - numberOfItemsInLastRow); remaining++)
            {
                addBlankButton(newRow);
            }

        }
        ll.addView(newRow);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public Bitmap decode(int index)
    {
        System.out.println(index);
        MetadexEntry newEntry = caughtEntries.get(index - 1);
        String base64 = newEntry.getmPhoto();
        System.out.println("ENTRY DETAILS" + newEntry.getmInitials());
        System.out.println("PHOTO DETAILS" + base64);
        byte[] data = Base64.decode(base64, Base64.DEFAULT);

        Bitmap b = BitmapFactory.decodeByteArray(data, 0 ,data.length);
        ratio = (float) b.getHeight()/ (float) b.getWidth();
        b = Bitmap.createScaledBitmap(b, 100, 100, false);

        return b;

    }

    @Override
    public void onBackPressed() {
        Intent back = new Intent(Metadex.this, HomeScreen.class);
        startActivity(back);
        onDestroy();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
