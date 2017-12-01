package edu.fullerton.teamobile.med;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.Notification;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.Calendar;

import javax.net.ssl.HttpsURLConnection;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by Rocio on 9/19/2017.
 */

//fix second action bar in the future
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        //intent id 0 = snooze Button. id = 2 for actual snooze alarm. id = 1 go to homepage button

        String medName = intent.getStringExtra("medName");
        String intentID = intent.getStringExtra("intentID");

        int id = Integer.parseInt(intentID);

        //do something as the intent goes off
        Log.e("onReceive", "Intent ID " + intentID + " " + medName);

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

        //Intent for Snooze button
        Intent btnSN = new Intent(context, SnoozePage.class);
        btnSN.putExtra("intentID", "0");
        btnSN.putExtra("medName", medName );
        PendingIntent btnSnooze = PendingIntent.getActivity(context, 0, btnSN, 0);

        Intent btnTook = new Intent(context, AlarmReceiver.class);
        btnTook.putExtra("intentID", "3");
        btnTook.putExtra("medName", medName);
        PendingIntent btnTookMed = PendingIntent.getBroadcast(context, 0, btnTook, PendingIntent.FLAG_UPDATE_CURRENT);//mine

        NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Time to take medication!")
                .setContentText(medName)
                .setTicker("Test ticker")
                .addAction(R.drawable.ic_notifications_paused, "Snooze", btnSnooze)
                .addAction(R.drawable.ic_notifications_off, "Took Medication", btnTookMed)
                .setSmallIcon(R.mipmap.ic_launcher_round);

        //what sound going off
        //notif.setDefaults(NotificationCompat.DEFAULT_SOUND);
        notif.setSound(notification);
        //stop notification once clicked
        //notif.setAutoCancel(true);

        //set top priority so we will always see the buttons
        notif.setPriority(Notification.PRIORITY_MAX);

        Intent main = new Intent(context, Homepage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1, main, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.setContentIntent(contentIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);


        // Builds the notification and issues it.
        Notification notific = notif.build();
        notific.flags |= Notification.FLAG_INSISTENT;
        notific.sound = notification;
        notific.defaults = (Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE);
        mNotifyMgr.notify(1, notific);
        //mNotifyMgr.notify(1, notif.build());


        if(id == 3) {
            //decrement the amount of medication left by 1.
            Log.e("onReceive", "Took Medication");

            mNotifyMgr.cancel(1);
            //NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //mNM.cancel(1);

            //check if logged in already
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
            String username = "", medname;
            boolean loggedIn = sharedPref.getBoolean("loggedIn", false);
            if (!loggedIn) {
                //go to main activity
                //Intent home = new Intent(AddMedPage.this, MainActivity.class);
                //toastMessage("Need to log in!");
                // startActivity(home);
                // finish();
            }
            else {
                //save username
                username = sharedPref.getString("username", "0");
            }

            //send to server information
            medname = intent.getStringExtra("medName");
            Log.d("medname: ", medname);
            new UpdateMed().execute(medname, username);


        }


    }

    //background thread 3 running at the same time.
    public class UpdateMed extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {

        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            try{
                // CALL send info to send information to website
                sendInfo(args);
            }
            catch(final Exception ex)
            {

            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {

        }

    }

    // Send information to php files
    public  void  sendInfo(String [] args) throws JSONException {
        JSONObject info = new JSONObject();
        String med = args[0];
        String user = args[1];
        info.put("medname", med);
        info.put("username", user);

        String text = "";
        BufferedReader reader = null;

        // Send data
        try
        {
            URL url;
            // Defined URL  where to send data
            //if its an edit go to update med
            url = new URL("https://medicinetracker.000webhostapp.com/androidphp/medTaken.php");

            // Send POST data request
            HttpsURLConnection conn = (HttpsURLConnection ) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //write the information to php file
            wr.write( info.toString() );
            wr.flush();

            // Get the server response
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line + "\n");
            }

            text = sb.toString();
            //Log.e("Server", text);

        }
        catch(Exception ex)
        {

        }
        finally
        {
            try
            {
                reader.close();
            }
            catch(Exception ex) {}
        }

    }//end of send info
}
