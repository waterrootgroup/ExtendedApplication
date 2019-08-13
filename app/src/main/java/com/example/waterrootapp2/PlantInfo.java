package com.example.waterrootapp2;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.waterrootapp2.R;

/**@author: Daniel Abadjiev
 * @date: 5/15/19
 * @description: This is an activity that displays two buttons which link to resources on plant information.
 */
public class PlantInfo extends AppCompatActivity {

    /**
     * Creates an instance of the PlantInfo activity
     * @param savedInstanceState is the activities previously saved state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_info);
    }

    /**
     * Takes the user to the AdditionalFeatures page from the PlantInfo page
     * @param v is the View object
     */

    public void onBack (View v){
        Intent startNewActivity = new Intent(PlantInfo.this,AdditionalFeatures.class);
        startActivity(startNewActivity);
    }

    /**
     * Opens google so the user may search for information on how to care for their plants effectively
     * @param v is the View object
     */
    public void onGoogle(View v){
        openWebPage("http://google.com/");
    }

    /**
     * Opens a website with tips for gardening and caring for plants
     * @param v is the View object
     */
    public void onSpruce(View v){
        openWebPage("https://www.thespruce.com/outdoors-and-gardening-4127780");
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
