package com.example.waterrootapp2;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.support.constraint.Constraints.TAG;
import static com.example.waterrootapp2.MainActivity.wateredTodayEditor;
import static com.example.waterrootapp2.MainActivity.wateredTodayPref;

/**
 * @author: Nilay McLaren
 * @date: 5/15/19
 * @description: A background service that automatically changes a variable in firebase to water a
 * plant when the current time is the same as the time that the user typed in.
 */

public class TimerService extends Service {
    /**
     * This is a string which stores the current time
     */
    String current;
    /**
     * This stores the calendar object used throughout the class.
     */
    Calendar calendar;
    /**
     * The hour of the day (24 hour clock) that the user wishes to water the plant
     */
    public static int userhour;
    /**
     * The minute of the day that the user wishes to water the plant
     */
    public static int userminute;
    /**
     * The day of the month that the user wishes to water the plant
     */
    public static int userday;
    /**
     * The month of the year that the user wishes to water the plant
     */
    public static int usermonth;
    /**
     * The year that the user wishes to water the plant
     */
    public static int useryear;
    /**
     * The number of seconds the user wishes to water the plant for
     */
    public static int userduration;
    /**
     * The userhour, userminute, userday, usermonth, and useryear integers formatted as one string
     */
    public static String userTimer;
    /**
     * Creates an instance of the TimerService background service
     */
    @Override
    public void onCreate() {
        wateredTodayPref = getSharedPreferences("waterToday", MODE_PRIVATE);
        wateredTodayEditor = wateredTodayPref.edit();
        thread p = new thread();
        new Thread(p).start();
        addRecentMoistureListener();
        userTimer = userhour + "/" + userminute + "/" + usermonth + "/" + userday + "/" + useryear;
        super.onCreate();
    }

    /**
     * Destroys service and kills the thread that checks if the current date equals the user date
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        thread.interrupted();
    }

    /**
     * The onBind method returns null when the first client connects with the service. After one
     * client has called onBind, it will not be called again.
     * @param intent is the intent being passed to the method
     * @return
     */
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }



    /**
     * Waters the plant by changing the pumpOn variable in firebase and adds new information to the WaterLog
     */
    public void autoWater() {
        if (current.equals(userTimer)) {
            Log.d(TAG, "strings are equal");
            FirebaseDatabase database2 = FirebaseDatabase.getInstance();
            DatabaseReference commands2 = database2.getReference("commands");
            sendIntent();
            commands2.child("pumpOn").setValue(1);
            try {
                thread.sleep(userduration);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            commands2.child("pumpOn").setValue(0);
            DatabaseReference log = database2.getReference("waterLog");
            SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:ss");
            String time = getCurrentTime();
            log.child(time).child("watered").setValue(true);
            log.child(time).child("moisture").setValue(0);
            log.child(time).child("duration").setValue(userduration / 1000);
            log.child(time).child("manual or automatic").setValue("Automatic");
        }
    }

    /**
     * Gets the current time and returns it formatted in a single string
     * Used for algorithmic purposes, this format is not shown to the user.
     * @return the formatted string that has the current hour, minute, day, month, and year
     */
    public String getTime() {
        calendar = Calendar.getInstance();
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String stringday = Integer.toString(day);
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        int minutes = calendar.get(Calendar.MINUTE);
        String stringminute = Integer.toString(minutes);
        current = new String(hour + "/" + stringminute + "/" + month + "/" + stringday + "/" + year);
        return current;
    }

    /**
     * This class is for the thread that automatically waters the plant. It runs every 45 seconds
     * and checks to see if the current time equals the time the user submitted.
     */
    class thread extends Thread {
        /**
         * Runs the code to check the user date and current date forever until the timer is shut off.
         */
        @Override
        public void run() {
            getTime();
            while (true) {
                getTime();
                if (current.equals(userTimer)) {
                    autoWater();
                   userday++;

                    userTimer = userhour + "/" + userminute + "/" + usermonth + "/" + userday + "/" + useryear;

                }
                try {
                    sleep(45000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }




    /**
     * Sends an intent to other activities. The intent will allow the MainActivity text view to say
     * that the plant has been watered after the timer goes changes pumpOn.
     */
    public void sendIntent(){
        Intent local = new Intent();
        local.setAction("com.hello.action");
        this.sendBroadcast(local);
    }



    /**
     * This method returns a time string that used for tagging moisture
     * @return the formatted string that is used for logging moisture
     */
    public static String getTime2() {
        Calendar calendar3 = Calendar.getInstance();
        String year = Integer.toString(calendar3.get(Calendar.YEAR));
        String month = Integer.toString(calendar3.get(Calendar.MONTH) + 1);
        int day = calendar3.get(Calendar.DAY_OF_MONTH);
        String stringday = Integer.toString(day);
        String hour = Integer.toString(calendar3.get(Calendar.HOUR_OF_DAY));
        int minutes = calendar3.get(Calendar.MINUTE);
        String stringminute = Integer.toString(minutes);
        if (stringminute.length() == 1)
            stringminute = "0" + stringminute;
        return month + "\\" + stringday + "\\" + year + " at " + hour + ":" + stringminute;
    }

    /**
     * This method adds a listener to the recentMoisture and logs all moisture updates
     * The application has to do this because the arduino does not have access to a reliable clock,
     * so when the arduino pushes a moisture value to the database it does not have an accurate time
     * stamp for it.
     * This is the method that actually listens for moisture updates correctly.
     */
    public static void addRecentMoistureListener() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference moistureRec = database.getReference("moistureLog").child("recentMoisture");
//        final String timeString="Time: "+currentTime;
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get the most recent moisture
                try {
                    long moistureVal = (long) dataSnapshot.getValue();
                    moistureRec.getParent().child(getTime2()).child("moisture").setValue(moistureVal);
                    Log.d("inService", "I just added a child ");
                } catch (java.lang.ClassCastException e) {
                    Log.d("oops", "I don't think that's the right object type.");
                    moistureRec.getParent().child(getTime2()).child("moisture").setValue("Ummm moisture is a long??!");

                } catch (java.lang.NullPointerException e) {
                    Log.wtf("inService", "moisture database structure missing!!!");
                }
                moistureRec.getParent().child("recentLog").removeValue();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("firebase", "loadPost:onCancelled", databaseError.toException());
                // ...
            }
        };
        moistureRec.addValueEventListener(postListener);
    }


    /**
     * Return the current time and date. A 24 hour clock is used.
     * @return the current date and time in military time
     */
    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        String year = Integer.toString(calendar.get(Calendar.YEAR));
        String month = Integer.toString(calendar.get(Calendar.MONTH) + 1);
        String day = Integer.toString(calendar.get(Calendar.DAY_OF_MONTH));
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        String second = Integer.toString(calendar.get(Calendar.SECOND));
        String current = "Time: " + hour + ":" + minute +":" + second + "," + " on " + day + "\\" + month + "\\" + year;
        return current;
    }




}


