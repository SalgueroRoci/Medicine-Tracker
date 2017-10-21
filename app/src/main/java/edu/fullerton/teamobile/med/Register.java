package edu.fullerton.teamobile.med;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Rocio on 10/13/2017.
 */

public class Register extends AppCompatActivity {
    //variables used
    private ProgressDialog pDialog;

    EditText txtEmail, txtUser, txtPass, txtConPass;
    Button btnRegister;
    String email, username, password, confimpass;
    String em, us, pas, text, message;
    int status;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register();
    }

    private void register() {
        // Get information inputted
        txtEmail =(EditText)findViewById(R.id.etxtEmail);
        txtUser = (EditText) findViewById(R.id.etxtUser);
        txtPass = (EditText) findViewById(R.id.etxtPassword);
        txtConPass = (EditText) findViewById(R.id.etxtConPass);

        // Create button
        btnRegister = (Button) findViewById(R.id.btnRegister);

        // button click event
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                email = txtEmail.getText().toString();
                username = txtUser.getText().toString();
                password = txtPass.getText().toString();
                confimpass = txtConPass.getText().toString();

                if(email.length() == 0 && username.length() == 0 && password.length() == 0 && confimpass.length() == 0) {
                    //check if form filled out
                    toastMessage("Please fill out form!");
                }
                else if(!password.contentEquals(confimpass)) {
                    //check if passwords match
                    toastMessage("Passwords do not match!");
                    txtPass.setText("");
                    txtConPass.setText("");
                }
                else if(password.length() < 6 ) {
                    //check that password is more than 6 characters.
                    toastMessage("Password too short! Must be more than 6 characters!");
                    txtPass.setText("");
                    txtConPass.setText("");
                }
                else if(!isEmailValid(email)) {
                    //check if email is correct
                    toastMessage("Email address not valid!");
                    txtEmail.setText("");
                }
                else {
                    // everything fine
                    // creating new user in background thread
                    new CreateNewUser().execute(email, username, password);
                }
            }
        });//end of onclick listener

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
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
    class CreateNewUser extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Register.this);
            pDialog.setMessage("Registering...");
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
                sendInfo(args);


                // Show response on activity
                //unOnUiThread used to modify the UI from this thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(status == 1) {
                            toastMessage("Successfully Registered. Please log in.");
                            //successfully created product
                            Intent i = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(i);

                            finish();
                        }
                        else if(status == 0) {
                            //failed exit and redo
                            //display error
                            toastMessage(message);
                            txtPass.setText("");
                            txtConPass.setText("");
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


    // Create GetText Method
    public  void  sendInfo(String [] args)  throws UnsupportedEncodingException
    {
        // Get user defined values
        em = args[0]; //email
        us   = args[1]; //username
        pas   = args[2]; //password

        // Create data variable for sent values to server
        String data = URLEncoder.encode("email", "UTF-8")
                + "=" + URLEncoder.encode(em, "UTF-8");

        data += "&" + URLEncoder.encode("username", "UTF-8") + "="
                + URLEncoder.encode(us, "UTF-8");

        data += "&" + URLEncoder.encode("password", "UTF-8")
                + "=" + URLEncoder.encode(pas, "UTF-8");

        //used to receive messages from server
        text = "";
        BufferedReader reader = null;

        // Send data
        try
        {
            // Defined URL  where to send data
            URL url = new URL("https://medicinetracker.000webhostapp.com/androidphp/add_new_user.php");

            // Send POST data request
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
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

    //validate email syntax
    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}
