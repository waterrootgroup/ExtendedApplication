package com.example.waterrootapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import static android.support.constraint.Constraints.TAG;
/**@author: Nilay McLaren
 * @date: 5/15/19
 * @description: A page that appears the first time the app is opened on a mobile device and informs
 * the user how to set up their WaterRoot.
 */

public class SplashScreenActivity extends AppCompatActivity {
    /**
     * SharedPreferences is used to store data that can be accessed from different activities after
     * the app has been closed and reopened. This shared preference will be used to save the value
     * of the first time boolean.
     */
    public static SharedPreferences sharedPreferences;
    /**
     * True if it is the first time that the app is being opened on a device
     */
    public static boolean firstTime;
    /**
     * Boolean that is true if the splash screen switch is one and false if it is off
     */
    public static boolean switchon;
    /**
     * SharedPreferences is used to store data that can be accessed from different activities after
     * the app has been closed and reopened. This shared preference will be used to save the value
     * of the switchon boolean.
     */
    public SharedPreferences myPrefs;

    /**
     * Creates an instance of SplashScreenActivity
     * @param savedInstanceState is the activities previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        firstTime = sharedPreferences.getBoolean("firstTime", true);
        if (myPrefs.getBoolean("switchon", true) == true) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    firstTime = false;
                    editor.putBoolean("firstTime", firstTime);
                    editor.apply();
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
             }
            }, 10000);
        }
        else if (firstTime == true) {
           new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    firstTime = false;
                    editor.putBoolean("firstTime", firstTime);
                    editor.apply();
                    Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(i);
                    finish();
               }
           }, 100000);
        } else {
            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }


    }
    /**
     * Takes the user to the MainActivity page from the SplashScreenActivity page
     * @param v is the View object
     */
    public void onExit(View v) {
        Intent startNewActivity = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(startNewActivity);


    }
}
