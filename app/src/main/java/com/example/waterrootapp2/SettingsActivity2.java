
package com.example.waterrootapp2;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.example.waterrootapp2.R;

import static android.support.constraint.Constraints.TAG;
import static com.example.waterrootapp2.SplashScreenActivity.sharedPreferences;
import static com.example.waterrootapp2.SplashScreenActivity.switchon;
import static com.example.waterrootapp2.TimerService.userTimer;
import static com.example.waterrootapp2.TimerService.userduration;
import static com.example.waterrootapp2.TimerService.userhour;
import static com.example.waterrootapp2.TimerService.userminute;
import static com.example.waterrootapp2.TimerService.usermonth;
import static com.example.waterrootapp2.TimerService.userday;
import static com.example.waterrootapp2.TimerService.useryear;

/**@author: Nilay McLaren
 * @date: 5/15/19
 * @description: A page that controls all of the information that the user is inputting into the application. This information is stored and sent to the other activities, firebase, and the timer service.
 */

public class SettingsActivity2 extends AppCompatActivity {
    /**
     * This is the name of the preferences file that is saved on the device
     */
    public static final String PREFS_NAME = "MyPrefsFile";
    /**
     * A shared preference that will be used to save the state of the switches and edit texts
     */
    public SharedPreferences myPrefs;
    /**
     *An editor that will be used to apply changes to a shared preference
     */
    public SharedPreferences.Editor editor2;


    /**
     * Creates an instance of the SettingsActivity
     * @param savedInstanceState is the activities previously saved state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings2);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        myPrefs = getSharedPreferences("prefID", Context.MODE_PRIVATE);
        editor2 = myPrefs.edit();
        Switch timerSwitch = (Switch) findViewById(R.id.switch1);
        SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, 0);
        boolean silent2 = settings2.getBoolean("timerkey", false);
        timerSwitch.setChecked(silent2);
        timerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Intent intent = new Intent(SettingsActivity2.this, TimerService.class);
                    startService(intent);
                } else if (isChecked == false) {
                    Intent intent2 = new Intent(SettingsActivity2.this, TimerService.class);
                    stopService(intent2);
                }
                SharedPreferences settings2 = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings2.edit();
                editor.putBoolean("timerkey", isChecked);
                editor.commit();
            }


        });


        Switch instructionsSwitch = (Switch) findViewById(R.id.switch2);
        if (instructionsSwitch.isChecked()) {
            switchon = true;
        } else {
            switchon = false;
        }
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        boolean silent = settings.getBoolean("switchkey", false);
        instructionsSwitch.setChecked(silent);
        instructionsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    editor2.putBoolean("switchon", true);
                    editor2.apply();
                }
                else {
                    editor2.putBoolean("switchon", false);
                    editor2.apply();
                }
                SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("switchkey", isChecked);
                editor.apply();
            }


        });
        final EditText timeOfWater = findViewById(R.id.editText);
        SharedPreferences settings4 = getSharedPreferences(PREFS_NAME, 0);
        String silent4 = settings.getString("timeofwaterkey", null);
        timeOfWater.setText(silent4);
        timeOfWater.addTextChangedListener(new TextChangedListener<EditText>(timeOfWater) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                String time = timeOfWater.getText().toString();
                Log.d(TAG, time);
                Intent timeIntent = new Intent(SettingsActivity2.this, TimerService.class);
                timeIntent.putExtra("timeofday", time);
                startService(timeIntent);
                SharedPreferences settings4 = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor4 = settings4.edit();
                editor4.putString("timeofwaterkey", time);
                editor4.apply();
            }
        });
        final EditText dateOfWater = findViewById(R.id.editText2);
        SharedPreferences settings5 = getSharedPreferences(PREFS_NAME, 0);
        String silent5 = settings.getString("dateofwaterkey", null);
        dateOfWater.setText(silent5);
        dateOfWater.addTextChangedListener(new TextChangedListener<EditText>(dateOfWater) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                String date = dateOfWater.getText().toString();
                Intent dateIntent = new Intent(SettingsActivity2.this, TimerService.class);
                dateIntent.putExtra("dateofwater", date);
                startService(dateIntent);
                SharedPreferences settings5 = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor5 = settings5.edit();
                editor5.putString("dateofwaterkey", date);
                editor5.apply();
            }
        });
        final EditText duration = findViewById(R.id.editText3);
        SharedPreferences settings6 = getSharedPreferences(PREFS_NAME, 0);
        String silent6 = settings6.getString("durationkey", null);
        duration.setText(silent6);
        duration.addTextChangedListener(new TextChangedListener<EditText>(duration) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                String durationstring = duration.getText().toString();
                Intent durationIntent = new Intent(SettingsActivity2.this, TimerService.class);
                durationIntent.putExtra("durationintentkey", durationstring);
                startService(durationIntent);
                SharedPreferences settings6 = getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor6 = settings6.edit();
                editor6.putString("durationkey", durationstring);
                editor6.apply();
            }
        });
    }

    /**
     * Takes the user to the MainActivity page from the SettingsActivity2 page
     * @param v is the View object
     */
    public void onReturn(View v) {
        Intent startNewActivity = new Intent(SettingsActivity2.this, MainActivity.class);
        startActivity(startNewActivity);
    }

    /**
     * This class is used to help with listening to changes to the text in the EditTexts
     * @param <T> the parameter this class is supposed to take in since it implements TextWatcher
     */
    public abstract class TextChangedListener<T> implements TextWatcher {
        private T target;
        public TextChangedListener(T target) {
            this.target = target;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
        @Override
        public void afterTextChanged(Editable s) {
            this.onTextChanged(target, s);
        }
        public abstract void onTextChanged(T target, Editable s);
    }

    /**
     * Saves changes to the settings. This method splits the date and time into there hour, minute, day, month, and year components and then automatically updates the values to the timer service so the user does not have to turn the timer on and off again. Additionally, if the data is not formatted correctly a message appears telling the user to reformat their information.
     * @param v is the view object
     */
    public void onSave(View v) {
        EditText timeOfWater = findViewById(R.id.editText);
        EditText dateofWater = findViewById(R.id.editText2);

        if (timeOfWater.getText().toString().contains(":") && checkUserDay()) {
            Toast.makeText(getApplicationContext(), "Changes Have Been Saved!", Toast.LENGTH_SHORT).show();
            String time = timeOfWater.getText().toString();
            String[] parts = time.split(":");
            String part1 = parts[0];
            userhour = Integer.parseInt(part1);
            String part2 = parts[1];
            userminute = Integer.parseInt(part2);
            String date = dateofWater.getText().toString();
            String[] parts2 = date.split("/");
            String part3 = parts2[0];
            usermonth = Integer.parseInt(part3);
            String part4 = parts2[1];
            userday = Integer.parseInt(part4);
            String part5 = parts2[2];
            useryear = Integer.parseInt(part5);
            EditText durationText = findViewById(R.id.editText3);
            userduration = 1000 * Integer.parseInt(durationText.getText().toString());
            userTimer = userhour + "/" + userminute + "/" + usermonth + "/" + userday + "/" + useryear;
        }

        else{
            Toast.makeText(getApplicationContext(),"Please Format Settings Correctly",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * Checks if the string the user types into the edit text contains two slashess
     * @return true if the '/' character is present twice in the string. This is a requirment for the date edit text
     */
    public boolean checkUserDay() {
        EditText dateofWater = findViewById(R.id.editText2);
        String s = dateofWater.getText().toString();
        String news = s.replaceAll("/", "");
        if (news.length() < s.length() - 1) {
            return true;
        }
        else {
            return false;
        }

    }

}










