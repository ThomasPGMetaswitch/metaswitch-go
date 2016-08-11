package com.example.metaswitchgo.metaswitchgo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;

public class CatchActivity extends Activity implements OnClickListener {

    private static final String TAG = "CL_FireworksActivity";
    CatchView mySurfaceView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.catch_layout);

            mySurfaceView = (CatchView) (findViewById(R.id.anim_view));
        } catch (Exception e) {
            Log.d(TAG, "Failed to create; " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {

    }
}