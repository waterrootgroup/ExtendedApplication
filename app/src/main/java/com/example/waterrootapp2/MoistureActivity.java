package com.example.waterrootapp2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.waterrootapp2.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
/**@author: Daniel Abadjiev
 * @date: 5/15/19
 * @description: This is an activity that display the information fom the MoistureLog in firebase. This information includes the date and time of water, the duration, and the reading for the moisture sensor.
 */
public class MoistureActivity extends AppCompatActivity {
    /**
     * This is a global variable to store the text that comes from the moisture log
     */
    public static String moistureLogText;
    /**
     * This is a global reference to the TextView that will be responsible for outputing text.
     */
    public static TextView textOutput;

    /**
     * Creates an instance of the MoistureActivity
     * @param savedInstanceState is the activities previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moisture);
        textOutput=findViewById(R.id.textOutput);
        printMoistureLog(textOutput);
        printMoistureLog(textOutput);
        TimerService.addRecentMoistureListener();

    }

    /**
     * Takes the user to the AdditionalFeatures page from the MoistureActivity page
     * @param v is the View object
     */
    public void onBack (View v){
        Intent startNewActivity = new Intent(MoistureActivity.this,AdditionalFeatures.class);
        startActivity(startNewActivity);
    }

    /**
     * Displays updated moisture readings from firebase
     * @param v is the View object
     */
    public void onRefresh(View v){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference().child(MainActivity.deviceName+"commands").child("moistureOn").setValue(1);
        printMoistureLog(textOutput);
    }

    /**
     * Displays Moisture Log entries from firebase
     * @param output is the TextView that will display the information
     */
    public static void printMoistureLog(TextView output){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference moistureLog = database.getReference(MainActivity.deviceName+"moistureLog");
        ValueEventListener postListener = new ValueEventListener() {
            /**
             *  This is the method that is called every time data are changed in this section of the
             *  database.
             *  It sets the text in the TextView output to the appropriate log
             * @param dataSnapshot is the DataSnapshot that contains the database entries whenever
             *                     they are changed
             */
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
//                waterLogText=dataSnapshot.getValue().toString();
                Iterator<DataSnapshot> iterator=dataSnapshot.getChildren().iterator();
                moistureLogText="You may need to click Refresh multiple times for current readings.\n\n";
                moistureLogText+="Recent Moisture: "+dataSnapshot.child("recentMoisture").getValue()+"";

                while (iterator.hasNext()) {
                    DataSnapshot entry = iterator.next();
                    //add the formatted string representing the entry as it's supposed to be
                    if (entry.getChildrenCount()!=1||entry.getKey().toString().equals("recentLog")){
                        //Maybe we'll do something here, probably not.
                    }
                    else{
                        Iterator<DataSnapshot> entryIta=entry.getChildren().iterator();
                        long moistureVal= ((long) entryIta.next().getValue());


                        moistureLogText+="\n"+entry.getKey().toString()+"\n\t \t Moisture: "+
                                moistureVal;


                    }
                }
                moistureLogText+="\nRecent Moisture: "+dataSnapshot.child("recentMoisture").getValue()+"";
                textOutput.setText(moistureLogText);
            }

            /**
             * This is the method to cancel the listener.
             * @param databaseError is the DatabseError that triggers this method
             */
            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        moistureLog.addValueEventListener(postListener);
    }
}
