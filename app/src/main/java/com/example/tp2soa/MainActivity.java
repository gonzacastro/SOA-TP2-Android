package com.example.tp2soa;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private EditText inputUsername;
    private  EditText inputPassword;
    private static Button loginButton;
//todas putas
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button loginButton = findViewById(R.id.loginButton);
        this.loginButton  = loginButton;
        inputUsername = findViewById(R.id.EditTextUserName);
        inputPassword = findViewById(R.id.EditTextPassword);
        Intent nombreSimpatico = getIntent();
        inputUsername.setText(nombreSimpatico.getExtras().getString("mail"));
        loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                String username = getInputUsername();
                String password = getInputPassword();
            }
        });
    }

    private  String getInputUsername(){
        return inputUsername.getText().toString();
    }

    public String getInputPassword(){
        return inputPassword.getText().toString();
    }

}