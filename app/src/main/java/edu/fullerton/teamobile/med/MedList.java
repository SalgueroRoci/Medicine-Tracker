package edu.fullerton.teamobile.med;

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
import android.widget.ListView;
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
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
//to do make todays php
public class MedList extends AppCompatActivity {

    SharedPreferences sharedPref; //saving log in state
    TextView title;
    ListView medList;
    String text, username, message;
    int status;
    boolean today;

    ProgressDialog pDialog;
    ArrayAdapter<String> adapter;
    List<String> names, stage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_list);

        Intent get = getIntent();
        today = get.getBooleanExtra("today", false);

        title = (TextView) findViewById(R.id.txtTestt);
        medList = (ListView) findViewById(R.id.lstMeds);

        names = new ArrayList<String>();
        stage = new ArrayList<String>();


        //check if logged in already
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loggedIn = sharedPref.getBoolean("loggedIn", false);
        if (loggedIn == false) {
            //go to main activity
            Intent home = new Intent(MedList.this, MainActivity.class);
            toastMessage("Need to log in!");
            startActivity(home);
            finish();
        }
        else {
            //save username
            username = sharedPref.getString("username", "0");
        }

        new GetMeds().execute();

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    class GetMeds extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MedList.this);
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
                    adapter = new ArrayAdapter<String>
                            (MedList.this, android.R.layout.simple_list_item_1, names){
                        @Override
                        public View getView(int position, View convertView, ViewGroup parent){
                            // Get the current item from ListView
                            View view = super.getView(position,convertView,parent);
                            if(stage.get(position) == "false")
                            {
                            // Set a background color for ListView regular row/item
                                view.setBackgroundColor(Color.parseColor("#00000000"));
                            }
                            else
                            {
                            // Set the background color for alternate row/item
                                view.setBackgroundColor(Color.parseColor("#b5433f"));
                            }
                            return view;
                        }
                    };

                    medList.setAdapter(adapter);
                    medList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            String name = String.valueOf(medList.getItemAtPosition(position));
                            toastMessage(name);
                            Intent info = new Intent(MedList.this, MedInfo.class);
                            info.putExtra("medname", name);
                            startActivity(info);
                        }
                    });
                }
            });

        }

    }//end of create user thread


    //Send information to server
    public  void  getMeds(String [] args)  throws UnsupportedEncodingException
    {
        //get current day time
        Calendar calendar = Calendar.getInstance();
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; //0-6
        int m = calendar.get(Calendar.MINUTE);
        String min;
        if(m < 10)
            min = "0" + String.valueOf(m);
        else
            min = String.valueOf(m);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        // Create data variable for sent values to server
        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(username, "UTF-8");
        data += "&" + URLEncoder.encode("day", "UTF-8")
                + "=" + URLEncoder.encode(String.valueOf(dayOfWeek) , "UTF-8");
        data += "&" + URLEncoder.encode("hour", "UTF-8")
                + "=" + URLEncoder.encode(String.valueOf(hour) , "UTF-8");
        data += "&" + URLEncoder.encode("min", "UTF-8")
                + "=" + URLEncoder.encode(min , "UTF-8");

        //JSONObject info = new JSONObject();
        //info.put("username", username);
        //info.put("day", String.valueOf(dayOfWeek) );
        //info.put("hour", String.valueOf(hour) );
        //info.put("min", min );

        //used to receive messages from server
        text = "";
        BufferedReader reader = null;

        // Send data
        try
        {
            URL url;
            // Defined URL  where to send data
            if(!today) {
                url = new URL("https://medicinetracker.000webhostapp.com/androidphp/getAllMeds.php");
            }
            else {
                url = new URL("https://medicinetracker.000webhostapp.com/androidphp/getTodayMeds.php");
            }


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
            JSONArray json_array= new JSONArray(json_data.getString("medicines"));
            JSONObject extract = new JSONObject();

            status = Integer.parseInt(json_data.getString("success"));
            message = json_data.getString("message");
            if(status == 1) {
                for (int i = 0; i < json_array.length(); i++) {
                    extract = json_array.getJSONObject(i);
                    names.add(extract.getString("MedName"));
                    stage.add(extract.getString("stage"));
                }
            }

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
