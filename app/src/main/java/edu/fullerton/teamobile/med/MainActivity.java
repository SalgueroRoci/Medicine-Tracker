package edu.fullerton.teamobile.med;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//Main page of the application. First thing that pops up.
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

        description.setText("Online Mobile App that helps track of your medication/prescription!");

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
