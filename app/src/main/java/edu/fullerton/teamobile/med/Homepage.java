package edu.fullerton.teamobile.med;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Homepage extends AppCompatActivity {

    public Button addBtn, allBtn;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Intent mainPage = getIntent();
        username = mainPage.getStringExtra("username");

        initButtons();


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
