package edu.fullerton.teamobile.med;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Griff on 11/29/2017.
 */

public class SnoozePage extends AppCompatActivity {
    SharedPreferences sharedPref;


    //Add medication button
    Button Five, Ten, Fifteen, None;
    private String username, medName;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_snooze_page);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) this.getSystemService(this.NOTIFICATION_SERVICE);
        mNotifyMgr.cancel(1);

        //check if logged in already
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loggedIn = sharedPref.getBoolean("loggedIn", false);
        if (loggedIn == false) {
            //go to main activity
            Intent home = new Intent(SnoozePage.this, MainActivity.class);
            toastMessage("Need to log in!");
            startActivity(home);
            finish();
        } else {
            //save username
            username = sharedPref.getString("username", "0");
        }

        //Get med Name
        Intent get = getIntent();
        medName = get.getStringExtra("medname");
        SnoozeBtn();
    }//end on create

    /**
     * customizable toast
     *
     * @param message
     */
    private void toastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }


    public void SnoozeBtn() {
        Five = (Button) findViewById(R.id.but_five);
        Ten = (Button) findViewById(R.id.but_ten);
        Fifteen = (Button) findViewById(R.id.but_fifteen);
        None = (Button) findViewById(R.id.but_none);


        Five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 5 * 60 * 1000;

                Intent actualSnooze = new Intent(SnoozePage.this, AlarmReceiver.class);
                actualSnooze.putExtra("intentID", "5");
                actualSnooze.putExtra("medName", medName );
                PendingIntent snooze = PendingIntent.getBroadcast(SnoozePage.this, 2, actualSnooze, PendingIntent.FLAG_UPDATE_CURRENT);
                //Snooze created for 5 minutes
                Calendar alarmCalendar = Calendar.getInstance();

                long currenttime = alarmCalendar.getTimeInMillis() + time; //5 minutes
                AlarmManager am = (AlarmManager) getSystemService((ALARM_SERVICE) );
                am.set(AlarmManager.RTC_WAKEUP, currenttime, snooze);

                toastMessage("Snooze for " + time + " minutes.");
                finish();

            }

        });

        Ten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 10 * 60 * 1000;
                Intent actualSnooze = new Intent(SnoozePage.this, AlarmReceiver.class);
                actualSnooze.putExtra("intentID", "0");
                actualSnooze.putExtra("medName", medName );
                PendingIntent snooze = PendingIntent.getBroadcast(SnoozePage.this, 2, actualSnooze, PendingIntent.FLAG_UPDATE_CURRENT);
                //Snooze created for 5 minutes
                Calendar alarmCalendar = Calendar.getInstance();

                long currenttime = alarmCalendar.getTimeInMillis() + time; //5 minutes
                AlarmManager am = (AlarmManager) getSystemService((ALARM_SERVICE) );
                am.set(AlarmManager.RTC_WAKEUP, currenttime, snooze);

                toastMessage("Snooze for " + time + " minutes.");
                finish();

            }

        });

        Fifteen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 15 * 60 * 1000;

                Intent actualSnooze = new Intent(SnoozePage.this, AlarmReceiver.class);
                actualSnooze.putExtra("intentID", "0");
                actualSnooze.putExtra("medName", medName );
                PendingIntent snooze = PendingIntent.getBroadcast(SnoozePage.this, 2, actualSnooze, PendingIntent.FLAG_UPDATE_CURRENT);
                //Snooze created for 5 minutes
                Calendar alarmCalendar = Calendar.getInstance();

                long currenttime = alarmCalendar.getTimeInMillis() + time; //5 minutes
                AlarmManager am = (AlarmManager) getSystemService((ALARM_SERVICE) );
                am.set(AlarmManager.RTC_WAKEUP, currenttime, snooze);

                toastMessage("Snooze for " + time + " minutes.");
                finish();

            }

        });

        None.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage("Dismissed...");

                finish();
            }

        });
    }

}
