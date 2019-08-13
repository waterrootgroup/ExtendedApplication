package com.example.waterrootapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;




/**
 * @author: Daniel A
 * @date: 5/15/2019
 * @description: An activity that contains instructions for setting up and using the device
 */
public class Instructions extends AppCompatActivity {
    /**
     * Creates the Instructions activity
     * @param savedInstanceState is the previously saved state of the activity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instructions);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    /**
     * Takes the user to the AdditionalFeatures page from the Instructions page
     * @param v is the View object
     */
    public void onBack (View v){
        Intent startNewActivity = new Intent(Instructions.this,AdditionalFeatures.class);
        startActivity(startNewActivity);
    }

    /**
     * This is the onClick that opens up the youtube link for the instructions video
     * @param v is the View that calls this onClick method.
     */
    public void onYoutube(View v){
        openWebPage("http://youtube.com/");
    }
    /**
     * This is the onClick that opens up the google docs link for the instructions document.
     * @param v is the View that calls this onClick method.
     */
    public void onInstructionsDoc(View v){
        openWebPage("https://docs.google.com/document/d/1X2OnNBVfjvMif_iIgI9PVWuOj9sCzQ85PrF8zqQsdcI/edit?usp=sharing");
    }
    /**
     * Opens a browser and automatically opens the correct website
     * @param url is the url of the website that will be opened
     */
    //copied from https://developer.android.com/guide/components/intents-common#Browser then updated
    public void openWebPage(String url) {
        Uri webpage = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
    //end copied code

}
