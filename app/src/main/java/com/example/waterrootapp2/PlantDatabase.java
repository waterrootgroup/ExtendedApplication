package com.example.waterrootapp2;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.service.autofill.CharSequenceTransformation;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlantDatabase extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_database);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText searchBar=findViewById(R.id.searchBar);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String search= s.toString();
//                Log.d("helpful hint",s.toString());
                updateResults(search);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }
    public void plantPopup(View v, String plantName){
        Log.d("plant name",plantName+"_text");
        String testDescription="No description, no link";//Multiple variables are used to get around the fact that description has to be final
        try {
            testDescription=getResources().getString(getResources().getIdentifier(plantName+"_text","string",getApplicationContext().getPackageName()));//Get description
        }
        catch(Exception e){
            e.printStackTrace();
        }
        final String description=testDescription;
        //Starting code from https://developer.android.com/guide/topics/ui/dialogs
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage(description)
                .setTitle(plantName);
        // Add the buttons
        builder.setPositiveButton("Open Original", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String url=lastLine(description).trim();
                Log.d("opening url",url);
                openWebPage(url);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        // Set other dialog properties


        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        //End copied code

        dialog.show();
        Log.d("Plant popup",plantName+" should have popuped");
    }

    public void updateResults(String search){
        clearChildren((LinearLayout) findViewById(R.id.plantItems));
        search=search.toLowerCase();
//            addPlant("test plant","plant description yayhtathlsadjfskldjf");
//            addPlant("test plant2","plant descasdfjlasdkfjk2222dfads222ription yayhtathlsadjfskldjf");
        InputStream io=getResources().openRawResource(R.raw.plants_with_blurbs);
        BufferedReader reader = new BufferedReader(new InputStreamReader(io));
        if (search.equals("")) {
            //clear all restrictions on results
            Log.d("helpful hint","nothing was searched");
            Toast.makeText(this, "implementation not finished, nothing was searched",Toast.LENGTH_SHORT);
            //add all the plants
            String line="";
            try {
                while ((line=reader.readLine()) !=null) {
                    //split by ','
                    String[] fields = line.split(",");
                    addPlant(fields[0],fields[1]);
                }
            }
            catch(IOException e){
                Log.wtf("MainActivity","ERROR reading data on line "+line);
            }
        }
        else {

            Log.d("helpful hint",search+" was searched");
            Toast.makeText(this, "implementation not finished, "+search+" was searched",Toast.LENGTH_SHORT);

            //add the plants that contain search term in their name
            String line="";
            try {
                while ((line=reader.readLine()) !=null) {
                    //split by ','
                    String[] fields = line.split(",");
                    ///Note: Could probably rewrite code to handle "" search term now, and would be less chunky
                    if (fields[0].indexOf(search)>-1)
                        addPlant(fields[0],fields[1]);
                }
            }
            catch(IOException e){
                Log.wtf("MainActivity","ERROR reading data on line "+line);
            }
        }


    }
    public void clearChildren(ViewGroup v){
        v.removeAllViews();
    }
    public void addPlant(final String plantName, String content){
        LinearLayout plantItems=findViewById(R.id.plantItems);
        TableRow plantItem=new TableRow(getApplicationContext());
        plantItems.addView(plantItem);
        TextView plantLabel=new TextView(this);
        plantLabel.setText(plantName);
        TextView plantBlurb=new TextView(this);
        plantBlurb.setText(content);
        boolean noImage;
        ImageButton plantImage=new ImageButton(this);
        try{
            //method of accessing drawable by name from
            //https://stackoverflow.com/questions/16369814/how-to-access-the-drawable-resources-by-name-in-android
            //may have to change because of apis??????????????
            plantImage.setImageDrawable(getDrawable(getApplicationContext().getResources().getIdentifier(plantName,"drawable",getApplicationContext().getPackageName())));

            noImage=false;
        }
        catch (Exception e){
            Log.wtf("Plant Database","No picture file, check naming");
            e.printStackTrace();
            noImage=true;
        }
        if (!noImage) {
            //Resize image, copied code from https://www.android-examples.com/change-imagebutton-image-width-height-in-android-programmatically/
//            RelativeLayout.LayoutParams layoutparams = plantImage.getLayoutParams();
//            layoutparams.height = 150;
//            layoutparams.width = 150;
//            plantImage.setLayoutParams(layoutparams);
            plantImage.setScaleType(ImageView.ScaleType.FIT_XY);
            //End copied code, turns out this was bad code
            plantImage.setAdjustViewBounds(true);
            plantImage.setMaxHeight(200);
            plantImage.setMaxWidth(200);



        }
        plantImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Plant popup",plantName+" should popup");
                plantPopup(v, plantName);
            }
        });
//        if (!noImage)
            plantItem.addView(plantImage);


        plantItem.addView(plantLabel);
        plantItem.addView(plantBlurb);
    }
    /**
     * Takes the user to the AdditionalFeatures page from the PlantInfo page
     * @param v is the View object
     */
    public void onBack (View v){
        Intent startNewActivity = new Intent(PlantDatabase.this,PlantInfo.class);
        startActivity(startNewActivity);
    }
    /**
     * Clears the search criteria
     * @param v is the View object
     */
    public void onClear(View v){
        //Need to do this implementation, should be as simple as just clearing the search box of text
        EditText searchBar=findViewById(R.id.searchBar);
        searchBar.setText("");
    }
    public static String lastLine(String s){
        //method from https://stackoverflow.com/questions/16393385/java-getting-last-linefastest-way-from-a-string-variable
        int lastIndex=s.lastIndexOf("\n");
        if (lastIndex+1>=s.length())
            return "";
        return s.substring(lastIndex+1);
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
