package edu.fullerton.teamobile.med;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class Login extends AppCompatActivity {

    SharedPreferences sharedPref; //saving log in state
    Button btnLogin;
    EditText txtUser, txtPassword;
    private ProgressDialog pDialog;

    String username, password, message, rtnUsername;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //used for savning log in state
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        //alreadyy logged in
        boolean loggedIn = sharedPref.getBoolean("loggedIn", false);
        if (loggedIn == true) {
            //go to main activity
            Intent home = new Intent(Login.this, Homepage.class);
            startActivity(home);
            finish();
        }
        //else
        login();
    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }


    private void login() {
        // Get information inputted
        txtUser =(EditText) findViewById(R.id.etxtUsername);
        txtPassword = (EditText) findViewById(R.id.etxtPassLogin);

        // Create button
        btnLogin = (Button) findViewById(R.id.btnLoginPage);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = txtUser.getText().toString();
                password = txtPassword.getText().toString();

                if( username.length() == 0 && password.length() == 0 ) {
                    toastMessage("Please fill out form!");
                }
                else {
                    // everything fine
                    // creating new user in background thread
                    new ServerLogin().execute(username, password);
                }
            }
        });

    } //end login

    /**
     * Background Async Task to Create new Patient
     * */
    class ServerLogin extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Login.this);
            pDialog.setMessage("Logging in...");
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
                sendLogin(args);
                // Show response on activity
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(status == 1) {
                            toastMessage("Successfully Logged in.");

                            //!!!!Save log in state on local phone!**/
                            //credit: https://stackoverflow.com/questions/26740185/android-login-registration-with-shared-preferences
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putBoolean("loggedIn", true);
                            editor.putString("username", rtnUsername);
                            editor.apply();

                            //successfully logged in
                            Intent i = new Intent(getApplicationContext(), Homepage.class);
                            //save state username
                            i.putExtra("username", rtnUsername);
                            startActivity(i);

                            finish();
                        }
                        else if(status == 0) {
                            //failed exit and redo
                            //display error
                            toastMessage(message);
                            txtPassword.setText("");
                        }

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


    //Send information to server
    public  void  sendLogin(String [] args)  throws UnsupportedEncodingException
    {
        String us, pas, text;
        // Get user defined values
        us = args[0];
        pas   = args[1];

        // Create data variable for sent values to server

        String data = URLEncoder.encode("username", "UTF-8")
                + "=" + URLEncoder.encode(us, "UTF-8");

        data += "&" + URLEncoder.encode("password", "UTF-8") + "="
                + URLEncoder.encode(pas, "UTF-8");

        text = "";
        BufferedReader reader=null;

        // Send data
        try
        {
            // Defined URL  where to send data
            URL url = new URL("https://medicinetracker.000webhostapp.com/androidphp/login.php");

            // Send POST data request
            URLConnection conn = url.openConnection();
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
            if(status == 1) {
                rtnUsername = json_data.getString("username");
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

