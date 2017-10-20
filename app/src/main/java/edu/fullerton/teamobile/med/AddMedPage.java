package edu.fullerton.teamobile.med;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

//to do : add to server and check it works!
//make sure alarm goes off correctly.

public class AddMedPage extends AppCompatActivity {

    //Add medication button
    Button addMedbtn, addAlarm;

    ArrayAdapter<String> adapter;

    private ListView alarmsList;
    private TimePicker time;
    private EditText medName, medPerBot, medDose;
    private CheckBox sun, mon, tue, wed, thurs, fri, sat;
    private TextView test;

    private String medicine, perbot, dose, username, text, h , m, message;
    int [] days, intID;
    int numdays, alarms, maxAlarms, status;
    List<String> list;

    ProgressDialog pDialog;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med_page);

        test = (TextView) findViewById(R.id.txtTest);
        alarms = 0;
        maxAlarms = 10;
        days = new int[7];
        intID = new int[100];
        list = new ArrayList<String>();

        Intent mainPage = getIntent();
        username = mainPage.getStringExtra("username");

        addMedicationBtn();
    }//end on create

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    public void addMedicationBtn() {
        addMedbtn = (Button) findViewById(R.id.btnAddMed);
        addAlarm = (Button) findViewById(R.id.btnAddAlarm);
        alarmsList = (ListView) findViewById(R.id.lstAlarms);
        time = (TimePicker) findViewById(R.id.addTime);

        medName = (EditText) findViewById(R.id.etxtMedName);
        medPerBot = (EditText) findViewById(R.id.etxtPerBottle);
        medDose = (EditText) findViewById(R.id.etxtDose);

        mon = (CheckBox) findViewById(R.id.cbMon);
        tue = (CheckBox) findViewById(R.id.cbTues);
        wed = (CheckBox) findViewById(R.id.cbWeds);
        thurs = (CheckBox) findViewById(R.id.cbThurs);
        fri = (CheckBox) findViewById(R.id.cbFri);
        sat = (CheckBox) findViewById(R.id.cbSat);
        sun = (CheckBox) findViewById(R.id.cbSun);

        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, list);
        alarmsList.setAdapter(adapter);

        //add alarm values to list
        addAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //add new alarm
                h = String.valueOf(time.getCurrentHour() );
                m = String.valueOf(time.getCurrentMinute() );
                if(alarms  < maxAlarms && h.length() != 0 && m.length() != 0 && h.length() <= 2 && m.length() <= 2) {
                    alarms++; //update amount of alarms
                    //add to list
                    list.add(h+":"+m);
                    // this method will refresh your listview manually
                    adapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(alarmsList);
                }
                else if (alarms > 10){
                    toastMessage("Too many alarms!");
                }
                else {
                    toastMessage("Please fill out hour and minute.");
                }
            }
        });

        //delete when onclicked long press
        alarmsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> arg0, View v,
                                           int index, long arg3) {

                //Toast.makeText(AddMedPage.this,alarmsList.getItemAtPosition(index).toString(), Toast.LENGTH_LONG).show();
                alarms--; //update amount of alarms
                //add to list
                list.remove(index);
                // this method will refresh your listview manually
                adapter.notifyDataSetChanged();
                return false;
            }
        });

        //add all information
        addMedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the information of the form page
                medicine = medName.getText().toString();
                perbot = medPerBot.getText().toString();
                dose = medDose.getText().toString();

                //get days checked
                int i = 0 ;
                if(sun.isChecked())   { days[i] = 0 ; i++;}
                if(mon.isChecked())   { days[i] = 1 ; i++;}
                if(tue.isChecked())   { days[i] = 2 ; i++;}
                if(wed.isChecked())   { days[i] = 3 ; i++;}
                if(thurs.isChecked()) { days[i] = 4 ; i++;}
                if(fri.isChecked())   { days[i] = 5 ; i++;}
                if(sat.isChecked())   { days[i] = 6 ; i++;}
                numdays = i;

                //get intIDs
                for (i =0; i < list.size()*numdays ; i++) {
                    intID[i] = (int) System.currentTimeMillis() + i;
                }

                //check if filled out. If form filled add to database, create alarm
                if ( medicine.length() != 0 && perbot.length() != 0 && dose.length() != 0 && numdays != 0 && alarms != 0) {
                    new CreateNewMedication().execute();

                    //return to main menu
                    //finish();

                } else {
                    toastMessage("You must fill out form!");
                }

            }
        });


    } //end of add medication


    /**** Method for Setting the Height of the ListView dynamically.
     **** Hack to fix the issue of not showing all the items of the ListView
     **** when placed inside a ScrollView
     * Credit: https://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view ****/
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }


    /***************************************************************************************
     *    Title: AsyncTask - threads that execute in parallel
     *    Author: Vishal Thakkar
     *    Date: 2016
     *    Code version: 1.0
     *    Availability: https://drive.google.com/file/d/0B7KpqDyiTdk5NDhtWVFTTTIydTg/view
     *
     ***************************************************************************************/
    //background thread 3 running at the same time.
    class CreateNewMedication extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(AddMedPage.this);
            pDialog.setMessage("Adding Medication...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            try{
                // CALL send info to send information to website
                sendInfo();
                //create alarms
                createAlarms();
            }
            catch(final Exception ex)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Something went wrong. Please retry." + ex.toString());
                    }
                });
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }//end of create user thread


    // Send information to php files
    public  void  sendInfo() throws UnsupportedEncodingException, JSONException {
        JSONObject info = new JSONObject();

        info.put("perbottle", perbot);
        info.put("dosage", dose);
        info.put("medname", medicine);
        info.put("username", username);

        //array of schedules
        JSONArray schedule = new JSONArray();
        int k =0;
        for(int i =0; i < numdays; i++) {
            for(int j = 0; j < list.size(); j++) {
                JSONObject jo = new JSONObject();
                String[] values = list.get(j).split(":");
                jo.put("min", values[1]);
                jo.put("hour", values[0]);
                jo.put("day", days[i]);
                jo.put("intentID", intID[k]);
                schedule.put(jo);
                k++; // go from 0 to j * i
            }
        }

        info.put("schedule", schedule);
        //used to receive messages from server

        text = info.toString();
        //TO DELETE LATER !
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                test.setText(text);
            }
        });
        BufferedReader reader = null;

        // Send data
        try
        {
            // Defined URL  where to send data
            URL url = new URL("https://medicinetracker.000webhostapp.com/androidphp/addMed.php");

            // Send POST data request
            URLConnection conn = url.openConnection();
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

            //parse string to key value pairs.
            JSONObject json_data= new JSONObject(text);
            status = Integer.parseInt(json_data.getString("success"));
            message = json_data.getString("message");

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

    //create the alarms to go off on the phone
    //int k - amount of unique intent IDs; j - amount of alarms made; i - amount of days selected
    public void  createAlarms() {
        int k =0;
        for(int i =0; i < numdays; i++) {
           for(int j = 0; j < list.size(); j++) {
                //make the alarm active
                //function to create alarms and save info too
                calendar = Calendar.getInstance();

               //sunday = 0
               if(days[i] == 0)
                calendar.set(calendar.DAY_OF_WEEK,  calendar.SUNDAY);
               else if(days[i] == 1)
                   calendar.set(calendar.DAY_OF_WEEK,  calendar.MONDAY);
               else if(days[i] == 2)
                   calendar.set(calendar.DAY_OF_WEEK,  calendar.TUESDAY);
               else if(days[i] == 3)
                   calendar.set(calendar.DAY_OF_WEEK,  calendar.WEDNESDAY);
               else if(days[i] == 4)
                   calendar.set(calendar.DAY_OF_WEEK,  calendar.THURSDAY);
               else if(days[i] == 5)
                   calendar.set(calendar.DAY_OF_WEEK,  calendar.FRIDAY);
               else if(days[i] == 6)
                   calendar.set(calendar.DAY_OF_WEEK,  calendar.SATURDAY);

                String[] values = list.get(j).split(":");
                calendar.set(calendar.HOUR_OF_DAY, Integer.parseInt(values[0]) );
                calendar.set(calendar.MINUTE, Integer.parseInt(values[1]) );
                calendar.set(calendar.SECOND, 0);
                calendar.set(calendar.MILLISECOND, 0);

                //unique ID
                Intent intent = new Intent(AddMedPage.this, AlarmReceiver.class);
                //pass the intent id and save
                intent.putExtra("intentID", String.valueOf(intID[k]) );
                intent.putExtra("username", "hello worls");
                intent.putExtra("medName", medicine);
                PendingIntent appIntent = PendingIntent.getBroadcast(AddMedPage.this, intID[k], intent,PendingIntent.FLAG_ONE_SHOT);
                AlarmManager ALARM1 = (AlarmManager)getSystemService(ALARM_SERVICE);

                //repeat weekly
                ALARM1.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY*7, appIntent);

               Log.e("Alarm Made", "Day " + days[i] + " Time " + list.get(j));

               k++;
            }
        }
    }//end of create alarms

}//end of class ADD PAGE
