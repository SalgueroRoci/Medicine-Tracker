package edu.fullerton.teamobile.med;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.net.Uri;


//add log out button
public class Homepage extends AppCompatActivity {

    SharedPreferences sharedPref;
    public Button addBtn, allBtn ,tdyBtn,outBtn, reportBtn;
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
    public void toastMessage(String message){
        Toast.makeText(this,message, Toast.LENGTH_SHORT).show();
    }

    //initialize buttons to be clickable
    public void initButtons() {
        addBtn = (Button) findViewById(R.id.btnAddMed);
        allBtn = (Button) findViewById(R.id.btnAllMeds);
        outBtn = (Button) findViewById(R.id.btnLogOut);
        tdyBtn = (Button) findViewById(R.id.btnToday);
        reportBtn = (Button) findViewById(R.id.btnReport);

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
                Intent intentMain = new Intent(Homepage.this, MedList.class);
                intentMain.putExtra("today", false);
                startActivity(intentMain);
            }
        });

        tdyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentMain = new Intent(Homepage.this, MedList.class);
                intentMain.putExtra("today", true);
                startActivity(intentMain);
            }
        });

        //log out
        outBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(Homepage.this);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
                Intent i = new Intent(Homepage.this,MainActivity.class);
                startActivity(i);
            }
        });

        reportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse("https://medicinetracker.000webhostapp.com/index.php"));
                startActivity(intent);
            }
        });
    }
}
