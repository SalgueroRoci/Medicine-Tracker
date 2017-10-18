package edu.fullerton.teamobile.med;

import android.app.ProgressDialog;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
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
import java.net.URLEncoder;

public class AddMedPage extends AppCompatActivity {

    //Add medication button
    Button addMedbtn;

    private EditText medName, medPerBot, medDose;
    private CheckBox sun, mon, tue, wed, thurs, fri, sat;
    private TextView test;

    private String medicine, perbot, dose, username, text;
    int [] days, hour, min, intID; int numdays, alarms;

    ProgressDialog pDialog;

    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_med_page);

        test = (TextView) findViewById(R.id.txtTest);
        days = new int[7];
        hour = new int[10];
        min = new int[10];
        intID = new int[10];

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

        addMedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the information of the form page
                medicine = medName.getText().toString();
                perbot = medPerBot.getText().toString();
                dose = medDose.getText().toString();

                int i = 0 ;
                if(sun.isChecked())   { days[i] = 0 ; i++;}
                if(mon.isChecked())   { days[i] = 1 ; i++;}
                if(tue.isChecked())   { days[i] = 2 ; i++;}
                if(wed.isChecked())   { days[i] = 3 ; i++;}
                if(thurs.isChecked()) { days[i] = 4 ; i++;}
                if(fri.isChecked())   { days[i] = 5 ; i++;}
                if(sat.isChecked())   { days[i] = 6 ; i++;}
                numdays = i;

                //check if filled out. If form filled add to database, create alarm
                if ( medicine.length() != 0 && perbot.length() != 0 && dose.length() != 0 && numdays != 0 ) {
                    new CreateNewMedication().execute();


                    //return to main menu
                    //finish();

                } else {
                    toastMessage("You must fill out form!");
                }

            }
        });

    } //end of add medication


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
                // CALL GetText method to make post method call
                sendInfo();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        test.setText(text);
                    }
                });
            }
            catch(Exception ex)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Something went wrong. Please retry.");
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

        alarms = 1; hour[0] = 5; min[0] = 56; intID[0] = 9;
        //array of schedules
        JSONArray schedule = new JSONArray();
        for(int i =0; i < numdays; i++) {
            for(int j = 0; j < alarms; j++) {
                JSONObject jo = new JSONObject();
                jo.put("min", min[j]);
                jo.put("hour", hour[j]);
                jo.put("day", days[i]);
                jo.put("intentID", intID[j]);
                schedule.put(jo);
            }
        }

        info.put("schedule", schedule);
        //used to receive messages from server

        text = info.toString();
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
            wr.write( "" );
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

            //text = sb.toString();

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
