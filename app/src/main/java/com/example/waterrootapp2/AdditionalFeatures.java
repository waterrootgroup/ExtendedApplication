package com.example.waterrootapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.waterrootapp2.R;


/**@author: Nilay McLaren
 * @date: 5/15/19
 * @description: A page that contains links to the WaterLog, MoistureLog, PlantInfo Database, and Instructions page.
 */



public class AdditionalFeatures extends AppCompatActivity {

    /**
     * Creates an instance of the AdditionalFeatures activity
     * @param savedInstanceState is the activities previously saved state
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_features);
    }

    /**
     * Takes the user to the MainActivity page from the AdditionalFeatures page
     * @param v is the View object
     */
    public void onReturn (View v){
        Intent startNewActivity = new Intent(AdditionalFeatures.this,MainActivity.class);
        startActivity(startNewActivity);


    }

    /**
     * Takes the user to the WaterLogActivity2 page from the AdditionalFeatures page
     * @param v is the View object
     */
    public void onLog (View v){
        Intent startNewActivity = new Intent(AdditionalFeatures.this,WaterLogActivity2.class);
        startActivity(startNewActivity);


    }

    /**
     * Takes the user to the MoistureActivity page from the AdditionalFeatures page
     * @param v is the View object
     */
    public void onMoisture (View v){
        Intent startNewActivity = new Intent(AdditionalFeatures.this,MoistureActivity.class);
        startActivity(startNewActivity);


    }

    /**
     * Takes the user to the Instructions page from the AdditionalFeatures page
     * @param v is the View object
     */
    public void onInstructions (View v){
        Intent startNewActivity = new Intent(AdditionalFeatures.this,Instructions.class);
        startActivity(startNewActivity);
    }

    /**
     * Takes the user to the PlantInfo page from the AdditionalFeatures page
     * @param v is the View object
     */
    public void onPlantInfo (View v){
        Intent startNewActivity = new Intent(AdditionalFeatures.this,PlantInfo.class);
        startActivity(startNewActivity);
    }
}
