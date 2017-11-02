package edu.fullerton.teamobile.med;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import javax.net.ssl.HttpsURLConnection;

public class MedInfo extends AppCompatActivity {
    SharedPreferences sharedPref;
    TextView title;
    Button btnEdit, btnDel;
    ProgressDialog pDialog;

    //TODO: add the parse values to these variables:
//TODO: medname , alarms, intentID, days, dose, amtleft, totalPills, overdose24, onetimeOver, warnings, ingredients, warnings, medInfo

    //text is to store all JSON response from server, message is to display 'error' or success message
    //status 1 for success, 0 for error in server
    String medname, username, text, message, medInfo, ingredients, warnings;
    int status, dose, amtleft, totalPills, overdose24, onetimeOver;
    List<String> alarms; //alarms will be a string of hh:mm ex. 2:30 or 12:05
    List<Integer> intentID, days; //days will have int 0-6 0 = sunday

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_info);

        //check if logged in already
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loggedIn = sharedPref.getBoolean("loggedIn", false);
        if (!loggedIn) {
            //go to main activity
            Intent home = new Intent(MedInfo.this, MainActivity.class);
            toastMessage("Need to log in!");
            startActivity(home);
            finish();
        }
        else {
            //save username
            username = sharedPref.getString("username", "0");
        }

        //Get med Name
        Intent get = getIntent();
        medname = get.getStringExtra("medname");

        //make arrays
        alarms = new ArrayList<String>();
        days = new ArrayList<Integer>();
        intentID = new ArrayList<Integer>();

        //bind all to a layout
        title = (TextView) findViewById(R.id.txtName);
        btnEdit = (Button) findViewById(R.id.btnEditMed);
        btnDel = (Button) findViewById(R.id.btnDelMed);

        //get medicine information and wait till finished
        try {
            new GetMed().execute().get(1000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DelMed().execute();
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: add edit button function add extras
                Intent edit = new Intent(MedInfo.this, AddMedPage.class);
                edit.putExtra("edit", true);
                startActivity(edit);
                finish();
            }
        });

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    class GetMed extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MedInfo.this);
            pDialog.setMessage("Getting Medication");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            try{
                // send login information
                getMeds(args);

            }
            catch(final Exception ex)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Something went wrong. Please retry." + ex );
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

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //TODO: ADD Info to UI page somehow
                    title.setText(text); //this is showing all json code unparsed
                }
            });

        }

    }//end of create user thread

    //Send information to server
    public  void  getMeds(String [] args)  throws UnsupportedEncodingException
    {

        // Create data variable for sent values to server
        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(username, "UTF-8");
        data += "&" + URLEncoder.encode("medname", "UTF-8")
                + "=" + URLEncoder.encode(medname , "UTF-8");

        //JSONObject info = new JSONObject();
        //info.put("username", username);
        //info.put("medname", medname );

        //used to receive messages from server
        text = "";
        BufferedReader reader = null;

        // Send data
        try
        {

            URL url = new URL("https://medicinetracker.000webhostapp.com/androidphp/getMedInfo.php");

            // Send POST data request
            HttpsURLConnection conn = (HttpsURLConnection ) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //write the information to php file
            wr.write( data );
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

            //TODO: parse string to key value pairs.


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

    }//end of get text

    class DelMed extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MedInfo.this);
            pDialog.setMessage("Getting Medication");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {

            try{
                // send login information
                delMed(args);

            }
            catch(final Exception ex)
            {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        toastMessage("Something went wrong. Please retry." + ex );
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

            //delete alarms
            for(int i = 0; i < intentID.size(); i++ ) {
                //delete alarms
                AlarmManager ALARM1 = (AlarmManager)getSystemService(ALARM_SERVICE);
                Intent intent = new Intent(MedInfo.this, AlarmReceiver.class);
                PendingIntent appIntent = PendingIntent.getBroadcast(MedInfo.this, intentID.get(i), intent,PendingIntent.FLAG_UPDATE_CURRENT);
                ALARM1.cancel(appIntent);
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    toastMessage("Medication Deleted. " + message);
                }
            });

            finish();

        }

    }//end of create user thread

    //Send information to server
    public  void  delMed(String [] args)  throws UnsupportedEncodingException
    {

        // Create data variable for sent values to server
        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(username, "UTF-8");
        data += "&" + URLEncoder.encode("medname", "UTF-8")
                + "=" + URLEncoder.encode(medname , "UTF-8");

        //JSONObject info = new JSONObject();
        //info.put("username", username);
        //info.put("medname", medname );

        //used to receive messages from server
        text = "";
        BufferedReader reader = null;

        // Send data
        try
        {

            URL url = new URL("https://medicinetracker.000webhostapp.com/androidphp/delMed.php");

            // Send POST data request
            HttpsURLConnection conn = (HttpsURLConnection ) url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            //write the information to php file
            wr.write( data );
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

    }//end of get text


}
