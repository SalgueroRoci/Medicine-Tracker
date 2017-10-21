package edu.fullerton.teamobile.med;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//loggedIn and username in shared preferences
public class MainActivity extends AppCompatActivity {

    Button btnLogin, btnRegister;
    TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnLoginPage);
        btnRegister = (Button) findViewById(R.id.btnRegisterPage);
        description = (TextView) findViewById(R.id.txtHomepage);

        //check if logged in already
        final SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean loggedIn = sharedPref.getBoolean("loggedIn", false);
        if (loggedIn == true) {
            //go to homepage
            Intent home = new Intent(MainActivity.this, Homepage.class);
            startActivity(home);
            finish();
        }
        //else
        onClick();
    }

    private void onClick() {
        //if login clicked
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(MainActivity.this, Login.class );
                startActivity(reg);
            }
        });

        //if Registration clicked
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reg = new Intent(MainActivity.this, Register.class );
                startActivity(reg);
            }
        });


    }
}
