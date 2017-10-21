package edu.fullerton.teamobile.med;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


//add log out button
public class Homepage extends AppCompatActivity {

    SharedPreferences sharedPref;
    public Button addBtn, allBtn;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //check if logged in already
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loggedIn = sharedPref.getBoolean("loggedIn", false);
        if (loggedIn == false) {
            //go to main activity
            Intent home = new Intent(Homepage.this, MainActivity.class);
            toastMessage("Need to log in!");
            startActivity(home);
            finish();
        }
        else {
            //save username
            username = sharedPref.getString("username", "0");
        }

        initButtons();

    }

    /**
     * customizable toast
     * @param message
     */
    private void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    //initialize buttons to be clickable
    public void initButtons() {
        addBtn = (Button) findViewById(R.id.btnAddMed);
        allBtn = (Button) findViewById(R.id.btnAllMeds);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, AddMedPage.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });

        allBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intentMain = new Intent(Homepage.this, MedsTodayPage.class);
                //startActivity(intentMain);
            }
        });
    }
}
