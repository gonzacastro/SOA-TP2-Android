package com.example.tp2soa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private EditText inputUsername;
    private  EditText inputPassword;
    private Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        inputUsername = findViewById(R.id.EditTextUserName);
        inputPassword = findViewById(R.id.EditTextPassword);
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