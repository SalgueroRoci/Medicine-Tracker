package edu.fullerton.teamobile.med;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.app.Notification;

import java.util.Calendar;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Rocio on 9/19/2017.
 */

//fix second action bar in the future
// icons!!!!
public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        //intent id 0 = snooze Button. id = 2 for actual snooze alarm. id = 1 go to homepage button

        String medName = intent.getStringExtra("medName");
        String intentID = intent.getStringExtra("intentID");

        int id = Integer.parseInt(intentID);

        //do something as the intent goes off
        Log.e("onReceive", "Intent ID " + intentID + " " + medName);

        //Intent for Snooze button
        Intent btnSN = new Intent(context, AlarmReceiver.class);
        btnSN.putExtra("intentID", "0");
        btnSN.putExtra("medName", medName );
        PendingIntent btnSnooze = PendingIntent.getBroadcast(context, 0, btnSN, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notif = new NotificationCompat.Builder(context)
                .setDefaults(Notification.DEFAULT_ALL)
                .setContentTitle("Time to take medication!")
                .setContentText(medName)
                .setTicker("Test ticker")
                .addAction(R.mipmap.ic_launcher_round, "Snooze", btnSnooze)
                //.addAction(R.mipmap.ic_launcher_round, "Check", snooze)
                .setSmallIcon(R.mipmap.ic_launcher_round);

        //what sound going off
        notif.setDefaults(NotificationCompat.DEFAULT_SOUND);

        //stop notification once clicked
        notif.setAutoCancel(true);

        // Create pending intent, mention the Activity which needs to be
        //triggered when user clicks on notification
        //what to do when notification clicked
        Intent main = new Intent(context, Homepage.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 1, main, PendingIntent.FLAG_UPDATE_CURRENT);
        notif.setContentIntent(contentIntent);

        // Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        // Builds the notification and issues it.
        mNotifyMgr.notify(1, notif.build());

        //if snooze button pressed then create new snooze alarm in 5 minutes
        if(id == 0) {
            Log.e("onReceive", "Snooze");
            //cancel notification and let set in five minutes
            NotificationManager mNM = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            mNM.cancel(1);

            Intent actualSnooze = new Intent(context, AlarmReceiver.class);
            actualSnooze.putExtra("intentID", "2");
            actualSnooze.putExtra("medName", medName );
            PendingIntent snooze = PendingIntent.getBroadcast(context, 2, actualSnooze, PendingIntent.FLAG_UPDATE_CURRENT);
            //Snooze created for 5 minutes
            Calendar alarmCalendar = Calendar.getInstance();

            long currenttime = alarmCalendar.getTimeInMillis() + 300000; //5 minutes
            AlarmManager am = (AlarmManager) context.getSystemService((ALARM_SERVICE) );
            am.set(AlarmManager.RTC_WAKEUP, currenttime, snooze);
        }

    }

}

